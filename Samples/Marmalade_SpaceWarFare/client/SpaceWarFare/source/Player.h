#ifndef __PLAYER_H__
#define __PLAYER_H__

#include "cocos2d.h"

USING_NS_CC;

class Player : public CCNode
{
	CCSprite *player;
	CCSpriteFrameCache *sprites;
	int direction;
	int speed;
	CCLabelTTF *label;
public:
	virtual bool init();
	void update(float dt, CCSize bounds);
	void setLabel(std::string text, float zoom);
	void run(int i);
	void stop();
	CCSprite *getSprite();
	CREATE_FUNC(Player);
};

#endif