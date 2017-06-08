package org.cmucreatelab.flutter_android.helpers.datalogging;

import android.util.Log;

import org.cmucreatelab.flutter_android.activities.DataLogsActivity;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.OpenLogDialog;

/**
 * Created by Steve on 3/13/2017.
 */

public class OpenLogState extends UpdateDataLogsState {


    public OpenLogState(DataLogsActivity dataLogsActivity) {
        super(dataLogsActivity);
    }


    /**
     * Shows the open log dialog after the list of data logs has been updated
     */
    @Override
    public void updatedPoints() {
        dataLogsActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(Constants.LOG_TAG, "DataLogsActivity.updateFromOpenLogAfter");
                globalHandler.sessionHandler.dismissProgressDialog();
                OpenLogDialog openLogDialog = OpenLogDialog.newInstance(
                        dataLogsActivity, globalHandler.dataLoggingHandler.getNumberOfPoints(), globalHandler.dataLoggingHandler.getDataName(), dataLogsActivity.getDataLogsUpdateHelper().getDataSetsOnDevice()
                );
                openLogDialog.show(dataLogsActivity.getSupportFragmentManager(), "tag");
                dataLogsActivity.checkIfLogging();
            }
        });
    }


    @Override
    public void updatedLogs() {
        dataLogsActivity.loadDataSet(dataLogsActivity.getDataLogsUpdateHelper().getDataSetOnFlutter());
    }

}
