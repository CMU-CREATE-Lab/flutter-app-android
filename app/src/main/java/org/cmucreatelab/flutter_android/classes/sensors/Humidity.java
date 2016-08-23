package org.cmucreatelab.flutter_android.classes.sensors;

import java.io.Serializable;

/**
 * Created by Steve on 8/11/2016.
 *
 * Humidity
 *
 * A class representing a humidity sensor.
 *
 */
public class Humidity extends A_Sensor implements Sensor, Serializable {

    private static final Sensor.Type sensorType = Type.HUMIDITY;


    public Humidity() {
        // empty
    }


    @Override
    public Type getSensorType() {
        return sensorType;
    }

}
