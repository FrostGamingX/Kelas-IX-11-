package com.example.chatapp;

public class Message {
    public String messageId; // Untuk delete
    public String senderId;
    public String text;
    public long timestamp;

    public Message() {}

    public Message(String senderId, String text, long timestamp) {
        this.senderId = senderId;
        this.text = text;
        this.timestamp = timestamp;
    }
}