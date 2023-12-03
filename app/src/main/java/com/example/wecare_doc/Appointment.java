package com.example.wecare_doc;

import java.io.Serializable;

public class Appointment implements Serializable {

    public String date, time, doctorUid, patientUid, doctorName;

    public Appointment(){

    }
    public Appointment(String date, String time, String doctorUid, String patientUid, String doctorName) {
        this.date = date;
        this.time = time;
        this.doctorUid = doctorUid;
        this.patientUid = patientUid;
        this.doctorName=doctorName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDoctorUid() {
        return doctorUid;
    }

    public void setDoctorUid(String doctorUid) {
        this.doctorUid = doctorUid;
    }

    public String getPatientUid() {
        return patientUid;
    }

    public void setPatientUid(String patientUid) {
        this.patientUid = patientUid;
    }
}
