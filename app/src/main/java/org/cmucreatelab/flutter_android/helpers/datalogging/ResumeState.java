package org.cmucreatelab.flutter_android.helpers.datalogging;

import android.util.Log;

import org.cmucreatelab.flutter_android.activities.DataLogsActivity;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

/**
 * Created by Steve on 3/13/2017.
 *
 * A class to let the DataLogsActivity know how to react after the data logs have been updated.
 */

public class ResumeState extends UpdateDataLogsState {


    public ResumeState(DataLogsActivity dataLogsActivity) {
        super(dataLogsActivity);
    }


    /**
     * Updates the main UI on the DataLogsActivity
     */
    @Override
    public void updatedPoints() {
        dataLogsActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(Constants.LOG_TAG, "DataLogsActivity.updateFromResume");
                globalHandler.sessionHandler.dismissProgressDialog();
                dataLogsActivity.updateDynamicViews();
                dataLogsActivity.checkIfLogging();
            }
        });
    }

}
