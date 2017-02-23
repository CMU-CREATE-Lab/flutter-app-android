package org.cmucreatelab.flutter_android.classes.sensors;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.helpers.static_classes.FlutterProtocol;

/**
 * Created by Steve on 8/11/2016.
 *
 * SoilMoistureSensor
 *
 * A class representing a soil moisture sensor.
 *
 */
public class SoilMoistureSensor extends Sensor {

    private static final short sensorType = FlutterProtocol.InputTypes.SOIL;
    private static final int sensorTypeId = R.string.soil_moisture;
    private static final int highTextId = R.string.wet;
    private static final int lowTextId = R.string.dry;
    private static final int typeTextId = R.string.soil_moisture;

    private static final int blueImageId = R.drawable.sensor_blue_soil_moisture_l_g_68;
    private static final int greenImageId = R.drawable.sensor_green_soil_moisture_l_g_68;
    private static final int orangeImageIdMd = R.drawable.sensor_orange_soil_moisture_m_d_40;
    private static final int orangeImageIdSm = R.drawable.sensor_orange_soil_moisture_s_m_22;
    private static final int greyImageIdSm = R.drawable.sensor_soil_moisture_grey_s_m_20;
    private static final int whiteImageIdSm = R.drawable.sensor_soil_moisture_s_m_20;


    public SoilMoistureSensor(int portNumber) {
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


    // custom Sensor ranges


    public static final int INPUT_MINIMUM = 0;
    public static final int INPUT_MAXIMUM = 90;
    public static final boolean INVERT_SENSOR = false;


    @Override
    public boolean isInverted() {
        return INVERT_SENSOR;
    }


    @Override
    public int voltageToPercent(int voltage) {
        double temp = (voltage - INPUT_MINIMUM) / (double)(INPUT_MAXIMUM-INPUT_MINIMUM);

        if (temp > 1.0) {
            temp = 1.0;
        } else if (temp < 0) {
            temp = 0;
        }

        return (int)(temp * 100);
    }


    @Override
    public int percentToVoltage(int percent) {
        double temp = ((double)percent / 100.0);

        if (temp > 1.0) {
            temp = 1.0;
        } else if (temp < 0) {
            temp = 0;
        }

        return (int)Math.round(temp*(INPUT_MAXIMUM-INPUT_MINIMUM))+INPUT_MINIMUM;
    }

}
