package org.cmucreatelab.flutter_android.classes;

import org.cmucreatelab.flutter_android.classes.relationships.NoRelationship;
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
public class Settings extends AdvancedSettings {


    private Sensor sensor;
    private Relationship relationship;

    // TODO - we may want to improve this so one settings class hold the settings for servos and the other for leds

    // used for servos
    private int outputMax;
    private int outputMin;

    // used for leds
    private int[] outputMaxColor;
    private int[] outputMinColor;


    public Settings() {
        sensor = new NoSensor(0);
        relationship = new NoRelationship();
        outputMax = 100;
        outputMin = 0;
        outputMaxColor = new int[3];
        outputMinColor = new int[3];
    }


    // getters
    public Sensor getSensor() {
        return sensor;
    }
    public Relationship getRelationship() {
        return relationship;
    }
    public int getOutputMax() {
        return outputMax;
    }
    public int getOutputMin() {
        return outputMin;
    }
    public int[] getOutputMaxColor() {
        return outputMaxColor;
    }
    public int[] getOutputMinColor() {
        return outputMinColor;
    }


    // setters
    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }
    public void setRelationship(Relationship relationship) {
        this.relationship = relationship;
    }
    public void setOutputMax(int max) {
        outputMax = max;
    }
    public void setOutputMin(int min) {
        outputMin = min;
    }
    public void setOutputMaxColor(int[] outputMaxColor) {
        this.outputMaxColor = outputMaxColor;
    }
    public void setOutputMinColor(int[] outputMinColor) {
        this.outputMinColor = outputMinColor;
    }

}
