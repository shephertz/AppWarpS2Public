package entities
{
	import org.flixel.FlxSprite;
	
	public class Item extends FlxSprite
	{
		private var tileWidth:int = 25;
		
		public function Item(X:Number=0, Y:Number=0, Type:int=1)
		{
			super(X*tileWidth, Y*tileWidth, null);
			
			loadGraphic(Assets.itemsPNG,true,false,25,25,false);
			
			addAnimation("health",[6]);
			addAnimation("bullet",[10]);
			addAnimation("diamond1",[13]);
			addAnimation("diamond2",[14]);
			addAnimation("diamond3",[15]);
			
			if(Type == 1)
				play("health");
			else if(Type == 2)
				play("bullet");
			else if(Type == 3)
				play("diamond1");
			else if(Type == 4)
				play("diamond2");
			else if(Type == 5)
				play("diamond3");
		}
	}
}