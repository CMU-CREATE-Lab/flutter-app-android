package org.cmucreatelab.flutter_android.classes.sensors;

import org.cmucreatelab.flutter_android.classes.outputs.Output;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;

/**
 * Created by Steve on 6/20/2016.
 *
 * Sensor
 *
 * Interface for the anything is a sensor. There are various types of sensors defined in the enum 'Type'
 *
 */
public interface Sensor {

    public static final String SENSOR_KEY = "sensor_key";

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

    void addLink(Output output);
    void removeLink(Output output);
    void clearLinks();

    Type getSensorType();
    int getSensorTypeId();
    int getHighTextId();
    int getLowTextId();
    int getBlueImageId();
    int getGreenImageId();
    int getOrangeImageIdMd();
    int getOrangeImageIdSm();
    int getSensorReading();
    int getPortNumber();
    void setSensorReading(int value);

}
