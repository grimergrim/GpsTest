package com.iavorskyi.gpstest.entities;

import android.location.Location;

import com.iavorskyi.gpstest.utils.TimeAndDateUtils;

public class GpsEntity {

    private double longitude;
    private double latitude;
    private String time;
    private float accuracy;
    private float speed;

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
