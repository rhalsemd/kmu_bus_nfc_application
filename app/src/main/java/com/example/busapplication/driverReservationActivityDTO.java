package com.example.busapplication;

import java.io.Serializable;


public class driverReservationActivityDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    private String userID;

    public driverReservationActivityDTO() {

    }

    public driverReservationActivityDTO(String userID){

        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
}
