package com.appwarp.s2;

import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.appwarp.s2.quizmania.R;
import com.appwarp.s2.util.Utils;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.AllRoomsEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.AllUsersEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveUserInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.MatchedRoomsEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.RoomRequestListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.ZoneRequestListener;

public class QuizActivity extends Activity implements ConnectionRequestListener
		 {

	private Spinner topicSpinner;
	private Spinner typeSpinner;
	private TextView connectTV;
	private EditText nameET;
	private ArrayAdapter<CharSequence> topicAdapter;
	private ArrayAdapter<CharSequence> typeAdapter;
	private WarpClient theClient;
	protected String roomId;
	protected int roomSize=-1;
	public ProgressDialog progressDialog;
	protected Intent myintent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quiz_connect);
		topicSpinner = (Spinner) findViewById(R.id.topicSpinner);
		typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
		connectTV = (TextView) findViewById(R.id.connectTV);
		nameET = (EditText) findViewById(R.id.nameET);
		initializeWarp();
		topicAdapter = ArrayAdapter.createFromResource(this,
				R.array.quiz_topic, android.R.layout.simple_spinner_item);
		topicAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		topicSpinner.setAdapter(topicAdapter);

		typeAdapter = ArrayAdapter.createFromResource(this, R.array.quiz_type,
				android.R.layout.simple_spinner_item);
		typeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		typeSpinner.setAdapter(typeAdapter);

		connectTV.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				if (nameET.getText().toString().trim().equalsIgnoreCase("")) {
					Utils.showDialog(QuizActivity.this, "Please enter username.");
				} else {
					if(typeSpinner.getSelectedItem().toString().equalsIgnoreCase("Single Player"))
						roomSize=1;
					else
						roomSize=2;
					progressDialog =  ProgressDialog.show(QuizActivity.this, "", "Connecting...");
					progressDialog.setCancelable(true);
					theClient.connectWithUserName(nameET.getText().toString()
							.trim(), "");
				}

			}
		});
	}

	private void initializeWarp() {
		try {
			WarpClient.initialize(Utils.API_KEY, Utils.HOST_NAME);
			WarpClient.setRecoveryAllowance(Utils.RECCOVERY_ALLOWANCE_TIME);
			theClient = WarpClient.getInstance();
		} catch (Exception ex) {
			Utils.showToastAlert(this, Utils.ALERT_INIT_EXEC);
		}

	}

	@Override
	public void onStart() {
		super.onStart();
		theClient.addConnectionRequestListener(this);
		

	}

	@Override
	public void onStop() {
		super.onStop();
		theClient.removeConnectionRequestListener(this);



	}

	@Override
	public void onConnectDone(final ConnectEvent event, String desc) {
		// TODO Auto-generated method stub
		System.out.println("connection done event.getResult()"+event.getResult());
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(progressDialog!=null && progressDialog.isShowing()){
					progressDialog.dismiss();
					progressDialog = null;
				}
				if (event.getResult() == WarpResponseResultCode.SUCCESS) {
					Utils.username=nameET.getText().toString().trim();
					 myintent = new Intent(QuizActivity.this, PlayScreen.class);
					 myintent.putExtra("QuizTopic", topicSpinner.getSelectedItem().toString());
					 myintent.putExtra("QuizType", roomSize);
	                 startActivity(myintent);
	                 finish();
				} else if (event.getResult() == WarpResponseResultCode.BAD_REQUEST) {
					
					Utils.showToastAlert(QuizActivity.this,
							Utils.ALERT_CONN_FAIL + event.getResult());
					
				} else {
				
					Utils.showToastAlert(QuizActivity.this,
							Utils.ALERT_CONN_FAIL + event.getResult());
				}

			}
		});
	}

	@Override
	public void onDisconnectDone(ConnectEvent event) {
		// TODO Auto-generated method stub
		if (event.getResult() == WarpResponseResultCode.SUCCESS) {

		} else {
			Utils.showToastAlertOnUIThread(QuizActivity.this,
					Utils.ALERT_ERR_DISCONN);
		}
	}

	@Override
	public void onInitUDPDone(byte result) {
		// TODO Auto-generated method stub

	}


}
