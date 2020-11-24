package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models;

public class Chapter {
    private String chapter_no;
    private String chapter_name;
    private String subject_name;
    private String class_name;

    public Chapter(String chapter_no, String chapter_name, String subject_name, String class_name) {
        this.chapter_no = chapter_no;
        this.chapter_name = chapter_name;
        this.subject_name = subject_name;
        this.class_name = class_name;
    }

    public Chapter() {
    }

    public String getChapter_no() {
        return chapter_no;
    }

    public void setChapter_no(String chapter_no) {
        this.chapter_no = chapter_no;
    }

    public String getChapter_name() {
        return chapter_name;
    }

    public void setChapter_name(String chapter_name) {
        this.chapter_name = chapter_name;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }
}
