package org.cmucreatelab.flutter_android.classes.datalogging;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
    // TODO - we probably will want to structure this differently
    private HashMap<String, List<DataPoint>> table;


    public DataSet() {
        this.name = "";
    }


    public void loadFromFile(File file) throws IOException {
        CSVReader csvReader = new CSVReader(new FileReader(file.getPath()));
        ArrayList<String[]> list = (ArrayList<String[]>) csvReader.readAll();
        csvReader.close();

        ArrayList<DataPoint> dataPoints = new ArrayList();
        String[] previousArray = new String[2];
        for (String[] array : list) {
            if (previousArray[0] != null && !previousArray[0].equals(array[0])) {
                table.put(previousArray[0], dataPoints);
                dataPoints = new ArrayList();
            }
            dataPoints.add(new DataPoint(Integer.valueOf(array[1])));
            previousArray = array;
        }
        table.put(previousArray[0], dataPoints);
    }


    // getters

    public String getName() { return this.name; }
    public HashMap<String, List<DataPoint>> getTable() { return this.table; }

    // setters

    public void setName(String name) { this.name = name; }
    public void setTable(HashMap<String, List<DataPoint>> table) { this.table = table; }
}
