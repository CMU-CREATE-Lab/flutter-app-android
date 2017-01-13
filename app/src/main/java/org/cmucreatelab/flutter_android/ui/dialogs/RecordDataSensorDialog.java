package org.cmucreatelab.flutter_android.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.SensorsActivity;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Steve on 1/9/2017.
 */
// TODO - use an abstract class to simplify this class
public class RecordDataSensorDialog extends BaseResizableDialog {

    private DialogRecordDataSensorListener dialogRecordDataSensorListener;

    private EditText dataSetNameText;
    private EditText intervalsText;
    private Spinner intervalSpinner;
    private EditText timePeriodText;
    private Spinner timePeriodSpinner;


    private int timeToSeconds(String time) {
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


    public static RecordDataSensorDialog newInstance(Serializable serializable) {
        RecordDataSensorDialog recordDataSensorDialog = new RecordDataSensorDialog();

        Bundle args = new Bundle();
        args.putSerializable(SensorsActivity.SENSORS_ACTIVITY_KEY, serializable);
        recordDataSensorDialog.setArguments(args);

        return recordDataSensorDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        dialogRecordDataSensorListener = (DialogRecordDataSensorListener) getArguments().getSerializable(SensorsActivity.SENSORS_ACTIVITY_KEY);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_sensors_record_data, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ButterKnife.bind(this, view);

        dataSetNameText = (EditText) view.findViewById(R.id.edit_data_set_name);
        intervalsText = (EditText) view.findViewById(R.id.edit_number_of_intervals);
        intervalSpinner = (Spinner) view.findViewById(R.id.spinner_dropdown_interval);
        timePeriodText = (EditText) view.findViewById(R.id.edit_time_period);
        timePeriodSpinner = (Spinner) view.findViewById(R.id.spinner_dropdown_time);

        intervalSpinner = (Spinner) view.findViewById(R.id.spinner_dropdown_interval);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.time_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intervalSpinner.setAdapter(adapter);

        timePeriodSpinner = (Spinner) view.findViewById(R.id.spinner_dropdown_time);
        adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.times_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timePeriodSpinner.setAdapter(adapter);

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }


    @OnClick(R.id.button_start_recording)
    public void onClickButtonStartRecording() {
        Log.d(Constants.LOG_TAG, "onClickButtonStartRecording");
        String name = dataSetNameText.getText().toString();
        int intervalsT = Integer.valueOf(intervalsText.getText().toString());
        // in seconds
        int interval = 0;

        String temp = intervalSpinner.getSelectedItem().toString();
        interval = timeToSeconds(temp);
        interval = interval / intervalsT;

        int timePeriodT = Integer.valueOf(timePeriodText.getText().toString());
        // in seconds
        int timePeriod = 0;
        temp = timePeriodSpinner.getSelectedItem().toString();
        timePeriod = timeToSeconds(temp);
        timePeriod = timePeriodT * timePeriod;
        int sample = timePeriod / interval;

        Log.d(Constants.LOG_TAG, "RecordDataLoggingDialog - " + name);
        Log.d(Constants.LOG_TAG, "RecordDataLoggingDialog - " + interval);
        Log.d(Constants.LOG_TAG, "RecordDataLoggingDialog - " + sample);

        dialogRecordDataSensorListener.onDataRecord(name, interval, sample);
        this.dismiss();
    }


    public interface DialogRecordDataSensorListener {
        public void onDataRecord(String name, int interval, int sample);
    }

}
