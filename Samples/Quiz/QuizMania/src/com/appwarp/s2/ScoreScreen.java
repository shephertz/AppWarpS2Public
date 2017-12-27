package com.appwarp.s2;

import com.appwarp.s2.quizmania.R;
import com.appwarp.s2.util.Utils;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;

public class ScoreScreen extends Activity implements ConnectionRequestListener {

	private TextView levelTV;
	private TextView oppCurrentScoreTV;
	private TextView currentScoreTV;
	private TextView totalScoreTV;
	private TextView oppTotalScoreTV;
	private TextView nextLevelTV;
	private int levelNumber=1;
	private int score=0;
	private int totalScore;
	private int roomSize;
	private TextView opponentTV;
	private WarpClient theClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scores);
		getWarpInstance();
		init();
		
		
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		theClient.addConnectionRequestListener(this);
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		theClient.removeConnectionRequestListener(this);
	}
	private void getWarpInstance() {
		try {
			theClient = WarpClient.getInstance();
		} catch (Exception ex) {
			Utils.showToastAlert(this, Utils.ALERT_INIT_EXEC);
		}
	}

	private void init() {
		// TODO Auto-generated method stub
		levelTV=(TextView)findViewById(R.id.levelTV);
		currentScoreTV=(TextView)findViewById(R.id.currentScoreTV);
		oppCurrentScoreTV=(TextView)findViewById(R.id.oppCurrentScoreTV);
		totalScoreTV=(TextView)findViewById(R.id.totalScoreTV);
		oppTotalScoreTV=(TextView)findViewById(R.id.oppTotalScoreTV);
		opponentTV=(TextView)findViewById(R.id.opponentTV);
		nextLevelTV=(TextView)findViewById(R.id.nextLevelTV);
		
		roomSize=getIntent().getIntExtra("roomSize", 1);
		totalScore=getIntent().getIntExtra("totalScore", 1);
		score=getIntent().getIntExtra("currentScore", 1);
		levelNumber=getIntent().getIntExtra("level", 1);
		levelTV.setText("Level : "+levelNumber);
		currentScoreTV.setText("Current level score : "+score);
		totalScoreTV.setText("Total score : "+totalScore);
		if(roomSize==1)
		{
			oppCurrentScoreTV.setVisibility(View.GONE);
			oppTotalScoreTV.setVisibility(View.GONE);
			opponentTV.setVisibility(View.GONE);
			
		}
		else
		{
			oppCurrentScoreTV.setVisibility(View.VISIBLE);
		
			oppTotalScoreTV.setVisibility(View.VISIBLE);
			opponentTV.setVisibility(View.VISIBLE);
			
			oppCurrentScoreTV.setText("Current level score : "+getIntent().getIntExtra("opponentCurrentScore", 0));
			oppTotalScoreTV.setText("Total score : "+getIntent().getIntExtra("opponentTotalScore", 0));
		}
		
		nextLevelTV.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				theClient.disconnect();
				  
				
			}
		});
		
	}
	@Override
	public void onConnectDone(ConnectEvent event, String desc) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onDisconnectDone(ConnectEvent event) {
		// TODO Auto-generated method stub
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent myintent = new Intent(ScoreScreen.this, QuizActivity.class);
                startActivity(myintent);
                finish();
			}
		});
	}
	@Override
	public void onInitUDPDone(byte result) {
		// TODO Auto-generated method stub
		
	}
}
