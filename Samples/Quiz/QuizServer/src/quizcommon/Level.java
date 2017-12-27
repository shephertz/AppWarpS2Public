/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quizcommon;

/**
 *
 * @author rahulwindows
 */
public class Level {
    
    public int levelId;
    public int totalQuestions;
    public int pointsPerCorrectAnswer;
    public int pointsPerWrongAnswer;
    public int timePerQuestion;

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public int getTimePerQuestion() {
        return timePerQuestion;
    }

    public void setTimePerQuestion(int timePerQuestion) {
        this.timePerQuestion = timePerQuestion;
    }
   

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int TotalQuestions) {
        this.totalQuestions = TotalQuestions;
    }

    public int getPointsPerCorrectAnswer() {
        return pointsPerCorrectAnswer;
    }

    public void setPointsPerCorrectAnswer(int pointsPerCorrectAnswer) {
        this.pointsPerCorrectAnswer = pointsPerCorrectAnswer;
    }

    public int getPointsPerWrongAnswer() {
        return pointsPerWrongAnswer;
    }

    public void setPointsPerWrongAnswer(int pointsPerWrongAnswer) {
        this.pointsPerWrongAnswer = pointsPerWrongAnswer;
    }

}
