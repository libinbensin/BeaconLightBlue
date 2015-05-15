package com.bean.lightblue.service;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;

import com.bean.lightblue.manager.BeaconManager;
import com.bean.lightblue.model.Beacon;
import com.bean.lightblue.scanner.BLeScanner;
import com.bean.lightblue.scanner.LollipopBLeScanner_;
import com.bean.lightblue.scanner.PreLollipopBLeScanner_;
import com.bean.lightblue.util.BeaconUtil;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;

/**
 *
 * @author Libin
 */
@EService
public class BeaconService extends Service implements BLeScanner.BleDeviceListener{

    private BLeScanner mBLeScanner;

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

        findBeacons();
        /*if(intent == null || TextUtils.isEmpty(intent.getAction())){
            stopSelf();
            return START_NOT_STICKY;
        }
        if(intent != null) {
            switch (intent.getAction()) {
                case BeaconUtil.ACTION_START_SCANNING:
                    findBeacons();
                    break;
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    BluetoothDevice device = intent.getExtras().getParcelable(BluetoothDevice.EXTRA_DEVICE);
                    if (device != null) {
                        findBeacons();
                    } else {
                        stopSelf();
                    }
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    stopSelf();
                    break;
            }
        }*/
        return super.onStartCommand(intent, flags, startId);
    }


    private void findBeacons(){
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            setup();
            mBLeScanner.startScanner();
        }
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
            broadCastBeacon(beacon);
        }
    }

    private void broadCastBeacon(Beacon beacon) {
        Intent intent = new Intent(BeaconUtil.ACTION_BEACON_FOUND);
        intent.putExtra(BeaconUtil.EXTRA_DEVICE , beacon);
        sendBroadcast(intent);
    }
}
