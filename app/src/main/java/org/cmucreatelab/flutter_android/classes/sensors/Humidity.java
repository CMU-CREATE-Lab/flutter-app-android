package org.cmucreatelab.flutter_android.classes.sensors;

/**
 * Created by Steve on 8/11/2016.
 *
 * Humidity
 *
 * A class representing a humidity sensor.
 *
 */
public class Humidity implements Sensor {

    private static final Sensor.Type sensorType = Type.HUMIDITY;


    @Override
    public Type getSensorType() {
        return sensorType;
    }


    @Override
    public int getSensorReading() {
        return 0;
    }

}
