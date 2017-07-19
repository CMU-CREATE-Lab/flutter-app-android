package org.cmucreatelab.flutter_android.ui.dialogs.sensors_tab;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.static_classes.FlutterProtocol;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialog;

import java.util.Calendar;

/**
 * Created by Steve on 7/11/2017.
 */

public class DataSnapshotDialog extends BaseResizableDialog {

    private static final String SENSOR_1_VALUE_KEY = "sensor_1_value_key";
    private static final String SENSOR_2_VALUE_KEY = "sensor_2_value_key";
    private static final String SENSOR_3_VALUE_KEY = "sensor_3_value_key";
    private static final String SENSOR_1_KEY = "sensor_1_key";
    private static final String SENSOR_2_KEY = "sensor_2_key";
    private static final String SENSOR_3_KEY = "sensor_3_key";
    private static final String UNIX_TIME_KEY = "unix_time_key";

    public static DataSnapshotDialog newInstance(int sensor1Value, Sensor sensor1,
                                                 int sensor2Value, Sensor sensor2,
                                                 int sensor3Value, Sensor sensor3,
                                                 long unixTime) {
        DataSnapshotDialog dataSnapshotDialog = new DataSnapshotDialog();

        Bundle args = new Bundle();
        args.putInt(SENSOR_1_VALUE_KEY, sensor1Value);
        args.putInt(SENSOR_2_VALUE_KEY, sensor2Value);
        args.putInt(SENSOR_3_VALUE_KEY, sensor3Value);
        args.putSerializable(SENSOR_1_KEY, sensor1);
        args.putSerializable(SENSOR_2_KEY, sensor2);
        args.putSerializable(SENSOR_3_KEY, sensor3);
        args.putLong(UNIX_TIME_KEY, unixTime);
        dataSnapshotDialog.setArguments(args);

        return dataSnapshotDialog;
    }


    private View.OnClickListener buttonDoneListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dismiss();
        }
    };


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


    private static String minuteOrSecond(int minute) {
        String result = "";

        if (minute < 10) {
            result = "0" + minute;
        } else {
            result = String.valueOf(minute);
        }

        return result;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        int sensor1Value = getArguments().getInt(SENSOR_1_VALUE_KEY);
        int sensor2Value = getArguments().getInt(SENSOR_2_VALUE_KEY);
        int sensor3Value = getArguments().getInt(SENSOR_3_VALUE_KEY);
        Sensor sensor1 = (Sensor) getArguments().getSerializable(SENSOR_1_KEY);
        Sensor sensor2 = (Sensor) getArguments().getSerializable(SENSOR_2_KEY);
        Sensor sensor3 = (Sensor) getArguments().getSerializable(SENSOR_3_KEY);
        long unixTime = getArguments().getLong(UNIX_TIME_KEY);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_datasnapshot, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(unixTime);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int amOrPm = calendar.get(Calendar.AM_PM);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        int second = calendar.get(Calendar.SECOND);

        StringBuilder date = new StringBuilder();
        date.append(month(month));
        date.append(" " + day);
        date.append(", " + year);

        StringBuilder time = new StringBuilder();
        time.append(hour(hour) + ":");
        time.append(minuteOrSecond(minute) + ":");
        time.append(minuteOrSecond(second) + morningOrNight(amOrPm));

        ((TextView) view.findViewById(R.id.text_date)).setText(date.toString());
        ((TextView) view.findViewById(R.id.text_time)).setText(time.toString());

        ((ImageView) view.findViewById(R.id.image_sensor_1)).setImageDrawable(ContextCompat.getDrawable(getActivity(), sensor1.getBlueImageId()));
        String sensorText = sensor1.getSensorType() == FlutterProtocol.InputTypes.NOT_SET ? getString(R.string.no_sensor) : getString(sensor1.getTypeTextId());
        ((TextView) view.findViewById(R.id.text_sensor_1)).setText(sensorText);
        ((TextView) view.findViewById(R.id.text_sensor_1_value)).setText(String.valueOf(sensor1Value));

        ((ImageView) view.findViewById(R.id.image_sensor_2)).setImageDrawable(ContextCompat.getDrawable(getActivity(), sensor2.getBlueImageId()));
        sensorText = sensor2.getSensorType() == FlutterProtocol.InputTypes.NOT_SET ? getString(R.string.no_sensor) : getString(sensor2.getTypeTextId());
        ((TextView) view.findViewById(R.id.text_sensor_2)).setText(sensorText);
        ((TextView) view.findViewById(R.id.text_sensor_2_value)).setText(String.valueOf(sensor2Value));

        ((ImageView) view.findViewById(R.id.image_sensor_3)).setImageDrawable(ContextCompat.getDrawable(getActivity(), sensor3.getBlueImageId()));
        sensorText = sensor3.getSensorType() == FlutterProtocol.InputTypes.NOT_SET ? getString(R.string.no_sensor) : getString(sensor3.getTypeTextId());
        ((TextView) view.findViewById(R.id.text_sensor_3)).setText(sensorText);
        ((TextView) view.findViewById(R.id.text_sensor_3_value)).setText(String.valueOf(sensor3Value));

        view.findViewById(R.id.button_done).setOnClickListener(buttonDoneListener);

        return builder.create();
    }
}
