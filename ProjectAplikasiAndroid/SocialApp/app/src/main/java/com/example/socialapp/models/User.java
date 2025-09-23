package com.example.socialapp.models;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String email;
    private String username;
    private String profilePicUrl;
    private String coverPicUrl;
    private Map<String, Boolean> followers = new HashMap<>();
    private Map<String, Boolean> following = new HashMap<>();

    public User() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getProfilePicUrl() { return profilePicUrl; }
    public void setProfilePicUrl(String profilePicUrl) { this.profilePicUrl = profilePicUrl; }
    public String getCoverPicUrl() { return coverPicUrl; }
    public void setCoverPicUrl(String coverPicUrl) { this.coverPicUrl = coverPicUrl; }
    public Map<String, Boolean> getFollowers() { return followers; }
    public void setFollowers(Map<String, Boolean> followers) { this.followers = followers; }
    public Map<String, Boolean> getFollowing() { return following; }
    public void setFollowing(Map<String, Boolean> following) { this.following = following; }
}