package appwarp.s2.cards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.ChatEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LobbyData;
import com.shephertz.app42.gaming.multiplayer.client.events.MoveEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomData;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.UpdateEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.NotifyListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.RoomRequestListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.TurnBasedRoomListener;
import com.shephertz.app42.gaming.multiplayer.client.util.Util;

public class GameActivity extends Activity implements ConnectionRequestListener, RoomRequestListener, TurnBasedRoomListener, NotifyListener {

	private RelativeLayout mainLayout;
	private int SCREEN_WIDTH;
	private int SCREEN_HEIGHT;
	private int cardWidth;
	private int cardHeight;
	private int selectedCardId = -1;
	private int selectedCardIndex = -1;
	private CardView[] cardViewArray = new CardView[9];
	private CardView selectedCardView = null;
	
	private String roomId = null;
	
	private int gameType = -1;
	private WarpClient theClient;
	private ProgressDialog progressDialog;
	
	private ImageView reqNewCardView;
	private ImageView topCardView;
	private TextView turnTextView;
	private Dialog gameStatusDialog;
	private Handler handler = new Handler();
	
	private int cardStartXX = 0;
	private int cardStartYY = 0;
	
	private int recoveryCount=0;
	
	// Game Data
	
	private boolean isUserTurn = false;
	
	private boolean isUserActionDone = false;
	private boolean isNewCardSelected = false;
	private boolean isTopCardSelected = false;
	
	private byte GAME_STATUS;
	
	private int TOP_CARD = -1;
	private int REQUESTED_CARD = -1;
	
	Bitmap[] cards;
    private ArrayList<Integer> USER_CARD = new ArrayList<Integer>();
	
	@Override
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		Display display = getWindowManager().getDefaultDisplay();
		reqNewCardView = (ImageView)findViewById(R.id.newCard);
		reqNewCardView.setBackgroundResource(R.drawable.cardback);
		topCardView = (ImageView)findViewById(R.id.topCard);
		topCardView.setBackgroundResource(R.drawable.cardback);
		turnTextView = (TextView)findViewById(R.id.turnTextView);
		Point size = new Point();
		if(display!=null){
			if(Build.VERSION.SDK_INT>Build.VERSION_CODES.HONEYCOMB_MR2){
				display.getSize(size);
			}else{
				size.x = display.getWidth();
				size.y = display.getHeight();
			}
		}
		SCREEN_WIDTH = size.x;
		SCREEN_HEIGHT = size.y;
		Log.d("SCREEN_WIDTH", String.valueOf(SCREEN_WIDTH));
		Log.d("SCREEN_HEIGHT", String.valueOf(SCREEN_HEIGHT));
		roomId = getIntent().getStringExtra("roomId");
		gameType = getIntent().getIntExtra("gameType", -1);
		getWarpInstance();
		mainLayout = (RelativeLayout)findViewById(R.id.gameActivityView);
		GAME_STATUS = Constants.STOPPED;// default game status is stopped
		theClient.addConnectionRequestListener(this);
		theClient.addRoomRequestListener(this);
		theClient.addTurnBasedRoomListener(this);
		theClient.addNotificationListener(this);
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		theClient.addConnectionRequestListener(this);
		theClient.addRoomRequestListener(this);
		theClient.addTurnBasedRoomListener(this);
		theClient.addNotificationListener(this);
		progressDialog = ProgressDialog.show(this, "", "Waiting to start game");
		progressDialog.setCancelable(true);
		
	}
	
	@Override
	protected void onStop(){
		super.onStop();
		theClient.removeTurnBasedRoomListener(this);
		theClient.removeRoomRequestListener(this);
		
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		theClient.removeConnectionRequestListener(this);
		theClient.removeNotificationListener(this);
	}
	
	private void getWarpInstance(){
        try {
            theClient = WarpClient.getInstance();
        } catch (Exception ex) {
        	Utils.showToastAlert(this, Constants.ALERT_INIT_EXEC);
        }
    }
	
	private void initScreen(){
		int cardBlockWidth = SCREEN_WIDTH/9;
		cards = Utils.getCardsBitmapArray(this, cardBlockWidth);
		
		cardWidth = cards[0].getWidth();
		cardHeight = cards[0].getHeight();
		Log.d("cardWidth", String.valueOf(cardWidth));
		Log.d("cardHeight", String.valueOf(cardHeight));
		cardWidth = cardBlockWidth;
		for(int i=0;i<USER_CARD.size();i++){// Drawing from TOP Left
			cardStartYY = SCREEN_HEIGHT-(cardHeight);
			final int selectedIndex = i;
			cardViewArray[i] = new CardView(this, USER_CARD.get(i), (i*cardWidth), cardStartYY);
			cardViewArray[i].setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(!isUserTurn){
						Utils.showToastAlert(GameActivity.this, Constants.ALERT_INV_MOVE);
						return false;
					}
					CardView cardView = (CardView)v;
					if(event.getAction()==MotionEvent.ACTION_DOWN){
						selectedCardIndex = selectedIndex;
						selectedCardId = cardView.getId();
					}else if(event.getAction()==MotionEvent.ACTION_UP){
						
					}else if(event.getAction()==MotionEvent.ACTION_MOVE){
						
					}
					return false;
				}
			});
			cardViewArray[i].setImageBitmap(cards[(USER_CARD.get(i)-1)]);
			// image array starts from zero
			mainLayout.addView(cardViewArray[i]);
		}
	}
	
	private void setCardInImageView(ImageView view, int index){
		BitmapDrawable b = new BitmapDrawable(cards[index-1]);
		view.setBackgroundDrawable(b);
	}
	
	private void setBitmapInImageView(ImageView view, Bitmap bmp){
		BitmapDrawable b = new BitmapDrawable(bmp);
		view.setBackgroundDrawable(b);
	}
	
	private void popUpCard(ImageView view, int index){
		if(selectedCardView!=null && mainLayout.indexOfChild(selectedCardView)!=-1){
			mainLayout.removeView(selectedCardView);
		}
		if(index!=-1){
			Bitmap bitmap = cards[index-1];
			selectedCardView = new CardView(this, index, (int)0, (int)view.getY());
			selectedCardView.setImageBitmap(bitmap);
			mainLayout.addView(selectedCardView);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent me){
		if(!isUserTurn){
			Utils.showToastAlert(this, Constants.ALERT_INV_MOVE);
			return false;
		}
		if(me.getAction()==MotionEvent.ACTION_DOWN){
			/*
			 * when user select any card from existing card list
			 */
			if(selectedCardIndex!=-1){
				Bitmap bitmap = ((BitmapDrawable)cardViewArray[selectedCardIndex].getDrawable()).getBitmap();
				selectedCardView = new CardView(this, 0, (int)cardViewArray[selectedCardIndex].getX(), (int)cardViewArray[selectedCardIndex].getY());
				selectedCardView.setImageBitmap(bitmap);
				mainLayout.addView(selectedCardView);
			}
		}else if(me.getAction()==MotionEvent.ACTION_UP){
			int releaseCardIndex = -1;
			if(selectedCardIndex!=-1 || TOP_CARD!=-1 || REQUESTED_CARD!=-1){
				//check for valid release index
				for(int i=0;i<cardViewArray.length;i++){
					if(me.getX()>(cardStartXX+(i*cardWidth)) && me.getX()<((i*cardWidth)+cardWidth+cardStartXX) &&
							me.getY()>cardStartYY && me.getY()<(cardStartYY+cardHeight)){
						releaseCardIndex = i;
						break;
					}
				}
				/*
				 * When user swap card from existing card list
				 * Result: Swap Cards
				 */
				if(!isNewCardSelected && !isTopCardSelected){
					if(releaseCardIndex!=-1 && selectedCardIndex!=releaseCardIndex){
						Bitmap selectedBitMap = ((BitmapDrawable)cardViewArray[selectedCardIndex].getDrawable()).getBitmap();
						Bitmap releasedBitMap = ((BitmapDrawable)cardViewArray[releaseCardIndex].getDrawable()).getBitmap();
						cardViewArray[selectedCardIndex].setImageBitmap(releasedBitMap);
						cardViewArray[releaseCardIndex].setImageBitmap(selectedBitMap);
						selectedBitMap = null;
						releasedBitMap = null;
						int sel = USER_CARD.get(selectedCardIndex);
						int rel = USER_CARD.get(releaseCardIndex);
						USER_CARD.set(selectedCardIndex, rel);
						USER_CARD.set(releaseCardIndex, sel);
					}else{
						resetCard(selectedCardIndex);// reset card on its previous position
					}
				}
				// if user select new card and replace it with his card
				else if(isNewCardSelected){
					if(releaseCardIndex!=-1){
						isUserActionDone = true;
						Bitmap selectedBitMap = ((BitmapDrawable)selectedCardView.getDrawable()).getBitmap();
						Bitmap releasedBitMap = ((BitmapDrawable)cardViewArray[releaseCardIndex].getDrawable()).getBitmap();
						reqNewCardView.setBackgroundResource(R.drawable.cardback);
						setBitmapInImageView(topCardView, releasedBitMap);
						TOP_CARD = cardViewArray[releaseCardIndex].getId();
						cardViewArray[releaseCardIndex].setImageBitmap(selectedBitMap);
						USER_CARD.set(releaseCardIndex, selectedCardView.getId());
						isNewCardSelected = false;
						selectedBitMap = null;
						releasedBitMap = null;
					}else{// place new card as top card
						isUserActionDone = true;
						Bitmap selectedBitMap = ((BitmapDrawable)selectedCardView.getDrawable()).getBitmap();
						setBitmapInImageView(topCardView, selectedBitMap);
						TOP_CARD = REQUESTED_CARD;
						reqNewCardView.setBackgroundResource(R.drawable.cardback);
						isNewCardSelected = false;
					}
				}
				// if user select top card
				else if(isTopCardSelected && TOP_CARD!=-1){
					if(releaseCardIndex!=-1){
						isUserActionDone = true;
						isTopCardSelected = false;
						Bitmap selectedBitMap = ((BitmapDrawable)selectedCardView.getDrawable()).getBitmap();
						Bitmap releasedBitMap = ((BitmapDrawable)cardViewArray[releaseCardIndex].getDrawable()).getBitmap();
						setBitmapInImageView(topCardView, releasedBitMap);
						TOP_CARD = cardViewArray[releaseCardIndex].getId();
						cardViewArray[releaseCardIndex].setImageBitmap(selectedBitMap);
						USER_CARD.set(releaseCardIndex, selectedCardView.getId());
//						Log.d("USER_CARD", USER_CARD+"");
						selectedBitMap = null;
						releasedBitMap = null;
					}else{// place new card as top card
						// do nothing
						isTopCardSelected = false;
					}
				}
				mainLayout.removeView(selectedCardView);
				selectedCardView = null;
				selectedCardIndex = -1;
				selectedCardId = -1;
				releaseCardIndex = -1;
				REQUESTED_CARD = -1;
			}
		}else if(me.getAction()==MotionEvent.ACTION_MOVE){
			if(selectedCardView!=null){
				selectedCardView.setX(me.getX());
				selectedCardView.setY(me.getY());
			}
		}
		return true;
	}
	
	private void resetCard(int id){
		if(id!=-1){
			cardViewArray[id].setX(cardViewArray[id].getInitX());
			cardViewArray[id].setY(cardViewArray[id].getInitY());
		}
	}
	
	public void onRequestNewCard(View view){
		if(!isUserTurn){
			Utils.showToastAlert(this, Constants.ALERT_INV_MOVE);
			return;
		}
		if(isUserActionDone){
			Utils.showToastAlert(this, "Invalid Move");
			return;
		}
		if(REQUESTED_CARD==-1){
			Log.d("getNewCard", "new card requested");
			theClient.invokeRoomRPC(roomId, "requestNewCard", Utils.userName);
		}else{
			selectedCardId = REQUESTED_CARD;
			popUpCard(reqNewCardView, REQUESTED_CARD);
		}
	}
	
	public void onTopCardSelected(View view){
		if(!isUserTurn){
			Utils.showToastAlert(this, Constants.ALERT_INV_MOVE);
			return;
		}
		if(REQUESTED_CARD!=-1){
			Utils.showToastAlert(this, "Invalid Move");
			return;
		}
		if(isUserActionDone){
			Utils.showToastAlert(this, "Invalid Move");
			return;
		}
		selectedCardId = TOP_CARD;
		isTopCardSelected = true;
		popUpCard(topCardView, TOP_CARD);
	}
	
	public void onSubmitCardsClicked(View view){
		if(!isUserTurn){
			Utils.showToastAlert(this, Constants.ALERT_INV_MOVE);
			return;
		}else{
			try {
				JSONArray data = new JSONArray(USER_CARD);
				theClient.sendChat(data.toString());
			} catch (Exception e){
				Log.d("onSubmitCardsClicked", e.toString());
			}
		}
	}
	
	public void onSendMoveClicked(View view){
		if(!isUserTurn){
			Utils.showToastAlert(this, Constants.ALERT_INV_MOVE);
			return;
		}else{
			if(isUserActionDone){
				try {
					JSONObject object = new JSONObject();
					object.put("top", TOP_CARD);
					object.put("cards", new JSONArray(USER_CARD));
					Log.d("onSendMoveClicked", object.toString());
					theClient.sendMove(object.toString());
				} catch (JSONException e) {
					Log.d("onSendMoveClicked", e.toString());
				}
			}else{
				Utils.showToastAlert(this, "Please complete your turn");
			}
		}
	}
	
	private void handleMessage(int code, String data){
		if(code==Constants.RESULT_USER_LEFT){
			showNotificationDialog(data);
		}else if(code==Constants.RESULT_GAME_OVER){
			roomId = null;
			try {
				showResultDialog(data);
			} catch (JSONException e) {
				Log.d("handleGameOver:showResultDialog", e.toString());
			}
		}
	}
	
	private void handleGamePause(String username){
		// handleGamePause
		if(gameStatusDialog==null){
			gameStatusDialog = new Dialog(this);
		}
		gameStatusDialog.setContentView(R.layout.custom_dialog);
		gameStatusDialog.setTitle(Constants.SERVER_NAME);
	 	TextView text = (TextView) gameStatusDialog.findViewById(R.id.dialogText);
		text.setText("This Game is stopped by "+username +" because all user's are" +
				" not online this time.");
		Button dialogButton = (Button) gameStatusDialog.findViewById(R.id.dialogOKButton);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utils.showToastAlert(GameActivity.this, "Game Paused");
			}
		});
		gameStatusDialog.show();
	}
	
	private void showNotificationDialog(String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
		       .setCancelable(false)
		       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                onBackPressed();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void showResultDialog(String data) throws JSONException{
		Log.d("showResultDialog", data);
		JSONObject message = new JSONObject(data);
		String userWin = message.getString("win");
		JSONArray cards = message.getJSONArray("cards");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LinearLayout outerLayout = new LinearLayout(this);
		outerLayout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout innerLayout = new LinearLayout(this);
		innerLayout.setOrientation(LinearLayout.HORIZONTAL);
		HorizontalScrollView scroll = new HorizontalScrollView(this);
		scroll.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
		                                             LayoutParams.WRAP_CONTENT));
		TextView txtView = new TextView(this);
		txtView.setText("Winning User: "+userWin);
		for(int i=0;i<cards.length();i++){
			int num = cards.getInt(i);
			CardView view = new CardView(this, num);
			view.setImageBitmap(this.cards[(num-1)]);
			innerLayout.addView(view);
		}
		outerLayout.addView(txtView);
		scroll.addView(innerLayout);
		outerLayout.addView(scroll);
		builder.setView(outerLayout);
		builder.setMessage("Game Result")
		       .setCancelable(false)
		       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	    onBackPressed();
		           }
		       });
		builder.show();
	}
	
	@Override
	public void onBackPressed() {
		handleLeave();
		super.onBackPressed();
	}
	
	private void handleLeave(){
		Log.d("handleLeave", "called");
		if(roomId!=null){
			theClient.leaveRoom(roomId);
			roomId = null;
		}
		theClient.disconnect();
	}
	
	//------------ Notification Start Here ---------------------	
	
	@Override
	public void onChatReceived(ChatEvent event) {
		Log.d("onChatReceived", event.getSender()+event.getMessage());
		if(event.getSender().equals(Constants.SERVER_NAME)){
			if(event.getMessage().indexOf('#')!=-1){
				int hashIndex = event.getMessage().indexOf('#');
				final String code = event.getMessage().substring(0, hashIndex);
				final String message = event.getMessage().substring(hashIndex+1, event.getMessage().length());
				try {
					final int CODE = Integer.parseInt(code);
					if(CODE==Constants.SUBMIT_CARD){
						Utils.showToastAlertOnUIThread(this, message);
					}else if(CODE==Constants.USER_HAND){
						JSONObject object = new JSONObject(message);
						JSONArray cardArray = object.getJSONArray(Utils.userName);
						for(int i=0;i<cardArray.length();i++){
			                USER_CARD.add((Integer)cardArray.get(i));
			            }
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								initScreen();
							}
						});
					}else if(CODE==Constants.RESULT_GAME_OVER || CODE==Constants.RESULT_USER_LEFT){
						GAME_STATUS = Constants.STOPPED;
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								handleMessage(CODE, message);
							}
						});
					}
				} catch (NumberFormatException e) {
					Log.d("GameActivity", "onChatReceived:NumberFormatException");
				}catch (JSONException e) {
					Log.d("GameActivity", "onChatReceived:JSONException");
				}
			}
		}
	}

	@Override
	public void onGameStarted(String sender, String roomId, final String nextTurn) {
		Log.d("onGameStarted: ", "sender: "+sender+" roomId: "+roomId+" nextTurn: "+nextTurn);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(GAME_STATUS==Constants.STOPPED){
					if(progressDialog!=null){
						progressDialog.dismiss();
						progressDialog=null;
					}
					GAME_STATUS = Constants.RUNNING;
				}else if(GAME_STATUS==Constants.PAUSED){
					if(gameStatusDialog!=null){
						gameStatusDialog.dismiss();
					}
					GAME_STATUS = Constants.RUNNING;
				}
				if(nextTurn.equals(Util.userName)){
					isUserTurn = true;
					turnTextView.setText("Your Turn");
				}else{
					isUserTurn = false;
					turnTextView.setText("Turn: "+nextTurn);
				}
			}
		});
	}

	@Override
	public void onGameStopped(final String sender, final String data) {
		Log.d("onGameStopped", "sender: "+sender+ "data: "+data);
		if(sender.equals(Constants.SERVER_NAME)){
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if(GAME_STATUS==Constants.RUNNING){
						GAME_STATUS = Constants.PAUSED;
						handleGamePause(sender);
					}
				}
			});
		}
	}

	@Override
	public void onMoveCompleted(final MoveEvent me) {
		Log.d("onMoveCompleted", "Sender: "+me.getSender()
				+" Next: "+me.getNextTurn()+" Data: "+me.getMoveData());
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(me.getNextTurn().equals(Utils.userName)){
					isUserTurn = true;
					isUserActionDone = false;
					turnTextView.setText("Your Turn");
				}else{
					isUserTurn = false;
					turnTextView.setText("Turn: "+me.getNextTurn());
				}
				try{
					if(me.getMoveData().trim().length()>0){
						JSONObject object = new JSONObject(me.getMoveData());
						TOP_CARD = object.getInt("top");
						REQUESTED_CARD = -1;
						setCardInImageView(topCardView, TOP_CARD);
					}else{
						Log.d("onMoveCompleted:me.getMoveData()", me.getMoveData());
					}
				}catch(JSONException e){
					Log.d("GameActivity", "onMoveCompleted:JSONException");
				}
			}
		});
	}

	@Override
	public void onPrivateChatReceived(String arg0, String arg1) {
		
		
	}

	@Override
	public void onRoomCreated(RoomData arg0) {
		
		
	}

	@Override
	public void onRoomDestroyed(RoomData roomData) {
		Log.d("onRoomDestroyed", roomData.getId());
	}

	@Override
	public void onUpdatePeersReceived(UpdateEvent arg0) {
		
		
	}

	@Override
	public void onUserChangeRoomProperty(RoomData arg0, String arg1,
			HashMap<String, Object> arg2, HashMap<String, String> arg3) {
		
	}

	@Override
	public void onUserJoinedLobby(LobbyData arg0, String arg1) {
		
	}

	@Override
	public void onUserJoinedRoom(RoomData arg0, String arg1) {
		
	}

	@Override
	public void onUserLeftLobby(LobbyData arg0, String arg1) {
		
	}

	@Override
	public void onUserLeftRoom(RoomData data, String name) {
		
	}

	@Override
	public void onUserPaused(String arg0, boolean arg1, String arg2) {
		
	}

	@Override
	public void onUserResumed(String arg0, boolean arg1, String arg2) {
		
	}
//------------ Notification End Here ---------------------
	
	@Override
	public void onGetMoveHistoryDone(byte arg0, MoveEvent[] arg1) {
		
		
	}

	@Override
	public void onSendMoveDone(byte result, String desc){
		if(result==WarpResponseResultCode.SUCCESS){
			isUserActionDone = false;
		}else {
			Utils.showToastAlertOnUIThread(GameActivity.this, Constants.ALERT_SEND_FAIL + result);
		}
	}

	@Override
	public void onStartGameDone(byte result, String data) {
		
		
	}

	@Override
	public void onStopGameDone(byte arg0, String arg1) {
		
		
	}

	@Override
	public void onGetLiveRoomInfoDone(LiveRoomInfoEvent arg0) {
		
		
	}

	@Override
	public void onJoinRoomDone(RoomEvent event, String desc) {
		
	}

	@Override
	public void onLeaveRoomDone(RoomEvent event) {
		Log.d("onLeaveRoomDone", event.getResult()+"");
		if(event.getData()!=null && event.getData().getId().equals(roomId)){
			roomId = null;
		}
	}

	@Override
	public void onLockPropertiesDone(byte arg0) {
		
		
	}

	@Override
	public void onSetCustomRoomDataDone(LiveRoomInfoEvent arg0) {
		
		
	}

	@Override
	public void onSubscribeRoomDone(RoomEvent event) {
		
	}

	@Override
	public void onUnSubscribeRoomDone(RoomEvent event) {
		
		
	}

	@Override
	public void onUnlockPropertiesDone(byte result) {
		
		
	}

	@Override
	public void onUpdatePropertyDone(LiveRoomInfoEvent arg0, String arg1) {
		
		
	}

	@Override
	public void onConnectDone(ConnectEvent event, String desc) {
		Log.d("onConnectDone", ""+event.getResult());
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(progressDialog!=null){
					progressDialog.dismiss();
					progressDialog = null;
				}
			}
		});
		if(event.getResult() == WarpResponseResultCode.SUCCESS){
        	Utils.showToastAlertOnUIThread(GameActivity.this, Constants.ALERT_CONN_SUCC);
        }
        else if(event.getResult() == WarpResponseResultCode.SUCCESS_RECOVERED){
        	Utils.showToastAlertOnUIThread(GameActivity.this, Constants.ALERT_CONN_RECOVERED);
        }
        else if(event.getResult() == WarpResponseResultCode.CONNECTION_ERROR_RECOVERABLE){
        	if(recoveryCount>=Constants.MAX_RECOVERY_ATTEMPT){
        		Utils.showToastAlertOnUIThread(this, Constants.ALERT_CONN_FAIL);
        		runOnUiThread(new Runnable() {
					@Override
					public void run() {
						onBackPressed();
					}
				});
        		return;
        	}
        	runOnUiThread(new Runnable() {
				@Override
				public void run() {
					progressDialog = ProgressDialog.show(GameActivity.this, "", Constants.ALERT_CONN_ERR_RECOVABLE);
				}
			});
        	handler.postDelayed(new Runnable() {
                @Override
                public void run() {    
                	recoveryCount++;
                	if(progressDialog!=null){
                		progressDialog.setMessage(Constants.RECOVER_TEXT);
                	}
                    theClient.RecoverConnection();
                }
            }, 5000);
        }
        else{
        	Utils.showToastAlertOnUIThread(GameActivity.this, Constants.ALERT_CONN_ERR_NON_RECOVABLE+event.getResult());
        }
	}

	@Override
	public void onDisconnectDone(ConnectEvent event) {
//		Utils.showToastAlertOnUIThread(GameActivity.this, "GameActivity: onDisconnectDone: "+event.getResult());
	}

	@Override
	public void onInitUDPDone(byte event) {
		
	}

	@Override
	public void onRPCDone(byte result, String function, Object returnValue) {
		if(result==WarpResponseResultCode.SUCCESS){
			isNewCardSelected = true;
			REQUESTED_CARD = (Integer)returnValue;
			runOnUiThread(new Runnable(){
				@Override
				public void run() {
					setCardInImageView(reqNewCardView, REQUESTED_CARD);
				}
			});
		}else{
			Utils.showToastAlertOnUIThread(this, "RPC Request Failed");
		}
	}
	
}