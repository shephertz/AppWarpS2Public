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

const std::string IP = "127.0.0.1";
const std::string API_KEY = "49130234-abf8-402b-b";
const std::string ROOM = "91586126";

class GameLayer : public cocos2d::CCLayer, public AppWarp::ConnectionRequestListener, public AppWarp::RoomRequestListener, public AppWarp::NotificationListener
{
	CCTMXTiledMap *map;
	CCSpriteFrameCache *sprites;
	Player *player;
	Enemy *enemy;
	int bulletSpeed;
	int state;
	CCPoint prevPos;
	std::string name;
	AppWarp::Client *warpClient;
	GameController *controls;
	std::map<std::string,Player*> remoteClients;
public:
    virtual bool init();  
	virtual void update(float dt);
    CREATE_FUNC(GameLayer);

	void setGameControllerLayer(GameController *layer);

	void buttonDown(int num);
	void buttonUp(int num);
	void touch(CCPoint loc);
	void bulletReached(CCNode* sender);

	void onConnectDone(int res);
	void onJoinRoomDone(AppWarp::room revent);
	void onSendRPCDone(AppWarp::RPCResult result);
	void onChatReceived(AppWarp::chat chatevent);
};

#endif
