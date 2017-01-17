package org.cmucreatelab.flutter_android.activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseNavigationActivity;
import org.cmucreatelab.flutter_android.adapters.DataInstanceListAdapter;
import org.cmucreatelab.flutter_android.adapters.DataLogListAdapter;
import org.cmucreatelab.flutter_android.classes.datalogging.DataPoint;
import org.cmucreatelab.flutter_android.classes.datalogging.DataSet;
import org.cmucreatelab.flutter_android.classes.flutters.FlutterOG;
import org.cmucreatelab.flutter_android.helpers.DataLoggingHandler;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.NoFlutterConnectedDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.RecordDataLoggingDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.RecordDataSensorDialog;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

// TODO - We need to make a database to hold the data logs
public class DataLogsActivity extends BaseNavigationActivity implements Serializable, RecordDataLoggingDialog.DialogRecordDataLoggingListener, FlutterOG.PopulatedDataSetListener {

    public static final String DATA_LOGS_ACTIVITY_KEY = "data_logging_key";

    private GlobalHandler globalHandler;
    private DataLoggingHandler dataLoggingHandler;

    private DataLogListAdapter dataLogListAdapter;
    private DataInstanceListAdapter dataInstanceListAdapter;

    private ProgressBar progressDataLogSelected;

    private DataSet dataSetOnFlutter;
    private FlutterOG flutter;

    private DataSet workingDataSet;


    private AdapterView.OnItemClickListener onDataLogClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Log.d(Constants.LOG_TAG, "onDataLogSelected");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_logs);
        ButterKnife.bind(this);
        globalHandler = GlobalHandler.getInstance(this);

        if (!globalHandler.melodySmartDeviceHandler.isConnected()) {
            NoFlutterConnectedDialog.displayDialog(this, R.string.no_flutter_data_logs);
        } else {
            dataLoggingHandler = globalHandler.dataLoggingHandler;
            flutter = globalHandler.sessionHandler.getSession().getFlutter();

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
            toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.tab_b_g_data));
            toolbar.setContentInsetsAbsolute(0,0);
            setSupportActionBar(toolbar);

            TextView logTitle = (TextView) findViewById(R.id.text_current_device_title);
            TextView textLogName = (TextView) findViewById(R.id.text_current_log_name);
            TextView textLogPoints = (TextView) findViewById(R.id.text_num_points);

            logTitle.setText(getString(R.string.on) + " " + flutter.getName() + " " + getString(R.string.flutter));
            if (!dataLoggingHandler.getDataName().equals(null) && dataLoggingHandler.getNumberOfPoints() != 0) {
                findViewById(R.id.relative_flutter_log).setVisibility(View.VISIBLE);
                textLogName.setText(dataLoggingHandler.getDataName());
                textLogPoints.setText(String.valueOf(dataLoggingHandler.getNumberOfPoints()));
            }

            dataLogListAdapter = new DataLogListAdapter(getLayoutInflater());
            ListView listDataLogs = (ListView) findViewById(R.id.list_data_logs);
            listDataLogs.setAdapter(dataLogListAdapter);
            listDataLogs.setOnItemClickListener(onDataLogClickListener);

            dataInstanceListAdapter = new DataInstanceListAdapter(getLayoutInflater());
            ListView listDataInstance = (ListView) findViewById(R.id.list_data_instance);
            listDataInstance.setAdapter(dataInstanceListAdapter);
            listDataInstance.setOnItemClickListener(onDataLogClickListener);

            progressDataLogSelected = (ProgressBar) findViewById(R.id.progress_load_data_log);
        }
    }


    @OnClick(R.id.text_send_log)
    public void onClickTextSendLog() {
        Log.d(Constants.LOG_TAG, "onClickTextSendLog");
    }


    @OnClick(R.id.text_clean_up)
    public void onClickTextCleanUp() {
        Log.d(Constants.LOG_TAG, "onClickTextCleanUp");
    }


    @OnClick(R.id.text_record_data)
    public void onClickTextRecordData() {
        Log.d(Constants.LOG_TAG, "onClickTextRecordData");
        RecordDataLoggingDialog recordDataLoggingDialog = RecordDataLoggingDialog.newInstance(this);
        recordDataLoggingDialog.show(getSupportFragmentManager(), "tag");
    }


    private void loadFlutterDataLog() {
        globalHandler.sessionHandler.getSession().getFlutter().populateDataSet(this, this);
        progressDataLogSelected.setVisibility(ProgressBar.VISIBLE);
        findViewById(R.id.include_data_log_landing).setVisibility(View.GONE);
    }
    @OnClick(R.id.text_open_log)
    public void onClickTextOpenLog() {
        Log.d(Constants.LOG_TAG, "onClickTextOpenLog");
        loadFlutterDataLog();
    }
    @OnClick(R.id.relative_flutter_log)
    public void onClickRelativeFlutterLog() {
        Log.d(Constants.LOG_TAG, "onClickRelativeFlutterLog");
        loadFlutterDataLog();
    }


    @Override
    public void onDataSetPopulated() {
        Log.d(Constants.LOG_TAG, "DataLogsActivity.onDataSetPopulated");
        dataSetOnFlutter = globalHandler.sessionHandler.getSession().getFlutter().getDataSet();
        if (dataSetOnFlutter != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    workingDataSet = dataSetOnFlutter;
                    progressDataLogSelected.setVisibility(ProgressBar.INVISIBLE);
                    findViewById(R.id.include_data_log_selected).setVisibility(View.VISIBLE);

                    TextView sensor1Type = (TextView) findViewById(R.id.text_sensor_1_type);
                    TextView sensor2Type = (TextView) findViewById(R.id.text_sensor_2_type);
                    TextView sensor3Type = (TextView) findViewById(R.id.text_sensor_3_type);
                    sensor1Type.setText(workingDataSet.getSensorNames()[0]);
                    sensor2Type.setText(workingDataSet.getSensorNames()[1]);
                    sensor3Type.setText(workingDataSet.getSensorNames()[2]);
                    // TODO - show the image of what type of sensor is chosen
                    /*sensor1Type.setCompoundDrawables(null, ContextCompat.getDrawable(this, flutter.getSensors()[0].getOrangeImageIdSm()), null, null);
                    sensor2Type.setCompoundDrawables(null, ContextCompat.getDrawable(this, flutter.getSensors()[1].getOrangeImageIdSm()), null, null);
                    sensor3Type.setCompoundDrawables(null, ContextCompat.getDrawable(this, flutter.getSensors()[2].getOrangeImageIdSm()), null, null);*/

                    Iterator it = workingDataSet.getData().entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry)it.next();
                        System.out.println(pair.getKey() + " = " + pair.getValue());
                        dataInstanceListAdapter.addDataPoint((DataPoint) pair.getValue());
                    }
                }
            });
        }
    }


    @Override
    public void onDataRecord(String name, int interval, int sample) {
        Log.d(Constants.LOG_TAG, "onDataRecord");
        globalHandler.dataLoggingHandler.startLogging(interval, sample, name);
    }

}
