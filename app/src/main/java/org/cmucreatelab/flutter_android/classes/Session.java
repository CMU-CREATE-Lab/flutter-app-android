package org.cmucreatelab.flutter_android.classes;

import android.app.Activity;
import android.util.Log;

import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseNavigationActivity;
import org.cmucreatelab.flutter_android.classes.flutters.Flutter;
import org.cmucreatelab.flutter_android.classes.flutters.FlutterConnectListener;
import org.cmucreatelab.flutter_android.classes.flutters.FlutterMessageListener;
import org.cmucreatelab.flutter_android.classes.outputs.Output;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.settings.SettingsAmplitude;
import org.cmucreatelab.flutter_android.classes.settings.SettingsChange;
import org.cmucreatelab.flutter_android.classes.settings.SettingsConstant;
import org.cmucreatelab.flutter_android.classes.settings.SettingsCumulative;
import org.cmucreatelab.flutter_android.classes.settings.SettingsFrequency;
import org.cmucreatelab.flutter_android.classes.settings.SettingsProportional;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.FlutterProtocol;
import org.cmucreatelab.flutter_android.helpers.static_classes.MessageConstructor;

/**
 * Created by mike on 12/28/16.
 *
 * Provides access to the current session's flutter and activities/listeners.
 *
 */
public class Session implements FlutterMessageListener {

    private BaseNavigationActivity currentActivity;
    private Flutter flutter;
    private boolean isSimulatingData;
    private FlutterConnectListener flutterConnectListener;
    private FlutterMessageListener flutterMessageListener;
    // getters/setters
    public BaseNavigationActivity getCurrentActivity() { return currentActivity; }
    public Flutter getFlutter() { return flutter; }
    public FlutterConnectListener getFlutterConnectListener() { return flutterConnectListener; }
    public FlutterMessageListener getFlutterMessageListener() { return flutterMessageListener; }
    public boolean isSimulatingData() { return isSimulatingData; }
    public void setCurrentActivity(BaseNavigationActivity currentActivity) { this.currentActivity = currentActivity; }
    public void setFlutter(Flutter flutter) { this.flutter = flutter; }
    public void setFlutterMessageListener(FlutterMessageListener flutterMessageListener) { this.flutterMessageListener = flutterMessageListener; }
    public void setFlutterConnectListener(FlutterConnectListener flutterConnectListener) { this.flutterConnectListener = flutterConnectListener; }


    public void setSimulatingData(boolean simulatingData) {
        isSimulatingData = simulatingData;
        if (simulatingData) {
            int value1,value2,value3;
            value1 = flutter.getSensors()[0].getSensorReading();
            value2 = flutter.getSensors()[1].getSensorReading();
            value3 = flutter.getSensors()[2].getSensorReading();
            GlobalHandler.getInstance(currentActivity.getApplicationContext()).melodySmartDeviceHandler.addMessage(MessageConstructor.constructSimulateData(value1,value2,value3));
        } else {
            GlobalHandler.getInstance(currentActivity.getApplicationContext()).melodySmartDeviceHandler.addMessage(MessageConstructor.constructStopSimulateData());
        }
    }


    public Session(BaseNavigationActivity currentActivity, Flutter flutter, FlutterConnectListener flutterConnectListener, FlutterMessageListener flutterMessageListener) {
        this.currentActivity = currentActivity;
        this.flutter = flutter;
        this.flutterConnectListener = flutterConnectListener;
        this.flutterMessageListener = flutterMessageListener;
        this.isSimulatingData = false;
    }


    @Override
    public void onFlutterMessageReceived(String request, String response) {
        Log.v(Constants.LOG_TAG, "onFlutterMessageReceived - " + " " + request + " "  + response);
        if (response.equals("OK") || response.equals("FAIL")) {
            Log.v(Constants.LOG_TAG,"ignoring onFlutterMessageReceived="+response);
            return;
        }
        String[] args = response.split(",");

        char command = args[0].charAt(0);
        switch(command) {
            case FlutterProtocol.Commands.READ_SENSOR_VALUES:
                if (args.length != 4) {
                    Log.e(Constants.LOG_TAG,"invalid number of arguments for READ_SENSOR_VALUES="+response);
                } else {
                    short value1,value2,value3;
                    value1 = Integer.valueOf(args[1]).shortValue();
                    value2 = Integer.valueOf(args[2]).shortValue();
                    value3 = Integer.valueOf(args[3]).shortValue();
                    if (this.isSimulatingData()) {
                        Log.w(Constants.LOG_TAG, "setting sensor values but flutter is flagged as simulating data; ignoring response.");
                    } else {
                        flutter.setSensorValues(value1, value2, value3);
                    }
                }
                break;
            case FlutterProtocol.Commands.SET_OUTPUT:
                break;
            case FlutterProtocol.Commands.REMOVE_RELATION:
                break;
            case FlutterProtocol.Commands.REMOVE_ALL_RELATIONS:
                break;
            case FlutterProtocol.Commands.START_LOGGING:
                break;
            case FlutterProtocol.Commands.STOP_LOGGING:
                break;
            case FlutterProtocol.Commands.SET_LOG_NAME:
                break;
            case FlutterProtocol.Commands.READ_LOG_NAME:
                if (args.length != 2) {
                    Log.e(Constants.LOG_TAG,"invalid number of arguments for READ_LOG_NAME="+response);
                } else {
//                    String logName = args[1];
                    GlobalHandler.getInstance(currentActivity).dataLoggingHandler.readLogName(response);
                }
                break;
            case FlutterProtocol.Commands.READ_NUMBER_POINTS_AVAILABLE:
                if (args.length != 4) {
                    Log.e(Constants.LOG_TAG,"invalid number of arguments for READ_NUMBER_POINTS_AVAILABLE="+response);
                } else {
//                    short numberOfPoints,totalNeeded;
//                    boolean currentlyLogging = Boolean.valueOf(args[3]);
//                    numberOfPoints = Integer.valueOf(args[1]).shortValue();
//                    totalNeeded = Integer.valueOf(args[2]).shortValue();
                    GlobalHandler.getInstance(currentActivity).dataLoggingHandler.readNumberOfPoints(response);
                }
                break;
            case FlutterProtocol.Commands.READ_POINT:
                if (args.length != 3) {
                    Log.e(Constants.LOG_TAG,"invalid number of arguments for READ_POINT="+response);
                } else {
                    // If the time is ffffffff, there is no point available
                    // TODO - temporarily commented out until we figure out what exactly to do with corrupted points
                    /*if (args[1].equals("ffffffff")) {
                        Log.w(Constants.LOG_TAG,"no point available for READ_POINT (first arg is ffffffff)");
                    } else {*/
//                        long unixTime = Long.valueOf(args[1], 16);
//                        short sensor1, sensor2, sensor3;
//                        sensor1 = Integer.valueOf(args[2], 16).shortValue();
//                        sensor2 = Integer.valueOf(args[3], 16).shortValue();
//                        sensor3 = Integer.valueOf(args[4], 16).shortValue();
                        GlobalHandler.getInstance(currentActivity).dataLoggingHandler.readPoint(response);
                    //}
                }
                break;
            case FlutterProtocol.Commands.DELETE_LOG:
                break;
            case FlutterProtocol.Commands.READ_OUTPUT_STATE:
                // TODO actions, but more complex (you also need to know the output you were talking about)
                String protocolString = request.substring(1);
                Output output = flutter.findOutputWithProtocolString(protocolString);
                if (args[1].equals("x")) {
                    output.setIsLinked(false, output);
                    Log.v(Constants.LOG_TAG,"UNLINK "+protocolString);
                } else if (args[1].equals("p")) {
                    // PROPORTIONAL
                    if (args.length != 7) {
                        Log.e(Constants.LOG_TAG,"Invalid number of arguments for READ_OUTPUT_STATE="+response);
                    } else {
                        int omin, omax, imin, imax, portNumber;
                        omin = Integer.valueOf(args[2], 16);
                        omax = Integer.valueOf(args[3], 16);
                        portNumber = Integer.valueOf(args[4]);
                        imin = Integer.valueOf(args[5], 16);
                        imax = Integer.valueOf(args[6], 16);

                        Sensor sensor = flutter.getSensors()[portNumber-1];
                        SettingsProportional settings = SettingsProportional.newInstance(output.getSettings());
                        settings.setSensorPortNumber(sensor.getPortNumber());
                        settings.setOutputMin(omin);
                        settings.setOutputMax(omax);
                        settings.getAdvancedSettings().setInputMin(sensor.voltageToPercent(imin));
                        settings.getAdvancedSettings().setInputMax(sensor.voltageToPercent(imax));
                        // check for inverted sensor
                        if (sensor.isInverted()) {
                            settings.invertOutputs();
                        }
                        output.setSettings(settings);
                        output.setIsLinked(true, output);
                        Log.v(Constants.LOG_TAG,"LINK (proportional) "+protocolString);
                    }
                } else if (args[1].equals("a")) {
                    // AMPLITUDE
                    if (args.length != 8) {
                        Log.e(Constants.LOG_TAG,"Invalid number of arguments for READ_OUTPUT_STATE="+response);
                    } else {
                        int omin, omax, imin, imax, portNumber, speed;
                        omin = Integer.valueOf(args[2], 16);
                        omax = Integer.valueOf(args[3], 16);
                        portNumber = Integer.valueOf(args[4]);
                        imin = Integer.valueOf(args[5], 16);
                        imax = Integer.valueOf(args[6], 16);
                        speed = Integer.valueOf(args[7], 16);

                        Sensor sensor = flutter.getSensors()[portNumber-1];
                        SettingsAmplitude settings = SettingsAmplitude.newInstance(output.getSettings());
                        settings.setSensorPortNumber(sensor.getPortNumber());
                        settings.setOutputMin(omin);
                        settings.setOutputMax(omax);
                        settings.getAdvancedSettings().setInputMin(sensor.voltageToPercent(imin));
                        settings.getAdvancedSettings().setInputMax(sensor.voltageToPercent(imax));
                        settings.getAdvancedSettings().setSpeed(speed);
                        // check for inverted sensor
                        if (sensor.isInverted()) {
                            settings.invertOutputs();
                        }
                        output.setSettings(settings);
                        output.setIsLinked(true, output);
                        Log.v(Constants.LOG_TAG,"LINK (amplitude) "+protocolString);
                    }
                } else if (args[1].equals("f")) {
                    // FREQUENCY
                    if (args.length != 7) {
                        Log.e(Constants.LOG_TAG,"Invalid number of arguments for READ_OUTPUT_STATE="+response);
                    } else {
                        int omin, omax, imin, imax, portNumber;
                        omin = Integer.valueOf(args[2], 16);
                        omax = Integer.valueOf(args[3], 16);
                        portNumber = Integer.valueOf(args[4]);
                        imin = Integer.valueOf(args[5], 16);
                        imax = Integer.valueOf(args[6], 16);

                        Sensor sensor = flutter.getSensors()[portNumber-1];
                        SettingsFrequency settings = SettingsFrequency.newInstance(output.getSettings());
                        settings.setSensorPortNumber(sensor.getPortNumber());
                        settings.setOutputMin(omin);
                        settings.setOutputMax(omax);
                        settings.getAdvancedSettings().setInputMin(sensor.voltageToPercent(imin));
                        settings.getAdvancedSettings().setInputMax(sensor.voltageToPercent(imax));
                        // check for inverted sensor
                        if (sensor.isInverted()) {
                            settings.invertOutputs();
                        }
                        output.setSettings(settings);
                        output.setIsLinked(true, output);
                        Log.v(Constants.LOG_TAG,"LINK (frequency) "+protocolString);
                    }
                } else if (args[1].equals("d")) {
                    // CHANGE
                    if (args.length != 7) {
                        Log.e(Constants.LOG_TAG,"Invalid number of arguments for READ_OUTPUT_STATE="+response);
                    } else {
                        int omin, omax, imin, imax, portNumber;
                        omin = Integer.valueOf(args[2], 16);
                        omax = Integer.valueOf(args[3], 16);
                        portNumber = Integer.valueOf(args[4]);
                        imin = Integer.valueOf(args[5], 16);
                        imax = Integer.valueOf(args[6], 16);

                        Sensor sensor = flutter.getSensors()[portNumber-1];
                        SettingsChange settings = SettingsChange.newInstance(output.getSettings());
                        settings.setSensorPortNumber(sensor.getPortNumber());
                        settings.setOutputMin(omin);
                        settings.setOutputMax(omax);
                        settings.getAdvancedSettings().setInputMin(sensor.voltageToPercent(imin));
                        settings.getAdvancedSettings().setInputMax(sensor.voltageToPercent(imax));
                        // check for inverted sensor
                        if (sensor.isInverted()) {
                            settings.invertOutputs();
                        }
                        output.setSettings(settings);
                        output.setIsLinked(true, output);
                        Log.v(Constants.LOG_TAG,"LINK (change) "+protocolString);
                    }
                } else if (args[1].equals("i")) {
                    // CUMULATIVE
                    if (args.length != 9) {
                        Log.e(Constants.LOG_TAG,"Invalid number of arguments for READ_OUTPUT_STATE="+response);
                    } else {
                        int omin, omax, imin, imax, portNumber, center, speed;
                        omin = Integer.valueOf(args[2], 16);
                        omax = Integer.valueOf(args[3], 16);
                        portNumber = Integer.valueOf(args[4]);
                        imin = Integer.valueOf(args[5], 16);
                        imax = Integer.valueOf(args[6], 16);
                        center = Integer.valueOf(args[7], 16);
                        speed = Integer.valueOf(args[8], 16);

                        Sensor sensor = flutter.getSensors()[portNumber-1];
                        SettingsCumulative settings = SettingsCumulative.newInstance(output.getSettings());
                        settings.setSensorPortNumber(sensor.getPortNumber());
                        settings.setOutputMin(omin);
                        settings.setOutputMax(omax);
                        settings.getAdvancedSettings().setInputMin(sensor.voltageToPercent(imin));
                        settings.getAdvancedSettings().setInputMax(sensor.voltageToPercent(imax));
                        settings.getAdvancedSettings().setSpeed(speed);
                        settings.getAdvancedSettings().setSensorCenterValue(center);
                        // check for inverted sensor
                        if (sensor.isInverted()) {
                            settings.invertOutputs();
                        }
                        output.setSettings(settings);
                        output.setIsLinked(true, output);
                        Log.v(Constants.LOG_TAG,"LINK (cumulative) "+protocolString);
                    }
                } else if (args.length == 2) {
                    // TODO @tasota use a real structure for (constant) SettingsProportional
                    int position;
                    position = Integer.valueOf(args[1], 16);

                    SettingsConstant settingsConstant = SettingsConstant.newInstance(output.getSettings());
                    settingsConstant.setValue(position);
                    output.setSettings(settingsConstant);
                    output.setIsLinked(true, output);
                    Log.v(Constants.LOG_TAG,"LINK (constant) "+protocolString);
                } else {
                    Log.e(Constants.LOG_TAG,"failed to parse output state (not implemented): "+args[1]);
                }
                break;
            case FlutterProtocol.Commands.SET_INPUT_TYPE:
                break;
            case FlutterProtocol.Commands.READ_INPUT_TYPE:
                if (args.length != 2) {
                    Log.e(Constants.LOG_TAG,"invalid number of arguments for READ_INPUT_TYPE="+response);
                } else {
                    int portNumber = Integer.valueOf(request.split(",")[1]);
                    short inputType = Integer.valueOf(args[1]).shortValue();

                    Sensor sensor = FlutterProtocol.sensorFromInputType(portNumber,inputType);
                    Log.v(Constants.LOG_TAG,"About to set portNumber="+portNumber+" to inputType="+inputType+"="+currentActivity.getString(sensor.getSensorTypeId()));
                    this.flutter.getSensors()[portNumber-1] = sensor;
                }
                break;
            case FlutterProtocol.Commands.ENABLE_PROPORTIONAL_CONTROL:
                break;
            default:
                Log.e(Constants.LOG_TAG,"could not match command for response="+response);
                break;
        }
    }

}
