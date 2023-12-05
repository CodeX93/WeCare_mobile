package com.example.wecare_doc;

public class Notification {
    private String id,title,content,type,senderId,receiverId,imageURL;             // Unique identifier for the notification
    private String timestamp;
    private boolean isRead;
    public Notification(String id, String title, String content, String type, String senderId, String receiverId, String imageURL, String timestamp, boolean isRead) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.type = type;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.imageURL = imageURL;
        this.timestamp = timestamp;
        this.isRead = isRead;
    }

    public Notification() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
