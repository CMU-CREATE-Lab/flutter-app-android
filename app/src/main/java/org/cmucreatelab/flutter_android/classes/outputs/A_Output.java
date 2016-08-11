package org.cmucreatelab.flutter_android.classes.outputs;

import org.cmucreatelab.flutter_android.classes.relationships.Proportional;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.sensors.NoSensor;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;

/**
 * Created by Steve on 8/11/2016.
 *
 * A_Output
 *
 * An abstract class that implements the linking between outputs, sensors and their relationship.
 *
 */
public abstract class A_Output implements Output{

    Sensor mSensor;
    Relationship mRelationship;


    public A_Output() {
        this.mSensor = new NoSensor();
        this.mRelationship = new Proportional();
    }


    public A_Output(Sensor sensor, Relationship relationship) {
        this.mSensor = sensor;
        this.mRelationship = relationship;
    }

    // getters
    public Sensor getSensor() { return this.mSensor; }
    public Relationship getRelationship() { return this.mRelationship; }


    // setters
    public void setSensor(Sensor sensor) { this.mSensor = sensor; }
    public void setRelationship(Relationship relationship) { this.mRelationship = relationship; }

}
