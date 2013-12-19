package rummydemo;

import com.shephertz.app42.server.idomain.BaseTurnRoomAdaptor;
import com.shephertz.app42.server.idomain.HandlingResult;
import com.shephertz.app42.server.idomain.IRoom;
import com.shephertz.app42.server.idomain.ITurnBasedRoom;
import com.shephertz.app42.server.idomain.IUser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONObject;

public class RummyRoomExtension3User extends BaseTurnRoomAdaptor {

    
    
    
    private ITurnBasedRoom gameRoom;
    ArrayList<IUser> pausedUserList = new ArrayList<IUser>();
    
    // GameData
    private ArrayList<Integer> TOTAL_CARDS = new ArrayList<Integer>();
    
    private ArrayList<Integer> USER_1 = new ArrayList<Integer>();
    private ArrayList<Integer> USER_2 = new ArrayList<Integer>();
    private ArrayList<Integer> USER_3 = new ArrayList<Integer>();
    
    private int TOP_CARD = -1;
    private int NEW_CARD = -1;
    
    private String user1 = null;
    private String user2 = null;
    private String user3 = null;
    
    public byte GAME_STATUS;
    
    public RummyRoomExtension3User(IRoom room){
        this.gameRoom = (ITurnBasedRoom)room;
           GAME_STATUS = CardsConstants.STOPPED;
    }
    
    @Override
    public void handleUserJoinRequest(IUser user, HandlingResult result){
        
    }
    
    /*
     * This is a RPC Method when user request for new Card
     */
    public int requestNewCard(){
        NEW_CARD = getNewCard();
        return NEW_CARD;
    }
    
    @Override
    public void handleMoveRequest(IUser sender, String moveData, HandlingResult result){
        try{
            int top_card =-1;
            JSONObject data = new JSONObject(moveData);
            if(data.has("top")){
               top_card = data.getInt("top");
            }
            validateAndHandleMove(sender, top_card, result);
            NEW_CARD = -1;
            if(checkForWin(USER_1) || checkForWin(USER_2) || checkForWin(USER_3)){
                handleFinishGame();
            }
//            printAll("handleMoveRequest", true);    
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /*
     * if any user leave then game contiune in remaining users and its card is added
     * into total card list
     */
    @Override
    public void onUserLeavingTurnRoom(IUser user, HandlingResult result){
        if(GAME_STATUS!=CardsConstants.RUNNING){
            return;
        }
        if(gameRoom.getJoinedUsers().size()==2){// if three users are playing and one of them left room
            if(user.getName().equals(user1)){
                TOTAL_CARDS.addAll(0, USER_1);
            }else if(user.getName().equals(user2)){
                TOTAL_CARDS.addAll(0, USER_2);
            }else if(user.getName().equals(user3)){
                TOTAL_CARDS.addAll(0, USER_3);
            }
            if(gameRoom.getTurnUser().getName().equals(user.getName()) && NEW_CARD!=-1){
                TOTAL_CARDS.add(NEW_CARD);
                NEW_CARD=-1;
            }
        }else if(gameRoom.getJoinedUsers().size()==1){// if two users are playing and one of them left room
            String leaveingUser = null;
            if(user.getName().equals(user1)){
                leaveingUser = user1;
            }else if(user.getName().equals(user2)){
                leaveingUser = user2;
            }else if(user.getName().equals(user3)){
                leaveingUser = user3;
            }
            String message = "You Win! Enemy "+leaveingUser+" left the room";
            gameRoom.BroadcastChat(CardsConstants.SERVER_NAME, CardsConstants.RESULT_USER_LEFT+"#"+message);
            gameRoom.stopGame(CardsConstants.SERVER_NAME);
        }
    }
    
    public void onUserPaused(IUser user){
        if(gameRoom.getJoinedUsers().contains(user)){
            pausedUserList.add(user);
            GAME_STATUS = CardsConstants.PAUSED;
            gameRoom.stopGame(CardsConstants.SERVER_NAME);
        }
    }
    
    public void onUserResume(IUser user){
        if(pausedUserList.indexOf(user)!=-1){
            pausedUserList.remove(user);
        }
        if(pausedUserList.isEmpty()){
            GAME_STATUS = CardsConstants.RESUMED;
        }
    }
    
    @Override
    public void onTurnExpired(IUser turn, HandlingResult result){
    
    }
    /*
     * This method deal new hand for each user and send
     * chat message having his cards array
     */
    private void dealNewCards(){
        
        for(int i=1;i<=CardsConstants.MAX_CARD;i++){
            TOTAL_CARDS.add(i);
        }
        Collections.shuffle(TOTAL_CARDS);
        for(int i=0;i<6;i++){
            USER_1.add(TOTAL_CARDS.remove(0));
            USER_2.add(TOTAL_CARDS.remove(0));
            USER_3.add(TOTAL_CARDS.remove(0));
        }
        List<IUser>list = gameRoom.getJoinedUsers();
        if(list.size()==3){
            IUser iuser1 = list.get(0);
            IUser iuser2 = list.get(1);
            IUser iuser3 = list.get(2);
            user1 = iuser1.getName();
            user2 = iuser2.getName();
            user3 = iuser3.getName();
            try{
                JSONObject dataUser1 = new JSONObject();
                dataUser1.put(iuser1.getName(), USER_1);
                JSONObject dataUser2 = new JSONObject();
                dataUser2.put(iuser2.getName(), USER_2);
                JSONObject dataUser3 = new JSONObject();
                dataUser3.put(iuser3.getName(), USER_3);
                iuser1.SendChatNotification(CardsConstants.SERVER_NAME, CardsConstants.CARD_DATA+"#"+dataUser1, gameRoom);
                iuser2.SendChatNotification(CardsConstants.SERVER_NAME, CardsConstants.CARD_DATA+"#"+dataUser2, gameRoom);
                iuser3.SendChatNotification(CardsConstants.SERVER_NAME, CardsConstants.CARD_DATA+"#"+dataUser3, gameRoom);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
//        printAll("dealNewCards", true);
    }
    
    @Override
    public void handleTimerTick(long time){
        /*
         * A game when room full
         * or we can say max users are equals to joined users
         */
        if(gameRoom.getMaxUsers()==3){
            if(GAME_STATUS==CardsConstants.STOPPED && gameRoom.getJoinedUsers().size()==gameRoom.getMaxUsers()){
                GAME_STATUS=CardsConstants.RUNNING;
                dealNewCards();
                gameRoom.startGame(CardsConstants.SERVER_NAME);
            }else if(GAME_STATUS==CardsConstants.RESUMED){
                GAME_STATUS=CardsConstants.RUNNING;
                gameRoom.startGame(CardsConstants.SERVER_NAME);
            }
        }
        
    }
    /*
     * This method return last element of TOTAL_CARDS
     * In case of empty list again shuffle cards
     */
    private Integer getNewCard(){
        if(TOTAL_CARDS.isEmpty()){
            for(int i=1;i<=CardsConstants.MAX_CARD;i++){
                TOTAL_CARDS.add(i);
            }
            Collections.shuffle(TOTAL_CARDS);
            for(Integer i:USER_1){
                TOTAL_CARDS.remove(new Integer(i));
            }
            for(Integer j:USER_2){
                TOTAL_CARDS.remove(new Integer(j));
            }
            for(Integer k:USER_3){
                TOTAL_CARDS.remove(new Integer(k));
            }
            if(TOP_CARD!=-1){
                TOTAL_CARDS.remove(new Integer(TOP_CARD));
            }
            if(NEW_CARD!=-1){
                TOTAL_CARDS.remove(new Integer(NEW_CARD));
            }
        }
        return TOTAL_CARDS.remove(TOTAL_CARDS.size()-1);
     }
            
    /*
     * This function checks if any user has completed cards pair according
     * winning rules.
     * Users should complete all pair(pair contain 3 cards) of same type
     * like:{4,4,4} or {K,K,K} etc.
     */
    
    private boolean checkForWin(ArrayList<Integer> list){
        int winCount = 0;
        for(int i=1;i<=13;i++){
            int count = 0;
            if(list.indexOf(i)!=-1){
                count++;
            }if(list.indexOf(i+13)!=-1){
                count++;
            }if(list.indexOf(i+26)!=-1){
                count++;
            }if(list.indexOf(i+39)!=-1){
                count++;
            }
            if(count>=3){
                winCount++;
            }
        }
        if(winCount==2){
            return true;
        }else{
            return false;
        }
    }
    private void validateAndHandleMove(IUser sender, int topCard, HandlingResult result){
        /*
         * if user throws same card(NEW_CARD)
         */
        if(topCard==this.NEW_CARD){// if user have sent same card
            TOP_CARD = topCard;
            return;
        }
        
        /*
         * if user select NEW_CARD and throws some other card from his card list
         */
        if(NEW_CARD!=-1){
            if(sender.getName().equals(user1)){
                if(USER_1.indexOf(topCard)!=-1){
                    USER_1.remove(new Integer(topCard));
                    USER_1.add(NEW_CARD);
                    TOP_CARD = topCard;
                }else{
                    result.code = CardsConstants.INVALID_MOVE;
                    result.description = "Invalid Move";
                }
            }else if(sender.getName().equals(user2)){
                if(USER_2.indexOf(topCard)!=-1){
                    USER_2.remove(new Integer(topCard));
                    USER_2.add(NEW_CARD);
                    TOP_CARD = topCard;
                }else{
                    result.code = CardsConstants.INVALID_MOVE;
                    result.description = "Invalid Move";
                }
            }else if(sender.getName().equals(user3)){
                if(USER_3.indexOf(topCard)!=-1){
                    USER_3.remove(new Integer(topCard));
                    USER_3.add(NEW_CARD);
                    TOP_CARD = topCard;
                }else{
                    result.code = CardsConstants.INVALID_MOVE;
                    result.description = "Invalid Move";
                }
            }
        }else{// if user select TOP_CARD and throws some other card from his card list
            if(sender.getName().equals(user1)){
                if(USER_1.indexOf(topCard)!=-1){
                    USER_1.remove(new Integer(topCard));
                    USER_1.add(TOP_CARD);
                    TOP_CARD = topCard;
                }else{
                    result.code = CardsConstants.INVALID_MOVE;
                    result.description = "Invalid Move";
                }
            }else if(sender.getName().equals(user2)){
                if(USER_2.indexOf(topCard)!=-1){
                    USER_2.remove(new Integer(topCard));
                    USER_2.add(TOP_CARD);
                    TOP_CARD = topCard;
                }else{
                    result.code = CardsConstants.INVALID_MOVE;
                    result.description = "Invalid Move";
                }
            }else if(sender.getName().equals(user3)){
                if(USER_3.indexOf(topCard)!=-1){
                    USER_3.remove(new Integer(topCard));
                    USER_3.add(TOP_CARD);
                    TOP_CARD = topCard;
                }else{
                    result.code = CardsConstants.INVALID_MOVE;
                    result.description = "Invalid Move";
                }
            }
        }
        
    }
    
    private void handleFinishGame(){
        try{
            JSONObject object = new JSONObject();
            if(checkForWin(USER_1)){
                object.put("win", user1);
                object.put("cards", USER_1);
            }else if(checkForWin(USER_2)){
                object.put("win", user2);
                object.put("cards", USER_2);
            }else if(checkForWin(USER_3)){
                object.put("win", user3);
                object.put("cards", USER_3);
            }
            GAME_STATUS = CardsConstants.FINISHED;
            gameRoom.BroadcastChat(CardsConstants.SERVER_NAME, CardsConstants.RESULT_GAME_OVER+"#"+object);
            gameRoom.stopGame(CardsConstants.SERVER_NAME);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void printAll(String TAG, boolean status){
        if(status){
            System.out.println("==================="+TAG+"======================");
            System.out.println("USER_1:   "+USER_1);
            System.out.println("USER_2:   "+USER_2);
            System.out.println("USER_3:   "+USER_3);
            System.out.println("TOTAL_CA: "+TOTAL_CARDS);
            System.out.println("TOP_CARD: "+TOP_CARD);
            System.out.println("NEW_CARD: "+NEW_CARD);
        }
    }
}
