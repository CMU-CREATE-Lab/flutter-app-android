package org.cmucreatelab.flutter_android.classes.datalogging;

/**
 * Created by Steve on 1/4/2017.
 */
public class DataPoint implements Comparable<DataPoint> {

    private String date;
    private String time;
    private String sensor1Value;
    private String sensor2Value;
    private String sensor3Value;


    public DataPoint() {
        date = "0/00/0000";
        time = "0:00:00";
        sensor1Value = "0";
        sensor2Value = "0";
        sensor3Value = "0";
    }


    public DataPoint(String date, String time, String sensor1Value, String sensor2Value, String sensor3Value) {
        this.date = date;
        this.time = time;
        this.sensor1Value = sensor1Value;
        this.sensor2Value = sensor2Value;
        this.sensor3Value = sensor3Value;
    }


    // getters

    public String getDate() { return this.date; }
    public String getTime() { return this.time; }
    public String getSensor1Value() { return this.sensor1Value; }
    public String getSensor2Value() { return this.sensor2Value; }
    public String getSensor3Value() { return this.sensor3Value; }


    // setters


    public void setDate(String date) { this.date = date; }
    public void setTime(String time) { this.time = time; }
    public void setSensor1Value(String sensor1Value) { this.sensor1Value = sensor1Value; }
    public void setSensor2Value(String sensor2Value) { this.sensor2Value = sensor2Value; }
    public void setSensor3Value(String sensor3Value) { this.sensor3Value = sensor3Value; }


    @Override
    public int compareTo(DataPoint dataPoint) {
        return time.compareTo(dataPoint.getTime());
    }

}
