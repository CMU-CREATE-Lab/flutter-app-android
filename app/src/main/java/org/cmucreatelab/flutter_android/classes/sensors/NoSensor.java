package org.cmucreatelab.flutter_android.classes.sensors;

import org.cmucreatelab.flutter_android.R;

import java.io.Serializable;

/**
 * Created by Steve on 8/11/2016.
 *
 * NoSensor
 *
 * A class that represents no sensor.
 *
 */
public class NoSensor extends A_Sensor implements Sensor, Serializable {

    private static final Sensor.Type sensorType = Type.NO_SENSOR;
    public static final int imageId = R.mipmap.ic_launcher;


    public NoSensor() {
        // empty
    }


    @Override
    public Type getSensorType() {
        return sensorType;
    }


    @Override
    public int getSensorImageId() {
        return imageId;
    }


    @Override
    public int getSensorReading() {
        return 0;
    }

}
