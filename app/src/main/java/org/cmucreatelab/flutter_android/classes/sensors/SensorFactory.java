package org.cmucreatelab.flutter_android.classes.sensors;

/**
 * Created by Steve on 8/23/2016.
 */
public class SensorFactory {


    public static Sensor getSensorType(Sensor.Type type) {
        Sensor result;

        switch (type) {
            case LIGHT:
                result = new Light();
                break;
            case SOIL_MOISTURE:
                result = new SoilMoisture();
                break;
            case DISTANCE:
                result = new Distance();
                break;
            case SOUND:
                result = new Sound();
                break;
            case WIND_SPEED:
                result = new WindSpeed();
                break;
            case HUMIDITY:
                result = new Humidity();
                break;
            case TEMPERATURE:
                result = new Temperature();
                break;
            case BAROMETRIC_PRESSURE:
                result = new BarometricPressure();
                break;
            case ANALOG_OR_UNKNOWN:
                result = new AnalogOrUnknown();
                break;
            case NO_SENSOR:
                result = new NoSensor();
                break;
            default:
                result = new AnalogOrUnknown();
                break;
        }

        return result;
    }

}
