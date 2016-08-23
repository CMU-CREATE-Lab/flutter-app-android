package org.cmucreatelab.flutter_android.classes.sensors;

import java.io.Serializable;

/**
 * Created by Steve on 8/11/2016.
 *
 * Temperature
 *
 * A class representing a temperature sensor.
 *
 */
public class Temperature extends A_Sensor implements Sensor, Serializable {

    private static final Sensor.Type sensorType = Type.TEMPERATURE;


    public Temperature() {
        // empty
    }


    @Override
    public Type getSensorType() {
        return sensorType;
    }


    @Override
    public int getSensorReading() {
        return 0;
    }

}
