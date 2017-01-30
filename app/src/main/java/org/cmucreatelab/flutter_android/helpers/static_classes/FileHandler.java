package org.cmucreatelab.flutter_android.helpers.static_classes;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.opencsv.CSVReader;

import org.cmucreatelab.flutter_android.classes.datalogging.DataPoint;
import org.cmucreatelab.flutter_android.classes.datalogging.DataSet;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Steve on 1/25/2017.
 */
public class FileHandler {

    private static final String DATA_SETS_PATH = "DATA_SETS";


    public static void saveDataSetToFile(GlobalHandler globalHandler, DataSet dataSet) {
        try {
            File directory = globalHandler.appContext.getDir(DATA_SETS_PATH, Context.MODE_PRIVATE);
            File csv = new File(directory, dataSet.getDataName() + ".csv");
            FileOutputStream fos = new FileOutputStream(csv);
            StringBuilder sb = new StringBuilder();
            Iterator it = dataSet.getData().entrySet().iterator();
            sb.append("Date,Time,Sensor 1,Sensor2,Sensor3\n");
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                DataPoint temp = (DataPoint) pair.getValue();
                sb.append(temp.getDate() + ',');
                sb.append(temp.getTime() + ',');
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


    public static ArrayList<DataSet> loadDataSetsFromFile(GlobalHandler globalHandler) {
        ArrayList<DataSet> dataSets = new ArrayList<>();

        File root = globalHandler.appContext.getDir(DATA_SETS_PATH, Context.MODE_PRIVATE);
        File[] files = root.listFiles();

        if (files != null && files.length > 0) {
            try {
                for (File file : files) {
                    Log.d(Constants.LOG_TAG, "there is a file");
                    CSVReader csvReader = null;
                    csvReader = new CSVReader(new FileReader(file.getPath()));
                    ArrayList<String[]> list = (ArrayList<String[]>) csvReader.readAll();
                    csvReader.close();

                    DataSet dataSet = new DataSet();
                    TreeMap<String, DataPoint> map = new TreeMap<>();
                    ArrayList<String> keys = new ArrayList<>();
                    String[] sensorNames = new String[3];

                    for (int i = 0; i < list.size(); i++) {
                        if (i ==0) {
                            String[] array = list.get(i);
                            sensorNames[0] = array[2];
                            sensorNames[1] = array[3];
                            sensorNames[2] = array[4];
                        } else {
                            String[] array = list.get(i);
                            DataPoint dataPoint = new DataPoint();
                            if (array.length == 5) {
                                dataPoint.setDate(array[0]);
                                dataPoint.setTime(array[1]);
                                dataPoint.setSensor1Value(array[2]);
                                dataPoint.setSensor2Value(array[3]);
                                dataPoint.setSensor3Value(array[4]);
                            }
                            map.put(dataPoint.getDate() + dataPoint.getTime(), dataPoint);
                            keys.add(dataPoint.getDate() + dataPoint.getTime());
                        }
                    }
                    String name = file.getName();
                    name = name.substring(0, name.indexOf("."));

                    dataSet.setData(map);
                    dataSet.setKeys(keys);
                    dataSet.setDataName(name);
                    dataSet.setSensorNames(sensorNames);
                    dataSets.add(dataSet);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return dataSets;
    }


    private FileHandler() {}

}
