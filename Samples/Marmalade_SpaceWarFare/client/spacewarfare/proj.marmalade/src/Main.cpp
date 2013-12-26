// Application main file.

#include "Main.h"
#include "../Classes/AppDelegate.h"

#include "cocos2d.h"
#include "IwGL.h"

USING_NS_CC;

int main()
{
	AppDelegate app;

	return cocos2d::CCApplication::sharedApplication()->Run();
}
