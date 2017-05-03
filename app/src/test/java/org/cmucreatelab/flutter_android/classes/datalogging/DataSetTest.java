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
        for (int i = 0; i < 10; i++) {
            data.put(String.valueOf(i), new DataPoint("", "", String.valueOf(i), String.valueOf(i), String.valueOf(i)));
        }
        dataSet = new DataSet(data, keys, "name", sensors);
    }


    @Test
    public void getMeans_isCorrect() throws Exception {
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
        int[] expectedMedians = new int[3];
        data.clear();
        for (int i = 0; i < 10; i++) {
            data.put(String.valueOf(i), new DataPoint("", "", String.valueOf(9-i), String.valueOf(9-i), String.valueOf(9-i)));
        }
        for (int i = 0; i < expectedMedians.length; i++) {
            expectedMedians[i] = 5;
        }
        int[] actualMedians = dataSet.getMedians();
        for (int i = 0; i < expectedMedians.length; i++) {
            assertEquals(expectedMedians[i], actualMedians[i]);
        }
    }
}
