package org.cmucreatelab.flutter_android.classes.datalogging;

/**
 * Created by Steve on 2/27/2017.
 */

public class DataLogDetails {

    private int intervalInt;
    private String intervalString;
    private int timePeriodInt;
    private String timePeriodString;


    public DataLogDetails(int intervalInt, String intervalString, int timePeriodInt, String timePeriodString) {
        this.intervalInt = intervalInt;
        this.intervalString = intervalString;
        this.timePeriodInt = timePeriodInt;
        this.timePeriodString = timePeriodString;
    }


    public int getIntervalInt() { return this.intervalInt; }
    public String getIntervalString() { return this.intervalString; }
    public int getTimePeriodInt() { return this.timePeriodInt; }
    public String getTimePeriodString() { return this.timePeriodString; }

}
