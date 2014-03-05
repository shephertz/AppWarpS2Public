/**
 *
 * @author Suyash Mohan
 */
package spacewarfare;

import com.shephertz.app42.server.idomain.BaseRoomAdaptor;
import com.shephertz.app42.server.idomain.HandlingResult;
import com.shephertz.app42.server.idomain.IRoom;
import com.shephertz.app42.server.idomain.IUser;
import org.json.JSONException;
import org.json.JSONObject;

public class SpaceRoom extends BaseRoomAdaptor{

    private DragonUser dragon;
    private IRoom m_room;
    private Long ticks;
    
    public SpaceRoom(IRoom room) {
        m_room = room;
        dragon = new DragonUser();
        dragon.SetPosition(50,50);
        ticks = 0L;
    }
    
    @Override
    public void handleUserJoinRequest(IUser user, HandlingResult result){
        System.out.println("User Joined " + user.getName());
    }
    
    @Override
    public void handleChatRequest(IUser sender, String message, HandlingResult result)
    {
        System.out.println(sender.getName() + " says " + message);
        
        try
        {
            JSONObject json = new JSONObject(message);
            if(json.getInt("type") == 2 && json.getString("p").equals("dragon")){
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
    
    @Override
    public void onTimerTick(long time) {
        if(time - ticks > 1000){
            ticks = time;
                      
            if(dragon.GetHealth() > 0)
            {
                dragon.MoveRandomStep(25,25,800-50, 480-50);

                JSONObject tobeSent = new JSONObject();
                try
                {
                    tobeSent.put("name", "Dragon");
                    tobeSent.put("type", 1);
                    tobeSent.put("x",dragon.GetX());
                    tobeSent.put("y", dragon.GetY());
                    tobeSent.put("health",dragon.GetHealth());
                }
                catch(JSONException ex)
                {

                }

                m_room.BroadcastChat("dragon", tobeSent.toString());
            }
            else{
                dragon.Spawn(20);
                dragon.SetPosition(50, 50);
            }
        }
    }
}
