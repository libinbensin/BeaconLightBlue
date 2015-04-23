package com.bean.lightblue.activities;

import android.app.Activity;
import android.content.Intent;
import android.widget.LinearLayout;

import com.bean.lightblue.R;
import com.bean.lightblue.manager.BeaconManager;
import com.bean.lightblue.model.Beacon;
import com.bean.lightblue.service.BeaconService_;
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

    @Override
    protected void onStart(){
        super.onStart();
        startService(new Intent(this , BeaconService_.class));
        List<Beacon> beacons = mBeaconManager.getBeacons();
        if(beacons != null && !beacons.isEmpty()) {
            showFoundDevice(beacons);
        }
    }

    private void showFoundDevice(List<Beacon> beacons) {
        mBLeDeviceListView.removeAllViews();

        for (Beacon beacon : beacons) {
            BeaconView beaconView = BeaconView_.build(this);
            beaconView.bind(beacon);
            mBLeDeviceListView.addView(beaconView);
        }
    }
}
