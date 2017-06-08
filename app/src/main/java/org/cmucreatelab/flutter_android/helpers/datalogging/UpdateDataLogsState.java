package org.cmucreatelab.flutter_android.helpers.datalogging;

import org.cmucreatelab.flutter_android.activities.DataLogsActivity;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;

/**
 * Created by Steve on 3/13/2017.
 *
 * You must call the super equivalent for everything in this class
 */

public abstract class UpdateDataLogsState {

    protected GlobalHandler globalHandler;
    protected DataLogsActivity dataLogsActivity;

    public UpdateDataLogsState(DataLogsActivity dataLogsActivity) {
        this.dataLogsActivity = dataLogsActivity;
        globalHandler = GlobalHandler.getInstance(dataLogsActivity);
    }

    /**
     * This is the updatedPoints event that will fire so the DataLogsActivity can update the points accordingly
     */
    public void updatedPoints() {
        dataLogsActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                globalHandler.sessionHandler.dismissProgressDialog();
            }
        });
    }

    /**
     * This is the updatedLogs event that will fire so the DataLogsActivity can update the logs accordingly
     */
    public void updatedLogs() {
        dataLogsActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                globalHandler.sessionHandler.dismissProgressDialog();
            }
        });
    }
}
