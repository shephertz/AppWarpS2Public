#ifndef __GAMESCENE_H__
#define __GAMESCENE_H__

#include "cocos2d.h"

class GameLayer;
class GameController;
class GameScene;

#include "GameLayer.h"
#include "GameController.h"

class GameScene : public cocos2d::CCLayer
{
	//GameLayer contains players, enemies and our map
	GameLayer *gameLayer;
	//GameController shows onscreen buttons and connection status
	GameController *controllerLayer;
public:
    virtual bool init();  
    static cocos2d::CCScene* scene();
    CREATE_FUNC(GameScene);
};

#endif
