/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package appwarpmasterchatsample;

import com.shephertz.appwarp.server.master.MasterServer;

/**
 *
 * @author Suyash
 */
public class AppWarpMasterChatSample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)throws Exception {
        String appconfigPath = System.getProperty("user.dir")+System.getProperty("file.separator")+"MasterConfig.json";
        MasterServer server = MasterServer.getInstance();
        server.getGameClientHandler().addListener(new ClientListener());
        server.getGameServerManager().addListener(new GameServerListener());
        boolean success = server.start(appconfigPath); 
        if(!success){
            throw new Exception("MasterServer did not start. Check MasterServer.config");
        }
    }
    
}
