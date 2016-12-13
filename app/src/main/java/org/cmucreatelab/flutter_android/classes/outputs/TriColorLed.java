package org.cmucreatelab.flutter_android.classes.outputs;

import java.io.Serializable;

/**
 * Created by Steve on 12/13/2016.
 */
public class TriColorLed implements Serializable{

    public static final String LED_KEY = "led_key";

    private RedLed redLed;
    private GreenLed greenLed;
    private BlueLed blueLed;
    private int portNumber;


    public TriColorLed(int portNumber) {
        this.portNumber = portNumber;
        redLed = new RedLed(this.portNumber);
        greenLed = new GreenLed(this.portNumber);
        blueLed = new BlueLed(this.portNumber);
    }


    public RedLed getRedLed() { return this.redLed; }
    public GreenLed getGreenLed() { return  this.greenLed; }
    public BlueLed getBlueLed() { return this.blueLed; }
    public int getPortNumber() { return this.portNumber; }


    public void setRedLed(RedLed redLed) { this.redLed = redLed; }
    public void setGreenLed(GreenLed greenLed) { this.greenLed = greenLed; }
    public void setBlueLed(BlueLed blueLed) { this.blueLed = blueLed; }

}
