package org.cmucreatelab.flutter_android.classes;

import android.bluetooth.BluetoothDevice;

/**
 * Created by Steve on 5/26/2016.
 */
public class ScanResult {

    // TODO - We can add in more attributes for more detains on the device if need be.

    public BluetoothDevice device;

    public ScanResult(BluetoothDevice device) {
        this.device = device;
    }

}
