
package screens
{	
	import org.flixel.FlxButton;
	import org.flixel.FlxG;
	import org.flixel.FlxSprite;
	import org.flixel.FlxState;
	import org.flixel.FlxTilemap;
	
	public class Menu extends FlxState
	{
		private var newBtn:FlxButton;
		
		public function Menu()
		{
			super();
			
			FlxG.mouse.show();
			
			var ground:FlxTilemap = new FlxTilemap();
			ground.loadMap(new Assets.groundCSV, Assets.TileSheet, 16,16);
			add(ground);
			
			var title:FlxSprite = new FlxSprite(0,32,Assets.title);
			add(title); 
			
			newBtn = new FlxButton(0,128,"New Game", newGame);
			newBtn.x = 400/2 - newBtn.width/2;
			add(newBtn);
		}
		
		private function newGame():void
		{
			FlxG.switchState(new Game());
		}
	}
}