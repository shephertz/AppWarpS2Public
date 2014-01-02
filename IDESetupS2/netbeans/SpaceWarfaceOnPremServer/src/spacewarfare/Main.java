/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spacewarfare;

import com.shephertz.app42.server.AppWarpServer;

/**
 *
 * @author DHRUV CHOPRA
 */

public class Main {

    public static void main(String[] args) {
        //locate the config file
        String appconfigPath = System.getProperty("user.dir")+System.getProperty("file.separator")+"AppConfig.json";
        //Start the server
	boolean started = AppWarpServer.start(new SpaceServer(), appconfigPath);
    }
}
