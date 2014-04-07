//
//  SampleAppDelegate.m
//  AppWarpChatDemo
//
//  Created by Nitin Gupta on 04/03/14.
//  Copyright (c) 2014 Nitin Gupta. All rights reserved.
//

#import "SampleAppDelegate.h"
#import "Reachability.h"
#import "Constants.h"

@implementation SampleAppDelegate
@synthesize isReachable = _isReachable;

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    [self initializeReachability];
    // Override point for customization after application launch.
    return YES;
}
							
- (void)applicationWillResignActive:(UIApplication *)application
{
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

#pragma mark - Reachability Methods
- (void) initializeReachability {
    kFunctionLog;
    //Change the host name here to change the server you want to monitor.
    NSString *remoteHostName = HOST_URL;
    _hostReachability = [Reachability reachabilityWithHostName:remoteHostName];
    [_hostReachability startNotifier];
    [self updateInterfaceWithReachability:_hostReachability];
    
    _internetReachability = [Reachability reachabilityForInternetConnection];
    [_internetReachability startNotifier];
    [self updateInterfaceWithReachability:_internetReachability];
    
    _wifiReachability = [Reachability reachabilityForLocalWiFi];
    [_wifiReachability startNotifier];
    [self updateInterfaceWithReachability:_wifiReachability];
}

/*! Called by Reachability whenever status changes.*/
- (void) reachabilityChanged:(NSNotification *)note {
    kFunctionLog;
	Reachability* curReach = [note object];
	NSParameterAssert([curReach isKindOfClass:[Reachability class]]);
	[self updateInterfaceWithReachability:curReach];
}


- (void)updateInterfaceWithReachability:(Reachability *)reachability {
    kFunctionLog;
    NSString* statusText = nil;

    if (reachability == _hostReachability || reachability == _internetReachability || reachability == _wifiReachability) {
        NetworkStatus netStatus = [reachability currentReachabilityStatus];
        BOOL connectionRequired = [reachability connectionRequired];
        if (!netStatus && connectionRequired) {
            _isReachable = FALSE;
            statusText = @"Internet connection is unavailable. Please check for connect any come back here.";
        } else {
            _isReachable = TRUE;
        }
    }

    if (!_isReachable) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Connection Error" message:statusText delegate:nil cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        isReachableAlertShown = YES;
        alert = nil;
    } else {
        isReachableAlertShown = NO;
    }
}

@end
