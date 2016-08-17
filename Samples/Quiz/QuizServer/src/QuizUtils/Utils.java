/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package QuizUtils;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.User;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
/**
 *
 * @author rahulwindows
 */
public class Utils {

    private static String folder = System.getProperty("user.dir") + System.getProperty("file.separator") + "Resource" + System.getProperty("file.separator");
    public static JSONArray LevelJson = null;
    public static ArrayList<ArrayList<JSONArray>> QuestionArrayPerLevel = new ArrayList<ArrayList<JSONArray>>();
    public static ArrayList<ArrayList<JSONArray>> AnswerArrayPerLevel = new ArrayList<ArrayList<JSONArray>>();
    public static final int TotalQuizTypes=2;
    public static int delayInBetweenTheLevels;
    public static JSONObject ReadJsonFile(String path) {
        String JsonFile = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(folder + path));
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                JsonFile = JsonFile + sCurrentLine;
            }
            return new JSONObject(JsonFile);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject getQuestionJsonObject(int type,int level, int questionNo) {
        try {
            return QuestionArrayPerLevel.get(type).get(level).getJSONObject(questionNo);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static JSONObject getAnswerJsonObject(int type,int level,int questionNo)
    {
     try {
            return AnswerArrayPerLevel.get(type).get(level).getJSONObject(questionNo);
        } catch (Exception e) {
            return null;
        }
    }
     public static List getFacebookFriends(String AccessToken) {
        FacebookClient facebookClient = new DefaultFacebookClient(AccessToken);
        Connection friends = facebookClient.fetchConnection("me/friends", User.class);
        List friendsList = friends.getData();
        return friendsList;
    }
    
}
