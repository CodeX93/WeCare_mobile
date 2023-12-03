package com.example.wecare_doc;

public class Doctor {

    public String dotorName;
    public String doctorSpeciality;
    public String uid;

    public int rating;

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
