package org.cmucreatelab.flutter_android.helpers.datalogging;

import android.util.Log;

import org.cmucreatelab.flutter_android.activities.DataLogsActivity;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.FileHandler;

/**
 * Created by Steve on 3/13/2017.
 */

public class SaveToKindleState extends UpdateDataLogsState {

    private String dataSetName;


    public SaveToKindleState(DataLogsActivity dataLogsActivity, String name) {
        super(dataLogsActivity);
        this.dataSetName = name;
    }


    @Override
    public void updatedLogs() {
        dataLogsActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(Constants.LOG_TAG, "DataLogsActivity.updateFromSaveToKindle");
                globalHandler.sessionHandler.dismissProgressDialog();
                if (dataLogsActivity.getDataLogsUpdateHelper().getDataSetOnFlutter().getDataName().equals(dataSetName)) {
                    dataLogsActivity.loadDataSet(dataLogsActivity.getDataLogsUpdateHelper().getDataSetOnFlutter());
                    dataLogsActivity.checkIfLogging();
                    if (!globalHandler.dataLoggingHandler.isLogging()) {
                        FileHandler.saveDataSetToFile(globalHandler, dataLogsActivity.getDataLogsUpdateHelper().getDataSetOnFlutter());
                        globalHandler.dataLoggingHandler.deleteLog();
                    }
                    return;
                }
            }
        });
    }

}
