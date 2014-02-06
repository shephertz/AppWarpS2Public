/**
 *
 * @author Suyash Mohan
 */
package chatapp;

import com.shephertz.app42.server.idomain.BaseServerAdaptor;
import com.shephertz.app42.server.idomain.IZone;

public class ChatServerAdaptor extends BaseServerAdaptor{
    @Override
    public void onZoneCreated(IZone zone)
    {   
        System.out.println("Zone Created " + zone.getName() + " with key " + zone.getAppKey());
        zone.setAdaptor(new ChatZoneAdaptor());
    }
}
