package org.cmucreatelab.flutter_android.classes;

import org.cmucreatelab.flutter_android.classes.relationships.NoRelationship;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.sensors.NoSensor;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;

/**
 * Created by Steve on 10/25/2016.
 */
public class Settings extends AdvancedSettings {


    private Sensor sensor;
    private Relationship relationship;
    private int outputMax;
    private int outputMin;


    public Settings() {
        sensor = new NoSensor(0);
        relationship = new NoRelationship();
        outputMax = 100;
        outputMin = 0;
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

}
