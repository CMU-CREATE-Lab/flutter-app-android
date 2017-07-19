package org.cmucreatelab.flutter_android.helpers.datalogging;

import android.util.Log;

import org.cmucreatelab.flutter_android.activities.DataLogsActivity;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseNavigationActivity;
import org.cmucreatelab.flutter_android.classes.datalogging.DataSet;
import org.cmucreatelab.flutter_android.classes.flutters.Flutter;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.FileHandler;

/**
 * Created by Steve on 3/13/2017.
 *
 * You must call the super equivalent for everything in this class
 */

public abstract class UpdateDataLogState extends DataRecordingTimer implements DataRecordingTimer.TimeExpireListener, Flutter.PopulatedDataSetListener {

    protected GlobalHandler globalHandler;
    protected DataLogsActivity dataLogsActivity;

    protected UpdateDataLogState updateDataLogState;
    private static DataSet dataSetOnFlutter;
    private static DataSet[] dataSetsOnDevice;

    private BaseNavigationActivity baseNavigationActivity;


    public UpdateDataLogState(DataLogsActivity dataLogsActivity) {
        super(5000);
        this.dataLogsActivity = dataLogsActivity;
        globalHandler = GlobalHandler.getInstance(dataLogsActivity);
        baseNavigationActivity = dataLogsActivity;
    }


    public void updateDataLogsOnDevice() {
        dataSetsOnDevice = FileHandler.loadDataSetsFromFile(globalHandler);
    }


    public void deleteDataSetOnFlutter() {
        dataSetOnFlutter = null;
    }


    public synchronized void updatePoints() {
        updateDataLogsOnDevice();
        if (globalHandler.melodySmartDeviceHandler.isConnected()) {
            globalHandler.sessionHandler.createProgressDialog(dataLogsActivity);
            globalHandler.sessionHandler.updateProgressDialogMessage(dataLogsActivity, "Updating data log points...");
            globalHandler.dataLoggingHandler.populatePointsAvailable((DataLoggingHandler.DataSetPointsListener) baseNavigationActivity);
        } else {
            updateDataLogState.updatedPoints();
        }
    }


    public synchronized void updateLogs() {
        updateDataLogsOnDevice();
        if (!globalHandler.melodySmartDeviceHandler.isConnected()) {
            this.updateDataLogState.updatedLogs();
        }
        else {
            globalHandler.sessionHandler.createProgressDialog(dataLogsActivity);
            globalHandler.sessionHandler.updateProgressDialogMessage(dataLogsActivity, "Updating data logs...");
            this.globalHandler.dataLoggingHandler.populateDataSetAvailable(this);
        }
    }



    /**
     * This is the updatedPoints event that will fire so the DataLogsActivity can update the points accordingly
     */
    public void updatedPoints() {
        if (globalHandler.melodySmartDeviceHandler.isConnected()) {
            dataLogsActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    globalHandler.sessionHandler.dismissProgressDialog();
                }
            });
        }
    }


    /**
     * This is the updatedLogs event that will fire so the DataLogsActivity can update the logs accordingly
     */
    public void updatedLogs() {
        if (globalHandler.melodySmartDeviceHandler.isConnected()) {
            dataLogsActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    globalHandler.sessionHandler.dismissProgressDialog();
                }
            });
        }
    }


    public synchronized void startTimer() {
        final TimeExpireListener timeExpireListener = this;
        dataLogsActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                stopTimer();
                startTimer(timeExpireListener);
            }
        });
    }


    public synchronized void stopTimer() {
        globalHandler.sessionHandler.dismissProgressDialog();
        super.stopTimer();
    }


    public void onDataSetPointsPopulated(boolean isSuccess) {
        this.updatedPoints();
    }


    @Override
    public void onDataSetPopulated(DataSet dataSet) {
        Log.d(Constants.LOG_TAG, "DataLogsUpdateHelper.onDataSetPopulated");
        dataSetOnFlutter = dataSet;
        this.updatedLogs();
    }


    @Override
    public void timerExpired() {
        globalHandler.dataLoggingHandler.populatePointsAvailable(new DataLoggingHandler.DataSetPointsListener() {
            @Override
            public void onDataSetPointsPopulated(boolean isSuccess) {
                if (isSuccess) {
                    dataLogsActivity.updateFromTimer();
                }
            }
        });
    }


    // getters
    public DataSet getDataSetOnFlutter() { return dataSetOnFlutter; }
    public DataSet[] getDataSetsOnDevice() { return dataSetsOnDevice; }

}
