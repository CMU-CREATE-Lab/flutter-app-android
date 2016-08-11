package org.cmucreatelab.flutter_android.classes.sensors;

/**
 * Created by Steve on 8/11/2016.
 *
 * WindSpeed
 *
 * A class representing a wind speed sensor.
 *
 */
public class WindSpeed implements Sensor {

    private static final Sensor.Type sensorType = Type.WIND_SPEED;


    @Override
    public Type getSensorType() {
        return sensorType;
    }


    @Override
    public int getSensorReading() {
        return 0;
    }

}
