using com.shephertz.app42.gaming.multiplayer.client;
using com.shephertz.app42.gaming.multiplayer.client.command;
using com.shephertz.app42.gaming.multiplayer.client.events;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Windows;
using System.Windows.Threading;

namespace QuizMania
{
    public class ConnectionListener : com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener
    {
        static int _recoverCounts = 0;
        DispatcherTimer timer;
        public ConnectionListener()
        {

        }

        public void onConnectDone(ConnectEvent eventObj)
        {
            switch (eventObj.getResult())
            {
                case WarpResponseResultCode.SUCCESS:
                    Debug.WriteLine("\nConnection Success\n");
                     GlobalContext.IsConnectedToAppWarp = true;
                     _recoverCounts = 0;                 
                     Deployment.Current.Dispatcher.BeginInvoke(delegate()
                     {
                         if (GlobalContext.tableProperties["IsPrivateRoom"].Equals("false"))
                             WarpClient.GetInstance().JoinRoomWithProperties(GlobalContext.tableProperties);
                     });
                      break;
                case WarpResponseResultCode.CONNECTION_ERROR_RECOVERABLE:
                     Deployment.Current.Dispatcher.BeginInvoke(delegate(){RecoverConnection();});
                      break;
                case WarpResponseResultCode.SUCCESS_RECOVERED:
                     break;
                default:
                     if(App.CurrentPage.Equals("HomePage"))
                      Deployment.Current.Dispatcher.BeginInvoke(delegate() { App.g_HomePageListener.ConnectionFailed(); });
                      GlobalContext.IsConnectedToAppWarp = false;
                      break;
            
            }

        }

        public void RecoverConnection()
        {
            if (_recoverCounts == 0)
            {
                timer = new DispatcherTimer();
                //Timer for Connection Recover:trying to reconnect in every 10 second,Since i have set recovery allowance to 60 seconds so 
                //it will try for 6 times i.e _recoverCounts<=6
                timer.Tick += timer_Tick;
                timer.Interval = new TimeSpan(0, 0, 0, 10);
                timer.Start();
            }
        
        }
        public void ConnectionRecovered()
        {
            timer.Stop();
            _recoverCounts = 0;
        }
        private void timer_Tick(object sender, EventArgs e)
        {
            _recoverCounts++;
            if (_recoverCounts <= 6)
            {
                WarpClient.GetInstance().RecoverConnection();
            }
            else
            {
                (sender as DispatcherTimer).Stop();
                if (App.CurrentPage.Equals("HomePage"))
                Deployment.Current.Dispatcher.BeginInvoke(delegate() 
                { 
                    App.g_HomePageListener.DisconnectCallback(); 
                });
                GlobalContext.IsConnectedToAppWarp = false;
            }
            
        }

        public void onDisconnectDone(ConnectEvent eventObj)
        {
            Deployment.Current.Dispatcher.BeginInvoke(delegate() 
            { 
                GlobalContext.IsConnectedToAppWarp = false; 
                if (App.CurrentPage.Equals("HomePage"))
                    App.g_HomePageListener.DisconnectCallback(); 
            });
        }


        public void onInitUDPDone(byte ResultCode)
        {
          
        }
    }
}
