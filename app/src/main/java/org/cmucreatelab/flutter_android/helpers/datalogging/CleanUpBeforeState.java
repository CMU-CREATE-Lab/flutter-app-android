package org.cmucreatelab.flutter_android.helpers.datalogging;

import android.util.Log;

import org.cmucreatelab.flutter_android.activities.DataLogsActivity;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.CleanUpLogsDialog;

/**
 * Created by Steve on 3/13/2017.
 */
public class CleanUpBeforeState extends UpdateDataLogsState {

    private DataLogsUpdateHelper dataLogsUpdateHelper;


    public CleanUpBeforeState(DataLogsActivity dataLogsActivity) {
        super(dataLogsActivity);
        this.dataLogsUpdateHelper = dataLogsActivity.getDataLogsUpdateHelper();
    }



    @Override
    public void updatedPoints() {
        // not empty (get list to display when flutter isn't connected)
        Log.v(Constants.LOG_TAG,"CleanUpBeforeState.updatedPoints");
        updatedLogs();
    }


    /**
     * Updates the list of data logs before the clean up dialog is shown so the list is most up to date.
     */
    @Override
    public void updatedLogs() {
        dataLogsActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(Constants.LOG_TAG, "DataLogsActivity.updateFromCleanUpBefore");
                globalHandler.sessionHandler.dismissProgressDialog();
                CleanUpLogsDialog cleanUpLogsDialog = CleanUpLogsDialog.newInstance(dataLogsActivity, dataLogsUpdateHelper.getDataSetOnFlutter(), dataLogsUpdateHelper.getDataSetsOnDevice());
                cleanUpLogsDialog.show(dataLogsActivity.getSupportFragmentManager(), "tag");
            }
        });
    }

}
