package appwarp.s2.cards;

import java.util.HashMap;
import java.util.Hashtable;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.AllRoomsEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.AllUsersEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveUserInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.MatchedRoomsEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomData;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.RoomRequestListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.ZoneRequestListener;


public class RoomSelectionActivity extends Activity implements ZoneRequestListener, RoomRequestListener {
	
	private WarpClient theClient;
	private ProgressDialog progressDialog;
	private int roomSize = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.room_selection);
		getWarpInstance();
	}
	
	private void getWarpInstance(){
        try {
            theClient = WarpClient.getInstance();
        } catch (Exception ex) {
        	Utils.showToastAlert(this, Constants.ALERT_INIT_EXEC);
        }
    }
	
	@Override
	public void onStart(){
		super.onStart();
		theClient.addZoneRequestListener(this);
		theClient.addRoomRequestListener(this);
		roomSize = -1;
	}
	
	@Override
	public void onStop(){
		super.onStop();
		theClient.removeZoneRequestListener(this);
		theClient.removeRoomRequestListener(this);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		theClient.disconnect();
	}
	
	public void onJoinTwoUserClicked(View view){
		roomSize = 2;
		HashMap<String, Object> table = new HashMap<String, Object>();
		table.put("twoUser", true);
		theClient.joinRoomWithProperties(table);
	}
	
	public void onJoinThreeUserClicked(View view){
		roomSize = 3;
		HashMap<String, Object> table = new HashMap<String, Object>();
		table.put("threeUser", true);
		theClient.joinRoomWithProperties(table);
	}
	
	@Override
	public void onCreateRoomDone(final RoomEvent event, String desc) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(progressDialog!=null){
					progressDialog.dismiss();
					progressDialog=null;
				}
				if(event.getResult()==WarpResponseResultCode.SUCCESS){// if room created successfully
					theClient.joinRoom(event.getData().getId());
				}else{
					Utils.showToastAlert(RoomSelectionActivity.this, Constants.ALERT_ROOM_CREATE + event.getResult());
				}
			}
		});
	}
	
	@Override
	public void onDeleteRoomDone(RoomEvent event, String desc) {
		
	}
	@Override
	public void onGetAllRoomsDone(AllRoomsEvent event) {
		
	}
	@Override
	public void onGetLiveUserInfoDone(LiveUserInfoEvent event) {
		
	}
	@Override
	public void onGetMatchedRoomsDone(final MatchedRoomsEvent event){
		
	}
	@Override
	public void onGetOnlineUsersDone(AllUsersEvent arg0) {
		
	}
	@Override
	public void onSetCustomUserDataDone(LiveUserInfoEvent arg0) {
		
	}
	
	public void startGame(String roomId, int maxUsers){
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra("roomId", roomId);
		intent.putExtra("gameType", maxUsers);
		startActivity(intent);
		finish();
	}
	
	@Override
	public void onRPCDone(byte arg0, String arg1, Object arg2) {
		
	}

	@Override
	public void onGetLiveRoomInfoDone(LiveRoomInfoEvent arg0) {
		
	}

	@Override
	public void onJoinRoomDone(final RoomEvent event, final String desc) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(event.getResult()==WarpResponseResultCode.SUCCESS){
					theClient.subscribeRoom(event.getData().getId());
				}else{
					HashMap<String, Object> table = new HashMap<String, Object>();
					if(roomSize==2){
						table.put("twoUser", true);
					}else if(roomSize==3){
						table.put("threeUser", true);
					}
					progressDialog = ProgressDialog.show(RoomSelectionActivity.this , "", "Creating room...");
		       		theClient.createTurnRoom(String.valueOf(System.currentTimeMillis()), "shephertz", roomSize, table, Constants.TURN_TIME);
				}
			}
		});
	}

	@Override
	public void onLeaveRoomDone(RoomEvent arg0) {
		
	}

	@Override
	public void onLockPropertiesDone(byte arg0) {
		
	}

	@Override
	public void onSetCustomRoomDataDone(LiveRoomInfoEvent arg0) {
		
	}

	@Override
	public void onSubscribeRoomDone(RoomEvent event) {
		if(event.getResult()==WarpResponseResultCode.SUCCESS){
			startGame(event.getData().getId(), event.getData().getMaxUsers());
		}else{
			Utils.showToastAlertOnUIThread(this, "onSubscribeRoomDone Failed"+event.getResult());
		}
		
	}

	@Override
	public void onUnSubscribeRoomDone(RoomEvent arg0) {
		
	}

	@Override
	public void onUnlockPropertiesDone(byte arg0) {
		
	}

	@Override
	public void onUpdatePropertyDone(LiveRoomInfoEvent arg0, String arg1) {
		
	}
}
