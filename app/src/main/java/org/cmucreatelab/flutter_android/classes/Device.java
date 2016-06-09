package org.cmucreatelab.flutter_android.classes;

import android.bluetooth.BluetoothDevice;

/**
 * Created by Steve on 5/26/2016.
 */
public class Device {

    // TODO - We can add in more attributes for more details on the device if need be.
    // For example, in the example app, they store 'byte[] scanRecord' (whatever that is)

    private BluetoothDevice mDevice;
    private String name;


    public Device(BluetoothDevice device, String name) {
        this.mDevice = device;
        this.name = name;
    }


    public BluetoothDevice getDevice() {
        return mDevice;
    }
    public String getName() {
        return name;
    }

}
