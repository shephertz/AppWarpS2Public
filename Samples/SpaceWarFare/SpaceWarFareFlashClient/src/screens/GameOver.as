package screens
{
	import org.flixel.FlxButton;
	import org.flixel.FlxG;
	import org.flixel.FlxSprite;
	import org.flixel.FlxState;
	import org.flixel.FlxTilemap;
	
	public class GameOver extends FlxState
	{
		public function GameOver()
		{
			super();
			
			FlxG.mouse.show();
			
			var ground:FlxTilemap = new FlxTilemap();
			ground.loadMap(new Assets.groundCSV, Assets.TileSheet, 16,16);
			add(ground);
			
			var title:FlxSprite = new FlxSprite(0,32,Assets.gameOver);
			add(title);
			
			var newBtn:FlxButton = new FlxButton(0,128+32,"Menu", newGame);
			newBtn.x = 400/2 - newBtn.width/2;
			add(newBtn);
		}
		
		private function newGame():void
		{
			var g:FlxState = new Menu();
			FlxG.switchState(g);
		}
	}
}