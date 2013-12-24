/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rummydemo;


public class CardsConstants {
    
    /*
    * 1.clover
    * 2.clover
    * 3.clover
    * 4.clover
    * 5.clover
    * 6.clover
    * 7.clover
    * 8.clover
    * 9.clover
    * 10.clover
    * 11.clover
    * 12.clover
    * 13.clover
    * 14.heart
    * 15.heart
    * 16.heart
    * 17.heart
    * 18.heart
    * 19.heart
    * 20.heart
    * 21.heart
    * 22.heart
    * 23.heart
    * 24.heart
    * 25.heart
    * 26.heart
    * 27.spade
    * 28.spade
    * 29.spade
    * 30.spade
    * 31.spade
    * 32.spade
    * 33.spade
    * 34.spade
    * 35.spade
    * 36.spade
    * 37.spade
    * 38.spade
    * 39.spade
    * 40.diamond
    * 41.diamond
    * 42.diamond
    * 43.diamond
    * 44.diamond
    * 45.diamond
    * 46.diamond
    * 47.diamond
    * 48.diamond
    * 49.diamond
    * 50.diamond
    * 51.diamond
    * 52.diamond
    */
    
    public static final String SERVER_NAME = "AppWarpS2";
    
    public static final byte MAX_CARD = 52;
    // Message Constants
    
    public static final byte PLAYER_HAND = 1;
    
    public static final byte RESULT_GAME_OVER = 3;
    
    public static final byte RESULT_USER_LEFT = 4;
    
    
    // error code
    public static final int SUBMIT_CARD = 111;
    public static final int INVALID_MOVE = 121;
    
    // GAME_STATUS
    
    public static final int STOPPED = 71;
    public static final int RUNNING = 72;
    public static final int PAUSED = 73;
    public static final int RESUMED = 74;
    public static final int FINISHED = 75;

}
