package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models;

public class NoticeModel {
    private String notice, date, title, fileUrl;

    public Boolean expanded;

    public NoticeModel() {
    }

    public NoticeModel(String notice, String date, String title, String fileUrl) {
        this.notice = notice;
        this.date = date;
        this.title = title;
        this.fileUrl = fileUrl;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
