#ifndef  _APP_DELEGATE_H_
#define  _APP_DELEGATE_H_

#include "cocos2d.h"

class  AppDelegate : private cocos2d::CCApplication
{
public:
    AppDelegate();
    virtual ~AppDelegate();
	//Trigegrs when application has launched
    virtual bool applicationDidFinishLaunching();
	//Trigger when application goes background
    virtual void applicationDidEnterBackground();
	//Trigger when applications comes foreground from background
    virtual void applicationWillEnterForeground();
};

#endif

