package com.bean.lightblue.util;

import com.bean.lightblue.model.Beacon;

/**
 * Created by salall on 5/14/15.
 */
public final class BeaconUtil {

    public static final String BEACON_UUID = "A4951797-C5B1-4B44-B512-1370F02D74DE";
    public final static String EXTRA_DEVICE = "beacon.device";
    public final static String ACTION_BEACON_FOUND = "bean.intent.action.BEACON_FOUND";
    public static final String ACTION_START_SCANNING = "beacon.start.scanner";

    public static boolean isThisMyBeacon(Beacon beacon) {
        return beacon.getUuid().toLowerCase().equals(BEACON_UUID.toLowerCase());
    }
}
