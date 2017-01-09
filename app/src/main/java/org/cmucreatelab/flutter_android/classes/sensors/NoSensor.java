package org.cmucreatelab.flutter_android.classes.sensors;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.helpers.static_classes.FlutterProtocol;

import java.io.Serializable;

/**
 * Created by Steve on 8/11/2016.
 *
 * NoSensor
 *
 * A class that represents no sensor.
 *
 */
public class NoSensor extends A_Sensor implements Sensor, Serializable {

    private static final short sensorType = FlutterProtocol.InputTypes.NOT_SET;
    private static final int sensorTypeId = R.string.no_sensor;
    private static final int highTextId = R.string.high;
    private static final int lowTextId = R.string.low;
    private static final int typeTextId = R.string.no_sensor;

    private static final int blueImageId = R.drawable.sensor_blue_no_sensor;
    private static final int greenImageId = R.drawable.sensor_green_no_sensor_l_g_68;
    private static final int orangeImageIdMd = R.drawable.sensor_orange_no_sensor;
    private static final int orangeImageIdSm = R.drawable.sensor_orange_no_sensor_s_m_22;
    private static final int greyImageIdSm = R.drawable.sensor_no_sensor_grey_s_m_20;
    private static final int whiteImageIdSm = R.drawable.sensor_no_sensor_s_m_20;


    public NoSensor(int portNumber) {
        super(portNumber);
    }


    @Override
    public short getSensorType() {
        return sensorType;
    }


    @Override
    public int getSensorTypeId() {
        return sensorTypeId;
    }


    @Override
    public int getHighTextId() {
        return highTextId;
    }


    @Override
    public int getLowTextId() {
        return lowTextId;
    }


    @Override
    public int getTypeTextId() {
        return typeTextId;
    }


    @Override
    public int getBlueImageId() {
        return blueImageId;
    }


    @Override
    public int getGreenImageId() {
        return greenImageId;
    }


    @Override
    public int getOrangeImageIdMd() {
        return orangeImageIdMd;
    }


    @Override
    public int getOrangeImageIdSm() {
        return orangeImageIdSm;
    }


    @Override
    public int getGreyImageIdSm() {
        return greyImageIdSm;
    }


    @Override
    public int getWhiteImageIdSm() {
        return whiteImageIdSm;
    }


    @Override
    public int getSensorReading() {
        return 0;
    }

}
