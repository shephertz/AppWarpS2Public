/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quizserver;

import QuizUtils.Utils;
import com.restfb.types.User;
import com.shephertz.app42.server.idomain.BaseZoneAdaptor;
import com.shephertz.app42.server.idomain.HandlingResult;
import com.shephertz.app42.server.idomain.IRoom;
import com.shephertz.app42.server.idomain.IUser;
import com.shephertz.app42.server.idomain.IZone;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.mina.core.buffer.IoBuffer;
import org.json.JSONArray;
import org.json.JSONObject;
import quizcommon.QuizResponseCode;

/**
 *
 * @author rahulwindows
 */
public class QuizZoneAdaptor extends BaseZoneAdaptor {

    IZone zone;

    public QuizZoneAdaptor(IZone zone) {
        this.zone = zone;
    }

    @Override 
    public void onUserRemoved(IUser user){
        System.out.println("QuizZoneAdaptor onUserRemoved "+user.getName());
        IRoom room = user.getLocation();
        if(room!=null){
        	
            QuizRoomAdaptor adaptor = (QuizRoomAdaptor) room.getAdaptor();
            adaptor.onUserLeaveRequest(user);
        }
    }
    
    @Override
    public void onAdminRoomAdded(IRoom room) {
        System.out.println("Room Created " + room.getName() + " with ID " + room.getId());
    }

    @Override
    public void handleCreateRoomRequest(IUser user, IRoom room, HandlingResult result) {
        System.out.println("Room Created " + room.getName() + " " + user.getName() + " with ID " + room.getId());
        room.setAdaptor(new QuizRoomAdaptor(this.zone, room));
    }

    @Override
    public void handlePrivateChatRequest(IUser sender, IUser toUser, HandlingResult result) {

        System.out.println("Sending Message " + sender.getName() + " to " + toUser.getName());
    }

    @Override
    public void handleDeleteRoomRequest(IUser user, IRoom room, HandlingResult result) {
    }

    @Override
    public void handleAddUserRequest(IUser user, String authData, HandlingResult result) {
        try {
            /*If user had joined using his facebook account this function will make
             *list of friends those are playing this game in current zone
             */
            System.out.println("Add user request " + user.getName());
            if (!authData.equals("")) {
                user.setCustomData(authData);
                try {
                    JSONObject statObj = new JSONObject(user.getCustomData());
                    List fbFriends = Utils.getFacebookFriends(statObj.getString("AccessToken"));
                    ArrayList<IUser> FBFriendsInMyZone = new ArrayList<IUser>();
                    Iterator zoneLstIterator = this.zone.getUsers().iterator();
                    while (zoneLstIterator.hasNext()) {
                        IUser zoneUser = (IUser) zoneLstIterator.next();
                        String fbId = new JSONObject(zoneUser.getCustomData()).getString("FacebookId");
                        Iterator lstIterator = fbFriends.listIterator();
                        while (lstIterator.hasNext()) {
                            User fbuser = (User) lstIterator.next();
                            if (fbId.equals(fbuser.getId())) {
                                FBFriendsInMyZone.add(zoneUser);
                            }
                        }
                    }
                    JSONArray usersArray = new JSONArray();
                    for (int i = 0; i < FBFriendsInMyZone.size(); i++) {
                        JSONObject objPayload = new JSONObject();
                        objPayload.put("username", FBFriendsInMyZone.get(i).getName());
                        objPayload.put("facebookId", new JSONObject(FBFriendsInMyZone.get(i).getCustomData()).getString("FacebookId"));
                        usersArray.put(objPayload);
                    }
                    int Length = 40 + usersArray.toString().getBytes().length;
                    IoBuffer buf = IoBuffer.allocate(Length);
                    buf.setAutoExpand(true);
                    buf.put(QuizResponseCode.FBFRIENDLIST);
                    buf.putInt(usersArray.toString().getBytes().length);
                    buf.put(usersArray.toString().getBytes());
                    user.SendUpdatePeersNotification(buf.array(), false);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
    }
}