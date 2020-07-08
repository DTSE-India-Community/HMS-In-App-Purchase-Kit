package com.huawei.hmsinapppurchaseexample.model;
/**
 * Name of the project QUIZ MANIA.
 * Created by Sanghati Mukherjee.
 * Huawei Technologies Co., Ltd.
 * sanghati.mukherjee@huawei.com
 */

public class Questions {

    private int id;
    private String topic;
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String answer;

    public Questions(String topc,String ques, String opta, String optb, String optc, String optd, String ans) {
        topic = topc;
        question = ques;
        optionA = opta;
        optionB = optb;
        optionC = optc;
        optionD = optd;
        answer = ans;
    }

    public Questions() {
        id = 0;
        topic ="";
        question = "";
        optionA = "";
        optionB = "";
        optionC = "";
        optionD = "";
        answer = "";
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}
