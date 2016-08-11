package org.cmucreatelab.flutter_android.classes.sensors;

/**
 * Created by Steve on 8/11/2016.
 *
 * AnalogOrUnknown
 *
 * A class representing an analog or unknown sensor.
 *
 */
public class AnalogOrUnkown implements Sensor{

    private static final Sensor.Type sensorType = Type.ANALOG_OR_UNKNOWN;


    @Override
    public Type getSensorType() {
        return sensorType;
    }


    @Override
    public int getSensorReading() {
        return 0;
    }

}
