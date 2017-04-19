package org.cmucreatelab.flutter_android.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.adapters.DataLogListAdapter;
import org.cmucreatelab.flutter_android.classes.datalogging.DataSet;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;

/**
 * Created by Steve on 2/6/2017.
 */
public class OpenLogDialog extends BaseResizableDialog {

    private static final String OPEN_LOG_LISTENER_KEY = "open_log_listener_key";
    private static final String DATA_SET_ON_FLUTTER_KEY = "data_set_on_fltter_key";
    private static final String DATA_SETS_ON_DEVICE_KEY = "data_sets_on_device_key";

    private GlobalHandler globalHandler;
    private OpenLogDialog instance;

    private OpenLogListener openLogListener;
    private DismissDialogListener dismissDialogListener;
    private DataSet dataSetOnFlutter;
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


    public static OpenLogDialog newInstance(Serializable openLogListener, Serializable dataSetOnFlutter, Serializable[] dataSetsOnDevice) {
        OpenLogDialog openLogDialog = new OpenLogDialog();

        Bundle args = new Bundle();
        args.putSerializable(OPEN_LOG_LISTENER_KEY, openLogListener);
        args.putSerializable(DATA_SET_ON_FLUTTER_KEY, dataSetOnFlutter);
        args.putSerializable(DATA_SETS_ON_DEVICE_KEY, dataSetsOnDevice);
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
        dismissDialogListener = (DismissDialogListener) getArguments().getSerializable(OPEN_LOG_LISTENER_KEY);
        dataSetOnFlutter = (DataSet) getArguments().getSerializable(DATA_SET_ON_FLUTTER_KEY);
        dataSetsOnDevice = (DataSet[]) getArguments().getSerializable(DATA_SETS_ON_DEVICE_KEY);
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
        if (dataSetOnFlutter != null) {
            TextView textLogName = (TextView) view.findViewById(R.id.text_current_log_name);
            TextView textLogPoints = (TextView) view.findViewById(R.id.text_num_points);
            noLogFlutterTextView.setVisibility(View.GONE);

            view.findViewById(R.id.relative_flutter_log).setVisibility(View.VISIBLE);
            textLogName.setText(dataSetOnFlutter.getDataName());
            textLogPoints.setText(String.valueOf(dataSetOnFlutter.getData().size()));
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
        openLogListener.onOpenedLog(dataSetOnFlutter);
        this.dismiss();
    }


    public interface OpenLogListener {
        public void onOpenedLog(DataSet dataSet);
    }

}
