
#include "GameListener.h"

//AppWarp::ConnectionRequestListener
void GameListener::onConnectDone(int res)
{
}
void GameListener::onDisconnectDone(int res)
{
}

//AppWarp::LobbyRequestListener
void GameListener::onJoinLobbyDone(AppWarp::lobby levent)
{
}
void GameListener::onSubscribeLobbyDone(AppWarp::lobby levent)
{
}
void GameListener::onUnsubscribeLobbyDone(AppWarp::lobby levent)
{
}
void GameListener::onLeaveLobbyDone(AppWarp::lobby levent)
{
}
void GameListener::onGetLiveLobbyInfoDone(AppWarp::livelobby levent)
{
}

//AppWarp::NotificationListener
void GameListener::onRoomCreated(AppWarp::room rData)
{
}
void GameListener::onRoomDestroyed(AppWarp::room rData)
{
}
void GameListener::onUserLeftRoom(AppWarp::room rData, std::string user)
{
}
void GameListener::onUserJoinedRoom(AppWarp::room rData, std::string user)
{
}
void GameListener::onUserLeftLobby(AppWarp::lobby ldata, std::string user)
{
}
void GameListener::onUserJoinedLobby(AppWarp::lobby ldata, std::string user)
{
}
void GameListener::onChatReceived(AppWarp::chat chatevent)
{
}
void GameListener::onUpdatePeersReceived(AppWarp::byte update[], int len)
{
}

//AppWarp::RoomRequestListener
void GameListener::onSubscribeRoomDone(AppWarp::room revent)
{
}
void GameListener::onUnsubscribeRoomDone(AppWarp::room revent)
{
}
void GameListener::onJoinRoomDone(AppWarp::room revent)
{
}
void GameListener::onLeaveRoomDone (AppWarp::room revent)
{
}
void GameListener::onGetLiveRoomInfoDone(AppWarp::liveroom revent)
{
}
void GameListener::onSetCustomRoomDataDone (AppWarp::liveroom revent)
{
}
void GameListener::onUpdatePropertyDone(AppWarp::liveroom revent)
{
}

//AppWarp::ZoneRequestListener
void GameListener::onCreateRoomDone (AppWarp::room revent)
{
}
void GameListener::onDeleteRoomDone (AppWarp::room revent)
{
}
void GameListener::onGetAllRoomsDone (AppWarp::liveresult res)
{
}
void GameListener::onGetOnlineUsersDone (AppWarp::liveresult res)
{
}
void GameListener::onGetLiveUserInfoDone (AppWarp::liveuser uevent)
{
}
void GameListener::onSetCustomUserInfoDone (AppWarp::liveuser uevent)
{
}
void GameListener::onGetMatchedRoomsDone(AppWarp::matchedroom mevent)
{
}

//AppWarp::ChatRequestListener
void GameListener::onSendChatDone(int res)
{
}

//AppWarp::UpdateRequestListener
void GameListener::onSendUpdateDone(int res)
{
}

//AppWarp::TurnBasedRoomRequestListener
void GameListener::onStartGameDone(int res)
{
}
void GameListener::onStopGameDone(int res)
{
}
void GameListener::onSendMoveDone(int res)
{
}
void GameListener:: onGetMoveHistoryDone(int res, std::vector<AppWarp::move> history)
{
}