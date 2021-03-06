package spacewarfare;

import com.shephertz.app42.server.domain.User;
import com.shephertz.app42.server.idomain.BaseRoomAdaptor;
import com.shephertz.app42.server.idomain.HandlingResult;
import com.shephertz.app42.server.idomain.IRoom;
import com.shephertz.app42.server.idomain.IUser;
import java.util.Random;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author shephertz
 */
public class SpaceRoom extends BaseRoomAdaptor{

    private DragonUser dragon;
    private IRoom m_room;
    private Long ticks;
    
    public SpaceRoom(IRoom room) {
        m_room = room;
        dragon = new DragonUser(m_room);
        dragon.SetPosition(1200,800);
        ticks = 0L;
    }
    
    /*
     * a user has sent a join room request
     */
    @Override
    public void handleUserJoinRequest(IUser user, HandlingResult result){
        System.out.println("User Joined " + user.getName());
    }
    
    /*
     * a new chat message has been recieved
     * here we will check if player has hit the dragon
     * type = 2 in JSON message means player has hit someone
     * and p = "dragon" show the person shot by player was dragon
     */
    @Override
    public void handleChatRequest(IUser sender, String message, HandlingResult result)
    {
        System.out.println(sender.getName() + " says " + message);
        
        try
        {
            JSONObject json = new JSONObject(message);
            if(json.getInt("type") == 2 && json.getString("p").equals("dragon")){
                //reduce the health of dragon by 1
                if(dragon.ReduceHealth() <= 0){
                    JSONObject tobeSent = new JSONObject();
                    try
                    {
                        tobeSent.put("name", "Dragon");
                        tobeSent.put("type", 3);
                    }
                    catch(JSONException ex)
                    {

                    }

                    m_room.BroadcastChat("dragon", tobeSent.toString());
                }
                
            }
        }
        catch(JSONException e){
            
        }
    }
    
    /*
     * Timer
     * Called after particular interval defined in AppConfig.json
     */
    @Override
    public void handleTimerTick(long time) {
        // if time has elapsed more then 1 second
        if(time - ticks > 1000){          
            //if health is more than 0
            if(dragon.GetHealth() > 0)
            {
                //move our dragon randomly 
                dragon.MoveRandomStep(100,100,2560-100, 1536-100);

                //and send information to all players
                JSONObject tobeSent = new JSONObject();
                try
                {
                    tobeSent.put("name", "Dragon"); // Name i.e. Dragon
                    tobeSent.put("type", 1); // type of message, 1 = movement
                    tobeSent.put("x",dragon.GetX());// x component of new position of dragon
                    tobeSent.put("y", dragon.GetY());// y component of new position of dragon
                    tobeSent.put("health",dragon.GetHealth()); // health of dragon
                }
                catch(JSONException ex)
                {

                }

                m_room.BroadcastChat("dragon", tobeSent.toString());
                ticks = time;
            }
            //if dragon has been dead for more than 10 seconds, re-spawn him
            else if(time-ticks > 10000){
                dragon.Spawn(20);
                dragon.SetPosition(1200, 800);
                ticks = time;
            }
        }
    }
}
