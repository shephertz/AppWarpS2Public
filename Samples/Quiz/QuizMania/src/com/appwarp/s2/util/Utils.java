package com.appwarp.s2.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Anjali on 7/12/2016.
 */
public class Utils {
    public static final String API_KEY ="490dc9f5-e8f1-4ff4-a" ;
  
    public static String HOST_NAME =  "192.168.1.48";
    
    public static final int RECCOVERY_ALLOWANCE_TIME = 60;
    public static final int TURN_TIME = 120;
    
    public static String ALERT_INIT_EXEC = "Exception in Initilization";
    public static String ALERT_ERR_DISCONN = "Can't Disconnect";
    public static String ALERT_CONN_FAIL = "Connection Failed";

    public static String username;
    
    public  static boolean checkNetworkConnection(Context context)
    
    {
    	ConnectivityManager cm =
            (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
    
    public static void showToastAlertOnUIThread(final Activity ctx,
			final String message) {
		ctx.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(ctx.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
			}
		});
	}
    
    public static void showToastAlert(Context ctx, String message) {
		Toast.makeText(ctx.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}

    public  static  void showDialog(Context ctx, String msg)

    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

        builder.setMessage(msg)
                .setCancelable(false)

                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //TOdo
                        dialog.cancel();
                    }
                });


        AlertDialog diaglog = builder.create();
        diaglog.show();
    }
}
