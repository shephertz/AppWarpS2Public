package chatapp;

import com.shephertz.app42.server.idomain.BaseServerAdaptor;
import com.shephertz.app42.server.idomain.IZone;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author shephertz
 */
public class ChatServerAdaptor extends BaseServerAdaptor{
    @Override
    public void onZoneCreated(IZone zone)
    {   
        System.out.println("Zone Created " + zone.getName() + " with key " + zone.getAppKey());
        zone.setAdaptor(new ChatZoneAdaptor());
    }
}
