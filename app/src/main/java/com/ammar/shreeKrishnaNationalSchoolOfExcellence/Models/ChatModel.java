package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models;

public class ChatModel {
    String receiver, sender, message, timestamp;

    public ChatModel() {
    }

    public ChatModel(String receiver, String sender, String message, String timestamp) {
        this.receiver = receiver;
        this.sender = sender;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
