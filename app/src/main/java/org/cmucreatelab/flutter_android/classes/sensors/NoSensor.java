package org.cmucreatelab.flutter_android.classes.sensors;

/**
 * Created by Steve on 8/11/2016.
 *
 * NoSensor
 *
 * A class that represents no sensor.
 *
 */
public class NoSensor extends A_Sensor implements Sensor{

    private static final Sensor.Type sensorType = Type.NO_SENSOR;


    @Override
    public Type getSensorType() {
        return null;
    }


    @Override
    public int getSensorReading() {
        return 0;
    }

}
