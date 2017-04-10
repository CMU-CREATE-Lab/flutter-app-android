package org.cmucreatelab.flutter_android.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.SensorsActivity;
import org.cmucreatelab.flutter_android.helpers.datalogging.DataLoggingHandler;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.io.Serializable;

import butterknife.OnClick;

/**
 * Created by Steve on 1/17/2017.
 */
public abstract class BaseDataLoggingDialog extends BaseResizableDialog implements Serializable, DataLoggingHandler.DataSetPointsListener {

    protected static final String RECORD_KEY = "record_key";
    protected static final String DISMISS_KEY = "dismiss_key";
    protected static final String BUTTON_KEY = "button_key";

    private GlobalHandler globalHandler;
    private boolean isLogging;
    private boolean isWaitingForResponse;

    private DialogRecordListener dialogRecordListener;
    private DismissDialogListener dismissDialogListener;
    private int buttonDrawableId;

    protected EditText dataSetNameText;
    protected EditText intervalsText;
    protected Spinner intervalSpinner;
    protected EditText timePeriodText;
    protected Spinner timePeriodSpinner;


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
        dialogRecordListener = (DialogRecordListener) getArguments().getSerializable(RECORD_KEY);
        dismissDialogListener = (DismissDialogListener) getArguments().getSerializable(DismissDialogListener.DISMISS_KEY);
        buttonDrawableId = getArguments().getInt(BUTTON_KEY);

        globalHandler = GlobalHandler.getInstance(getActivity());
        globalHandler.dataLoggingHandler.populatePointsAvailable(this);
        isLogging = false;
        isWaitingForResponse = true;

        return dialog;
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (dismissDialogListener != null)
            dismissDialogListener.onDialogDismissed();
    }


    @OnClick(R.id.button_start_recording)
    public void onClickButtonStartRecording() {
        Log.d(Constants.LOG_TAG, "onClickButtonStartRecording");
        String name = dataSetNameText.getText().toString();
        if (!name.matches("")) {
            String intervalString = intervalsText.getText().toString();
            if (!intervalString.matches("")) {
                String timerPeriodString = timePeriodText.getText().toString();
                if (!timerPeriodString.matches("")) {
                    if (!getIsWaitingForResponse()) {
                        if (!getIsLogging()) {
                            GlobalHandler globalHandler = GlobalHandler.getInstance(getActivity());
                            int iInt = Integer.parseInt(intervalsText.getText().toString());
                            String iString = intervalSpinner.getSelectedItem().toString();
                            int tInt = Integer.parseInt(timePeriodText.getText().toString());
                            String tString = timePeriodSpinner.getSelectedItem().toString();
                            globalHandler.dataLoggingHandler.saveDataLogDetails(getActivity(), iInt, iString, tInt, tString);

                            int intervalsT = Integer.valueOf(intervalString);
                            // in seconds
                            int interval = 0;

                            String temp = intervalSpinner.getSelectedItem().toString();
                            interval = timeToSeconds(temp);
                            interval = interval / intervalsT;

                            if (interval != 0) {
                                int timePeriodT = Integer.valueOf(timerPeriodString);
                                // in seconds
                                int timePeriod = 0;
                                temp = timePeriodSpinner.getSelectedItem().toString();
                                timePeriod = timeToSeconds(temp);
                                timePeriod = timePeriodT * timePeriod;
                                int sample = timePeriod / interval;

                                dialogRecordListener.onRecordData(name, interval, sample);

                                DataLoggingConfirmation dataLoggingConfirmation = DataLoggingConfirmation.newInstance((Serializable) dismissDialogListener, buttonDrawableId);
                                dataLoggingConfirmation.show(getFragmentManager(), "tag");

                                this.dismiss();
                            } else {
                                // TODO - warn the user that he or she cannot record data points more than once per second
                                this.dismiss();
                            }
                        } else {
                            IsRecordingDialog isRecordingDialog = IsRecordingDialog.newInstance(this);
                            isRecordingDialog.show(getFragmentManager(), "tag");
                            this.dismiss();
                        }
                    }
                } else {
                    timePeriodText.setError(getString(R.string.this_field_cannot_be_blank));
                }
            } else {
                intervalsText.setError(getString(R.string.this_field_cannot_be_blank));
            }
        } else {
            dataSetNameText.setError(getString(R.string.this_field_cannot_be_blank));
        }
    }


    @Override
    public void onDataSetPointsPopulated(boolean isSuccess) {
        isWaitingForResponse = false;
        if (isSuccess) {
            isLogging = globalHandler.dataLoggingHandler.isLogging();
        }
    }


    public boolean getIsLogging() { return this.isLogging; }
    public boolean getIsWaitingForResponse() { return this.isWaitingForResponse; }


    public interface DialogRecordListener {
        public void onRecordData(String name, int interval, int sample);
    }
}
