package com.example.wecare_doc;

import java.util.HashMap;
import java.util.Map;

public class Prescription {
    private Map<String, String> medicineDetails;
    private String date;
    private String patientName;
    private String patientEmail;

    // Constructor
    public Prescription() {
        // Initialize the medicineDetails map
        this.medicineDetails = new HashMap<>();
    }

    // Constructor with parameters
    public Prescription(Map<String, String> medicineDetails, String date, String patientName, String patientEmail) {
        this.medicineDetails = medicineDetails;
        this.date = date;
        this.patientName = patientName;
        this.patientEmail = patientEmail;
    }

    // Getters and Setters
    public Map<String, String> getMedicineDetails() {
        return medicineDetails;
    }

    public void setMedicineDetails(Map<String, String> medicineDetails) {
        this.medicineDetails = medicineDetails;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    // Method to add medicine details
    public void addMedicineDetail(String key, String value) {
        this.medicineDetails.put(key, value);
    }

    // Method to remove a medicine detail
    public void removeMedicineDetail(String key) {
        this.medicineDetails.remove(key);
    }
}
