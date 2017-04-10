package org.cmucreatelab.flutter_android.classes.settings;

import android.util.Log;

import org.cmucreatelab.flutter_android.classes.flutters.Flutter;
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
        Settings result = null;
        if (oldInstance.getClass() == SettingsProportional.class) {
            result = SettingsProportional.newInstance(oldInstance);
        } else if (oldInstance.getClass() == SettingsConstant.class) {
            result = SettingsConstant.newInstance(oldInstance);
        } else if (oldInstance.getClass() == SettingsAmplitude.class) {
            result = SettingsAmplitude.newInstance(oldInstance);
        } else {
            Log.e(Constants.LOG_TAG, "Settings.newInstance: Cannot determine Settings subclass");
        }
        return result;
    }


    public abstract boolean hasAdvancedSettings();

    public abstract Relationship getRelationship();

    public abstract Sensor getSensor();

    /**
     * Determines if the Settings instance has all necessary parameters to be linked
     *
     * @return true if the instance has all necessary parameters, false otherwise.
     */
    public abstract boolean isSettable();

}
