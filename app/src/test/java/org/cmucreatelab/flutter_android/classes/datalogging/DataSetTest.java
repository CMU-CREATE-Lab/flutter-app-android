package org.cmucreatelab.flutter_android.classes.datalogging;

import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;

/**
 * Created by Steve on 5/3/2017.
 */

public class DataSetTest {

    private TreeMap<String, DataPoint> data;
    private DataSet dataSet;

    @Mock
    private Sensor[] sensors;
    @Mock
    private ArrayList<String> keys;

    @Before
    public void init() {
        data = new TreeMap<>();
        dataSet = new DataSet(data, keys, "name", "flutter_name", sensors);
    }


    @Test
    public void getMeans_isCorrect() throws Exception {
        for (int i = 0; i < 10; i++) {
            data.put(String.valueOf(i), new DataPoint("", "", String.valueOf(i), String.valueOf(i), String.valueOf(i)));
        }

        int[] expectedMeans = new int[3];
        for (int i = 0; i < expectedMeans.length; i++) {
            expectedMeans[i] = 5;
        }
        int[] actualMeans = dataSet.getMeans();
        for (int i = 0; i < expectedMeans.length; i++) {
            assertEquals(expectedMeans[i], actualMeans[i]);
        }
    }


    @Test
    public void getMedians_isCorrect() throws Exception {
        for (int i = 0; i < 10; i++) {
            data.put(String.valueOf(i), new DataPoint("", "", String.valueOf(9-i), String.valueOf(9-i), String.valueOf(9-i)));
        }

        int[] expectedMedians = new int[3];
        for (int i = 0; i < expectedMedians.length; i++) {
            expectedMedians[i] = 5;
        }
        int[] actualMedians = dataSet.getMedians();
        for (int i = 0; i < expectedMedians.length; i++) {
            assertEquals(expectedMedians[i], actualMedians[i]);
        }
    }


    @Test
    public void getModes_isCorrect() throws Exception {
        for (int i = 0; i < 9; i++) {
            data.put(String.valueOf(i), new DataPoint("", "", String.valueOf(9-i), String.valueOf(9-i), String.valueOf(9-i)));
        }
        data.put(String.valueOf(9), new DataPoint("", "", String.valueOf(9-1), String.valueOf(9-1), String.valueOf(9-1)));

        int[] expectedModes = new int[3];
        for (int i = 0; i < expectedModes.length; i++) {
            expectedModes[i] = 8;
        }
        int[] actualModes = dataSet.getModes();
        for (int i = 0; i < expectedModes.length; i++) {
            assertEquals(expectedModes[i], actualModes[i]);
        }
    }


    @Test
    public void getMinimums_isCorrect() throws Exception {
        for (int i = 0; i < 10; i++) {
            data.put(String.valueOf(i), new DataPoint("", "", String.valueOf(9-i), String.valueOf(9-i), String.valueOf(9-i)));
        }

        int[] expectedMins = new int[3];
        for (int i = 0; i < expectedMins.length; i++) {
            expectedMins[i] = 0;
        }
        int[] actualMins = dataSet.getMinimums();
        for (int i = 0; i < expectedMins.length; i++) {
            assertEquals(expectedMins[i], actualMins[i]);
        }
    }


    @Test
    public void getMaximums_isCorrect() throws Exception {
        for (int i = 0; i < 10; i++) {
            data.put(String.valueOf(i), new DataPoint("", "", String.valueOf(9-i), String.valueOf(9-i), String.valueOf(9-i)));
        }

        int[] expectedMaxs = new int[3];
        for (int i = 0; i < expectedMaxs.length; i++) {
            expectedMaxs[i] = 9;
        }
        int[] actualMaxs = dataSet.getMaximums();
        for (int i = 0; i < expectedMaxs.length; i++) {
            assertEquals(expectedMaxs[i], actualMaxs[i]);
        }
    }
}
