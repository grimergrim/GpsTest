package com.iavorskyi.gpstest.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.iavorskyi.gpstest.Constants;
import com.iavorskyi.gpstest.R;
import com.iavorskyi.gpstest.services.GpsTrackingService;
import com.iavorskyi.gpstest.utils.FileUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button driver1Button = (Button) findViewById(R.id.driver1);
        Button driver2Button = (Button) findViewById(R.id.driver2);
        Button driver3Button = (Button) findViewById(R.id.driver3);
        Button driver4Button = (Button) findViewById(R.id.driver4);
        driver1Button.setOnClickListener(this);
        driver2Button.setOnClickListener(this);
        driver3Button.setOnClickListener(this);
        driver4Button.setOnClickListener(this);
    }

    public boolean checkPermissions() {
        boolean doWeHaveAllPermissions = false;
        int coarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int fineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int readPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int wifiPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE);
        int networkPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
        if (coarseLocationPermission == PackageManager.PERMISSION_GRANTED
                && fineLocationPermission == PackageManager.PERMISSION_GRANTED
                && readPermission == PackageManager.PERMISSION_GRANTED
                && writePermission == PackageManager.PERMISSION_GRANTED
                && wifiPermission == PackageManager.PERMISSION_GRANTED
                && networkPermission == PackageManager.PERMISSION_GRANTED) {
            doWeHaveAllPermissions = true;
        } else {
            new FileUtils().writeLogToFile("Main screen. Permission check.", "We don't have all permissions");
        }
        return doWeHaveAllPermissions;
    }

    @Override
    public void onClick(View v) {
        if (checkPermissions()) {
            Intent intent = new Intent(this, GpsTrackingService.class);
            switch (v.getId()) {
                case R.id.driver1: intent.putExtra(Constants.DRIVER_EXTRA, 1);
                    break;
                case R.id.driver2: intent.putExtra(Constants.DRIVER_EXTRA, 2);
                    break;
                case R.id.driver3: intent.putExtra(Constants.DRIVER_EXTRA, 3);
                    break;
                case R.id.driver4: intent.putExtra(Constants.DRIVER_EXTRA, 4);
                    break;
            }
            startService(intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "We have no permissions!!", Toast.LENGTH_LONG).show();
        }
    }
}
