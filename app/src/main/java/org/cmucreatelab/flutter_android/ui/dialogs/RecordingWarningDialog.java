package org.cmucreatelab.flutter_android.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Steve on 2/24/2017.
 */

public abstract class RecordingWarningDialog extends BaseDataLoggingDialog {

    protected static final String NAME_KEY = "name_key";
    protected static final String TIMES_KEY = "times_key";
    protected static final String TIME_KEY = "time_key";
    protected static final String FOR_TIMES_KEY = "for_times_key";
    protected static final String FOR_TIME_KEY = "for_time_key";

    private WarningButtonListener warningButtonListener;
    protected Button buttonOk;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        GlobalHandler globalHandler = GlobalHandler.getInstance(getActivity());
        String dataLogName = getArguments().getString(NAME_KEY);
        String intervalsString = getArguments().getString(TIME_KEY);
        String timePeriodString = getArguments().getString(FOR_TIME_KEY);
        int intervals = getArguments().getInt(TIMES_KEY);
        int timePeriod = getArguments().getInt(FOR_TIMES_KEY);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_recording_warning, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ButterKnife.bind(this, view);

        TextView title = (TextView) view.findViewById(R.id.text_record_title);
        TextView flutterName = (TextView) view.findViewById(R.id.text_warning_flutter);
        TextView textDataLogName = (TextView) view.findViewById(R.id.text_warning_data_log);
        TextView intervalsText = (TextView) view.findViewById(R.id.edit_number_of_intervals);
        TextView intervalSpinner = (TextView) view.findViewById(R.id.spinner_dropdown_interval);
        TextView timePeriodText = (TextView) view.findViewById(R.id.edit_time_period);
        TextView timePeriodSpinner = (TextView) view.findViewById(R.id.spinner_dropdown_time);

        title.setText(getString(R.string.data_recording));
        flutterName.setText(globalHandler.sessionHandler.getSession().getFlutter().getName() + " " +  getString(R.string.is_currently_recording));
        textDataLogName.setText("\"" + dataLogName + "\"" + " " + getString(R.string.data_set));
        intervalsText.setText(String.valueOf(intervals));
        intervalSpinner.setText(intervalsString);
        timePeriodText.setText(String.valueOf(timePeriod));
        timePeriodSpinner.setText(timePeriodString);

        buttonOk = (Button) view.findViewById(R.id.button_ok);

        return builder.create();
    }


    @OnClick(R.id.button_ok)
    public void onClickOk() {
        warningButtonListener.onButtonOk();
    }

    @OnClick(R.id.button_cancel_recording)
    public void onClickCancel() {
        warningButtonListener.onCancelRecording();
    }


    public void registerWarningListener(WarningButtonListener warningButtonListener) {
        this.warningButtonListener = warningButtonListener;
    }


    public interface WarningButtonListener {
        public void onCancelRecording();
        public void onButtonOk();
    }

}
