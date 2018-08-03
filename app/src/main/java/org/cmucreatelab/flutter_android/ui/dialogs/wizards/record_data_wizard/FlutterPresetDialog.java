package org.cmucreatelab.flutter_android.ui.dialogs.wizards.record_data_wizard;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.datalogging.DataLogDetails;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialog;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Steve on 7/11/2017.
 */

public class FlutterPresetDialog extends BaseResizableDialog {

    private static final String DATA_LOG_DETAILS_KEY = "data_log_details_key";
    private static final String SERIALIZABLE_KEY = "serializable_key";
    private static final String WIZARD_ENUM_KEY = "wizard_enum_key";
    private static final String REVIEW_ENABLED_KEY = "review_enabled_key";

    private Serializable dismissAndDialogRecordListener;
    private DataLogDetails dataLogDetails;
    private boolean isReviewEnabled;
    private Constants.RECORD_DATA_WIZARD_TYPE wizardType;


    /**
     * @param dataLogDetails  - The details of the recording.
     * @param serializable    - The dismiss and data recording listener
     * @param wizardEnum      - The type of activity the wizard was started on
     * @param isReviewEnabled - Whether the dialog was navigation from the review recording dialog.
     * @return
     */
    public static FlutterPresetDialog newInstance(Serializable dataLogDetails, Serializable serializable, Serializable wizardEnum, boolean isReviewEnabled) {
        FlutterPresetDialog flutterSampleDialog = new FlutterPresetDialog();
        Bundle args = new Bundle();
        args.putSerializable(DATA_LOG_DETAILS_KEY, dataLogDetails);
        args.putSerializable(SERIALIZABLE_KEY, serializable);
        args.putSerializable(WIZARD_ENUM_KEY, wizardEnum);
        args.putBoolean(REVIEW_ENABLED_KEY, isReviewEnabled);
        flutterSampleDialog.setArguments(args);
        return flutterSampleDialog;
    }


    /* OnClick Listeners */


    @OnClick(R.id.button_sixty_times_minute)
    public void onClickButtonSixtyTimesMinute() {
        dataLogDetails.setIntervalInt(60);
        dataLogDetails.setIntervalString("minute");
        dataLogDetails.setTimePeriodInt(1);
        dataLogDetails.setTimePeriodString("minute");
        FlutterNameRecordingDialog flutterNameRecordingDialog = FlutterNameRecordingDialog.newInstance(dataLogDetails, dismissAndDialogRecordListener, wizardType, isReviewEnabled);
        flutterNameRecordingDialog.show(getActivity().getSupportFragmentManager(), "tag");
        dismiss();
    }


    @OnClick(R.id.button_sixty_times_hour)
    public void onClickButtonSixtyTimesHour() {
        dataLogDetails.setIntervalInt(60);
        dataLogDetails.setIntervalString("hour");
        dataLogDetails.setTimePeriodInt(1);
        dataLogDetails.setTimePeriodString("hour");
        FlutterNameRecordingDialog flutterNameRecordingDialog = FlutterNameRecordingDialog.newInstance(dataLogDetails, dismissAndDialogRecordListener, wizardType, isReviewEnabled);
        flutterNameRecordingDialog.show(getActivity().getSupportFragmentManager(), "tag");
        dismiss();
    }


    @OnClick(R.id.button_two_times_hour_day)
    public void onClickButtonTwoTimesHourDay() {
        dataLogDetails.setIntervalInt(2);
        dataLogDetails.setIntervalString("hour");
        dataLogDetails.setTimePeriodInt(1);
        dataLogDetails.setTimePeriodString("day");
        FlutterNameRecordingDialog flutterNameRecordingDialog = FlutterNameRecordingDialog.newInstance(dataLogDetails, dismissAndDialogRecordListener, wizardType, isReviewEnabled);
        flutterNameRecordingDialog.show(getActivity().getSupportFragmentManager(), "tag");
        dismiss();
    }


    @OnClick(R.id.button_choose_own)
    public void onClickChooseOwn() {
        FlutterSampleDialog flutterSampleDialog = FlutterSampleDialog.newInstance(dataLogDetails, dismissAndDialogRecordListener, wizardType, isReviewEnabled);
        flutterSampleDialog.show(getActivity().getSupportFragmentManager(), "tag");
        dismiss();
    }


    @OnClick(R.id.button_cancel)
    public void onClickCancel() {
        dismiss();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_flutter_preset_sample, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);

        dismissAndDialogRecordListener = getArguments().getSerializable(SERIALIZABLE_KEY);
        dataLogDetails = (DataLogDetails) getArguments().getSerializable(DATA_LOG_DETAILS_KEY);
        isReviewEnabled = getArguments().getBoolean(REVIEW_ENABLED_KEY);
        wizardType = (Constants.RECORD_DATA_WIZARD_TYPE) getArguments().getSerializable(WIZARD_ENUM_KEY);

        ButterKnife.bind(this, view);

        flutterAudioPlayer.addAudio(R.raw.a_22);
        flutterAudioPlayer.playAudio();

        return builder.create();
    }


    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(convertDpToPx(400), ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
