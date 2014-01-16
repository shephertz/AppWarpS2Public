#ifndef __MENU_H__
#define __MENU_H__

#include "cocos2d.h"

class Menu : public cocos2d::CCLayerColor
{
	
public:
    virtual bool init();  
    static cocos2d::CCScene* scene();
    CREATE_FUNC(Menu);
	void playBtnClicked(CCObject *sender);
};

#endif
