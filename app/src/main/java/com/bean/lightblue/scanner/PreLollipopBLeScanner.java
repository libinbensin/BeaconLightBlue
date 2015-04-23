package com.bean.lightblue.scanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.UUID;

/**
 *
 * @author Libin
 */
@EBean(scope = EBean.Scope.Singleton)
public class PreLollipopBLeScanner extends BLeScanner {

    private static final String TAG = PreLollipopBLeScanner.class.getSimpleName();

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 30000;

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scannedBytes) {
            if(scannedBytes != null && scannedBytes.length > 0) {
                onDeviceScan(device, rssi, scannedBytes);
            }
        }
    };

    @RootContext
    Context mContext;

    @AfterInject
    protected void onAfterInject(){
        setup(mContext);
    }

    @Override
    public void startScanner() {
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        stopScanner();
        if(!mBluetoothAdapter.isEnabled()){
            Log.d(TAG, "Bluetooth not enabled");
            return;
        }
        scanLeDevice();
    }

    @Override
    public void stopScanner() {
        if (mScanning) {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    private void scanLeDevice() {
        // Stops scanning after a pre-defined scan period.
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScanning = false;
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        }, SCAN_PERIOD);

        mScanning = true;
        UUID[] uuids = {UUID.fromString(BEAN_LIGHT_UUID)};

        mBluetoothAdapter.startLeScan(mLeScanCallback);
    }

}
