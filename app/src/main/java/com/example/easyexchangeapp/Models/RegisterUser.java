package com.example.easyexchangeapp.Models;

import java.util.ArrayList;
import java.util.List;

public class RegisterUser {
    String userName;
    String userEmail;
    String userPhoneNo;
    List<String> chatRooms=new ArrayList<>();

    public  RegisterUser(){

    }

    public RegisterUser(String userName, String userEmail, String userPhoneNo) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhoneNo = userPhoneNo;
    }

    public List<String> getChatRooms() {
        return chatRooms;
    }

    public void setChatRooms(List<String> chatRooms) {
        this.chatRooms = chatRooms;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhoneNo() {
        return userPhoneNo;
    }

    public void setUserPhoneNo(String userPhoneNo) {
        this.userPhoneNo = userPhoneNo;
    }
}
