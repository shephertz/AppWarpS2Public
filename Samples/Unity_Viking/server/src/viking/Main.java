/**
 *
 * @author Suyash Mohan
 */
package viking;

import com.shephertz.app42.server.AppWarpServer;

public class Main {
    public static void main(String[] args) {
        //Get the config file for AppWarp S2
        String appconfigPath = System.getProperty("user.dir")+System.getProperty("file.separator")+"AppConfig.json";
        //Start the AppWarp server 
	boolean started = AppWarpServer.start(new VikingServer(), appconfigPath);
    }
}
