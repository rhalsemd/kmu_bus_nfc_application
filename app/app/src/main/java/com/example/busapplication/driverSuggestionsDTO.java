package com.example.busapplication;

import java.io.Serializable;


public class driverSuggestionsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String content;
    private String time;
    private String userID;

    public driverSuggestionsDTO() {

    }

    public driverSuggestionsDTO(String title, String content, String time, String userID){
        this.title = title;
        this.content = content;
        this.time = time;
        this.userID = userID;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }

    public String getTime(){
        return time;
    }

    public void setTime(String time){
        this.time = time;
    }

    public String getuserID(){
        return userID;
    }

    public void setuserID(String userID){
        this.userID = userID;
    }

    @Override
    public String toString(){
        return "driverSuggestionsDTO{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", userID='" + userID +
                "'}";
    }

}
