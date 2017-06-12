package org.cmucreatelab.flutter_android.helpers.datalogging;

import org.cmucreatelab.flutter_android.activities.DataLogsActivity;

/**
 * Created by Steve on 6/9/2017.
 */

public class PauseReadingState extends UpdateDataLogState {


    public PauseReadingState(DataLogsActivity dataLogsActivity) {
        super(dataLogsActivity);
        super.updateDataLogState = this;
    }


    @Override
    public void updatedPoints() {
        stopTimer();
    }


    @Override
    public void updatedLogs() {
        stopTimer();
    }
}
