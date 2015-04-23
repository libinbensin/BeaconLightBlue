package com.bean.lightblue.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.bean.lightblue.R;
import com.bean.lightblue.activities.HomeActivity_;
import com.bean.lightblue.manager.BeaconManager;
import com.bean.lightblue.model.Beacon;
import com.bean.lightblue.scanner.BLeScanner;
import com.bean.lightblue.scanner.LollipopBLeScanner_;
import com.bean.lightblue.scanner.PreLollipopBLeScanner_;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.SystemService;

/**
 *
 * @author Libin
 */
@EService
public class BeaconService extends Service implements BLeScanner.BleDeviceListener{

    private static final int NOTIFICATION_ID = 0x2792; // some random

    private BLeScanner mBLeScanner;

    @SystemService
    NotificationManager mNotificationManager;

    @Bean
    BeaconManager mBeaconManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void setup () {
        if(mBLeScanner == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBLeScanner = LollipopBLeScanner_.getInstance_(this);
            } else {
                mBLeScanner = PreLollipopBLeScanner_.getInstance_(this);
            }
            mBLeScanner.setBleDeviceListener(this);
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            setup();
            mBLeScanner.startScanner();
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        if(mBLeScanner != null) {
            mBLeScanner.stopScanner();
        }
        super.onDestroy();
    }

    @Override
    public void onBLeDeviceFound(Beacon beacon) {
        if(mBeaconManager.isThisNewBeacon(beacon)){
            mBeaconManager.addBeacon(beacon);
            showNotification(beacon);
        }
    }

    private void showNotification(Beacon beacon) {
        Intent resultIntent = new Intent(this, HomeActivity_.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this.getApplicationContext() , 0,
                resultIntent ,PendingIntent.FLAG_UPDATE_CURRENT );
        NotificationCompat.Action action = new NotificationCompat.Action(R.mipmap.ic_launcher , "Check-In" , resultPendingIntent);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText("Found new beacon (" + beacon.getDeviceName() + "). Do you like to check-in now ?")
                        .setContentIntent(resultPendingIntent)
                        .addAction(action);

        mNotificationManager.notify(NOTIFICATION_ID , mBuilder.build());
    }
}
