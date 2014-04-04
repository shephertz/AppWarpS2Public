using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;
using com.shephertz.app42.gaming.multiplayer.client;
using QuizMania.QuizModels;
using Facebook;
using Microsoft.Phone.Controls;
using QuizMania.AppWarpS2;
using System.IO;
using SimpleJSON;
using System.Windows.Navigation;

namespace QuizMania
{
    public partial class MainPage : PhoneApplicationPage, HomePageListener
    {
        private readonly FacebookClient _fb = new FacebookClient();
        private const String ExtendedPermissions = "read_stream,publish_stream,offline_access,publish_actions,user_location, user_birthday";
        List<FBFriend> m_FbFriendList;

        // Constructor
        public MainPage()
        {
            InitializeComponent();
            App.g_HomePageListener = this;
            WarpClient.initialize(GlobalContext.API_KEY, GlobalContext.HOST_NAME);
            GlobalContext.warpClient = WarpClient.GetInstance();
            AddListeners();
            FbFriendList.DataContext = this;
        }

        private void btnConnect_Click(object sender, RoutedEventArgs e)
        {
            GlobalContext.joinedUsers.Clear();
            //if (!GlobalContext.GameRoomId.Equals(""))
            //{
            //    WarpClient.GetInstance().LeaveRoom(GlobalContext.GameRoomId);
            //    WarpClient.GetInstance().DeleteRoom(GlobalContext.GameRoomId);
            //    WarpClient.GetInstance().Disconnect();
            //    GlobalContext.GameRoomId = "";
            //}
            if (lstQuizType != null)
            {
                switch (lstQuizType.SelectedIndex)
                {
                    //case 0: GlobalContext.MaxUsersInRoom = 1;
                    //    stckNamePanel.Visibility = Visibility.Visible;
                    //    break;
                    case 0:
                        stckNamePanel.Visibility = Visibility.Visible;
                        GlobalContext.tableProperties["IsPrivateRoom"] = "false";
                        if (GlobalContext.IsConnectedToAppWarp)
                        {
                            if (!GlobalContext.localUsername.Equals(tbxName.Text))
                            {
                                MessageBox.Show("You are alerady connected with other user name");
                                return;
                            }
                            WarpClient.GetInstance().JoinRoomWithProperties(GlobalContext.tableProperties);
                        }
                        else
                        {
                            messageGrid.Visibility = Visibility.Visible;
                            GlobalContext.GameRoomId = "";
                            GlobalContext.localUsername = tbxName.Text;
                            WarpClient.GetInstance().Connect(GlobalContext.localUsername, "");
                        }
                        break;
                    case 1:
                        LoginWithFB();
                        stckNamePanel.Visibility = Visibility.Collapsed;
                        break;
                }
            } 
        }

        public void showResult(String result)
        {
            MessageBox.Show(result);
        }

        private void AddListeners()
        {
            GlobalContext.conListenObj = new ConnectionListener();
            GlobalContext.warpClient.AddConnectionRequestListener(GlobalContext.conListenObj);
            if (GlobalContext.roomReqListenerObj == null)
            {
                GlobalContext.roomReqListenerObj = new RoomReqListener();
                GlobalContext.warpClient.AddRoomRequestListener(GlobalContext.roomReqListenerObj);
            }
            if (GlobalContext.notificationListenerObj == null)
            {
                GlobalContext.notificationListenerObj = new NotificationListener();
                WarpClient.GetInstance().AddNotificationListener(GlobalContext.notificationListenerObj);
            }
            if (GlobalContext.zoneRequestListenerobj == null)
            {
                GlobalContext.zoneRequestListenerobj = new ZoneRequestListener();
                WarpClient.GetInstance().AddZoneRequestListener(GlobalContext.zoneRequestListenerobj);
            }
        }

        public void StartQuiz()
        {
            brdFbFriendList.Visibility = Visibility.Collapsed;
            messageGrid.Visibility = Visibility.Collapsed;
            NavigationService.Navigate(new Uri("/quizpage.xaml", UriKind.Relative));
        }

        private void lstQuizType_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if (lstQuizType != null)
            {
                switch (lstQuizType.SelectedIndex)
                {
                    //case 0:
                    case 0:
                        GlobalContext.tableProperties["IsPrivateRoom"] = "false";
                        stckNamePanel.Visibility = Visibility.Visible;
                        break;
                    case 1:
                        GlobalContext.tableProperties["IsPrivateRoom"] = "true";
                        stckNamePanel.Visibility = Visibility.Collapsed;
                        break;

                }
            }
        }

        private void lstQuizTopic_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if (lstQuizTopic != null)
            {
                switch (lstQuizTopic.SelectedIndex)
                {
                    case 0: GlobalContext.tableProperties["QuizTopic"] = "Politics";
                        break;
                    case 1: GlobalContext.tableProperties["QuizTopic"] = "Cricket";
                        break;
                }
            }
        }

        private void Image_Tap_1(object sender, System.Windows.Input.GestureEventArgs e)
        {
            FacbookLayout.Visibility = Visibility.Collapsed;
        }

        //Facebook Handling
        private void LoginWithFB()
        {
            FacbookLayout.Visibility = Visibility.Visible;
            var loginUrl = GetFacebookLoginUrl(GlobalContext.FacebookAppId, ExtendedPermissions);
            _webBrowser.Navigate(loginUrl);
            _webBrowser.Navigated += WebBrowser_Navigated;
        }

        private Uri GetFacebookLoginUrl(String appId, String extendedPermissions)
        {
            var parameters = new Dictionary<String, object>();
            parameters["client_id"] = appId;
            parameters["redirect_uri"] = "https://m.facebook.com/connect/login_success.html";
            parameters["response_type"] = "token";
            parameters["display"] = "touch";
            if (!String.IsNullOrEmpty(extendedPermissions))
            {
                parameters["scope"] = extendedPermissions;
            }
            return _fb.GetLoginUrl(parameters);
        }

        void WebBrowser_Navigated(object sender, System.Windows.Navigation.NavigationEventArgs e)
        {
            FacebookOAuthResult oauthResult;
            if (!_fb.TryParseOAuthCallbackUrl(e.Uri, out oauthResult))
            {
                return;
            }
            if (oauthResult.IsSuccess)
            {
                GlobalContext.FacebookAccessToken = oauthResult.AccessToken;
                FacbookLayout.Visibility = Visibility.Collapsed;
                MyProfile();
            }
            else
            {
                MessageBox.Show(oauthResult.ErrorDescription);
            }
        }

        // My facebook feed.
        internal void MyProfile()
        {
            String name = null;
            String id = null;
            var fb = new FacebookClient(GlobalContext.FacebookAccessToken);
            fb.GetCompleted +=
                (o, ex) =>
                {

                    var feed = (IDictionary<String, object>)ex.GetResultData();
                    name = feed["name"].ToString();
                    id = feed["id"].ToString();
                    DBManager.getInstance().saveData(DBManager.DB_Profile, feed);
                    var fbprofiledata = DBManager.getInstance().getDBData(DBManager.DB_Profile);
                    JSONNode jsonObj = JSON.Parse(fbprofiledata);
                    GlobalContext.localUsername = jsonObj["name"].ToString().Split(new char[] { ' ' })[0].Substring(1);
                    GlobalContext.UserFacebookId = id;
                    GlobalContext.GameRoomId = "";
                    JSONNode AuthObj = new JSONClass();
                    AuthObj.Add("FacebookId", id);
                    AuthObj.Add("AccessToken", GlobalContext.FacebookAccessToken);
                    String authString = AuthObj.ToString();
                    Deployment.Current.Dispatcher.BeginInvoke(delegate()
                    {
                        messageGrid.Visibility = Visibility.Visible;
                        WarpClient.GetInstance().Connect(GlobalContext.localUsername, authString);
                    });
                };

            var parameters = new Dictionary<String, object>();
            parameters["fields"] = "id,name";
            fb.GetAsync("me", parameters);
        }

        protected override void OnNavigatedTo(System.Windows.Navigation.NavigationEventArgs e)
        {
            App.CurrentPage="HomePage";
            if ((e.NavigationMode == NavigationMode.Back) && GlobalContext.IsConnectedToAppWarp)
            {
                btnConnect.IsEnabled = false;
               // WarpClient.GetInstance().Disconnect();
            }
            //if (DBManager.getInstance().isDBAvailable(DBManager.DB_Profile))
            //{
            //    // btnLogout.Visibility = Visibility.Visible;
            //}
        }

        private void btnLogout_Click(object sender, RoutedEventArgs e)
        {
            DBManager.getInstance().cleanData(DBManager.DB_Profile);
            //  btnLogout.Visibility = Visibility.Collapsed;
        }

        public void DisconnectCallback()
        {
            btnConnect.IsEnabled = true;
            btnConnect.Visibility = Visibility.Visible;
            btnConnect.Content = "Connect";
        }

        public void FBFriendList(List<FBFriend> fbFriendList)
        {
            try
            {
                messageGrid.Visibility = Visibility.Collapsed;
                if (fbFriendList.Count == 0)
                {
                    brdFbFriendMessage.Visibility = Visibility.Visible;
                }
                else
                {
                    brdFbFriendList.Visibility = Visibility.Visible;
                    m_FbFriendList = fbFriendList;
                    FbFriendList.ItemsSource = m_FbFriendList;
                }
            }
            catch (Exception e)
            {

            }
        }

        private void btnInvite_Click(object sender, RoutedEventArgs e)
        {
            GlobalContext.RemoteFacebookId = (sender as Button).Tag.ToString();
            for (int i = 0; i < m_FbFriendList.Count; i++)
            {
                if (m_FbFriendList[i].facebookId.Equals(GlobalContext.RemoteFacebookId))
                {
                    GlobalContext.RemoteFacebookName = m_FbFriendList[i].username;
                }
            }
            if (GlobalContext.GameRoomId.Equals(""))
            {
                GlobalContext.AmIOwner = true;
                GlobalContext.tableProperties["IsPrivateRoom"] = "true";
                WarpClient.GetInstance().CreateRoom("QuizRoom", "QuizRoom", GlobalContext.MaxUsersInRoom, GlobalContext.tableProperties);
            }
            else
            {
                MoveMessage.SendInvitation();
            }
            messageGrid.Visibility = Visibility.Visible;
        }

        private void btnClose_Tap(object sender, System.Windows.Input.GestureEventArgs e)
        {
            brdFbFriendList.Visibility = Visibility.Collapsed;
        }

        private void CloseButton_Click(object sender, RoutedEventArgs e)
        {
            brdFbFriendMessage.Visibility = Visibility.Collapsed;
        }

        public void ClosePopup()
        {
            messageGrid.Visibility = Visibility.Collapsed;
        }


        public void ConnectionFailed()
        {
            messageGrid.Visibility = Visibility.Collapsed;
            MessageBox.Show("Connection failed");
            DisconnectCallback();
        }
    }
}