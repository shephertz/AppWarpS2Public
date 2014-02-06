/**
 *
 * @author Suyash Mohan
 */
package spacewarfare;

import com.shephertz.app42.server.idomain.BaseServerAdaptor;
import com.shephertz.app42.server.idomain.IZone;

public class SpaceServer extends BaseServerAdaptor{
    
    @Override
    public void onZoneCreated(IZone zone)
    {             
        System.out.println("Zone Created " + zone.getName());
        zone.setAdaptor(new SpaceZone());
        /*for(IRoom iroom:zone.getRooms()){
            iroom.setAdaptor(new RoomAdaptor(iroom));
        }*/
        
    }
}
