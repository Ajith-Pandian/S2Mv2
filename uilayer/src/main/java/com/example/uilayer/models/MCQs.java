package com.example.uilayer.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by thoughtchimp on 12/14/2016.
 */

public class MCQs implements Serializable {
    private ArrayList<McqOptions> options;
    private int id;
    private String question, answer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<McqOptions> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<McqOptions> options) {
        this.options = options;
    }


}
