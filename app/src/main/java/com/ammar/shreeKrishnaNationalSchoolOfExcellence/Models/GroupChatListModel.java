package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models;

import java.util.HashMap;

public class GroupChatListModel {
    private String groupId, createdAt, groupTitle, groupDesc;
    private HashMap<String, Object> participants;

    public GroupChatListModel(String groupId, String createdAt, String groupTitle, String groupDesc, HashMap<String, Object> participants) {
        this.groupId = groupId;
        this.createdAt = createdAt;
        this.groupTitle = groupTitle;
        this.groupDesc = groupDesc;
        this.participants = participants;
    }

    public GroupChatListModel() {
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public HashMap<String, Object> getParticipants() {
        return participants;
    }

    public void setParticipants(HashMap<String, Object> participants) {
        this.participants = participants;
    }
}
