/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quizcommon;

import com.shephertz.app42.server.idomain.IUser;
import java.util.ArrayList;

/**
 *
 * @author rahulwindows
 */
public class UserStatus {

    private IUser User;
    private int UserCurrentLevel;
    private int ScoreTillLastLevel;
    private ArrayList<Integer> userScorePerLevel;   
    private ArrayList<ArrayList<QuizAnswer>> AnswersPerLevel;

   
    public UserStatus(IUser User, int UserCurrentLevel, int ScoreTillLastLevel, ArrayList<Integer> userScorePerLevel,ArrayList<ArrayList<QuizAnswer>> AnswersPerLevel) {
        this.User = User;
        this.UserCurrentLevel = UserCurrentLevel;
        this.ScoreTillLastLevel = ScoreTillLastLevel;
        this.userScorePerLevel = userScorePerLevel;
        this.AnswersPerLevel = AnswersPerLevel;
    }
    public int getScoreTillLastLevel() {
        return ScoreTillLastLevel;
    }

    public void setScoreTillLastLevel(int ScoreTillLastLevel) {
        this.ScoreTillLastLevel = ScoreTillLastLevel;
    }
   
    public int getUserCurrentLevel() {
        return UserCurrentLevel;
    }

    public void setUserCurrentLevel(int UserCurrentLevel) {
        this.UserCurrentLevel = UserCurrentLevel;
    }

    public IUser getUser() {
        return User;
    }

    public void setUser(IUser User) {
        this.User = User;
    }

    public ArrayList<ArrayList<QuizAnswer>> getAnswersPerLevel() {
        return AnswersPerLevel;
    }

    public void setAnswersPerLevel(ArrayList<ArrayList<QuizAnswer>> AnswersPerLevel) {
        this.AnswersPerLevel = AnswersPerLevel;
    }

    public ArrayList<Integer> getUserScorePerLevel() {
        return userScorePerLevel;
    }

    public void setUserScorePerLevel(ArrayList<Integer> userScorePerLevel) {
        this.userScorePerLevel = userScorePerLevel;
    }
}
