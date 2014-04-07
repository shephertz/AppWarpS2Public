using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace QuizMania.QuizModels
{
    public interface QuizPageListener
    {
        void ReceivedStartLevelPacket(int levelNumber, QuestionPacket questionPacket);
        void ReceivedNewQuestion(int lastQuestionAnswer, QuestionPacket questionPacket);
        void ReceivedScorePacket(ScorePacket _Score);
        void ReceivedLevelEndPacket(int levelNumber,int resultType, List<ScorePacket> _Scores);
        void OpponentLeftRoom();
    }
}
