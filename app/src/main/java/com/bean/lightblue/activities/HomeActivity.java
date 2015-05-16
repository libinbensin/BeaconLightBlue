package com.bean.lightblue.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.LinearLayout;

import com.bean.lightblue.R;
import com.bean.lightblue.manager.BeaconManager;
import com.bean.lightblue.model.Beacon;
import com.bean.lightblue.service.BeaconService_;
import com.bean.lightblue.util.BeaconUtil;
import com.bean.lightblue.view.BeaconView;
import com.bean.lightblue.view.BeaconView_;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.activity_home)
public class HomeActivity extends Activity{

    @Bean
    BeaconManager mBeaconManager;

    @ViewById(R.id.bleDeviceList)
    LinearLayout mBLeDeviceListView;

    private BroadcastReceiver mBeaconReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showBeacons();
        }
    };

    @Override
    protected void onStart(){
        super.onStart();
        Intent intent = new Intent(this , BeaconService_.class);
        intent.setAction(BeaconUtil.ACTION_START_SCANNING);
        startService(intent);
        registerReceiver(mBeaconReceiver , new IntentFilter(BeaconUtil.ACTION_BEACON_FOUND));
    }

    @Override
    protected void onStop(){
        unregisterReceiver(mBeaconReceiver);
        super.onStop();
    }

    protected void showBeacons() {
        List<Beacon> beacons = mBeaconManager.getBeacons();
        if(beacons == null || beacons.isEmpty()) {
            return;
        }

        mBLeDeviceListView.removeAllViews();
        for (Beacon beacon : beacons) {
            BeaconView beaconView = BeaconView_.build(this);
            beaconView.bind(beacon);
            mBLeDeviceListView.addView(beaconView);
        }
    }
}
