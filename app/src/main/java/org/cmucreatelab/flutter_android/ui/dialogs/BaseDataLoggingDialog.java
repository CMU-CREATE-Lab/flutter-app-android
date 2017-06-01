package org.cmucreatelab.flutter_android.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.datalogging.DataSet;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.datalogging.DataLoggingHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.FileHandler;

import java.io.Serializable;

import butterknife.OnClick;

/**
 * Created by Steve on 1/17/2017.
 */
public abstract class BaseDataLoggingDialog extends BaseResizableDialog implements Serializable, DataLoggingHandler.DataSetPointsListener,
        EmojiconGridFragment.OnEmojiconClickedListener,
        EmojiconsFragment.OnEmojiconBackspaceClickedListener{

    protected static final String RECORD_KEY = "record_key";
    protected static final String DISMISS_KEY = "dismiss_key";
    protected static final String BUTTON_KEY = "button_key";

    private static final int MAX_BITS = 15;

    private GlobalHandler globalHandler;
    private boolean isLogging;
    private boolean isWaitingForResponse;

    private DialogRecordListener dialogRecordListener;
    private DismissDialogListener dismissDialogListener;
    private int buttonDrawableId;

    protected int finalInterval, finalSample;

    protected InformationDialog informationDialog;
    protected EmojiconEditText dataSetNameText;
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


    private boolean testName() {
        boolean result = true;

        String name = dataSetNameText.getText().toString();
        for (DataSet dataSet : FileHandler.loadDataSetsFromFile(globalHandler)) {
            if (name.equals(dataSet.getDataName())) {
                dataSetNameText.setError("This name has already been used, use a different name.");
                result = false;
            }
        }
        if (name.equals(globalHandler.dataLoggingHandler.getDataName())) {
            dataSetNameText.setError("This name has already been used, use a different name.");
            result = false;
        }
        if(name.equals("")) {
            dataSetNameText.setError(getString(R.string.empty_data_name));
            result = false;
        }
        if (name.getBytes().length > MAX_BITS) {
            dataSetNameText.setError(getString(R.string.too_many_bits));
            result = false;
        }

        return result;
    }


    private boolean testInterval() {
        boolean result = true;
        String intervalString = intervalsText.getText().toString();
        if (intervalString.matches("") || intervalString.matches("0")) {
            intervalsText.setError(getString(R.string.this_field_cannot_be_blank_or_zero));
            result = false;
        }
        return result;
    }


    private boolean testTimerPeriod() {
        boolean result = true;
        String timePeriodString = timePeriodText.getText().toString();
        if (timePeriodString.equals("") || timePeriodString.equals("0")) {
            timePeriodText.setError(getString(R.string.this_field_cannot_be_blank_or_zero));
            result = false;
        }
        return result;
    }


    protected InformationDialog.DismissAndCancelWarningListener warningDialogListener = new InformationDialog.DismissAndCancelWarningListener() {
        @Override
        public void onPositiveButton() {
            Log.d(Constants.LOG_TAG, "this is getting dismissed");
            dialogRecordListener.onRecordData(dataSetNameText.getText().toString(), finalInterval, finalSample);
            DataLoggingConfirmation dataLoggingConfirmation = DataLoggingConfirmation.newInstance((Serializable) dismissDialogListener, buttonDrawableId);
            dataLoggingConfirmation.show(getFragmentManager(), "tag");
            dismiss();
        }
    };


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

        dataSetNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().getBytes().length > MAX_BITS){
                    dataSetNameText.setError(getString(R.string.too_many_bits));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return dialog;
    }


    @OnClick(R.id.button_start_recording)
    public void onClickButtonStartRecording() {
        Log.d(Constants.LOG_TAG, "onClickButtonStartRecording");
        boolean isGoodName = testName();
        boolean isGoodInterval = testInterval();
        boolean isGoodTimePeriod = testTimerPeriod();

        if (isGoodName && isGoodInterval && isGoodTimePeriod && !isWaitingForResponse && !isLogging) {
            GlobalHandler globalHandler = GlobalHandler.getInstance(getActivity());
            int iInt = Integer.valueOf(intervalsText.getText().toString());
            String iString = intervalSpinner.getSelectedItem().toString();
            int tInt = Integer.parseInt(timePeriodText.getText().toString());
            String tString = timePeriodSpinner.getSelectedItem().toString();
            globalHandler.dataLoggingHandler.saveDataLogDetails(getActivity(), iInt, iString, tInt, tString);

            int inter = 0;

            String temp = intervalSpinner.getSelectedItem().toString();
            inter = timeToSeconds(temp);
            inter = inter / iInt;

            if (inter != 0) {
                int timePeriodT = Integer.valueOf(timePeriodText.getText().toString());
                // in seconds
                int timePeriod = 0;
                temp = timePeriodSpinner.getSelectedItem().toString();
                timePeriod = timeToSeconds(temp);
                timePeriod = timePeriodT * timePeriod;
                finalSample = timePeriod / inter;
                finalInterval = inter;

                if (finalSample < 200) {
                    dialogRecordListener.onRecordData(dataSetNameText.getText().toString(), finalInterval, finalSample);
                    DataLoggingConfirmation dataLoggingConfirmation = DataLoggingConfirmation.newInstance((Serializable) dismissDialogListener, buttonDrawableId);
                    dataLoggingConfirmation.show(getFragmentManager(), "tag");
                    this.dismiss();
                } else {
                    Log.d(Constants.LOG_TAG, "about to show the warning dialog");
                    informationDialog.show(getFragmentManager(), "tag");
                }
            } else {
                intervalsText.setError(getString(R.string.please_enter_60_or_less));
            }
        }
    }


    @Override
    public void onDataSetPointsPopulated(boolean isSuccess) {
        isWaitingForResponse = false;
        if (isSuccess) {
            isLogging = globalHandler.dataLoggingHandler.isLogging();
        }
    }


    @Override
    public void onEmojiconClicked(Emojicon emojicon) {

        EmojiconsFragment.input(dataSetNameText, emojicon);
    }


    @Override
    public void onEmojiconBackspaceClicked(View view) {

        EmojiconsFragment.backspace(dataSetNameText);
    }


    public boolean getIsLogging() { return this.isLogging; }
    public boolean getIsWaitingForResponse() { return this.isWaitingForResponse; }


    public interface DialogRecordListener {
        public void onRecordData(String name, int interval, int sample);
    }
}
