package com.example.j_paschal_java_mobile_app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.core.app.NotificationCompat;


public class NotificationBroadcast extends BroadcastReceiver {
//    final String CHANNEL_ID = "843548343518343";
    @Override
    public void onReceive(Context context, Intent intent)
    {
        int id = intent.getIntExtra("notificationId", -1);
        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(new NotificationChannel(id+"", "Vacation Notifications", NotificationManager.IMPORTANCE_DEFAULT));


        Notification notification = new NotificationCompat.Builder(context, id+"").setContentText(intent.getStringExtra("notificationText")).setContentTitle(intent.getStringExtra("notificationTitle")).setSmallIcon(R.drawable.ic_launcher_background).build();
        manager.notify(id, notification);
    }

}
