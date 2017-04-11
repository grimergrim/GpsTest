package com.iavorskyi.gpstest.entities;

import android.location.Location;

import com.iavorskyi.gpstest.utils.TimeAndDateUtils;

public class GpsEntity {

    private double longitude;
    private double latitude;
    private String time;
    private double accuracy;
    private double speed;

    public GpsEntity() {
    }

    public GpsEntity(Location location) {
        this.longitude = location.getLongitude();
        this.latitude = location.getLatitude();
        this.accuracy = location.getAccuracy();
        this.speed = location.getSpeed();
        this.time = TimeAndDateUtils.colverMillisecondsToAmtTimeFormat(location.getTime());
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
