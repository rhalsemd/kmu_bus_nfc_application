package com.example.busapplication;

import java.io.Serializable;


public class driverBusDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String bus_type;
    private String bus_name;
    private String userID;

    public driverBusDTO() {

    }

    public driverBusDTO(String bus_type, String bus_name, String userID){
        this.bus_type = bus_type;
        this.bus_name = bus_name;
        this.userID = userID;
    }

    public String getBus_type() {
        return bus_type;
    }

    public void setBus_type(String bus_type) {
        this.bus_type = bus_type;
    }

    public String getBus_name() {
        return bus_name;
    }

    public void setBus_name(String bus_name) {
        this.bus_name = bus_name;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
