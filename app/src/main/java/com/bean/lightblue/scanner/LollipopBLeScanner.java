package com.bean.lightblue.scanner;

import android.annotation.TargetApi;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Libin
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
@EBean(scope = EBean.Scope.Singleton)
public class LollipopBLeScanner extends BLeScanner {

    private static final String TAG = LollipopBLeScanner.class.getSimpleName();
    private BluetoothLeScanner mBluetoothLeScanner;
    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            if(result == null || result.getScanRecord() == null) {
                return;
            }

            byte[] scannedBytes = result.getScanRecord().getBytes();

            if(scannedBytes != null && scannedBytes.length > 0) {
                onDeviceScan(result.getDevice(), result.getRssi(), scannedBytes);
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    };

    @RootContext
    Context mContext;

    @AfterInject
    protected void onAfterInject(){
        setup(mContext);
        mBluetoothLeScanner = mBluetoothManager.getAdapter().getBluetoothLeScanner();
    }

    @Override
    public void startScanner() {
        if(!mBluetoothManager.getAdapter().isEnabled()){
            Log.d(TAG, "Bluetooth not enabled");
            return;
        }
        stopScanner();
        scanLeDevice();
    }

    @Override
    public void stopScanner() {
        if(mScanning) {
            mBluetoothLeScanner.stopScan(mScanCallback);
        }
    }

    private void scanLeDevice() {
        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                .setReportDelay(1000 * 30) // 30 seconds
                .build();
        List<ScanFilter> filters = new ArrayList<ScanFilter>();
        mBluetoothLeScanner.startScan(filters, settings, mScanCallback);
        mScanning = true;
    }
}
