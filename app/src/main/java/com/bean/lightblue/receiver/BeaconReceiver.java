package com.bean.lightblue.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.bean.lightblue.model.Beacon;
import com.bean.lightblue.util.BeaconUtil;
import com.bean.lightblue.util.NotificationUI;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EReceiver;

/**
 * @author Libin
 */
@EReceiver
public class BeaconReceiver extends BroadcastReceiver {

    @Bean
    NotificationUI mNotificationUI;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent == null || TextUtils.isEmpty(intent.getAction())){
            return;
        }

        switch (intent.getAction()){
            case BeaconUtil.ACTION_BEACON_FOUND:
                if(intent.getExtras() != null) {
                    Beacon beacon = intent.getExtras().getParcelable(BeaconUtil.EXTRA_DEVICE);
                    if (BeaconUtil.isThisMyBeacon(beacon)) {
                        mNotificationUI.showNotification(context, beacon);
                    }
                }
                break;
        }
    }
}
