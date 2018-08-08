package org.cmucreatelab.flutter_android.helpers.datalogging;

import org.cmucreatelab.flutter_android.classes.datalogging.DataSet;

/**
 * Created by Steve on 6/8/2017.
 *
 * DataLogsUpdateHelper
 *
 * A class for helping to update data log points.
 */
public class DataLogsUpdateHelper {

    private static DataLogsUpdateHelper instance;
    private UpdateDataLogState updateDataLogState;


    private DataLogsUpdateHelper() {
        // empty
    }


    public static DataLogsUpdateHelper getInstance() {
        if (instance == null) {
            instance = new DataLogsUpdateHelper();
        }
        return instance;
    }


    public void registerStateAndUpdatePoints(UpdateDataLogState updateDataLogState) {
        if (this.updateDataLogState != null)
            this.updateDataLogState.stopTimer();
        this.updateDataLogState = updateDataLogState;
        updateDataLogState.updatePoints();
    }


    public void registerStateAndUpdateLogs(UpdateDataLogState updateDataLogState) {
        if (this.updateDataLogState != null)
            this.updateDataLogState.stopTimer();
        this.updateDataLogState = updateDataLogState;
        updateDataLogState.updateLogs();
    }


    // getters
    public UpdateDataLogState getUpdateDataLogState() { return this.updateDataLogState; }
    public DataSet getDataSetOnFlutter() { return updateDataLogState.getDataSetOnFlutter(); }
    public DataSet[] getDataSetsOnDevice() { return updateDataLogState.getDataSetsOnDevice(); }

}
