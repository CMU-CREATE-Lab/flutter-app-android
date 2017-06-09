package org.cmucreatelab.flutter_android.helpers.datalogging;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.cmucreatelab.android.melodysmart.models.MelodySmartMessage;
import org.cmucreatelab.flutter_android.classes.datalogging.DataLogDetails;
import org.cmucreatelab.flutter_android.classes.datalogging.DataPoint;
import org.cmucreatelab.flutter_android.classes.datalogging.DataSet;
import org.cmucreatelab.flutter_android.classes.flutters.FlutterMessageListener;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.FlutterProtocol;
import org.cmucreatelab.flutter_android.helpers.static_classes.MessageConstructor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Steve on 12/14/2016.
 */

public class DataLoggingHandler implements FlutterMessageListener {

    private static final int MAX_INTERVAL = 65535;
    private static final int MAX_SAMPLES = 255;
    private static final String INTERVAL_INT_KEY = "interval_int_key";
    private static final String INTERVAL_STRING_KEY = "interval_string_key";
    private static final String TIME_PERIOD_INT_KEY = "time_period_int_key";
    private static final String TIME_PERIOD_STRING_KEY = "time_period_string_key";

    private Context appContext;
    private GlobalHandler globalHandler;
    private DataSetPointsListener dataSetPointsListener;
    private DataSetListener dataSetListener;
    private int numberOfPoints, totalPoints;
    private ArrayList<String> keys;
    private String dataName;
    private TreeMap<String, DataPoint> data;

    // TODO @tasota did we still need these to be tracking something?
    private boolean isLogging;


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
        Log.d(Constants.LOG_TAG, "DataLoggingHandler.readLogName");
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
        totalPoints = Integer.valueOf(num);

        // is logging
        temp = temp.substring(num.length()+1, temp.length());
        if (temp.equals("1"))
            isLogging = true;
        else
            isLogging = false;

        dataSetPointsListener.onDataSetPointsPopulated(true);
    }


    public void readPoint(String output) {
        Log.d(Constants.LOG_TAG, "DataLoggingHandler.readPoint");
        // TODO - temporary fix to corrupt data points
        if (output.contains("ffffffff")) {
            DataPoint dataPoint = new DataPoint();
            Calendar calendar = Calendar.getInstance();
            data.put(String.valueOf(calendar.getTimeInMillis()), dataPoint);
            keys.add(String.valueOf(calendar.getTimeInMillis()));
        } else {
            String temp = output.substring(2, output.length());
            String[] sensorValues = new String[3];

            int index = temp.indexOf(",");
            String dataPointTime = temp.substring(0, index);
            Calendar calendar = Calendar.getInstance();
            StringBuilder date = new StringBuilder();
            StringBuilder time = new StringBuilder();
            calendar.setTimeInMillis(Long.parseLong(dataPointTime, 16)*1000);

            String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
            String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            String year = String.valueOf(calendar.get(Calendar.YEAR));
            String hour = String.valueOf(calendar.get(Calendar.HOUR));
            String minute = String.valueOf(calendar.get(Calendar.MINUTE));
            String second = String.valueOf(calendar.get(Calendar.SECOND));
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
                time.append("0" + minute);
            } else {
                time.append(minute);
            }
            if( second.length() < 2) {
                time.append(":0" + second + " ");
            } else {
                time.append(":" + second + " ");
            }

            time.append(amOrPm);
            date.append(month + "/" + day + "/" + year);

            temp = temp.substring(dataPointTime.length()+1, temp.length());
            sensorValues[0] = temp.substring(0, 2);
            temp = temp.substring(sensorValues[0].length(), temp.length());
            sensorValues[1] = temp.substring(0,2);
            sensorValues[2] = temp.substring(sensorValues[1].length(), temp.length());
            Integer sensor1 = Integer.parseInt(sensorValues[0], 16);
            Integer sensor2 = Integer.parseInt(sensorValues[1], 16);
            Integer sensor3 = Integer.parseInt(sensorValues[2], 16);

            // populate treemap
            DataPoint dataPoint = new DataPoint(date.toString(), time.toString(), sensor1.toString(), sensor2.toString(), sensor3.toString());
            data.put(date.toString() + time.toString(), dataPoint);
            keys.add(date.toString() + time.toString());
        }
    }


    public DataLoggingHandler(Context context) {
        this.appContext = context;
        this.isLogging = false;
        this.data = new TreeMap<>();
        this.keys = new ArrayList<>();
        this.dataName = "";
    }


    /**
     * Starts recording data on the Flutter.
     * @param interval How long to record in seconds.
     * @param samples The number of data points to record within the given interval.
     * @param logName The name that will be given to the data log.
     */
    public void startLogging(int interval, int samples, String logName) {
        globalHandler = GlobalHandler.getInstance(appContext);

        StringBuilder builder = new StringBuilder();
        String timestamp = getTimeInHex();
        String intervalString = getIntervalInHex(interval);
        String samplesString = getSamplesInHex(samples);
        builder.append("l," + timestamp + "," + intervalString + "," + samplesString);

        this.dataName = logName;

        globalHandler.melodySmartDeviceHandler.addMessage(new MelodySmartMessage("n," + logName));
        globalHandler.melodySmartDeviceHandler.addMessage(new MelodySmartMessage(builder.toString()));
    }


    /**
     * Saved the details of the recording data log.
     * @param activity Current activity (needed to access SharedPreferences).
     * @param intervalInt The interval integer supplied by the user.
     * @param intervalString The interval String (ex. 'minute') supplied by the user.
     * @param timePeriodInt The time period interger supplied by the user.
     * @param timePeriodString The timer period String (ex 'minutes') supplied by the user.
     */
    public void saveDataLogDetails(Activity activity, int intervalInt, String intervalString, int timePeriodInt, String timePeriodString) {
        SharedPreferences sharedPref = activity.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(INTERVAL_INT_KEY, intervalInt);
        editor.putString(INTERVAL_STRING_KEY, intervalString);
        editor.putInt(TIME_PERIOD_INT_KEY, timePeriodInt);
        editor.putString(TIME_PERIOD_STRING_KEY, timePeriodString);
        editor.apply();
    }


    /**
     * Loads the data log details from SharedPreferences.
     * @param activity Current activity (needed to access SharedPreferences)
     * @return The details of the data log contained within a DataLogDetails object.
     */
    public DataLogDetails loadDataLogDetails(Activity activity) {
        SharedPreferences sharedPref = activity.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        int intervalInt = sharedPref.getInt(INTERVAL_INT_KEY, 0);
        String intervalString = sharedPref.getString(INTERVAL_STRING_KEY, "minute");
        int timePeriodInt = sharedPref.getInt(TIME_PERIOD_INT_KEY, 0);
        String timePeriodString = sharedPref.getString(TIME_PERIOD_STRING_KEY, "minutes");
        return new DataLogDetails(intervalInt, intervalString, timePeriodInt, timePeriodString);
    }


    /**
     * Populates the data logs name and the number of points currently available. Refer to the protocol for more info.
     * @param dataSetPointsListener An event listener to be fired after this message has been fulfilled.
     */
    public synchronized void populatePointsAvailable(DataSetPointsListener dataSetPointsListener) {
        Log.d(Constants.LOG_TAG, "populatePointsAvailable");
        this.dataSetPointsListener = dataSetPointsListener;
        globalHandler = GlobalHandler.getInstance(appContext);
        globalHandler.sessionHandler.getSession().setFlutterMessageListener(this);

        globalHandler.melodySmartDeviceHandler.addMessage(MessageConstructor.constructReadLogName());
        globalHandler.melodySmartDeviceHandler.addMessage(MessageConstructor.constructReadNumberPointsAvailable());
    }


    /**
     * Populates the data set by calling for each data point. Refer to the protocol for more info.
     * @param dataSetListener An event listener to be fired after this message has been fulfilled.
     */
    public synchronized void populatedDataSet(DataSetListener dataSetListener) {
        this.dataSetListener = dataSetListener;
        this.globalHandler.sessionHandler.getSession().setFlutterMessageListener(this);
        this.data.clear();
        this.keys.clear();
        if (numberOfPoints > 0) {
            for (int i = 0; i < numberOfPoints; i++) {
                globalHandler.melodySmartDeviceHandler.addMessage(MessageConstructor.constructReadPoint((short)i));
            }
        } else {
            dataSetListener.onDataSetPopulated(null);
        }
    }


    /**
     * Deletes the data log on the Flutter.
     */
    public void deleteLog() {
        this.dataName = "";
        this.data = new TreeMap<>();
        this.isLogging = false;
        this.keys = new ArrayList<>();
        this.numberOfPoints = 0;
        this.totalPoints = 0;
        globalHandler.melodySmartDeviceHandler.addMessage(MessageConstructor.constructDeleteLog());
    }


    /**
     * Stops the current recording.
     */
    public void stopRecording() {
        globalHandler.melodySmartDeviceHandler.addMessage(MessageConstructor.constructStopLogging());
    }


    @Override
    public void onFlutterMessageReceived(String request, String response) {
        Log.d(Constants.LOG_TAG, "onMessageReceived - Request: " + request.substring(0,1) + " Response: " + response);
        if ((data.size() == numberOfPoints || data.size() == totalPoints) && data.size() != 0
                && request.substring(0,1).equals(String.valueOf(FlutterProtocol.Commands.READ_POINT))
                && dataSetListener != null) {
            Sensor[] sensors;
            sensors = globalHandler.sessionHandler.getSession().getFlutter().getSensors();
            String flutterName = globalHandler.sessionHandler.getSession().getFlutter().getName();
            DataSet dataSet = new DataSet(data, keys, dataName, flutterName, sensors);
            dataSetListener.onDataSetPopulated(dataSet);
        }
        if (numberOfPoints == totalPoints) {
            isLogging = false;
        }
    }


    // getters


    public boolean isLogging() { return isLogging; }
    public String getDataName() { return dataName; }
    public int getNumberOfPoints() { return numberOfPoints; }


    public interface DataSetPointsListener {
        public void onDataSetPointsPopulated(boolean isSuccess);
    }


    public interface DataSetListener {
        public void onDataSetPopulated(DataSet dataSet);
    }

}
