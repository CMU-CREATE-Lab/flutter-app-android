package org.cmucreatelab.flutter_android.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.SensorsActivity;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.SensorsTab.RecordDataSensorDialog;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Steve on 2/15/2017.
 */

public class IsRecordingDialog extends BaseResizableDialog {

    private GlobalHandler globalHandler;


    public static IsRecordingDialog newInstance(Serializable serializable) {
        IsRecordingDialog isRecordingDialog = new IsRecordingDialog();

        Bundle args = new Bundle();
        args.putSerializable(SensorsActivity.SENSORS_ACTIVITY_KEY, serializable);
        isRecordingDialog.setArguments(args);

        return isRecordingDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_is_recording, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ButterKnife.bind(this, view);
        globalHandler = GlobalHandler.getInstance(getActivity());

        // Get the correct color buttons
        if (((BaseDataLoggingDialog) getArguments().getSerializable(SensorsActivity.SENSORS_ACTIVITY_KEY)).getClass().getSimpleName().equals(RecordDataSensorDialog.class.getSimpleName())) {
            (view.findViewById(R.id.button_stop_recording)).setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_blue_button_bottom_left));
            (view.findViewById(R.id.button_keep_recording)).setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_blue_button_bottom_right));
        } else {
            (view.findViewById(R.id.button_stop_recording)).setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_orange_button_bottom_left));
            (view.findViewById(R.id.button_keep_recording)).setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_orange_button_bottom_right));
        }

        return builder.create();
    }


    @OnClick(R.id.button_stop_recording)
    public void onClickStopRecording() {
        Log.d(Constants.LOG_TAG, "IsRecordingDialog.onClickStopRecording");
        globalHandler.dataLoggingHandler.stopRecording();
        this.dismiss();
    }


    @OnClick(R.id.button_keep_recording)
    public void onClickKeepRecording() {
        Log.d(Constants.LOG_TAG, "IsRecordingDialog.onClickKeepRecording");
        this.dismiss();
    }

}
