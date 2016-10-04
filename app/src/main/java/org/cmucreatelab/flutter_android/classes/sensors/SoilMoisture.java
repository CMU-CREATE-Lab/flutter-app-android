package org.cmucreatelab.flutter_android.classes.sensors;

import org.cmucreatelab.flutter_android.R;

import java.io.Serializable;

/**
 * Created by Steve on 8/11/2016.
 *
 * SoilMoisture
 *
 * A class representing a soil moisture sensor.
 *
 */
public class SoilMoisture extends A_Sensor implements Sensor, Serializable {

    private static final Sensor.Type sensorType = Type.SOIL_MOISTURE;
    private static final int highTextId = R.string.high;
    private static final int lowTextId = R.string.low;

    public static final int blueImageId = R.drawable.sensor_blue_soilmoisture;
    public static final int greenImageId = R.drawable.sensor_green_soilmoisture;
    public static final int orangeImageId = R.drawable.sensor_orange_soilmoisture;


    public SoilMoisture() {
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
