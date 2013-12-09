package screens
{
	import com.shephertz.appwarp.messages.LiveRoom;
	import com.shephertz.appwarp.types.ResultCode;
	
	import entities.Blast;
	import entities.Bullet;
	import entities.Character;
	import entities.Item;
	import entities.World;
	
	import org.flixel.FlxButton;
	import org.flixel.FlxG;
	import org.flixel.FlxGroup;
	import org.flixel.FlxObject;
	import org.flixel.FlxPoint;
	import org.flixel.FlxRect;
	import org.flixel.FlxState;
	import org.flixel.FlxText;
	
	public class Game extends FlxState
	{
		private var world:World;
		private var player:Character;
		private const Width:int = 800;
		private const Height:int = 480;
		private var tileWidth:int = 25;
		
		private var roomID:String;
		private var remote:Array;
		private var remoteMsg:Array;
		private var remoteUserNames:Array;
		private var msg:FlxText;
		private var youLable:FlxText;
		private var log:FlxText;
		private var logCount:int;
		private var itemsFlx:FlxGroup;
		private var properties:Object;
		private var score:FlxText;
		private var BulletsFlx:FlxGroup;
		private var closeBtn:FlxButton;
		
		private var Health:int;
		private var Bullets:int;
		private var Points:int;
		
		private var user:String;
		private var name:String;
		
		public function Game(u:String,n:String)
		{
			user = u;
			name = n;
		}
		
		override public function create():void
		{
			setup();
		}
		
		override public function update():void
		{
			updateGame();
			super.update();
			FlxG.overlap(player, itemsFlx, gotItem);
		}
		
		public function setup():void
		{
			roomID = "91586126";//"410174383";
			remote = new Array();
			remoteMsg = new Array();
			remoteUserNames = new Array();
			Health = 10;
			Bullets = 50;
			Points = 0;
			FlxG.mouse.show();
			
			world = new World();
			world.create(this);
			
			closeBtn = new FlxButton(400-80,240-20,"Exit", function click():void{
				Connector.leave(roomID);
				//kill();
				FlxG.switchState(new Menu());
			});
			closeBtn.scrollFactor.x = closeBtn.scrollFactor.y = 0; 
			//add(closeBtn);
			
			player = new Character(1,1,0,0,Width,Height);
			add(player);
			
			FlxG.worldBounds = new FlxRect(0,0,Width,Height);
			FlxG.camera.follow(player);
			FlxG.camera.setBounds(0,0,Width, Height);
			
			msg = new FlxText(0,0,128,"Connecting...");
			msg.scrollFactor.x = msg.scrollFactor.y = 0;
			add(msg);
			
			log = new FlxText(0,8,128,"");
			log.scrollFactor.x = log.scrollFactor.y = 0;
			add(log);
			logCount = 0;

			itemsFlx = new FlxGroup();
			add(itemsFlx);
			
			BulletsFlx = new FlxGroup();
			add(BulletsFlx)
			
			score = new FlxText(Width/2-128,0,128,"Score : "+Points+"\nHealth : "+Health+"\nBullets : "+Bullets);
			score.scrollFactor.x = score.scrollFactor.y = 0;
			score.alignment = "right";
			add(score);
			
			user = Connector.connect(this,user,name);
			
			if(name == "")
				youLable = new FlxText(player.x,player.y-8,64,user);
			else
				youLable = new FlxText(player.x,player.y-8,64,name.search(" ") != -1 ? name.substring(0,name.search(" ")) : name);
			
			add(youLable);
		}
		
		public function updateGame():void
		{
			var pos:FlxPoint;
			
			if(FlxG.keys.UP)
				pos = player.move(Character.Up);
			else if(FlxG.keys.LEFT)
				pos = player.move(Character.Left);
			else if(FlxG.keys.DOWN)
				pos = player.move(Character.Down);
			else if(FlxG.keys.RIGHT)
				pos = player.move(Character.Right);
			else if(FlxG.keys.ESCAPE)
			{
				Connector.leave(roomID);
				//kill();
				FlxG.switchState(new Menu());
			}
			
			if(pos!=null)
				Connector.SendMove(pos.x,pos.y);
			
			if(remoteMsg.length > 0)
			{
				if(remoteMsg[0].sender == "dragon")
				{
					remoteUserNames[remoteMsg[0].sender].text = remoteMsg[0].sender + "("+remoteMsg[0].health+"%)";
				}
				
				if(remote[remoteMsg[0].sender].moveXY(remoteMsg[0].x,remoteMsg[0].y) != null)
				{
					remoteMsg.shift();
				}
				
			}
			
			for (var senderName:String in remoteUserNames)
			{
				remoteUserNames[senderName].x = remote[senderName].x;
				remoteUserNames[senderName].y = remote[senderName].y-10;
			}
			
			youLable.x = player.x;
			youLable.y = player.y - 10;
			
			if(FlxG.mouse.justReleased() && Bullets > 0)
			{
				var shot:Bullet = new Bullet(BulletsFlx,player.x+12,player.y+12,FlxG.mouse.x,FlxG.mouse.y,makeBlast);
				Bullets -= 1;
				score.text = "Score : "+Points+"\nHealth : "+Health+"\nBullets : "+Bullets;
				
				for (var sender:String in remote)
				{
					if(FlxG.mouse.x >= remote[sender].x && FlxG.mouse.x <= remote[sender].x+remote[sender].width)
					{
						if(FlxG.mouse.y >= remote[sender].y && FlxG.mouse.y <= remote[sender].y+remote[sender].height)
						{
							Points += 100;
							score.text = "Score : "+Points+"\nHealth : "+Health+"\nBullets : "+Bullets;
							Connector.SendAttack(sender);
						}
					}
				}
			}
			
			var dragon:String = "dragon";
			if(dragon in remote)
			{
				if(checkCollision(remote[dragon].x, remote[dragon].y,remote[dragon].width, remote[dragon].height,player.x,player.y,player.width, player.height) == true)
				{
					Connector.leave(roomID);
					//kill();
					FlxG.switchState(new GameOver());
				}
			}
			
			if(Health <= 0)
			{
				Connector.leave(roomID);
				//kill();
				FlxG.switchState(new GameOver());
			}
		}
		
		private function checkCollision(x:int, y:int, w:int,h:int, x1:int, y1:int, w1:int, h1:int):Boolean
		{
			return !((y+h < y1) || (y > y1+h1) || (x > x1+w1) || (x+w < x1));
		}
		
		public function connectDone(res:int):void
		{
			if(res == ResultCode.success)
			{
				msg.text = "Joining Game...";
				Connector.setProp(roomID);
				Connector.join(roomID);		
			}
			else
			{
				msg.text = "Not Connected";
			}
		}
		
		public function disconnectDone(res:int):void
		{
			msg.text = "DisConnected";
		}
		
		public function joinDone(res:int):void
		{
			if(res == ResultCode.success)
			{
				msg.text = "Connected";
				Connector.getRoomInfo(roomID);
				//AppWarp.setProp(roomID);
			}
		}
		
		public function listen(sender:String,obj:Object):void
		{
			if(sender in remote)
			{		
				if(obj.type == 1)
				{
					obj.sender = sender;
					//remote[sender].moveXY(obj.x,obj.y);
					remoteMsg.push(obj);
					logCount += 1;
					try
					{
						log.text = "["+logCount + "]" +obj.name + " Moved";
					}
					catch(e){}	
				}
				else if(obj.type == 2)
				{
					if(obj.p == Connector.getUser())
					{
						try
						{
							Health -= 1;
							score.text = "Score : "+Points+"\nHealth : "+Health+"\nBullets : "+Bullets;
							var shot:Bullet = new Bullet(BulletsFlx,remote[sender].x,remote[sender].y,player.x+12,player.y+12, makeBlast);
							
							logCount += 1;
							log.text = "["+logCount + "]" + obj.name + " Shot You";
						}
						catch(e)
						{
							
						}						
					}
				}
				else if(obj.type == 3)
				{
					remote[sender].kill();
					remoteUserNames[sender].kill();
					delete remoteUserNames[sender];
					delete remote[sender];
				}
			}
			else
			{
				var p:Character = new Character(obj.x/tileWidth,obj.y/tileWidth,0,0,Width,Height, sender == "dragon" ? true : false);
				add(p);	
				remote[sender] = p;
				
				var rname:FlxText = new FlxText(obj.x/tileWidth,obj.y/tileWidth,72,obj.name.search(" ") != -1 ? obj.name.substring(0,obj.name.search(" ")) : obj.name);
				add(rname);
				remoteUserNames[sender] = rname;
				
				try
				{
					logCount += 1;
					log.text = "["+logCount + "]" + obj.name + " Joined";	
				}
				catch(e){}
			}
		}
		
		public function makeBlast(px:int,py:int):void
		{
			var b:Blast = new Blast(this,px,py);
		}
		
		public function roomInfoDone(obj:LiveRoom):void
		{
			properties = obj.properties;
			
			itemsFlx.callAll("kill");
			itemsFlx.clear();
			for(var i:Object in properties)
			{
				var item:Item = new Item(properties[i].x,properties[i].y,parseInt(i.substr(-1,1)));
				item.ID = parseInt(i.substr(-1,1));
				itemsFlx.add(item);
			}
		}
		
		public function gotItem(player:FlxObject,item:FlxObject):void
		{
			var x:int = Math.random()*25;
			var y:int = Math.random()*15;
			
			if(item.ID == 1)
			{
				Health += 1;
				Points += 10;
			}
			else if(item.ID == 2)
			{
				Bullets += 3;
				Points += 10;
			}
			else
			{
				Points += 50;
			}
			
			score.text = "Score : "+Points+"\nHealth : "+Health+"\nBullets : "+Bullets;
			
			Connector.placeItems(roomID,properties,"item"+item.ID,x,y);
			itemsFlx.remove(item);
			item.kill();
		}
		
		public function leftRoom(user:String):void
		{
			if(user in remote)
			{
				logCount += 1;
				log.text = "["+logCount + "]" + remoteUserNames[user] + " Left";
				
				remote[user].kill();
				remoteUserNames[user].kill();
				delete remoteUserNames[user];
				delete remote[user];
			}
		}
	}
}