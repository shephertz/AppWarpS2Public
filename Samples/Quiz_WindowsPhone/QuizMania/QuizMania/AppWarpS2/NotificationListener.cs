using System;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;
using System.Linq;
using com.shephertz.app42.gaming.multiplayer.client.events;
using com.shephertz.app42.gaming.multiplayer.client.command;
using com.shephertz.app42.gaming.multiplayer.client;
using System.Collections.Generic;
using System.Windows.Threading;
using System.Diagnostics;

namespace QuizMania
{
    public class NotificationListener : com.shephertz.app42.gaming.multiplayer.client.listener.NotifyListener
    {
      
        public NotificationListener()
        {
        }
       
        public void onRoomCreated(RoomData eventObj)
        {
           
        }

        public void onRoomDestroyed(RoomData eventObj)
        {
            
        }

        public void onUserLeftRoom(RoomData eventObj, String username)
        {
            if (eventObj.getId().Equals(GlobalContext.GameRoomId))
            {
                if (!GlobalContext.localUsername.Equals(username))
                {
                    Deployment.Current.Dispatcher.BeginInvoke(delegate() { App.g_QuizPageListener.OpponentLeftRoom(); });
                }
                WarpClient.GetInstance().Disconnect();
            }
        }

        public void onUserJoinedRoom(RoomData roomObj, String username)
        {
            Debug.WriteLine("On User Joined Room "+username);
            if (!GlobalContext.joinedUsers.Contains(username))
            {
                GlobalContext.joinedUsers.Add(username);
            }
            if (!GlobalContext.localUsername.Equals(username))
            {
                GlobalContext.opponentName = username;
            }
            if((GlobalContext.joinedUsers.Count==2)&& GlobalContext.tableProperties["IsPrivateRoom"].Equals("true"))
            {
                Deployment.Current.Dispatcher.BeginInvoke(delegate() { App.g_HomePageListener.StartQuiz(); });
            }
        }

        public void onUserLeftLobby(LobbyData eventObj, String username)
        {
            
        }

        public void onUserJoinedLobby(LobbyData eventObj, String username)
        {
            
        }

        public void onChatReceived(ChatEvent eventObj)
        {
          
        }

        public void onUpdatePeersReceived(UpdateEvent eventObj)
        {
            Debug.WriteLine("Update peer received");
            MoveMessage.buildMessage(eventObj.getUpdate());           
        }

        public void onUserChangeRoomProperty(RoomData roomData, string sender, Dictionary<string, object> properties)
        {
            GlobalContext.tableProperties = properties;
        }
        public void onMoveCompleted(MoveEvent moveEvent)
        {
           
        }

        public void onPrivateChatReceived(string sender, string message)
        {
            if (!sender.Equals(GlobalContext.localUsername))
            {
                MoveMessage.HandlePrivateMessage(message);
            }
        }

        public void onUserChangeRoomProperty(RoomData roomData, string sender, Dictionary<string, object> properties, Dictionary<string, string> lockedPropertiesTable)
        {
            GlobalContext.tableProperties = properties;
        }


        public void onUserPaused(string locid, bool isLobby, string username)
        {
        }

        public void onUserResumed(string locid, bool isLobby, string username)
        {
             
        }

        public void onGameStarted(string sender, string roomId, string nextTurn)
        { 
        }

        public void onGameStopped(string sender, string roomId)
        {
        }
    }
}
