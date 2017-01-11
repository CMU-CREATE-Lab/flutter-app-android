package org.cmucreatelab.flutter_android.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import org.cmucreatelab.flutter_android.classes.datalogging.DataPoint;
import org.cmucreatelab.flutter_android.classes.FlutterMessage;
import org.cmucreatelab.flutter_android.classes.datalogging.DataSet;
import org.cmucreatelab.flutter_android.classes.flutters.FlutterMessageListener;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Steve on 12/14/2016.
 */

// TODO - properly name a data log and properly start logging

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

    private DataSetListener dataSetListener;

    private int numberOfPoints;
    private int remainingPoints;
    private boolean isLogging;
    private boolean isSending;

    private ArrayList<String> keys;
    private String dataName;
    private TreeMap<String, DataPoint> data;


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
        Log.d(Constants.LOG_TAG, "readNumberOfPoints");
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

        if (numberOfPoints > 0) {
            for (int i = 0; i < numberOfPoints; i++) {
                globalHandler.melodySmartDeviceHandler.addMessage(new FlutterMessage(READ_POINT + "," + i));
            }
        } else {
            dataSetListener.onDataSetDetailsPopulated(null);
        }
    }


    public void readPoint(String output) {
        String temp = output.substring(2, output.length());
        String[] sensorValues = new String[3];

        int index = temp.indexOf(",");
        String dataPointTime = temp.substring(0, index);
        Calendar calendar = Calendar.getInstance();
        StringBuilder date = new StringBuilder();
        StringBuilder time = new StringBuilder();
        calendar.setTimeInMillis(Long.parseLong(dataPointTime, 16)*1000);
        Date dateTime = calendar.getTime();

        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String hour = String.valueOf(calendar.get(Calendar.HOUR));
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));
        String amOrPm = "";
        if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
            amOrPm = "AM";
        }
        else {
            amOrPm = "PM";
        }

        if (hour.equals("0")) {
            hour = "12";
        }

        time.append(hour + ":");
        if (minute.length() < 2) {
            time.append("0" + minute + " " + amOrPm);
        }
        else {
            time.append(minute + " " + amOrPm);
        }
        date.append(month + "/" + day + "/" + year);

        temp = temp.substring(dataPointTime.length()+1, temp.length());
        sensorValues[0] = temp.substring(0, 2);
        temp = temp.substring(sensorValues[0].length(), temp.length());
        sensorValues[1] = temp.substring(0,2);
        sensorValues[2] = temp.substring(sensorValues[1].length(), temp.length());
        Integer sensor1 = Integer.parseInt(sensorValues[0], 16);
        Integer sensor2 = Integer.parseInt(sensorValues[1], 16);
        Integer sensor3 = Integer.parseInt(sensorValues[2], 16);

        // populate hashmap
        DataPoint dataPoint = new DataPoint(dateTime, date.toString(), time.toString(), sensor1.toString(), sensor2.toString(), sensor3.toString());
        data.put(dataPointTime, dataPoint);
        keys.add(dataPointTime);
    }


    public DataLoggingHandler(Context context) {
        this.appContext = context;
        this.isSending = false;
        this.isLogging = false;
        //this.messageSender = new MessageSender();
        this.data = new TreeMap<>();
        this.keys = new ArrayList<>();
        this.dataName = "";
    }


    public void setDataSetListener(DataSetListener dataSetListener) {
        this.dataSetListener = dataSetListener;
    }


    // TODO - Do we want to delete the data log that is currently on the device before we start to log again?
    public void startLogging(int interval, int samples, String logName) {
        globalHandler = GlobalHandler.getInstance(appContext);

        StringBuilder builder = new StringBuilder();
        String timestamp = getTimeInHex();
        String intervalString = getIntervalInHex(interval);
        String samplesString = getSamplesInHex(samples);
        builder.append("l," + timestamp + "," + intervalString + "," + samplesString);

        globalHandler.melodySmartDeviceHandler.addMessage(new FlutterMessage("n," + logName));
        globalHandler.melodySmartDeviceHandler.addMessage(new FlutterMessage(builder.toString()));
    }


    public void populateDataSetDetails() {
        Log.d(Constants.LOG_TAG, "populateDataSetDetails");
        globalHandler = GlobalHandler.getInstance(appContext);
        globalHandler.sessionHandler.getSession().setFlutterMessageListener(this);

        globalHandler.melodySmartDeviceHandler.addMessage(new FlutterMessage(READ_LOG_NAME));
        globalHandler.melodySmartDeviceHandler.addMessage(new FlutterMessage(READ_NUMBER_OF_POINTS));
    }


    @Override
    public void onFlutterMessageReceived(String request, String response) {
        Log.d(Constants.LOG_TAG, "onMessageReceived - " + response);
        if (data.size() == numberOfPoints && data.size() != 0) {
            String[] sensorNames = new String[3];
            sensorNames[0] = "TODO";
            sensorNames[1] = "TODO";
            sensorNames[2] = "TODO";
            DataSet dataSet = new DataSet(data, keys, dataName, sensorNames);
            dataSetListener.onDataSetDetailsPopulated(dataSet);
        }
        isSending = false;
    }


    public interface DataSetListener {
        public void onDataSetDetailsPopulated(DataSet dataSet);
    }

}
