#ifndef __GameController_H__
#define __GameController_H__

#include "cocos2d.h"
#include "GameScene.h"

using namespace cocos2d;

class GameController : public cocos2d::CCLayer
{
	CCSprite *btnUp;
	CCSprite *btnDown;
	CCSprite *btnRight;
	CCSprite *btnLeft;
	CCLabelTTF *label;
	GameLayer *game;
public:
	void SetGameLayer(GameLayer *layer);
    virtual bool init();  
	virtual void ccTouchesEnded(cocos2d::CCSet* touches, cocos2d::CCEvent* event);
	virtual void ccTouchesBegan(cocos2d::CCSet* touches, cocos2d::CCEvent* event);
	void setLabel(std::string text);
    CREATE_FUNC(GameController);
};

#endif
