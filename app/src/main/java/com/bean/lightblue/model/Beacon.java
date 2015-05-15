package com.bean.lightblue.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * @author Libin
 */
public class Beacon implements Parcelable{

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

    public Beacon(Parcel parcel) {
        proximityUuid = parcel.readString();
        major = parcel.readInt();
        minor = parcel.readInt();
        rssi = parcel.readInt();
        deviceName = parcel.readString();
        macAddress = parcel.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(proximityUuid);
        parcel.writeInt(major);
        parcel.writeInt(minor);
        parcel.writeInt(rssi);
        parcel.writeString(deviceName);
        parcel.writeString(macAddress);
    }

    public static final Parcelable.Creator<Beacon> CREATOR = new Parcelable.Creator<Beacon>() {

        @Override
        public Beacon createFromParcel(Parcel in) {
            return new Beacon(in);
        }

        @Override
        public Beacon[] newArray(int size) {
            return new Beacon[size];
        }
    };
}
