package
{
	public class Assets
	{
		[Embed(source = '../assets/images/player_green.png')] 
		public static var playerGreenPNG:Class;
		
		[Embed(source = '../assets/images/items.png')] 
		public static var itemsPNG:Class;
		
		[Embed(source = '../assets/images/shot.png')] 
		public static var shotPNG:Class;
		
		[Embed(source = '../assets/images/blast.png')] 
		public static var blastPNG:Class;
		
		[Embed(source = "../assets/maps/mapCSV_World_Ground.csv", mimeType = "application/octet-stream")] 
		public static var groundCSV:Class;
		
		[Embed(source = "../assets/maps/mapCSV_World_GroundOver.csv", mimeType = "application/octet-stream")] 
		public static var groundOverCSV:Class;
		
		[Embed(source = "../assets/images/tileset.png")] 
		public static var TileSheet:Class;
		
		[Embed(source = "../assets/images/title.png")] 
		public static var title:Class;
		
		[Embed(source = "../assets/images/gameover.png")] 
		public static var gameOver:Class;
	}
}