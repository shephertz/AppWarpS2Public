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
public class LevelResult {

    public IUser User;
    public int LevelId;
    public int NumberOfCorrectQuestions;
    public int NumberOfAttempedQuestions;
    public int TotalTimeTaken;
    public ArrayList<Object> CorrectAnswerList;
    public ArrayList<Object> UserAnswerList;

    public IUser getUser() {
        return User;
    }

    public void setUser(IUser User) {
        this.User = User;
    }

    public int getLevelId() {
        return LevelId;
    }

    public void setLevelId(int LevelId) {
        this.LevelId = LevelId;
    }

    public int getNumberOfCorrectQuestions() {
        return NumberOfCorrectQuestions;
    }

    public void setNumberOfCorrectQuestions(int NumberOfCorrectQuestions) {
        this.NumberOfCorrectQuestions = NumberOfCorrectQuestions;
    }

    public int getTotalTimeTaken() {
        return TotalTimeTaken;
    }

    public int getNumberOfAttempedQuestions() {
        return NumberOfAttempedQuestions;
    }

    public void setNumberOfAttempedQuestions(int NumberOfAttempedQuestions) {
        this.NumberOfAttempedQuestions = NumberOfAttempedQuestions;
    }

    public ArrayList<Object> getCorrectAnswerList() {
        return CorrectAnswerList;
    }

    public void setCorrectAnswerList(ArrayList<Object> CorrectAnswerList) {
        this.CorrectAnswerList = CorrectAnswerList;
    }

    public ArrayList<Object> getUserAnswerList() {
        return UserAnswerList;
    }

    public void setUserAnswerList(ArrayList<Object> UserAnswerList) {
        this.UserAnswerList = UserAnswerList;
    }

    public LevelResult(IUser User, int NumberOfCorrectQuestions, int AttempedQuestions, int TotalTimeTaken, ArrayList<Object> CorrectAnswerList, ArrayList<Object> UserAnswerList) {
        this.User = User;
        this.NumberOfCorrectQuestions = NumberOfCorrectQuestions;
        this.NumberOfAttempedQuestions = AttempedQuestions;
        this.TotalTimeTaken = TotalTimeTaken;
        this.CorrectAnswerList = CorrectAnswerList;
        this.UserAnswerList = UserAnswerList;
    }

    public void setTotalTimeTaken(int TotalTimeTaken) {
        this.TotalTimeTaken = TotalTimeTaken;
    }
}
