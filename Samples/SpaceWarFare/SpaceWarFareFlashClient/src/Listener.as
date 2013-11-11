package
{
	import com.adobe.serialization.json.JSON;
	import com.shephertz.appwarp.WarpClient;
	import com.shephertz.appwarp.listener.ConnectionRequestListener;
	import com.shephertz.appwarp.listener.NotificationListener;
	import com.shephertz.appwarp.listener.RoomRequestListener;
	import com.shephertz.appwarp.messages.Chat;
	import com.shephertz.appwarp.messages.LiveRoom;
	import com.shephertz.appwarp.messages.Lobby;
	import com.shephertz.appwarp.messages.Move;
	import com.shephertz.appwarp.messages.Room;
	import com.shephertz.appwarp.types.ResultCode;
	
	import flash.utils.ByteArray;

	public class Listener implements ConnectionRequestListener, RoomRequestListener, NotificationListener
	{
		private var caller:Object;
		private var roomId:String;
		private var client:WarpClient;
		private var User:String;
		
		public function Listener(c:Object,warp:WarpClient,username:String):void
		{
			caller = c;
			client = warp;
			User = username;
		}
		
		public function onConnectDone(res:int):void
		{
			if(res == ResultCode.success)
				Connector.Connected = true;
			else
				Connector.Connected = false;
			
			caller.connectDone(res);
		}
		
		public function onDisConnectDone(res:int):void
		{
			Connector.Connected = false;
			caller.disconnectDone(res);
		}
		
		
		public function onInitUDPDone(res:int):void{}
		
		public function joinRoom(id:String):void
		{
			roomId = id;
			client.subscribeRoom(roomId);
		}
		
		public function onSubscribeRoomDone(event:Room):void
		{
			if(event.result == ResultCode.success)
			{
				client.joinRoom(roomId);
			}
			else
			{
				caller.joinDone(ResultCode.unknown_error);
			}
		}
		
		public function onUnsubscribeRoomDone(event:Room):void
		{
			client.leaveRoom(event.roomId);
		}
		
		public function onJoinRoomDone(event:Room):void
		{
			if(event.result == ResultCode.success)
			{
				caller.joinDone(ResultCode.success);
			}
			else
			{
				caller.joinDone(ResultCode.unknown_error);
			}
		}
		
		public function onLeaveRoomDone(event:Room):void
		{
			
		}
		
		public function onGetLiveRoomInfoDone(event:LiveRoom):void
		{
			caller.roomInfoDone(event);
		}
		
		public function onSetCustomRoomDataDone(event:LiveRoom):void
		{
			
		}
		
		public function onUpdatePropertyDone(event:LiveRoom):void
		{
			
		}
		
		public function onLockPropertiesDone(result:int):void
		{
			
		}
		
		public function onUnlockPropertiesDone(result:int):void
		{
			
		}
		
		public function onUpdatePropertiesDone(event:LiveRoom):void
		{
			client.getLiveRoomInfo(event.room.roomId);
		}
		
		public function onRoomCreated(event:Room):void
		{
			
		}
		
		public function onRoomDestroyed(event:Room):void
		{
			
		}
		
		public function onUserLeftRoom(event:Room, user:String):void
		{
			caller.leftRoom(user);
		}
		
		public function onUserJoinedRoom(event:Room, user:String):void
		{
			
		}
		
		public function onUserLeftLobby(event:Lobby, user:String):void
		{
			
		}
		
		public function onUserJoinedLobby(event:Lobby, user:String):void
		{
			
		}
		
		public function onChatReceived(event:Chat):void
		{
			if(event.sender != User)
				caller.listen(event.sender,com.adobe.serialization.json.JSON.decode(event.chat));
		}
		
		public function onUpdatePeersReceived(update:ByteArray, isUDP:Boolean):void
		{
			
		}
		
		public function onUserChangeRoomProperty(room:Room, user:String,properties:Object):void
		{
			
		}
		
		public function onPrivateChatReceived(sender:String, chat:String):void
		{
			
		}
		
		public function onUserChangeRoomProperties(room:Room, user:String,properties:Object, lockTable:Object):void
		{
			client.getLiveRoomInfo(room.roomId);
		}
		
		public function onMoveCompleted(move:Move):void
		{
			
		}
		
		
		public function onUserPaused(locid:String, isLobby:Boolean, username:String):void{}
		public function onUserResumed(locid:String, isLobby:Boolean, username:String):void{}
		public function onGameStarted(sender:String, roomid:String, nextTurn:String):void{}
		public function onGameStopped(sender:String, roomid:String):void{}
	}
}