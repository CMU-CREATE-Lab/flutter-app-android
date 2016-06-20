package org.cmucreatelab.flutter_android.classes;

/**
 * Created by Steve on 6/20/2016.
 */
public class LED {


    public static enum LED_TYPE{ RED, GREEN, BLUE };
    private static final int MINIMUM = 0;
    private static final int MAXIMUM = 180;

    private LED_TYPE type;
    private int intensity;


    public LED(LED_TYPE type, int intensity) {
        this.type = type;
        this.setIntensity(intensity);
    }


    public void setType(LED_TYPE type) {
        this.type = type;
    }
    public LED_TYPE getType() {
        return this.type;
    }
    public void setIntensity(int intensity) {
        if (intensity >= MINIMUM && intensity <= MAXIMUM) {
            this.intensity = intensity;
        }
    }
    public int getIntensity() {
        return this.intensity;
    }
}
