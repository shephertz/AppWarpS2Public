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
using Microsoft.Phone.Controls;
using System.Windows.Threading;
using QuizMania.QuizModels;
using com.shephertz.app42.gaming.multiplayer.client;
using System.IO;
using QuizMania.AppWarpS2;
using System.Diagnostics;

namespace QuizMania
{
    public partial class quizpage : PhoneApplicationPage, QuizPageListener
    {
        DispatcherTimer _QuizTimer = new DispatcherTimer();
        QuestionPacket currentQuestion;
        Storyboard mCorrectStoryboard, mWrongStoryboard;
        int _quizTimeCount = 10, lastAnswerClient = -1;
        bool hasAnswered = false;
        bool hasOpponentLeftTheRoom;
        public quizpage()
        {
            InitializeComponent();
            setInitials();
            App.g_QuizPageListener = this;
            _QuizTimer.Interval = new TimeSpan(0, 0, 0, 1);
            _QuizTimer.Tick += _QuizTimer_Tick;
            SendStartQuizPacket();
        }

        void setInitials()
        {
            txtLocalUser.Text = GlobalContext.localUsername;
            txtRemoteUser.Text = GlobalContext.opponentName;
            txtLocalUserScore.Text = txtRemoteUserScore.Text = "0";
            txtTimeCount.Text = _quizTimeCount.ToString();
            mCorrectStoryboard = new Storyboard();
            mCorrectStoryboard.Children.Add(getColorAnimationFrames(Colors.White, Colors.Green));
            Storyboard.SetTargetProperty(
                mCorrectStoryboard, new PropertyPath("(Shape.Fill).(SolidColorBrush.Color)"));
            mCorrectStoryboard.Completed += mCorrectStoryboard_Completed;
            mWrongStoryboard = new Storyboard();
            mWrongStoryboard.Children.Add(getColorAnimationFrames(Colors.White, Colors.Red));
            Storyboard.SetTargetProperty(
                mWrongStoryboard, new PropertyPath("(Shape.Fill).(SolidColorBrush.Color)"));
        }

        //AppWarp Callbacks 
        /// <summary>
        /// This Method Will be Called Whenever server send any new question to user
        /// </summary>
        /// <param name="lastAnswerServer"></param>
        /// <param name="questionPacket"></param>
        public void ReceivedStartLevelPacket(int LevelNumber, QuestionPacket questionPacket)
        {
            txtRemoteUser.Text = GlobalContext.opponentName;
            Deployment.Current.Dispatcher.BeginInvoke(delegate() { MessagePopup.Visibility = Visibility.Collapsed; });
            _QuizTimer.Stop();
            ResetAnswers();
            levelEndPopup.Visibility = Visibility.Collapsed;
            currentQuestion = questionPacket;
            setQuestionData();
        }

        /// <summary>
        /// This Method Will be Called Whenever server send any new question to user
        /// </summary>
        /// <param name="lastAnswerServer"></param>
        /// <param name="questionPacket"></param>
        public void ReceivedNewQuestion(int lastAnswerServer, QuestionPacket questionPacket)
        {
            Deployment.Current.Dispatcher.BeginInvoke(delegate() { MessagePopup.Visibility = Visibility.Collapsed; });
            _QuizTimer.Stop();
            currentQuestion = questionPacket;
            if (lastAnswerServer != -1)
            {
                StartAnimation(lastAnswerServer);
            }
            else
            {
                setQuestionData();
            }

        }

        /// <summary>
        /// After submit answer server will send the score status of all users 
        /// </summary>
        /// <param name="lastAnswerServer"></param>
        /// <param name="questionPacket"></param>
        public void ReceivedScorePacket(ScorePacket _Score)
        {
                if (_Score.user.Equals(GlobalContext.localUsername))
                {
                    txtLocalUserScore.Text = _Score.currentLevelScore.ToString();
                }
                else
                {
                    txtRemoteUser.Text = _Score.user;
                    txtRemoteUserScore.Text = _Score.currentLevelScore.ToString();
                }
        }

        /// <summary>
        /// After submit answer server will send the score status of all users 
        /// </summary>
        /// <param name="lastAnswerServer"></param>
        /// <param name="questionPacket"></param>
        public void ReceivedLevelEndPacket(int levelNumber,int ResultType,List<ScorePacket> _Scores)
        {
            int myTotalScore = 0, remoteUserTotalScore = 0;
            levelEndPopup.Visibility = Visibility.Visible;
            _QuizTimer.Stop();
            if (ResultType != QuizResponseCode.FINALRESULT)
            {
                tblMessage.Visibility = Visibility.Collapsed;
                _QuizTimer.Start();
            }
            txtLocalUserScore.Text = "0";
            txtRemoteUserScore.Text = "0";
            _quizTimeCount = 3;
            tblLevelEndCounter.Text = _quizTimeCount.ToString();
            tblLevelNumber.Text = "Level "+(levelNumber+1).ToString();
            for (int i = 0; i < _Scores.Count; i++)
            {
                if (_Scores[i].user.Equals(GlobalContext.localUsername))
                {
                    tblMyCLevelScore.Text = "Current Level Score :" + _Scores[i].currentLevelScore.ToString();
                    tblMyCTotalScore.Text = "Total Score :" + _Scores[i].totalScore.ToString();
                    myTotalScore = _Scores[i].totalScore;
                }
                else
                {
                    tblOppCLevelScore.Text = "Current Level Score :" + _Scores[i].currentLevelScore.ToString();
                    tblOppTLevelScore.Text = "Total Score :" + _Scores[i].totalScore.ToString();
                    remoteUserTotalScore = _Scores[i].totalScore;
                }
            }
            if (_Scores.Count == 1)
            {
                rmtStackPanel.Visibility = Visibility.Collapsed;
                stckOpponent.Visibility = Visibility.Collapsed;
                tblOpponentScores.Visibility = Visibility.Collapsed;
            }
            else
            {
                if (ResultType == QuizResponseCode.FINALRESULT)
                {
                    tblMessage.Visibility = Visibility.Visible;
                    if (myTotalScore > remoteUserTotalScore)
                    {
                        tblMessage.Text = "VICTORY";
                    }
                    else if (myTotalScore == remoteUserTotalScore)
                    {
                        tblMessage.Text = "TIE";
                    }
                    else
                    {
                        tblMessage.Text = "DEFEAT";
                    }
                
                }         
            }
        }

        private void _QuizTimer_Tick(object sender, EventArgs e)
        {
            if (_quizTimeCount > 0)
            {
                _quizTimeCount--;
                tblLevelEndCounter.Text = _quizTimeCount.ToString();
                txtTimeCount.Text = _quizTimeCount.ToString();
            }
        }

        private void StartAnimation(int lastAnswerServer)
        {
            mCorrectStoryboard.Stop();
            mWrongStoryboard.Stop();
            if (lastAnswerClient != -1 && lastAnswerServer != lastAnswerClient)
            {
                switch (lastAnswerClient-1)
                {
                    case 0: Storyboard.SetTarget(mWrongStoryboard, recOption1);
                        break;
                    case 1: Storyboard.SetTarget(mWrongStoryboard, recOption2);
                        break;
                    case 2: Storyboard.SetTarget(mWrongStoryboard, recOption3);
                        break;
                    case 3: Storyboard.SetTarget(mWrongStoryboard, recOption4);
                        break;
                }
                mWrongStoryboard.Begin();
            }
            switch (lastAnswerServer-1)
            {
                case 0: Storyboard.SetTarget(mCorrectStoryboard, recOption1);
                    break;
                case 1: Storyboard.SetTarget(mCorrectStoryboard, recOption2);
                    break;
                case 2: Storyboard.SetTarget(mCorrectStoryboard, recOption3);
                    break;
                case 3: Storyboard.SetTarget(mCorrectStoryboard, recOption4);
                    break;
            }
            mCorrectStoryboard.Begin();
        }

        private void setQuestionData()
        {
            lastAnswerClient = -1;
            hasAnswered = false;
            txtQuestion.Text = currentQuestion.Question.Question;
            switch (currentQuestion.Question.AttachedElement.ElementType)
            {
                case QuizElement.Type.Text:
                    break;
                case QuizElement.Type.Image:
                    break;
                case QuizElement.Type.Sound: break;
            }
            switch (currentQuestion.Options[0].ElementType)
            {
                case QuizElement.Type.Text:
                    txtOption1.Text = currentQuestion.Options[0].Value;
                    txtOption2.Text = currentQuestion.Options[1].Value;
                    txtOption3.Text = currentQuestion.Options[2].Value;
                    txtOption4.Text = currentQuestion.Options[3].Value;
                    break;
                case QuizElement.Type.Image: break;
                case QuizElement.Type.Sound: break;
            }
            _quizTimeCount = 10;
            _QuizTimer.Start();
        }
        protected override void OnNavigatedTo(System.Windows.Navigation.NavigationEventArgs e)
        {
            App.CurrentPage = "QuizPage";
            popupMessage.Text = "Waiting for an opponent..";
            MessagePopup.Visibility = Visibility.Visible;
            rmtStackPanel.Visibility = Visibility.Visible;
            if (App.IsItFAS)
            {
                App.IsItFAS = false;
                NavigationService.GoBack();
            }
            base.OnNavigatedTo(e);
        }
        protected override void OnNavigatedFrom(System.Windows.Navigation.NavigationEventArgs e)
        {
            if (!hasOpponentLeftTheRoom)
            {
                WarpClient.GetInstance().LeaveRoom(GlobalContext.GameRoomId);
            }
            base.OnNavigatedFrom(e);
        }
        protected override void OnBackKeyPress(System.ComponentModel.CancelEventArgs e)
        {
            base.OnBackKeyPress(e);
        }
        private void recOption_Tap(object sender, System.Windows.Input.GestureEventArgs e)
        {
            if (!hasAnswered)
            {
                if (_quizTimeCount > 0)
                {
                    hasAnswered = true;
                    _QuizTimer.Stop();
                    int tag = Convert.ToInt32((sender as FrameworkElement).Tag.ToString());
                    lastAnswerClient = tag + 1;
                    try
                    {
                        WarpClient.GetInstance().SendUpdatePeers(getAnswerPacket());
                    }
                    catch (Exception e1)
                    {
                        Debug.WriteLine(e1.Message);
                    }
                    switch (tag)
                    {
                        case 0: recOption1.Fill = new SolidColorBrush(Colors.Gray); break;
                        case 1: recOption2.Fill = new SolidColorBrush(Colors.Gray); break;
                        case 2: recOption3.Fill = new SolidColorBrush(Colors.Gray); break;
                        case 3: recOption4.Fill = new SolidColorBrush(Colors.Gray); break;
                    }
                }
            }
        }

        private void mCorrectStoryboard_Completed(object sender, EventArgs e)
        {
            ResetAnswers();
            mCorrectStoryboard.Stop();
            mWrongStoryboard.Stop();
            setQuestionData();
        }

        private void ResetAnswers()
        {
            switch (lastAnswerClient - 1)
            {
                case 0: recOption1.Fill = new SolidColorBrush(Colors.White); break;
                case 1: recOption2.Fill = new SolidColorBrush(Colors.White); break;
                case 2: recOption3.Fill = new SolidColorBrush(Colors.White); break;
                case 3: recOption4.Fill = new SolidColorBrush(Colors.White); break;
            }
        }

        private byte[] getAnswerPacket()
        { 
            MemoryStream memStream = new MemoryStream();
            int size = sizeof(byte) + 2 * sizeof(int);
            Debug.WriteLine(size.ToString());
            BinaryWriter buf = new BinaryWriter(memStream);
            buf.Write(QuizRequestPacket.ANSWERPACKET);
            buf.Write(IPAddress.HostToNetworkOrder(currentQuestion.Id));
            buf.Write(IPAddress.HostToNetworkOrder(lastAnswerClient));
            return memStream.ToArray();
        }

        private void SendStartQuizPacket()
        {
            MemoryStream memStream = new MemoryStream();
            BinaryWriter buf = new BinaryWriter(memStream);
            buf.Write(QuizRequestPacket.STARTQUIZ);
            WarpClient.GetInstance().SendUpdatePeers(memStream.ToArray());
            Debug.WriteLine("Send Start Quiz Packet");
        }
        private ColorAnimationUsingKeyFrames getColorAnimationFrames(Color firstColor, Color secondColor)
        {
            ColorAnimationUsingKeyFrames animationFrames = new ColorAnimationUsingKeyFrames();
            for (int i = 1; i < 6; i++)
            {
                if (i % 2 == 0)
                {   
                    DiscreteColorKeyFrame frame=new DiscreteColorKeyFrame();
                    frame.KeyTime=new TimeSpan(0,0,0,0,i*250);
                    frame.Value=firstColor;
                    animationFrames.KeyFrames.Add(frame);
                }
                else
                {
                    DiscreteColorKeyFrame frame = new DiscreteColorKeyFrame();
                    frame.KeyTime = new TimeSpan(0, 0, 0, 0, i * 250);
                    frame.Value = secondColor;
                    animationFrames.KeyFrames.Add(frame); 
                }
            }
            return animationFrames;
        }

        void QuizPageListener.OpponentLeftRoom()
        {
            hasOpponentLeftTheRoom = true;
            popupMessage.Text = "Opponent has left,Please select back and start new quiz";
            MessagePopup.Visibility = Visibility.Visible;
        }

        private void PhoneApplicationPage_Loaded(object sender, RoutedEventArgs e)
        {

        }
    }
}