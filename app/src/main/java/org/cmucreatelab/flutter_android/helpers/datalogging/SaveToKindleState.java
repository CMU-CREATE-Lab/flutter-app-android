package org.cmucreatelab.flutter_android.helpers.datalogging;

import android.util.Log;

import org.cmucreatelab.flutter_android.activities.DataLogsActivity;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.FileHandler;

/**
 * Created by Steve on 3/13/2017.
 *
 * SaveToKindleState
 *
 * A helper class for saving a data log to the kindle.
 */
public class SaveToKindleState extends UpdateDataLogState {

    private String dataSetName;


    public SaveToKindleState(DataLogsActivity dataLogsActivity, String name) {
        super(dataLogsActivity);
        this.dataSetName = name;
        globalHandler.sessionHandler.dismissProgressDialog();
        stopTimer();
        super.updateDataLogState = this;
    }

    @Override
    public void updatedLogs() {
        final UpdateDataLogState instance = this;
        dataLogsActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(Constants.LOG_TAG, "DataLogsActivity.updateFromSaveToKindle");
                globalHandler.sessionHandler.dismissProgressDialog();
                if (globalHandler.dataLoggingHandler.getDataName().equals(dataSetName)) {
                    dataLogsActivity.loadDataSet(dataLogsActivity.getDataLogsUpdateHelper().getDataSetOnFlutter());
                    if (!globalHandler.dataLoggingHandler.isLogging()) {
                        FileHandler.saveDataSetToFile(globalHandler, dataLogsActivity.getDataLogsUpdateHelper().getDataSetOnFlutter());
                        globalHandler.dataLoggingHandler.deleteLog();
                        instance.deleteDataSetOnFlutter();
                    }
                    return;
                }
            }
        });
        dataLogsActivity.getDataLogsUpdateHelper().registerStateAndUpdatePoints(new ResumeState(dataLogsActivity));
    }

}
