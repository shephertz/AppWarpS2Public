/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quizserver;

import QuizUtils.Utils;
import com.shephertz.app42.server.domain.Room;
import com.shephertz.app42.server.idomain.BaseRoomAdaptor;
import com.shephertz.app42.server.idomain.HandlingResult;
import com.shephertz.app42.server.idomain.IRoom;
import com.shephertz.app42.server.idomain.IUser;
import com.shephertz.app42.server.idomain.IZone;
import java.util.ArrayList;
import org.apache.mina.core.buffer.IoBuffer;
import org.json.JSONArray;
import org.json.JSONObject;
import quizcommon.QuizAnswer;
import quizcommon.QuizRequestCode;
import quizcommon.QuizResponseCode;
import quizcommon.QuizType;
import quizcommon.UserStatus;

/**
 *
 * @author rahulwindows
 */
public class QuizRoomAdaptor extends BaseRoomAdaptor {

    private IZone izone;
    private IRoom gameRoom;
    ArrayList<UserStatus> UserStatusList = new ArrayList<UserStatus>();
    public static ArrayList<JSONArray> QuestionArrayofAttempedLevel = new ArrayList<JSONArray>();
    public static ArrayList<JSONArray> AnswerArrayofAttempedLevel = new ArrayList<JSONArray>();
    private int QuizTimerCount = 0;
    private int GameCurrentLevel = 0, GameCurrentQuestion = -1;
    private byte GAME_STATUS;
    private byte QUIZ_TYPE = QuizType.POLITICS;
    private int StartQuizFlag = 0;
    private boolean HasAnyUserLeftTheRoom=false;
    
    public QuizRoomAdaptor(IZone izone, IRoom room) {
        this.izone = izone;
        this.gameRoom = room;
        GAME_STATUS = QuizConstants.STOPPED;
        try {
            if (((Room) this.gameRoom).getProperties().get("QuizTopic").toString().equalsIgnoreCase("Politics")) {
                QUIZ_TYPE = QuizType.POLITICS;
            } else {
                QUIZ_TYPE = QuizType.CRICKET;
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onUserLeaveRequest(IUser user) {
        StartQuizFlag = 0;
        HasAnyUserLeftTheRoom=true;
        System.out.println("QuizRoomAdaptor onUserLeaveRequest " + user.getName() + " left room " + user.getLocation().getId());
        UserStatusList.clear();
    }

    @Override
    public void handleUserJoinRequest(IUser user, HandlingResult result) {
        System.out.println(user.getName() + " joined room Request");
        if (UserStatusList.size() < gameRoom.getMaxUsers()) {
            System.out.println(user.getName() + " joined room Request Success");
            UserStatus newUser = new UserStatus(user, 0, 0, new ArrayList<Integer>(), new ArrayList<ArrayList<QuizAnswer>>());
            UserStatusList.add(newUser);
        }
    }

    @Override
    public void handleUpdatePeersRequest(IUser sender, byte[] update, HandlingResult result) {
        try {
            result.sendNotification = false;
            int size;
            byte[] fbInvitation;
            String fbObject;
            JSONObject invObject;
            IoBuffer buf = IoBuffer.allocate(update.length, false);
            buf.setAutoExpand(true);
            buf.put(update, 0, update.length);
            buf.flip();
            buf.position(0);
            byte bt = buf.get();
            switch (bt) {
                case QuizRequestCode.STARTQUIZ:
                    StartQuizFlag++;
                    System.out.println("Received Start Quiz Packet" + StartQuizFlag + "RoomId " + this.gameRoom.getId());
                    break;
                case QuizRequestCode.ANSWERPACKET:
                    System.out.println("Answer Received " + bt);
                    for (int i = 0; i < UserStatusList.size(); i++) {
                        if (UserStatusList.get(i).getUser().getName().equalsIgnoreCase(sender.getName())) {
                            int queID = buf.getInt();
                            int ans = buf.getInt();
                            if (UserStatusList.get(i).getAnswersPerLevel().size() == GameCurrentLevel) {
                                UserStatusList.get(i).getAnswersPerLevel().add(new ArrayList<QuizAnswer>());
                            }
                            int currentSize = UserStatusList.get(i).getAnswersPerLevel().get(GameCurrentLevel).size();
                            QuizAnswer userAnswer = new QuizAnswer(queID, ans);
                            if (currentSize == 0) {
                                UserStatusList.get(i).getAnswersPerLevel().get(GameCurrentLevel).add(userAnswer);
                                updateAndBroadCastScore(i, queID - 1, ans);
                            } else if (UserStatusList.get(i).getAnswersPerLevel().get(GameCurrentLevel).get(currentSize - 1).QuestionId != queID) {
                                UserStatusList.get(i).getAnswersPerLevel().get(GameCurrentLevel).add(userAnswer);
                                updateAndBroadCastScore(i, queID - 1, ans);
                            }
                        }
                    }
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTimerTick(long time) {
        /*
         * A game when room full
         * or we can say max users are equals to joined users
         * Once the game has started it will send question on every 10 seconds
         */
       try {
        if (HasAnyUserLeftTheRoom) {
            HasAnyUserLeftTheRoom=false;
            System.out.println("Deleting Room " + this.gameRoom.getId() + " " + this.izone.deleteRoom(gameRoom.getId()));
            GAME_STATUS = QuizConstants.STOPPED;
        }
        if (GAME_STATUS == QuizConstants.STOPPED && gameRoom.getJoinedUsers().size() == gameRoom.getMaxUsers() && gameRoom.getMaxUsers() == StartQuizFlag) {
            GAME_STATUS = QuizConstants.RUNNING;
            QuizTimerCount=Utils.LevelJson.getJSONObject(GameCurrentLevel).getInt("timePerQuestion") * 2-1;
            StartQuizFlag = 0;
        } else if (GAME_STATUS == QuizConstants.RUNNING) {
            QuizTimerCount++;
           
                if (GameCurrentLevel < Utils.LevelJson.length()) {
                    int timeCount = Utils.LevelJson.getJSONObject(GameCurrentLevel).getInt("timePerQuestion") * 2;
                    if (QuizTimerCount == timeCount) {
                        int totalQuestion = Utils.LevelJson.getJSONObject(GameCurrentLevel).getInt("totalQuestions");
                        if (GameCurrentQuestion != -1) {
                                AddDefaultAnswers();
                            }
                        if (GameCurrentQuestion < (totalQuestion - 1)) {
                            SendQuestion();
                        } else {
                            SendLevelEndPacket();
                        }
                    }
                }
            }
        }
        catch (Exception e) {
                e.printStackTrace();
            }
    }

    private void AddDefaultAnswers() {

        /* if user had not submmited the last question answer then this function 
         *adds -1 as user answer and broadcast the scores
         */
        for (int i = 0; i < UserStatusList.size(); i++) {
            if (UserStatusList.get(i).getAnswersPerLevel().size() == GameCurrentLevel) {
                UserStatusList.get(i).getAnswersPerLevel().add(new ArrayList<QuizAnswer>());
            }
            int currentSize = UserStatusList.get(i).getAnswersPerLevel().get(GameCurrentLevel).size();
            QuizAnswer userAnswer = new QuizAnswer(GameCurrentQuestion, -1);
            if (currentSize == 0) {
                UserStatusList.get(i).getAnswersPerLevel().get(GameCurrentLevel).add(userAnswer);
                updateAndBroadCastScore(i, GameCurrentQuestion, -1);
            } else if (UserStatusList.get(i).getAnswersPerLevel().get(GameCurrentLevel).get(currentSize - 1).QuestionId != GameCurrentQuestion) {
                UserStatusList.get(i).getAnswersPerLevel().get(GameCurrentLevel).add(userAnswer);
                updateAndBroadCastScore(i, GameCurrentQuestion, -1);
            }

        }
    }

    private void SendQuestion() {
        QuizTimerCount = 0;
        GameCurrentQuestion++;
        byte[] questionPacket = getNextQuestionPacket();
        for (int i = 0; i < UserStatusList.size(); i++) {
            UserStatus user = UserStatusList.get(i);
            System.out.println("Sending Question to User "+user.getUser().getName().toString());
            user.getUser().SendUpdatePeersNotification(questionPacket, true);
        }
    }

    private void SendLevelEndPacket() {
        QuizTimerCount = (QuizTimerCount-Utils.delayInBetweenTheLevels*2);
        try {
            JSONArray usersArray = new JSONArray();
            for (int i = 0; i < UserStatusList.size(); i++) {
                JSONObject objPayload = new JSONObject();
                objPayload.put("user", UserStatusList.get(i).getUser().getName());
                objPayload.put("currentLevelscore", UserStatusList.get(i).getUserScorePerLevel().get(GameCurrentLevel));
                int totalScore = UserStatusList.get(i).getScoreTillLastLevel() + UserStatusList.get(i).getUserScorePerLevel().get(GameCurrentLevel);
                UserStatusList.get(i).setScoreTillLastLevel(totalScore);
                objPayload.put("totalScore", UserStatusList.get(i).getScoreTillLastLevel());
                usersArray.put(objPayload);
            }
            IoBuffer buf = IoBuffer.allocate(48 + usersArray.length());
            buf.setAutoExpand(true);
            if (GameCurrentLevel < Utils.LevelJson.length() - 1) {
                buf.put(QuizResponseCode.LEVELRESULT);
            } else {
                buf.put(QuizResponseCode.FINALRESULT);
            }
            buf.putInt(GameCurrentLevel);
            buf.putInt(usersArray.toString().getBytes().length);
            buf.put(usersArray.toString().getBytes());
            for (int i = 0; i < UserStatusList.size(); i++) {
                UserStatus user = UserStatusList.get(i);
                user.getUser().SendUpdatePeersNotification(buf.array(), true);
            }
            GameCurrentLevel++;
            GameCurrentQuestion = -1;
        } catch (Exception e) {
        }
    }

    private byte[] getNextQuestionPacket() {
        try {
            JSONObject question = Utils.getQuestionJsonObject(QUIZ_TYPE, GameCurrentLevel, GameCurrentQuestion);
            IoBuffer buf = IoBuffer.allocate(48 + question.length());
            buf.setAutoExpand(true);
            if (GameCurrentQuestion < 1) {
                buf.put(QuizResponseCode.LEVELSTART);
                buf.putInt(GameCurrentLevel);
            } else {
                buf.put(QuizResponseCode.QUESTIONPACKET);
                JSONObject LastQuestionAnswer = Utils.getAnswerJsonObject(QUIZ_TYPE, GameCurrentLevel, GameCurrentQuestion - 1);
                int ans = LastQuestionAnswer.getInt("Answer");
                System.out.println("Last Question Answer " + ans);
                buf.putInt(ans);
            }
            buf.putInt(question.toString().getBytes().length);
            buf.put(question.toString().getBytes());
            System.out.println("Send Question Packet of " + buf.array().length);
            return buf.array();
        } catch (Exception e) {
            return null;
        }
    }

    private void updateAndBroadCastScore(int userIndex, int queID, int ans) {
        try {
            System.out.println("Send Answer");
            JSONObject LastQuestionAnswer = Utils.getAnswerJsonObject(QUIZ_TYPE, GameCurrentLevel, queID);
            int currentSize = UserStatusList.get(userIndex).getUserScorePerLevel().size();
            if (currentSize > GameCurrentLevel) {
                int score = UserStatusList.get(userIndex).getUserScorePerLevel().remove(currentSize - 1);
                if (LastQuestionAnswer.getInt("Answer") == ans) {
                    UserStatusList.get(userIndex).getUserScorePerLevel().add(score + 30);
                } else if (ans == -1) {
                    UserStatusList.get(userIndex).getUserScorePerLevel().add(score);
                } else {
                    UserStatusList.get(userIndex).getUserScorePerLevel().add(score - 10);
                }
            } else {
                //it is for 1st question of cuurentLevel
                if (LastQuestionAnswer.getInt("Answer") == ans) {
                    UserStatusList.get(userIndex).getUserScorePerLevel().add(30);
                } else if (ans == -1) {
                    UserStatusList.get(userIndex).getUserScorePerLevel().add(0);
                } else {
                    UserStatusList.get(userIndex).getUserScorePerLevel().add(-10);
                }
            }
            JSONObject objPayload = new JSONObject();
            objPayload.put("user", UserStatusList.get(userIndex).getUser().getName());
            objPayload.put("currentLevelscore", UserStatusList.get(userIndex).getUserScorePerLevel().get(GameCurrentLevel));
            IoBuffer buf = IoBuffer.allocate(48 + objPayload.toString().getBytes().length);
            buf.setAutoExpand(true);
            buf.put(QuizResponseCode.UPDATESCORE);
            buf.putInt(objPayload.toString().getBytes().length);
            buf.put(objPayload.toString().getBytes());
            for (int i = 0; i < UserStatusList.size(); i++) {
                UserStatus user = UserStatusList.get(i);
                user.getUser().SendUpdatePeersNotification(buf.array(), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
