package com.example.chatapp;

public class User {
    public String uid; // Tambah untuk kemudahan
    public String name;
    public String email;
    public String status;
    public String profilePic;

    public User() {}

    public User(String name, String email, String status, String profilePic) {
        this.name = name;
        this.email = email;
        this.status = status;
        this.profilePic = profilePic;
    }
}