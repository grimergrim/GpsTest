package com.iavorskyi.gpstest.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.iavorskyi.gpstest.Constants;
import com.iavorskyi.gpstest.R;
import com.iavorskyi.gpstest.gps.GpsCoordinatesProvider;
import com.iavorskyi.gpstest.tasks.LoginTask;
import com.iavorskyi.gpstest.ui.MainActivity;
import com.iavorskyi.gpstest.utils.FileUtils;

public class GpsTrackingService extends Service {

    public static String CURRENT_DRIVER_ID;
    public static String CURRENT_DRIVER_LOGIN;
    public static String CURRENT_DRIVER_PASSWORD;
    public static int CURRENT_TRANSPORT_ID;

    private static final int ONGOING_NOTIFICATION_ID = 15111984;
    private GpsCoordinatesProvider mGpsCoordinatesProvider;
    private FileUtils mFileUtils;

    @Override
    public void onCreate() {
        super.onCreate();
        mFileUtils = new FileUtils();
        mGpsCoordinatesProvider = new GpsCoordinatesProvider(this.getApplicationContext());
        mGpsCoordinatesProvider.connect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int driverNumber = intent.getIntExtra(Constants.DRIVER_EXTRA, 0);
        mFileUtils.writeLogToFile("GPS tracker started with driver: ", "" + driverNumber);
        saveCurrentUserInfo(driverNumber);
        mFileUtils.writeLogToFile("Login as", CURRENT_DRIVER_LOGIN);
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
        mFileUtils.writeLogToFile("GPS tracker was stoped.", "");
        if (mGpsCoordinatesProvider != null)
            mGpsCoordinatesProvider.disconnect();
        super.onDestroy();
    }

    private void saveCurrentUserInfo(int userId) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        switch (userId) {
            case 1:
                CURRENT_DRIVER_ID = Constants.DRIVER_ONE_ID;
                CURRENT_DRIVER_LOGIN = Constants.DRIVER_ONE_LOGIN;
                CURRENT_DRIVER_PASSWORD = Constants.DRIVER_ONE_PASSWORD;
                CURRENT_TRANSPORT_ID = Constants.TRANSPORT_ONE_ID;
                mFileUtils.writeLogToFile("DriverId: " + CURRENT_DRIVER_ID + " , transportId: " + CURRENT_TRANSPORT_ID, "was set as current.");
                editor.putString("driverId", CURRENT_DRIVER_ID);
                editor.putInt("transportId", CURRENT_TRANSPORT_ID);
                editor.commit();
                break;
            case 2:
                CURRENT_DRIVER_ID = Constants.DRIVER_TWO_ID;
                CURRENT_DRIVER_LOGIN = Constants.DRIVER_TWO_LOGIN;
                CURRENT_DRIVER_PASSWORD = Constants.DRIVER_TWO_PASSWORD;
                CURRENT_TRANSPORT_ID = Constants.TRANSPORT_TWO_ID;
                mFileUtils.writeLogToFile("DriverId: " + CURRENT_DRIVER_ID + " , transportId: " + CURRENT_TRANSPORT_ID, "was set as current.");
                editor.putString("driverId", CURRENT_DRIVER_ID);
                editor.putInt("transportId", CURRENT_TRANSPORT_ID);
                editor.commit();
                break;
            case 3:
                CURRENT_DRIVER_ID = Constants.DRIVER_THREE_ID;
                CURRENT_DRIVER_LOGIN = Constants.DRIVER_THREE_LOGIN;
                CURRENT_DRIVER_PASSWORD = Constants.DRIVER_THREE_PASSWORD;
                CURRENT_TRANSPORT_ID = Constants.TRANSPORT_THREE_ID;
                mFileUtils.writeLogToFile("DriverId: " + CURRENT_DRIVER_ID + " , transportId: " + CURRENT_TRANSPORT_ID, "was set as current.");
                editor.putString("driverId", CURRENT_DRIVER_ID);
                editor.putInt("transportId", CURRENT_TRANSPORT_ID);
                editor.commit();
                break;
            case 4:
                CURRENT_DRIVER_ID = Constants.DRIVER_FOUR_ID;
                CURRENT_DRIVER_LOGIN = Constants.DRIVER_FOUR_LOGIN;
                CURRENT_DRIVER_PASSWORD = Constants.DRIVER_FOUR_PASSWORD;
                CURRENT_TRANSPORT_ID = Constants.TRANSPORT_FOUR_ID;
                mFileUtils.writeLogToFile("DriverId: " + CURRENT_DRIVER_ID + " , transportId: " + CURRENT_TRANSPORT_ID, "was set as current.");
                editor.putString("driverId", CURRENT_DRIVER_ID);
                editor.putInt("transportId", CURRENT_TRANSPORT_ID);
                editor.commit();
                break;
        }
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
