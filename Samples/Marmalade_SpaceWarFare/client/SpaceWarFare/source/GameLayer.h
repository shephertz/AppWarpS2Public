#ifndef __GAMELAYER_H__
#define __GAMELAYER_H__

#include "cocos2d.h"
#include "GameListener.h"
#include "Enemy.h"
#include "Player.h"
#include "GameScene.h"

using namespace cocos2d;

const float ZOOM = 4.0;
const float ZOOM2 = 3.0; 

//Location of your server
const std::string IP = "127.0.0.1";
//AppKey of the Zone. Zone should be created before starting the game. Use AdminDashboard to create a zone
const std::string API_KEY = "49130234-abf8-402b-b";
//Room where game will be player. Room too can be create with AdminDashboard
const std::string ROOM = "91586126";

/*
GameLayer derives from CCLayer along with some AppWarpS2 Listener Classes. 

ConnectionRequestListener - Contains callbacks which will be invoked when a response from the server is received for Connect and Disconnect APIs.
RoomRequestListener - Contains callbacks which will be invoked when a response from the server is received for requests pertaining to a room.
NotificationListener - Contains callbacks which will be invoked when a notification is received from the server from any subscribed location (room or lobby).

For details go here - http://appwarp.shephertz.com/game-development-center/marmalade-game-developers-home/marmalade-client-listener/
*/
class GameLayer : public cocos2d::CCLayer, public AppWarp::ConnectionRequestListener, public AppWarp::RoomRequestListener, public AppWarp::NotificationListener
{
	//TiledMap
	CCTMXTiledMap *map;
	//Cache for storing sprites
	CCSpriteFrameCache *sprites;
	//Player
	Player *player;
	//Enemy
	Enemy *enemy;
	int bulletSpeed;
	int state;
	float updateTime;
	std::string name;
	//WarpClient to communicate with AppWarpS2 server
	AppWarp::Client *warpClient;
	//GameController
	GameController *controls;
	//Other Remote players
	std::map<std::string,Player*> remoteClients;
	bool firstUpdate;
public:
    virtual bool init();  
	virtual void update(float dt);
    CREATE_FUNC(GameLayer);

	void setGameControllerLayer(GameController *layer);

	void buttonDown(int num);
	void buttonUp(int num);
	void touch(CCPoint loc);
	void bulletReached(CCNode* sender);

	//AppWarpS2 Events
	//Invoked in response to a connect request. 
	void onConnectDone(int res);
	//Invoked in response to a joinRoom request. 
	void onJoinRoomDone(AppWarp::room revent);
	//Invoked in response to a InvokeRPC request. 
	void onSendRPCDone(AppWarp::RPCResult result);
	//Invoked when a joined user sends a chat. Room subscribers will receive this. 
	void onChatReceived(AppWarp::chat chatevent);
	//Invoked when a joined user lefts the room. Room subscribers will receive this. 
	void onUserLeftRoom(AppWarp::room rData, std::string user);
};

#endif
