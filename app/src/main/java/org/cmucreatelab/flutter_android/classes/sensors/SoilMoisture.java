package org.cmucreatelab.flutter_android.classes.sensors;

/**
 * Created by Steve on 8/11/2016.
 *
 * SoilMoisture
 *
 * A class representing a soil moisture sensor.
 *
 */
public class SoilMoisture implements Sensor{

    private static final Sensor.Type sensorType = Type.SOIL_MOISTURE;


    @Override
    public Type getSensorType() {
        return sensorType;
    }


    @Override
    public int getSensorReading() {
        return 0;
    }

}
