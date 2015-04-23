package com.bean.lightblue.manager;

import android.content.Context;

import com.bean.lightblue.model.Beacon;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Libin
 */
@EBean(scope = EBean.Scope.Singleton)
public class BeaconManager {

    @RootContext
    Context mContext;

    private Map<String , Beacon> mBeacons = new HashMap<>();

    public void addBeacon(Beacon newBeacon) {
        // delete if already exist
        if(mBeacons == null) {
            mBeacons = new HashMap<>();
        }
        if(newBeacon != null) {
            String uuid = newBeacon.getUuid();
            if(mBeacons.containsKey(uuid)) {
                mBeacons.remove(mBeacons.get(uuid));
            }
            mBeacons.put(uuid , newBeacon);
        }
    }

    public boolean isThisNewBeacon(Beacon newBeacon) {
        return mBeacons == null || !mBeacons.containsKey(newBeacon.getUuid());
    }

    public List<Beacon> getBeacons(){

        if(mBeacons != null && !mBeacons.isEmpty()) {
            List<Beacon> beacons = new ArrayList<>();
            for(Map.Entry<String,Beacon> map : mBeacons.entrySet()){
                beacons.add(map.getValue());
            }
            return beacons;
        }
        return null;
    }
}
