/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package appwarpmasterchatsample;

import com.shephertz.appwarp.server.master.MasterServer;
import com.shephertz.appwarp.server.master.application.GameClientEventsListener;
import com.shephertz.appwarp.server.master.application.IGameClient;
import com.shephertz.appwarp.server.master.application.IGameServer;
import com.shephertz.appwarp.server.master.application.IGameServerManager;
import com.shephertz.appwarp.server.master.application.IZone;
import java.util.ArrayList;

/**
 *
 * @author Suyash
 */
public class ClientListener implements GameClientEventsListener {
    @Override
    public void onCustomRequest(IGameClient source, byte[] message) {
        String msg = new String(message);
        String response = new String();
        boolean found = false;
            IGameServerManager gameServerManeger = MasterServer.getInstance().getGameServerManager();
            ArrayList<IGameServer> gameServers = gameServerManeger.getServers();
            for(IGameServer s: gameServers){
                ArrayList<IZone> zones = s.getZones();
                for(IZone zone:zones){
                    ArrayList<String> userNames = zone.getUsernames();
                    if(userNames.contains(msg)){
                        response += zone.getAppKey() + "<br>"; 
                        found = true;
                    }
                }
            }
            
        if(found == false)
        {
            String str = "Not Found";
            source.sendMessage(str.getBytes());
        }
        else
        {
            source.sendMessage(response.getBytes());
        }
    }

    @Override
    public void onClientConnected(IGameClient client) {
        //System.out.println("onClientConnected: "+client.getAddress().host+":"+client.getAddress().port);
    }

    @Override
    public void onClientDisconnected(IGameClient client) {
        //System.out.println("onClientDisconnected: "+client.getAddress().host+":"+client.getAddress().port);
    }

    @Override
    public void onTimerTick(long time) {
        //System.out.println("onTimerTick: "+time);
    }
}
