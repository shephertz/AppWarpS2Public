#include "GameLayer.h"
#include "time.h"
#include "sstream"

USING_NS_CC;

bool GameLayer::init()
{
    if ( !CCLayer::init() )
    {
        return false;
    }

	sprites = CCSpriteFrameCache::sharedSpriteFrameCache();
	sprites->addSpriteFramesWithFile("sprites.plist");

	map = CCTMXTiledMap::create("map.tmx");
	map->setScale(ZOOM);
	addChild(map, 0);

	//Create the player
	player = Player::create();
	player->init();
	player->setPosition(CCPoint(CCDirector::sharedDirector()->getWinSize().width /2,100));
	player->setScale(ZOOM);
	player->setLabel("You",ZOOM/4);
	prevPos = player->getPosition();
	addChild(player, 1);
	player->run(2);

	enemy = NULL;
	controls = NULL;
	firstUpdate = true;

	bulletSpeed = 480;
	state = 0;

	//We need a unique name to connect to server
	//You can use facebook id or something like that, but to keep this simple, 
	//I am generating a random unmber based on time.
	std::stringstream ss;
	unsigned long int t = time(NULL);
	ss << t;
	name = ss.str();

	this->schedule(schedule_selector(GameLayer::update));
	this->runAction(CCFollow::create(player, map->boundingBox()));

	//Initialise the WarpClient with APIKey and Address of server
	AppWarp::Client::initialize(API_KEY, IP);
	//WarpClient is a singleton object
	warpClient = AppWarp::Client::getInstance();
	//Since we are using this class only to listen to warpclient, set listeners to 'this' object
	warpClient->setConnectionRequestListener(this);
	warpClient->setRoomRequestListener(this);
	warpClient->setNotificationListener(this);
	//Let's connect to server.
	//First parameter is name of player which should be unique
	//And second parameter is used for custom authentication, since we don't need it now, leave it blank
	warpClient->connect(name,"");

    return true;
}

void GameLayer::buttonDown(int num)
{
	player->run(num);
}

void GameLayer::buttonUp(int num)
{
	player->stop();
}

void GameLayer::update(float dt)
{
	warpClient->update();
	//s3eDebugTracePrintf("Updating %f",dt);
	player->update(dt, CCSize(map->getContentSize().width*ZOOM,map->getContentSize().height*ZOOM));

	//We send update about player only when he has moved 100 distance.
	//If we send chat messages at every update, there will numberous amount of traffic resulting in network failure
	float distance = sqrtf(pow(prevPos.x - player->getPositionX(), 2) + pow(prevPos.y - player->getPositionY(),2));
	if(distance >= 100)
	{
		//Create JSON message to be sent
		cJSON *json;
		json = cJSON_CreateObject();
		cJSON_AddNumberToObject(json, "type", 1); // Type of message = 1 i.e. moving
		cJSON_AddNumberToObject(json, "x", (int)player->getPositionX()); // X componenet of position vector
		cJSON_AddNumberToObject(json, "y", (int)player->getPositionY()); // Y componenet of position vector
		cJSON_AddStringToObject(json, "name", name.c_str()); // Name of player
		char* cRet = cJSON_PrintUnformatted(json);
		std::string message = cRet;
		free(cRet);
		//Send the message to all players in room
		warpClient->sendChat(message);
		prevPos = player->getPosition();
	}

	if(firstUpdate == true)
	{
		player->stop();
		firstUpdate = false;
	}

	/*for(std::map<std::string, Player*>::iterator p = remoteClients.begin(); p != remoteClients.end(); ++p)
	{
		p->second->update(dt, ccpMult(ccp(map->getContentSize().width,map->getContentSize().height), ZOOM));
	}*/
}

void GameLayer::touch(CCPoint loc)
{
	//A touch has occured.
	//Shot bullet in that direction
	CCPoint location = this->convertToNodeSpace(loc);
	CCSprite *bullet = CCSprite::create("shot.png");
	bullet->setPosition(player->getPosition());
	bullet->setScale(4.0);
	bullet->getTexture()->setAliasTexParameters();
	addChild(bullet);
	float distance = sqrtf(pow(location.x - player->getPositionX(), 2) + pow(location.y - player->getPositionY(),2));
	bullet->runAction(CCSequence::create(CCMoveTo::create(distance/bulletSpeed, location), CCCallFuncN::create(this, callfuncN_selector(GameLayer::bulletReached)),NULL));

	if(enemy != NULL)
	{
		//Check if bullet will collide with enemy
		CCRect *box = new CCRect(enemy->getPositionX(), enemy->getPositionY(), enemy->getSprite()->getContentSize().width*ZOOM, enemy->getSprite()->getContentSize().height*ZOOM);
		s3eDebugTracePrintf("%f, %f ,%f, %f",enemy->getPositionX(),enemy->getPositionY(), location.x,location.y);
		if(box->containsPoint(location))
		{
			//Bullet is going to hit the enemy
			//Let's send this information
			s3eDebugTracePrintf("Enemy Hit");
			//Create JSON message
			cJSON *json;
			json = cJSON_CreateObject();
			cJSON_AddNumberToObject(json, "type", 2); // Type = 2 i.e. someone has been hit
			cJSON_AddNumberToObject(json, "x", location.x);
			cJSON_AddNumberToObject(json, "y", location.y);
			cJSON_AddStringToObject(json, "p", "dragon"); // p=dragon i.e It's the dragon who has been hit with bullet
			char* cRet = cJSON_PrintUnformatted(json);
			std::string message = cRet;
			free(cRet);
			//Send chat message
			warpClient->sendChat(message);
		}
	}
}

void GameLayer::bulletReached(CCNode* sender)
{
  CCSprite *sprite = (CCSprite *)sender;
  this->removeChild(sprite, true);
}

void GameLayer::onConnectDone(int res)
{
	s3eDebugTracePrintf("onConnectDone %d", res);
	//If we have been successfully connected, then join the room

	if(controls != NULL)
	{
		std::stringstream ss;
		ss << "OnConnectDone : " << res;
		controls->setLabel(ss.str());
	}

	if(res == AppWarp::ResultCode::success)
		warpClient->joinRoom(ROOM);
}

void GameLayer::onJoinRoomDone(AppWarp::room revent)
{
	s3eDebugTracePrintf("onJoinRoomDone %d",revent.result);
	if(revent.result == AppWarp::ResultCode::success)
		state = 1;
}

void GameLayer::onSendRPCDone(AppWarp::RPCResult result)
{
	s3eDebugTracePrintf("onSendRPCDone %d",result.result);
}

void GameLayer::onChatReceived(AppWarp::chat chatevent)
{
	//A chat message has been recieved, let's parse it
	s3eDebugTracePrintf( "%s says : %s",chatevent.sender.c_str(),chatevent.chat.c_str());
	std::string name;
	int type=0, health=0,x=0,y=0;
	cJSON *json, *begPtr;
	json = cJSON_Parse(chatevent.chat.c_str());
	begPtr = json;
	json = json->child;
	while(json != NULL)
	{
		//extract info like name, x,y,type, health

		if(strcmp(json->string,"name") ==  0)
		{
			name = json->valuestring;
		}
		else if(strcmp(json->string,"type") ==  0)
		{
			type = json->valueint;
		}
		else if(strcmp(json->string,"x") ==  0)
		{
			x = json->valueint;
		}
		else if(strcmp(json->string,"y") ==  0)
		{
			y = json->valueint;
		}
		else if(strcmp(json->string,"health") ==  0)
		{
			health = json->valueint;
		}
		json = json->next;
	}
	cJSON_Delete(begPtr);

	if(chatevent.sender.compare("dragon") == 0)
	{
		//It's dragon who has sent the message
		//Remember dragon is an enemy that exits on server
		
		if(type == 1)
		{
			//type = 1 i.e. movement

			if(enemy == NULL)
			{
				//This is the first message about dragon, so lets create him first
				enemy = Enemy::create();
				enemy->init();
				enemy->setScale(ZOOM);
				enemy->setPosition((float)x, (float)y);
				enemy->setTarget((float)x, (float)y);
				enemy->setLabel(name,ZOOM/4);
				enemy->setHealth(health);
				this->addChild(enemy);
			}
			else
			{
				//Dragon is already present, update him with new position
				enemy->setTarget((float)x, (float)y);
				enemy->setHealth(health);
			}
		}
		else if(type == 3)
		{
			//type = 3 i.e dragon has been killed
			this->removeChild(enemy,true);
			enemy = NULL;
		}
	}
	else if(chatevent.sender.compare(this->name) != 0) 
	{
		//The sender is not the current player nor the dragon i.e. he is a remote player 
		Player *remote;
		std::map<std::string,Player*>::iterator p = remoteClients.find(chatevent.sender);

		if(p == remoteClients.end())
		{
			//If remote player does not exits, create him first
			remote = Player::create();
			remote->setScale(ZOOM);
			remote->setPosition(CCPoint(x,y));
			addChild(remote, 1);
			remoteClients.insert(std::pair<std::string, Player*>(chatevent.sender, remote));
		}
		else 
		{
			//remote already exits, simply update him with new position
			remote = p->second;
			remote->runAction(CCMoveTo::create(1.0,CCPoint(x,y)));
		}
	}
}

void GameLayer::setGameControllerLayer(GameController *layer)
{
	controls = layer;
}