package org.cmucreatelab.flutter_android.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.cmucreatelab.flutter_android.R;

import java.io.Serializable;

import butterknife.ButterKnife;

/**
 * Created by Steve on 1/9/2017.
 */
public class RecordDataSensorDialog extends BaseDataLoggingDialog implements Serializable {


    public static RecordDataSensorDialog newInstance(Serializable serializable, int buttonDrawableId) {
        RecordDataSensorDialog recordDataSensorDialog = new RecordDataSensorDialog();

        Bundle args = new Bundle();
        args.putSerializable(RECORD_KEY, serializable);
        args.putSerializable(DISMISS_KEY, serializable);
        args.putInt(BUTTON_KEY, buttonDrawableId);
        recordDataSensorDialog.setArguments(args);

        return recordDataSensorDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

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

        informationDialog = InformationDialog.newInstance(
                getString(R.string.a_lot_of_data_points),
                getString(R.string.a_lot_of_data_points_details),
                R.drawable.round_blue_button_bottom_right,
                R.drawable.round_blue_button_bottom_left,
                warningDialogListener
        );
        informationDialog.setCancelable(false);

        return builder.create();
    }

}
