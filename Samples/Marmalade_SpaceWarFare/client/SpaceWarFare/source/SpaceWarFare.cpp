#include "s3e.h"
#include "IwDebug.h"

#include "cocos2d.h"
#include "AppDelegate.h"


// Main entry point for the application
int main()
{

	//Creating our App
    AppDelegate* app = new AppDelegate;
	//Let's run our app
    cocos2d::CCApplication::sharedApplication()->Run();

	return 0;
}
