package org.cmucreatelab.flutter_android.helpers;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import org.cmucreatelab.flutter_android.classes.Device;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

/**
 * Created by Steve on 5/31/2016.
 *
 * This class handles your session (when you select a specific device on ScanActivity)
 * - connect
 * - disconnect
 */
public class SessionHandler {

    private Device mDevice;


    public SessionHandler() {
        mDevice = null;
    }


    public void startSession(Device device) {
        Log.d(Constants.LOG_TAG, "Starting session with " + device.getDevice().getName());
        mDevice = device;
    }


    public BluetoothDevice getBlueToothDevice() {
        return mDevice.getDevice();
    }

}
