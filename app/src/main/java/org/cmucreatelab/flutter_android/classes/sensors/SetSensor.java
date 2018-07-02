package org.cmucreatelab.flutter_android.classes.sensors;

import org.cmucreatelab.flutter_android.R;

/**
 * Created by Steve on 6/2/2017.
 *
 * SetSensor
 *
 * A class that denotes a sensor that can be set.
 *
 */

public class SetSensor extends NoSensor {

    private static final int typeTextId = R.string.set_sensor;

    private static final int whiteImageIdSm = R.drawable.questionmark_white_sm20;


    public SetSensor(int portNumber) {
        super(portNumber);
    }


    @Override
    public int getTypeTextId() {
        return typeTextId;
    }


    @Override
    public int getWhiteImageIdSm() {
        return whiteImageIdSm;
    }

}
