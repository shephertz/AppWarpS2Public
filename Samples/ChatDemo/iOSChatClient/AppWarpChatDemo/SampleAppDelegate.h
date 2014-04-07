//
//  SampleAppDelegate.h
//  AppWarpChatDemo
//
//  Created by Nitin Gupta on 04/03/14.
//  Copyright (c) 2014 Nitin Gupta. All rights reserved.
//

#import <UIKit/UIKit.h>

@class Reachability;

@interface SampleAppDelegate : UIResponder <UIApplicationDelegate> {
    /*! Reachablity Related*/
    Reachability *_hostReachability;
    Reachability *_internetReachability;
    Reachability *_wifiReachability;
    
    BOOL isReachableAlertShown;
}

@property (strong, nonatomic) UIWindow *window;
@property (readonly, nonatomic) BOOL isReachable;

@end
