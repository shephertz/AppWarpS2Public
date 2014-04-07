//
//  SampleViewController.m
//  AppWarpChatDemo
//
//  Created by Nitin Gupta on 04/03/14.
//  Copyright (c) 2014 Nitin Gupta. All rights reserved.
//

#import "SampleViewController.h"
#import "ChatViewController.h"

@interface SampleViewController ()

@end

@implementation SampleViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self initializeAppWarp];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)dealloc {
    _roomIDArray = nil;
}

- (void) prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([segue.identifier isEqualToString:@"ChatViewSegue"]) {
        ChatViewController *chatView = (ChatViewController *)[segue destinationViewController];
        chatView.selectedRoomID = [_roomIDArray objectAtIndex:[[rsListTableView indexPathForSelectedRow] row]];
        [[self navigationItem] setTitle:@"Disconnect"];
    }
}

- (void) viewWillAppear:(BOOL)animated {
    
    WarpClient *wrCl = [WarpClient getInstance];
    [wrCl disconnect];
    
    [self updateViewForLaunch];
    	// Do any additional setup after loading the view, typically from a nib.
}

#pragma mark  Life Cycle
- (void) updateViewForLaunch {
    kFunctionLog;
    [[roomSelectionView layer] setCornerRadius:5.0f];
    [[rsHeaderView layer] setCornerRadius:1.0f];
    [[rsListTableView layer] setCornerRadius:1.0f];
    [userNameTextField setText:@""];
    [[self navigationItem] setTitle:@"Connect"];

    [self hideSelectionView];
    [self showConnectView];
}

- (void) showSelectionViewAnimated:(BOOL)_isAnim {
    [roomSelectionView setTransform:CGAffineTransformMakeScale(0.1, 0.1)];
    if (_isAnim) {
        [UIView animateKeyframesWithDuration:0.1 delay:0.0f options:UIViewKeyframeAnimationOptionCalculationModeLinear animations:^{
            [roomSelectionView setHidden:NO];
            [roomSelectionView setTransform:CGAffineTransformIdentity];
        } completion:^(BOOL finished) {
        }];
    } else {
        [roomSelectionView setHidden:NO];
    }
}

- (void) hideSelectionView {
    [roomSelectionView setHidden:YES];
}


-(void)initializeAppWarp {
    kFunctionLog;
    NSLog(@"%@,%@",apiKey,server);
    [WarpClient initWarp:apiKey server:server];
    WarpClient *wrCl = [WarpClient getInstance];
    [wrCl  addConnectionRequestListener:self];
    [wrCl  addZoneRequestListener:self];
}

- (void) connectWithUser:(NSString *)_aUserName {
    kFunctionLog;
    WarpClient *wrCl = [WarpClient getInstance];
    [wrCl connectWithUserName:_aUserName authData:@""];
}

- (void) getAllRoomsInfo {
    WarpClient *wrCl = [WarpClient getInstance];
    [wrCl getAllRooms];
}

- (void) showIndicator {
    if (!_indicator) {
        _indicator = [[UIActivityIndicatorView alloc]  initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleGray];
        [self.view addSubview:_indicator];
        [_indicator setCenter:[[self view] center]];
        [[self view] bringSubviewToFront:_indicator];
    }
    [_indicator startAnimating];
}

- (void) stopIndicator {
    if (_indicator && [_indicator isAnimating]) {
        [_indicator stopAnimating];
    }
}

- (void) hideConnectView {
    [userNameTextField setHidden:YES];
    [connectButton setHidden:YES];
    [userNameLabel setHidden:YES];
}

- (void) showConnectView {
    [userNameTextField setHidden:NO];
    [connectButton setHidden:NO];
    [userNameLabel setHidden:NO];
}


#pragma mark - IBAction Method
- (IBAction)joinAction:(id)sender {
    kFunctionLog;
    NSString *aUserName = [userNameTextField text];
    aUserName = [aUserName stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];

    if (aUserName && [aUserName length]) {
        [self connectWithUser:aUserName];
        [self showIndicator];
        [self hideConnectView];
    } else {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Ooops!" message:@"Invalid User Name" delegate:Nil cancelButtonTitle:@"Ok" otherButtonTitles: nil];
        [alert show];
        alert = nil;
    }
    
    if ([userNameTextField isFirstResponder]) {
        [userNameTextField resignFirstResponder];
    }

}

#pragma mark - UITextField Delegate
- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [self joinAction:nil];
    return YES;
}

#pragma mark - UITableViewDataSource Delegate
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    kFunctionLog;
    return [_roomIDArray count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    kFunctionLog;
    static NSString *cellIdentifier = @"SR_List_Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    if (! cell) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
    }
    [[cell textLabel] setText:[_roomIDArray objectAtIndex:[indexPath row]]];
    
    return cell;
}

#pragma mark - UITableView Delegate
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    kFunctionLog;
    [self hideSelectionView];
}

#pragma mark - Connection Request Listener Delegate
-(void)onConnectDone:(ConnectEvent*) event {
    kFunctionLog;
    NSLog(@"%d",event.result);
    if (!event.result) {
        [self stopIndicator];
        [self getAllRoomsInfo];
    } else {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Ooops!" message:@"Connection Error, Please try again" delegate:Nil cancelButtonTitle:@"Ok" otherButtonTitles: nil];
        [alert show];
        alert = nil;
        [self showConnectView];
    }
}

-(void)onDisconnectDone:(ConnectEvent*) event {
    kFunctionLog;
    
}

-(void)onInitUDPDone:(Byte)result {
    kFunctionLog;
    
}

#pragma mark - Zone Request Listener Delegate

-(void)onCreateRoomDone:(RoomEvent*)roomEvent {
    kFunctionLog;
    if (roomEvent.result == SUCCESS) {
        RoomData *roomData = roomEvent.roomData;
        NSLog(@"roomEvent result = %i",roomEvent.result);
        NSLog(@"room id = %@",roomData.roomId);
    } else {

    }
}

-(void)onDeleteRoomDone:(RoomEvent*)roomEvent {
    kFunctionLog;
    if (roomEvent.result == SUCCESS) {
        NSLog(@" Rooms=%@",roomEvent.roomData);
    } else {
        
    }
}

-(void)onGetAllRoomsDone:(AllRoomsEvent*)event {
    kFunctionLog;
    
    NSLog(@" Result=%d",event.result);
    NSLog(@"%@",event.roomIds);

    if (event.result == SUCCESS) {
        NSLog(@" Rooms=%@",event.roomIds);
        if (event.roomIds.count != 0) {
            _roomIDArray = [NSMutableArray arrayWithArray:event.roomIds];
            [rsListTableView reloadData];
            [self showSelectionViewAnimated:YES];
        }
    } else {
        
    }
}

-(void)onGetOnlineUsersDone:(AllUsersEvent*)event {
    kFunctionLog;
    
    if (event.result == SUCCESS) {
        NSLog(@"usernames = %@",event.userNames);
    } else {
        
    }
}

-(void)onGetLiveUserInfoDone:(LiveUserInfoEvent*)event {
    kFunctionLog;
    
    NSLog(@"onGetLiveUserInfo called");
    if (event.result == SUCCESS) {
        NSLog(@"User Name = %@",event.name);
    } else {
        
    }
}

-(void)onSetCustomUserDataDone:(LiveUserInfoEvent*)event {
    kFunctionLog;
    
    if (event.result == SUCCESS) {
        NSLog(@"User Name = %@",event.name);
    } else {
        
    }
}

-(void)onGetMatchedRoomsDone:(MatchedRoomsEvent*)event {
    kFunctionLog;
    
    if (event.result == SUCCESS) {
        NSLog(@"roomData = %@",event.roomData);
    } else {
        
    }
}

@end
