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

    private ArrayList<Output> mOutputs;
    private int reading;
    private int portNumber;


    public A_Sensor(int portNumber) {
        this.mOutputs = new ArrayList<>();
        reading = 0;
        this.portNumber = portNumber;
    }


    @Override
    public void addLink(Output output) {
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
    public int getPortNumber() {
        return portNumber;
    }


    @Override
    public ArrayList<Output> getLinks() {
        return mOutputs;
    }

    @Override
    public void setSensorReading(int value) {
        reading = value;
    }

}
