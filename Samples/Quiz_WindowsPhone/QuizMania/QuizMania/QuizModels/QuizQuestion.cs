using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace QuizMania.QuizModels
{
    public class QuizQuestion
    {

        public String Question;
        public QuizElement AttachedElement;

        public QuizQuestion(String Question, QuizElement AttachedElement)
        {
            this.Question = Question;
            this.AttachedElement = AttachedElement;
        }

        public String getQuestion()
        {
            return Question;
        }

        public void setQuestion(String Question)
        {
            this.Question = Question;
        }

        public QuizElement getAttachedElement()
        {
            return AttachedElement;
        }

        public void setAttachedElement(QuizElement AttachedElement)
        {
            this.AttachedElement = AttachedElement;
        }
    }
}
