package com.iavorskyi.gpstest.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.iavorskyi.gpstest.Constants;
import com.iavorskyi.gpstest.R;
import com.iavorskyi.gpstest.gps.GpsCoordinatesProvider;
import com.iavorskyi.gpstest.tasks.LoginTask;
import com.iavorskyi.gpstest.ui.MainActivity;

public class GpsTrackingService extends Service {

    public static String CURRENT_DRIVER_ID;
    public static String CURRENT_DRIVER_LOGIN;
    public static String CURRENT_DRIVER_PASSWORD;

    private static final int ONGOING_NOTIFICATION_ID = 15111984;
    private GpsCoordinatesProvider mGpsCoordinatesProvider;

    @Override
    public void onCreate() {
        super.onCreate();

        CURRENT_DRIVER_ID = Constants.DRIVER_ONE_ID;
        CURRENT_DRIVER_LOGIN = Constants.DRIVER_ONE_LOGIN;
        CURRENT_DRIVER_PASSWORD = Constants.DRIVER_ONE_PASSWORD;

        mGpsCoordinatesProvider = new GpsCoordinatesProvider(this.getApplicationContext());
        mGpsCoordinatesProvider.connect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int driverNumber = intent.getIntExtra(Constants.DRIVER_EXTRA, 0);
        switch (driverNumber) {
            case 1:
                CURRENT_DRIVER_ID = Constants.DRIVER_ONE_ID;
                CURRENT_DRIVER_LOGIN = Constants.DRIVER_ONE_LOGIN;
                CURRENT_DRIVER_PASSWORD = Constants.DRIVER_ONE_PASSWORD;
                break;
            case 2:
                CURRENT_DRIVER_ID = Constants.DRIVER_TWO_ID;
                CURRENT_DRIVER_LOGIN = Constants.DRIVER_TWO_LOGIN;
                CURRENT_DRIVER_PASSWORD = Constants.DRIVER_TWO_PASSWORD;
                break;
            case 3:
                CURRENT_DRIVER_ID = Constants.DRIVER_THREE_ID;
                CURRENT_DRIVER_LOGIN = Constants.DRIVER_THREE_LOGIN;
                CURRENT_DRIVER_PASSWORD = Constants.DRIVER_THREE_PASSWORD;
                break;
        }
        LoginTask loginTask = new LoginTask();
        loginTask.setContext(getApplicationContext());
        loginTask.execute(CURRENT_DRIVER_LOGIN, CURRENT_DRIVER_PASSWORD);
        startForeground(ONGOING_NOTIFICATION_ID, buildNotification());
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (mGpsCoordinatesProvider != null)
            mGpsCoordinatesProvider.disconnect();
        super.onDestroy();
    }

    private Notification buildNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        return new Notification.Builder(this)
                .setContentTitle(getText(R.string.notification_title))
                .setContentText(getText(R.string.notification_message))
                .setSmallIcon(R.drawable.ic_place_black_24dp)
                .setContentIntent(pendingIntent)
                .setTicker(getText(R.string.ticker_text))
                .build();
    }

}
