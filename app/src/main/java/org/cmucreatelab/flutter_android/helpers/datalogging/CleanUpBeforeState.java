package org.cmucreatelab.flutter_android.helpers.datalogging;

/**
 * Created by Steve on 3/13/2017.
 */

public class CleanUpBeforeState implements UpdateDataLogsState {

    private CleanUpBeforeStateListener cleanUpStateListener;


    public CleanUpBeforeState(CleanUpBeforeStateListener cleanUpStateListener) {
        this.cleanUpStateListener = cleanUpStateListener;
    }


    @Override
    public void update() {
        cleanUpStateListener.updateFromCleanUpBefore();
    }


    public interface CleanUpBeforeStateListener {
        public void updateFromCleanUpBefore();
    }

}
