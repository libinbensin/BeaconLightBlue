package com.bean.lightblue.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bean.lightblue.model.Beacon;
import com.bean.lightblue.R;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 *
 * @author Libin
 */
@EViewGroup(R.layout.view_beacon)
public class BeaconView extends LinearLayout {

    private final Context mContext;

    @ViewById(R.id.beaconUUID)
    TextView mUUID;

    @ViewById(R.id.beaconDeviceName)
    TextView mDeviceName;

    @ViewById(R.id.beaconMajorID)
    TextView mMajorID;

    @ViewById(R.id.beaconMinorID)
    TextView mMinorID;

    @ViewById(R.id.beaconMacAddress)
    TextView mMacAddress;

    @ViewById(R.id.beaconRSSI)
    TextView mRssi;

    public BeaconView(Context context) {
        super(context);
        mContext = context;
    }

    public BeaconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public void bind(Beacon beacon) {
        mUUID.setText(getString(R.string.beacon_uuid , beacon.getUuid()));
        mDeviceName.setText(getString(R.string.beacon_device_name , beacon.getDeviceName()));
        mMajorID.setText(getString(R.string.beacon_major_id , Integer.toString(beacon.getMajor())));
        mMinorID.setText(getString(R.string.beacon_minor_id , Integer.toString(beacon.getMinor())));
        mMacAddress.setText(getString(R.string.beacon_mac_address , beacon.getMacAddress()));
        mRssi.setText(getString(R.string.beacon_rssi , Integer.toString(beacon.getRssi())));
    }

    private String getString(int id , String extra) {
        return mContext.getString(id , extra);
    }
}
