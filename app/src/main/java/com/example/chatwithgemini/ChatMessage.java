package com.example.chatwithgemini;

public class ChatMessage {

    private String message;
    private boolean isUser; // true for user messages, false for AI messages

    public ChatMessage(String message, boolean isUser) {
        this.message = message;
        this.isUser = isUser;
    }

    public String getMessage() {
        return message;
    }

    public boolean isUser() {
        return isUser;
    }
}
