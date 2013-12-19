package rummydemo;

import com.shephertz.app42.server.idomain.BaseZoneAdaptor;
import com.shephertz.app42.server.idomain.HandlingResult;
import com.shephertz.app42.server.idomain.IRoom;
import com.shephertz.app42.server.idomain.IUser;
import com.shephertz.app42.server.idomain.IZone;
import com.shephertz.app42.server.message.WarpResponseResultCode;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONException;
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
    
    RummyRoomExtension2User rummyRoomExtension;
    RummyRoomExtension3User rummyRoomExtension3User;
    
    IZone izone;
    
    RummyZoneExtension(IZone izone){
        this.izone = izone;
    }
    
    @Override
    public void handleAddUserRequest(final IUser user, final String authData, final HandlingResult result){
        if(authData!=null && authData.length()>0){
            result.code = WarpResponseResultCode.AUTH_PENDING;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    checkForAuth(user, authData, result);
                }
            }).start();
        }
    }
    
    private void checkForAuth(IUser user, String authData, HandlingResult result){
        String token;
        String userName;
        String displayName;
        try{
            JSONObject data = new JSONObject(authData);
            token = data.getString("token");
            userName = data.getString("userName");
            displayName = data.getString("displayName");
            URL oracle = new URL("https://graph.facebook.com/me?access_token="+token);
            URLConnection yc = oracle.openConnection();
            
            BufferedReader in = new BufferedReader(new InputStreamReader(
                                        yc.getInputStream()));
            String inputLine;
            while((inputLine=in.readLine())!=null){
                JSONObject response = new JSONObject(inputLine);
                if(response.get("id").equals(userName)){
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
            System.out.print("authData"+authData);
            e.printStackTrace();
        }
    }
    
    @Override
    public void handleCreateRoomRequest(IUser user, IRoom room, HandlingResult result)
    {
        if(room.isTurnBased() && room.getMaxUsers()==2){
            rummyRoomExtension = new RummyRoomExtension2User(room);
            room.setAdaptor(rummyRoomExtension);
        }else if(room.isTurnBased() && room.getMaxUsers()==3){
            rummyRoomExtension3User = new RummyRoomExtension3User(room);
            room.setAdaptor(rummyRoomExtension3User);
        }
        System.out.println("handleCreateRoomRequest: ");
    } 
    
    @Override
    public void onUserPaused(IUser user){
        if(user.getLocation().getMaxUsers()==2){
            RummyRoomExtension2User extension = (RummyRoomExtension2User)user.getLocation().getAdaptor();
            extension.onUserPaused(user);
        }else if(user.getLocation().getMaxUsers()==3){
            RummyRoomExtension3User extension = (RummyRoomExtension3User)user.getLocation().getAdaptor();
            extension.onUserPaused(user);
        }
    }
    
    @Override
    public void handleResumeUserRequest(IUser user, String authData, HandlingResult result)
    {
        if(user.getLocation().getMaxUsers()==2){
            RummyRoomExtension2User extension = (RummyRoomExtension2User)user.getLocation().getAdaptor();
            extension.onUserResume(user);
        }else if(user.getLocation().getMaxUsers()==3){
            RummyRoomExtension3User extension = (RummyRoomExtension3User)user.getLocation().getAdaptor();
            extension.onUserResume(user);
        }
    } 
    
    public int add1(int a, int b){
        return (a+b);
    }
}
