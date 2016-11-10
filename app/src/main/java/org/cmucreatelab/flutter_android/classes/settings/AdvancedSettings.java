package org.cmucreatelab.flutter_android.classes.settings;

import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;

/**
 * Created by Steve on 10/25/2016.
 *
 * AdvancedSettings
 *
 * An abstract class representing the advanced settings options when creating links between sensor and output
 */
public abstract class AdvancedSettings {


    private int inputMax;
    private int inputMin;
    private int zeroValue;

    protected Sensor sensor;
    protected Relationship relationship;


    protected String intToHex(int val) {
        String result = "";

        return result;
    }


    public AdvancedSettings() {
        inputMax = 100;
        inputMin = 0;
        zeroValue = 0;
    }


    // getters
    public int getInputMax() {
        return inputMax;
    }
    public int getInputMin() {
        return inputMin;
    }
    public int getZeroValue() {
        return zeroValue;
    }
    public Relationship getRelationship() {
        return relationship;
    }
    public Sensor getSensor() {
        return sensor;
    }

    // setters
    public void setInputMax(int max) {
        inputMax = max;
    }
    public void setInputMin(int min) {
        inputMin = min;
    }
    public void setZeroValue(int zero) {
        zeroValue = zero;
    }
    public void setRelationship(Relationship relationship) {
        this.relationship = relationship;
    }
    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }
}
