package com.bean.lightblue.activities;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bean.lightblue.R;
import com.bean.lightblue.manager.BeaconManager;
import com.bean.lightblue.model.Beacon;
import com.bean.lightblue.scanner.BLeScanner;
import com.bean.lightblue.scanner.LollipopBLeScanner_;
import com.bean.lightblue.scanner.PreLollipopBLeScanner_;
import com.bean.lightblue.view.BeaconView;
import com.bean.lightblue.view.BeaconView_;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.activity_home)
public class HomeActivity extends Activity implements BLeScanner.BleDeviceListener{

    @Bean
    BeaconManager mBeaconManager;

    @ViewById(R.id.bleDeviceList)
    LinearLayout mBLeDeviceListView;

    private BLeScanner mBLeScanner;

    @AfterInject
    protected void onAfterInject(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBLeScanner = LollipopBLeScanner_.getInstance_(this);
        }else {
            mBLeScanner = PreLollipopBLeScanner_.getInstance_(this);
        }
    }

    @AfterViews
    protected void onAfterViews() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        mBLeScanner.setBleDeviceListener(this);
        mBLeScanner.startScanner();
    }

    @Override
    protected void onStop(){
        super.onStop();
        mBLeScanner.setBleDeviceListener(null);
        mBLeScanner.stopScanner();
    }

    @Override
    public void onBLeDeviceFound(Beacon newBeacon) {
        mBeaconManager.addBeacon(newBeacon);

        List<Beacon> beacons = mBeaconManager.getBeacons();
        showFoundDevice(beacons);
    }

    @UiThread
    protected void showFoundDevice(List<Beacon> beacons) {
        mBLeDeviceListView.removeAllViews();

        for (Beacon beacon : beacons) {
            BeaconView beaconView = BeaconView_.build(this);
            beaconView.bind(beacon);
            mBLeDeviceListView.addView(beaconView);
        }
    }
}
