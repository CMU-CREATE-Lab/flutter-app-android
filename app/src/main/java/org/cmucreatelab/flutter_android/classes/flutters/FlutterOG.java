package org.cmucreatelab.flutter_android.classes.flutters;

import android.bluetooth.BluetoothDevice;

import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.outputs.Speaker;
import org.cmucreatelab.flutter_android.classes.outputs.TriColorLed;
import org.cmucreatelab.flutter_android.classes.sensors.NoSensor;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;

/**
 * Created by Steve on 5/26/2016.
 *
 * Device
 *
 * A class representing the original gangsta Flutter. One with 3 servos, 3 leds, 1 speaker and 3 sensors.
 *
 */
public class FlutterOG extends Flutter {

    private static final int FLUTTER_VERSION = 1;

    private static final int NUMBER_OF_SERVOS = 3;
    private static final int NUMBER_OF_LEDS = 3;
    private static final int NUMBER_OF_SPEAKERS = 1;
    private static final int NUMBER_OF_SENSORS = 3;

    // I decided to have separate arrays for the different outputs, unlike the array of sensors.
    // I did this because the flutter we are working with (OG) will always have those specific types of outputs,
    // whereas the type of sensors can change.
    private Servo[] mServos;
    private TriColorLed[] mTriColorLeds;
    private Speaker mSpeaker;
    private Sensor[] mSensors;

    private String name;


    public FlutterOG(BluetoothDevice device, String name) {
        this.mDevice = device;
        this.name = name;
        this.mServos = new Servo[NUMBER_OF_SERVOS];
        this.mTriColorLeds = new TriColorLed[NUMBER_OF_LEDS];
        this.mSpeaker = new Speaker(0);
        this.mSensors = new Sensor[NUMBER_OF_SENSORS];

        for (int i = 0; i < mServos.length; i++) {
            mServos[i] = new Servo(i+1);
        }
        for (int i = 0; i < mTriColorLeds.length; i++) {
            mTriColorLeds[i] = new TriColorLed(i+1);
        }
        for (int i = 0; i < mSensors.length; i++) {
            mSensors[i] = new NoSensor(i+1);
        }
    }


    // getters

    public Servo[] getServos() { return mServos; }
    public TriColorLed[] getTriColorLeds() { return mTriColorLeds; }
    public Speaker getSpeaker() { return mSpeaker; }
    public Sensor[] getSensors() { return mSensors; }
    public String getName() { return name; }

    // setters
    public void setSensors(Sensor[] sensors) {
        mSensors = sensors;
    }

}
