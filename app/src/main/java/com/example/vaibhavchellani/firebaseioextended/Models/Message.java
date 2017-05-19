package com.example.vaibhavchellani.firebaseioextended.Models;

/**
 * Created by vaibhavchellani on 5/19/17.
 */

public class Message {
    private String userName,userMessage;

    public Message(){}

    public Message(String userName, String userMessage) {
        this.userName = userName;
        this.userMessage = userMessage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }
}
