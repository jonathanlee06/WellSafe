package com.example.wellsafe.api;

public class MalaysiaData {
    public String location;
    public int confirmed;
    public int deaths;
    public int recovered;
    public int active;

    public MalaysiaData() {

    }

    public MalaysiaData(String location, int confirmed, int deaths, int recovered, int active){
        this.location = location;
        this.confirmed = confirmed;
        this.deaths = deaths;
        this.recovered = recovered;
        this.active = active;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed){
        this.confirmed = confirmed;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths){
        this.deaths = deaths;
    }
    public int getActive() {
        return active;
    }

    public void setActive(int active){
        this.active = active;
    }

}
