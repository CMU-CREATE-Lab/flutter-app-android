package org.cmucreatelab.flutter_android.helpers.datalogging;

/**
 * Created by Steve on 3/13/2017.
 */

public class SaveToKindleState implements UpdateDataLogsState {

    private SaveToKindleStateListener saveToKindleStateListener;
    private String dataSetName;


    public SaveToKindleState(SaveToKindleStateListener saveToKindleStateListener, String name) {
        this.saveToKindleStateListener = saveToKindleStateListener;
        this.dataSetName = name;
    }


    @Override
    public void update() {
        saveToKindleStateListener.updateFromSaveToKindle();
    }


    // getters


    public String getDataSetName() { return this.dataSetName; }


    public interface SaveToKindleStateListener {
        public void updateFromSaveToKindle();
    }
}
