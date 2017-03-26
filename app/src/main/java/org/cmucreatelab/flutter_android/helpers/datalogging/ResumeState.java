package org.cmucreatelab.flutter_android.helpers.datalogging;

/**
 * Created by Steve on 3/13/2017.
 *
 * A class to let the DataLogsActivity know how to react after the data logs have been updated.
 */

public class ResumeState implements UpdateDataLogsState {


    private ResumeStateListener resumeStateListener;


    public ResumeState(ResumeStateListener resumeStateListener) {
        this.resumeStateListener = resumeStateListener;
    }


    @Override
    public void update() {
        resumeStateListener.updateFromResume();
    }


    public interface ResumeStateListener {
        public void updateFromResume();
    }
}
