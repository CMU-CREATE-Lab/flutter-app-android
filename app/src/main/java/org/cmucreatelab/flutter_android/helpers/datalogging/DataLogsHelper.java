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
 */

public class DataLogsHelper implements DataLoggingHandler.DataSetPointsListener, Flutter.PopulatedDataSetListener {

    private GlobalHandler globalHandler;
    private UpdateDataLogsState updateDataLogsState;

    private DataSet dataSetOnFlutter;
    private DataSet[] dataSetsOnDevice;


    public DataLogsHelper(Context context) {
        this.globalHandler = GlobalHandler.getInstance(context);
    }


    public void registerStateAndUpdate(UpdateDataLogsState updateDataLogsState) {
        this.updateDataLogsState = updateDataLogsState;
        if (globalHandler.melodySmartDeviceHandler.isConnected())
            this.globalHandler.dataLoggingHandler.populatePointsAvailable(this);
        dataSetsOnDevice = FileHandler.loadDataSetsFromFile(globalHandler);
        if (!globalHandler.melodySmartDeviceHandler.isConnected())
            this.updateDataLogsState.update();

    }


    @Override
    public void onDataSetPointsPopulated(boolean isSuccess) {
        this.globalHandler.sessionHandler.getSession().getFlutter().populateDataSet(globalHandler.appContext, this);
    }


    @Override
    public void onDataSetPopulated() {
        Log.d(Constants.LOG_TAG, "DataLogsHelper.onDataSetPopulated");
        dataSetOnFlutter = globalHandler.sessionHandler.getSession().getFlutter().getDataSet();
        this.updateDataLogsState.update();
    }


    // getters
    public UpdateDataLogsState getUpdateDataLogsState() { return this.updateDataLogsState; }
    public DataSet getDataSetOnFlutter() { return this.dataSetOnFlutter; }
    public DataSet[] getDataSetsOnDevice() { return this.dataSetsOnDevice; }
}
