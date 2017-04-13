package org.cmucreatelab.flutter_android.classes.settings;

import org.cmucreatelab.flutter_android.classes.flutters.Flutter;
import org.cmucreatelab.flutter_android.classes.relationships.Frequency;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.sensors.NoSensor;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;

/**
 * Created by mike on 4/13/17.
 *
 * SettingsFrequency
 *
 * A class that represents the generic settings made with Frequency relationship
 */
public class SettingsFrequency extends Settings {

    // getters
    public int getOutputMax() { return outputMax; }
    public int getOutputMin() { return outputMin; }
    public int getSensorPortNumber() { return sensorPortNumber; }
    public AdvancedSettings getAdvancedSettings() { return advancedSettings; }
    // setters
    public void setOutputMax(int max) { outputMax = max; }
    public void setOutputMin(int min) { outputMin = min; }
    public void setSensorPortNumber(int portNumber) { this.sensorPortNumber = portNumber; }
    public void setAdvancedSettings (AdvancedSettings advancedSettings) { this.advancedSettings = advancedSettings; }


    protected SettingsFrequency(Settings settings) {
        super(settings);
    }


    protected SettingsFrequency(int min, int max, Flutter flutter) {
        super(min, max, flutter);
    }


    @Override
    public boolean hasAdvancedSettings() {
        return true;
    }


    @Override
    public Relationship getRelationship() {
        return Frequency.getInstance();
    }


    @Override
    public Sensor getSensor() {
        if (getSensorPortNumber() == 0)
            return new NoSensor(0);
        return flutter.getSensors()[getSensorPortNumber()-1];
    }


    @Override
    public boolean isSettable() {
        if (getSensor().getClass() == NoSensor.class) {
            return false;
        }
        return true;
    }


    /**
     * When opening a dialog on RobotsActivity, we want to create a new instance of its respective
     * SettingsFrequency. That way we can display changes the user makes and, if the settings are not saved,
     * then the real SettingsProportional will not be overwritten.
     *
     * @param oldInstance The object that is to be copied.
     * @return A new instance of SettingsFrequency.
     */
    public static SettingsFrequency newInstance(Settings oldInstance) {
        SettingsFrequency newInstance = new SettingsFrequency(oldInstance.outputMin, oldInstance.outputMax, oldInstance.flutter);
        newInstance.sensorPortNumber = oldInstance.sensorPortNumber;
        newInstance.advancedSettings = AdvancedSettings.newInstance(oldInstance.advancedSettings);
        return newInstance;
    }


    public static SettingsFrequency newInstance(Flutter flutter) {
        SettingsFrequency newInstance = new SettingsFrequency(0,100,flutter);
        // TODO create new instance for Adv Settings?
        return newInstance;
    }

}
