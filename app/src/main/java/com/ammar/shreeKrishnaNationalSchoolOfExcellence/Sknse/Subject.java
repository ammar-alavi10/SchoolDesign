package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

public class Subject {
    String subject_name;
    String class_name;

    public Subject(){

    }

    public Subject(String subject_name, String class_name) {
        this.subject_name = subject_name;
        this.class_name = class_name;
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
