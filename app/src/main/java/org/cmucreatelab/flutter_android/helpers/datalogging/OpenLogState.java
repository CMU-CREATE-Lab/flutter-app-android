package org.cmucreatelab.flutter_android.helpers.datalogging;

/**
 * Created by Steve on 3/13/2017.
 */

public class OpenLogState implements UpdateDataLogsState {

    private OpenLogStateListener openLogStateListener;


    public OpenLogState(OpenLogStateListener openLogStateListener) {
        this.openLogStateListener = openLogStateListener;
    }


    @Override
    public void update() {
        openLogStateListener.updateFromOpenLog();
    }


    public interface OpenLogStateListener {
        public void updateFromOpenLog();
    }
}
