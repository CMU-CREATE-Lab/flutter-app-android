package org.cmucreatelab.flutter_android.ui.dialogs.wizards.record_data_wizard;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.datalogging.DataLogDetails;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.datalogging.DataLoggingHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.DismissDialogListener;
import org.cmucreatelab.flutter_android.ui.dialogs.error_dialogs.DataLogErrorDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.error_dialogs.TooManyDataPointsErrorDialog;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Steve on 1/17/2017.
 *
 * ReviewRecordingDialog
 *
 * A dialog for the final step of the recording wizard to confirm the user choices.
 */
public class ReviewRecordingDialog extends BaseResizableDialog implements Serializable, DataLoggingHandler.DataSetPointsListener,
        EmojiconGridFragment.OnEmojiconClickedListener,
        EmojiconsFragment.OnEmojiconBackspaceClickedListener{

    private static final String DATA_LOG_DETAILS_KEY = "data_log_details_key";
    private static final String RECORD_KEY = "record_key";
    private static final String DISMISS_KEY = "dismiss_key";
    private static final String WIZARD_ENUM_KEY = "wizard_enum_key";

    private GlobalHandler globalHandler;
    private boolean isLogging;
    private boolean isWaitingForResponse;

    private DialogRecordListener dialogRecordListener;
    private DismissDialogListener dismissDialogListener;
    private Serializable serializable;
    private Constants.RECORD_DATA_WIZARD_TYPE wizardType;

    private int buttonDrawableId;

    private int finalInterval, finalSample;

    private DataLogDetails dataLogDetails;
    private EmojiconEditText dataSetNameText;
    private EditText intervalsText;
    private EditText intervalSpinner;
    private EditText timePeriodText;
    private EditText timePeriodSpinner;


    public static ReviewRecordingDialog newInstance(Serializable dataLogDetails, Serializable serializable, Constants.RECORD_DATA_WIZARD_TYPE wizardType) {
        ReviewRecordingDialog reviewRecordingDialog = new ReviewRecordingDialog();

        Bundle args = new Bundle();
        args.putSerializable(DATA_LOG_DETAILS_KEY, dataLogDetails);
        args.putSerializable(RECORD_KEY, serializable);
        args.putSerializable(DISMISS_KEY, serializable);
        args.putSerializable(WIZARD_ENUM_KEY, wizardType);
        reviewRecordingDialog.setArguments(args);

        return reviewRecordingDialog;
    }


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


    /* Listeners */


    protected TooManyDataPointsErrorDialog.DismissWarningListener warningDialogListener = new TooManyDataPointsErrorDialog.DismissWarningListener() {
        @Override
        public void onOkButton() {
            Log.d(Constants.LOG_TAG, "this is getting dismissed");
            dialogRecordListener.onRecordData(dataSetNameText.getText().toString(), finalInterval, finalSample);
            DataLoggingConfirmation dataLoggingConfirmation = DataLoggingConfirmation.newInstance((Serializable) dismissDialogListener, wizardType);
            dataLoggingConfirmation.show(getFragmentManager(), "tag");
            dismiss();
        }
    };


    protected View.OnClickListener intervalClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FlutterSampleDialog flutterSampleDialog = FlutterSampleDialog.newInstance(
                    dataLogDetails, serializable, wizardType, true
            );
            flutterSampleDialog.show(getActivity().getSupportFragmentManager(), "tag");
            dismiss();
        }
    };


    protected View.OnClickListener timePeriodClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FlutterTimeRecordDialog flutterTimeRecordDialog = FlutterTimeRecordDialog.newInstance(
                    dataLogDetails, serializable, wizardType, true
            );
            flutterTimeRecordDialog.show(getActivity().getSupportFragmentManager(), "tag");
            dismiss();
        }
    };


    protected View.OnClickListener dataNameClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FlutterNameRecordingDialog flutterNameRecordingDialog = FlutterNameRecordingDialog.newInstance(
                    dataLogDetails, serializable, wizardType, true
            );
            flutterNameRecordingDialog.show(getActivity().getSupportFragmentManager(), "tag");
            dismiss();
        }
    };


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_record_data, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ButterKnife.bind(this, view);

        serializable = getArguments().getSerializable(RECORD_KEY);
        dialogRecordListener = (DialogRecordListener) getArguments().getSerializable(RECORD_KEY);
        dismissDialogListener = (DismissDialogListener) getArguments().getSerializable(DismissDialogListener.DISMISS_KEY);
        wizardType = (Constants.RECORD_DATA_WIZARD_TYPE) getArguments().getSerializable(WIZARD_ENUM_KEY);
        buttonDrawableId = Constants.WIZARD_TYPE_TO_REVIEW_BUTTON.get(wizardType);

        globalHandler = GlobalHandler.getInstance(getActivity());
        globalHandler.dataLoggingHandler.populatePointsAvailable(this);
        isLogging = false;
        isWaitingForResponse = true;

        dataLogDetails = (DataLogDetails) getArguments().getSerializable(DATA_LOG_DETAILS_KEY);
        dataSetNameText = (EmojiconEditText) view.findViewById(R.id.edit_data_set_name);
        intervalsText = (EditText) view.findViewById(R.id.edit_number_of_intervals);
        intervalSpinner = (EditText) view.findViewById(R.id.edit_dropdown_interval);
        timePeriodText = (EditText) view.findViewById(R.id.edit_time_period);
        timePeriodSpinner = (EditText) view.findViewById(R.id.edit_dropdown_time);
        view.findViewById(R.id.button_start_recording).setBackground(ContextCompat.getDrawable(getActivity(), buttonDrawableId));

        dataSetNameText.setOnClickListener(dataNameClicked);
        view.findViewById(R.id.linear_interval_container).setOnClickListener(intervalClicked);
        intervalsText.setOnClickListener(intervalClicked);
        intervalSpinner.setOnClickListener(intervalClicked);
        view.findViewById(R.id.linear_time_period_container).setOnClickListener(timePeriodClicked);
        timePeriodText.setOnClickListener(timePeriodClicked);
        timePeriodSpinner.setOnClickListener(timePeriodClicked);

        dataSetNameText.setText(dataLogDetails.getDataLogName());
        intervalsText.setText(String.valueOf(dataLogDetails.getIntervalInt()));
        intervalSpinner.setText(dataLogDetails.getIntervalString());
        timePeriodText.setText(String.valueOf(dataLogDetails.getTimePeriodInt()));
        timePeriodSpinner.setText(dataLogDetails.getTimePeriodString());

        dataSetNameText.setKeyListener(null);
        intervalsText.setKeyListener(null);
        intervalSpinner.setKeyListener(null);
        timePeriodText.setKeyListener(null);
        timePeriodSpinner.setKeyListener(null);

        return builder.create();
    }


    @OnClick(R.id.button_start_recording)
    public void onClickButtonStartRecording() {
        Log.d(Constants.LOG_TAG, "onClickButtonStartRecording");

        GlobalHandler globalHandler = GlobalHandler.getInstance(getActivity());
        dataLogDetails = (DataLogDetails) getArguments().getSerializable(DATA_LOG_DETAILS_KEY);
        int iInt = dataLogDetails.getIntervalInt();
        String iString = dataLogDetails.getIntervalString();
        int tInt = dataLogDetails.getTimePeriodInt();
        String tString = dataLogDetails.getTimePeriodString();
        globalHandler.dataLoggingHandler.saveDataLogDetails(getActivity(), dataLogDetails);

        int inter = 0;

        String temp = iString;
        inter = timeToSeconds(temp);
        inter = inter / iInt;

        if (inter != 0) {
            int timePeriodT = tInt;
            // in seconds
            int timePeriod = 0;
            temp = tString;
            timePeriod = timeToSeconds(temp);
            timePeriod = timePeriodT * timePeriod;
            finalSample = timePeriod / inter;
            finalInterval = inter;

            if (finalSample < 200) {
                dialogRecordListener.onRecordData(dataSetNameText.getText().toString(), finalInterval, finalSample);
                DataLoggingConfirmation dataLoggingConfirmation = DataLoggingConfirmation.newInstance((Serializable) dismissDialogListener, wizardType);
                dataLoggingConfirmation.show(getFragmentManager(), "tag");
                this.dismiss();
            } else {
                Log.d(Constants.LOG_TAG, "about to show the warning dialog");
                TooManyDataPointsErrorDialog tooManyDataPointsErrorDialog = TooManyDataPointsErrorDialog.newInstance(warningDialogListener);
                tooManyDataPointsErrorDialog.show(getFragmentManager(), "tag");
            }
        }
        else
        {
            DataLogErrorDialog dataLogErrorDialog = DataLogErrorDialog.newInstance(DataLogErrorDialog.DataLogErrorTypes.TOO_MUCH_SAMPLES);
            dataLogErrorDialog.show(getFragmentManager(), "tag");
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