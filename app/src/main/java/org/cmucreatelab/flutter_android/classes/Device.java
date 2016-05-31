package org.cmucreatelab.flutter_android.classes;

import android.bluetooth.BluetoothDevice;

/**
 * Created by Steve on 5/26/2016.
 */
public class Device {

    // TODO - We can add in more attributes for more details on the device if need be.

    private BluetoothDevice mDevice;


    public Device(BluetoothDevice device) {
        this.mDevice = device;
    }


    public BluetoothDevice getDevice() {
        return mDevice;
    }

}
