package org.cmucreatelab.flutter_android.helpers.datalogging;

/**
 * Created by Steve on 3/13/2017.
 *
 * Each class that implements this will define their own interface for DataLogsActivity to implement.
 * This interface will be a unique event in order for DataLogsActivity to know how to react.
 */

public interface UpdateDataLogsState {
    /**
     * This is the updatePoints event that will fire so the DataLogsActivity can update the points accordingly
     */
    void updatePoints();

    /**
     * This is the updateLogs event that will fire so the DataLogsActivity can update the logs accordingly
     */
    void updateLogs();
}
