package org.cmucreatelab.flutter_android.classes.sensors;

import org.cmucreatelab.flutter_android.R;

import java.io.Serializable;

/**
 * Created by Steve on 8/11/2016.
 *
 * AnalogOrUnknown
 *
 * A class representing an analog or unknown sensor.
 *
 */
public class AnalogOrUnknown extends A_Sensor implements Sensor, Serializable {

    private static final Sensor.Type sensorType = Type.ANALOG_OR_UNKNOWN;
    private static final int sensorTypeId = R.string.analog_or_unknown;
    private static final int highTextId = R.string.high;
    private static final int lowTextId = R.string.low;
    private static final int typeTextId = R.string.analog_or_unknown;

    private static final int blueImageId = R.drawable.sensor_blue_analog_l_g_68;
    private static final int greenImageId = R.drawable.sensor_green_analog_l_g_68;
    private static final int orangeImageIdMd = R.drawable.sensor_orange_analog_m_d_40;
    private static final int orangeImageIdSm = R.drawable.sensor_orange_analog_copy;
    private static final int greyImageIdSm = R.drawable.sensor_analog_grey_s_m_20;


    public AnalogOrUnknown(int portNumber) {
        super(portNumber);
    }


    @Override
    public Type getSensorType() {
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

}
