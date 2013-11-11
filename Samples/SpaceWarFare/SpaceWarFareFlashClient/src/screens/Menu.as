
package screens
{
	//import flash.external.ExternalInterface;
	
	import org.flixel.FlxButton;
	import org.flixel.FlxG;
	import org.flixel.FlxSprite;
	import org.flixel.FlxState;
	import org.flixel.FlxTilemap;
	
	public class Menu extends FlxState
	{
		private var fb:FB;
		
		private var startBtn:FlxButton;
		private var newBtn:FlxButton;
		private var shareBtn:FlxButton;
		private var inviteBtn:FlxButton;
		
		public function Menu()
		{
			super();
			
			FlxG.mouse.show();
			
			var ground:FlxTilemap = new FlxTilemap();
			ground.loadMap(new Assets.groundCSV, Assets.TileSheet, 16,16);
			add(ground);
			
			var title:FlxSprite = new FlxSprite(0,32,Assets.title);
			add(title);			
			
			fb = new FB();
			fb.init("539928402722587",onInit);    
		}
		
		private function onInit(response:Object, fail:Object):void
		{
			//trace("[Facebook] Initialised");
			//ExternalInterface.call("console.log","[Facebook] Initialised");
			if(response)
			{
				//trace("[Facebook] Already Logged In");
				//ExternalInterface.call("console.log","[Facebook] Already Logged In");
			}
			else
			{
				//trace("[Facebook] Not Logged In");
				//ExternalInterface.call("console.log","[Facebook] Not Logged In");
			}
			
			startBtn = new FlxButton(0,128,"Start", Login);
			startBtn.x = 400/2 - startBtn.width/2;
			add(startBtn);
			
		}
		
		private function Login():void
		{
			fb.Login(onLogin);
			//beginGame(null,null);
		}
		
		private function onLogin(response:Object, fail:Object):void
		{
			if(response)
			{
				//trace("[Facebook] Logging Successful");
				//ExternalInterface.call("console.log","[Facebook] Logging Successful");
				
				try
				{
					startBtn.kill();
					
					newBtn = new FlxButton(0,128,"New Game", newGame);
					newBtn.x = 400/2 - newBtn.width/2;
					add(newBtn);
					
					shareBtn = new FlxButton(0,128+32,"Share on FB", share);
					shareBtn.x = 400/2 - shareBtn.width/2;
					//add(shareBtn);
					
					inviteBtn = new FlxButton(0,128+32+32,"Invite Friends", invite);
					inviteBtn.x = 400/2 - inviteBtn.width/2;
					//add(inviteBtn);
				}
				catch(e){}
			}
			else
			{
				//trace("[Facebook] Logging Failed");
				//ExternalInterface.call("console.log","[Facebook] Logging Failed");
			}
		}
		
		private function share():void
		{
			fb.postOnWal("Google","Google Search Engine","http://www.google.com","Search the web",onPosting);
		}
		
		private function onPosting(response:Object, fail:Object):void
		{
			//trace("[Facebook] Posted");
			//ExternalInterface.call("console.log","[Facebook] Posted");
			shareBtn.label.text = "Shared";
		}
		
		private function invite():void
		{
			fb.IniviteFBFriends();
		}
		
		private function newGame():void
		{
			fb.GetMe(beginGame);
			newBtn.label.text = "Loading";
		}
		
		private function beginGame(response:Object, fail:Object):void
		{
			var user:String = "";
			var name:String = "";
			if(response)
			{
				user = response.id;
				name = response.name;
			}
			//kill();
			FlxG.switchState(new Game(user,name));
		}
	}
}