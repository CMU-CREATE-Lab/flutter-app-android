package org.cmucreatelab.flutter_android.classes.sensors;

import org.cmucreatelab.flutter_android.R;

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
    public static final int imageId = R.mipmap.ic_launcher;


    public Temperature() {
        // empty
    }


    @Override
    public Type getSensorType() {
        return sensorType;
    }

}
