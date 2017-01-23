package org.cmucreatelab.flutter_android.classes.datalogging;

import android.os.Environment;
import android.util.Log;

import org.cmucreatelab.flutter_android.classes.relationships.Constant;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Steve on 8/15/2016.
 *
 * DataSet
 *
 * A class representing the data as a whole.
 *
 */
public class DataSet {

    private TreeMap<String, DataPoint> data;
    private ArrayList<String> keys;
    private String dataName;
    private String[] sensorNames;
    private File csv;


    private void populateFile() {
        try {
            FileOutputStream fos = new FileOutputStream(csv);
            StringBuilder sb = new StringBuilder();
            Iterator it = data.entrySet().iterator();
            sb.append("Time,Sensor 1,Sensor2,Sensor3\n");
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                DataPoint temp = (DataPoint) pair.getValue();
                sb.append(temp.getDate() + " " + temp.getTime());
                sb.append(',');
                sb.append(temp.getSensor1Value() + ',');
                sb.append(temp.getSensor2Value() + ',');
                sb.append(temp.getSensor3Value() + '\n');
            }
            fos.write(sb.toString().getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public DataSet(TreeMap<String, DataPoint> data, ArrayList<String> keys, String dataName, String[] sensorNames) {
        this.data = data;
        this.keys = keys;
        this.dataName = dataName;
        this.sensorNames = sensorNames;
        this.csv = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath(), "/" + dataName + ".csv");
        populateFile();
    }


    // getters
    public TreeMap<String, DataPoint> getData() { return data; }
    public ArrayList<String> getKeys() { return keys; }
    public String getDataName() { return dataName; }
    public String[] getSensorNames() { return this.sensorNames; }


    // setters
    public void setData(TreeMap<String, DataPoint> data) {
        this.data = data;
        populateFile();
    }
    public void setKeys(ArrayList<String> keys) { this.keys = keys; }
    public void setDataName(String name) { this.dataName = name; }
    public void setSensorNames(String[] sensorNames) { this.sensorNames = sensorNames; }


    // TODO - We may want this somewhere since you can send data logs via email.
    // Used to import a csv into the app someway
    /*public void loadFromFile(File file) throws IOException {
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
    }*/


    public File getFile() {
        return csv;
    }

}
