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
    private int zeroValue;

    // getters
    public int getInputMax() { return inputMax; }
    public int getInputMin() { return inputMin; }
    // TODO @tasota this should be speed; zero value is not used anywhere else right now
    public int getZeroValue() { return zeroValue; }
    // setters
    public void setInputMax(int max) { inputMax = max; }
    public void setInputMin(int min) { inputMin = min; }
    public void setZeroValue(int zero) { zeroValue = zero; }


    public AdvancedSettings() {
        inputMax = 100;
        inputMin = 0;
        zeroValue = 0;
    }


    public static AdvancedSettings newInstance(AdvancedSettings oldInstance) {
        AdvancedSettings newInstance = new AdvancedSettings();
        newInstance.inputMax = oldInstance.inputMax;
        newInstance.inputMin = oldInstance.inputMin;
        newInstance.zeroValue = oldInstance.zeroValue;
        return newInstance;
    }

}
