package org.cmucreatelab.flutter_android.helpers.static_classes;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import org.cmucreatelab.flutter_android.classes.datalogging.DataPoint;
import org.cmucreatelab.flutter_android.classes.datalogging.DataSet;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Steve on 8/11/2016.
 *
 * FileHandler
 *
 * A helper class to handle saving and opening the data logs.
 *
 */
public class FileHandler {

    public static void saveFile(Context context, GlobalHandler globalHandler, DataSet dataSet) throws IOException {
        String name = globalHandler.sessionHandler.getFlutterName();
        if (name.contains(" ")) {
            name = name.replace(" ", "_");
        }

        File directory = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), name);
        File file = new File(directory.getPath() + File.separator + Constants.DATA_SET_PREFIX + dataSet.getName() + ".csv");

        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                Log.d(Constants.LOG_TAG, "failed to create directory");
                return;
            }
        }

        // Write to the csv file
        CSVWriter csvWriter = new CSVWriter(new FileWriter(file.getPath()));
        ArrayList<String[]> data = new ArrayList<>();
        Collection collection = dataSet.getTable().values();
        Iterator contentIt = collection.iterator();
        Set<String> keys = dataSet.getTable().keySet();
        Iterator keysIt = keys.iterator();
        while(contentIt.hasNext() && keysIt.hasNext()) {
            ArrayList<DataPoint> columnContents = (ArrayList<DataPoint>) contentIt.next();
            String key = (String) keysIt.next();
            for (DataPoint dp : columnContents) {
                data.add(new String[] {key, String.valueOf(dp.getValue())});
            }
        }
        csvWriter.writeAll(data);
        csvWriter.close();
    }


    public static HashMap<String, List<DataPoint>> openFile(Context context, GlobalHandler globalHandler, String fileName) throws IOException {
        String name = globalHandler.sessionHandler.getFlutterName();
        if (name.contains(" ")) {
            name = name.replace(" ", "_");
        }

        File directory = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), name);
        File file = new File(directory.getPath() + File.separator + fileName);

        CSVReader csvReader = new CSVReader(new FileReader(file.getPath()));
        ArrayList<String[]> list = (ArrayList<String[]>) csvReader.readAll();
        csvReader.close();

        HashMap<String, List<DataPoint>> dataSet = new HashMap<>();
        ArrayList<DataPoint> dataPoints = new ArrayList();
        String[] previousArray = new String[2];
        for (String[] array : list) {
            if (previousArray[0] != null && !previousArray[0].equals(array[0])) {
                dataSet.put(previousArray[0], dataPoints);
                dataPoints = new ArrayList();
            }
            dataPoints.add(new DataPoint(Integer.valueOf(array[1])));
            previousArray = array;
        }
        dataSet.put(previousArray[0], dataPoints);

        return dataSet;
    }

}
