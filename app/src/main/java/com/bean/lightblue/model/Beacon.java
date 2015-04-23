package com.bean.lightblue.model;

/**
 *
 * @author Libin
 */
public class Beacon {

    private String proximityUuid;
    /**
     * A 16 bit integer typically used to represent a group of iBeacons
     */
    private int major;
    /**
     * A 16 bit integer that identifies a specific iBeacon within a group
     */
    private int minor;

    private int rssi;

    private String deviceName;

    private String macAddress;

    public Beacon(String proximityUuid, int major, int minor) {
        this.proximityUuid = proximityUuid;
        this.major = major;
        this.minor = minor;
    }

    public String getUuid() {
        return proximityUuid;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }
}
