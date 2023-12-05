package com.example.wecare_doc;

public class TimeSlots {

    String Date,StartTime,Duration,DoctorId,booked,EndTime;


    public TimeSlots() {
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String isBooked() {
        return booked;
    }

    public void setBooked(String booked) {
        this.booked = booked;
    }

    public String getDoctorId() {
        return DoctorId;
    }

    public void setDoctorId(String doctorId) {
        DoctorId = doctorId;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public TimeSlots(String date, String startTime, String duration, String booked, String doctorId, String endTime) {
        Date = date;
        StartTime = startTime;
        Duration = duration;
        this.booked = booked;
        this.DoctorId=doctorId;
        this.EndTime=endTime;
    }
}
