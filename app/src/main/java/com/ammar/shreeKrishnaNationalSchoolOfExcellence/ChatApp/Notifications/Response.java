package com.ammar.shreeKrishnaNationalSchoolOfExcellence.ChatApp.Notifications;

public class Response {
    private String success;

    public Response(String success) {
        this.success = success;
    }

    public Response() {
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
