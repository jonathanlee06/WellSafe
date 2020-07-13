package com.example.wellsafe.ui.checkin;

public class CheckInData {

    public String checkinID, location, date, time;

    public CheckInData() {

    }

    public CheckInData(String location, String date, String time){
        this.location = location;
        this.date = date;
        this.time = time;
    }

    public String getCheckinID() {
        return checkinID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public void setTime(String Time) {
        this.time = time;
    }

}
