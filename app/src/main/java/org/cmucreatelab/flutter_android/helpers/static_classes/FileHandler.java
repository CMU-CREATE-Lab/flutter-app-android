package org.cmucreatelab.flutter_android.helpers.static_classes;

import android.content.Context;

import com.opencsv.CSVReader;

import org.cmucreatelab.flutter_android.classes.datalogging.DataPoint;
import org.cmucreatelab.flutter_android.classes.datalogging.DataSet;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
            File root = new File(globalHandler.appContext.getFilesDir(), DATA_SETS_PATH);
            root.mkdirs();
            File csv = new File(root, dataSet.getDataName() + ".csv");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(csv));
            StringBuilder sb = new StringBuilder();
            Iterator it = dataSet.getData().entrySet().iterator();
            Context context = globalHandler.appContext;
            sb.append(dataSet.getFlutterName() + "\n");
            sb.append("Date,Time," + context.getString(dataSet.getSensors()[0].getTypeTextId()) + "," +
                    context.getString(dataSet.getSensors()[1].getTypeTextId()) + "," + context.getString(dataSet.getSensors()[2].getTypeTextId()) + "\n");
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                DataPoint temp = (DataPoint) pair.getValue();
                sb.append(temp.getDate() + ',');
                sb.append(temp.getTime() + ',');
                sb.append(temp.getSensor1Value() + ',');
                sb.append(temp.getSensor2Value() + ',');
                sb.append(temp.getSensor3Value() + '\n');
            }
            bufferedWriter.write(sb.toString());
            bufferedWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static DataSet[] loadDataSetsFromFile(GlobalHandler globalHandler) {
        ArrayList<DataSet> dataSets = new ArrayList<>();

        File root = new File(globalHandler.appContext.getFilesDir(), DATA_SETS_PATH);
        File[] files = root.listFiles();

        if (files != null && files.length > 0) {
            try {
                for (File file : files) {
                    CSVReader csvReader = null;
                    csvReader = new CSVReader(new FileReader(file.getPath()));
                    ArrayList<String[]> list = (ArrayList<String[]>) csvReader.readAll();
                    csvReader.close();

                    DataSet dataSet = new DataSet();
                    TreeMap<String, DataPoint> map = new TreeMap<>();
                    ArrayList<String> keys = new ArrayList<>();
                    String[] sensorNames = new String[3];

                    for (int i = 0; i < list.size(); i++) {
                        if (i == 0) {
                            if (list.get(0).length == 1) {
                                // new format
                                dataSet.setFlutterName(list.get(0)[0]);
                                i++;
                            } else {
                                // old format
                                dataSet.setFlutterName("Unknown");
                            }

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
                    Sensor[] sensors = new Sensor[3];
                    sensors[0] = SensorFactory.getSensorFromName(1, sensorNames[0]);
                    sensors[1] = SensorFactory.getSensorFromName(2, sensorNames[1]);
                    sensors[2] = SensorFactory.getSensorFromName(3, sensorNames[2]);


                    dataSet.setData(map);
                    dataSet.setKeys(keys);
                    dataSet.setDataName(name);
                    dataSet.setSensors(sensors);
                    dataSets.add(dataSet);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return dataSets.toArray(new DataSet[dataSets.size()]);
    }


    public static File getFileFromDataSet(GlobalHandler globalHandler, DataSet dataSet) {
        File result = new File("");

        File root = new File(globalHandler.appContext.getFilesDir(), DATA_SETS_PATH);
        File[] files = root.listFiles();

        for(File file : files) {
            String name = file.getName();
            name = name.substring(0, name.indexOf("."));
            if (name.equals(dataSet.getDataName())) {
                result = file;
            }
        }

        return result;
    }


    public static void deleteFile(GlobalHandler globalHandler, DataSet dataSet) {
        File fileToDelete = getFileFromDataSet(globalHandler, dataSet);
        fileToDelete.delete();
    }


    private FileHandler() {}

}
