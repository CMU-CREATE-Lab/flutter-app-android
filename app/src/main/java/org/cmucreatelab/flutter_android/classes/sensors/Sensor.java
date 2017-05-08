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
    public int getPortNumber() { return portNumber; }
    // setters
    public void setSensorReading(int value) { reading = value; }


    public Sensor(int portNumber) {
        reading = 0;
        this.portNumber = portNumber;
    }


    /**
     * Readings from a sensor are based on their voltage, but since we want all sensor readings
     * to be between 0 and 100, some sensors have to redefine their min and max voltage.
     * @return A sensor "reading" between 0 and 100.
     */
    public int getSensorReading() {
        if (isInverted()) {
            return 100 - voltageToPercent(reading);
        }
        return voltageToPercent(reading);
    }


    /**
     * Indicates if the Sensor has a custom or inverted voltage range. Generally, if any of the
     * methods {@link #isInverted()} {@link #voltageToPercent(int)} {@link #percentToVoltage(int)}
     * are overridden, this method likely should be overridden as well and return true.
     * @return True if the range of values for the sensor are modified; false otherwise.
     */
    public boolean hasCustomSensorRange() { return false; }


    /**
     * Indicates whether the voltage from the Sensor is opposite of what is expected. For example,
     * distance sensor voltage is 0 when "far" but conceptually distant objects should have a
     * higher value.
     * @return True if voltage is inverted; false otherwise.
     */
    public boolean isInverted() {
        return false;
    }


    /**
     * Converts voltage into a percent based on the Sensor's "true" voltage range.
     * This is 0-100 by default for Sensors.
     * @param voltage the physical voltage reported from the protocol.
     * @return A percentage representing the percentage of voltage range (0-100)
     */
    public int voltageToPercent(int voltage) {
        return voltage;
    }


    /**
     * Converts voltage into a percent based on the Sensor's "true" voltage range.
     * This is 0-100 by default for Sensors.
     * @param percent A percentage representing the percentage of voltage range (0-100)
     * @return The voltage associated with percent for Sensor.
     */
    public int percentToVoltage(int percent) {
        return percent;
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
