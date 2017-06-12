package org.cmucreatelab.flutter_android.helpers.datalogging;

import android.util.Log;

import org.cmucreatelab.flutter_android.activities.DataLogsActivity;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.FileHandler;

/**
 * Created by Steve on 3/13/2017.
 *
 * A class to let the DataLogsActivity know how to react after the data logs have been updated.
 */

public class ResumeState extends UpdateDataLogState {

    public  ResumeState(DataLogsActivity dataLogsActivity) {
        super(dataLogsActivity);
        super.updateDataLogState = this;
    }


    @Override
    public synchronized void updatePoints() {
        updateDataLogsOnDevice();
        if (globalHandler.melodySmartDeviceHandler.isConnected()) {
            startTimer();
        } else {
            updatedPoints();
        }
    }

    /**
     * Updates the main UI on the DataLogsActivity
     */
    @Override
    public void updatedPoints() {
        super.updatedPoints();
        dataLogsActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(Constants.LOG_TAG, "ResumeState.updateFromResume");
                dataLogsActivity.updateDynamicViews();
            }
        });
    }

}
