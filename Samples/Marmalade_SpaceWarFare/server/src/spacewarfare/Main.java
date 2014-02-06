/**
 *
 * @author Suyash Mohan
 */
package spacewarfare;

import com.shephertz.app42.server.AppWarpServer;

public class Main {

    public static void main(String[] args) {
        //locate the config file
        String appconfigPath = System.getProperty("user.dir")+System.getProperty("file.separator")+"AppConfig.json";
        //Start the server
	boolean started = AppWarpServer.start(new SpaceServer(), appconfigPath);
    }
}
