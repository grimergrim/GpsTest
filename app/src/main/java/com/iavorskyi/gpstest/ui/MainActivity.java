package com.iavorskyi.gpstest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.iavorskyi.gpstest.services.GpsTrackingService;
import com.iavorskyi.gpstest.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (checkPermissions()) {
            Intent intent = new Intent(this, GpsTrackingService.class);
            startService(intent);
        }
        finish();
    }

    public boolean checkPermissions() {
        //TODO implement. Check gps and write storage permissions
        return true;
    }

}
