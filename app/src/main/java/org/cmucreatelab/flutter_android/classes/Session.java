package org.cmucreatelab.flutter_android.classes;

import android.app.Activity;
import android.util.Log;

import org.cmucreatelab.flutter_android.classes.flutters.FlutterConnectListener;
import org.cmucreatelab.flutter_android.classes.flutters.FlutterMessageListener;
import org.cmucreatelab.flutter_android.classes.flutters.FlutterOG;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.FlutterProtocol;

/**
 * Created by mike on 12/28/16.
 *
 * Provides access to the current session's flutter and activities/listeners.
 *
 */
public class Session implements FlutterMessageListener {

    private Activity currentActivity;
    private FlutterOG flutter;
    private FlutterConnectListener flutterConnectListener;
    private FlutterMessageListener flutterMessageListener;
    // getters/setters
    public Activity getCurrentActivity() { return currentActivity; }
    public FlutterOG getFlutter() { return flutter; }
    public FlutterConnectListener getFlutterConnectListener() { return flutterConnectListener; }
    public FlutterMessageListener getFlutterMessageListener() { return flutterMessageListener; }
    public void setCurrentActivity(Activity currentActivity) { this.currentActivity = currentActivity; }
    public void setFlutter(FlutterOG flutter) { this.flutter = flutter; }
    public void setFlutterMessageListener(FlutterMessageListener flutterMessageListener) { this.flutterMessageListener = flutterMessageListener; }
    public void setFlutterConnectListener(FlutterConnectListener flutterConnectListener) { this.flutterConnectListener = flutterConnectListener; }


    public Session(Activity currentActivity, FlutterOG flutter, FlutterConnectListener flutterConnectListener, FlutterMessageListener flutterMessageListener) {
        this.currentActivity = currentActivity;
        this.flutter = flutter;
        this.flutterConnectListener = flutterConnectListener;
        this.flutterMessageListener = flutterMessageListener;
    }


    @Override
    public void onFlutterMessageReceived(String request, String response) {
        Log.d(Constants.LOG_TAG, "onFlutterMessageReceived - " + " " + request + " "  + response);
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

                    Sensor[] sensors = getFlutter().getSensors();
                    sensors[0].setSensorReading(value1);
                    sensors[1].setSensorReading(value2);
                    sensors[2].setSensorReading(value3);
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
                break;
            case FlutterProtocol.Commands.SET_INPUT_TYPE:
                break;
            case FlutterProtocol.Commands.READ_INPUT_TYPE:
                if (args.length != 2) {
                    Log.e(Constants.LOG_TAG,"invalid number of arguments for READ_INPUT_TYPE="+response);
                } else {
                    // TODO you need to know the input that you were asking about
                    short inputType = Integer.valueOf(args[1],16).shortValue();

                    // TODO actions
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
