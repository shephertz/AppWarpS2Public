
package
{
	import com.adobe.serialization.json.JSON;
	import com.shephertz.appwarp.WarpClient;
	import com.shephertz.appwarp.types.ResultCode;

	public class Connector
	{	
		private static var _listener:Listener;
		private static var caller:Object;
		private static var APIKEY:String = "49130234-abf8-402b-b"; // Your APP Key
		private static var HOST:String = "localhost";
		public static var Connected:Boolean = false;
		
		private static var client:WarpClient;
		private static var State:int = 0;
		private static var User:String;
		private static var Name:String;
		
		private static function generateRandomString(strlen:Number):String{
			var chars:String = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
			var num_chars:Number = chars.length - 1;
			var randomChar:String = "";
			for (var i:Number = 0; i < strlen; i++){
				randomChar += chars.charAt(Math.floor(Math.random() * num_chars));
			}
			return randomChar;
		}

		public static function connect(c:Object,id:String,username:String):String
		{
			caller = c;
			Name = username;
			if(Connected == false)
			{
				WarpClient.initialize(APIKEY, HOST);
				client = WarpClient.getInstance();
				if(id == "")
					User = generateRandomString(6);
				else
					User = id;
				
				_listener = new Listener(caller,client,User);
				client.setConnectionRequestListener(_listener);
				client.setRoomRequestListener(_listener);
				client.setNotificationListener(_listener);
				client.connect(User, "");
			}
			else
			{
				_listener = new Listener(caller,client,User);
				client.setConnectionRequestListener(_listener);
				client.setRoomRequestListener(_listener);
				client.setNotificationListener(_listener);
				
				caller.connectDone(ResultCode.success);
			}
			
			return User;
		}
		
		public static function join(roomId:String):void
		{
			_listener.joinRoom(roomId);
		}
	
		public static function leave(roomId:String):void
		{
			client.unsubscribeRoom(roomId);
		}
		
		public static function Send(obj:Object):void
		{
			if(Connected == true)
			{
				client.sendChat(com.adobe.serialization.json.JSON.encode(obj));
			}
		}
		
		public static function SendMove(x:int,y:int):void
		{
			var obj:Object = new Object;
			obj.type = 1;
			obj.x = x;
			obj.y = y;
			if(Name != "")
				obj.name = Name;
			else 
				obj.name = User;
			
			Send(obj);
		}
		
		public static function SendAttack(player:String):void
		{
			var obj:Object = new Object;
			obj.type = 2;
			obj.p = player;
			if(Name != "")
				obj.name = Name;
			else 
				obj.name = User;
			
			Send(obj);
		}
		
		public static function getRoomInfo(room:String):void
		{
			client.getLiveRoomInfo(room);	
		}
		
		public static function setProp(room:String):void
		{
			var str:String = '{"item1": {"x": 9,"y": 10},"item2": {"x": 5,"y": 8},"item3": {"x": 20,"y": 14},"item4": {"x": 15,"y": 18},"item5": {"x": 25,"y": 10}}';
			client.updateRoomProperties(room,com.adobe.serialization.json.JSON.decode(str),new Array("x","y"));
		}
		
		public static function placeItems(room:String,props:Object,prop:String,x:int,y:int):void
		{
			props[prop].x = x;
			props[prop].y = y;
			client.updateRoomProperties(room,props,null);
		}
		
		public static function getUser():String
		{
			return User;
		}
	}
}