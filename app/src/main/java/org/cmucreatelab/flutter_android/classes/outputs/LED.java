package org.cmucreatelab.flutter_android.classes.outputs;

import org.cmucreatelab.flutter_android.R;

import java.io.Serializable;

/**
 * Created by Steve on 6/20/2016.
 *
 * LED
 *
 * A class that represents an LED.
 *
 */
public class LED extends A_Output implements Serializable, Output {


    public static final String LED_KEY = "led_key";


    private static final Output.Type outputType = Type.LED;
    private static final int MINIMUM = 0;
    private static final int MAXIMUM = 100;

    public static final int imageId = R.mipmap.ic_launcher;

    private int red;
    private int green;
    private int blue;
    private int intensity;


    public LED(int portNumber) {
        super(portNumber);
        this.red = 0;
        this.green = 0;
        this.blue = 0;
        this.intensity = MINIMUM;
    }


    public LED(int red, int green, int blue, int intensity, int portNumber) {
        super(portNumber);
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.setIntensity(intensity);
    }


    public void setRed(int red) {
        this.red = red;
    }
    public void setGreen(int green) {
        this.green = green;
    }
    public void setBlue(int blue) {
        this.blue = blue;
    }
    public void setIntensity(int intensity) {
        if (intensity >= MINIMUM && intensity <= MAXIMUM) {
            this.intensity = intensity;
        } else {
            this.intensity = 0;
        }
    }
    public int getRed() {
        return this.red;
    }
    public int getGreen() {
        return this.green;
    }
    public int getBlue() {
        return this.blue;
    }
    public int getIntensity() {
        return this.intensity;
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
