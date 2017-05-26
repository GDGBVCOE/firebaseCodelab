package com.example.vaibhavchellani.firebaseioextended;

/**
 * Created by vaibhavchellani on 5/19/17.
 */

public class Message {
    private String userName,userMessage;
    private String imageURL;

    public Message(){}

    public Message(String userName, String userMessage) {
        this.userName = userName;
        this.userMessage = userMessage;
        imageURL=null;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
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
