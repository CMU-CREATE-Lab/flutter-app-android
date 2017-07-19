package org.cmucreatelab.flutter_android.ui.dialogs.sensors_tab;

import org.cmucreatelab.flutter_android.ui.dialogs.record_data_wizard.ReviewRecordingDialog;

import java.io.Serializable;

/**
 * Created by Steve on 1/9/2017.
 */
public class RecordDataSensorDialog extends ReviewRecordingDialog implements Serializable {


    /*public static RecordDataSensorDialog newInstance(Serializable serializable, int buttonDrawableId) {
        RecordDataSensorDialog recordDataSensorDialog = new RecordDataSensorDialog();

        Bundle args = new Bundle();
        args.putSerializable(RECORD_KEY, serializable);
        args.putSerializable(DISMISS_KEY, serializable);
        //args.putInt(BUTTON_KEY, buttonDrawableId);
        recordDataSensorDialog.setArguments(args);

        return recordDataSensorDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_sensors_record_data, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ButterKnife.bind(this, view);

        dataSetNameText = (EmojiconEditText) view.findViewById(R.id.edit_data_set_name);
        super.onCreateDialog(savedInstanceState);

        intervalsText = (EditText) view.findViewById(R.id.edit_number_of_intervals);
        intervalSpinner = (EditText) view.findViewById(R.id.edit_dropdown_interval);
        timePeriodText = (EditText) view.findViewById(R.id.edit_time_period);
        timePeriodSpinner = (EditText) view.findViewById(R.id.edit_dropdown_time);

        informationDialog = InformationDialog.newInstance(
                getString(R.string.a_lot_of_data_points),
                getString(R.string.a_lot_of_data_points_details),
                R.drawable.round_blue_button_bottom_right,
                R.drawable.round_blue_button_bottom_left,
                null,
                warningDialogListener
        );
        informationDialog.setCancelable(false);

        return builder.create();
    }*/

}
