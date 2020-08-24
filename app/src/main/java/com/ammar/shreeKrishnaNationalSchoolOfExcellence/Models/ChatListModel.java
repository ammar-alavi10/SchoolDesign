package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models;

public class ChatListModel {
    private String uid, name, message;

    public ChatListModel(String uid, String name, String message) {
        this.uid = uid;
        this.name = name;
        this.message = message;
    }

    public ChatListModel() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
