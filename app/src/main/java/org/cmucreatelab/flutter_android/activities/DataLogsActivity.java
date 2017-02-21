package org.cmucreatelab.flutter_android.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseNavigationActivity;
import org.cmucreatelab.flutter_android.adapters.DataInstanceListAdapter;
import org.cmucreatelab.flutter_android.adapters.DataLogListAdapter;
import org.cmucreatelab.flutter_android.classes.datalogging.DataPoint;
import org.cmucreatelab.flutter_android.classes.datalogging.DataSet;
import org.cmucreatelab.flutter_android.classes.flutters.Flutter;
import org.cmucreatelab.flutter_android.helpers.DataLoggingHandler;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.FileHandler;
import org.cmucreatelab.flutter_android.ui.dialogs.CleanUpLogsDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.EmailDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.NoFlutterConnectedDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.OpenLogDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.RecordDataLoggingDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DataLogsActivity extends BaseNavigationActivity implements Serializable, RecordDataLoggingDialog.DialogRecordDataLoggingListener, Flutter.PopulatedDataSetListener, DataLoggingHandler.DataSetPointsListener, OpenLogDialog.OpenLogListener {

    public static final String DATA_LOGS_ACTIVITY_KEY = "data_logging_key";

    private DataLogsActivity instance;
    private DataLoggingHandler dataLoggingHandler;
    private DataLogListAdapter dataLogListAdapter;
    private DataInstanceListAdapter dataInstanceListAdapter;
    private DataSet dataSetOnFlutter;
    private DataSet[] dataSetsOnDevice;
    private DataSet workingDataSet;
    private boolean isDataLogSelected;
    private DataPoint workingDataPoint;
    private ImageView workingDataPointImage;
    private TextView sendLogTextView;
    private LinearLayout dataOnFlutterContainer;
    private LinearLayout dataOnDeviceContainer;


    private void loadDataSet(DataSet dataSet) {
        Log.d(Constants.LOG_TAG, "onDataLogSelected");

        isDataLogSelected = true;
        sendLogTextView.setEnabled(true);
        workingDataSet = dataSet;
        findViewById(R.id.include_data_log_landing).setVisibility(View.GONE);
        findViewById(R.id.include_data_log_selected).setVisibility(View.VISIBLE);

        TextView sensor1Type = (TextView) findViewById(R.id.text_sensor_1_type);
        TextView sensor2Type = (TextView) findViewById(R.id.text_sensor_2_type);
        TextView sensor3Type = (TextView) findViewById(R.id.text_sensor_3_type);
        sensor1Type.setText(getString(dataSet.getSensors()[0].getTypeTextId()));
        sensor2Type.setText(getString(dataSet.getSensors()[1].getTypeTextId()));
        sensor3Type.setText(getString(dataSet.getSensors()[2].getTypeTextId()));
        // TODO - figure out why these images are not populating
        sensor1Type.setCompoundDrawables(null, ContextCompat.getDrawable(this, dataSet.getSensors()[0].getOrangeImageIdSm()), null, null);
        sensor2Type.setCompoundDrawables(null, ContextCompat.getDrawable(this, dataSet.getSensors()[1].getOrangeImageIdSm()), null, null);
        sensor3Type.setCompoundDrawables(null, ContextCompat.getDrawable(this, dataSet.getSensors()[2].getOrangeImageIdSm()), null, null);

        Iterator it = dataSet.getData().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            dataInstanceListAdapter.addDataPoint((DataPoint) pair.getValue());
        }

        GlobalHandler.getInstance(getApplicationContext()).sessionHandler.dismissProgressDialog();
    }


    private AdapterView.OnItemClickListener onDataLogClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());
            globalHandler.sessionHandler.createProgressDialog(instance);
            globalHandler.sessionHandler.updateProgressDialogMessage(getString(R.string.loading_data));
            loadDataSet(dataSetsOnDevice[i]);
        }
    };


    private AdapterView.OnItemClickListener onDataInstanceClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            ArrayList<String> keys = workingDataSet.getKeys();
            if (workingDataPoint == null) {
                workingDataPointImage = (ImageView) view.findViewById(R.id.image_selector);
                workingDataPointImage.setImageResource(R.drawable.circle_selected);
                workingDataPoint = workingDataSet.getData().get(keys.get(i));
            } else {
                DataPoint temp = workingDataSet.getData().get(keys.get(i));
                if (temp.equals(workingDataPoint)) {
                    workingDataPointImage = (ImageView) view.findViewById(R.id.image_selector);
                    workingDataPointImage.setImageResource(R.drawable.circle_not_selected);
                    workingDataPointImage = null;
                    workingDataPoint = null;
                } else {
                    workingDataPointImage.setImageResource(R.drawable.circle_not_selected);
                    workingDataPointImage = (ImageView) view.findViewById(R.id.image_selector);
                    workingDataPointImage.setImageResource(R.drawable.circle_selected);
                    workingDataPoint = workingDataSet.getData().get(keys.get(i));
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_logs);
        ButterKnife.bind(this);
        instance = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.tab_b_g_data));
        toolbar.setContentInsetsAbsolute(0,0);
        setSupportActionBar(toolbar);

        sendLogTextView = (TextView) findViewById(R.id.text_send_log);
        dataOnFlutterContainer = (LinearLayout) findViewById(R.id.linear_flutter_data_container);
        dataOnDeviceContainer = (LinearLayout) findViewById(R.id.linear_device_data_container);
    }


    @Override
    public void onResume() {
        Log.d(Constants.LOG_TAG, "onResume");
        super.onResume();
        GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());
        TextView flutterStatusText = (TextView)findViewById(R.id.text_flutter_connection_status);
        ImageView flutterStatusIcon = (ImageView)findViewById(R.id.image_flutter_status_icon);

        if (!globalHandler.melodySmartDeviceHandler.isConnected()) {
            NoFlutterConnectedDialog.displayDialog(this, R.string.no_flutter_data_logs);
            flutterStatusText.setText(R.string.connection_disconnected);
            flutterStatusText.setTextColor(Color.GRAY);
            flutterStatusIcon.setImageResource(R.drawable.flutterdisconnectgraphic);
        } else if (!isDataLogSelected) {
            dataLoggingHandler = globalHandler.dataLoggingHandler;

            String flutterName = globalHandler.sessionHandler.getSession().getFlutter().getName();
            TextView flutterStatusButtonName = (TextView)findViewById(R.id.text_connected_flutter_name);
            flutterStatusButtonName.setText(flutterName);
            flutterStatusText.setText(R.string.connection_connected);
            flutterStatusText.setTextColor(getResources().getColor(R.color.fluttergreen));
            flutterStatusIcon.setImageResource(R.drawable.flutterconnectgraphic);

            globalHandler.sessionHandler.createProgressDialog(this);
            globalHandler.sessionHandler.updateProgressDialogMessage(getString(R.string.loading_data_log_on_flutter));
            dataLoggingHandler.populatePointsAvailable(this);
            isDataLogSelected = false;
            sendLogTextView.setEnabled(false);

            globalHandler.sessionHandler.getSession().setDataSets(FileHandler.loadDataSetsFromFile(globalHandler));
            dataSetsOnDevice = globalHandler.sessionHandler.getSession().getDataSets();

            if (dataSetsOnDevice.length > 0)
                dataOnDeviceContainer.setVisibility(View.VISIBLE);
            else
                dataOnDeviceContainer.setVisibility(View.GONE);

            dataLogListAdapter = new DataLogListAdapter(getLayoutInflater());
            ListView listDataLogs = (ListView) findViewById(R.id.list_data_logs);
            listDataLogs.setAdapter(dataLogListAdapter);
            listDataLogs.setOnItemClickListener(onDataLogClickListener);

            for (DataSet dataSet : dataSetsOnDevice) {
                dataLogListAdapter.addDataLog(dataSet);
            }

            dataInstanceListAdapter = new DataInstanceListAdapter(getLayoutInflater());
            ListView listDataInstance = (ListView) findViewById(R.id.list_data_instance);
            listDataInstance.setAdapter(dataInstanceListAdapter);
            listDataInstance.setOnItemClickListener(onDataInstanceClickListener);
        }
    }


    @OnClick(R.id.text_open_log)
    public void onClickTextOpenLog() {
        OpenLogDialog openLogDialog = OpenLogDialog.newInstance(this, dataSetOnFlutter, dataSetsOnDevice);
        openLogDialog.show(getSupportFragmentManager(), "tag");
    }


    @OnClick(R.id.text_send_log)
    public void onClickTextSendLog() {
        if (isDataLogSelected) {
            Log.d(Constants.LOG_TAG, "onClickTextSendLog");
            EmailDialog emailDialog = EmailDialog.newInstance(workingDataSet);
            emailDialog.show(getSupportFragmentManager(), "tag");
        }
    }


    @OnClick(R.id.text_clean_up)
    public void onClickTextCleanUp() {
        Log.d(Constants.LOG_TAG, "onClickTextCleanUp");
        CleanUpLogsDialog cleanUpLogsDialog = CleanUpLogsDialog.newInstance(this, dataSetOnFlutter, dataSetsOnDevice);
        cleanUpLogsDialog.show(getSupportFragmentManager(), "tag");
    }


    @OnClick(R.id.text_record_data)
    public void onClickTextRecordData() {
        Log.d(Constants.LOG_TAG, "onClickTextRecordData");
        RecordDataLoggingDialog recordDataLoggingDialog = RecordDataLoggingDialog.newInstance(this);
        recordDataLoggingDialog.show(getSupportFragmentManager(), "tag");
    }

    private void loadFlutterDataLog() {
        GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());

        globalHandler.sessionHandler.createProgressDialog(this);
        loadDataSet(dataSetOnFlutter);
        if (!dataLoggingHandler.getIsLogging()) {
            FileHandler.saveDataSetToFile(globalHandler, dataSetOnFlutter);
            dataLoggingHandler.deleteLog();
        }
    }
    @OnClick(R.id.relative_flutter_log)
    public void onClickRelativeFlutterLog() {
        Log.d(Constants.LOG_TAG, "onClickRelativeFlutterLog");
        loadFlutterDataLog();
    }


    @Override
    public void onDataSetPopulated() {
        Log.d(Constants.LOG_TAG, "DataLogsActivity.onDataSetPopulated");
        GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());

        dataSetOnFlutter = globalHandler.sessionHandler.getSession().getFlutter().getDataSet();
        globalHandler.sessionHandler.dismissProgressDialog();
        if (dataSetOnFlutter != null && dataSetOnFlutter.getData().size() > 0)
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dataOnFlutterContainer.setVisibility(View.VISIBLE);
                }
            });
    }


    @Override
    public void onDataSetPointsPopulated(boolean isSuccess) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView logTitle = (TextView) findViewById(R.id.text_current_device_title);
                TextView textLogName = (TextView) findViewById(R.id.text_current_log_name);
                TextView textLogPoints = (TextView) findViewById(R.id.text_num_points);
                Flutter flutter = GlobalHandler.getInstance(getApplicationContext()).sessionHandler.getSession().getFlutter();

                logTitle.setText(getString(R.string.on) + " " + flutter.getName() + " " + getString(R.string.flutter));
                if (!dataLoggingHandler.getDataName().equals(null) && dataLoggingHandler.getNumberOfPoints() != 0) {
                    findViewById(R.id.relative_flutter_log).setVisibility(View.VISIBLE);
                    textLogName.setText(dataLoggingHandler.getDataName());
                    textLogPoints.setText(String.valueOf(dataLoggingHandler.getNumberOfPoints()));
                } else {
                    findViewById(R.id.relative_flutter_log).setVisibility(View.GONE);
                }
            }
        });
        GlobalHandler.getInstance(getApplicationContext()).sessionHandler.getSession().getFlutter().populateDataSet(this, this);
    }


    @Override
    public void onDataRecord(String name, int interval, int sample) {
        Log.d(Constants.LOG_TAG, "onDataRecord");
        GlobalHandler.getInstance(getApplicationContext()).dataLoggingHandler.startLogging(interval, sample, name);
    }

    @Override
    public void onOpenedLog(DataSet dataSet) {
        GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());

        if (dataSet.equals(dataSetOnFlutter)) {
            globalHandler.sessionHandler.createProgressDialog(this);
            globalHandler.sessionHandler.updateProgressDialogMessage(getString(R.string.loading_data));
            loadDataSet(dataSet);
            if (!dataLoggingHandler.getIsLogging()) {
                FileHandler.saveDataSetToFile(globalHandler, dataSetOnFlutter);
                dataLoggingHandler.deleteLog();
            }
        } else {
            globalHandler.sessionHandler.createProgressDialog(instance);
            globalHandler.sessionHandler.updateProgressDialogMessage(getString(R.string.loading_data));
            loadDataSet(dataSet);
        }
    }

    @Override
    public void onDismissed() {
        onResume();
    }
}
