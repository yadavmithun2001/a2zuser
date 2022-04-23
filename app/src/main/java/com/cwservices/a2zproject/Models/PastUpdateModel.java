package com.cwservices.a2zproject.Models;

public class PastUpdateModel {
    String date,status,time_status;

    public PastUpdateModel(String date, String status, String time_status) {
        this.date = date;
        this.status = status;
        this.time_status = time_status;
    }
    public PastUpdateModel(){

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime_status() {
        return time_status;
    }

    public void setTime_status(String time_status) {
        this.time_status = time_status;
    }
}
