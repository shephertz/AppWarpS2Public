
#include "Player.h"

bool Player::init()
{
	/*if ( !CCNode::init() )
    {
        return false;
    }*/

	sprites = CCSpriteFrameCache::sharedSpriteFrameCache();
	//player = CCSprite::createWithSpriteFrameName("slice28_28.png");
	//player->getTexture()->setAliasTexParameters();
	player = CCSprite::create();
	this->addChild(player);

	direction = 0;
	speed = 100;

	return true;
}

void Player::update(float dt, CCSize bounds)
{
	if(direction == 1 && this->getPositionY() < (bounds.height - this->getContentSize().height))
		this->setPosition(CCPoint(this->getPositionX(), this->getPositionY() + speed*dt));
	else if(direction == 2 && this->getPositionY() >( this->getContentSize().height)/2)
		this->setPosition(CCPoint(this->getPositionX(), this->getPositionY() - speed*dt));
	else if(direction == 3  && this->getPositionX() < (bounds.width - this->getContentSize().width))
		this->setPosition(CCPoint(this->getPositionX() + speed*dt, this->getPositionY()));
	else if(direction == 4 && this->getPositionX() > (this->getContentSize().width)/2)
		this->setPosition(CCPoint(this->getPositionX() - speed*dt, this->getPositionY()));
}

void Player::setLabel(std::string text, float zoom)
{
	label = CCLabelTTF::create(text.c_str(), "CosmicSansNeueMono", 16, CCSizeMake(28, 18), kCCTextAlignmentCenter);
	ccColor3B color;
	color.r = color.g = color.b = 0;
	label->setScale(zoom);
	label->setColor(color);
	label->getTexture()->setAliasTexParameters();
	label->setPosition(ccpAdd(player->getPosition(),CCPoint(0,18)));
	this->addChild(label);
}

void Player::run(int i)
{
	direction = i;
	
	if(direction == 1)
	{
		CCAnimation *animation = CCAnimation::create();
		animation->setDelayPerUnit(0.2f);
		animation->addSpriteFrame(sprites->spriteFrameByName("slice45_45.png"));
		animation->addSpriteFrame(sprites->spriteFrameByName("slice47_47.png"));
		animation->addSpriteFrame(sprites->spriteFrameByName("slice49_49.png"));
		animation->addSpriteFrame(sprites->spriteFrameByName("slice51_51.png"));
	
		player->runAction(CCRepeatForever::create(CCAnimate::create(animation)));
	}
	else if(direction == 2)
	{
		CCAnimation *animation = CCAnimation::create();
		animation->setDelayPerUnit(0.2f);
		animation->addSpriteFrame(sprites->spriteFrameByName("slice28_28.png"));
		animation->addSpriteFrame(sprites->spriteFrameByName("slice33_33.png"));
		animation->addSpriteFrame(sprites->spriteFrameByName("slice37_37.png"));
		animation->addSpriteFrame(sprites->spriteFrameByName("slice42_42.png"));
	
		player->runAction(CCRepeatForever::create(CCAnimate::create(animation)));
	}
	else if(direction == 3)
	{
		CCAnimation *animation = CCAnimation::create();
		animation->setDelayPerUnit(0.2f);
		animation->addSpriteFrame(sprites->spriteFrameByName("slice53_53.png"));
		animation->addSpriteFrame(sprites->spriteFrameByName("slice55_55.png"));
		animation->addSpriteFrame(sprites->spriteFrameByName("slice57_57.png"));
		animation->addSpriteFrame(sprites->spriteFrameByName("slice59_59.png"));
	
		player->runAction(CCRepeatForever::create(CCAnimate::create(animation)));
	}
	else if(direction == 4)
	{
		CCAnimation *animation = CCAnimation::create();
		animation->setDelayPerUnit(0.2f);
		animation->addSpriteFrame(sprites->spriteFrameByName("slice61_61.png"));
		animation->addSpriteFrame(sprites->spriteFrameByName("slice62_62.png"));
		animation->addSpriteFrame(sprites->spriteFrameByName("slice64_64.png"));
		animation->addSpriteFrame(sprites->spriteFrameByName("slice65_65.png"));
	
		player->runAction(CCRepeatForever::create(CCAnimate::create(animation)));
	}
}

void Player::stop()
{
	direction = 0;
	player->stopAllActions();
}

CCSprite *Player::getSprite()
{
	return player;
}