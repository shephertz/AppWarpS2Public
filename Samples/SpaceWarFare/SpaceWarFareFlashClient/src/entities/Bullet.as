package entities
{	
	import org.flixel.FlxGroup;
	import org.flixel.FlxPoint;
	import org.flixel.FlxSprite;
	
	public class Bullet extends FlxSprite
	{
		private var origin:FlxPoint = new FlxPoint;
		private var target:FlxPoint = new FlxPoint;
		private var container:FlxGroup;
		private var func:Function = null;
		
		public var Speed:int = 500;
		
		public function Bullet(group:FlxGroup,X:Number=0, Y:Number=0,X1:Number=0, Y1:Number=0,f:Function = null)
		{
			super(X, Y, null);
			
			loadGraphic(Assets.shotPNG,false,false,8,8);
			origin.x = X;
			origin.y = Y;
			target.x = X1;
			target.y = Y1;
			container = group;
			group.add(this);
			
			/*angle = FlxU.getAngle(new FlxPoint(X,Y),new FlxPoint(X1,Y1)) - 90;
			velocity.x = Math.cos(angle*(Math.PI/180))*Speed;
			velocity.y = Math.sin(angle*(Math.PI/180))*Speed;*/
			angle = Math.atan2(target.y-origin.y,target.x-origin.x);
			velocity.x = Math.cos(angle)*Speed;
			velocity.y = Math.sin(angle)*Speed;
			
			//TweensyZero.to(this,{x:target.x,y:target.y},0.5,Linear.easeIn,0,null,done);
			
			func = f;
		}
		
		private function done():void
		{
			container.remove(this);
			if(func != null)
				func(target.x-20, target.y-20);
			kill();
		}
		
		override public function update( ):void
		{
			super.update();
			
			if(origin.x > target.x && x < target.x)
			{
				done();
			}
			
			if(origin.x < target.x && x > target.x)
			{
				done();
			}
			
			if(origin.y > target.y && y < target.y)
			{
				done();
			}
			
			if(origin.y < target.y && y > target.y)
			{
				done();
			}
		}
		
	}
}
