package com.prgguru.example;

/**
 * Created by philippadler on 23.03.16.
 */

public class Message {

    private String email;
    private String content;
    private String date;

    public Message(){}

    public Message(String email, String date, String content) {
        this.email = email;
        this.date = date;
        this.content = content;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEmail() {
        return email;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }
}