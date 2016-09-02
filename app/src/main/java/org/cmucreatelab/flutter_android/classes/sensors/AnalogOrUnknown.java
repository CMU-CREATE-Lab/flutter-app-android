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
    public static final int imageId = R.mipmap.ic_launcher;


    public AnalogOrUnknown() {
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

}
