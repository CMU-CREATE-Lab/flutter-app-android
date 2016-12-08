package org.cmucreatelab.flutter_android.classes.outputs;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.settings.Settings;

import java.io.Serializable;

/**
 * Created by Steve on 6/20/2016.
 *
 * Servo
 *
 * A class that represents a servo on the flutter.
 *
 */
public class Servo extends A_Output implements Serializable, Output {


    public static final String SERVO_KEY = "servo_key";

    private static final Output.Type outputType = Type.SERVO;
    private static final int MINIMUM = 0;
    private static final int MAXIMUM = 180;

    public static final int imageId = R.mipmap.ic_launcher;

    private Settings servoSettings;


    public Servo(int portNumber) {
        super(portNumber);
        servoSettings = new Settings("s", MAXIMUM, MINIMUM);
        setSettings(servoSettings);
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


    public Settings getServoSettings() {
        return this.servoSettings;
    }


    public void setServoSettings(Settings servoSettings) {
        this.servoSettings = servoSettings;
    }

}
