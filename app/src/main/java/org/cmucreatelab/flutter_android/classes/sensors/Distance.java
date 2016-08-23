package org.cmucreatelab.flutter_android.classes.sensors;

import java.io.Serializable;

/**
 * Created by Steve on 8/11/2016.
 *
 * Distance
 *
 * A class representing a distance sensor.
 *
 */
public class Distance extends A_Sensor implements Sensor, Serializable {

    private static final Sensor.Type sensorType = Type.DISTANCE;


    public Distance() {
        // empty
    }


    @Override
    public Type getSensorType() {
        return sensorType;
    }

}
