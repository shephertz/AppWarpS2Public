package rummydemo;

import com.shephertz.app42.server.idomain.BaseZoneAdaptor;
import com.shephertz.app42.server.idomain.HandlingResult;
import com.shephertz.app42.server.idomain.IRoom;
import com.shephertz.app42.server.idomain.ITurnBasedRoom;
import com.shephertz.app42.server.idomain.IUser;
import com.shephertz.app42.server.idomain.IZone;
import com.shephertz.app42.server.message.WarpResponseResultCode;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONObject;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author shephertz
 */
public class RummyZoneExtension extends BaseZoneAdaptor {
    
    private IZone izone;
    
    RummyZoneExtension(IZone izone){
        this.izone = izone;
    }
    
    /*
     *  This function invoked when server receive an authntication request. If user choose login with facebook
     *  client send auth data (facebook token, id, display name) etc.  otherwise this is a guest user and no auth
     *  token verification will be done.
     */
    
    @Override
    public void handleAddUserRequest(final IUser user, final String authData, final HandlingResult result){
        if(authData!=null && authData.length()>0){
            result.code = WarpResponseResultCode.AUTH_PENDING;// indicates that response will be sent asynchronously
            new Thread(new Runnable() {
                @Override
                public void run() {
                    checkForAuth(user, authData, result);
                }
            }).start();
        }
    }
    
    /*
     * This function checks using facebook graph api if username(fb_id) is same for the received facebook token.
     */
    
    private void checkForAuth(IUser user, String authData, HandlingResult result){
        String token;
        try{
            JSONObject data = new JSONObject(authData);
            token = data.getString("token");
            URL oracle = new URL("https://graph.facebook.com/me?access_token="+token);
            URLConnection yc = oracle.openConnection();
            
            BufferedReader in = new BufferedReader(new InputStreamReader(
                                        yc.getInputStream()));
            String inputLine;
            while((inputLine=in.readLine())!=null){
                JSONObject response = new JSONObject(inputLine);
                if(response.get("id").equals(user.getName())){
                    // "Auth success on server"
                    izone.sendAddUserResponse(user, WarpResponseResultCode.SUCCESS, "Auth success on server");
                }else{
                    // "Auth Failed on server"
                    izone.sendAddUserResponse(user, WarpResponseResultCode.AUTH_ERROR, "Auth failed on server");
                }
            }
            in.close();
        }catch(Exception e){
            izone.sendAddUserResponse(user, WarpResponseResultCode.AUTH_ERROR, "");
        }
    }
    
    /*
     * This function invoked when server receive create room request. 
     * we set adapter to room by checking maxUsers in room.
     */
    
    @Override
    public void handleCreateRoomRequest(IUser user, IRoom room, HandlingResult result)
    {
        if(room.isTurnBased() && room.getMaxUsers()==2){
            room.setAdaptor(new RummyRoomExtension2User(izone, (ITurnBasedRoom)room));
        }else if(room.isTurnBased() && room.getMaxUsers()==3){
            room.setAdaptor(new RummyRoomExtension3User(izone, (ITurnBasedRoom)room));
        }
        else{
            result.code = WarpResponseResultCode.BAD_REQUEST;
        }
    } 
    
    /*
     * This function invoked when the given user loses its connection due to an intermittent
     * connection failure. (Using Connection Resiliency feature)
     */
    
    @Override
    public void onUserPaused(IUser user){
        if(user.getLocation() == null){
            return;
        }
        if(user.getLocation().getMaxUsers()==2){
            RummyRoomExtension2User extension = (RummyRoomExtension2User)user.getLocation().getAdaptor();
            extension.onUserPaused(user);
        }else if(user.getLocation().getMaxUsers()==3){
            RummyRoomExtension3User extension = (RummyRoomExtension3User)user.getLocation().getAdaptor();
            extension.onUserPaused(user);
        }
    }
    
    /*
     * This function invoked when the given user recovers its connection from an intermittent
     * connection failure. (Using Connection Resiliency feature)
     */    
    @Override
    public void handleResumeUserRequest(IUser user, String authData, HandlingResult result)
    {
        if(user.getLocation() == null){
            return;
        }
        if(user.getLocation().getMaxUsers()==2){
            RummyRoomExtension2User extension = (RummyRoomExtension2User)user.getLocation().getAdaptor();
            extension.onUserResume(user);
        }else if(user.getLocation().getMaxUsers()==3){
            RummyRoomExtension3User extension = (RummyRoomExtension3User)user.getLocation().getAdaptor();
            extension.onUserResume(user);
        }
    } 
    
}
