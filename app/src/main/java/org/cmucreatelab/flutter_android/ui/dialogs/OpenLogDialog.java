package org.cmucreatelab.flutter_android.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.adapters.DataLogListAdapter;
import org.cmucreatelab.flutter_android.classes.datalogging.DataSet;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Steve on 2/6/2017.
 */
public class OpenLogDialog extends BaseResizableDialog {

    private static final String OPEN_LOG_LISTENER_KEY = "open_log_listener_key";
    private static final String NUMBER_OF_POINTS_KEY = "number_of_points_key";
    private static final String DATA_LOG_NAME_KEY = "data_log_name_key";
    private static final String DATA_SETS_ON_DEVICE_KEY = "data_sets_on_device_key";
    private static final String DISMISS_DIALOG_LISTENER_KEY = "dismiss_dialog_listener_key";

    private GlobalHandler globalHandler;
    private OpenLogDialog instance;

    private OpenLogListener openLogListener;
    private DismissDialogListener dismissDialogListener;
    private String flutterDataSetName;
    private int numberOfPoints;
    private DataSet[] dataSetsOnDevice;

    private ListView listDeviceDataSets;
    private DataLogListAdapter dataLogListAdapter;

    private TextView noLogsDeviceTextView, noLogFlutterTextView;


    private AdapterView.OnItemClickListener dataLogListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            openLogListener.onOpenedLog(dataSetsOnDevice[i]);
            instance.dismiss();
        }
    };


    public static OpenLogDialog newInstance(Serializable listener, int numberOfPoints, String dataLogName, Serializable[] dataSetsOnDevice) {
        OpenLogDialog openLogDialog = new OpenLogDialog();

        Bundle args = new Bundle();
        args.putSerializable(OPEN_LOG_LISTENER_KEY, listener);
        args.putInt(NUMBER_OF_POINTS_KEY, numberOfPoints);
        args.putString(DATA_LOG_NAME_KEY, dataLogName);
        args.putSerializable(DATA_SETS_ON_DEVICE_KEY, dataSetsOnDevice);
        args.putSerializable(DISMISS_DIALOG_LISTENER_KEY, listener);
        openLogDialog.setArguments(args);

        return openLogDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        globalHandler = GlobalHandler.getInstance(this.getActivity());
        instance = this;
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_open_log, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ButterKnife.bind(this, view);

        openLogListener = (OpenLogListener) getArguments().getSerializable(OPEN_LOG_LISTENER_KEY);
        flutterDataSetName = getArguments().getString(DATA_LOG_NAME_KEY);
        numberOfPoints = getArguments().getInt(NUMBER_OF_POINTS_KEY);
        dataSetsOnDevice = (DataSet[]) getArguments().getSerializable(DATA_SETS_ON_DEVICE_KEY);
        dismissDialogListener = (DismissDialogListener) getArguments().getSerializable(DISMISS_DIALOG_LISTENER_KEY);

        listDeviceDataSets = (ListView) view.findViewById(R.id.list_data_logs);
        noLogFlutterTextView = (TextView) view.findViewById(R.id.text_no_log_flutter);
        noLogsDeviceTextView = (TextView) view.findViewById(R.id.text_no_logs_device);
        dataLogListAdapter = new DataLogListAdapter(inflater);

        listDeviceDataSets.setOnItemClickListener(dataLogListener);
        listDeviceDataSets.setAdapter(dataLogListAdapter);
        if (dataSetsOnDevice.length > 0) {
            noLogsDeviceTextView.setVisibility(View.GONE);
            for (DataSet dataSet : dataSetsOnDevice) {
                dataLogListAdapter.addDataLog(dataSet);
            }
        }

        TextView logTitle = (TextView) view.findViewById(R.id.text_current_device_title);
        String name = "";
        if (globalHandler.melodySmartDeviceHandler.isConnected()) {
            name = globalHandler.sessionHandler.getSession().getFlutter().getName();
        }
        logTitle.setText(getString(R.string.on) + " " + name + " " + getString(R.string.flutter));

        // populate the data log on the flutter
        if (numberOfPoints > 0) {
            TextView textLogName = (TextView) view.findViewById(R.id.text_current_log_name);
            TextView textLogPoints = (TextView) view.findViewById(R.id.text_num_points);
            noLogFlutterTextView.setVisibility(View.GONE);

            view.findViewById(R.id.relative_flutter_log).setVisibility(View.VISIBLE);
            textLogName.setText(flutterDataSetName);
            textLogPoints.setText(String.valueOf(numberOfPoints));
        }

        return builder.create();
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        dismissDialogListener.onDialogDismissed();
    }

    @OnClick(R.id.relative_flutter_log)
    public void onClickDataLogOnFlutter() {
        openLogListener.onOpenedLog(null);
        this.dismiss();
    }


    public interface OpenLogListener {
        public void onOpenedLog(DataSet dataSet);
    }

}
