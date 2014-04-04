using System;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;
using com.shephertz.app42.gaming.multiplayer.client.events;
using com.shephertz.app42.gaming.multiplayer.client.command;
using com.shephertz.app42.gaming.multiplayer.client;
using System.Text;
using System.Collections.Generic;
using System.Diagnostics;
using QuizMania.QuizModels;
using QuizMania.AppWarpS2;
using System.IO;
using Newtonsoft.Json;

namespace QuizMania
{
    public class RoomReqListener : com.shephertz.app42.gaming.multiplayer.client.listener.RoomRequestListener
    { 
  
        public RoomReqListener()
        {
            
        }

        public void onSubscribeRoomDone(RoomEvent eventObj)
        {
           
        }
        public void onUnSubscribeRoomDone(RoomEvent eventObj)
        {
          
        }

        public void onJoinRoomDone(RoomEvent roomEventObj)
        {
           
            if (roomEventObj.getResult() == WarpResponseResultCode.SUCCESS)
            {
                // reset the local global properties as we are starting a new game play session          
                GlobalContext.GameRoomId = roomEventObj.getData().getId();
                Debug.WriteLine("Room Joined !!");
                WarpClient.GetInstance().SubscribeRoom(GlobalContext.GameRoomId);
                // get live information to fetch the name of the opponent if already inside
                WarpClient.GetInstance().GetLiveRoomInfo(GlobalContext.GameRoomId);
                Deployment.Current.Dispatcher.BeginInvoke(delegate()
                {
                    Debug.WriteLine("On Joined Room");
                    if (GlobalContext.tableProperties["IsPrivateRoom"].Equals("true")&&GlobalContext.AmIOwner)
                    {
                        GlobalContext.AmIOwner = false;
                        Debug.WriteLine("Send Invitation");
                        MoveMessage.SendInvitation();
                    }
                    else
                    {
                       App.g_HomePageListener.StartQuiz();
                    }
                });
            }
            else
            {
              
                try
                {   
                    Deployment.Current.Dispatcher.BeginInvoke(delegate() {
                        Debug.WriteLine("Room Joined Failed " + roomEventObj);
                        WarpClient.GetInstance().CreateRoom("QuizRoom","QuizRoom", GlobalContext.MaxUsersInRoom, GlobalContext.tableProperties); 
                    });
                }
                catch (Exception e)
                {
                    MessageBox.Show(e.Message);
                }
            }
        }

        public void onLeaveRoomDone(RoomEvent eventObj)
        {
            Debug.WriteLine("On Leave Room Done"+eventObj.getResult());
            WarpClient.GetInstance().Disconnect();
            if (eventObj.getResult() == WarpResponseResultCode.SUCCESS)
            {
                GlobalContext.opponentName = "No Opponent";
            }
        }

        public void onGetLiveRoomInfoDone(LiveRoomInfoEvent liveRoomInfoObj)
        {
            if (liveRoomInfoObj.getData() != null)
            {
                GlobalContext.tableProperties = liveRoomInfoObj.getProperties();
                if ((liveRoomInfoObj.getJoinedUsers().Length == 2))
                {
                    if (liveRoomInfoObj.getJoinedUsers()[0].Equals(GlobalContext.localUsername))
                    {
                        GlobalContext.opponentName = liveRoomInfoObj.getJoinedUsers()[1];
                    }
                    else
                    {
                        GlobalContext.opponentName = liveRoomInfoObj.getJoinedUsers()[0];
                    }
                    Debug.WriteLine("get Live RoomInfo");
                    Deployment.Current.Dispatcher.BeginInvoke(delegate() { App.g_HomePageListener.StartQuiz(); });
                }
            }
        }

        public void onSetCustomRoomDataDone(LiveRoomInfoEvent eventObj)
        {

        }

        public void onUpdatePropertyDone(LiveRoomInfoEvent lifeLiveRoomInfoEvent)
        {
            if (lifeLiveRoomInfoEvent.getResult() == WarpResponseResultCode.SUCCESS)
            {
                GlobalContext.tableProperties = lifeLiveRoomInfoEvent.getProperties();
            }
        }
        public void onLockPropertiesDone(byte result)
        {
        
        }

        public void onUnlockPropertiesDone(byte result)
        {
        }


        public void onRPCDone(byte result, string function, object returnValue)
        {
          
        }


        public void onInvokeRoomRPCDone(RPCEvent rpcEvent)
        {
            //throw new NotImplementedException();
        }
    }
}
