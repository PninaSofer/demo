package com.example.demo.objects;

public class MessageObject {
    private int id;
    private String sender;
    private String receiver;
    private String sendDate;
    private String readDate;
    private String title;
    private String content;

    public MessageObject(){

    }
    public MessageObject(int id, String sender, String receiver, String title, String content, String sendDate, String readDate) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.sendDate = sendDate;
        this.readDate = readDate;
        this.title = title;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSenderId() {
        return sender;
    }

    public void setSenderId(String senderId) {
        this.sender = senderId;
    }

    public String getReceiverId() {
        return receiver;
    }

    public void setReceiverId(String receiverI) {
        this.receiver = receiver;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getReadDate() {
        return readDate;
    }

    public void setReadDate(String readDate) {
        this.readDate = readDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
