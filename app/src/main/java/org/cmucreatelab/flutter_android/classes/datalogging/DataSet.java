package org.cmucreatelab.flutter_android.classes.datalogging;

import android.util.Log;

import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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


    public int[] getMeans() {
        Iterator it = data.entrySet().iterator();
        int[] means = new int[3];

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            DataPoint dataPoint = (DataPoint) pair.getValue();
            means[0] += Integer.parseInt(dataPoint.getSensor1Value());
            means[1] += Integer.parseInt(dataPoint.getSensor2Value());
            means[2] += Integer.parseInt(dataPoint.getSensor3Value());
        }

        means[0] = (int) Math.round((double)means[0]/data.size());
        means[1] = (int) Math.round((double)means[1]/data.size());
        means[2] = (int) Math.round((double)means[2]/data.size());

        return means;
    }


    // TODO - do we want this to return a decimal value?
    public int[] getMedians() {
        Iterator it = data.entrySet().iterator();
        int[] medians = new int[3];
        int[][] sensorValues = new int[3][data.size()];

        for (int i = 0; i < data.size(); i++) {
            if (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                DataPoint dataPoint = (DataPoint) pair.getValue();
                sensorValues[0][i] = Integer.parseInt(dataPoint.getSensor1Value());
                sensorValues[1][i] = Integer.parseInt(dataPoint.getSensor2Value());
                sensorValues[2][i] = Integer.parseInt(dataPoint.getSensor3Value());
            }
        }

        Arrays.sort(sensorValues[0]);
        Arrays.sort(sensorValues[1]);
        Arrays.sort(sensorValues[2]);
        if (data.size() % 2 == 0) {
            medians[0] = (int) Math.round(((double)sensorValues[0][sensorValues[0].length / 2] + (double)sensorValues[0][sensorValues[0].length / 2 - 1]) / 2);
            medians[1] = (int)Math.round(((double)sensorValues[1][sensorValues[1].length / 2] + (double)sensorValues[1][sensorValues[1].length / 2 - 1]) / 2);
            medians[2] = (int)Math.round(((double)sensorValues[2][sensorValues[2].length / 2] + (double)sensorValues[2][sensorValues[2].length / 2 - 1]) / 2);
        }
        else {
            medians[0] = sensorValues[0][sensorValues[0].length / 2];
            medians[1] = sensorValues[1][sensorValues[1].length / 2];
            medians[2] = sensorValues[2][sensorValues[2].length / 2];
        }

        return medians;
    }


    // TODO - should probably handle when a data set has multiple modes
    public int[] getModes() {
        Iterator it = data.entrySet().iterator();
        int[] modes = new int[3];
        int[] max = new int[3];
        HashMap<String,Integer> tallySensor1 = new HashMap<>();
        HashMap<String,Integer> tallySensor2 = new HashMap<>();
        HashMap<String,Integer> tallySensor3 = new HashMap<>();

        Arrays.fill(max, 1);

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            DataPoint dataPoint = (DataPoint) pair.getValue();

            if(tallySensor1.get(dataPoint.getSensor1Value()) != null) {
                int count = tallySensor1.get(dataPoint.getSensor1Value());
                count++;
                tallySensor1.put(dataPoint.getSensor1Value(), count);
                if(count > max[0]) {
                    max[0] = count;
                    modes[0] = Integer.parseInt(dataPoint.getSensor1Value());
                }
            }
            else {
                tallySensor1.put(dataPoint.getSensor1Value(), 1);
            }

            if(tallySensor2.get(dataPoint.getSensor2Value()) != null) {
                int count = tallySensor2.get(dataPoint.getSensor2Value());
                count++;
                tallySensor2.put(dataPoint.getSensor2Value(), count);
                if(count > max[1]) {
                    max[1] = count;
                    modes[1] = Integer.parseInt(dataPoint.getSensor2Value());
                }
            }
            else {
                tallySensor2.put(dataPoint.getSensor2Value(), 1);
            }

            if(tallySensor3.get(dataPoint.getSensor3Value()) != null) {
                int count = tallySensor3.get(dataPoint.getSensor3Value());
                count++;
                tallySensor3.put(dataPoint.getSensor3Value(), count);
                if(count > max[2]) {
                    max[2] = count;
                    modes[2] = Integer.parseInt(dataPoint.getSensor3Value());
                }
            }
            else {
                tallySensor3.put(dataPoint.getSensor3Value(), 1);
            }
        }

        return modes;
    }


    public int[] getMinimums() {
        Iterator it = data.entrySet().iterator();
        int[] minimums = new int[3];

        if (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            DataPoint dataPoint = (DataPoint) pair.getValue();
            minimums[0] = Integer.parseInt(dataPoint.getSensor1Value());
            minimums[1] = Integer.parseInt(dataPoint.getSensor2Value());
            minimums[2] = Integer.parseInt(dataPoint.getSensor3Value());

            while (it.hasNext()) {
                pair = (Map.Entry)it.next();
                dataPoint = (DataPoint) pair.getValue();
                int temp = Integer.parseInt(dataPoint.getSensor1Value());
                if (minimums[0] > temp)
                    minimums[0] = temp;
                temp = Integer.parseInt(dataPoint.getSensor2Value());
                if (minimums[1] > temp)
                    minimums[1] = temp;
                temp = Integer.parseInt(dataPoint.getSensor3Value());
                if (minimums[2] > temp)
                    minimums[2] = temp;
            }
        }


        return minimums;
    }


    public int[] getMaximums() {
        Iterator it = data.entrySet().iterator();
        int[] maximums = new int[3];

        if (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            DataPoint dataPoint = (DataPoint) pair.getValue();
            maximums[0] = Integer.parseInt(dataPoint.getSensor1Value());
            maximums[1] = Integer.parseInt(dataPoint.getSensor1Value());
            maximums[2] = Integer.parseInt(dataPoint.getSensor1Value());

            while (it.hasNext()) {
                pair = (Map.Entry)it.next();
                dataPoint = (DataPoint) pair.getValue();
                int temp = Integer.parseInt(dataPoint.getSensor1Value());
                if (maximums[0] < temp)
                    maximums[0] = temp;
                temp = Integer.parseInt(dataPoint.getSensor2Value());
                if (maximums[1] < temp)
                    maximums[1] = temp;
                temp = Integer.parseInt(dataPoint.getSensor3Value());
                if (maximums[2] < temp)
                    maximums[2] = temp;
            }
        }


        return maximums;
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
