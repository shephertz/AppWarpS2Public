package com.appwarp.s2;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.appwarp.s2.quizmania.R;
import com.appwarp.s2.util.QuestionPacket;
import com.appwarp.s2.util.QuizElement;
import com.appwarp.s2.util.QuizQuestion;
import com.appwarp.s2.util.QuizRequestCode;
import com.appwarp.s2.util.QuizResponseCode;
import com.appwarp.s2.util.Utils;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.AllRoomsEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.AllUsersEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.ChatEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveUserInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LobbyData;
import com.shephertz.app42.gaming.multiplayer.client.events.MatchedRoomsEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.MoveEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomData;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.UpdateEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.NotifyListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.RoomRequestListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.ZoneRequestListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PlayScreen extends Activity implements OnClickListener,
		ConnectionRequestListener, NotifyListener, RoomRequestListener,
		ZoneRequestListener {
	
	private TextView levelTV;
	private TextView myNameTV;
	private TextView oppNameTV;
	private TextView myScoreTV;
	private TextView questionTV;
	private TextView oppScoreTV;
	private TextView optionATV;
	private TextView optionBTV;
	private TextView optionCTV;
	private TextView optionDTV;
	private TextView timeValueTV;
	private int roomSize;
	private HashMap<String, Object> table;
	private WarpClient theClient;
	private ProgressDialog progressDialog;
	private int levelNumber = 1;
	private int score = 0;
	private int totalScore=0;
	
	protected String roomId;
	private QuestionPacket quesPacket;
	private TextView timeTV;
	private int opponentScore;
	private int opponentTotalScore;
	private RelativeLayout headerLay;
	private LinearLayout questionLay;
	private TextView oppTotalScoreTV;
	private TextView myTotalScoreTV;
	protected int oppScore=0;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play);
		init();
		getWarpInstance();
		progressDialog = ProgressDialog.show(PlayScreen.this, "",
				"Connecting...");
		progressDialog.setCancelable(false);
		table = new HashMap<String, Object>();
		table.put("QuizTopic", getIntent().getStringExtra("QuizTopic"));
		roomSize = getIntent().getIntExtra("QuizType", 1);
		table.put("QuizType", roomSize);
		theClient.joinRoomWithProperties(table);
		myNameTV.setText(Utils.username);
		myScoreTV.setText("Score : " + score);
		myTotalScoreTV.setText("Total Score : " + totalScore);
		headerLay.setVisibility(View.GONE);
		questionLay.setVisibility(View.GONE);
		
		if (roomSize == 1) {
			oppNameTV.setVisibility(View.GONE);
			oppScoreTV.setVisibility(View.GONE);
			oppTotalScoreTV.setVisibility(View.GONE);
			timeValueTV.setVisibility(View.GONE);
			timeTV.setVisibility(View.GONE);

		} else {
			oppNameTV.setVisibility(View.VISIBLE);
			oppScoreTV.setVisibility(View.VISIBLE);
			timeValueTV.setVisibility(View.VISIBLE);
			timeTV.setVisibility(View.VISIBLE);
			oppTotalScoreTV.setVisibility(View.VISIBLE);
			
		}

	}

	private void init() {
		// TODO Auto-generated method stub
		levelTV = (TextView) findViewById(R.id.levelTV);
		myNameTV = (TextView) findViewById(R.id.myNameTV);
		oppNameTV = (TextView) findViewById(R.id.oppNameTV);
		myScoreTV = (TextView) findViewById(R.id.myScoreTV);
		oppScoreTV = (TextView) findViewById(R.id.oppScoreTV);
		myTotalScoreTV = (TextView) findViewById(R.id.myTotalScoreTV);
		oppTotalScoreTV = (TextView) findViewById(R.id.oppTotalScoreTV);
		questionTV = (TextView) findViewById(R.id.questionTV);
		timeValueTV = (TextView) findViewById(R.id.timeValueTV);
		timeTV = (TextView) findViewById(R.id.timeTV);
		questionLay = (LinearLayout) findViewById(R.id.questionLay);
		headerLay = (RelativeLayout) findViewById(R.id.headerLay);
		optionATV = (TextView) findViewById(R.id.optionATV);
		optionBTV = (TextView) findViewById(R.id.optionBTV);
		optionCTV = (TextView) findViewById(R.id.optionCTV);
		optionDTV = (TextView) findViewById(R.id.optionDTV);
	

		// levelTV.setText("levelNumber");
	}

	private void getWarpInstance() {
		try {
			theClient = WarpClient.getInstance();
		} catch (Exception ex) {
			Utils.showToastAlert(this, Utils.ALERT_INIT_EXEC);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		theClient.addConnectionRequestListener(this);
		theClient.addRoomRequestListener(this);
		theClient.addZoneRequestListener(this);
		theClient.addNotificationListener(this);
	}
 @Override
public void onBackPressed() {
	// TODO Auto-generated method stub
	super.onBackPressed();
	theClient.disconnect();
}
	@Override
	public void onStop() {
		super.onStop();
		theClient.removeConnectionRequestListener(this);
		theClient.removeRoomRequestListener(this);
		theClient.removeNotificationListener(this);
		theClient.removeZoneRequestListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

			if (roomSize == 1) {
				progressDialog = ProgressDialog.show(PlayScreen.this, "",
						"Waiting for next question...");
				progressDialog.setCancelable(false);
			}
			ByteBuffer buf = ByteBuffer.allocate(9);
			buf.put(QuizRequestCode.ANSWERPACKET);
			buf.putInt(quesPacket.getId());
			optionATV.setOnClickListener(null);
			optionBTV.setOnClickListener(null);
			optionCTV.setOnClickListener(null);
			optionDTV.setOnClickListener(null);
			switch (v.getId()) {

			case R.id.optionATV:

				buf.putInt(1);

				break;
			case R.id.optionBTV:

				buf.putInt(2);

				break;
			case R.id.optionCTV:

				buf.putInt(3);

				break;
			case R.id.optionDTV:

				buf.putInt(4);

				break;

			}
			theClient.sendUpdatePeers(buf.array());
		 
	}

	@Override
	public void onSubscribeRoomDone(RoomEvent event) {
		// TODO Auto-generated method stub
		if (event.getResult() == WarpResponseResultCode.SUCCESS) {
			System.out.println("QUIZ SUBSCRIBE ROOM SUCCESS");
			ByteBuffer buf = ByteBuffer.allocate(9);
			buf.put(QuizRequestCode.STARTQUIZ);
			theClient.sendUpdatePeers(buf.array());
		} else {
			theClient.disconnect();

			Utils.showToastAlert(PlayScreen.this,
					Utils.ALERT_CONN_FAIL + event.getResult());
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
				progressDialog = null;
			}
		}
	}

	@Override
	public void onUnSubscribeRoomDone(RoomEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onJoinRoomDone(final RoomEvent event, String desc) {
		// TODO Auto-generated method stub
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (event.getResult() == WarpResponseResultCode.SUCCESS) {
					roomId = event.getData().getId();
					theClient.subscribeRoom(event.getData().getId());
					System.out.println("QUIZ JOIN ROOM SUCCESS");
				} else if (event.getResult() == WarpResponseResultCode.CONNECTION_ERROR) {
					theClient.disconnect();
					if (progressDialog != null && progressDialog.isShowing()) {
						progressDialog.dismiss();
						progressDialog = null;
					}
				} else {
					System.out.println("QUIZ JOIN ROOM FAIL");
					table = new HashMap<String, Object>();
					table.put("QuizTopic",
							getIntent().getStringExtra("QuizTopic"));
					table.put("QuizType", roomSize);
					theClient.createTurnRoom(
							String.valueOf(System.currentTimeMillis()),
							"shephertz", roomSize, table, Utils.TURN_TIME);
				}
			}
		});
	}

	@Override
	public void onLeaveRoomDone(RoomEvent event) {
		

	}

	private void restart() {
		// TODO Auto-generated method stub
		Intent myintent = new Intent(PlayScreen.this, QuizActivity.class);
		startActivity(myintent);
		finish();

	}

	@Override
	public void onGetLiveRoomInfoDone(LiveRoomInfoEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSetCustomRoomDataDone(LiveRoomInfoEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onUpdatePropertyDone(LiveRoomInfoEvent event, String desc) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onLockPropertiesDone(byte result) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onUnlockPropertiesDone(byte result) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onRPCDone(byte result, String function, Object returnValue) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onDeleteRoomDone(RoomEvent event, String desc) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onGetAllRoomsDone(AllRoomsEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onCreateRoomDone(final RoomEvent event, String desc) {
		// TODO Auto-generated method stub
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (event.getResult() == WarpResponseResultCode.SUCCESS) {// if
					// room
					System.out.println("QUIZ CREATE ROOM SUCCESS"); // created
					// successfully

					theClient.joinRoom(event.getData().getId());
				} else {

					if (progressDialog != null && progressDialog.isShowing()) {
						progressDialog.dismiss();
						progressDialog = null;
					}
					theClient.disconnect();

				}
			}
		});
	}

	@Override
	public void onGetOnlineUsersDone(AllUsersEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onGetLiveUserInfoDone(LiveUserInfoEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSetCustomUserDataDone(LiveUserInfoEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onGetMatchedRoomsDone(MatchedRoomsEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onConnectDone(ConnectEvent event, String desc) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onDisconnectDone(ConnectEvent event) {
		// TODO Auto-generated method stub

			
				restart();
			
		
	}

	@Override
	public void onInitUDPDone(byte result) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onRoomCreated(RoomData event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onRoomDestroyed(RoomData event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onUserLeftRoom(RoomData event, String username) {
		// TODO Auto-generated method stub
		System.out.println("ON USER LEFT CALLED");
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				showDialog(PlayScreen.this, oppNameTV.getText().toString()+ " has left the room. You win !");
				restart();
			}
		});
		
	}
	  private void showDialog(Context ctx, String msg)

	    {
	        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

	        builder.setMessage(msg)
	                .setCancelable(false)

	                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int id) {
	                        //TOdo
	                        dialog.cancel();
	                     restart();
	                    }
	                });


	        AlertDialog diaglog = builder.create();
	        diaglog.show();
	    }
	@Override
	public void onUserJoinedRoom(RoomData event, final String username) {
		// TODO Auto-generated method stub
		System.out.println("User Joined" + username);
	
		if (!username.equalsIgnoreCase(Utils.username)) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					oppNameTV.setText(username);
					oppScoreTV.setText("Score :" + oppScore);
					oppTotalScoreTV.setText("Total Score :" + opponentTotalScore);
				}
			});
		}
	}

	@Override
	public void onUserLeftLobby(LobbyData event, String username) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onUserJoinedLobby(LobbyData event, String username) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onChatReceived(final ChatEvent event) {
		// TODO Auto-generated method stub
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				oppNameTV.setText(event.getMessage());
				oppScoreTV.setText("Score :" + score);
				oppTotalScoreTV.setText("Total Score :" + opponentTotalScore);
			}
		});

		System.out.println("CHAT RECEIVED");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}

	@Override
	public void onPrivateChatReceived(String sender, String message) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onUpdatePeersReceived(UpdateEvent event) {
		// TODO Auto-generated method stub
		ByteBuffer decodingBuf = ByteBuffer.wrap(event.getUpdate(), 0,
				event.getUpdate().length);
		final byte type = decodingBuf.get();
		System.out.println("UPDATE PEER TYPE :" + type);
	
		int payLoadSize;
		byte[] payLoadBytes;

		switch (type) {
		case QuizResponseCode.QUESTIONPACKET:

			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
				progressDialog = null;
			}
			levelNumber = decodingBuf.getInt();
			int lastAnswer = decodingBuf.getInt();
			payLoadSize = decodingBuf.getInt();
			payLoadBytes = new byte[payLoadSize];
			decodingBuf.get(payLoadBytes);
			decodeQuestionToJsonObject(payLoadBytes, payLoadSize, false);
			break;
		case QuizResponseCode.UPDATESCORE:
			payLoadSize = decodingBuf.getInt();
			payLoadBytes = new byte[payLoadSize];
			decodingBuf.get(payLoadBytes);
			decodeScoreToJsonObject(payLoadBytes, payLoadSize);
			break;
		case QuizResponseCode.LEVELSTART:
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
				progressDialog = null;
			}
			levelNumber = decodingBuf.getInt();
			payLoadSize = decodingBuf.getInt();
			payLoadBytes = new byte[payLoadSize];
			decodingBuf.get(payLoadBytes);
			decodeQuestionToJsonObject(payLoadBytes, payLoadSize, true);

			break;
		case QuizResponseCode.LEVELRESULT:
			levelNumber = decodingBuf.getInt();
			payLoadSize = decodingBuf.getInt();
			payLoadBytes = new byte[payLoadSize];
			decodingBuf.get(payLoadBytes);
			decodeLevelResultToJsonArray(payLoadBytes, payLoadSize,false);
			break;
		case QuizResponseCode.FINALRESULT:
			levelNumber = decodingBuf.getInt();
			payLoadSize = decodingBuf.getInt();
			payLoadBytes = new byte[payLoadSize];
			decodingBuf.get(payLoadBytes);
			decodeLevelResultToJsonArray(payLoadBytes, payLoadSize,true);
			break;
		}
	}

	private void decodeLevelResultToJsonArray(byte[] payLoadBytes,
			int payLoadSize, final boolean finalResult) {
		// TODO Auto-generated method stub
		try {
			JSONArray jsonArray = new JSONArray(new String(payLoadBytes, 0,
					payLoadSize));
			System.out.println("DEcode Final Result" + jsonArray.toString());
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json = jsonArray.getJSONObject(i);
				String username = json.getString("user");
				if (username.equalsIgnoreCase(Utils.username)) {
					score = json.getInt("currentLevelscore");
					totalScore = json.getInt("totalScore");
				} else {
					opponentScore = json.getInt("currentLevelscore");
					opponentTotalScore = json.getInt("totalScore");
				}

			}
	
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					myScoreTV.setText("Score :" + score);
					oppScoreTV.setText("Score :" + opponentScore);
					myTotalScoreTV.setText("Total Score :" + totalScore);
					oppTotalScoreTV.setText("Total Score :" + opponentTotalScore);
					if(finalResult){
					Intent myintent = new Intent(PlayScreen.this,
							ScoreScreen.class);
					myintent.putExtra("level", levelNumber);
					myintent.putExtra("currentScore", score);
					myintent.putExtra("roomSize", roomSize);
					myintent.putExtra("totalScore", totalScore);
					if (roomSize == 2) {
						myintent.putExtra("opponentCurrentScore",
								opponentScore);
						myintent.putExtra("opponentTotalScore",
								opponentTotalScore);
					}
					startActivity(myintent);
					finish();}

				}
			});

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void decodeScoreToJsonObject(byte[] payLoadBytes, int payLoadSize) {
		// TODO Auto-generated method stub
		try {
			final JSONObject jsonObject = new JSONObject(new String(
					payLoadBytes, 0, payLoadSize));
			System.out.println("DECODE" + jsonObject.toString());
			
			score = jsonObject.getInt("currentLevelscore");
			System.out.println("Score : " + score);
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						if (jsonObject.getString("user").equalsIgnoreCase(
								Utils.username))
						{	myScoreTV.setText("Score :" + score);
						
						totalScore=totalScore+score;
						myTotalScoreTV.setText("Total Score :" + totalScore);}
						else
						{	
							oppScoreTV.setText("Score :" + score);
						opponentScore=score;
						opponentTotalScore=opponentTotalScore+opponentScore;
						oppTotalScoreTV.setText("Total Score :" + opponentTotalScore);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void decodeQuestionToJsonObject(byte[] payLoadBytes,
			int payLoadSize, boolean levelStart) {
		// TODO Auto-generated method stub
		try {
			JSONObject jsonObject = new JSONObject(new String(payLoadBytes, 0,
					payLoadSize));
			JSONObject attachedElement = jsonObject.getJSONObject("Question")
					.getJSONObject("AttachedElement");
			QuizElement element = new QuizElement(QuizElement.Type.Text,
					attachedElement.getString("Value"));
			QuizQuestion ques = new QuizQuestion(jsonObject.getJSONObject(
					"Question").getString("Question"), element);
			ArrayList<QuizElement> optionList = new ArrayList<QuizElement>();
			JSONArray optionArray = jsonObject.getJSONArray("Options");
			for (int i = 0; i < optionArray.length(); i++) {
				JSONObject optionObject = optionArray.getJSONObject(i);
				QuizElement optionElement = new QuizElement(
						QuizElement.Type.Text, optionObject.getString("Value"));
				optionList.add(optionElement);
			}
			quesPacket = new QuestionPacket(jsonObject.getInt("id"), ques,
					optionList);
			UpdateUI(quesPacket, levelStart);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void UpdateUI(final QuestionPacket quesPacket,
			final boolean levelStart) {
		// TODO Auto-generated method stub
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				headerLay.setVisibility(View.VISIBLE);
				questionLay.setVisibility(View.VISIBLE);
				questionTV.setText(quesPacket.getQuestion().getQuestion());
				optionATV.setText(quesPacket.getOptions().get(0).Value);
				optionBTV.setText(quesPacket.getOptions().get(1).Value);
				optionCTV.setText(quesPacket.getOptions().get(2).Value);
				optionDTV.setText(quesPacket.getOptions().get(3).Value);
				optionATV.setOnClickListener(PlayScreen.this);
				optionBTV.setOnClickListener(PlayScreen.this);
				optionCTV.setOnClickListener(PlayScreen.this);
				optionDTV.setOnClickListener(PlayScreen.this);
				levelTV.setText("Level " + String.valueOf(levelNumber));
				if (levelStart) {
					myScoreTV.setText("Score :" + 0);

					oppScoreTV.setText("Score :" + 0);
				
				}
			}
		});

	}

	@Override
	public void onUserChangeRoomProperty(RoomData event, String username,
			HashMap<String, Object> properties,
			HashMap<String, String> lockProperties) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onMoveCompleted(MoveEvent moveEvent) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onGameStarted(String sender, String roomId, String nextTurn) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onGameStopped(String sender, String roomId) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onUserPaused(String locid, boolean isLobby, String username) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onUserResumed(String locid, boolean isLobby, String username) {
		// TODO Auto-generated method stub
	}
}