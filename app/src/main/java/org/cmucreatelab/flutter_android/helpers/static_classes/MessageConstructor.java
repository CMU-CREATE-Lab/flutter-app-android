package org.cmucreatelab.flutter_android.helpers.static_classes;

import android.util.Log;

import org.cmucreatelab.android.melodysmart.models.MelodySmartMessage;
import org.cmucreatelab.flutter_android.classes.outputs.Output;
import org.cmucreatelab.flutter_android.classes.outputs.Pitch;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.settings.Settings;
import org.cmucreatelab.flutter_android.classes.settings.SettingsAmplitude;
import org.cmucreatelab.flutter_android.classes.settings.SettingsConstant;
import org.cmucreatelab.flutter_android.classes.settings.SettingsProportional;

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


    public static MelodySmartMessage constructEnableAmplitudeControl(Output output, Sensor input, int minOutputValue, int maxOutputValue, int minInputValue, int maxInputValue, int speed) {
        // Request: ‘aoutput,minOutputValue,maxOutputValue,input,minInputValue,maxInputValue,speed’
        return new MelodySmartMessage(String.valueOf(FlutterProtocol.Commands.ENABLE_AMPLITUDE_CONTROL)+output.getProtocolString()+","+Integer.toHexString(minOutputValue)+","+Integer.toHexString(maxOutputValue)+","+input.getPortNumber()+","+Integer.toHexString(minInputValue)+","+Integer.toHexString(maxInputValue)+","+Integer.toHexString(speed));
    }


    public static MelodySmartMessage constructRelationshipMessage(Output output, Settings settings) {
        MelodySmartMessage result = null;

        if (settings.getClass() == SettingsProportional.class) {
            SettingsProportional settingsProportional = (SettingsProportional) settings;
            Sensor sensor = settings.getSensor();
            // check for inverted sensor
            if (sensor.isInverted()) {
                result = constructEnableProportionalControl(output, sensor, settingsProportional.getOutputMax(), settingsProportional.getOutputMin(), sensor.percentToVoltage(settingsProportional.getAdvancedSettings().getInputMin()), sensor.percentToVoltage(settingsProportional.getAdvancedSettings().getInputMax()));
            } else {
                result = constructEnableProportionalControl(output, sensor, settingsProportional.getOutputMin(), settingsProportional.getOutputMax(), sensor.percentToVoltage(settingsProportional.getAdvancedSettings().getInputMin()), sensor.percentToVoltage(settingsProportional.getAdvancedSettings().getInputMax()));
            }
        } else if (settings.getClass() == SettingsConstant.class) {
            SettingsConstant settingsConstant = (SettingsConstant) settings;
            result = constructSetOutput(output, settingsConstant.getValue());
        } else if (settings.getClass() == SettingsAmplitude.class) {
            SettingsAmplitude settingsAmplitude = (SettingsAmplitude) settings;
            Sensor sensor = settings.getSensor();

            // For buzzer pitch, output values are scaled by 10 (to reduce packet size)
            int omin,omax;
            if (output.getClass() == Pitch.class) {
                omin = settingsAmplitude.getOutputMin() / 10;
                omax = settingsAmplitude.getOutputMax() / 10;
            } else {
                omin = settingsAmplitude.getOutputMin();
                omax = settingsAmplitude.getOutputMax();
            }

            // speed values range from 0-15
            int speed = settingsAmplitude.getAdvancedSettings().getZeroValue();
            if (speed < 0) {
                Log.w(Constants.LOG_TAG, "tried settings Amplitude speed below 0; reset to 0.");
                speed = 0;
            }
            if (speed > 15) {
                Log.w(Constants.LOG_TAG, "tried settings Amplitude speed above 15; reset to 15.");
                speed = 15;
            }

            // check for inverted sensor
            if (sensor.isInverted()) {
                result = constructEnableAmplitudeControl(output, sensor, omax, omin, sensor.percentToVoltage(settingsAmplitude.getAdvancedSettings().getInputMin()), sensor.percentToVoltage(settingsAmplitude.getAdvancedSettings().getInputMax()), speed);
            } else {
                result = constructEnableAmplitudeControl(output, sensor, omin, omax, sensor.percentToVoltage(settingsAmplitude.getAdvancedSettings().getInputMin()), sensor.percentToVoltage(settingsAmplitude.getAdvancedSettings().getInputMax()), speed);
            }
        } else {
            Log.e(Constants.LOG_TAG,"relationship not implemented in constructRelationshipMessage: " + settings.getRelationship().getClass());
        }

        return result;
    }

}
