package org.cmucreatelab.flutter_android.classes.settings;

import org.cmucreatelab.flutter_android.classes.relationships.Proportional;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.sensors.NoSensor;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;

/**
 * Created by Steve on 10/25/2016.
 *
 * Settings
 *
 * A class that extends off of AdvancedSettings and represents the generic settings made when creating links between sensor and output
 */
public class Settings {

    private int outputMax;
    private int outputMin;
    private String type;
    private Sensor sensor;
    private Relationship relationship;
    private AdvancedSettings advancedSettings;

    // getters
    public int getOutputMax() { return outputMax; }
    public int getOutputMin() { return outputMin; }
    public String getType() { return type; }
    public Relationship getRelationship() { return relationship; }
    public Sensor getSensor() { return sensor; }
    public AdvancedSettings getAdvancedSettings() { return advancedSettings; }
    // setters
    public void setOutputMax(int max) { outputMax = max; }
    public void setOutputMin(int min) { outputMin = min; }
    public void setType(String type) { this.type = type; }
    public void setRelationship(Relationship relationship) { this.relationship = relationship; }
    public void setSensor(Sensor sensor) { this.sensor = sensor; }
    public void setAdvancedSettings (AdvancedSettings advancedSettings) { this.advancedSettings = advancedSettings; }


    // The type will be used by the message constructor to create messages for the flutter
    public Settings(String type, int max, int min) {
        advancedSettings = new AdvancedSettings();
        sensor = new NoSensor(0);
        relationship = Proportional.getInstance();
        outputMax = max;
        outputMin = min;
        this.type = type;
    }


    /**
     * When opening a dialog on RobotsActivity, we want to create a new instance of its respective
     * Settings. That way we can display changes the user makes and, if the settings are not saved,
     * then the real Settings will not be overwritten.
     *
     * @param oldInstance The object that is to be copied.
     * @return A new instance of Settings.
     */
    public static Settings newInstance(Settings oldInstance) {
        Settings newInstance = new Settings(oldInstance.type, oldInstance.outputMax, oldInstance.outputMin);
        newInstance.sensor = oldInstance.sensor;
        newInstance.relationship = oldInstance.relationship;
        newInstance.advancedSettings = AdvancedSettings.newInstance(oldInstance.advancedSettings);
        return newInstance;
    }


    public void invertOutputs() {
        int temp;
        temp = getOutputMax();
        setOutputMax(getOutputMin());
        setOutputMin(temp);
    }

}
