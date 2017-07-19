package org.cmucreatelab.flutter_android.helpers.datalogging;

import android.util.Log;

import org.cmucreatelab.flutter_android.activities.DataLogsActivity;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.data_logs_tab.CleanUpLogsDialog;

/**
 * Created by Steve on 3/13/2017.
 */
public class CleanUpBeforeState extends UpdateDataLogState {


    public CleanUpBeforeState(DataLogsActivity dataLogsActivity) {
        super(dataLogsActivity);
        super.updateDataLogState = this;
    }


    /**
     * Updates the list of data logs before the clean up dialog is shown so the list is most up to date.
     */
    @Override
    public void updatedLogs() {
        super.updatedLogs();
        dataLogsActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(Constants.LOG_TAG, "DataLogsActivity.updateFromCleanUpBefore");
                CleanUpLogsDialog cleanUpLogsDialog = CleanUpLogsDialog.newInstance(dataLogsActivity, getDataSetOnFlutter(), getDataSetsOnDevice());
                cleanUpLogsDialog.show(dataLogsActivity.getSupportFragmentManager(), "tag");
            }
        });
    }

}
