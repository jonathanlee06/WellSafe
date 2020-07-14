package com.example.wellsafe.ui.checkin;

public class CheckInData {

    public String location, date, time, temperature;

    public CheckInData() {

    }

    public CheckInData(String location, String date, String time, String temperature){
        this.location = location;
        this.date = date;
        this.time = time;
        this.temperature = temperature;
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

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

}
