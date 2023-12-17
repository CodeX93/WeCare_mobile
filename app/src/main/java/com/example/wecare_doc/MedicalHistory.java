package com.example.wecare_doc;

public class MedicalHistory {
    private String id;
    private String testType;
    private String title;
    private String date;
    private String documentUrl;

    public MedicalHistory() {
        // Required empty constructor for Firebase Firestore deserialization
    }

    public MedicalHistory(String id, String testType, String title, String date, String documentUrl, String user) {
        this.id = id;
        this.testType = testType;
        this.title = title;
        this.date = date;
        this.documentUrl = documentUrl;
        this.userId = user;
    }

    private String userId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public MedicalHistory(String id, String testType, String title, String date, String documentUrl) {
        this.id = id;
        this.testType = testType;
        this.title = title;
        this.date = date;
        this.documentUrl = documentUrl;
    }

    // Add getters and setters as needed
}
