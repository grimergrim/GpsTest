package com.iavorskyi.gpstest.rest.json;

import com.google.gson.annotations.SerializedName;
import com.iavorskyi.gpstest.entities.GpsEntity;

import java.util.ArrayList;
import java.util.List;

public class SendCoordinatesRequest {

    @SerializedName("Latitude")
    private Double latitude;
    @SerializedName("Longitude")
    private Double longitude;
    @SerializedName("Date")
    private String date;
    @SerializedName("GpsAccuracy")
    private Double gpsAccuracy;
    @SerializedName("GsmAccuracy")
    private Double gsmAccuracy;
    @SerializedName("InstantaneousSpeed")
    private Double instantaneousSpeed;
    @SerializedName("TransportId")
    private Integer transportId;
    @SerializedName("VehicleId")
    private String vehicleId;
    @SerializedName("AverageSpeed")
    private Double averageSpeed;
    @SerializedName("UserId")
    private String userId;
    @SerializedName("Phones")
    private List<String> phonesRequest = new ArrayList<>();

    public SendCoordinatesRequest(GpsEntity gpsEntity, String userId, int transportId) {
        this.userId = userId;
        this.transportId = transportId;
        this.latitude = gpsEntity.getLatitude();
        this.longitude = gpsEntity.getLongitude();
        this.instantaneousSpeed = (double) gpsEntity.getSpeed();
        this.date = String.valueOf(gpsEntity.getTime());
        this.gpsAccuracy = (double) gpsEntity.getAccuracy();
        this.gsmAccuracy = (double) gpsEntity.getAccuracy();
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getGpsAccuracy() {
        return gpsAccuracy;
    }

    public void setGpsAccuracy(Double gpsAccuracy) {
        this.gpsAccuracy = gpsAccuracy;
    }

    public Double getGsmAccuracy() {
        return gsmAccuracy;
    }

    public void setGsmAccuracy(Double gsmAccuracy) {
        this.gsmAccuracy = gsmAccuracy;
    }

    public Double getInstantaneousSpeed() {
        return instantaneousSpeed;
    }

    public void setInstantaneousSpeed(Double instantaneousSpeed) {
        this.instantaneousSpeed = instantaneousSpeed;
    }

    public Integer getTransportId() {
        return transportId;
    }

    public void setTransportId(Integer transportId) {
        this.transportId = transportId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Double getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(Double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getPhonesRequest() {
        return phonesRequest;
    }

    public void setPhonesRequest(List<String> phonesRequest) {
        this.phonesRequest = phonesRequest;
    }
}
