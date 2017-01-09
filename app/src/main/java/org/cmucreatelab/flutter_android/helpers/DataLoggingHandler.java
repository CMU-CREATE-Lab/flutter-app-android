package org.cmucreatelab.flutter_android.helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.cmucreatelab.flutter_android.classes.FlutterMessage;
import org.cmucreatelab.flutter_android.classes.datalogging.DataSet;
import org.cmucreatelab.flutter_android.classes.flutters.FlutterMessageListener;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Steve on 12/14/2016.
 */
// TODO - may make a DataSet class to hold dataset details
public class DataLoggingHandler implements FlutterMessageListener {

    private static final int MAX_INTERVAL = 65535;
    private static final int MAX_SAMPLES = 255;
    private static final String STOP_LOGGING = "L";
    private static final String READ_NUMBER_OF_POINTS = "P";
    private static final String READ_LOG_NAME = "N";
    private static final String READ_POINT = "R";
    private static final String DELETE_LOG = "D";

    private Context appContext;
    private GlobalHandler globalHandler;
    private MessageSender messageSender;

    private DataSetListener dataSetListener;

    private int numberOfPoints;
    private int remainingPoints;
    private boolean isLogging;
    private boolean isSending;

    private ArrayList<String> keys;
    private String dataName;
    private HashMap<String, String[]> data;


    private String getTimeInHex() {
        String result = "";

        long timeStamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        result = Long.toHexString(timeStamp);

        return result;
    }


    private String getIntervalInHex(int interval) {
        String result = "";

        if (interval > MAX_INTERVAL) {
            interval = MAX_INTERVAL;
        }
        result = Integer.toHexString(interval);

        return result;
    }


    private String getSamplesInHex(int samples) {
        String result = "";

        if (samples > MAX_SAMPLES) {
            samples = MAX_SAMPLES;
        }
        result = Integer.toHexString(samples);

        return result;
    }


    public void readLogName(String output) {
        String temp = output.substring(2, output.length());
        dataName = temp;
    }


    public void readNumberOfPoints(String output) {
        output = "P,0,0,0";
        String temp = output.substring(2, output.length());

        // number of points
        int index = temp.indexOf(",");
        String num = temp.substring(0, index);
        numberOfPoints = Integer.valueOf(num);

        // remaining points
        temp = temp.substring(num.length()+1, temp.length());
        index = temp.indexOf(",");
        num = temp.substring(0, index);
        remainingPoints = Integer.valueOf(num);

        // is logging
        temp = temp.substring(num.length()+1, temp.length());
        isLogging = Boolean.valueOf(temp);

        // Now populate the actual data set
        messageSender = new MessageSender();
        String[] messages = new String[numberOfPoints];
        for (int i = 0; i < numberOfPoints; i++) {
            messages[i] = READ_POINT + "," + i;
        }

        if (numberOfPoints > 0) {
            messageSender.execute(messages);
        } else {
            dataSetListener.onDataSetDetailsPopulated(null);
        }
    }


    public void readPoint(String output) {
        String temp = output.substring(2, output.length());
        String[] sensorValues = new String[3];

        int index = temp.indexOf(",");
        String dataPointTime = temp.substring(0, index);

        temp = temp.substring(dataPointTime.length()+1, temp.length());
        sensorValues[0] = temp.substring(0, 2);
        temp = temp.substring(sensorValues[0].length(), temp.length());
        sensorValues[1] = temp.substring(0,2);
        sensorValues[2] = temp.substring(sensorValues[1].length(), temp.length());

        // populate hashmap
        data.put(dataPointTime, sensorValues);
        keys.add(dataPointTime);
    }


    public DataLoggingHandler(Context context) {
        this.appContext = context;
        this.isSending = false;
        this.isLogging = false;
        this.messageSender = new MessageSender();
        this.data = new HashMap<>();
        this.keys = new ArrayList<>();
        this.dataName = "";
    }


    public void setDataSetListener(DataSetListener dataSetListener) {
        this.dataSetListener = dataSetListener;
    }


    public void startLogging(int interval, int samples, String logName) {
        globalHandler = GlobalHandler.getInstance(appContext);
        String[] messages = new String[2];

        StringBuilder builder = new StringBuilder();
        String timestamp = getTimeInHex();
        String intervalString = getIntervalInHex(interval);
        String samplesString = getSamplesInHex(samples);
        builder.append("l," + timestamp + "," + intervalString + "," + samplesString);

        messages[0] = builder.toString();
        messages[1] = "n," + logName;
        this.messageSender = new MessageSender();
        this.messageSender.execute(messages);
    }


    public void populateDataSetDetails() {
        Log.d(Constants.LOG_TAG, "populateDataSetDetails");
        globalHandler = GlobalHandler.getInstance(appContext);
        globalHandler.sessionHandler.getSession().setFlutterMessageListener(this);
        messageSender = new MessageSender();
        String[] messages = new String[2];
        messages[0] = READ_LOG_NAME;
        messages[1] = READ_NUMBER_OF_POINTS;
        messageSender.execute(messages);
    }


    @Override
    public void onFlutterMessageReceived(String request, String response) {
        Log.d(Constants.LOG_TAG, "onMessageReceived - " + response);
        isSending = false;
    }


    private class MessageSender extends AsyncTask<String, Void, DataSet> {

        @Override
        protected DataSet doInBackground(String... strings) {
            isSending = true;

            for (int i = 0; i < strings.length; i++) {
                Log.d(Constants.LOG_TAG, String.valueOf(i));
                // TODO ?
                globalHandler.melodySmartDeviceHandler.addMessage(new FlutterMessage(strings[i]));
                while (isSending) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                isSending = true;
            }
            isSending = false;

            if (data.size() != 0) {
                DataSet dataSet = new DataSet(data, keys, dataName);
                return dataSet;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(DataSet dataSet) {
            super.onPostExecute(dataSet);
            if (dataSet != null)
                dataSetListener.onDataSetDetailsPopulated(dataSet);
        }
    }


    public interface DataSetListener {
        public void onDataSetDetailsPopulated(DataSet dataSet);
    }

}
