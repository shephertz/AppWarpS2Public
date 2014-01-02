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

	player = Player::create();
	player->init();
	player->setPosition(CCPoint(CCDirector::sharedDirector()->getWinSize().width /2,100));
	player->setScale(ZOOM);
	player->setLabel("You");
	prevPos = player->getPosition();
	addChild(player, 1);

	enemy = NULL;
	controls = NULL;

	bulletSpeed = 480;
	state = 0;
	std::stringstream ss;
	unsigned long int t = time(NULL);
	ss << t;
	name = ss.str();

	this->schedule(schedule_selector(GameLayer::update));
	this->runAction(CCFollow::create(player, map->boundingBox()));

	AppWarp::Client::initialize(API_KEY, IP);
	warpClient = AppWarp::Client::getInstance();
	warpClient->setConnectionRequestListener(this);
	warpClient->setRoomRequestListener(this);
	warpClient->setNotificationListener(this);
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

	float distance = sqrtf(pow(prevPos.x - player->getPositionX(), 2) + pow(prevPos.y - player->getPositionY(),2));
	if(distance >= 100)
	{
		cJSON *json;
		json = cJSON_CreateObject();
		cJSON_AddNumberToObject(json, "type", 1);
		cJSON_AddNumberToObject(json, "x", (int)player->getPositionX());
		cJSON_AddNumberToObject(json, "y", (int)player->getPositionY());
		cJSON_AddStringToObject(json, "name", name.c_str());
		char* cRet = cJSON_PrintUnformatted(json);
		std::string message = cRet;
		free(cRet);
		warpClient->sendChat(message);
		prevPos = player->getPosition();
	}

	/*for(std::map<std::string, Player*>::iterator p = remoteClients.begin(); p != remoteClients.end(); ++p)
	{
		p->second->update(dt, ccpMult(ccp(map->getContentSize().width,map->getContentSize().height), ZOOM));
	}*/
}

void GameLayer::touch(CCPoint loc)
{
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
		CCRect *box = new CCRect(enemy->getPositionX(), enemy->getPositionY(), enemy->getSprite()->getContentSize().width*ZOOM, enemy->getSprite()->getContentSize().height*ZOOM);
		s3eDebugTracePrintf("%f, %f ,%f, %f",enemy->getPositionX(),enemy->getPositionY(), location.x,location.y);
		if(box->containsPoint(location))
		{
			s3eDebugTracePrintf("Enemy Hit");
			cJSON *json;
			json = cJSON_CreateObject();
			cJSON_AddNumberToObject(json, "type", 2);
			cJSON_AddNumberToObject(json, "x", location.x);
			cJSON_AddNumberToObject(json, "y", location.y);
			cJSON_AddStringToObject(json, "p", "dragon");
			char* cRet = cJSON_PrintUnformatted(json);
			std::string message = cRet;
			free(cRet);
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
	s3eDebugTracePrintf( "%s says : %s",chatevent.sender.c_str(),chatevent.chat.c_str());
	std::string name;
	int type=0, health=0,x=0,y=0;
	cJSON *json, *begPtr;
	json = cJSON_Parse(chatevent.chat.c_str());
	begPtr = json;
	json = json->child;
	while(json != NULL)
	{
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
		if(type == 1)
		{
			if(enemy == NULL)
			{
				enemy = Enemy::create();
				enemy->init();
				enemy->setScale(ZOOM);
				enemy->setPosition((float)x, (float)y);
				enemy->setTarget((float)x, (float)y);
				enemy->setLabel(name);
				enemy->setHealth(health);
				this->addChild(enemy);
			}
			else
			{
				enemy->setTarget((float)x, (float)y);
				enemy->setHealth(health);
			}
		}
		else if(type == 3)
		{
			this->removeChild(enemy,true);
			enemy = NULL;
		}
	}
	else if(chatevent.sender.compare(this->name) != 0) 
	{
		Player *remote;
		std::map<std::string,Player*>::iterator p = remoteClients.find(chatevent.sender);

		if(p == remoteClients.end())
		{
			remote = Player::create();
			remote->setScale(ZOOM);
			remote->setPosition(CCPoint(x,y));
			addChild(remote, 1);
			remoteClients.insert(std::pair<std::string, Player*>(chatevent.sender, remote));
		}
		else 
		{
			remote = p->second;
			remote->runAction(CCMoveTo::create(1.0,CCPoint(x,y)));
		}
	}
}

void GameLayer::setGameControllerLayer(GameController *layer)
{
	controls = layer;
}