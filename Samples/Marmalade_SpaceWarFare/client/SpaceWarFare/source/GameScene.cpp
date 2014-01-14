#include "GameScene.h"

USING_NS_CC;

CCScene* GameScene::scene()
{
    CCScene *scene = CCScene::create();
    GameScene *layer = GameScene::create();
    scene->addChild(layer);

    return scene;
}

bool GameScene::init()
{
    if ( !CCLayer::init() )
    {
        return false;
    }

	/*
	Create GameLayer and GameController and add them to Game
	*/

	gameLayer = GameLayer::create();
	controllerLayer = GameController::create();

	controllerLayer->SetGameLayer(gameLayer);
	gameLayer->setGameControllerLayer(controllerLayer);

	addChild(gameLayer, 0);
	addChild(controllerLayer, 1);

    return true;
}
