package entities
{
	import org.flixel.FlxState;
	import org.flixel.FlxTilemap;
	
	public class World
	{	
		private var ground:FlxTilemap;
		private var groundOver:FlxTilemap;
		
		public function World()
		{
			
		}
		
		public function create(game:FlxState):void
		{
			ground = new FlxTilemap();
			ground.loadMap(new Assets.groundCSV, Assets.TileSheet, 16,16);
			game.add(ground);
			
			groundOver = new FlxTilemap();
			groundOver.loadMap(new Assets.groundOverCSV, Assets.TileSheet, 16,16);
			game.add(groundOver);	
		}
	}
}