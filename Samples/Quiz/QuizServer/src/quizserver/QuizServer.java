/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quizserver;

import QuizUtils.Utils;
import com.shephertz.app42.server.AppWarpServer;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author rahulwindows
 */
public class QuizServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String appconfigPath = System.getProperty("user.dir") + System.getProperty("file.separator") + "AppConfig.json";
        boolean started = AppWarpServer.start(new QuizServerAdaptor(), appconfigPath);
        try {
            JSONObject lJson=Utils.ReadJsonFile("Levels.txt");
            Utils.LevelJson = lJson.getJSONArray("Levels");
            Utils.delayInBetweenTheLevels=lJson.getInt("delayInBetweenTheLevels");
            for (Integer quiztype = 0; quiztype < Utils.TotalQuizTypes; quiztype++) {
                Utils.QuestionArrayPerLevel.add(new ArrayList<JSONArray>());
                Utils.AnswerArrayPerLevel.add(new ArrayList<JSONArray>());
                for (Integer i = 0; i < Utils.LevelJson.length(); i++) {
                    JSONObject json = Utils.ReadJsonFile("Questions_" +quiztype.toString()+ i.toString() + ".txt");
                    Utils.QuestionArrayPerLevel.get(quiztype).add(json.getJSONArray("Questions"));
                    Utils.AnswerArrayPerLevel.get(quiztype).add(json.getJSONArray("Answers"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
