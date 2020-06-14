package com.example.busapplication;

import java.io.Serializable;


public class adStudentStatsActivityDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    private String total_seated;


    public adStudentStatsActivityDTO() {

    }

    public adStudentStatsActivityDTO(String total_seated) {
        this.total_seated = total_seated;

    }

    public String getTotal_seated() {
        return total_seated;
    }

    public void setTotal_seated(String total_seated) {
        this.total_seated = total_seated;
    }


}
