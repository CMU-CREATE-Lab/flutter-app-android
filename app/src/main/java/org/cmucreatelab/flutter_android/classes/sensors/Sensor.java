package org.cmucreatelab.flutter_android.classes.sensors;

import org.cmucreatelab.flutter_android.classes.Link;
import org.cmucreatelab.flutter_android.classes.outputs.Output;

/**
 * Created by Steve on 6/20/2016.
 *
 * Sensor
 *
 * Interface for the anything is a sensor. There are various types of sensors defined in the enum 'Type'
 *
 */
public interface Sensor {

    // I am unsure of the use of an Analog or Unknown Sensor.
    // Is this for error checking? Should the user always have a sensor that is defined?
    enum Type {
        LIGHT,
        SOIL_MOISTURE,
        DISTANCE,
        SOUND,
        WIND_SPEED,
        HUMIDITY,
        TEMPERATURE,
        BAROMETRIC_PRESSURE,
        ANALOG_OR_UNKNOWN,
        NO_SENSOR
    }

    void addLink(Link link);
    void clearLink(Output output);
    void clearLinks();

    Type getSensorType();

    int getSensorReading();

}
