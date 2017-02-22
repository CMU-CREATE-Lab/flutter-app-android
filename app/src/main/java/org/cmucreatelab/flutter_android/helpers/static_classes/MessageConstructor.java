package org.cmucreatelab.flutter_android.helpers.static_classes;

import android.util.Log;

import org.cmucreatelab.android.melodysmart.models.MelodySmartMessage;
import org.cmucreatelab.flutter_android.classes.outputs.Output;
import org.cmucreatelab.flutter_android.classes.relationships.Constant;
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


    public static MelodySmartMessage constructReadSensorValues() {
        // Request: 'r'
        return new MelodySmartMessage(String.valueOf(FlutterProtocol.Commands.READ_SENSOR_VALUES));
    }


    public static MelodySmartMessage constructRemoveRelation(Output output) {
        // Request: 'xoutput'
        return new MelodySmartMessage(String.valueOf(FlutterProtocol.Commands.REMOVE_RELATION)+output.getProtocolString());
    }


    public static MelodySmartMessage constructRemoveAllRelations() {
        // Request: 'X'
        return new MelodySmartMessage(String.valueOf(FlutterProtocol.Commands.REMOVE_ALL_RELATIONS));
    }


    // ASSERT: unixTime is 8 bytes, loggingInterval is 4 bytes, samples is 2 bytes (all are treated as unsigned)
    public static MelodySmartMessage constructStartLogging(long unixTime, int loggingInterval, short samples) {
        // Request: 'l,unix_time,logging_interval,samples'
        return new MelodySmartMessage(String.valueOf(FlutterProtocol.Commands.START_LOGGING)+","+Long.toHexString(unixTime)+","+Integer.toHexString(loggingInterval)+","+shortToHexString(samples));
    }


    public static MelodySmartMessage constructStopLogging() {
        // Request: 'L'
        return new MelodySmartMessage(String.valueOf(FlutterProtocol.Commands.STOP_LOGGING));
    }


    public static MelodySmartMessage constructSetLogName(String logName) {
        // Request: 'n,log_name'
        return new MelodySmartMessage(String.valueOf(FlutterProtocol.Commands.SET_LOG_NAME)+","+logName);
    }


    public static MelodySmartMessage constructReadLogName() {
        // Request: 'N'
        return new MelodySmartMessage(String.valueOf(FlutterProtocol.Commands.READ_LOG_NAME));
    }


    public static MelodySmartMessage constructReadNumberPointsAvailable() {
        // Request: 'P'
        return new MelodySmartMessage(String.valueOf(FlutterProtocol.Commands.READ_NUMBER_POINTS_AVAILABLE));
    }


    public static MelodySmartMessage constructReadPoint(short pointNumber) {
        // Request: 'R,pointNumber'
        return new MelodySmartMessage(String.valueOf(FlutterProtocol.Commands.READ_POINT)+","+shortToHexString(pointNumber));
    }


    public static MelodySmartMessage constructDeleteLog() {
        // Request: 'D'
        return new MelodySmartMessage(String.valueOf(FlutterProtocol.Commands.DELETE_LOG));
    }


    public static MelodySmartMessage constructReadOutputState(Output output) {
        // Request: 'Ooutput'
        return new MelodySmartMessage(String.valueOf(FlutterProtocol.Commands.READ_OUTPUT_STATE)+output.getProtocolString());
    }


    public static MelodySmartMessage constructSetInputType(Sensor sensor, short inputType) {
        // Request: 'y,input,type'
        return new MelodySmartMessage(String.valueOf(FlutterProtocol.Commands.SET_INPUT_TYPE)+","+sensor.getPortNumber()+","+inputType);
    }


    public static MelodySmartMessage constructReadInputType(Sensor input) {
        // Request: 'Y,input'
        return new MelodySmartMessage(String.valueOf(FlutterProtocol.Commands.READ_INPUT_TYPE)+","+input.getPortNumber());
    }


    // Links


    public static MelodySmartMessage constructSetOutput(Output output, int value) {
        // Request: 'soutput,value'
        return new MelodySmartMessage(String.valueOf(FlutterProtocol.Commands.SET_OUTPUT)+output.getProtocolString()+","+Integer.toHexString(value));
    }


    public static MelodySmartMessage constructEnableProportionalControl(Output output, Sensor input, int minOutputValue, int maxOutputValue, int minInputValue, int maxInputValue) {
        // Request: 'poutput,minOutputValue,maxOutputValue,input,minInputValue,maxInputValue'
        return new MelodySmartMessage(String.valueOf(FlutterProtocol.Commands.ENABLE_PROPORTIONAL_CONTROL)+output.getProtocolString()+","+Integer.toHexString(minOutputValue)+","+Integer.toHexString(maxOutputValue)+","+input.getPortNumber()+","+Integer.toHexString(minInputValue)+","+Integer.toHexString(maxInputValue));
    }


    // TODO @tasota this is just a helper for now, but it might be okay to handle all relationships this way
    public static MelodySmartMessage constructRelationshipMessage(Output output, Settings settings) {
        MelodySmartMessage result = null;

        if (settings.getRelationship().getClass() == Proportional.class) {
            Sensor sensor = settings.getSensor();
            // check for inverted sensor
            if (sensor.isInverted()) {
                result = constructEnableProportionalControl(output, sensor, settings.getOutputMax(), settings.getOutputMin(), sensor.percentToVoltage(settings.getAdvancedSettings().getInputMin()), sensor.percentToVoltage(settings.getAdvancedSettings().getInputMax()));
            } else {
                result = constructEnableProportionalControl(output, sensor, settings.getOutputMin(), settings.getOutputMax(), sensor.percentToVoltage(settings.getAdvancedSettings().getInputMin()), sensor.percentToVoltage(settings.getAdvancedSettings().getInputMax()));
            }
        } else if (settings.getRelationship().getClass() == Constant.class) {
            // TODO @tasota use a real structure for (constant) Settings
            result = constructSetOutput(output, settings.getOutputMax());
        } else {
            Log.e(Constants.LOG_TAG,"relationship not implemented in constructRelationshipMessage: " + settings.getRelationship().getClass());
        }

        return result;
    }

}
