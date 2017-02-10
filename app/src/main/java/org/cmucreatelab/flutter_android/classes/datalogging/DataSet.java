package org.cmucreatelab.flutter_android.classes.datalogging;

import org.cmucreatelab.flutter_android.classes.sensors.Sensor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by Steve on 8/15/2016.
 *
 * DataSet
 *
 * A class representing the data as a whole.
 *
 */
public class DataSet implements Serializable {

    private TreeMap<String, DataPoint> data;
    private ArrayList<String> keys;
    private String dataName;
    private Sensor[] sensors;


    public DataSet() {
        this.data = null;
        this.keys = null;
        this.dataName = null;
        this.sensors = null;
    }


    public DataSet(TreeMap<String, DataPoint> data, ArrayList<String> keys, String dataName, Sensor[] sensors) {
        this.data = data;
        this.keys = keys;
        this.dataName = dataName;
        this.sensors = sensors;
    }


    // getters
    public TreeMap<String, DataPoint> getData() { return data; }
    public ArrayList<String> getKeys() { return keys; }
    public String getDataName() { return dataName; }
    public Sensor[] getSensors() { return this.sensors; }


    // setters
    public void setData(TreeMap<String, DataPoint> data) { this.data = data; }
    public void setKeys(ArrayList<String> keys) { this.keys = keys; }
    public void setDataName(String name) { this.dataName = name; }
    public void setSensors(Sensor[] sensors) { this.sensors = sensors; }

}
