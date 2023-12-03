package com.example.wecare_doc;

import android.os.Parcelable;

import java.io.Serializable;

public class Doctor implements Serializable {

    public String dotorName;
    public String doctorSpeciality;
    public String uid;
    public int rating;

    public String experience, patients, information;

    public Doctor(){

    }

    public Doctor(String dotorName, String doctorSpeciality, String uid) {
        this.dotorName = dotorName;
        this.doctorSpeciality = doctorSpeciality;
        this.uid = uid;
    }

    public int getRating() {
        return rating;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getPatients() {
        return patients;
    }

    public void setPatients(String patients) {
        this.patients = patients;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDotorName() {
        return dotorName;
    }

    public void setDotorName(String dotorName) {
        this.dotorName = dotorName;
    }

    public String getDoctorSpeciality() {
        return doctorSpeciality;
    }

    public void setDoctorSpeciality(String doctorSpeciality) {
        this.doctorSpeciality = doctorSpeciality;
    }
}
