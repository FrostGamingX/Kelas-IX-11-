package com.example.blogapp.models;

public class User {
    public String uid;
    public String username;
    public String email;
    public String photoUrl;

    public User() {}

    public User(String uid, String username, String email, String photoUrl) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.photoUrl = photoUrl;
    }
}