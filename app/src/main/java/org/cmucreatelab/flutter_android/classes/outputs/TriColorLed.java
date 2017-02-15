package org.cmucreatelab.flutter_android.classes.outputs;

import android.graphics.Color;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

/**
 * Created by Steve on 12/13/2016.
 */
public class TriColorLed implements FlutterOutput {

    public static final String LED_KEY = "led_key";
    private static final int NUMBER_OF_OUTPUTS = 3;

    private int portNumber;
    private Output[] outputs;

    // getters
    public RedLed getRedLed() { return (RedLed)outputs[0]; }
    public GreenLed getGreenLed() { return  (GreenLed)outputs[1]; }
    public BlueLed getBlueLed() { return (BlueLed)outputs[2]; }
    public int getPortNumber() { return this.portNumber; }


    public TriColorLed(int portNumber) {
        this.portNumber = portNumber;
        this.outputs = new Output[NUMBER_OF_OUTPUTS];
        outputs[0] = new RedLed(this.portNumber);
        outputs[1] = new GreenLed(this.portNumber);
        outputs[2] = new BlueLed(this.portNumber);
    }


    // NOTE: this doesn't handle every hex value from 00 to ff (since it is using values from 0 to 100)
    private static String getHexValue(int percent) {
        String value = Integer.toHexString((int)((float)percent/100.0 * 255));
        value = value.length() < 2 ? "0".concat(value) : value;
        return value;
    }


    public int getMaxSwatch() {
        String r,g,b,color;
        r = getHexValue(getRedLed().getSettings().getOutputMax());
        g = getHexValue(getGreenLed().getSettings().getOutputMax());
        b = getHexValue(getBlueLed().getSettings().getOutputMax());
        color = "#".concat(r.concat(g).concat(b));
        int c = Color.parseColor(color);
        if (Constants.COLOR_RES.containsKey(c)) {
            return Constants.COLOR_RES.get(c);
        }
        // default to black if color isn't in COLOR_RES
        return R.drawable.swatch_black;
    }


    public int getMinSwatch() {
        String r,g,b,color;
        r = getHexValue(getRedLed().getSettings().getOutputMin());
        g = getHexValue(getGreenLed().getSettings().getOutputMin());
        b = getHexValue(getBlueLed().getSettings().getOutputMin());
        color = "#".concat(r.concat(g).concat(b));
        int c = Color.parseColor(color);
        if (Constants.COLOR_RES.containsKey(c)) {
            return Constants.COLOR_RES.get(c);
        }
        // default to black if color isn't in COLOR_RES
        return R.drawable.swatch_black;
    }


    @Override
    public Output[] getOutputs() {
        return this.outputs;
    }

}
