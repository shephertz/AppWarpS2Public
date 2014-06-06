/**
 *
 * @author Suyash Mohan
 */
package chatapp;

import com.shephertz.app42.server.AppWarpServer;

public class Main {
    public static void main(String[] args) {
		String appconfigPath = System.getProperty("user.dir")+System.getProperty("file.separator")+"AppConfig.json";
		boolean started = AppWarpServer.start(new ChatServerAdaptor(), appconfigPath);
    }
}
