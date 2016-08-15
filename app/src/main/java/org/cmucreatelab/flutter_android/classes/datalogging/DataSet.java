package org.cmucreatelab.flutter_android.classes.datalogging;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Steve on 8/15/2016.
 *
 * DataSet
 *
 * A class representing the data as a whole.
 *
 */
public class DataSet {

    private String name;
    private HashMap<String, List<DataPoint>> table;


    public DataSet() {
        this.name = "";
    }


    // getters

    public String getName() { return this.name; }
    public HashMap<String, List<DataPoint>> getTable() { return this.table; }

    // setters

    public void setName(String name) { this.name = name; }
    public void setTable(HashMap<String, List<DataPoint>> table) { this.table = table; }
}
