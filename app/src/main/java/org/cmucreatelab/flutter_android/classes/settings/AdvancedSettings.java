package org.cmucreatelab.flutter_android.classes.settings;

/**
 * Created by Steve on 10/25/2016.
 *
 * AdvancedSettings
 *
 * An abstract class representing the advanced settings options when creating links between sensor and output
 */
public class AdvancedSettings {

    private int voltageMax;
    private int voltageMin;
    private int speed;
    private int sensorCenterValue;
    private Settings settings;

    // getters
    public int getVoltageMax() { return voltageMax; }
    public int getVoltageMin() { return voltageMin; }
    public int getSpeed() { return speed; }
    public int getSensorCenterValue() { return sensorCenterValue; }
    // setters
    public void setVoltageMax(int max) { voltageMax = max; }
    public void setVoltageMin(int min) { voltageMin = min; }
    public void setSpeed(int zero) { speed = zero; }
    public void setSensorCenterValue(int sensorCenterValue) { this.sensorCenterValue = sensorCenterValue; }


    public int getPercentMin() {
        return settings.getSensor().voltageToPercent(voltageMin);
    }


    public int getPercentMax() {
        return settings.getSensor().voltageToPercent(voltageMax);
    }


    public void setPercentMin(int percent) {
        this.voltageMin = settings.getSensor().percentToVoltage(percent);
    }


    public void setPercentMax(int percent) {
        this.voltageMax = settings.getSensor().percentToVoltage(percent);
    }


    public void updateVoltageWithNewSensorType() {
        updateVoltageWithNewSensorType(voltageMin, voltageMax);
    }


    public void updateVoltageWithNewSensorType(int min, int max) {
        setVoltageMin(settings.getSensor().percentToVoltage(settings.getSensor().voltageToPercent(min)));
        setVoltageMax(settings.getSensor().percentToVoltage(settings.getSensor().voltageToPercent(max)));
    }


    public AdvancedSettings(Settings settings) {
        voltageMax = 100;
        voltageMin = 0;
        speed = 0;
        sensorCenterValue = 0;
        this.settings = settings;
    }


    public static AdvancedSettings newInstance(AdvancedSettings oldInstance, Settings newSettings) {
        AdvancedSettings newInstance = new AdvancedSettings(newSettings);
        newInstance.voltageMax = oldInstance.voltageMax;
        newInstance.voltageMin = oldInstance.voltageMin;
        newInstance.speed = oldInstance.speed;
        newInstance.sensorCenterValue = oldInstance.sensorCenterValue;
        return newInstance;
    }

}
