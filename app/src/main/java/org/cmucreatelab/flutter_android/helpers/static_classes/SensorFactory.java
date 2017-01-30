package org.cmucreatelab.flutter_android.helpers.static_classes;

import android.util.Log;

import org.cmucreatelab.flutter_android.classes.sensors.AnalogOrUnknown;
import org.cmucreatelab.flutter_android.classes.sensors.BarometricPressure;
import org.cmucreatelab.flutter_android.classes.sensors.Distance;
import org.cmucreatelab.flutter_android.classes.sensors.Humidity;
import org.cmucreatelab.flutter_android.classes.sensors.Light;
import org.cmucreatelab.flutter_android.classes.sensors.NoSensor;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.sensors.SoilMoisture;
import org.cmucreatelab.flutter_android.classes.sensors.Sound;
import org.cmucreatelab.flutter_android.classes.sensors.Temperature;
import org.cmucreatelab.flutter_android.classes.sensors.WindSpeed;

/**
 * Created by Steve on 1/30/2017.
 */
public class SensorFactory {


    public static Sensor getSensorFromName(int port, String name) {
        Sensor result = new NoSensor(port);

        Log.d(Constants.LOG_TAG, "The name from file is - " + name);
        switch(name) {
            case Constants.SensorTypeWords.ANALOG_OR_UNKNOWN:
                result = new AnalogOrUnknown(port);
                break;
            case Constants.SensorTypeWords.BAROMETRIC_PRESSURE:
                result = new BarometricPressure(port);
                break;
            case Constants.SensorTypeWords.DISTANCE:
                result = new Distance(port);
                break;
            case Constants.SensorTypeWords.HUMIDITY:
                result = new Humidity(port);
                break;
            case Constants.SensorTypeWords.LIGHT:
                result = new Light(port);
                break;
            case Constants.SensorTypeWords.SOIL_MOISTURE:
                result = new SoilMoisture(port);
                break;
            case Constants.SensorTypeWords.SOUND:
                result = new Sound(port);
                break;
            case Constants.SensorTypeWords.TEMPERATURE:
                result = new Temperature(port);
                break;
            case Constants.SensorTypeWords.WIND_SPEED:
                result = new WindSpeed(port);
                break;
            default:
                break;
        }

        return result;
    }


    private SensorFactory() {}

}
