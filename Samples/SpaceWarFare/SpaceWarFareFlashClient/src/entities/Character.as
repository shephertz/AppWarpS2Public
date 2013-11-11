package entities
{
	
	import org.flixel.FlxG;
	import org.flixel.FlxPoint;
	import org.flixel.FlxRect;
	import org.flixel.FlxSprite;

	public class Character extends FlxSprite
	{
		public static const Left:int = 0;
		public static const Right:int = 1;
		public static const Up:int = 2;
		public static const Down:int = 3;
		public static const Stand:int = 4;
		
		public var Speed:int = 50;
		
		private var tileWidth:int = 25;
		private var moving:int = Stand;
		private var nextStep:int = 0;
		private var count:Number = 0;
		private var bounds:FlxRect = new FlxRect();
		
		public function Character(X:Number=0, Y:Number=0,OX:Number=0,OY:Number=0,W:Number=0,H:Number=0)
		{	
			bounds.x = OX;
			bounds.y = OY;
			bounds.width = W;
			bounds.height = H;
			super(X*tileWidth, Y*tileWidth, null);
			
			loadGraphic(Assets.playerGreenPNG,true,true,tileWidth,tileWidth);
			
			addAnimation("walkRight",[8,9,10,11],10,true);
			addAnimation("walkLeft",[12,13,14,15],10,true);
			addAnimation("walkUp",[4,5,6,7],10,true);
			addAnimation("walkDown",[0,1,2,3],10,true);
			addAnimation("idle",[0],5,true);
			play("idle");
		}
		
		override public function update( ):void
		{
			if(moving != Stand)
			{
				if(moving == Left)
				{
					x -= Speed*FlxG.elapsed;
					if(x < nextStep)
					{
						x = nextStep;
						moving = Stand;
						count = 0;
					}
				}
				else if(moving == Right)
				{
					x += Speed*FlxG.elapsed;
					if(x > nextStep)
					{
						x = nextStep;
						moving = Stand;
						count = 0;
					}
				}
				else if(moving == Up)
				{
					y -= Speed*FlxG.elapsed;
					if(y < nextStep)
					{
						y = nextStep;
						moving = Stand;
						count = 0;
					}
				}
				else if(moving == Down)
				{
					y += Speed*FlxG.elapsed;
					if(y > nextStep)
					{
						y = nextStep;
						moving = Stand;
						count = 0;
					}
				}
			}
			else 
			{
				if(count < 0.3)
					count += FlxG.elapsed;
			}
			
			if(count > 0.3)
				play("idle");
			
			super.update();
		}
		
		public function place(px:int, py:int):void
		{
			x = px * tileWidth;
			y = py * tileWidth;
		}
		
		public function move(dir:int):FlxPoint
		{
			var pos:FlxPoint = new FlxPoint(x,y);
			if(moving == Stand)
			{
				count = 0;
				moving = dir;
				if(dir == Left && x > bounds.x)
				{
					nextStep = x - tileWidth;
					play("walkLeft");
					pos.x = nextStep;
					return pos;
				}
				else if(dir == Right && x < bounds.width-width)
				{
					nextStep = x + tileWidth;
					play("walkRight");
					pos.x = nextStep;
					return pos;
				}
				else if(dir == Up  && y > bounds.y)
				{
					nextStep = y - tileWidth;
					play("walkUp");
					pos.y = nextStep;
					return pos;
				}
				else if(dir == Down && y < bounds.height-height)
				{
					nextStep = y + tileWidth;
					play("walkDown");
					pos.y = nextStep;
					return pos;
				}
				else
				{
					moving = Stand;
					return null;
				}				
			}
			else
				return null;
		}
		
		public function moveXY(nx:int, ny:int):FlxPoint
		{
			var pos:FlxPoint = new FlxPoint(nx, ny);
			
			if(nx > x)
				pos = move(Right);
			else if(nx < x)
				pos = move(Left);
			
			if(ny > y)
				pos = move(Down);
			else if(ny < y)
				pos = move(Up);
			
			return pos;
		}
	}
}