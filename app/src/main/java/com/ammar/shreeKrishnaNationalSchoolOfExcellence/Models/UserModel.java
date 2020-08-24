package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models;

public class UserModel {
    private String class_name;
    private String Uid;
    private String name;
    private String email;

    public UserModel(String class_name, String subject_name, String uid, String name, String email) {
        this.class_name = class_name;
        this.Uid = uid;
        this.name = name;
        this.email = email;
    }

    public UserModel() {
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
