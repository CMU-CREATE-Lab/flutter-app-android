package org.cmucreatelab.flutter_android.classes.flutters;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import org.cmucreatelab.android.melodysmart.DeviceHandler;
import org.cmucreatelab.flutter_android.classes.datalogging.DataSet;
import org.cmucreatelab.flutter_android.classes.outputs.FlutterOutput;
import org.cmucreatelab.flutter_android.classes.outputs.Output;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.outputs.Speaker;
import org.cmucreatelab.flutter_android.classes.outputs.TriColorLed;
import org.cmucreatelab.flutter_android.classes.sensors.NoSensor;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.datalogging.DataLoggingHandler;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.MessageConstructor;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Steve on 5/26/2016.
 *
 * Flutter
 *
 * A class representing the first released FlutterBoard (3 servos, 3 leds, 1 speaker and 3 sensors).
 *
 */
public class Flutter implements FlutterBoard, DataLoggingHandler.DataSetListener {

    private static final int FLUTTER_VERSION = 1;

    private static final int NUMBER_OF_SERVOS = 3;
    private static final int NUMBER_OF_LEDS = 3;
    private static final int NUMBER_OF_SPEAKERS = 1;
    private static final int NUMBER_OF_SENSORS = 3;

    // I decided to have separate arrays for the different outputs, unlike the array of sensors.
    // I did this because the flutter we are working with (OG) will always have those specific types of outputs,
    // whereas the type of sensors can change.
    private Servo[] servos;
    private TriColorLed[] triColorLeds;
    private Speaker speaker;
    private Sensor[] sensors;

    private String name;
    private DataSet dataSet;
    private PopulatedDataSetListener listener;
    private BluetoothDevice bluetoothDevice;

    // getters
    public Servo[] getServos() { return servos; }
    public TriColorLed[] getTriColorLeds() { return triColorLeds; }
    public Speaker getSpeaker() { return speaker; }
    public String getName() { return name; }
    public DataSet getDataSet() { return dataSet; }
    // setters
    public void setSensors(Sensor[] sensors) { this.sensors = sensors; }
    public void setDatatSet(DataSet dataSet) { this.dataSet = dataSet; }
    public void setSpeaker(Speaker speaker) { this.speaker = speaker; }


    public Flutter(BluetoothDevice device, String name) {
        this.bluetoothDevice = device;
        this.name = name;
        this.servos = new Servo[NUMBER_OF_SERVOS];
        this.triColorLeds = new TriColorLed[NUMBER_OF_LEDS];
        this.speaker = new Speaker(0, this);
        this.sensors = new Sensor[NUMBER_OF_SENSORS];

        for (int i = 0; i < servos.length; i++) {
            servos[i] = new Servo(i+1, this);
        }
        for (int i = 0; i < triColorLeds.length; i++) {
            triColorLeds[i] = new TriColorLed(i+1, this);
        }
        for (int i = 0; i < sensors.length; i++) {
            sensors[i] = new NoSensor(i+1);
        }
    }


    public void setSensorValues(int value1, int value2, int value3) {
        sensors[0].setSensorReading(value1);
        sensors[1].setSensorReading(value2);
        sensors[2].setSensorReading(value3);
    }


    public void updateSensorAtPort(DeviceHandler deviceHandler, int portNumber, Sensor sensor) {
        Sensor oldSensor = sensors[portNumber-1];

        // update the sensor
        sensors[portNumber-1] = sensor;

        // send message to flutter with sensor type
        short inputType = sensor.getSensorType();
        deviceHandler.addMessage(MessageConstructor.constructSetInputType(sensor, inputType));

        // update all outputs that use sensor's portNumber
        if (oldSensor.hasCustomSensorRange() || sensor.hasCustomSensorRange()) {
            for (Servo servo : servos) {
                if (servo.isLinked() && servo.getSettings().getSensor().getPortNumber() == portNumber) {
                    // update
                    servo.getSettings().updateWithNewSensorType(oldSensor,sensor);
                }
            }
            for (TriColorLed led: triColorLeds) {
                if (led.getRedLed().isLinked() && led.getRedLed().getSettings().getSensor().getPortNumber() == portNumber) {
                    // update
                    led.getRedLed().getSettings().updateWithNewSensorType(oldSensor,sensor);
                }
                if (led.getGreenLed().isLinked() && led.getGreenLed().getSettings().getSensor().getPortNumber() == portNumber) {
                    // update
                    led.getGreenLed().getSettings().updateWithNewSensorType(oldSensor,sensor);
                }
                if (led.getBlueLed().isLinked() && led.getBlueLed().getSettings().getSensor().getPortNumber() == portNumber) {
                    // update
                    led.getBlueLed().getSettings().updateWithNewSensorType(oldSensor,sensor);
                }
            }
            if (speaker.getVolume().isLinked() && speaker.getVolume().getSettings().getSensor().getPortNumber() == portNumber) {
                // update
                speaker.getVolume().getSettings().updateWithNewSensorType(oldSensor,sensor);
            }
            if (speaker.getPitch().isLinked() && speaker.getPitch().getSettings().getSensor().getPortNumber() == portNumber) {
                // update
                speaker.getPitch().getSettings().updateWithNewSensorType(oldSensor,sensor);
            }
        }
    }


    public ArrayList<Output> getOutputs() {
        ArrayList<Output> result = new ArrayList<>();

        result.addAll(Arrays.asList(servos));
        for (TriColorLed led : triColorLeds) {
            result.add(led.getRedLed());
            result.add(led.getGreenLed());
            result.add(led.getBlueLed());
        }
        result.add(speaker.getPitch());
        result.add(speaker.getVolume());

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


    public void populateDataSet(Context context, PopulatedDataSetListener listener) {
        Log.d(Constants.LOG_TAG, "populateDataSet");
        GlobalHandler globalHandler = GlobalHandler.getInstance(context);
        this.listener = listener;
        this.dataSet = new DataSet();
        globalHandler.dataLoggingHandler.populatedDataSet(this);
    }


    @Override
    public void onDataSetPopulated(DataSet dataSet) {
        Log.d(Constants.LOG_TAG, "Flutter.onDataSetPopulated");
        this.dataSet = dataSet;
        listener.onDataSetPopulated();
    }


    public interface PopulatedDataSetListener {
        void onDataSetPopulated();
    }


    @Override
    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }


    @Override
    public Sensor[] getSensors() {
        return sensors;
    }


    @Override
    public FlutterOutput[] getFlutterOutputs() {
        FlutterOutput[] flutterOutputs = new FlutterOutput[servos.length + triColorLeds.length + 1];

        int index=0;
        for (Servo servo : servos) {
            flutterOutputs[index] = servo;
            index++;
        }
        for (TriColorLed led : triColorLeds) {
            flutterOutputs[index] = led;
            index++;
        }
        flutterOutputs[index] = speaker;

        return flutterOutputs;
    }

}
