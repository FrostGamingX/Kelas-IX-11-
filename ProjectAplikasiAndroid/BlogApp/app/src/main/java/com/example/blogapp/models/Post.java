package com.example.blogapp.models;

import java.util.HashMap;

public class Post {
    public String postId;
    public String title;
    public String description;
    public String authorUid;
    public long timestamp;
    public HashMap<String, Boolean> likes = new HashMap<>();
    public HashMap<String, Boolean> saves = new HashMap<>();

    public Post() {}

    public Post(String title, String description, String authorUid) {
        this.title = title;
        this.description = description;
        this.authorUid = authorUid;
        this.timestamp = System.currentTimeMillis();
    }
}