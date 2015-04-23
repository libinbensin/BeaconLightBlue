package com.bean.lightblue.scanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;

import com.bean.lightblue.model.Beacon;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Libin
 */

public abstract class BLeScanner {

    public static final String BEAN_LIGHT_UUID = "A4951797-C5B1-4B44-B512-1370F02D74DE"; //A495FF10-C5B1-4B44-B512-1370F02D74DE
    private static final String TAG = BLeScanner.class.getSimpleName();
    final private static char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

    protected BluetoothManager mBluetoothManager;
    protected BluetoothAdapter mBluetoothAdapter;
    protected boolean mScanning;
    protected Handler mHandler;
    protected BleDeviceListener mBleDeviceListener;
    private Context mContext;

    protected List<UUID> parseUuids(byte[] advertisedData) {
        List<UUID> uuids = new ArrayList<UUID>();

        ByteBuffer buffer = ByteBuffer.wrap(advertisedData).order(ByteOrder.LITTLE_ENDIAN);
        while (buffer.remaining() > 2) {
            byte length = buffer.get();
            if (length == 0) break;

            byte type = buffer.get();
            switch (type) {
                case 0x02: // Partial list of 16-bit UUIDs
                case 0x03: // Complete list of 16-bit UUIDs
                    while (length >= 2) {
                        uuids.add(UUID.fromString(String.format(
                                "%08x-0000-1000-8000-00805f9b34fb", buffer.getShort())));
                        length -= 2;
                    }
                    break;

                case 0x06: // Partial list of 128-bit UUIDs
                case 0x07: // Complete list of 128-bit UUIDs
                    while (length >= 16) {
                        long lsb = buffer.getLong();
                        long msb = buffer.getLong();
                        uuids.add(new UUID(msb, lsb));
                        length -= 16;
                    }
                    break;

                default:
                    buffer.position(buffer.position() + length - 1);
                    break;
            }
        }

        return uuids;
    }


    protected void setup(Context context){
        mContext = context;
        mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mHandler = new Handler();
    }

    public void setBleDeviceListener(BleDeviceListener listener){
        mBleDeviceListener = listener;
    }

    public interface BleDeviceListener{
        void onBLeDeviceFound(Beacon beacon);
    }

    public abstract void startScanner();
    public abstract void stopScanner();


    protected void onDeviceScan(BluetoothDevice device, int rssi, byte[] scannedBytes) {
        if(isThisBeacon(scannedBytes)) {
            Beacon beacon = getBeaconFromScannedRecord(scannedBytes);
            if (beacon != null) {
                beacon.setDeviceName(device.getName());
                beacon.setRssi(rssi);
                beacon.setMacAddress(device.getAddress());
                if (mBleDeviceListener != null) {
                    mBleDeviceListener.onBLeDeviceFound(beacon);
                }
            }
        }
    }

    protected boolean isThisBeacon(byte[] scanData) {
        int startByte = 2;
        while (startByte <= 5) {
            if (((int)scanData[startByte+2] & 0xff) == 0x02 &&
                    ((int)scanData[startByte+3] & 0xff) == 0x15) {
                return true;
            }
            startByte++;
        }
        return false;
    }

    protected Beacon getBeaconFromScannedRecord(byte[] scanData) {

        int startByte = 2;

        int major = (scanData[startByte+20] & 0xff) * 0x100 + (scanData[startByte+21] & 0xff);
        int minor = (scanData[startByte+22] & 0xff) * 0x100 + (scanData[startByte+23] & 0xff);

        // AirLocate:
        // 02 01 1a 1a ff 4c 00 02 15  # Apple's fixed iBeacon advertising prefix
        // e2 c5 6d b5 df fb 48 d2 b0 60 d0 f5 a7 10 96 e0 # iBeacon profile uuid
        // 00 00 # major
        // 00 00 # minor
        // c5 # The 2's complement of the calibrated Tx Power

        // Estimote:
        // 02 01 1a 11 07 2d 24 bf 16
        // 394b31ba3f486415ab376e5c0f09457374696d6f7465426561636f6e00000000000000000000000000000000000000000000000000

        byte[] proximityUuidBytes = new byte[16];
        System.arraycopy(scanData, startByte+4, proximityUuidBytes, 0, 16);
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
        String proximityUuid = sb.toString();

        return new Beacon(proximityUuid , major , minor);
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
}
