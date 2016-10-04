package org.cmucreatelab.flutter_android.classes.sensors;

import org.cmucreatelab.flutter_android.R;

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
    private static final int highTextId = R.string.high;
    private static final int lowTextId = R.string.low;

    public static final int blueImageId = R.drawable.sensor_blue_humidity;
    public static final int greenImageId = R.drawable.sensor_green_humidity;
    public static final int orangeImageId = R.drawable.sensor_orange_humidity;


    public Humidity() {
        // empty
    }


    @Override
    public Type getSensorType() {
        return sensorType;
    }


    @Override
    public int getHighTextId() {
        return highTextId;
    }


    @Override
    public int getLowTextId() {
        return lowTextId;
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
