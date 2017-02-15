package org.cmucreatelab.flutter_android.classes.sensors;

import java.io.Serializable;

/**
 * Created by Steve on 8/11/2016.
 *
 * Sensor
 *
 * An abstract class that implements the sensor readings values and the port number.
 *
 */
public abstract class Sensor implements Serializable {

    private int reading;
    private int portNumber;

    // getters
    public int getSensorReading() { return reading; }
    public int getPortNumber() { return portNumber; }
    // setters
    public void setSensorReading(int value) { reading = value; }


    public Sensor(int portNumber) {
        reading = 0;
        this.portNumber = portNumber;
    }


    // abstract methods


    public abstract short getSensorType();

    public abstract int getSensorTypeId();

    public abstract int getHighTextId();

    public abstract int getLowTextId();

    public abstract int getTypeTextId();

    public abstract int getBlueImageId();

    public abstract int getGreenImageId();

    public abstract int getOrangeImageIdMd();

    public abstract int getOrangeImageIdSm();

    public abstract int getGreyImageIdSm();

    public abstract int getWhiteImageIdSm();

}
