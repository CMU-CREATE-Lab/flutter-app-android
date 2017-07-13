package org.cmucreatelab.flutter_android.helpers.datalogging;

import android.util.Log;
import android.widget.Toast;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.DataLogsActivity;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.DataLogsTab.OpenLogDialog;

/**
 * Created by Steve on 3/13/2017.
 */

public class OpenLogState extends UpdateDataLogState {


    public OpenLogState(DataLogsActivity dataLogsActivity) {
        super(dataLogsActivity);
        super.updateDataLogState = this;
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
                if (globalHandler.dataLoggingHandler.getNumberOfPoints() > 0)
                    Toast.makeText(dataLogsActivity, R.string.done_recording, Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void updatedLogs() {
        dataLogsActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dataLogsActivity.loadDataSet(dataLogsActivity.getDataLogsUpdateHelper().getDataSetOnFlutter());
                dataLogsActivity.getDataLogsUpdateHelper().registerStateAndUpdatePoints(new ResumeState(dataLogsActivity));
            }
        });
    }

}
