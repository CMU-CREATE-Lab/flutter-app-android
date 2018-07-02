package org.cmucreatelab.flutter_android.helpers.static_classes;

import android.util.Log;

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
import org.cmucreatelab.flutter_android.classes.sensors.AirQualitySensor;

/**
 * Created by Steve on 1/30/2017.
 *
 * SensorFactory
 *
 * A class for retrieving the type of a sensor.
 *
 */
public class SensorFactory {


    public static Sensor getSensorFromName(int port, String name) {
        Sensor result = new NoSensor(port);

        Log.d(Constants.LOG_TAG, "The name from file is - " + name);
        switch(name) {
            case Constants.SensorTypeWords.ANALOG_OR_UNKNOWN:
                result = new AnalogOrUnknownSensor(port);
                break;
            case Constants.SensorTypeWords.BAROMETRIC_PRESSURE:
                result = new BarometricPressureSensor(port);
                break;
            case Constants.SensorTypeWords.DISTANCE:
                result = new DistanceSensor(port);
                break;
            case Constants.SensorTypeWords.HUMIDITY:
                result = new HumiditySensor(port);
                break;
            case Constants.SensorTypeWords.LIGHT:
                result = new LightSensor(port);
                break;
            case Constants.SensorTypeWords.SOIL_MOISTURE:
                result = new SoilMoistureSensor(port);
                break;
            case Constants.SensorTypeWords.SOUND:
                result = new SoundSensor(port);
                break;
            case Constants.SensorTypeWords.TEMPERATURE:
                result = new TemperatureSensor(port);
                break;
            case Constants.SensorTypeWords.AIR_QUALITY:
                result = new AirQualitySensor(port);
                break;
            default:
                break;
        }

        return result;
    }


    private SensorFactory() {}

}
