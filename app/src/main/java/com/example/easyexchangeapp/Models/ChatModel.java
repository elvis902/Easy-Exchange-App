package com.example.easyexchangeapp.Models;

public class ChatModel {
    String sender;
    String message;
    String senderID;

    public ChatModel(String sender, String message,String senderID) {
        this.sender = sender;
        this.senderID=senderID;
        this.message = message;
    }

    public ChatModel(){

    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
