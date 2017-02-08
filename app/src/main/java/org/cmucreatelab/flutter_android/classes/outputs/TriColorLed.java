package org.cmucreatelab.flutter_android.classes.outputs;

import java.io.Serializable;

/**
 * Created by Steve on 12/13/2016.
 */
public class TriColorLed implements FlutterOutput, Serializable {

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


    @Override
    public Output[] getOutputs() {
        return this.outputs;
    }

}
