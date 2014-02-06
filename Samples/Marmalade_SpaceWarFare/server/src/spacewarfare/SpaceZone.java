/**
 *
 * @author Suyash Mohan
 */

package spacewarfare;

import com.shephertz.app42.server.idomain.BaseZoneAdaptor;
import com.shephertz.app42.server.idomain.HandlingResult;
import com.shephertz.app42.server.idomain.IRoom;
import com.shephertz.app42.server.idomain.IUser;

public class SpaceZone extends BaseZoneAdaptor {
    /*
     * Static Room has been created, assign a adptor to it
     */
    @Override
    public void onAdminRoomAdded(IRoom room)
    {
        System.out.println("Room Creatd " + room.getName());
        room.setAdaptor(new SpaceRoom(room));
    } 
    
    @Override
    public void handleAddUserRequest(IUser user, String authString, HandlingResult result)
    {
        System.out.println("UserRequest " + user.getName());
    }   
}
