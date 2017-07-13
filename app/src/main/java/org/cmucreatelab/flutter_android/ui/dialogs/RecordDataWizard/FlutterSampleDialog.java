package org.cmucreatelab.flutter_android.ui.dialogs.RecordDataWizard;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.datalogging.DataLogDetails;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialog;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Steve on 7/11/2017.
 */

public class FlutterSampleDialog extends BaseResizableDialog {

    private static final String DATA_LOG_DETAILS_KEY = "data_log_details_key";
    private static final String SERIALIZABLE_KEY = "serializable_key";
    private static final String WIZARD_ENUM_KEY = "wizard_enum_key";
    private static final String REVIEW_ENABLED_KEY = "review_enabled_key";

    private EditText intervalsText;
    private Spinner intervalSpinner;
    private Serializable dismissAndDialogRecordListener;
    private DataLogDetails dataLogDetails;
    private boolean isReviewEnabled;
    private Constants.RECORD_DATA_WIZARD_TYPE wizardType;

    private final HashMap<String, Integer> TIME_INDEX = new HashMap(){{
        put("minute", 0);
        put("hour", 1);
        put("day", 2);
        put("week", 3);
    }};


    /**
     *
     * @param dataLogDetails - The details of the recording.
     * @param serializable - The dismiss and data recording listener
     * @param wizardEnum - The type of activity the wizard was started on
     * @param isReviewEnabled - Whether the dialog was navigation from the review recording dialog.
     * @return
     */
    public static FlutterSampleDialog newInstance(Serializable dataLogDetails, Serializable serializable, Serializable wizardEnum, boolean isReviewEnabled) {
        FlutterSampleDialog flutterSampleDialog = new FlutterSampleDialog();
        Bundle args = new Bundle();
        args.putSerializable(DATA_LOG_DETAILS_KEY, dataLogDetails);
        args.putSerializable(SERIALIZABLE_KEY, serializable);
        args.putSerializable(WIZARD_ENUM_KEY, wizardEnum);
        args.putBoolean(REVIEW_ENABLED_KEY, isReviewEnabled);
        flutterSampleDialog.setArguments(args);
        return  flutterSampleDialog;
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


    /* OnClick Listeners */


    private View.OnClickListener cancelOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dismiss();
        }
    };


    private View.OnClickListener nextOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (testInterval()) {
                if (!isReviewEnabled) {
                    dataLogDetails.setIntervalInt(Integer.parseInt(intervalsText.getText().toString()));
                    dataLogDetails.setIntervalString(intervalSpinner.getSelectedItem().toString());
                    FlutterTimeRecordDialog flutterTimeRecordDialog = FlutterTimeRecordDialog.newInstance(
                            dataLogDetails, dismissAndDialogRecordListener, wizardType, isReviewEnabled
                    );
                    flutterTimeRecordDialog.show(getActivity().getSupportFragmentManager(), "tag");
                    dismiss();
                } else {
                    ReviewRecordingDialog reviewRecordingDialog = ReviewRecordingDialog.newInstance(dataLogDetails, dismissAndDialogRecordListener, wizardType);
                    reviewRecordingDialog.show(getActivity().getSupportFragmentManager(), "tag");
                    dismiss();
                }
            }
        }
    };

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_flutter_sample, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);

        dismissAndDialogRecordListener = getArguments().getSerializable(SERIALIZABLE_KEY);
        dataLogDetails = (DataLogDetails) getArguments().getSerializable(DATA_LOG_DETAILS_KEY);
        isReviewEnabled = getArguments().getBoolean(REVIEW_ENABLED_KEY);
        wizardType = (Constants.RECORD_DATA_WIZARD_TYPE) getArguments().getSerializable(WIZARD_ENUM_KEY);

        int cancelButtonId = Constants.WIZARD_TYPE_TO_CANCEL_BACKGROUND.get(wizardType);
        int cancelButtonTextId = Constants.WIZARD_TYPE_TO_CANCEL_TEXT.get(wizardType);
        int nextButtonId = Constants.WIZARD_TYPE_TO_NEXT_BACKGROUND.get(wizardType);

        intervalsText = (EditText) view.findViewById(R.id.edit_number_of_intervals);
        intervalSpinner = (Spinner) view.findViewById(R.id.spinner_dropdown_interval);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.time_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intervalSpinner.setAdapter(adapter);

        intervalsText.setText(dataLogDetails.getIntervalInt() > 0 ? String.valueOf(dataLogDetails.getIntervalInt()) : "");
        intervalSpinner.setSelection(TIME_INDEX.containsKey(dataLogDetails.getIntervalString()) ? TIME_INDEX.get(dataLogDetails.getIntervalString()) : 0);

        Button cancel = (Button) view.findViewById(R.id.button_back_cancel);
        Button next = (Button) view.findViewById(R.id.button_next);
        cancel.setText(getString(R.string.cancel));
        cancel.setTextColor(getResources().getColor(cancelButtonTextId));
        next.setText(isReviewEnabled ? getString(R.string.review_recording) : getString(R.string.next));
        cancel.setBackground(ContextCompat.getDrawable(getActivity(), cancelButtonId));
        next.setBackground(ContextCompat.getDrawable(getActivity(), nextButtonId));
        cancel.setOnClickListener(cancelOnClick);
        next.setOnClickListener(nextOnClick);

        return builder.create();
    }
}
