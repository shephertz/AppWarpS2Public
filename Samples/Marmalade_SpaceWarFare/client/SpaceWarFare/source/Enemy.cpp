
#include "Enemy.h"
#include "sstream"

/*Enemy *Enemy::create()
{
	Enemy *ret = new Enemy();
	ret->autorelease();
	return ret;
}*/

bool Enemy::init()
{
	/*if ( !CCNode::init() )
    {
        return false;
    }*/

	sprites = CCSpriteFrameCache::sharedSpriteFrameCache();
	enemySprite = CCSprite::createWithSpriteFrameName("slice01_01.png");
	//enemySprite->getTexture()->setAliasTexParameters();
	this->addChild(enemySprite);

	state = 0;
	target.x = target.y = 0.0;
	health = 0;

	return true;
}

void Enemy::run()
{
	state = 1;

	CCAnimation *animation = CCAnimation::create();
	animation->setDelayPerUnit(0.2f);
	animation->addSpriteFrame(sprites->spriteFrameByName("slice01_01.png"));
	animation->addSpriteFrame(sprites->spriteFrameByName("slice05_05.png"));
	animation->addSpriteFrame(sprites->spriteFrameByName("slice09_09.png"));
	
	enemySprite->runAction(CCRepeatForever::create(CCAnimate::create(animation)));
}

void Enemy::stop()
{
	enemySprite->stopAllActions();
}

void Enemy::setTarget(float x, float y)
{
	target.x = x;
	target.y = y;

	//float distance = sqrtf(pow(enemySprite->getPositionX() - target.x, 2) + pow(enemySprite->getPositionX() - target.y,2));
	this->runAction(CCMoveTo::create(1.0,target));
}

void Enemy::setLabel(std::string text)
{
	name = text;
	std::stringstream ss;
	ss << text << " " << health;
	label = CCLabelTTF::create(ss.str().c_str(), "CosmicSansNeueMono", 12, CCSizeMake(72, 24), kCCTextAlignmentCenter);
	ccColor3B color;
	color.r = color.g = color.b = 0;
	label->setColor(color);
	label->getTexture()->setAliasTexParameters();
	label->setPosition(ccpAdd(enemySprite->getPosition(),CCPoint(0,24)));
	this->addChild(label);
}

void Enemy::setHealth(int i)
{
	health = i;
	if(label != NULL)
	{
		std::stringstream ss;
		ss << name << " " << health;	
		label->setString(ss.str().c_str());
		label->getTexture()->setAliasTexParameters();
	}
}

CCSprite *Enemy::getSprite()
{
	return enemySprite;
}

CCRect Enemy::getBoundingBox()
{
	CCRect rect=enemySprite->boundingBox();
	CCPoint pos=this->convertToWorldSpace(rect.origin);
	CCRect enemyRect(pos.x,pos.y,rect.size.width,rect.size.height);
	return enemyRect;
}