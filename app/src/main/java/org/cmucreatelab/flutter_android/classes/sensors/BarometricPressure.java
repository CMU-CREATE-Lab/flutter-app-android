package org.cmucreatelab.flutter_android.classes.sensors;

/**
 * Created by Steve on 8/11/2016.
 *
 * BarometricPressure
 *
 * A class representing a barometric pressure sensor.
 *
 */
public class BarometricPressure implements Sensor {

    private static final Sensor.Type sensorType = Type.BAROMETRIC_PRESSURE;


    @Override
    public Type getSensorType() {
        return sensorType;
    }


    @Override
    public int getSensorReading() {
        return 0;
    }

}
