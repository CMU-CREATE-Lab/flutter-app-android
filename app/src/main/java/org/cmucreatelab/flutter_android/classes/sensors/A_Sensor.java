package org.cmucreatelab.flutter_android.classes.sensors;

import java.io.Serializable;

/**
 * Created by Steve on 8/11/2016.
 *
 * A_Sensor
 *
 * An abstract class that implements the sensor readings values and the port number.
 *
 */
public abstract class A_Sensor implements Sensor, Serializable {

    private int reading;
    private int portNumber;


    public A_Sensor(int portNumber) {
        reading = 0;
        this.portNumber = portNumber;
    }


    @Override
    public int getSensorReading() {
        return reading;
    }


    @Override
    public int getPortNumber() {
        return portNumber;
    }


    @Override
    public void setSensorReading(int value) {
        reading = value;
    }

}
