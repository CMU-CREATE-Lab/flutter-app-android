package org.cmucreatelab.flutter_android.classes.outputs;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.settings.Settings;

import java.io.Serializable;

/**
 * Created by Steve on 6/20/2016.
 *
 * Led
 *
 * A class that represents an Led.
 *
 */
public class Led extends A_Output implements Serializable, Output {


    public static final String LED_KEY = "led_key";
    public static final int imageId = R.mipmap.ic_launcher;

    private static final Output.Type outputType = Type.LED;
    private static final int MINIMUM = 0;
    private static final int MAXIMUM = 100;

    private Settings redSettings;
    private Settings greenSettings;
    private Settings blueSettings;


    public Led(int portNumber) {
        super(portNumber);
        redSettings = new Settings("r", MAXIMUM, MINIMUM);
        greenSettings = new Settings("g", MAXIMUM, MINIMUM);
        blueSettings = new Settings("b", MAXIMUM, MINIMUM);
        setSettings(redSettings);
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


    public Settings getRedSettings() { return redSettings; }
    public Settings getGreenSettings() { return greenSettings; }
    public Settings getBlueSettings() { return blueSettings; }


    public void setRedSettings(Settings settings) { this.redSettings = settings; }
    public void setGreenSettings (Settings settings) { this.greenSettings = settings; }
    public void setBlueSettings (Settings settings) { this.blueSettings = settings; }

}
