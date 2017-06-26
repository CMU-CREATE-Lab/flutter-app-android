package org.cmucreatelab.flutter_android.classes;

import android.util.Log;

import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseNavigationActivity;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseSensorReadingActivity;
import org.cmucreatelab.flutter_android.classes.datalogging.DataSet;
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
import org.cmucreatelab.flutter_android.helpers.datalogging.DataLoggingHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.FlutterProtocol;
import org.cmucreatelab.flutter_android.helpers.static_classes.MessageConstructor;

import java.util.TreeMap;

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
    private boolean wasSensorOneSetThisSession;
    private boolean wasSensorTwoSetThisSession;
    private boolean wasSensorThreeSetThisSession;
    // getters/setters
    public BaseNavigationActivity getCurrentActivity() { return currentActivity; }
    public Flutter getFlutter() { return flutter; }
    public FlutterConnectListener getFlutterConnectListener() { return flutterConnectListener; }
    public boolean isSimulatingData() { return isSimulatingData; }
    public void setCurrentActivity(BaseNavigationActivity currentActivity) { this.currentActivity = currentActivity; }
    public void setFlutter(Flutter flutter) { this.flutter = flutter; }
    public void setFlutterConnectListener(FlutterConnectListener flutterConnectListener) { this.flutterConnectListener = flutterConnectListener; }


    public boolean wasPortSetThisSession(Integer portNumber) {
        switch (portNumber) {
            case 1:
                return wasSensorOneSetThisSession;
            case 2:
                return wasSensorTwoSetThisSession;
            case 3:
                return wasSensorThreeSetThisSession;
            default:
                return false;
        }
    }

    public void setWasPortSetThisSession(Integer portNumber) {
        switch (portNumber) {
            case 1:
                wasSensorOneSetThisSession = true;
                break;
            case 2:
                wasSensorTwoSetThisSession = true;
                break;
            case 3:
                wasSensorThreeSetThisSession = true;
                break;
            default:
                break;
        }
    }

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
        this.isSimulatingData = false;
        this.wasSensorOneSetThisSession = false;
        this.wasSensorTwoSetThisSession = false;
        this.wasSensorThreeSetThisSession = false;
    }


    // Integer/Hex Parsing helpers (avoid exceptions from malformed strings)


    private Integer parseHex(String value) {
        Integer result;
        try {
            result = Integer.valueOf(value, 16);
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG,"could not parse '"+value+"' as hex; returning 0.");
            result = 0;
        }
        return result;
    }


    private Integer parseInt(String value) {
        Integer result;
        try {
            result = Integer.valueOf(value);
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG,"could not parse '"+value+"' as an integer; returning 0.");
            result = 0;
        }
        return result;
    }


    // FlutterMessageListener methods


    @Override
    public void onFlutterMessageReceived(String request, String response) {
        Log.v(Constants.LOG_TAG, "Session.onFlutterMessageReceived - " + request + " "  + response);
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
                    value1 = parseInt(args[1]).shortValue();
                    value2 = parseInt(args[2]).shortValue();
                    value3 = parseInt(args[3]).shortValue();
                    if (this.isSimulatingData()) {
                        Log.w(Constants.LOG_TAG, "setting sensor values but flutter is flagged as simulating data; ignoring response.");
                    } else {
                        flutter.setSensorValues(value1, value2, value3);
                        ((BaseSensorReadingActivity) currentActivity).updateSensorViews();
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
                if (response.equals("N,")) {
                    Log.w(Constants.LOG_TAG, "No datalog found on Flutter.");
                } else if (args.length != 2) {
                    Log.e(Constants.LOG_TAG,"invalid number of arguments for READ_LOG_NAME="+response);
                } else {
                    String logName = args[1];
                    DataLoggingHandler dataLoggingHandler = GlobalHandler.getInstance(currentActivity).dataLoggingHandler;
                    dataLoggingHandler.setDataName(logName);
                }
                break;
            case FlutterProtocol.Commands.READ_NUMBER_POINTS_AVAILABLE:
                if (args.length != 4) {
                    Log.e(Constants.LOG_TAG,"invalid number of arguments for READ_NUMBER_POINTS_AVAILABLE="+response);
                } else {
                    DataLoggingHandler dataLoggingHandler = GlobalHandler.getInstance(currentActivity).dataLoggingHandler;
                    short numberOfPoints,totalNeeded;
                    boolean currentlyLogging = !args[3].equals("0");
                    numberOfPoints = Integer.valueOf(args[1]).shortValue();
                    totalNeeded = Integer.valueOf(args[2]).shortValue();

                    dataLoggingHandler.setIsLogging(currentlyLogging);
                    dataLoggingHandler.setNumberOfPoints(numberOfPoints);
                    dataLoggingHandler.setTotalPoints(totalNeeded);
                    dataLoggingHandler.getDataSetPointsListener().onDataSetPointsPopulated(true);
                }
                break;
            case FlutterProtocol.Commands.READ_POINT:
                if (args.length != 3) {
                    Log.e(Constants.LOG_TAG,"invalid number of arguments for READ_POINT="+response);
                } else {
                    // If the time is ffffffff, there is no point available
                    if (args[1].equals("ffffffff")) {
                        Log.w(Constants.LOG_TAG,"no point available for READ_POINT (first arg is ffffffff)");
                    } else {
                        long unixTime = Long.valueOf(args[1], 16);
                        short sensor1, sensor2, sensor3;
                        String sensorValues = args[2];
                        sensor1 = Integer.valueOf(sensorValues.substring(0,2), 16).shortValue();
                        sensor2 = Integer.valueOf(sensorValues.substring(2, 4), 16).shortValue();
                        sensor3 = Integer.valueOf(sensorValues.substring(4,6), 16).shortValue();

                        DataLoggingHandler dataLoggingHandler = GlobalHandler.getInstance(currentActivity).dataLoggingHandler;
                        dataLoggingHandler.updateDataSet(unixTime, sensor1, sensor2, sensor3);

                        TreeMap data = dataLoggingHandler.getData();
                        if ((data.size() == dataLoggingHandler.getNumberOfPoints() || data.size() == dataLoggingHandler.getTotalPoints()) && data.size() != 0
                                && request.substring(0,1).equals(String.valueOf(FlutterProtocol.Commands.READ_POINT))
                                && currentActivity != null) {
                            Sensor[] sensors;
                            sensors = getFlutter().getSensors();
                            String flutterName = getFlutter().getName();
                            final DataSet dataSet = new DataSet(data, dataLoggingHandler.getKeys() , dataLoggingHandler.getDataName(), flutterName, sensors);

                            dataLoggingHandler.getDataSetListener().onDataSetPopulated(dataSet);
                        }
                    }
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
                        omin = parseHex(args[2]);
                        omax = parseHex(args[3]);
                        portNumber = parseHex(args[4]);
                        imin = parseHex(args[5]);
                        imax = parseHex(args[6]);

                        Sensor sensor = flutter.getSensors()[portNumber-1];
                        SettingsProportional settings = SettingsProportional.newInstance(output.getSettings());
                        settings.setSensorPortNumber(sensor.getPortNumber());
                        settings.setOutputMin(omin);
                        settings.setOutputMax(omax);
                        settings.getAdvancedSettings().setVoltageMin(imin);
                        settings.getAdvancedSettings().setVoltageMax(imax);
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
                        omin = parseHex(args[2]);
                        omax = parseHex(args[3]);
                        portNumber = parseInt(args[4]);
                        imin = parseHex(args[5]);
                        imax = parseHex(args[6]);
                        speed = parseHex(args[7]);

                        Sensor sensor = flutter.getSensors()[portNumber-1];
                        SettingsAmplitude settings = SettingsAmplitude.newInstance(output.getSettings());
                        settings.setSensorPortNumber(sensor.getPortNumber());
                        settings.setOutputMin(omin);
                        settings.setOutputMax(omax);
                        settings.getAdvancedSettings().setVoltageMin(imin);
                        settings.getAdvancedSettings().setVoltageMax(imax);
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
                        omin = parseHex(args[2]);
                        omax = parseHex(args[3]);
                        portNumber = parseInt(args[4]);
                        imin = parseHex(args[5]);
                        imax = parseHex(args[6]);

                        Sensor sensor = flutter.getSensors()[portNumber-1];
                        SettingsFrequency settings = SettingsFrequency.newInstance(output.getSettings());
                        settings.setSensorPortNumber(sensor.getPortNumber());
                        settings.setOutputMin(omin);
                        settings.setOutputMax(omax);
                        settings.getAdvancedSettings().setVoltageMin(imin);
                        settings.getAdvancedSettings().setVoltageMax(imax);
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
                        omin = parseHex(args[2]);
                        omax = parseHex(args[3]);
                        portNumber = parseInt(args[4]);
                        imin = parseHex(args[5]);
                        imax = parseHex(args[6]);

                        Sensor sensor = flutter.getSensors()[portNumber-1];
                        SettingsChange settings = SettingsChange.newInstance(output.getSettings());
                        settings.setSensorPortNumber(sensor.getPortNumber());
                        settings.setOutputMin(omin);
                        settings.setOutputMax(omax);
                        settings.getAdvancedSettings().setVoltageMin(imin);
                        settings.getAdvancedSettings().setVoltageMax(imax);
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
                        omin = parseHex(args[2]);
                        omax = parseHex(args[3]);
                        portNumber = parseInt(args[4]);
                        imin = parseHex(args[5]);
                        imax = parseHex(args[6]);
                        center = parseHex(args[7]);
                        speed = parseHex(args[8]);

                        Sensor sensor = flutter.getSensors()[portNumber-1];
                        SettingsCumulative settings = SettingsCumulative.newInstance(output.getSettings());
                        settings.setSensorPortNumber(sensor.getPortNumber());
                        settings.setOutputMin(omin);
                        settings.setOutputMax(omax);
                        settings.getAdvancedSettings().setVoltageMin(imin);
                        settings.getAdvancedSettings().setVoltageMax(imax);
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
                    position = parseHex(args[1]);

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
                    int portNumber = parseInt(request.split(",")[1]);
                    short inputType = parseInt(args[1]).shortValue();

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
