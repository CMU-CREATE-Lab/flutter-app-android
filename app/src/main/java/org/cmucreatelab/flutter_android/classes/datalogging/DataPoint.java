package org.cmucreatelab.flutter_android.classes.datalogging;

/**
 * Created by Steve on 8/15/2016.
 *
 * DataPoint
 *
 * A class that represents a single data point in a data set.
 *
 */
public class DataPoint {

    // temporary
    private int value;


    public DataPoint(int value) {
        this.value = value;
    }


    // getters

    public int getValue() { return this.value; }

    // setters

    public void setValue(int value) { this.value = value; }

}
