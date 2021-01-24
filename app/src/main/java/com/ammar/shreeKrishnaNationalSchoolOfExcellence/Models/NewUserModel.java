package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models;

public class NewUserModel {
    private String class_name;
    private String Uid;
    private String name;
    private String email;
    private String yoa;
    private String dob;
    private String rollno;

    public NewUserModel(String class_name, String uid, String name, String email, String yoa,
                     String dob, String rollno) {
        this.class_name = class_name;
        this.Uid = uid;
        this.name = name;
        this.email = email;
        this.yoa = yoa;
        this.dob = dob;
        this.rollno = rollno;
    }

    public NewUserModel() {
    }

    public String getYoa() {
        return yoa;
    }

    public void setYoa(String yoa) {
        this.yoa = yoa;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
