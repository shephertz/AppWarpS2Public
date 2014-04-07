using System;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;
using Newtonsoft.Json.Linq;
using System.IO;
using QuizMania.AppWarpS2;
using System.Text;
using QuizMania.QuizModels;
using Newtonsoft.Json;
using System.Collections.Generic;
using System.Diagnostics;
using com.shephertz.app42.gaming.multiplayer.client;

namespace QuizMania
{
    /// <summary>
    /// CatapultWar gameplay message class. Objects of this class represent actions of the user
    /// and are used to serialize/deserialize JSON exchanged between users in the room
    /// </summary>
    public class MoveMessage
    {
        public String sender;
        public String Type = "NONE";

        public static void buildMessage(byte[] update)
        {
            try
            {
                BinaryReader buf = new BinaryReader(new MemoryStream(update));
                byte MessageType = buf.ReadByte();
                Debug.WriteLine("Received Bytes " + update.Length + "Type " + MessageType);
                int payLoadSize;
                QuestionPacket qp;
                ScorePacket usercore;
                List<FBFriend> fbFriends;
                byte[] payLoadBytes;
                switch (MessageType)
                {
                    case QuizResponseCode.QUESTIONPACKET:
                        Debug.WriteLine("Received Question Packet");
                        int lastQuestionAnswer = IPAddress.NetworkToHostOrder(buf.ReadInt32());
                        payLoadSize = IPAddress.NetworkToHostOrder(buf.ReadInt32());
                        payLoadBytes = new byte[payLoadSize];
                        payLoadBytes = buf.ReadBytes(payLoadBytes.Length);
                        qp = JsonConvert.DeserializeObject<QuestionPacket>(Encoding.UTF8.GetString(payLoadBytes, 0, payLoadBytes.Length));
                        Deployment.Current.Dispatcher.BeginInvoke(delegate() { App.g_QuizPageListener.ReceivedNewQuestion(lastQuestionAnswer, qp); });
                        break;
                    case QuizResponseCode.SCOREPACKET:
                       
                        payLoadSize = IPAddress.NetworkToHostOrder(buf.ReadInt32());
                        payLoadBytes = new byte[payLoadSize];
                        payLoadBytes = buf.ReadBytes(payLoadBytes.Length);
                        usercore = JsonConvert.DeserializeObject<ScorePacket>(Encoding.UTF8.GetString(payLoadBytes, 0, payLoadBytes.Length));
                        Deployment.Current.Dispatcher.BeginInvoke(delegate() { App.g_QuizPageListener.ReceivedScorePacket(usercore); });
                        break;
                    case QuizResponseCode.LEVELSTART:
                        Debug.WriteLine("Received Level Start Packet");
                        int levelNumber = IPAddress.NetworkToHostOrder(buf.ReadInt32());
                        payLoadSize = IPAddress.NetworkToHostOrder(buf.ReadInt32());
                        payLoadBytes = new byte[payLoadSize];
                        payLoadBytes = buf.ReadBytes(payLoadBytes.Length);
                        qp = JsonConvert.DeserializeObject<QuestionPacket>(Encoding.UTF8.GetString(payLoadBytes, 0, payLoadBytes.Length));
                        Deployment.Current.Dispatcher.BeginInvoke(delegate() {
                        App.g_QuizPageListener.ReceivedStartLevelPacket(levelNumber, qp); 
                        });
                        break;
                    case QuizResponseCode.LEVELRESULT:
                    case QuizResponseCode.FINALRESULT:
                        int level = IPAddress.NetworkToHostOrder(buf.ReadInt32());
                        payLoadSize = IPAddress.NetworkToHostOrder(buf.ReadInt32());
                        payLoadBytes = new byte[payLoadSize];
                        payLoadBytes = buf.ReadBytes(payLoadBytes.Length);
                        List<ScorePacket> usercores = JsonConvert.DeserializeObject<List<ScorePacket>>(Encoding.UTF8.GetString(payLoadBytes, 0, payLoadBytes.Length));
                        Deployment.Current.Dispatcher.BeginInvoke(delegate() { App.g_QuizPageListener.ReceivedLevelEndPacket(level, MessageType, usercores); });
                        break;
                    case QuizResponseCode.FBFRIENDLIST:
                        payLoadSize = IPAddress.NetworkToHostOrder(buf.ReadInt32());
                        payLoadBytes = new byte[payLoadSize];
                        payLoadBytes = buf.ReadBytes(payLoadBytes.Length);
                        String str = Encoding.UTF8.GetString(payLoadBytes, 0, payLoadBytes.Length);
                        fbFriends = JsonConvert.DeserializeObject<List<FBFriend>>(str);
                        Deployment.Current.Dispatcher.BeginInvoke(delegate() { App.g_HomePageListener.FBFriendList(fbFriends); });
                        break;

                }
            }
            catch (Exception e)
            {
                MessageBox.Show(e.Message);
            }
        }

        public static void HandlePrivateMessage(String Message)
        {
            FBInvitation fbInvite = JsonConvert.DeserializeObject<FBInvitation>(Message);
            switch (fbInvite.MessageCode)
            {
                case MessageCode.INVITEDBYFRIEND:
                    if (fbInvite.RemoteFBId.Equals(GlobalContext.UserFacebookId))
                    {
                        Deployment.Current.Dispatcher.BeginInvoke(delegate()
                        {
                            MessageBoxResult result = MessageBox.Show("Your friend " + fbInvite.HostFBName + " inviting you to play a quiz game with him", "Invitation", MessageBoxButton.OKCancel);
                            if (result == MessageBoxResult.OK)
                            {
                                if (!GlobalContext.GameRoomId.Equals(""))
                                {
                                    WarpClient.GetInstance().LeaveRoom(GlobalContext.GameRoomId);
                                    WarpClient.GetInstance().JoinRoom(fbInvite.RoomId);
                                }
                                else
                                {
                                    WarpClient.GetInstance().JoinRoom(fbInvite.RoomId);
                                }

                            }
                            else if (result == MessageBoxResult.Cancel)
                            {
                                fbInvite.MessageCode = MessageCode.INVITEFRIENDRESPONSE;
                                WarpClient.GetInstance().sendPrivateChat(fbInvite.HostFBName, JsonConvert.SerializeObject(fbInvite, Formatting.None));
                            }
                        });
                    }
                    break;
                case MessageCode.INVITEFRIENDRESPONSE:
                    Deployment.Current.Dispatcher.BeginInvoke(delegate()
                       {
                           if (fbInvite.HostFBId.Equals(GlobalContext.UserFacebookId))
                           {
                               MessageBoxResult result = MessageBox.Show("Your friend has denied your request", "Invitation Response", MessageBoxButton.OK);
                               App.g_HomePageListener.ClosePopup();
                           }
                       });
                    break;

            }
        }

        public static void SendInvitation()
        {
            FBInvitation FBInvitation = new FBInvitation();
            FBInvitation.MessageCode = MessageCode.INVITEDBYFRIEND;
            FBInvitation.RoomId = GlobalContext.GameRoomId;
            FBInvitation.HostFBName = GlobalContext.localUsername;
            FBInvitation.HostFBId = GlobalContext.UserFacebookId;
            FBInvitation.RemoteFBId = GlobalContext.RemoteFacebookId;
            String jsonstring = JsonConvert.SerializeObject(FBInvitation, Formatting.None);
            WarpClient.GetInstance().sendPrivateChat(GlobalContext.RemoteFacebookName, jsonstring);
        }
    }
}
