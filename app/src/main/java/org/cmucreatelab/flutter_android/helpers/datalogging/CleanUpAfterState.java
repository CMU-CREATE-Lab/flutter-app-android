package org.cmucreatelab.flutter_android.helpers.datalogging;

import android.util.Log;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.DataLogsActivity;
import org.cmucreatelab.flutter_android.classes.datalogging.DataSet;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

/**
 * Created by Steve on 3/13/2017.
 */

public class CleanUpAfterState implements UpdateDataLogsState {


    private GlobalHandler globalHandler;
    private DataLogsActivity dataLogsActivity;
    private DataSet deletedDataSet;


    public CleanUpAfterState(DataLogsActivity dataLogsActivity, DataSet dataSet) {
        this.dataLogsActivity = dataLogsActivity;
        this.deletedDataSet = dataSet;
        this.globalHandler = GlobalHandler.getInstance(dataLogsActivity);
    }


    /**
     *
     */
    @Override
    public void updatePoints() {
        dataLogsActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(Constants.LOG_TAG, "DataLogsActivity.updateFromCleanUpAfter");
                globalHandler.sessionHandler.createProgressDialog(dataLogsActivity);
                globalHandler.sessionHandler.updateProgressDialogMessage(dataLogsActivity.getString(R.string.loading_data));

                // test if the current selected data log was one that got deleted
                DataSet workingDataSet = dataLogsActivity.getWorkingDataSet();
                if (workingDataSet != null) {
                    if (workingDataSet.getDataName().equals(deletedDataSet.getDataName())) {
                        dataLogsActivity.setWorkingDataSet(null);
                        dataLogsActivity.setWorkingDataPoint(null);
                    }
                }
                dataLogsActivity.getDataLogsUpdateHelper().registerStateAndUpdatePoints(new ResumeState(dataLogsActivity));
            }
        });
    }

    @Override
    public void updateLogs() {

    }

}
