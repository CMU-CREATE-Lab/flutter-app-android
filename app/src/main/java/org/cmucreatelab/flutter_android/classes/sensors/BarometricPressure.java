package org.cmucreatelab.flutter_android.classes.sensors;

import java.io.Serializable;

/**
 * Created by Steve on 8/11/2016.
 *
 * BarometricPressure
 *
 * A class representing a barometric pressure sensor.
 *
 */
public class BarometricPressure extends A_Sensor implements Sensor, Serializable {

    private static final Sensor.Type sensorType = Type.BAROMETRIC_PRESSURE;


    public BarometricPressure() {
        // empty
    }


    @Override
    public Type getSensorType() {
        return sensorType;
    }


    @Override
    public int getSensorReading() {
        return 0;
    }

}
