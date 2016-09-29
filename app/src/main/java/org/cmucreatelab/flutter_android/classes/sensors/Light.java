package org.cmucreatelab.flutter_android.classes.sensors;

import org.cmucreatelab.flutter_android.R;

import java.io.Serializable;

/**
 * Created by Steve on 8/11/2016.
 *
 * Light
 *
 * A class representing a light sensor.
 *
 */
public class Light extends A_Sensor implements Sensor, Serializable {

    private static final Sensor.Type sensorType = Type.LIGHT;
    public static final int blueImageId = R.drawable.sensor_blue_light;
    public static final int greenImageId = R.drawable.sensor_green_light;
    public static final int orangeImageId = R.drawable.sensor_orange_light;


    public Light() {
        // empty
    }


    @Override
    public Type getSensorType() {
        return sensorType;
    }


    @Override
    public int getBlueImageId() {
        return blueImageId;
    }


    @Override
    public int getGreenImageId() {
        return greenImageId;
    }


    @Override
    public int getOrangeImageId() {
        return orangeImageId;
    }

}
