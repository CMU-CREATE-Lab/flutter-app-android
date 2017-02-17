package org.cmucreatelab.flutter_android.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;

import org.cmucreatelab.flutter_android.helpers.DataLoggingHandler;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;

/**
 * Created by Steve on 1/17/2017.
 */
public abstract class BaseDataLoggingDialog extends BaseResizableDialog implements DataLoggingHandler.DataSetPointsListener {

    private GlobalHandler globalHandler;
    private boolean isLogging;
    private boolean isWaitingForResponse;


    protected int timeToSeconds(String time) {
        int result = 0;

        if (time.equals("minute") || time.equals("minutes")) {
            result = 60;
        } else if(time.equals("hour") || time.equals("hours")) {
            result = 3600;
        } else if (time.equals("day") || time.equals("days")) {
            result = 86400;
        } else if(time.equals("week") || time.equals("weeks")) {
            result = 604800;
        }

        return result;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        globalHandler = GlobalHandler.getInstance(getActivity());
        globalHandler.dataLoggingHandler.populatePointsAvailable(this);
        isLogging = false;
        isWaitingForResponse = true;

        return dialog;
    }


    @Override
    public void onDataSetPointsPopulated(boolean isSuccess) {
        isWaitingForResponse = false;
        if (isSuccess) {
            isLogging = globalHandler.dataLoggingHandler.getIsLogging();
        }
    }


    public boolean getIsLogging() { return this.isLogging; }
    public boolean getIsWaitingForResponse() { return this.isWaitingForResponse; }
}
