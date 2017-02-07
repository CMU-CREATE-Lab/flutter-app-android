package org.cmucreatelab.flutter_android.helpers.static_classes;

import android.util.Log;

import org.cmucreatelab.flutter_android.classes.FlutterMessage;
import org.cmucreatelab.flutter_android.classes.outputs.Output;
import org.cmucreatelab.flutter_android.classes.relationships.Proportional;
import org.cmucreatelab.flutter_android.classes.sensors.DistanceSensor;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.settings.Settings;

/**
 * Created by Steve on 8/23/2016.
 *
 * MessageConstructor
 *
 * A static class which will generate the strings depending on the output passed into the appropriate methods
 *
 */
public class MessageConstructor {

    private static String shortToHexString(short value) {
        // only need this because there is no Short.toHexString() function.
        int result= value;
        if (value < 0)
            result = -value + 32767;
        return Integer.toHexString(result);
    }


    public static FlutterMessage constructReadSensorValues() {
        // Request: 'r'
        return new FlutterMessage(String.valueOf(FlutterProtocol.Commands.READ_SENSOR_VALUES));
    }


    public static FlutterMessage constructSetOutput(Output output, int value) {
        // Request: 'soutput,value'
        return new FlutterMessage(String.valueOf(FlutterProtocol.Commands.SET_OUTPUT)+output.getProtocolString()+","+Integer.toHexString(value));
    }


    public static FlutterMessage constructRemoveRelation(Output output) {
        // Request: 'xoutput'
        return new FlutterMessage(String.valueOf(FlutterProtocol.Commands.REMOVE_RELATION)+output.getProtocolString());
    }


    public static FlutterMessage constructRemoveAllRelations() {
        // Request: 'X'
        return new FlutterMessage(String.valueOf(FlutterProtocol.Commands.REMOVE_ALL_RELATIONS));
    }


    // ASSERT: unixTime is 8 bytes, loggingInterval is 4 bytes, samples is 2 bytes (all are treated as unsigned)
    public static FlutterMessage constructStartLogging(long unixTime, int loggingInterval, short samples) {
        // Request: 'l,unix_time,logging_interval,samples'
        return new FlutterMessage(String.valueOf(FlutterProtocol.Commands.START_LOGGING)+","+Long.toHexString(unixTime)+","+Integer.toHexString(loggingInterval)+","+shortToHexString(samples));
    }


    public static FlutterMessage constructStopLogging() {
        // Request: 'L'
        return new FlutterMessage(String.valueOf(FlutterProtocol.Commands.STOP_LOGGING));
    }


    public static FlutterMessage constructSetLogName(String logName) {
        // Request: 'n,log_name'
        return new FlutterMessage(String.valueOf(FlutterProtocol.Commands.SET_LOG_NAME)+","+logName);
    }


    public static FlutterMessage constructReadLogName() {
        // Request: 'N'
        return new FlutterMessage(String.valueOf(FlutterProtocol.Commands.READ_LOG_NAME));
    }


    public static FlutterMessage constructReadNumberPointsAvailable() {
        // Request: 'P'
        return new FlutterMessage(String.valueOf(FlutterProtocol.Commands.READ_NUMBER_POINTS_AVAILABLE));
    }


    public static FlutterMessage constructReadPoint(short pointNumber) {
        // Request: 'R,pointNumber'
        return new FlutterMessage(String.valueOf(FlutterProtocol.Commands.READ_POINT)+","+shortToHexString(pointNumber));
    }


    public static FlutterMessage constructDeleteLog() {
        // Request: 'D'
        return new FlutterMessage(String.valueOf(FlutterProtocol.Commands.DELETE_LOG));
    }


    public static FlutterMessage constructReadOutputState(Output output) {
        // Request: 'Ooutput'
        return new FlutterMessage(String.valueOf(FlutterProtocol.Commands.READ_OUTPUT_STATE)+output.getProtocolString());
    }


    public static FlutterMessage constructSetInputType(Sensor sensor, short inputType) {
        // Request: 'y,input,type'
        return new FlutterMessage(String.valueOf(FlutterProtocol.Commands.SET_INPUT_TYPE)+","+sensor.getPortNumber()+","+inputType);
    }


    public static FlutterMessage constructReadInputType(Sensor input) {
        // Request: 'Y,input'
        return new FlutterMessage(String.valueOf(FlutterProtocol.Commands.READ_INPUT_TYPE)+","+input.getPortNumber());
    }


    public static FlutterMessage constructEnableProportionalControl(Output output, Sensor input, int minOutputValue, int maxOutputValue, int minInputValue, int maxInputValue) {
        // Request: 'poutput,minOutputValue,maxOutputValue,input,minInputValue,maxInputValue'
        return new FlutterMessage(String.valueOf(FlutterProtocol.Commands.ENABLE_PROPORTIONAL_CONTROL)+output.getProtocolString()+","+Integer.toHexString(minOutputValue)+","+Integer.toHexString(maxOutputValue)+","+input.getPortNumber()+","+Integer.toHexString(minInputValue)+","+Integer.toHexString(maxInputValue));
    }


    // TODO @tasota this is just a helper for now, but it might be okay to handle all relationships this way
    public static FlutterMessage constructRelationshipMessage(Output output, Settings settings) {
        FlutterMessage result = null;

        if (settings.getRelationship().getClass() == Proportional.class) {
            // TODO @tasota hacked for inverted distance
            if (settings.getSensor().getSensorType() == FlutterProtocol.InputTypes.DISTANCE) {
                result = constructEnableProportionalControl(output, settings.getSensor(), settings.getOutputMax(), settings.getOutputMin(), DistanceSensor.INPUT_MINIMUM, DistanceSensor.INPUT_MAXIMUM);
            } else {
                result = constructEnableProportionalControl(output, settings.getSensor(), settings.getOutputMin(), settings.getOutputMax(), settings.getAdvancedSettings().getInputMin(), settings.getAdvancedSettings().getInputMax());
            }
        } else {
            Log.e(Constants.LOG_TAG,"relationship not implemented in constructRelationshipMessage: " + settings.getRelationship().getClass());
        }

        return result;
    }

}
