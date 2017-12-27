package com.appwarp.s2;

import com.appwarp.s2.quizmania.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {
	
	private Intent myintent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
      

        new Handler().postDelayed(new Runnable(){
             @Override
            public void run() {
            	  myintent = new Intent(SplashScreen.this, QuizActivity.class);
                 startActivity(myintent);
                 finish();
            }
         }, 3000);
	}

}
