package org.cmucreatelab.flutter_android.classes.datalogging;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Steve on 8/15/2016.
 *
 * DataSet
 *
 * A class representing the data as a whole.
 *
 */
public class DataSet {

    private HashMap<String, String[]> data;
    private ArrayList<String> keys;
    private String dataName;


    public DataSet(HashMap<String, String[]> data, ArrayList<String> keys, String dataName) {
        this.data = data;
        this.keys = keys;
        this.dataName = dataName;
    }


    // getters
    public HashMap<String, String[]> getData() { return data; }
    public ArrayList<String> getKeys() { return keys; }
    public String getDataName() { return dataName; }


    // setters
    public void setData(HashMap<String, String[]> data) { this.data = data; }
    public void setKeys(ArrayList<String> keys) { this.keys = keys; }
    public void setDataName(String name) { this.dataName = name; }


    public void loadFromFile(File file) throws IOException {
        /*CSVReader csvReader = new CSVReader(new FileReader(file.getPath()));
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
        table.put(previousArray[0], dataPoints);*/
    }

}
