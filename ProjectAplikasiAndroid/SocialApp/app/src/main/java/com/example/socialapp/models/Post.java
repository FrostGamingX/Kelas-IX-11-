package com.example.socialapp.models;

import java.util.HashMap;
import java.util.Map;

public class Post {
    private String postId;
    private String userUid;
    private String caption;
    private String imageUrl;
    private long timestamp;
    private Map<String, Boolean> likes = new HashMap<>(); // Default empty map
    private Map<String, Comment> comments = new HashMap<>(); // Default empty map

    public Post() {}

    // Constructor
    public Post(String userUid, String caption, String imageUrl, long timestamp) {
        this.userUid = userUid;
        this.caption = caption;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
        this.likes = new HashMap<>();
        this.comments = new HashMap<>();
    }

    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }

    public String getUserUid() { return userUid; }
    public void setUserUid(String userUid) { this.userUid = userUid; }

    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public Map<String, Boolean> getLikes() {
        if (this.likes == null) {
            this.likes = new HashMap<>();
        }
        return likes;
    }

    public void setLikes(Map<String, Boolean> likes) { this.likes = likes; }

    public Map<String, Comment> getComments() {
        if (this.comments == null) {
            this.comments = new HashMap<>();
        }
        return comments;
    }

    public void setComments(Map<String, Comment> comments) { this.comments = comments; }
}