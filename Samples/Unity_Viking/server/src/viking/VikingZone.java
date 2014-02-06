/**
 *
 * @author Suyash Mohan
 */
package viking;

import com.shephertz.app42.server.idomain.BaseZoneAdaptor;
import com.shephertz.app42.server.idomain.HandlingResult;
import com.shephertz.app42.server.idomain.IRoom;
import com.shephertz.app42.server.idomain.IUser;

public class VikingZone extends BaseZoneAdaptor {
    /*
     * onAdminRoomAdded is called for all the rooms being
     * created at startup. Here we are setting adptor for the rooms
     */
    @Override
    public void onAdminRoomAdded(IRoom room)
    {
        System.out.println("Room Creatd " + room.getName());
        room.setAdaptor(new VikingRoom(room));
    } 
    
    /*
     * handleAddUserRequest is called for every user join room request.
     */
    @Override
    public void handleAddUserRequest(IUser user, String authString, HandlingResult result)
    {
        System.out.println("UserRequest " + user.getName());
    }   
}
