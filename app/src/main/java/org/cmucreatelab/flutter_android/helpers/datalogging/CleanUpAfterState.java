package org.cmucreatelab.flutter_android.helpers.datalogging;

import org.cmucreatelab.flutter_android.classes.datalogging.DataSet;

/**
 * Created by Steve on 3/13/2017.
 */

public class CleanUpAfterState implements UpdateDataLogsState {


    private CleanUpStateAfterListener cleanUpStateAfterListener;
    private DataSet deletedDataSet;


    public CleanUpAfterState(CleanUpStateAfterListener cleanUpStateAfterListener, DataSet dataSet) {
        this.cleanUpStateAfterListener = cleanUpStateAfterListener;
        this.deletedDataSet = dataSet;
    }


    @Override
    public void update() {
        cleanUpStateAfterListener.updateFromCleanUpAfter();
    }


    // getters


    public DataSet getDeletedDataSet() { return this.deletedDataSet; }


    public interface CleanUpStateAfterListener {
        public void updateFromCleanUpAfter();
    }
}
