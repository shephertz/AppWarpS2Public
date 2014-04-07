/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quizcommon;
/**
 *
 * @author rahulwindows
 */
public class QuizElement {
   public enum Type {
        Text,
        Image,
        Sound
    }
   public Type ElementType;
   public String Value;
   // Constructor
   public QuizElement(Type type, String value) {
        ElementType = type;
        Value = value;
    }
   
   public static QuizElement buildQuizElement()
   {
   return null;
   }
}
