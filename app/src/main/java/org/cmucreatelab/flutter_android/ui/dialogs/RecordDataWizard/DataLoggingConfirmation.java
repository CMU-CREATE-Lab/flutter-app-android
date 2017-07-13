package org.cmucreatelab.flutter_android.ui.dialogs.RecordDataWizard;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.datalogging.DataLogDetails;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.DismissDialogListener;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Steve on 2/27/2017.
 */

public class DataLoggingConfirmation extends BaseResizableDialog {

    private static final String WIZARD_ENUM_KEY = "wizard_enum_key";
    private static final String DISMISS_KEY = "dismiss_key";

    private GlobalHandler globalHandler;
    private Button buttonOk;
    private DismissDialogListener dismissDialogListener;
    private Constants.RECORD_DATA_WIZARD_TYPE wizardType;


    public static DataLoggingConfirmation newInstance(Serializable serializable, Constants.RECORD_DATA_WIZARD_TYPE wizardType) {
        DataLoggingConfirmation dataLoggingConfirmation = new DataLoggingConfirmation();
        Bundle args = new Bundle();
        args.putSerializable(DISMISS_KEY, serializable);
        args.putSerializable(WIZARD_ENUM_KEY, wizardType);
        dataLoggingConfirmation.setArguments(args);
        return  dataLoggingConfirmation;
    }


    private static int timeToSeconds(String time) {
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


    private static String ordinal(int i) {
        String[] sufixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + sufixes[i % 10];

        }
    }


    private static String month(int month) {
        String result = "";

        switch (month) {
            case 0:
                result = "January";
                break;
            case 1:
                result = "February";
                break;
            case 2:
                result = "March";
                break;
            case 3:
                result = "April";
                break;
            case 4:
                result = "May";
                break;
            case 5:
                result = "June";
                break;
            case 6:
                result = "July";
                break;
            case 7:
                result = "August";
                break;
            case 8:
                result = "September";
                break;
            case 9:
                result = "October";
                break;
            case 10:
                result = "November";
                break;
            case 11:
                result = "December";
        }

        return result;
    }


    private static String morningOrNight(int amOrPm) {
        String result = "";

        if (amOrPm == Calendar.AM) {
            result = "am";
        }
        else {
            result = "pm";
        }

        return  result;
    }


    private static String hour(int hour) {
        String result = "";

        if (hour == 0) {
            hour = 12;
        }
        result = String.valueOf(hour);

        return result;
    }


    private static String minute(int minute) {
        String result = "";

        if (minute < 10) {
            result = "0" + minute;
        } else {
            result = String.valueOf(minute);
        }

        return result;
    }


    private String populateMessage(DataLogDetails dataLogDetails) {
        int intervalsT = dataLogDetails.getIntervalInt();
        // in seconds
        int interval = 0;

        String temp = dataLogDetails.getIntervalString();
        interval = timeToSeconds(temp);
        interval = interval / intervalsT;

        int timePeriodT = dataLogDetails.getTimePeriodInt();
        // in seconds
        int timePeriod = 0;
        temp = dataLogDetails.getTimePeriodString();
        timePeriod = timeToSeconds(temp);
        timePeriod = timePeriodT * timePeriod;
        int sample = timePeriod / interval;

        long timeInSecondsToElapse = sample * interval;
        long currentTimeInMilliseconds = new Date().getTime();
        long endingTime = (timeInSecondsToElapse*1000) + currentTimeInMilliseconds;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(endingTime);

        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int amOrPm = calendar.get(Calendar.AM_PM);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        StringBuilder builder = new StringBuilder();
        builder.append(
                "Your Flutter will record " + String.valueOf(sample) + " data points and finish at "
                        + hour(hour) + ":" + minute(minute) + morningOrNight(amOrPm) + " on " + month(month) + " " + ordinal(day) + "."
        );

        return builder.toString();
    }


    // OnClickListeners


    private View.OnClickListener buttonOkListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(Constants.LOG_TAG, "DataLoggingConfirmation.onClickButtonOk");
            dismiss();
        }
    };


    private View.OnClickListener buttonCancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(Constants.LOG_TAG, "DataLoggingConfirmation.onClickButtonCancel");
            GlobalHandler.getInstance(getActivity()).dataLoggingHandler.stopRecording();
            if (wizardType == Constants.RECORD_DATA_WIZARD_TYPE.DATA_LOGS_TAB) {
                getActivity().findViewById(R.id.data_recording_spinner).setVisibility(View.INVISIBLE);
            }
            dismiss();
        }
    };


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        dismissDialogListener = (DismissDialogListener) getArguments().getSerializable(DISMISS_KEY);
        wizardType = (Constants.RECORD_DATA_WIZARD_TYPE) getArguments().getSerializable(WIZARD_ENUM_KEY);

        globalHandler = GlobalHandler.getInstance(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_record_data_confirmation, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        buttonOk = (Button) view.findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(buttonOkListener);
        buttonOk.setBackground(ContextCompat.getDrawable(getActivity(), Constants.WIZARD_TYPE_TO_CONFIRM_BUTTON.get(wizardType)));
        Button buttonCancel = (Button) view.findViewById(R.id.button_cancel);
        buttonCancel.setBackground(ContextCompat.getDrawable(getActivity(), Constants.WIZARD_TYPE_TO_CANCEL_BACKGROUND.get(wizardType)));
        buttonCancel.setTextColor(getResources().getColor(Constants.WIZARD_TYPE_TO_CANCEL_TEXT.get(wizardType)));
        buttonCancel.setOnClickListener(buttonCancelListener);

        String message = populateMessage(globalHandler.dataLoggingHandler.loadDataLogDetails(getActivity()));
        ((TextView) view.findViewById(R.id.text_time_details)).setText(message);

        return builder.create();
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        dismissDialogListener.onDialogDismissed();
    }
}
