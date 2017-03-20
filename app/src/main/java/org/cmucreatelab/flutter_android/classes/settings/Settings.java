package org.cmucreatelab.flutter_android.classes.settings;

import org.cmucreatelab.flutter_android.classes.flutters.Flutter;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;

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
