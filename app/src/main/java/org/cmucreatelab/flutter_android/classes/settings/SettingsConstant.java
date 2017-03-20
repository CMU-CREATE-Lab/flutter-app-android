package org.cmucreatelab.flutter_android.classes.settings;

import org.cmucreatelab.flutter_android.classes.flutters.Flutter;
import org.cmucreatelab.flutter_android.classes.relationships.Constant;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.sensors.NoSensor;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;

/**
 * Created by mike on 3/17/17.
 *
 * Represents generic settings with a Constant relationship
 */
public class SettingsConstant extends Settings {

    // getters
    public int getValue() { return outputMax; }
    // setters
    public void setValue(int value) { this.outputMax = value; }


    protected SettingsConstant(Settings settings) {
        super(settings);
    }


    protected SettingsConstant(int min, int max, Flutter flutter) {
        super(min, max, flutter);
    }


    @Override
    public boolean hasAdvancedSettings() {
        return false;
    }


    @Override
    public Relationship getRelationship() {
        return Constant.getInstance();
    }


    @Override
    public Sensor getSensor() {
        return new NoSensor(0);
    }


    @Override
    public boolean isSettable() {
        return true;
    }


    public static SettingsConstant newInstance(Settings oldInstance) {
        SettingsConstant newInstance = new SettingsConstant(oldInstance.outputMin, oldInstance.outputMax, oldInstance.flutter);
        newInstance.sensorPortNumber = oldInstance.sensorPortNumber;
        newInstance.advancedSettings = AdvancedSettings.newInstance(oldInstance.advancedSettings);
        return newInstance;
    }


    public static SettingsConstant newInstance(Flutter flutter) {
        SettingsConstant newInstance = new SettingsConstant(0,100,flutter);
        // TODO create new instance for Adv Settings?
        return newInstance;
    }

}
