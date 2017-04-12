package com.iavorskyi.gpstest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.iavorskyi.gpstest.R;
import com.iavorskyi.gpstest.services.GpsTrackingService;

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
        driver1Button.setOnClickListener(this);
        driver2Button.setOnClickListener(this);
        driver3Button.setOnClickListener(this);
    }

    public boolean checkPermissions() {
        //TODO implement. Check gps and write storage permissions
        return true;
    }

    @Override
    public void onClick(View v) {
        if (checkPermissions()) {
            Intent intent = new Intent(this, GpsTrackingService.class);
            //TODO problems with this users
//            switch (v.getId()) {
//                case R.id.driver1: intent.putExtra(Constants.DRIVER_EXTRA, 1);
//                    break;
//                case R.id.driver2: intent.putExtra(Constants.DRIVER_EXTRA, 2);
//                    break;
//                case R.id.driver3: intent.putExtra(Constants.DRIVER_EXTRA, 3);
//                    break;
//            }
            startService(intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "We have no permissions!!", Toast.LENGTH_LONG).show();
        }
    }
}
