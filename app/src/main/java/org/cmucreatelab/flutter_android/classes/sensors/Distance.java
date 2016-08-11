package org.cmucreatelab.flutter_android.classes.sensors;

/**
 * Created by Steve on 8/11/2016.
 *
 * Distance
 *
 * A class representing a distance sensor.
 *
 */
public class Distance implements Sensor {

    private static final Sensor.Type sensorType = Type.DISTANCE;


    @Override
    public Type getSensorType() {
        return sensorType;
    }


    @Override
    public int getSensorReading() {
        return 0;
    }

}
