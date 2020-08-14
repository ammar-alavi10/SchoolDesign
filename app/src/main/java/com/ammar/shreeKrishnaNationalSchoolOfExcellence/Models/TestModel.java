package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models;

import java.util.List;

public class TestModel {
    private String testTitle, subject_name, testTime;
    private List<Question> questions;
    private int no_of_ques;

    public TestModel(String testTitle, String subject_name, String testTime, List<Question> questions, int no_of_ques) {
        this.testTitle = testTitle;
        this.subject_name = subject_name;
        this.testTime = testTime;
        this.questions = questions;
        this.no_of_ques = no_of_ques;
    }

    public TestModel() {
    }

    public String getTestTime() {
        return testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }

    public int getNo_of_ques() {
        return no_of_ques;
    }

    public void setNo_of_ques(int no_of_ques) {
        this.no_of_ques = no_of_ques;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getTestTitle() {
        return testTitle;
    }

    public void setTestTitle(String testTitle) {
        this.testTitle = testTitle;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
