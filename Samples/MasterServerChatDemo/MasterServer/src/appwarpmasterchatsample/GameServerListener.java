/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package appwarpmasterchatsample;

import com.shephertz.appwarp.server.master.application.GameServerEventsListener;
import com.shephertz.appwarp.server.master.application.IGameServer;

/**
 *
 * @author Suyash
 */
public class GameServerListener implements GameServerEventsListener{
    @Override
    public void gameServerConnected(IGameServer server) {
        //System.out.println("gameServerConnected: "+server.getAddress().host + ":"+server.getAddress().port);
    }

    @Override
    public void gameServerDisconnected(IGameServer server) {
        //System.out.println("gameServerDisconnected: "+server.getAddress().host + ":"+server.getAddress().port);
    }

    @Override
    public void receivedMessage(IGameServer source, byte[] message) {
        //System.out.println("receivedMessage: "+source.getAddress().host + ":"+source.getAddress().port + " message: " + new String(message));
    }

    @Override
    public void onTimerTick(long time) {
        
    }
}
