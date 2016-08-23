package org.cmucreatelab.flutter_android.classes.sensors;

import java.io.Serializable;

/**
 * Created by Steve on 8/11/2016.
 *
 * WindSpeed
 *
 * A class representing a wind speed sensor.
 *
 */
public class WindSpeed extends A_Sensor implements Sensor, Serializable {

    private static final Sensor.Type sensorType = Type.WIND_SPEED;


    public WindSpeed() {
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
