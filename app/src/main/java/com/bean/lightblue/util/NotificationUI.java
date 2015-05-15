package com.bean.lightblue.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.bean.lightblue.R;
import com.bean.lightblue.activities.HomeActivity_;
import com.bean.lightblue.model.Beacon;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.SystemService;

/**
 *
 * @author Libin
 */
@EBean
public class NotificationUI {

    private static final int NOTIFICATION_ID = 0x2792; // some random

    @SystemService
    NotificationManager mNotificationManager;

    public void showNotification(Context context , Beacon beacon) {
        Intent resultIntent = new Intent(context, HomeActivity_.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context.getApplicationContext() , 0,
                resultIntent ,PendingIntent.FLAG_UPDATE_CURRENT );
        NotificationCompat.Action action = new NotificationCompat.Action(R.mipmap.ic_launcher , "Check-In" , resultPendingIntent);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText("Found new beacon (" + beacon.getDeviceName() + "). Do you like to check-in now ?")
                .setContentIntent(resultPendingIntent)
                .addAction(action);

        mNotificationManager.notify(NOTIFICATION_ID , mBuilder.build());
    }
}
