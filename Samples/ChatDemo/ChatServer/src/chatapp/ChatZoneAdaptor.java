/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp;

import com.shephertz.app42.server.idomain.BaseZoneAdaptor;
import com.shephertz.app42.server.idomain.HandlingResult;
import com.shephertz.app42.server.idomain.IRoom;

/**
 *
 * @author shephertz
 */
public class ChatZoneAdaptor extends BaseZoneAdaptor {
    @Override
    public void handleCreateRoomRequest(IRoom room, HandlingResult result)
    {
        System.out.println("Room Created " + room.getName() + " with ID " + room.getId() );
        room.setAdaptor(new ChatRoomAdaptor());
    }  
}
