using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace QuizMania.QuizModels
{
  public class QuestionPacket
    {
        public int Id;
        public QuizQuestion Question;
        public List<QuizElement> Options;

        public QuestionPacket(int Id, QuizQuestion Question, List<QuizElement> Options)
        {
            this.Id = Id;
            this.Question = Question;
            this.Options = Options;
        }

        public int getId()
        {
            return Id;
        }

        public void setId(int Id)
        {
            this.Id = Id;
        }

        public QuizQuestion getQuestion()
        {
            return Question;
        }

        public void setQuestion(QuizQuestion Question)
        {
            this.Question = Question;
        }

        public List<QuizElement> getOptions()
        {
            return Options;
        }

        public void setOptions(List<QuizElement> Options)
        {
            this.Options = Options;
        }
    }
}
