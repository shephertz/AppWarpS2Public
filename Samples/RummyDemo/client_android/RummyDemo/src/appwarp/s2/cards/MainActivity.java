package appwarp.s2.cards;


import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import appwarp.s2.cards.facebook.FacebookProfileRequesterActivity;
import appwarp.s2.cards.facebook.FacebookService;
import appwarp.s2.cards.facebook.UserContext;

import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener;

public class MainActivity extends FacebookProfileRequesterActivity implements ConnectionRequestListener {

	private EditText nameEditText;
	private WarpClient theClient;
    public ProgressDialog progressDialog;
    private String authData = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		nameEditText = (EditText)findViewById(R.id.nameEditText);
		init();
	}
	
	public void onPlayGameClicked(View view){
		if(nameEditText.getText().length()==0){
			Utils.showToastAlert(this, getApplicationContext().getString(R.string.enterName));
			return;
		}
		String userName = nameEditText.getText().toString().trim();
		loginToAppWarp(userName, "");
		
	}
	
	private void loginToAppWarp(String name, String authData){
		Log.d("loginToAppWarp", ""+name);
		Utils.userName = name;
		theClient.connectWithUserName(name, authData);
		progressDialog =  ProgressDialog.show(this, "", "connecting to appwarp");
		progressDialog.setCancelable(true);
	}
	
	public void onFBClicked(View view){
		FacebookService.instance().setContext(getApplicationContext());
    	FacebookService.instance().fetchFacebookProfile(this);
	}
	
	private void init(){
		try {
			WarpClient.initialize(Constants.APP_KEY, Constants.HOST_NAME);
			WarpClient.setRecoveryAllowance(Constants.RECCOVERY_ALLOWANCE_TIME);
			theClient = WarpClient.getInstance();
		} catch (Exception ex) {
        	Utils.showToastAlert(this, Constants.ALERT_INIT_EXEC);
        }
    }
	
	@Override 
	public void onStart(){
		super.onStart();
		theClient.addConnectionRequestListener(this); 
	}
	
	@Override
	public void onStop(){
		super.onStop();
		theClient.removeConnectionRequestListener(this); 
		
	}
	
	public void goToRoomList(){
		Log.d("goToRoomList", "goToRoomList called");
		Intent intent = new Intent(getApplicationContext(), RoomSelectionActivity.class);
		startActivity(intent);
	}
	
	@Override
	public void onConnectDone(final ConnectEvent event, String desc) {
		Log.d("onConnectDone", event.getResult()+" listener "+desc);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(progressDialog!=null){
					progressDialog.dismiss();
					progressDialog = null;
				}
				if (event.getResult() == WarpResponseResultCode.SUCCESS) {
					goToRoomList();
				}else if (event.getResult() == WarpResponseResultCode.BAD_REQUEST) {
					theClient.disconnect();
				}else {
					Utils.showToastAlert(MainActivity.this, Constants.ALERT_CONN_FAIL + event.getResult());
				}
			}
		});
	}

	@Override
	public void onDisconnectDone(ConnectEvent event) {
		if (event.getResult() == WarpResponseResultCode.SUCCESS) {
			
		} else {
			Utils.showToastAlertOnUIThread(MainActivity.this, Constants.ALERT_ERR_DISCONN);
		}
	}

	@Override
	public void onInitUDPDone(final byte resultCode) {

	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.d("onActivityResult", "onActivityResult");
        FacebookService.instance().authorizeCallback(requestCode, resultCode, data);
    }
	
	public void onFacebookProfileRetreived(boolean success) {
//		Log.d("onFacebookProfileRetreived", ""+success);
		if(progressDialog!=null){
			progressDialog.dismiss();
			progressDialog = null;
		}
		if(success){
			// do success logic
			Log.d("UserContext.AccessToken", UserContext.AccessToken);
			Log.d("UserContext.MyUserName", UserContext.MyUserName);
			try {
				JSONObject data = new JSONObject();
				data.put("token", UserContext.AccessToken);
				data.put("userName", UserContext.MyUserName);
				loginToAppWarp(UserContext.MyUserName, data.toString());
			} catch (Exception e) {
				Utils.showToastAlert(this, "onFacebookProfileRetreived"+e.toString());
			}
		}
	}
	
}
