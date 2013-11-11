package spacewarfare;

import com.shephertz.app42.server.idomain.BaseZoneAdaptor;
import com.shephertz.app42.server.idomain.HandlingResult;
import com.shephertz.app42.server.idomain.IRoom;
import com.shephertz.app42.server.idomain.IUser;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author shephertz
 */
public class SpaceZone extends BaseZoneAdaptor {
    @Override
    public void handleCreateRoomRequest(IRoom room, HandlingResult result)
    {
        System.out.println("Room Creatd " + room.getName());
        room.setAdaptor(new SpaceRoom(room));
    } 
    
    @Override
    public void handleAddUserRequest(IUser user, String authData, HandlingResult result)
    {
        System.out.println("UserRequest " + user.getName());
    }   
}
