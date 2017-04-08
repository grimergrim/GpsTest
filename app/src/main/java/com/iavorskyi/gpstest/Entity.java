package com.iavorskyi.gpstest;

class Entity {

    private double longitude;
    private double latitude;
    private String time;

    Entity(double longitude, double latitude, String time) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.time = time;
    }

    double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
