package com.iavorskyi.gpstest;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class GpsTrackingService extends Service {

    private static final int ONGOING_NOTIFICATION_ID = 15111984;

//    private Thread mThread;
    private GpsCoordinatesProvider mGpsCoordinatesProvider;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("== tracking service ==", "onCreate");
        mGpsCoordinatesProvider = new GpsCoordinatesProvider(this.getApplicationContext());
        mGpsCoordinatesProvider.connect();
//        mThread = new Thread("GPS tracking mThread") {
//            @Override
//            public void run() {
//                trackGps();
//            }
//        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(ONGOING_NOTIFICATION_ID, buildNotification());

//        if (mThread != null && !mThread.isAlive()) {
//            mThread.start();
//            startForeground(ONGOING_NOTIFICATION_ID, buildNotification());
//        }
        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.e("== tracking service ==", "onDestroy");
//        if (mThread != null && !mThread.isInterrupted()) {
//            mThread.interrupt();
//        }
        if (mGpsCoordinatesProvider != null)
            mGpsCoordinatesProvider.disconnect();
        super.onDestroy();
    }

//    private void trackGps() {
//        while (true) {
//            Log.e("== tracking service ==", "Running. Time: " + System.currentTimeMillis());
//            try {
//                Thread.sleep(3 * 1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }

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
