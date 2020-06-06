package com.example.busapplication;

import java.io.Serializable;


public class adStudentDataDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userName;
    private String sanctions;
    private String userID;

    public adStudentDataDTO() {

    }

    public adStudentDataDTO(String userID, String userName, String sanctions){
        this.sanctions = sanctions;
        this.userName = userName;
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSanctions() {
        return sanctions;
    }

    public void setSanctions(String sanctions) {
        this.sanctions = sanctions;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
