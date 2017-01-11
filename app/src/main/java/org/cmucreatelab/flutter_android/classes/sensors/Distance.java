package org.cmucreatelab.flutter_android.classes.sensors;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.helpers.static_classes.FlutterProtocol;

import java.io.Serializable;

/**
 * Created by Steve on 8/11/2016.
 *
 * Distance
 *
 * A class representing a distance sensor.
 *
 */
public class Distance extends A_Sensor implements Sensor, Serializable {

    private static final short sensorType = FlutterProtocol.InputTypes.DISTANCE;
    private static final int sensorTypeId = R.string.distance;
    private static final int highTextId = R.string.far;
    private static final int lowTextId = R.string.near;
    private static final int typeTextId = R.string.distance;

    private static final int blueImageId = R.drawable.sensor_blue_distance_l_g_68;
    private static final int greenImageId = R.drawable.sensor_green_distance_l_g_68;
    private static final int orangeImageIdMd = R.drawable.sensor_orange_distance_m_d_40;
    private static final int orangeImageIdSm = R.drawable.sensor_orange_distance_s_m_22;
    private static final int greyImageIdSm = R.drawable.sensor_distance_grey_s_m_20;
    private static final int whiteImageIdSm = R.drawable.sensor_distance_s_m_20;

    public static final int INPUT_MINIMUM = 0;
    public static final int INPUT_MAXIMUM = 75;


    public Distance(int portNumber) {
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


    // TODO @tasota hacked for custom distance input value ranges
    @Override
    public int getSensorReading() {
        int value = super.getSensorReading();
        double temp = (value - INPUT_MINIMUM) / (double)INPUT_MAXIMUM;
        if (temp > 1.0) {
            temp = 1.0;
        } else if (temp < 0) {
            temp = 0;
        }
        temp = 1.0 - temp;
        return (int)(temp * 100);
    }

}
