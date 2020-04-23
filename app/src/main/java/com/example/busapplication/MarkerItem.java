package com.example.busapplication;

public class MarkerItem {
    double lat;
    double lon;
    String destination;

    public MarkerItem(double lat, double lon, String destination) {
        this.lat = lat;
        this.lon = lon;
        this.destination = destination;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getdestination() {
        return destination;
    }

    public void setPrice(String destination) {
        this.destination = destination;
    }
}
