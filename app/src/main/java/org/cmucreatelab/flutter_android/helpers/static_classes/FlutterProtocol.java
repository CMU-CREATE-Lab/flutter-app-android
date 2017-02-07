package org.cmucreatelab.flutter_android.helpers.static_classes;

import org.cmucreatelab.flutter_android.classes.sensors.AnalogOrUnknownSensor;
import org.cmucreatelab.flutter_android.classes.sensors.BarometricPressureSensor;
import org.cmucreatelab.flutter_android.classes.sensors.DistanceSensor;
import org.cmucreatelab.flutter_android.classes.sensors.HumiditySensor;
import org.cmucreatelab.flutter_android.classes.sensors.LightSensor;
import org.cmucreatelab.flutter_android.classes.sensors.NoSensor;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.sensors.SoilMoistureSensor;
import org.cmucreatelab.flutter_android.classes.sensors.SoundSensor;
import org.cmucreatelab.flutter_android.classes.sensors.TemperatureSensor;
import org.cmucreatelab.flutter_android.classes.sensors.WindSpeedSensor;

/**
 * Created by mike on 1/4/17.
 *
 * This is based on Josh's document in Google Docs for how to interface with the Flutter board.
 * Serves as an easy reference to constants found in the protocol.
 *
 */
// TODO @tasota these will be used later for constructing flutter protocol messages
public final class FlutterProtocol {

    public final class InputTypes {
        public static final short NOT_SET = 255;
        public static final short LIGHT = 0;
        public static final short SOIL = 1;
        public static final short SOUND = 2;
        public static final short DISTANCE = 3;
        public static final short TEMPERATURE = 4;
        public static final short HUMIDITY = 5;
        public static final short WIND_SPEED = 6;
        public static final short BAROMETRIC_PRESSURE = 7;
        public static final short ANALOG_OR_UNKNOWN = 8;
        // **please add more**
    }

    public static Sensor sensorFromInputType(int portNumber, short inputType) {
        Sensor result;

        switch(inputType) {
            case InputTypes.LIGHT:
                result = new LightSensor(portNumber);
                break;
            case InputTypes.SOIL:
                result = new SoilMoistureSensor(portNumber);
                break;
            case InputTypes.SOUND:
                result = new SoundSensor(portNumber);
                break;
            case InputTypes.DISTANCE:
                result = new DistanceSensor(portNumber);
                break;
            case InputTypes.TEMPERATURE:
                result = new TemperatureSensor(portNumber);
                break;
            case InputTypes.HUMIDITY:
                result = new HumiditySensor(portNumber);
                break;
            case InputTypes.WIND_SPEED:
                result = new WindSpeedSensor(portNumber);
                break;
            case InputTypes.BAROMETRIC_PRESSURE:
                result = new BarometricPressureSensor(portNumber);
                break;
            case InputTypes.ANALOG_OR_UNKNOWN:
                result = new AnalogOrUnknownSensor(portNumber);
                break;
            case InputTypes.NOT_SET:
            default:
                result = new NoSensor(portNumber);
        }

        return result;
    }

    public final class Commands {
        public static final char READ_SENSOR_VALUES = 'r';
        public static final char SET_OUTPUT = 's';
        public static final char REMOVE_RELATION = 'x';
        public static final char REMOVE_ALL_RELATIONS = 'X';
        public static final char START_LOGGING = 'l';
        public static final char STOP_LOGGING = 'L';
        public static final char SET_LOG_NAME = 'n';
        public static final char READ_LOG_NAME = 'N';
        public static final char READ_NUMBER_POINTS_AVAILABLE = 'P';
        public static final char READ_POINT = 'R';
        public static final char DELETE_LOG = 'D';
        public static final char READ_OUTPUT_STATE = 'O';
        public static final char SET_INPUT_TYPE = 'y';
        public static final char READ_INPUT_TYPE = 'Y';
        public static final char ENABLE_PROPORTIONAL_CONTROL = 'p';
    }

//    Output correlations
//    Servos: s1, s2, s3
//    LEDs: r1, g1, b1, r2, g2, b2, r3, g3, b3
//    Buzzer volume: v
//    Buzzer frequency: f

//    Ranges:
//    Servos: 0-180
//    LEDs: 0-100
//    Buzzer volume: 0-100
//    Buzzer frequency: 0-20000
//    Sensor Outputs: 0-100

//    Read values
//    Request   : ‘r’
//    Response: ‘r,value1,value2,value3’

//    Set output
//    Request: ‘soutput,value’

//    Remove relation
//    Request: ‘xoutput’

//    Remove relationships for all outputs
//    Request: ‘X’

//    Start logging
//    Request: ‘l,unix_time,logging_interval,samples’

//    Stop logging
//    Request: ‘L’

//    Set log name
//    Request: ‘n,log_name’

//    Read log name
//    Request: ‘N’
//    Response: ‘N,log_name

//    Read number of points available
//    Request: ‘P’
//    Response: ‘P,number_of_points,total_needed,currently_logging’

//    Read point
//    Request: ‘R,pointNumber’
//    pointNumber’ is 2 bytes long, starting at 0
//    Response: ‘R,unix_time,sensor1,sensor2,sensor3

//    Delete log (erase points and name)
//    Request: ‘D’

//    Read Output state
//    Request: ‘Ooutput’
//    Response: ‘O,outputState’

//    Set Input Tyoe
//    Request: ‘y,input,type’

//    Read input type
//    Request ‘Y,input’
//    Response: ‘Y,type’

//    Enable proportional control
//    Request: ‘poutput,minOutputValue,maxOutputValue,input,minInputValue,maxInputValue’

}
