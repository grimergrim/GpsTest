package com.iavorskyi.gpstest.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.iavorskyi.gpstest.R;
import com.iavorskyi.gpstest.gps.GpsCoordinatesProvider;
import com.iavorskyi.gpstest.tasks.LoginTask;
import com.iavorskyi.gpstest.ui.MainActivity;

public class GpsTrackingService extends Service {

    private static final String LOGIN = "297";
    private static final String PASSWORD = "297";
    private static final int ONGOING_NOTIFICATION_ID = 15111984;
    private GpsCoordinatesProvider mGpsCoordinatesProvider;

    @Override
    public void onCreate() {
        super.onCreate();
        LoginTask loginTask = new LoginTask();
        loginTask.setContext(getApplicationContext());
        loginTask.execute(LOGIN, PASSWORD);
        mGpsCoordinatesProvider = new GpsCoordinatesProvider(this.getApplicationContext());
        mGpsCoordinatesProvider.connect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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
        //TODO make cool notification
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
