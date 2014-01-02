package viking;

import com.shephertz.app42.server.domain.User;
import com.shephertz.app42.server.idomain.BaseRoomAdaptor;
import com.shephertz.app42.server.idomain.HandlingResult;
import com.shephertz.app42.server.idomain.IRoom;
import com.shephertz.app42.server.idomain.IUser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
public class VikingRoom extends BaseRoomAdaptor{

    //Reference to current room
    private IRoom m_room;
    //ticks to simulate a timer
    private Long ticks;
    //Instance of our robot
    private Robot bot;
    
    public VikingRoom(IRoom room) {
        m_room = room;
        ticks = 0L;
        bot = new Robot();
        //Set path from (38.84f, 2.26f, 46.51f) to (68.84f, 2.26f, 46.51f)
        bot.setPath(new Vector3f(38.84f, 2.26f, 46.51f), new Vector3f(68.84f, 2.26f, 46.51f), 1);
    }
    
    /*
     * A user has joined a room
     */
    @Override
    public void handleUserJoinRequest(IUser user, HandlingResult result){
        System.out.println("User Joined " + user.getName());
    }
    
    /*
     * There is a new chat message in the room.
     */
    @Override
    public void handleChatRequest(IUser sender, String message, HandlingResult result)
    {
        System.out.println(sender.getName() + " says " + message);
    }
    
    /*
     * Timer
     * Called after particular interval defined in AppConfig.json
     */
    @Override
    public void handleTimerTick(long time) {
        bot.moveStepX();
        Vector3f pos = bot.getPosition();
        //Get the current position of robot and send it to all clients
        JSONObject tobeSent = new JSONObject();
        try
        {
            tobeSent.put("x",pos.x);
            tobeSent.put("y",pos.y);
            tobeSent.put("z",pos.z);
        }
        catch(JSONException ex)
        {
        }

        m_room.BroadcastChat("robot", tobeSent.toString());
    }
}