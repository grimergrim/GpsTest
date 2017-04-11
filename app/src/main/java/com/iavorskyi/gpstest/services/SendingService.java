package com.iavorskyi.gpstest.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.iavorskyi.gpstest.R;
import com.iavorskyi.gpstest.tasks.SendCoordinatesTask;
import com.iavorskyi.gpstest.ui.MainActivity;
import com.iavorskyi.gpstest.utils.FileUtils;
import com.iavorskyi.gpstest.utils.InternetUtils;
import com.iavorskyi.gpstest.utils.TimeAndDateUtils;

public class SendingService extends Service implements SendingFinishedListener{

    private static final int ONGOING_NOTIFICATION_ID = 151119842;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("=============", "service starting");
        startForeground(ONGOING_NOTIFICATION_ID, buildNotification());
        if (new InternetUtils(getApplicationContext()).isInternetConnected()) {
            SendCoordinatesTask sendCoordinatesTask = new SendCoordinatesTask();
            sendCoordinatesTask.setListener(this);
            sendCoordinatesTask.setContext(getApplicationContext());
            sendCoordinatesTask.execute();
        } else {
            Log.e("=============", "error");
            new FileUtils().writeErrorToFile(new TimeAndDateUtils().getDateAsStringFromSystemTime(
                    System.currentTimeMillis()), "no internet", this.getClass().toString());
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
        Log.e("=============", "service stopping");
        stopForeground(true);
        stopSelf();
    }
}
