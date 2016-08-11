package org.cmucreatelab.flutter_android.classes.flutters;

import android.bluetooth.BluetoothDevice;

/**
 * Created by Steve on 8/11/2016.
 *
 * Flutter
 *
 * An abstract class that different versions of flutter boards will inherit from.
 *
 */
public abstract class Flutter {

    protected BluetoothDevice mDevice;
    protected String name;


    protected BluetoothDevice getDevice() {
        return mDevice;
    }
    protected String getName() {
        return name;
    }

}
