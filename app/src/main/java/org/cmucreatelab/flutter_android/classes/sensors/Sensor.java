package org.cmucreatelab.flutter_android.classes.sensors;

/**
 * Created by Steve on 6/20/2016.
 *
 * Sensor
 *
 * Interface for the anything is a sensor. There are various types of sensors defined in the enum 'Type'
 *
 */
public interface Sensor {

    String SENSOR_KEY = "sensor_key";
    String SENSOR_PORT_KEY = "sensor_text_key";

    short getSensorType();
    int getSensorTypeId();
    int getHighTextId();
    int getLowTextId();
    int getTypeTextId();
    int getBlueImageId();
    int getGreenImageId();
    int getOrangeImageIdMd();
    int getOrangeImageIdSm();
    int getGreyImageIdSm();
    int getWhiteImageIdSm();
    int getSensorReading();
    int getPortNumber();
    void setSensorReading(int value);

}
