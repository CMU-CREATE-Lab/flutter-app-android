package org.cmucreatelab.flutter_android.classes.sensors;

/**
 * Created by Steve on 8/11/2016.
 *
 * Temperature
 *
 * A class representing a temperature sensor.
 *
 */
public class Temperature implements Sensor {

    private static final Sensor.Type sensorType = Type.TEMPERATURE;


    @Override
    public Type getSensorType() {
        return sensorType;
    }


    @Override
    public int getSensorReading() {
        return 0;
    }

}
