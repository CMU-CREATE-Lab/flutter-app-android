package org.cmucreatelab.flutter_android.classes.sensors;

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


    public AnalogOrUnknown() {
        // empty
    }


    @Override
    public Type getSensorType() {
        return sensorType;
    }

}
