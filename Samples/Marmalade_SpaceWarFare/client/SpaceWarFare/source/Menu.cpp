#include "Menu.h"
#include "GameScene.h"

USING_NS_CC;

CCScene* Menu::scene()
{
    CCScene *scene = CCScene::create();
    Menu *layer = Menu::create();
    scene->addChild(layer);

    return scene;
}

bool Menu::init()
{
	ccColor4B bg;
	bg.r = 44;
	bg.g = 62;
	bg.b = 80;
	bg.a = 255;
	if ( !CCLayerColor::initWithColor(bg) )
    {
        return false;
    }

	CCMenuItemImage *btn = CCMenuItemImage::create("play_btn.png","play_btn.png",this,menu_selector(Menu::playBtnClicked));
	btn->setScale(2.0);
	btn->setPosition(CCPoint(0, -1 * btn->getContentSize().height *3  ));
	CCMenu *menu = CCMenu::create(btn, NULL);
	this->addChild(menu);

	CCSprite *title = CCSprite::create("title.png");
	title->setPosition(CCPoint(CCDirector::sharedDirector()->getWinSize().width/2, CCDirector::sharedDirector()->getWinSize().height/2));
	title->setScale(2.0);
	title->getTexture()->setAliasTexParameters();
	this->addChild(title);

    return true;
}

void Menu::playBtnClicked(CCObject *sender)
{
	CCScene *game = GameScene::scene();
	CCDirector::sharedDirector()->replaceScene(CCTransitionFadeTR::create(1.0,game));
}
