package org.cmucreatelab.flutter_android.classes.outputs;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.flutters.Flutter;
import org.cmucreatelab.flutter_android.classes.settings.SettingsConstant;
import org.cmucreatelab.flutter_android.classes.settings.SettingsProportional;

/**
 * Created by Steve on 6/20/2016.
 *
 * Servo
 *
 * A class that represents a servo on the flutter.
 *
 */
public class Servo extends Output implements FlutterOutput {

    public static final String SERVO_KEY = "servo_key";

    private static final Output.Type outputType = Type.SERVO;
    private static final int MINIMUM = 0;
    private static final int MAXIMUM = 180;
    private static final String TYPE = "s";
    private static final int imageId = R.mipmap.ic_launcher;

    private Flutter flutter;


    public Servo(int portNumber, Flutter flutter) {
        super(MAXIMUM, MINIMUM, portNumber, flutter);
        this.flutter = flutter;
    }


    public static Servo newInstance(Servo oldInstance) {
        Servo newInstance = new Servo(oldInstance.getPortNumber(),oldInstance.flutter);

        // settings
        if (oldInstance.getSettings().getClass() == SettingsConstant.class) {
            newInstance.setSettings(SettingsConstant.newInstance(oldInstance.getSettings()));
        } else {
            newInstance.setSettings(SettingsProportional.newInstance(oldInstance.getSettings()));
        }

        return newInstance;
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


    @Override
    public Output[] getOutputs() {
        Output[] outputs = {this};
        return outputs;
    }

}
