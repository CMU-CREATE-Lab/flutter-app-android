package org.cmucreatelab.flutter_android.classes.sensors;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.helpers.static_classes.FlutterProtocol;

/**
 * Created by Steve on 8/11/2016.
 *
 * TemperatureSensor
 *
 * A class representing a temperature sensor.
 *
 */
public class TemperatureSensor extends Sensor {

    private static final short sensorType = FlutterProtocol.InputTypes.TEMPERATURE;
    private static final int sensorTypeId = R.string.temperature;
    private static final int highTextId = R.string.hot;
    private static final int lowTextId = R.string.cold;
    private static final int typeTextId = R.string.temperature;

    private static final int blueImageId = R.drawable.sensor_blue_temperature_l_g_68;
    private static final int greenImageId = R.drawable.sensor_green_temperature_l_g_68;
    private static final int orangeImageIdMd = R.drawable.sensor_orange_temperature_m_d_40;
    private static final int orangeImageIdSm = R.drawable.sensor_orange_temperature_s_m_22;
    private static final int greyImageIdSm = R.drawable.sensor_temperature_grey_s_m_20;
    private static final int whiteImageIdSm = R.drawable.sensor_temperature_s_m_20;


    public TemperatureSensor(int portNumber) {
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

}
