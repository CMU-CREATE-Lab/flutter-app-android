package org.cmucreatelab.flutter_android.classes.settings;

import org.cmucreatelab.flutter_android.classes.flutters.Flutter;
import org.cmucreatelab.flutter_android.classes.relationships.Change;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.sensors.NoSensor;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;

/**
 * Created by mike on 4/13/17.
 *
 * SettingsChange
 *
 * A class that represents the generic settings made with Change relationship.
 */
public class SettingsChange extends Settings {

    // getters
    public int getOutputMax() { return outputMax; }
    public int getOutputMin() { return outputMin; }
    public int getSensorPortNumber() { return sensorPortNumber; }
    public AdvancedSettings getAdvancedSettings() { return advancedSettings; }
    // setters
    public void setOutputMax(int max) { outputMax = max; }
    public void setOutputMin(int min) { outputMin = min; }
    public void setAdvancedSettings (AdvancedSettings advancedSettings) { this.advancedSettings = advancedSettings; }


    protected SettingsChange(Settings settings) {
        super(settings);
    }


    protected SettingsChange(int min, int max, Flutter flutter) {
        super(min, max, flutter);
    }


    public void setSensorPortNumber(int portNumber) {
        Sensor oldSensor, newSensor;
        oldSensor = getSensor();
        this.sensorPortNumber = portNumber;
        newSensor = getSensor();
        updateWithNewSensorType(oldSensor, newSensor);
    }


    @Override
    public boolean hasAdvancedSettings() {
        return true;
    }


    @Override
    public Relationship getRelationship() {
        return Change.getInstance();
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
     * SettingsChange. That way we can display changes the user makes and, if the settings are not saved,
     * then the real SettingsProportional will not be overwritten.
     *
     * @param oldInstance The object that is to be copied.
     * @return A new instance of SettingsChange.
     */
    public static SettingsChange newInstance(Settings oldInstance) {
        SettingsChange newInstance = new SettingsChange(oldInstance.outputMin, oldInstance.outputMax, oldInstance.flutter);
        newInstance.sensorPortNumber = oldInstance.sensorPortNumber;
        newInstance.advancedSettings = AdvancedSettings.newInstance(oldInstance.advancedSettings, newInstance);
        return newInstance;
    }


    public static SettingsChange newInstance(Flutter flutter) {
        SettingsChange newInstance = new SettingsChange(0,100,flutter);
        // TODO create new instance for Adv Settings?
        return newInstance;
    }

}
