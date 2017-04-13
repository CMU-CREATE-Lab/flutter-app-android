package org.cmucreatelab.flutter_android.classes.settings;

/**
 * Created by Steve on 10/25/2016.
 *
 * AdvancedSettings
 *
 * An abstract class representing the advanced settings options when creating links between sensor and output
 */
public class AdvancedSettings {

    private int inputMax;
    private int inputMin;
    private int speed;

    // getters
    public int getInputMax() { return inputMax; }
    public int getInputMin() { return inputMin; }
    public int getSpeed() { return speed; }
    // setters
    public void setInputMax(int max) { inputMax = max; }
    public void setInputMin(int min) { inputMin = min; }
    public void setSpeed(int zero) { speed = zero; }


    public AdvancedSettings() {
        inputMax = 100;
        inputMin = 0;
        speed = 0;
    }


    public static AdvancedSettings newInstance(AdvancedSettings oldInstance) {
        AdvancedSettings newInstance = new AdvancedSettings();
        newInstance.inputMax = oldInstance.inputMax;
        newInstance.inputMin = oldInstance.inputMin;
        newInstance.speed = oldInstance.speed;
        return newInstance;
    }

}
