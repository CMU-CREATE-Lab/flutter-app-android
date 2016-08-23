package org.cmucreatelab.flutter_android.classes.sensors;

import java.io.Serializable;

/**
 * Created by Steve on 8/11/2016.
 *
 * Sound
 *
 * A class representing a sound sensor.
 *
 */
public class Sound extends A_Sensor implements Sensor, Serializable {

    private static final Sensor.Type sensorType = Type.SOUND;


    public Sound() {
        // empty
    }


    @Override
    public Type getSensorType() {
        return sensorType;
    }

}
