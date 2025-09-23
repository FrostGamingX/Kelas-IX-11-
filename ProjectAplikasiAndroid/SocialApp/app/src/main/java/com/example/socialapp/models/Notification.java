package com.example.socialapp.models;

public class Notification {
    private String notifId;
    private String type; // "like", "comment", "follow"
    private String fromUid;
    private String postId;
    private String storyId;
    private String text;
    private long timestamp;
    private boolean read;

    public Notification() {}

    public Notification(String type, String fromUid, String postId, String text, long timestamp) {
        this.type = type;
        this.fromUid = fromUid;
        this.postId = postId;
        this.text = text;
        this.timestamp = timestamp;
        this.read = false;
    }

    public String getNotifId() { return notifId; }
    public void setNotifId(String notifId) { this.notifId = notifId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getFromUid() { return fromUid; }
    public void setFromUid(String fromUid) { this.fromUid = fromUid; }
    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }
    public String getStoryId() { return storyId; }
    public void setStoryId(String storyId) { this.storyId = storyId; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
}