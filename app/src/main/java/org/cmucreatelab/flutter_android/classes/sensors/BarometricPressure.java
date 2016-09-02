package org.cmucreatelab.flutter_android.classes.sensors;

import org.cmucreatelab.flutter_android.R;

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
    public static final int imageId = R.mipmap.ic_launcher;


    public BarometricPressure() {
        // empty
    }


    @Override
    public Type getSensorType() {
        return sensorType;
    }

}
