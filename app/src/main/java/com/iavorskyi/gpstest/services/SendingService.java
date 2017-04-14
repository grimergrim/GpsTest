package com.iavorskyi.gpstest.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.iavorskyi.gpstest.R;
import com.iavorskyi.gpstest.tasks.SendCoordinatesTask;
import com.iavorskyi.gpstest.ui.MainActivity;
import com.iavorskyi.gpstest.utils.FileUtils;
import com.iavorskyi.gpstest.utils.InternetUtils;

public class SendingService extends Service implements SendingFinishedListener{

    private static final int ONGOING_NOTIFICATION_ID = 151119842;
    private FileUtils mFileUtils;
    private boolean is_now_sending;

    @Override
    public void onCreate() {
        is_now_sending = true;
        mFileUtils = new FileUtils();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putBoolean("IS_NOW_SENDING", true);
        editor.commit();
        mFileUtils.writeLogToFile("Sending service started", "");
        startForeground(ONGOING_NOTIFICATION_ID, buildNotification());
        if (!is_now_sending) {
            if (new InternetUtils(getApplicationContext()).isInternetConnected()) {
                SendCoordinatesTask sendCoordinatesTask = new SendCoordinatesTask();
                sendCoordinatesTask.setListener(this);
                sendCoordinatesTask.setContext(getApplicationContext());
                sendCoordinatesTask.execute();
            } else {
                new FileUtils().writeLogToFile("no internet", this.getClass().toString());
                stopService();
            }
        } else {
            new FileUtils().writeLogToFile("Trying to send when sending.", this.getClass().toString());
        }

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        is_now_sending = false;
        mFileUtils.writeLogToFile("Sending service destroyed", "");
        super.onDestroy();
    }

    private Notification buildNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        return new Notification.Builder(this)
                .setContentTitle(getText(R.string.sending_notification_title))
                .setContentText(getText(R.string.sending_notification_message))
                .setSmallIcon(R.drawable.ic_backup_black_24dp)
                .setContentIntent(pendingIntent)
                .setTicker(getText(R.string.sending_ticker_text))
                .build();
    }

    @Override
    public void sendingFinished() {
        mFileUtils.writeLogToFile("Sending service finished its work", "");
        stopService();
    }

    private void stopService() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putBoolean("IS_NOW_SENDING", false);
        editor.commit();
        Log.e("=============", "service stopping");
        stopForeground(true);
        stopSelf();
    }

}
