package com.bean.lightblue.util;

import com.bean.lightblue.model.Beacon;

/**
 * @author Libin
 */
public final class BeaconValidator {
    public static final String BEACON_UUID = "b9407f30-f5f8-466e-aff9-25556b57fe6d";//"9a6db1a1-1607-4274-b693-40463a10e611";//"A4951797-C5B1-4B44-B512-1370F02D74DE";
    private final static char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

    public static boolean isThisMyBeacon(Beacon beacon) {
        return beacon.getUuid().toLowerCase().equals(BEACON_UUID.toLowerCase());
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String toBeaconUUIDString(byte[] proximityUuidBytes){
        String hexString = bytesToHex(proximityUuidBytes);
        StringBuilder sb = new StringBuilder();
        sb.append(hexString.substring(0,8));
        sb.append("-");
        sb.append(hexString.substring(8,12));
        sb.append("-");
        sb.append(hexString.substring(12,16));
        sb.append("-");
        sb.append(hexString.substring(16,20));
        sb.append("-");
        sb.append(hexString.substring(20,32));
        return sb.toString();
    }
}
