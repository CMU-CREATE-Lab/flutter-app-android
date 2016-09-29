package org.cmucreatelab.flutter_android.classes.sensors;

import org.cmucreatelab.flutter_android.R;

import java.io.Serializable;

/**
 * Created by Steve on 8/11/2016.
 *
 * AnalogOrUnknown
 *
 * A class representing an analog or unknown sensor.
 *
 */
public class AnalogOrUnknown extends A_Sensor implements Sensor, Serializable {

    private static final Sensor.Type sensorType = Type.ANALOG_OR_UNKNOWN;
    public static final int blueImageId = R.drawable.sensor_blue_analog;
    public static final int greenImageId = R.drawable.sensor_green_analog;
    public static final int orangeImageId = R.drawable.sensor_orange_analog;


    public AnalogOrUnknown() {
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
