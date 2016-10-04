package org.cmucreatelab.flutter_android.classes.sensors;

import org.cmucreatelab.flutter_android.R;

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
    private static final int sensorTypeId = R.string.distance;
    private static final int highTextId = R.string.high;
    private static final int lowTextId = R.string.low;

    public static final int blueImageId = R.drawable.sensor_blue_distance;
    public static final int greenImageId = R.drawable.sensor_green_distance;
    public static final int orangeImageId = R.drawable.sensor_orange_distance;


    public Distance() {
        // empty
    }


    @Override
    public Type getSensorType() {
        return sensorType;
    }


    @Override
    public int getSensorTypeId() {
        return sensorTypeId;
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
