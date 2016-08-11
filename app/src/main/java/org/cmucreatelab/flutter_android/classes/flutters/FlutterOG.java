package org.cmucreatelab.flutter_android.classes.flutters;

import android.bluetooth.BluetoothDevice;

import org.cmucreatelab.flutter_android.classes.outputs.LED;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.outputs.Speaker;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;

/**
 * Created by Steve on 5/26/2016.
 *
 * Device
 *
 * A class representing a FlutterOG.
 *
 */
public class FlutterOG {

    private static final int NUMBER_OF_SERVOS = 3;
    private static final int NUMBER_OF_LEDS = 3;
    private static final int NUMBER_OF_SPEAKERS = 1;
    private static final int NUMBER_OF_SENSORS = 3;

    // I decided to have separate arrays for the different outputs, unlike the array of sensors.
    // I did this because the flutter we are working with (OG) will always have those specific types of outputs,
    // whereas the type of sensors can change.
    private BluetoothDevice mDevice;
    private String name;
    private Servo[] mServos;
    private LED[] mLeds;
    private Speaker mSpeaker;
    private Sensor[] mSensors;


    public FlutterOG(BluetoothDevice device, String name) {
        this.mDevice = device;
        this.name = name;
        this.mServos = new Servo[NUMBER_OF_SERVOS];
        this.mLeds = new LED[NUMBER_OF_LEDS];
        this.mSpeaker = new Speaker();
        this.mSensors = new Sensor[NUMBER_OF_SENSORS];
    }


    public BluetoothDevice getDevice() {
        return mDevice;
    }
    public String getName() {
        return name;
    }

}
