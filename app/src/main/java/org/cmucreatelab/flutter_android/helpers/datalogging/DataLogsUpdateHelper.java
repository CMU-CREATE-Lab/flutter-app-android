package org.cmucreatelab.flutter_android.helpers.datalogging;

import android.content.Context;
import android.util.Log;

import org.cmucreatelab.flutter_android.classes.datalogging.DataSet;
import org.cmucreatelab.flutter_android.classes.flutters.Flutter;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.FileHandler;

/**
 * Created by Steve on 3/13/2017.
 *
 * This class helps DataLogsActivity to updatedPoints the list of data logs and then react appropriately to whoever did the updating
 */
public class DataLogsUpdateHelper implements DataLoggingHandler.DataSetPointsListener, Flutter.PopulatedDataSetListener {

    private GlobalHandler globalHandler;
    private UpdateDataLogsState updateDataLogsState;

    private DataSet dataSetOnFlutter;
    private DataSet[] dataSetsOnDevice;


    public DataLogsUpdateHelper(Context context) {
        this.globalHandler = GlobalHandler.getInstance(context);
    }


    public synchronized void registerStateAndUpdatePoints(UpdateDataLogsState updateDataLogsState) {
        this.updateDataLogsState = updateDataLogsState;
        dataSetsOnDevice = FileHandler.loadDataSetsFromFile(globalHandler);
        if (globalHandler.melodySmartDeviceHandler.isConnected()) {
            this.globalHandler.dataLoggingHandler.populatePointsAvailable(this);
        } else {
            updateDataLogsState.updatedPoints();
        }
    }


    public synchronized void registerStateAndUpdateLogs(UpdateDataLogsState updateDataLogsState) {
        this.updateDataLogsState = updateDataLogsState;
        dataSetsOnDevice = FileHandler.loadDataSetsFromFile(globalHandler);
        if (!globalHandler.melodySmartDeviceHandler.isConnected()) {
            this.updateDataLogsState.updatedPoints();
        }
        else {
            this.globalHandler.sessionHandler.getSession().getFlutter().populateDataSet(globalHandler.appContext, this);
        }

    }


    @Override
    public void onDataSetPointsPopulated(boolean isSuccess) {
        this.updateDataLogsState.updatedPoints();
    }


    @Override
    public void onDataSetPopulated() {
        Log.d(Constants.LOG_TAG, "DataLogsUpdateHelper.onDataSetPopulated");
        dataSetOnFlutter = globalHandler.sessionHandler.getSession().getFlutter().getDataSet();
        this.updateDataLogsState.updatedLogs();
    }


    // getters
    public UpdateDataLogsState getUpdateDataLogsState() { return this.updateDataLogsState; }
    public DataSet getDataSetOnFlutter() { return this.dataSetOnFlutter; }
    public DataSet[] getDataSetsOnDevice() { return this.dataSetsOnDevice; }

}
