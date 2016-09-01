package org.cmucreatelab.flutter_android.classes.sensors;

import org.cmucreatelab.flutter_android.classes.outputs.Output;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;

import java.util.ArrayList;

/**
 * Created by Steve on 8/11/2016.
 *
 * A_Sensor
 *
 * An abstract class that implements the process of linking a sensor with an output.
 *
 */
public abstract class A_Sensor implements Sensor {

    protected ArrayList<Output> mOutputs;
    private int reading;


    public A_Sensor() {
        this.mOutputs = new ArrayList<>();
        reading = 0;
    }


    @Override
    public void addLink(Output output, Relationship relationship) {
        output.setSensor(this);
        output.setRelationship(relationship);
        mOutputs.add(output);
    }


    @Override
    public void removeLink(Output output) {
        mOutputs.remove(output);
    }


    @Override
    public void clearLinks() {
        mOutputs.clear();
    }


    @Override
    public int getSensorReading() {
        return reading;
    }
    @Override
    public void setSensorReading(int value) {
        reading = value;
    }

}
