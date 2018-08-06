package org.cmucreatelab.flutter_android.classes.flutters;

import android.bluetooth.BluetoothDevice;

import org.cmucreatelab.flutter_android.classes.outputs.FlutterOutput;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;

/**
 * Created by Steve on 8/11/2016.
 *
 * FlutterBoard
 *
 * An abstract class that different versions of flutter boards will inherit from.
 */
public interface FlutterBoard {

    BluetoothDevice getBluetoothDevice();

    Sensor[] getSensors();

    FlutterOutput[] getFlutterOutputs();

}
