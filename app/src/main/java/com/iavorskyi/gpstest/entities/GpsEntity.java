package com.iavorskyi.gpstest.entities;

import android.location.Location;

public class GpsEntity {

    private double longitude;
    private double latitude;
    private long time;
    private double accuracy;
    private double speed;

    public GpsEntity() {
    }

    public GpsEntity(Location location) {
        this.longitude = location.getLongitude();
        this.latitude = location.getLatitude();
        this.accuracy = location.getAccuracy();
        this.speed = location.getSpeed();
        this.time = location.getTime();
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
