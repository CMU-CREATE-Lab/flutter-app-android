package org.cmucreatelab.flutter_android.classes.outputs;

import org.cmucreatelab.flutter_android.R;

import java.io.Serializable;

/**
 * Created by Steve on 12/13/2016.
 */
public class GreenLed extends A_Output implements Serializable {

    private static final int MINIMUM = 0;
    private static final int MAXIMUM = 100;
    private static final String TYPE = "g";
    private static final Output.Type outputType = Type.GREEN_LED;

    public static final String LED_KEY = "green_led_key";
    public static final int imageId = R.mipmap.ic_launcher;

    public GreenLed(int portNumber) {
        super(TYPE, MAXIMUM, MINIMUM, portNumber);
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
