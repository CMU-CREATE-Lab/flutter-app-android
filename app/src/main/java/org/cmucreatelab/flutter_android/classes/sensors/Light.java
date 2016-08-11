package org.cmucreatelab.flutter_android.classes.sensors;

/**
 * Created by Steve on 8/11/2016.
 *
 * Light
 *
 * A class representing a light sensor.
 *
 */
public class Light extends A_Sensor implements Sensor {

    private static final Sensor.Type sensorType = Type.LIGHT;


    @Override
    public Type getSensorType() {
        return sensorType;
    }


    @Override
    public int getSensorReading() {
        return 0;
    }

}
