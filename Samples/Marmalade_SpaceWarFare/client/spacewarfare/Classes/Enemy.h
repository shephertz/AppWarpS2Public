#ifndef __ENEMY_H__
#define __ENEMY_H__

#include "cocos2d.h"

USING_NS_CC;

class Enemy : public CCNode
{
	CCSprite *enemySprite;
	CCSpriteFrameCache *sprites;
	int state;
	int health;
	CCPoint target;
	CCLabelTTF *label;
	std::string name;
public:
	//static Enemy* create();
	virtual bool init();
	void setHealth(int i);
	void setLabel(std::string text);
	void run();
	void stop();
	void setTarget(float x, float y);
	CCSprite *getSprite();
	CCRect getBoundingBox();
	CREATE_FUNC(Enemy);
};

#endif