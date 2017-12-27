/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.appwarp.s2.util;

import java.util.ArrayList;

/**
 *
 * @author rahulwindows
 */
public class QuestionPacket {

    public int Id;
    public QuizQuestion Question;
    public ArrayList<QuizElement> Options;

    public QuestionPacket(int Id, QuizQuestion Question, ArrayList<QuizElement> Options) {
        this.Id = Id;
        this.Question = Question;
        this.Options = Options;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public QuizQuestion getQuestion() {
        return Question;
    }

    public void setQuestion(QuizQuestion Question) {
        this.Question = Question;
    }

    public ArrayList<QuizElement> getOptions() {
        return Options;
    }

    public void setOptions(ArrayList<QuizElement> Options) {
        this.Options = Options;
    }
}
