//
//  ChatViewController.h
//  AppWarpChatDemo
//
//  Created by Nitin Gupta on 05/03/14.
//  Copyright (c) 2014 Nitin Gupta. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Constants.h"

@interface ChatViewController : UIViewController<RoomRequestListener,ChatRequestListener,NotifyListener,UITextFieldDelegate> {
    NSMutableArray *_roomChatArray;
    __weak IBOutlet UITextField *chatTextField;
    __weak IBOutlet UITableView *chatTableView;
}

@property (strong, nonatomic) NSString *selectedRoomID;

- (IBAction)sendAction:(id)sender;

@end
