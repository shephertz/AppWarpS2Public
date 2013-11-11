/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp;

import com.shephertz.app42.server.idomain.BaseRoomAdaptor;
import com.shephertz.app42.server.idomain.HandlingResult;
import com.shephertz.app42.server.idomain.IUser;

/**
 *
 * @author shephertz
 */
public class ChatRoomAdaptor extends BaseRoomAdaptor{
    
    private String[] blacklist = {"fuck", "shit", "asshole", "cunt", "fag", "fuk", "fck", "fcuk", "assfuck", "assfucker", "fucker",
                                "motherfucker", "asscock", "asshead", "asslicker", "asslick", "assnigger", "nigger", "asssucker", "bastard", "bitch", "bitchtits",
                                "bitches", "bitch", "brotherfucker", "bullshit", "bumblefuck", "buttfucka", "fucka", "buttfucker", "buttfucka", "fagbag", "fagfucker",
                                "faggit", "faggot", "faggotcock", "fagtard", "fatass", "fuckoff", "fuckstick", "fucktard", "fuckwad", "fuckwit", "dick",
                                "dickfuck", "dickhead", "dickjuice", "dickmilk", "doochbag", "douchebag", "douche", "dickweed", "dyke", "dumbass", "dumass",
                                "fuckboy", "fuckbag", "gayass", "gayfuck", "gaylord", "gaytard", "nigga", "niggers", "niglet", "paki", "piss", "prick", "pussy",
                                "poontang", "poonany", "porchmonkey","porch monkey", "poon", "queer", "queerbait", "queerhole", "queef", "renob", "rimjob", "ruski",
                                "sandnigger", "sand nigger", "schlong", "shitass", "shitbag", "shitbagger", "shitbreath", "chinc", "carpetmuncher", "chink", "choad", "clitface"
                                , "clusterfuck", "cockass", "cockbite", "cockface", "skank", "skeet", "skullfuck", "slut", "slutbag", "splooge", "twatlips", "twat",
                                "twats", "twatwaffle", "vaj", "vajayjay", "va-j-j", "wank", "wankjob", "wetback", "whore", "whorebag", "whoreface"};
                                
    @Override
    public void onUserLeaveRequest(IUser user){
        System.out.println(user.getName() + " left room " + user.getLocation().getId());
    }

    @Override
    public void handleUserJoinRequest(IUser user, HandlingResult result){
        System.out.println(user.getName() + " joined room " );
    }
    
    @Override
    public void handleChatRequest(IUser sender, String message, HandlingResult result)
    {
        System.out.println(sender.getName() + " says " + message);
        for(String word:blacklist)
        {
            if(message.indexOf(word) != -1)
            {
                sender.SendChatNotification("Admin", "You are not allowed to use abusive language" , sender.getLocation());
                result.code = 1;
                result.description = "Bad Words Used";
                result.sendResponse = true;
                result.sendNotification = false;
            }
        }
    }
}
