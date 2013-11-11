package
{
	import flash.events.Event;
	
	import org.flixel.FlxGame;
	
	import screens.Menu;
	
	[SWF(frameRate="30", width="800", height="480")]
	public class SpaceWarefare extends FlxGame
	{
		public function SpaceWarefare()
		{
			super(400,240,Menu,2);
		}
		
		override protected function create(FlashEvent:Event):void
		{
			super.create(FlashEvent);
			stage.removeEventListener(Event.DEACTIVATE, onFocusLost);
			stage.removeEventListener(Event.ACTIVATE, onFocus);
		}
	}
}