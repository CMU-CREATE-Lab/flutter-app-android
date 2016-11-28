package org.cmucreatelab.flutter_android.classes.settings;

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


    private int outputMax;
    private int outputMin;
    private String type;


    public Settings(String type) {
        sensor = new NoSensor(0);
        relationship = new NoRelationship();
        outputMax = 100;
        outputMin = 0;
        this.type = type;
    }


    // getters

    public int getOutputMax() {
        return outputMax;
    }
    public int getOutputMin() {
        return outputMin;
    }
    public String getType() { return type; }


    // setters

    public void setOutputMax(int max) {
        outputMax = max;
    }
    public void setOutputMin(int min) {
        outputMin = min;
    }
    public void setType(String type) { this.type = type; }

}
