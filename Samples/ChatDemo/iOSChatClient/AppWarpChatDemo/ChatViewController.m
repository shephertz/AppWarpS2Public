//
//  ChatViewController.m
//  AppWarpChatDemo
//
//  Created by Nitin Gupta on 05/03/14.
//  Copyright (c) 2014 Nitin Gupta. All rights reserved.
//

#import "ChatViewController.h"

@interface ChatViewController ()

@end

static NSString *kSenderKey = @"Sender";
static NSString *kMessageKey = @"Message";
static NSString *kLocIDKey = @"LocID";

@implementation ChatViewController
@synthesize selectedRoomID = _selectedRoomID;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    WarpClient *wrCl = [WarpClient getInstance];
    [wrCl  addChatRequestListener:self];
    [wrCl addNotificationListener:self];
    [wrCl  addRoomRequestListener:self];

    _roomChatArray = [NSMutableArray array];
    [self joinRoomForSelectedID];
	// Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) viewWillDisappear:(BOOL)animated {

    [super viewWillDisappear: animated];
    
    WarpClient *wrCl = [WarpClient getInstance];
    [wrCl leaveRoom:_selectedRoomID];
    [wrCl unsubscribeRoom:_selectedRoomID];
}

#pragma mark - Life Cycle Method
- (void) joinRoomForSelectedID {
    WarpClient *wrCl = [WarpClient getInstance];
    [wrCl joinRoom:_selectedRoomID];
    [wrCl subscribeRoom:_selectedRoomID];
}

- (void) sendChatForText:(NSString *)_text {
    WarpClient *wrCl = [WarpClient getInstance];
    [wrCl sendChat:_text];
}

- (IBAction)sendAction:(id)sender {
    NSString *chatText = [chatTextField text];
    chatText = [chatText stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    if ([chatText length]) {
        [self sendChatForText:chatText];
        [chatTextField setText:@""];
    }
}

#pragma mark - UITableViewDataSource Delegate
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    kFunctionLog;
    NSInteger _count = [_roomChatArray count];
    _count = _count <= 0 ? 1: _count;
    return _count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    kFunctionLog;
    static NSString *cellIdentifier = @"SR_Chat_Cell";
    NSInteger _count = [_roomChatArray count];
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    if (! cell) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
    }

    if (_count  <= 0 && indexPath.row == 0) {
        [cell setAccessoryType:UITableViewCellAccessoryNone];
        [[cell textLabel] setText:@"No Chat Result"];
        [[cell detailTextLabel] setText:@""];
    } else  {
        NSDictionary *_chatInfo = [_roomChatArray objectAtIndex:[indexPath row]];
        if (_chatInfo) {
            [[cell textLabel] setText:[_chatInfo objectForKey:kMessageKey]];
            [[cell detailTextLabel] setText:[_chatInfo objectForKey:kSenderKey]];
        }
        [cell setAccessoryType:UITableViewCellAccessoryCheckmark];
    }
    
    return cell;
}

#pragma mark - UITextField Delegate
- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    [self sendAction:nil];
    return YES;
}

-(void)textFieldDidBeginEditing:(UITextField *)textField {
    //Keyboard becomes visible
    textField.frame = CGRectMake(textField.frame.origin.x,
                                  textField.frame.origin.y - 210,
                                  textField.frame.size.width,
                                  textField.frame.size.height );   //resize
}

-(void)textFieldDidEndEditing:(UITextField *)textField {
    //keyboard will hide
    textField.frame = CGRectMake(textField.frame.origin.x,
                                 textField.frame.origin.y + 210,
                                 textField.frame.size.width,
                                 textField.frame.size.height );   //resize
}

#pragma mark - UITableView Delegate
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    kFunctionLog;
}

#pragma mark - Room Listner Delegate
-(void)onSubscribeRoomDone:(RoomEvent*)roomEvent {
    kFunctionLog;
    
    if (roomEvent.result == SUCCESS) {
        NSLog(@"onSubscribeRoomDone  SUCCESS");
    } else {
        NSLog(@"onSubscribeRoomDone  Failed");
    }
}

-(void)onUnSubscribeRoomDone:(RoomEvent*)roomEvent {
    kFunctionLog;
    if (roomEvent.result == SUCCESS) {
        
    } else {
        
    }
}

-(void)onJoinRoomDone:(RoomEvent*)roomEvent {
    kFunctionLog;
    
    NSLog(@".onJoinRoomDone..on Join room listener called");
    if (roomEvent.result == SUCCESS) {
        NSLog(@".onJoinRoomDone..on Join room listener called Success");
    } else {
        NSLog(@".onJoinRoomDone..on Join room listener called failed");
    }
}

-(void)onLeaveRoomDone:(RoomEvent*)roomEvent {
    kFunctionLog;
    
    if (roomEvent.result == SUCCESS) {
        
    } else {
        
    }
}

-(void)onGetLiveRoomInfoDone:(LiveRoomInfoEvent*)event {
    kFunctionLog;
    NSString *joinedUsers = @"";
    NSLog(@"joined users array = %@",event.joinedUsers);
    for (int i=0; i<[event.joinedUsers count]; i++) {
        joinedUsers = [joinedUsers stringByAppendingString:[event.joinedUsers objectAtIndex:i]];
    }
}

-(void)onSetCustomRoomDataDone:(LiveRoomInfoEvent*)event {
    kFunctionLog;
    NSLog(@"event joined users = %@",event.joinedUsers);
    NSLog(@"event custom data = %@",event.customData);
}

-(void)onUpdatePropertyDone:(LiveRoomInfoEvent *)event {
    kFunctionLog;
    
    if (event.result == SUCCESS) {
        
    } else {
        
    }
}

-(void)onLockPropertiesDone:(Byte)result {
    kFunctionLog;
}

-(void)onUnlockPropertiesDone:(Byte)result {
    kFunctionLog;
}

#pragma mark - Chat Listener Delegate
-(void)onSendChatDone:(Byte)result {
    kFunctionLog;
    if (result==SUCCESS) {

    } else {
    }
}

-(void)onSendPrivateChatDone:(Byte)result {
    kFunctionLog;
}

#pragma mark - NotifyListener Delegate
-(void)onRoomCreated:(RoomData*)roomData {
    kFunctionLog;
}

-(void)onRoomDestroyed:(RoomData*)roomEvent {
    kFunctionLog;
}

-(void)onUserLeftRoom:(RoomData*)roomData username:(NSString*)username {
    kFunctionLog;
}

-(void)onUserJoinedRoom:(RoomData*)roomData username:(NSString*)username {
    kFunctionLog;
}

-(void)onUserLeftLobby:(LobbyData*)lobbyData username:(NSString*)username {
    kFunctionLog;
}

-(void)onUserJoinedLobby:(LobbyData*)lobbyData username:(NSString*)username {
    kFunctionLog;
}

-(void)onChatReceived:(ChatEvent*)chatEvent {
    kFunctionLog;
    NSLog(@"chatEvent.sender = %@, chatEvent.message = %@, chatEvent.locId = %@",chatEvent.sender,chatEvent.message,chatEvent.locId);
    NSDictionary *_chatDict = [NSDictionary dictionaryWithObjectsAndKeys:chatEvent.sender,kSenderKey,
                                                                                                            chatEvent.message,kMessageKey,
                                                                                                            chatEvent.locId,kLocIDKey, nil];
    [_roomChatArray addObject:_chatDict];
    [chatTableView reloadData];
}

-(void)onPrivateChatReceived:(NSString*)message fromUser:(NSString*)senderName {
    kFunctionLog;
}

-(void)onUserChangeRoomProperty:(RoomData*)event username:(NSString*)username properties:(NSDictionary*)properties lockedProperties:(NSDictionary*)lockedProperties {
    kFunctionLog;
}

-(void)onMoveCompleted:(MoveEvent*) moveEvent {
    kFunctionLog;
}

-(void)onUserPaused:(NSString*)userName withLocation:(NSString*)locId isLobby:(BOOL)isLobby {
    kFunctionLog;
}

-(void)onUserResumed:(NSString*)userName withLocation:(NSString*)locId isLobby:(BOOL)isLobby {
    kFunctionLog;
}

-(void)onGameStarted:(NSString*)sender roomId:(NSString*)roomId  nextTurn:(NSString*)nextTurn {
    kFunctionLog;
}

-(void)onGameStopped:(NSString*)sender roomId:(NSString*)roomId {
    kFunctionLog;
}

-(void)onUpdatePeersReceived:(UpdateEvent*)updateEvent {
    kFunctionLog;
    NSData *data = updateEvent.update;
    NSError *error = nil;
	NSPropertyListFormat plistFormat;
    
    /*! Data Conversion*/
	if(data) {
        id contentObject = [NSPropertyListSerialization propertyListWithData:data options:0 format:&plistFormat error:&error];
        if(error) {
            NSLog(@"DataConversion Failed. ErrorDescription: %@",[error description]);
        } else {
            NSLog(@"Data received--- %@",contentObject);
        }
	}
}

-(void)onUserChangeRoomProperty:(RoomData *)event username:(NSString *)username properties:(NSDictionary *)properties {
    kFunctionLog;
}

@end
