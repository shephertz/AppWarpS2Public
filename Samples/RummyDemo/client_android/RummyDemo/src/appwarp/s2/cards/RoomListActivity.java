package appwarp.s2.cards;

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
import com.shephertz.app42.gaming.multiplayer.client.events.LiveUserInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.MatchedRoomsEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomData;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.ZoneRequestListener;


public class RoomListActivity extends Activity implements ZoneRequestListener {
	
	private WarpClient theClient;
	private RoomlistAdapter roomlistAdapter;
	private TextView textViewRoomSearch;
	private ListView listView;
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.room_list);
		listView = (ListView)findViewById(R.id.roomList);
		textViewRoomSearch = (TextView)findViewById(R.id.textViewRoomSearch);
		roomlistAdapter = new RoomlistAdapter(this);
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
		Log.d("RoomListActivity", "onStart called...");
		theClient.addZoneRequestListener(this);
		theClient.getRoomInRange(1, 2);
	}
	
	@Override
	public void onStop(){
		super.onStop();
		theClient.removeZoneRequestListener(this);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		Log.d("RoomListActivity", "onDestroy called...");
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		theClient.disconnect();
	}
	
	public void onJoinNewRoomClicked(View view){
		showSelectGameTypeSpinner();
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
					String roomId = event.getData().getId();
					startGame(roomId, event.getData().getMaxUsers());
					Log.d("onCreateRoomDone", event.getResult()+" id: "+roomId + " max: "+event.getData().getMaxUsers());
				}else{
					Utils.showToastAlert(RoomListActivity.this, Constants.ALERT_ROOM_CREATE + event.getResult());
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
	public void onGetMatchedRoomsDone(final MatchedRoomsEvent event) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(progressDialog!=null){
					progressDialog.dismiss();
					progressDialog = null; 
				}
				RoomData[] roomDataList = event.getRoomsData();
				if(roomDataList.length>0){
					textViewRoomSearch.setText("Please select any room");
					roomlistAdapter.setData(roomDataList);
					listView.setAdapter(roomlistAdapter);
				}else{
					textViewRoomSearch.setText("No room found");
					roomlistAdapter.clear();
				}
			}
		});
		
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
	
	private void showSelectGameTypeSpinner(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle(R.string.chooseGameType);
	    builder.setItems(R.array.gameType, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int index) {
            	   String spinnerData = getResources().getStringArray(R.array.gameType)[index].toString();
            	   if(index==0){
            		   progressDialog = ProgressDialog.show(RoomListActivity.this , "", "Pleaes wait...");
	           		   progressDialog.setCancelable(true);
	           		   theClient.createTurnRoom(String.valueOf(System.currentTimeMillis()), "shephertz", 2, null, Constants.TURN_TIME);
            	   }else if(index==1){
            		   progressDialog = ProgressDialog.show(RoomListActivity.this , "", "Pleaes wait...");
	           		   progressDialog.setCancelable(true);
	           		   theClient.createTurnRoom(String.valueOf(System.currentTimeMillis()), "shephertz", 3, null, Constants.TURN_TIME);
            	   }
            	   Log.d("showSelectGameTypeSpinner", spinnerData);
	           }
	    });
	    builder.create().show();
	}

	@Override
	public void onRPCDone(byte arg0, String arg1, Object arg2) {
		// TODO Auto-generated method stub
		
	}
}
