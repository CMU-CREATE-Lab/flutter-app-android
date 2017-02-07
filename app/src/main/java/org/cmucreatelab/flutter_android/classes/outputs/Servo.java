package org.cmucreatelab.flutter_android.classes.outputs;

import org.cmucreatelab.flutter_android.R;

import java.io.Serializable;

/**
 * Created by Steve on 6/20/2016.
 *
 * Servo
 *
 * A class that represents a servo on the flutter.
 *
 */
public class Servo extends Output implements Serializable {

    public static final String SERVO_KEY = "servo_key";

    private static final Output.Type outputType = Type.SERVO;
    private static final int MINIMUM = 0;
    private static final int MAXIMUM = 180;
    private static final String TYPE = "s";
    private static final int imageId = R.mipmap.ic_launcher;


    public Servo(int portNumber) {
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
