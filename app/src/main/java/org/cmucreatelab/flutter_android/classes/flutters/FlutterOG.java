package org.cmucreatelab.flutter_android.classes.flutters;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import org.cmucreatelab.flutter_android.classes.datalogging.DataSet;
import org.cmucreatelab.flutter_android.classes.outputs.Output;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.outputs.Speaker;
import org.cmucreatelab.flutter_android.classes.outputs.TriColorLed;
import org.cmucreatelab.flutter_android.classes.sensors.NoSensor;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.DataLoggingHandler;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Steve on 5/26/2016.
 *
 * Device
 *
 * A class representing the original gangsta Flutter. One with 3 servos, 3 leds, 1 speaker and 3 sensors.
 *
 */
public class FlutterOG extends Flutter implements DataLoggingHandler.DataSetListener {

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
    private DataSet mDataSet;
    private DataSetListener listener;


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


    public ArrayList<Output> getOutputs() {
        ArrayList<Output> result = new ArrayList<>();

        result.addAll(Arrays.asList(mServos));
        for (TriColorLed led : mTriColorLeds) {
            result.add(led.getRedLed());
            result.add(led.getGreenLed());
            result.add(led.getBlueLed());
        }
        result.add(mSpeaker.getPitch());
        result.add(mSpeaker.getVolume());

        return result;
    }


    public Output findOutputWithProtocolString(String protocolString) {
        // LEDs
        switch (protocolString) {
            case "r1":
                return getTriColorLeds()[0].getRedLed();
            case "r2":
                return getTriColorLeds()[1].getRedLed();
            case "r3":
                return getTriColorLeds()[2].getRedLed();
            case "g1":
                return getTriColorLeds()[0].getGreenLed();
            case "g2":
                return getTriColorLeds()[1].getGreenLed();
            case "g3":
                return getTriColorLeds()[2].getGreenLed();
            case "b1":
                return getTriColorLeds()[0].getBlueLed();
            case "b2":
                return getTriColorLeds()[1].getBlueLed();
            case "b3":
                return getTriColorLeds()[2].getBlueLed();
        }
        // Servos
        switch (protocolString) {
            case "s1":
                return getServos()[0];
            case "s2":
                return getServos()[1];
            case "s3":
                return getServos()[2];
        }
        // Speaker
        switch (protocolString) {
            case "v":
                return getSpeaker().getVolume();
            case "f":
                return getSpeaker().getPitch();
        }
        // fail
        Log.e(Constants.LOG_TAG,"Cannot find Output with protocolString="+protocolString);
        return null;
    }


    public void populateDataSet(Context context, DataSetListener listener) {
        Log.d(Constants.LOG_TAG, "populateDataSet");
        GlobalHandler globalHandler = GlobalHandler.getInstance(context);
        globalHandler.dataLoggingHandler.setDataSetListener(this);
        this.listener = listener;
        globalHandler.dataLoggingHandler.populateDataSetDetails();
    }


    @Override
    public void onDataSetDetailsPopulated(DataSet dataSet) {
        Log.d(Constants.LOG_TAG, "onDataSetDetailsPopulated");
        this.mDataSet = dataSet;
        listener.onDataSetPopulated();
    }


    // getters

    public Servo[] getServos() { return mServos; }
    public TriColorLed[] getTriColorLeds() { return mTriColorLeds; }
    public Speaker getSpeaker() { return mSpeaker; }
    public Sensor[] getSensors() { return mSensors; }
    public String getName() { return name; }
    public DataSet getDataSet() { return mDataSet; }

    // setters
    public void setSensors(Sensor[] sensors) {
        mSensors = sensors;
    }
    public void setDatatSet(DataSet datatSet) { mDataSet = datatSet; }


    public interface DataSetListener {
        void onDataSetPopulated();
    }

}
