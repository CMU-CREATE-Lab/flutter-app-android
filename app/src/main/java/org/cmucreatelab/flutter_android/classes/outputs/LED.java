package org.cmucreatelab.flutter_android.classes.outputs;

/**
 * Created by Steve on 6/20/2016.
 *
 * LED
 *
 * A class that represents an LED.
 *
 */
public class LED implements Output {


    private static final Output.Type outputType = Type.LED;
    private static final int MINIMUM = 0;
    private static final int MAXIMUM = 180;

    private int red;
    private int green;
    private int blue;
    private int intensity;


    public LED(int red, int green, int blue, int intensity) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.setIntensity(intensity);
    }


    public void setIntensity(int intensity) {
        if (intensity >= MINIMUM && intensity <= MAXIMUM) {
            this.intensity = intensity;
        }
    }
    public int getIntensity() {
        return this.intensity;
    }


    @Override
    public Type getOutputType() {
        return outputType;
    }
}
