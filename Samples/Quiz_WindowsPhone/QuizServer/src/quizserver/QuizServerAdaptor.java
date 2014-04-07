/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quizserver;

import com.shephertz.app42.server.idomain.BaseServerAdaptor;
import com.shephertz.app42.server.idomain.IZone;

/**
 *
 * @author rahulwindows
 */
public class QuizServerAdaptor extends BaseServerAdaptor{
    
    @Override
    public void onZoneCreated(IZone zone)
    {   
        System.out.println("Zone Created " + zone.getName() + " with key " + zone.getAppKey());
        zone.setAdaptor(new QuizZoneAdaptor(zone));
    }
    
}
