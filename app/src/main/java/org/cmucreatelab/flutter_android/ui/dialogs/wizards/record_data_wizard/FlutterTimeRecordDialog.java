package org.cmucreatelab.flutter_android.ui.dialogs.wizards.record_data_wizard;

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

public class FlutterTimeRecordDialog extends BaseResizableDialog {

    private static final String DATA_LOG_DETAILS_KEY = "data_log_details_key";
    private static final String SERIALIZABLE_KEY = "serializable_key";
    private static final String WIZARD_ENUM_KEY = "wizard_enum_key";
    private static final String REVIEW_ENABLED_KEY = "review_enabled_key";

    private EditText timePeriodText;
    private Spinner timePeriodSpinner;

    private Serializable dismissAndDialogRecordListener;
    private DataLogDetails dataLogDetails;
    private boolean isReviewEnabled;
    private Constants.RECORD_DATA_WIZARD_TYPE wizardType;

    private final HashMap<String, Integer> TIME_INDEX = new HashMap(){{
        put("minutes", 0);
        put("hours", 1);
        put("days", 2);
        put("weeks", 3);
    }};


    /**
     *
     * @param dataLogDetails - The details of the recording.
     * @param serializable - The dismiss and data recording listener
     * @param wizardEnum - The type of activity the wizard was started on
     * @param isReviewEnabled - Whether the dialog was navigation from the review recording dialog.
     * @return
     */
    public static FlutterTimeRecordDialog newInstance(Serializable dataLogDetails, Serializable serializable, Constants.RECORD_DATA_WIZARD_TYPE wizardEnum, boolean isReviewEnabled) {
        FlutterTimeRecordDialog flutterTimeRecordDialog = new FlutterTimeRecordDialog();
        Bundle args = new Bundle();
        args.putSerializable(DATA_LOG_DETAILS_KEY, dataLogDetails);
        args.putSerializable(SERIALIZABLE_KEY, serializable);
        args.putSerializable(WIZARD_ENUM_KEY, wizardEnum);
        args.putBoolean(REVIEW_ENABLED_KEY, isReviewEnabled);
        flutterTimeRecordDialog.setArguments(args);
        return flutterTimeRecordDialog;
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


    /* OnClick Listeners */


    private View.OnClickListener backOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FlutterSampleDialog flutterSampleDialog = FlutterSampleDialog.newInstance(
                    dataLogDetails, dismissAndDialogRecordListener, wizardType, false
            );
            flutterSampleDialog.show(getActivity().getSupportFragmentManager(), "tag");
            dismiss();
        }
    };


    private View.OnClickListener nextOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (testTimerPeriod()) {
                dataLogDetails.setTimePeriodInt(Integer.parseInt(timePeriodText.getText().toString()));
                dataLogDetails.setTimePeriodString(timePeriodSpinner.getSelectedItem().toString());
                if (!isReviewEnabled) {
                    FlutterNameRecordingDialog flutterNameRecordingDialog = FlutterNameRecordingDialog.newInstance(
                            dataLogDetails, dismissAndDialogRecordListener, wizardType, isReviewEnabled
                    );
                    flutterNameRecordingDialog.show(getActivity().getSupportFragmentManager(), "tag");
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
        final View view = inflater.inflate(R.layout.dialog_flutter_record_time, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);

        dismissAndDialogRecordListener = getArguments().getSerializable(SERIALIZABLE_KEY);
        dataLogDetails = (DataLogDetails) getArguments().getSerializable(DATA_LOG_DETAILS_KEY);
        wizardType = (Constants.RECORD_DATA_WIZARD_TYPE) getArguments().getSerializable(WIZARD_ENUM_KEY);
        isReviewEnabled = getArguments().getBoolean(REVIEW_ENABLED_KEY);

        int backButtonId = Constants.WIZARD_TYPE_TO_CANCEL_BACKGROUND.get(wizardType);
        int backButtonTextId = Constants.WIZARD_TYPE_TO_CANCEL_TEXT.get(wizardType);
        int nextButtonId = Constants.WIZARD_TYPE_TO_NEXT_BACKGROUND.get(wizardType);

        timePeriodText = (EditText) view.findViewById(R.id.edit_time_period);
        timePeriodSpinner = (Spinner) view.findViewById(R.id.spinner_dropdown_time);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.times_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timePeriodSpinner.setAdapter(adapter);

        timePeriodText.setText(dataLogDetails.getTimePeriodInt() > 0 ? String.valueOf(dataLogDetails.getTimePeriodInt()) : "");
        timePeriodSpinner.setSelection(TIME_INDEX.containsKey(dataLogDetails.getTimePeriodString()) ? TIME_INDEX.get(dataLogDetails.getTimePeriodString()) : 0);

        Button back = (Button) view.findViewById(R.id.button_back_cancel);
        Button next = (Button) view.findViewById(R.id.button_next);
        back.setText(getString(R.string.back));
        back.setTextColor(getResources().getColor(backButtonTextId));
        next.setText(isReviewEnabled ? getString(R.string.review_recording) : getString(R.string.next));
        back.setBackground(ContextCompat.getDrawable(getActivity(), backButtonId));
        next.setBackground(ContextCompat.getDrawable(getActivity(), nextButtonId));
        back.setOnClickListener(backOnClick);
        next.setOnClickListener(nextOnClick);

        flutterAudioPlayer.addAudio(R.raw.audio_24);
        flutterAudioPlayer.playAudio();

        return builder.create();
    }
}
