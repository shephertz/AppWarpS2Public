using com.shephertz.app42.gaming.multiplayer.client;
using com.shephertz.app42.gaming.multiplayer.client.command;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;

namespace QuizMania
{
   public class ZoneRequestListener : com.shephertz.app42.gaming.multiplayer.client.listener.ZoneRequestListener
    {
        public void onCreateRoomDone(com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent eventObj)
        {
            try
            {
                Debug.WriteLine(GlobalContext.localUsername + " Created Room");
                GlobalContext.GameRoomId = eventObj.getData().getId();
                WarpClient.GetInstance().JoinRoom(eventObj.getData().getId());
            }
            catch (Exception e)
            { 
            
            }
          
        }

        public void onDeleteRoomDone(com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent roomEventObj)
        {
            if (roomEventObj.getData() != null)
            {
                Debug.WriteLine("onDeleteRoom " + roomEventObj.getData().getId() + " " + roomEventObj.getResult());
            }
        }

        public void onGetAllRoomsDone(com.shephertz.app42.gaming.multiplayer.client.events.AllRoomsEvent eventObj)
        {
          
        }

        public void onGetLiveUserInfoDone(com.shephertz.app42.gaming.multiplayer.client.events.LiveUserInfoEvent eventObj)
        {
            if (eventObj.getResult() == WarpResponseResultCode.SUCCESS)
            {
                // Join the room where the friend is playing
                GlobalContext.GameRoomId = eventObj.getLocationId();
                WarpClient.GetInstance().JoinRoom(GlobalContext.GameRoomId);
            }
           
        }

        public void onGetMatchedRoomsDone(com.shephertz.app42.gaming.multiplayer.client.events.MatchedRoomsEvent matchedRoomsEvent)
        {
          
        }

        public void onGetOnlineUsersDone(com.shephertz.app42.gaming.multiplayer.client.events.AllUsersEvent eventObj)
        {
           
        }

        public void onSetCustomUserDataDone(com.shephertz.app42.gaming.multiplayer.client.events.LiveUserInfoEvent eventObj)
        {
         
        }


        public void onRPCDone(byte result, string function, object returnValue)
        {
           
        }


        public void onInvokeZoneRPCDone(com.shephertz.app42.gaming.multiplayer.client.events.RPCEvent rpcEvent)
        {
           // throw new NotImplementedException();
        }
    }
}
