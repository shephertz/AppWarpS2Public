#ifndef __GAME_LISTENER_H__
#define __GAME_LISTENER_H__

#include "appwarp.h"

class GameListener : public AppWarp::ConnectionRequestListener, public AppWarp::LobbyRequestListener, public AppWarp::NotificationListener, public AppWarp::RoomRequestListener, public AppWarp::ZoneRequestListener,public AppWarp::ChatRequestListener, public AppWarp::UpdateRequestListener, public AppWarp::TurnBasedRoomRequestListener
{
public:
	//AppWarp::ConnectionRequestListener
	void onConnectDone(int res);
	void onDisconnectDone(int res);

	//AppWarp::LobbyRequestListener
	void onJoinLobbyDone(AppWarp::lobby levent) ;
	void onSubscribeLobbyDone(AppWarp::lobby levent) ;
	void onUnsubscribeLobbyDone(AppWarp::lobby levent) ;
	void onLeaveLobbyDone(AppWarp::lobby levent) ;
	void onGetLiveLobbyInfoDone(AppWarp::livelobby levent) ;

	//AppWarp::NotificationListener
	void onRoomCreated(AppWarp::room rData) ;
	void onRoomDestroyed(AppWarp::room rData) ;
	void onUserLeftRoom(AppWarp::room rData, std::string user) ;
	void onUserJoinedRoom(AppWarp::room rData, std::string user);
	void onUserLeftLobby(AppWarp::lobby ldata, std::string user);
	void onUserJoinedLobby(AppWarp::lobby ldata, std::string user) ;
	void onChatReceived(AppWarp::chat chatevent);
	void onUpdatePeersReceived(AppWarp::byte update[], int len);

	//AppWarp::RoomRequestListener
	void onSubscribeRoomDone(AppWarp::room revent);
	void onUnsubscribeRoomDone(AppWarp::room revent);
	void onJoinRoomDone(AppWarp::room revent);
	void onLeaveRoomDone (AppWarp::room revent);
	void onGetLiveRoomInfoDone(AppWarp::liveroom revent);
	void onSetCustomRoomDataDone (AppWarp::liveroom revent);
	void onUpdatePropertyDone(AppWarp::liveroom revent);

	//AppWarp::ZoneRequestListener
	void onCreateRoomDone (AppWarp::room revent);
	void onDeleteRoomDone (AppWarp::room revent);
	void onGetAllRoomsDone (AppWarp::liveresult res);
	void onGetOnlineUsersDone (AppWarp::liveresult res);
	void onGetLiveUserInfoDone (AppWarp::liveuser uevent);
	void onSetCustomUserInfoDone (AppWarp::liveuser uevent);
	void onGetMatchedRoomsDone(AppWarp::matchedroom mevent);

	//AppWarp::ChatRequestListener
	void onSendChatDone(int res);

	//AppWarp::UpdateRequestListener
	void onSendUpdateDone(int res);

	//AppWarp::TurnBasedRoomRequestListener
	void onStartGameDone(int res);
    void onStopGameDone(int res);
    void onSendMoveDone(int res);
    void onGetMoveHistoryDone(int res, std::vector<AppWarp::move> history);
};

#endif