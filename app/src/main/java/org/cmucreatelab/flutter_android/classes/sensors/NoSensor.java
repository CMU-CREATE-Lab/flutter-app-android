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
    private static final int sensorTypeId = R.string.no_sensor;
    private static final int highTextId = R.string.high;
    private static final int lowTextId = R.string.low;

    public static final int blueImageId = R.drawable.sensor_blue_no_sensor;
    public static final int greenImageId = R.drawable.sensor_green_nosensor;
    public static final int orangeImageId = R.drawable.sensor_orange_nosensor;


    public NoSensor() {
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


    @Override
    public int getSensorReading() {
        return 0;
    }

}
