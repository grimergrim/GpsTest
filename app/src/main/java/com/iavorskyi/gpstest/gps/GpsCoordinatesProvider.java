package com.iavorskyi.gpstest.gps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.iavorskyi.gpstest.entities.GpsEntity;
import com.iavorskyi.gpstest.services.SendingService;
import com.iavorskyi.gpstest.tasks.SaveDataTask;
import com.iavorskyi.gpstest.utils.FileUtils;
import com.iavorskyi.gpstest.utils.TimeAndDateUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class GpsCoordinatesProvider implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private final static int SEND_TO_SERVER_INTERVAL = 1000 * 60 * 2;
    private final static int UPDATE_INTERVAL = 1000 * 10;
    private final static int FASTEST_UPDATE_INTERVAL = 1000 * 10;
    private final static int SEND_COUNTER_MAX_VALUE = SEND_TO_SERVER_INTERVAL / UPDATE_INTERVAL;
    private int counter = 0;
    private Location mLastLocation;
    private FileUtils mFileUtils;
    private TimeAndDateUtils mTimeAndDateUtils;
    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private String mCoordinatesFileName;

    public GpsCoordinatesProvider(Context context) {
        mContext = context;
        mFileUtils = new FileUtils();
        mTimeAndDateUtils = new TimeAndDateUtils();
        mCoordinatesFileName = mFileUtils.getNewFileName();
        mLastLocation = new Location("Fake provider");
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest = createLocationRequest();
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
        editor.putBoolean("IS_NOW_SENDING", false);
        editor.commit();
    }

    public void connect() {
        if (mGoogleApiClient != null)
            mFileUtils.writeLogToFile("GpsProvider", "Connecting to gps");
            mGoogleApiClient.connect();
    }

    public void disconnect() {
        if (mGoogleApiClient != null)
            mFileUtils.writeLogToFile("GpsProvider", "Disconnecting from gps");
            mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
        mFileUtils.writeLogToFile("GpsProvider", "Connection to gps established");
    }

    @Override
    public void onLocationChanged(Location location) {
        counter++;
        boolean is_now_sending = PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean("IS_NOW_SENDING", false);
        if (mLastLocation.getLongitude() != location.getLongitude()
                && mLastLocation.getLatitude() != location.getLatitude()) {
            mLastLocation = location;
            SaveDataTask saveDataTask = new SaveDataTask();
            location.setAccuracy(roundGpsAccuracy(location.getAccuracy()));
            location.setSpeed(convertSpeedToKmPerHour(location.getSpeed()));
            Map<String, GpsEntity> parameters = new HashMap<>();
            parameters.put(mCoordinatesFileName, new GpsEntity(location));
            saveDataTask.execute(parameters);
            if (counter >= SEND_COUNTER_MAX_VALUE && !is_now_sending) {
                mContext.startService(new Intent(mContext, SendingService.class));
                mCoordinatesFileName = mFileUtils.getNewFileName();
                counter = 0;
            }
        } else {
            mFileUtils.writeLogToFile("GpsProvider", "We got same location");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (mFileUtils != null && mTimeAndDateUtils != null) {
            mFileUtils.writeLogToFile("connection to gps suspended", this.getClass().toString());
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (mFileUtils != null && mTimeAndDateUtils != null) {
            mFileUtils.writeLogToFile("connection to gps failed", this.getClass().toString());
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                mContext, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    private LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    private Float roundGpsAccuracy(Float accuracy) {
        if (accuracy != null && accuracy > 0) {
            return new BigDecimal(accuracy).setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();
        }
        return 0.0F;
    }

    private Float convertSpeedToKmPerHour(Float speedInMetersPerSecond) {
        if (speedInMetersPerSecond != null && speedInMetersPerSecond > 0) {
            return new BigDecimal(speedInMetersPerSecond * 3.6).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        }
        return 0.0F;
    }

}
