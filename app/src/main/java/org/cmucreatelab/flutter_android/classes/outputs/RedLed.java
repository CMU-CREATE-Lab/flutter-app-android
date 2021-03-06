package org.cmucreatelab.flutter_android.classes.outputs;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.flutters.Flutter;

/**
 * Created by Steve on 12/13/2016.
 *
 * RedLed
 *
 * A class that represents a red LED on the flutter.
 */
public class RedLed extends Output {

    private static final int MINIMUM = 0;
    private static final int MAXIMUM = 100;
    private static final String TYPE = "r";
    private static final Output.Type outputType = Type.RED_LED;
    private static final int imageId = R.mipmap.ic_launcher;


    RedLed(int portNumber, Flutter flutter) {
        super(MAXIMUM, MINIMUM, portNumber, flutter);
    }


    @Override
    public String getProtocolString() {
        return TYPE+getPortNumber();
    }


    @Override
    public Type getOutputType() {
        return outputType;
    }


    @Override
    public int getOutputImageId() {
        return imageId;
    }


    @Override
    public int getMax() {
        return MAXIMUM;
    }


    @Override
    public int getMin() {
        return MINIMUM;
    }

}
