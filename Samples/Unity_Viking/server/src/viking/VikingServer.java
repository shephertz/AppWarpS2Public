/**
 *
 * @author Suyash Mohan
 */
package viking;

import com.shephertz.app42.server.idomain.BaseServerAdaptor;
import com.shephertz.app42.server.idomain.IZone;

public class VikingServer extends BaseServerAdaptor{
    
    /*
     * This function is called when a zone is created
     * Also when server starts and all predefined zones are loaded, 
     * this function is called
     * 
     * Here we set the adaptor for our zone i.e. VikingZone 
     */
    @Override
    public void onZoneCreated(IZone zone)
    {             
        System.out.println("Zone Created " + zone.getName());
        zone.setAdaptor(new VikingZone());
        /*for(IRoom iroom:zone.getRooms()){
            iroom.setAdaptor(new RoomAdaptor(iroom));
        }*/
        
    }
}
