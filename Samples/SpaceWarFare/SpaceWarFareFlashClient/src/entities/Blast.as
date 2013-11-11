package entities
{
	import org.flixel.FlxSprite;
	import org.flixel.FlxState;
	import org.flixel.system.FlxAnim;
	
	public class Blast extends FlxSprite
	{
		public function Blast(obj:FlxState,X:Number=0, Y:Number=0)
		{
			super(X, Y, null);
			loadGraphic(Assets.blastPNG,true,false,40,40);
			addAnimation("blast",[0,1,2,3,4,5,6,7,8],10,false);
			play("blast");
			addAnimationCallback(blastAnimCallback);
			obj.add(this);
		}
		
		private function getFlxAnim():FlxAnim
		{
			if (_curAnim)
				return (_curAnim)
			else
				return null ;
		}
		
		private function blastAnimCallback(animationName:String, currentFrame:uint, currentFrameIndex:uint):void
		{
			var currFlxAnim:FlxAnim = getFlxAnim() ;
			if (currentFrame >= currFlxAnim.frames.length-1)
			{
					kill();
			}
		}
	}
}