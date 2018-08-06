package org.cmucreatelab.flutter_android.classes.datalogging;

import java.io.Serializable;

/**
 * Created by Steve on 2/27/2017.
 *
 * DataLogDetails
 *
 * A class that stores the details of a data log.
 */
public class DataLogDetails implements Serializable {

    private String dataLogName;
    private int intervalInt;
    private String intervalString;
    private int timePeriodInt;
    private String timePeriodString;


    public DataLogDetails() {
        this.dataLogName = "";
        this.intervalInt = 0;
        this.intervalString = "";
        this.timePeriodInt = 0;
        this.timePeriodString = "";
    }


    public DataLogDetails(String name, int intervalInt, String intervalString, int timePeriodInt, String timePeriodString) {
        this.intervalInt = intervalInt;
        this.intervalString = intervalString;
        this.timePeriodInt = timePeriodInt;
        this.timePeriodString = timePeriodString;
    }


    /* getters/setters */


    public String getDataLogName() { return this.dataLogName; }
    public int getIntervalInt() { return this.intervalInt; }
    public String getIntervalString() { return this.intervalString; }
    public int getTimePeriodInt() { return this.timePeriodInt; }
    public String getTimePeriodString() { return this.timePeriodString; }
    public void setDataLogName(String dataLogName) { this.dataLogName = dataLogName; }
    public void setIntervalInt(int intervalInt) { this.intervalInt = intervalInt; }
    public void setIntervalString(String intervalString) { this.intervalString = intervalString; }
    public void setTimePeriodInt(int timePeriodInt) { this.timePeriodInt = timePeriodInt; }
    public void setTimePeriodString(String timePeriodString) { this.timePeriodString = timePeriodString; }

}
