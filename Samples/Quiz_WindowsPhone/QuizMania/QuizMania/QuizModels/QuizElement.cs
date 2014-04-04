using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace QuizMania.QuizModels
{
    public class QuizElement
    {
        public enum Type
        {
            Text=0,
            Image=1,
            Sound=2
        }
        public Type ElementType;
        public String Value;
        // Constructor
        public QuizElement(Type type, String value)
        {
            ElementType = type;
            Value = value;
        }

        public static QuizElement buildQuizElement()
        {
            return null;
        }
    }
}
