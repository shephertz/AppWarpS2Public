using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace QuizMania.QuizModels
{
    public class ScorePacket
    {
        public String user {get;set;}
        public int currentLevelScore { get; set; }
        public int totalScore { get; set; }
    }
}
