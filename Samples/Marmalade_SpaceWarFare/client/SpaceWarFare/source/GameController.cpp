#include "GameController.h"

USING_NS_CC;

void GameController::SetGameLayer(GameLayer *layer)
{
	game = layer;
}

bool GameController::init()
{
    if ( !CCLayer::init() )
    {
        return false;
    }

	btnUp = CCSprite::create("button.png");
	btnDown = CCSprite::create("button.png");
	btnRight = CCSprite::create("button.png");
	btnLeft = CCSprite::create("button.png");

	btnUp->setPosition(CCPoint((24+24)*ZOOM2, (24+24+24)*ZOOM2));
	btnDown->setPosition(CCPoint((24+24)*ZOOM2, (24)*ZOOM2));
	btnRight->setPosition(CCPoint((24+24+24)*ZOOM2,(24+24)*ZOOM2));
	btnLeft->setPosition(CCPoint((24)*ZOOM2, (24+24)*ZOOM2));

	btnUp->setScale(ZOOM2);
	btnDown->setScale(ZOOM2);
	btnRight->setScale(ZOOM2);
	btnLeft->setScale(ZOOM2);

	btnUp->getTexture()->setAliasTexParameters();
	btnDown->getTexture()->setAliasTexParameters();
	btnRight->getTexture()->setAliasTexParameters();
	btnLeft->getTexture()->setAliasTexParameters();

	addChild(btnUp,0);
	addChild(btnDown,0);
	addChild(btnRight,0);
	addChild(btnLeft,0);

	this->setTouchEnabled(true);
	game = NULL;

	label = CCLabelTTF::create(" ", "CosmicSansNeueMono", 32, CCSizeMake(384, 32), kCCTextAlignmentLeft);
	ccColor3B color;
	color.r = color.g = color.b = 0;
	label->setColor(color);
	label->getTexture()->setAliasTexParameters();
	label->setPosition(CCPoint(192,CCDirector::sharedDirector()->getWinSize().height - 32));
	this->addChild(label);
    
    return true;
}

void GameController::ccTouchesEnded(cocos2d::CCSet* touches, cocos2d::CCEvent* event)
{
	CCTouch *touch = (CCTouch *)(touches->anyObject());
	CCPoint location = touch->getLocationInView();
	location = CCDirector::sharedDirector()->convertToGL(location);
	location = this->convertToNodeSpace(location);

	if(btnUp->boundingBox().containsPoint(location))
	{
		if(game != NULL)
			game->buttonUp(1);
	}
	else if(btnDown->boundingBox().containsPoint(location))
	{
		if(game != NULL)
			game->buttonUp(2);
	}
	else if(btnRight->boundingBox().containsPoint(location))
	{
		if(game != NULL)
			game->buttonUp(3);
	}
	else if(btnLeft->boundingBox().containsPoint(location))
	{
		if(game != NULL)
			game->buttonUp(4);
	}
	else
	{
		if(game != NULL)
			game->touch(location);
	}
}

void GameController::ccTouchesBegan(cocos2d::CCSet* touches, cocos2d::CCEvent* event)
{
	CCTouch *touch = (CCTouch *)(touches->anyObject());
	CCPoint location = touch->getLocationInView();
	location = CCDirector::sharedDirector()->convertToGL(location);
	location = this->convertToNodeSpace(location);

	if(btnUp->boundingBox().containsPoint(location))
	{
		if(game != NULL)
			game->buttonDown(1);
	}
	else if(btnDown->boundingBox().containsPoint(location))
	{
		if(game != NULL)
			game->buttonDown(2);
	}
	else if(btnRight->boundingBox().containsPoint(location))
	{
		if(game != NULL)
			game->buttonDown(3);
	}
	else if(btnLeft->boundingBox().containsPoint(location))
	{
		if(game != NULL)
			game->buttonDown(4);
	}
}

void GameController::setLabel(std::string text)
{
	label->setString(text.c_str());
	label->getTexture()->setAliasTexParameters();
}
