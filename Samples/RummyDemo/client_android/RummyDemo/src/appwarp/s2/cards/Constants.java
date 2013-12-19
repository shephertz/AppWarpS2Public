package appwarp.s2.cards;
public class Constants {
	
	public static String APP_KEY = "YOUR_APP_KEY";
	
    public static String HOST_NAME =  "YOUR_SERVER_ADDRESS";
    
    public static final String FB_APP_ID = "421905537937918";
    
    public static final int RECCOVERY_ALLOWANCE_TIME = 60;
    
    public static final int MAX_RECOVERY_ATTEMPT = 10;
    
    public static final int TURN_TIME = 120;
    
    public static final int TOTAL_CARDS = 52;
    
    public static String SERVER_NAME = "AppWarpS2";
    
    public static final byte CARD_DATA= 1;
    public static final byte NEW_CARD = 2;
    public static final byte RESULT_GAME_OVER = 3;
    public static final byte RESULT_USER_LEFT = 4;
    
    public static final int REQUEST_NEW_CARD = 111;
    // error code
    public static final int INVALID_MOVE = 121;
    
    // GAME_STATUS
    
    public static final int STOPPED = 71;
    public static final int RUNNING = 72;
    public static final int PAUSED = 73;
    
    // String constants
    
    public static String RECOVER_TEXT = "Recovering...";
    
    // Alert Messages
    public static String ALERT_INIT_EXEC = "Exception in Initilization";
    public static String ALERT_ERR_DISCONN = "Can't Disconnect";
    public static String ALERT_INV_MOVE = "Invalid Move: Not Your Turn";
    public static String ALERT_ROOM_CREATE = "Room creation failed";
    public static String ALERT_CONN_FAIL = "Connection Failed";
    public static String ALERT_SEND_FAIL = "Send Move Failed";
    public static String ALERT_CONN_SUCC = "Connection Success";
    public static String ALERT_CONN_RECOVERED = "Connection Recovered";
    public static String ALERT_CONN_ERR_RECOVABLE = "Recoverable connection error. Recovering session after 5 seconds";
    public static String ALERT_CONN_ERR_NON_RECOVABLE = "Non-recoverable connection error.";
}
