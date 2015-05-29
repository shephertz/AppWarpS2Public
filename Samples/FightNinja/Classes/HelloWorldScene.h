#ifndef __HELLOWORLD_SCENE_H__
#define __HELLOWORLD_SCENE_H__

#include "cocos2d.h"
#include "Player.h"
#include "StartGameLayer.h"
#include "appwarp.h"


#define ROOM_ID             "390970905"


class HelloWorld : public cocos2d::LayerColor, public AppWarp::ConnectionRequestListener,public AppWarp::RoomRequestListener,public AppWarp::NotificationListener,public AppWarp::ZoneRequestListener
{
public:
    // there's no 'id' in cpp, so we recommend returning the class instance pointer
    static cocos2d::Scene* createScene();
    
    // Here's a difference. Method 'init' in cocos2d-x returns bool, instead of returning 'id' in cocos2d-iphone
    virtual bool init();
    
    // a selector callback
    void menuCloseCallback(cocos2d::Ref* pSender);
    
    // implement the "static create()" method manually
    CREATE_FUNC(HelloWorld);
    
    cocos2d::Array *_targets;
    cocos2d::Array *_projectiles;
    int _projectilesDestroyed;
    Player *player;
    Player *enemy;
    int score;
    bool isEnemyAdded;
    bool isConnected;
    std::string userName;
    bool isFirstLaunch;
    StartGameLayer *startGameLayer;
    
    // there's no 'id' in cpp, so we recommend to return the class instance pointer
    static cocos2d::Scene* scene();
    void showStartGameLayer();
    void removeStartGameLayer();
    void connectToAppWarp(cocos2d::Ref* pSender);
    // a selector callback
    void startGame();
    void pauseGame();
    void updateEnemyStatus(cocos2d::Point pos,float duration);
    void spriteMoveFinished(cocos2d::Node* pSender);
    void sendData(float x, float y, float duration);
    virtual void onTouchesEnded(const std::vector<cocos2d::Touch*>& touches, cocos2d::Event *event);
    virtual void update(float time);
    
    // preprocessor macro for "static create()" constructor ( node() deprecated )
    
    void onConnectDone(int res);
    void onJoinRoomDone(AppWarp::room revent);
    void onSubscribeRoomDone(AppWarp::room revent);
    void onChatReceived(AppWarp::chat chatevent);
    void onUserPaused(std::string user,std::string locId,bool isLobby);
    void onUserResumed(std::string user,std::string locId,bool isLobby);
    
    void scheduleRecover();
    void unscheduleRecover();
    void recover(float dt);
    void showReconnectingLayer(std::string message);
    
    
};

#endif // __HELLOWORLD_SCENE_H__
