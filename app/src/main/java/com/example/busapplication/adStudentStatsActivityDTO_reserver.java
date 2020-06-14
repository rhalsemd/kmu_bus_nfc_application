package com.example.busapplication;

import java.io.Serializable;


public class adStudentStatsActivityDTO_reserver implements Serializable {
    private static final long serialVersionUID = 1L;


    private String userID;
    private String bus_name;
    private String bus_type;


    public adStudentStatsActivityDTO_reserver() {

    }

    public adStudentStatsActivityDTO_reserver(String userID, String bus_name, String bus_type) {
        this.userID = userID;
        this.bus_name = bus_name;
        this.bus_type = bus_type;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getBus_name() {
        return bus_name;
    }

    public void setBus_name(String bus_name) {
        this.bus_name = bus_name;
    }

    public String getBus_type() {
        return bus_type;
    }

    public void setBus_type(String bus_type) {
        this.bus_type = bus_type;
    }
}
