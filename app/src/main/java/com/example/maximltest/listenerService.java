package com.example.maximltest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class listenerService extends Service {
    ClipboardManager cm;

    public static final String CHANNEL_ID="copyService";
    //NotificationManager nm;

    public listenerService() {
    }

    @Override
    public void onCreate() {

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("SERVICE STARTED");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText("copy Check")
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
        cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        cm.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                if (cm.hasPrimaryClip()) {
                    String url = cm.getPrimaryClip().getItemAt(0).getText().toString();
                    System.out.println(url+"IN SERVICEEEE");
                    Intent intent=new Intent();
                    intent.putExtra("URL",url);
                    intent.setAction("URL-COPIED");
                    Log.i("clipboard", "changed to:" + url);
                    sendBroadcast(intent);

                    stopSelf();
                }

            }
        });

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}

