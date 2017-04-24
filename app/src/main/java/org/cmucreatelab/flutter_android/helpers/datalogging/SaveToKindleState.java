package org.cmucreatelab.flutter_android.helpers.datalogging;

import android.util.Log;

import org.cmucreatelab.flutter_android.activities.DataLogsActivity;
import org.cmucreatelab.flutter_android.classes.datalogging.DataSet;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

/**
 * Created by Steve on 3/13/2017.
 */

public class SaveToKindleState implements UpdateDataLogsState {

    private GlobalHandler globalHandler;
    private DataLogsActivity dataLogsActivity;
    private String dataSetName;


    public SaveToKindleState(DataLogsActivity dataLogsActivity, String name) {
        this.dataLogsActivity = dataLogsActivity;
        this.dataSetName = name;
        this.globalHandler = GlobalHandler.getInstance(dataLogsActivity);
    }


    /**
     * Updates the UI in a unique manner
     */
    @Override
    public void update() {
        dataLogsActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(Constants.LOG_TAG, "DataLogsActivity.updateFromSaveToKindle");
                globalHandler.sessionHandler.dismissProgressDialog();
                for (DataSet dataSet : dataLogsActivity.getDataLogsUpdateHelper().getDataSetsOnDevice()) {
                    if (dataSet.getDataName().equals(dataSetName)) {
                        dataLogsActivity.loadDataSet(dataSet);
                        dataLogsActivity.checkIfLogging();
                        return;
                    }
                }
            }
        });
    }

}
