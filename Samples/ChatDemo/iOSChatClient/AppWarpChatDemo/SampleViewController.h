//
//  SampleViewController.h
//  AppWarpChatDemo
//
//  Created by Nitin Gupta on 04/03/14.
//  Copyright (c) 2014 Nitin Gupta. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Constants.h"

@interface SampleViewController : UIViewController <UIAlertViewDelegate,UITableViewDataSource,UITableViewDelegate,UITextFieldDelegate,ConnectionRequestListener,ZoneRequestListener> {
    
    __weak IBOutlet UITextField *userNameTextField;
    __weak IBOutlet UIView *roomSelectionView;
    __weak IBOutlet UIView *rsHeaderView;
    __weak IBOutlet UILabel *rsHeaderLabel;
    __weak IBOutlet UITableView *rsListTableView;
    __weak IBOutlet UIButton *connectButton;
    __weak IBOutlet UILabel *userNameLabel;
    
    UIActivityIndicatorView *_indicator;
    
    NSMutableArray *_roomIDArray;
}

- (IBAction)joinAction:(id)sender;

@end
