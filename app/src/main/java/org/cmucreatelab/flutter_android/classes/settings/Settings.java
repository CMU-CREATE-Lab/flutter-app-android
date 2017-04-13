package org.cmucreatelab.flutter_android.classes.settings;

import android.util.Log;

import org.cmucreatelab.flutter_android.classes.flutters.Flutter;
import org.cmucreatelab.flutter_android.classes.relationships.Amplitude;
import org.cmucreatelab.flutter_android.classes.relationships.Constant;
import org.cmucreatelab.flutter_android.classes.relationships.Proportional;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

/**
 * Created by mike on 3/16/17.
 *
 * Abstract class for Settings; number of fields depends on the Relationship type.
 */
public abstract class Settings {

    protected int outputMin;
    protected int outputMax;
    protected int sensorPortNumber;
    protected AdvancedSettings advancedSettings;
    protected Flutter flutter;


    protected Settings(Settings settings) {
        this.outputMin = settings.outputMin;
        this.outputMax = settings.outputMax;
        this.sensorPortNumber = settings.sensorPortNumber;
        this.advancedSettings = settings.advancedSettings;
        this.flutter = settings.flutter;
    }


    protected Settings(int min, int max, Flutter flutter) {
        outputMin = min;
        outputMax = max;
        sensorPortNumber = 0;
        advancedSettings = new AdvancedSettings();
        this.flutter = flutter;
    }


    /**
     * When opening a dialog on RobotsActivity, we want to create a new instance of its respective
     * SettingsAmplitude. That way we can display changes the user makes and, if the settings are not saved,
     * then the real Settings will not be overwritten.
     *
     * @param oldInstance The object that is to be copied.
     * @return A new instance of Settings.
     */
    public static Settings newInstance(Settings oldInstance) {
        return newInstance(oldInstance, oldInstance.getRelationship());
    }


    /**
     * When opening a dialog on RobotsActivity, we want to create a new instance of its respective
     * SettingsAmplitude. That way we can display changes the user makes and, if the settings are not saved,
     * then the real Settings will not be overwritten.
     *
     * @param oldInstance The object that is to be copied.
     * @param relationship The type of relationship that we want the new instance to be
     * @return A new instance of Settings.
     */
    public static Settings newInstance(Settings oldInstance, Relationship relationship) {
        Settings result = null;

        if (relationship.getClass() == Proportional.class) {
            result = SettingsProportional.newInstance(oldInstance);
        } else if (relationship.getClass() == Constant.class) {
            result = SettingsConstant.newInstance(oldInstance);
        } else if (relationship.getClass() == Amplitude.class) {
            result = SettingsAmplitude.newInstance(oldInstance);
        } else {
            Log.e(Constants.LOG_TAG, "Settings.newInstance: Cannot determine Relationship subclass");
        }

        return result;
    }


    public abstract boolean hasAdvancedSettings();

    public boolean hasSpeed() { return false; }

    public boolean hasSensorCenterValue() { return false; }

    public abstract Relationship getRelationship();

    public abstract Sensor getSensor();

    /**
     * Determines if the Settings instance has all necessary parameters to be linked
     *
     * @return true if the instance has all necessary parameters, false otherwise.
     */
    public abstract boolean isSettable();

}
