package org.cmucreatelab.flutter_android.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseNavigationActivity;
import org.cmucreatelab.flutter_android.adapters.DataInstanceListAdapter;
import org.cmucreatelab.flutter_android.adapters.DataLogListAdapter;
import org.cmucreatelab.flutter_android.classes.Stat;
import org.cmucreatelab.flutter_android.classes.datalogging.DataLogDetails;
import org.cmucreatelab.flutter_android.classes.datalogging.DataPoint;
import org.cmucreatelab.flutter_android.classes.datalogging.DataSet;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.datalogging.CleanUpAfterState;
import org.cmucreatelab.flutter_android.helpers.datalogging.CleanUpBeforeState;
import org.cmucreatelab.flutter_android.helpers.datalogging.DataLoggingHandler;
import org.cmucreatelab.flutter_android.helpers.datalogging.DataLogsUpdateHelper;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.datalogging.DataRecordingTimer;
import org.cmucreatelab.flutter_android.helpers.datalogging.OpenLogState;
import org.cmucreatelab.flutter_android.helpers.datalogging.ResumeState;
import org.cmucreatelab.flutter_android.helpers.datalogging.SaveToKindleState;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.FileHandler;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseDataLoggingDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.CleanUpLogsDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.DismissDialogListener;
import org.cmucreatelab.flutter_android.ui.dialogs.EmailDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.NoFlutterConnectedDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.OpenLogDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.RecordDataLoggingDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.RecordingWarningDataDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.SaveToKindleDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.WarningDialog;
import org.cmucreatelab.flutter_android.ui.realtivelayout.StatsRelativeLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import butterknife.ButterKnife;

import static org.cmucreatelab.flutter_android.helpers.static_classes.FlutterProtocol.InputTypes.NOT_SET;

public class DataLogsActivity extends BaseNavigationActivity implements Serializable, BaseDataLoggingDialog.DialogRecordListener,
        OpenLogDialog.OpenLogListener, SaveToKindleDialog.SaveToKindleListener, DismissDialogListener, DataRecordingTimer.TimeExpireListener {

    public static final String DATA_LOGS_ACTIVITY_KEY = "data_logging_key";

    private GlobalHandler globalHandler;
    private DataLogsActivity instance;
    private DataLogsUpdateHelper dataLogsUpdateHelper;
    private DataSet workingDataSet;
    private DataPoint workingDataPoint;
    private DataRecordingTimer dataRecordingTimer;

    private Constants.STATS statState;
    private Stat[] means, medians, modes, maxs, mins;
    private boolean isMax, isMin;

    private DataLogListAdapter dataLogListAdapter;
    private DataInstanceListAdapter dataInstanceListAdapter;
    private RelativeLayout dataOnFlutterRelativeContainer;
    private ListView listDataLogsOnDevice, listDataInstance;
    private ProgressBar progressSensor1, progressSensor2, progressSensor3;
    private StatsRelativeLayout[] statsRelativeLayouts;
    private ImageView workingDataPointImage, imageSensor1, imageSensor2, imageSensor3;
    private Button buttonMean, buttonMedian, buttonMode, buttonMax, buttonMin;
    private TextView openLogTextView, sendLogTextView, cleanUpTextView, recordDataTextView, noLogsDeviceTextView, noLogsFlutterTextView;


    // utility methods used by the class


    public void updateDynamicViews() {
        Log.d(Constants.LOG_TAG, "DataLogsActivity.updateDynamicViews");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView sensorHigh;
                TextView sensorLow;

                if (workingDataSet == null) {
                    findViewById(R.id.include_data_log_landing).setVisibility(View.VISIBLE);
                    findViewById(R.id.include_data_log_selected).setVisibility(View.GONE);

                    if (dataLogsUpdateHelper.getDataSetsOnDevice().length > 0)
                        noLogsDeviceTextView.setVisibility(View.GONE);
                    else
                        noLogsDeviceTextView.setVisibility(View.VISIBLE);

                    if (dataLogsUpdateHelper.getDataSetOnFlutter() != null && dataLogsUpdateHelper.getDataSetOnFlutter().getData().size() > 0)
                        noLogsFlutterTextView.setVisibility(View.GONE);
                    else
                        noLogsFlutterTextView.setVisibility(View.VISIBLE);

                    TextView logTitle = (TextView) findViewById(R.id.text_current_device_title);
                    TextView textLogName = (TextView) findViewById(R.id.text_current_log_name);
                    TextView textLogPoints = (TextView) findViewById(R.id.text_num_points);

                    String name = "";
                    if (globalHandler.melodySmartDeviceHandler.isConnected()) {
                        name = globalHandler.sessionHandler.getSession().getFlutter().getName();
                    }
                    logTitle.setText(getString(R.string.on) + " " + name + " " + getString(R.string.flutter));
                    if (!globalHandler.dataLoggingHandler.getDataName().equals(null) && globalHandler.dataLoggingHandler.getNumberOfPoints() != 0) {
                        findViewById(R.id.relative_flutter_log).setVisibility(View.VISIBLE);
                        textLogName.setText(globalHandler.dataLoggingHandler.getDataName());
                        textLogPoints.setText(String.valueOf(globalHandler.dataLoggingHandler.getNumberOfPoints()));
                    } else {
                        findViewById(R.id.relative_flutter_log).setVisibility(View.GONE);
                    }

                    dataLogListAdapter.clearDataLogs();
                    for (DataSet dataSet : dataLogsUpdateHelper.getDataSetsOnDevice()) {
                        dataLogListAdapter.addDataLog(dataSet);
                    }

                } else if (workingDataPoint != null && workingDataSet != null){
                    Sensor sensor1 = workingDataSet.getSensors()[0];
                    Sensor sensor2 = workingDataSet.getSensors()[1];
                    Sensor sensor3 = workingDataSet.getSensors()[2];

                    if (sensor1.getSensorType() != NOT_SET) {
                        sensorHigh = (TextView) findViewById(R.id.text_high_1);
                        sensorLow = (TextView) findViewById(R.id.text_low_1);
                        sensorHigh.setText(sensor1.getHighTextId());
                        sensorLow.setText(sensor1.getLowTextId());
                        progressSensor1.setProgress(Integer.parseInt(workingDataPoint.getSensor1Value()));
                    }
                    if (sensor2.getSensorType() != NOT_SET) {
                        sensorHigh = (TextView) findViewById(R.id.text_high_2);
                        sensorLow = (TextView) findViewById(R.id.text_low_2);
                        sensorHigh.setText(sensor2.getHighTextId());
                        sensorLow.setText(sensor2.getLowTextId());
                        progressSensor2.setProgress(Integer.parseInt(workingDataPoint.getSensor2Value()));
                    }
                    if (sensor3.getSensorType() != NOT_SET) {
                        sensorHigh = (TextView) findViewById(R.id.text_high_3);
                        sensorLow = (TextView) findViewById(R.id.text_low_3);
                        sensorHigh.setText(sensor3.getHighTextId());
                        sensorLow.setText(sensor3.getLowTextId());
                        progressSensor3.setProgress(Integer.parseInt(workingDataPoint.getSensor3Value()));
                    }
                } else {
                    sensorHigh = (TextView) findViewById(R.id.text_high_1);
                    sensorLow = (TextView) findViewById(R.id.text_low_1);
                    sensorHigh.setText("");
                    sensorLow.setText("");
                    progressSensor1.setProgress(0);
                    sensorHigh = (TextView) findViewById(R.id.text_high_2);
                    sensorLow = (TextView) findViewById(R.id.text_low_2);
                    sensorHigh.setText("");
                    sensorLow.setText("");
                    progressSensor2.setProgress(0);
                    sensorHigh = (TextView) findViewById(R.id.text_high_3);
                    sensorLow = (TextView) findViewById(R.id.text_low_3);
                    sensorHigh.setText("");
                    sensorLow.setText("");
                    progressSensor3.setProgress(0);
                    removeAllStats();
                }
            }
        });
    }


    public void loadDataSet(final DataSet dataSet) {
        Log.d(Constants.LOG_TAG, "DataLogsActivity.loadDataSet");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sendLogTextView.setEnabled(true);
                workingDataSet = dataSet;
                findViewById(R.id.include_data_log_landing).setVisibility(View.GONE);
                findViewById(R.id.include_data_log_selected).setVisibility(View.VISIBLE);

                TextView dataLogTitle = (TextView) findViewById(R.id.text_data_log_title);
                TextView sensor1Type = (TextView) findViewById(R.id.text_sensor_1_type);
                TextView sensor2Type = (TextView) findViewById(R.id.text_sensor_2_type);
                TextView sensor3Type = (TextView) findViewById(R.id.text_sensor_3_type);
                dataLogTitle.setText(dataSet.getDataName());
                sensor1Type.setText(getString(dataSet.getSensors()[0].getTypeTextId()));
                sensor2Type.setText(getString(dataSet.getSensors()[1].getTypeTextId()));
                sensor3Type.setText(getString(dataSet.getSensors()[2].getTypeTextId()));
                sensor1Type.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(instance, dataSet.getSensors()[0].getOrangeImageIdSm()), null, null);
                sensor2Type.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(instance, dataSet.getSensors()[1].getOrangeImageIdSm()), null, null);
                sensor3Type.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(instance, dataSet.getSensors()[2].getOrangeImageIdSm()), null, null);

                dataInstanceListAdapter.clearDataPoints();
                Iterator it = dataSet.getData().entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    dataInstanceListAdapter.addDataPoint((DataPoint) pair.getValue());
                }

                GlobalHandler.getInstance(getApplicationContext()).sessionHandler.dismissProgressDialog();

                if (workingDataSet.getSensors()[0].getSensorType() != NOT_SET)
                    imageSensor1.setImageDrawable(ContextCompat.getDrawable(instance, workingDataSet.getSensors()[0].getOrangeImageIdMd()));
                else
                    imageSensor1.setImageDrawable(ContextCompat.getDrawable(instance, R.drawable.grey_question_mark));

                if (workingDataSet.getSensors()[1].getSensorType() != NOT_SET)
                    imageSensor2.setImageDrawable(ContextCompat.getDrawable(instance, workingDataSet.getSensors()[1].getOrangeImageIdMd()));
                else
                    imageSensor2.setImageDrawable(ContextCompat.getDrawable(instance, R.drawable.grey_question_mark));

                if (workingDataSet.getSensors()[2].getSensorType() != NOT_SET)
                    imageSensor3.setImageDrawable(ContextCompat.getDrawable(instance, workingDataSet.getSensors()[2].getOrangeImageIdMd()));
                else
                    imageSensor3.setImageDrawable(ContextCompat.getDrawable(instance, R.drawable.grey_question_mark));

                removeAllStats();
                isMin = false;
                isMax = false;
                workingDataPoint = null;
                buttonMax.setBackground(ContextCompat.getDrawable(instance, R.drawable.orange_button_border));
                buttonMax.setTextColor(getResources().getColor(R.color.orange));
                buttonMin.setBackground(ContextCompat.getDrawable(instance, R.drawable.orange_button_border));
                buttonMin.setTextColor(getResources().getColor(R.color.orange));

                updateDynamicViews();
            }
        });
    }


    // OnClick Listeners


    private TextView.OnClickListener openLogClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(Constants.LOG_TAG, "DataLogsActivity.onClickOpenLog");
            if (dataLogsUpdateHelper.getDataSetOnFlutter() != null || dataLogsUpdateHelper.getDataSetsOnDevice().length > 0) {
                globalHandler.sessionHandler.createProgressDialog(instance);
                globalHandler.sessionHandler.updateProgressDialogMessage(getString(R.string.loading_data));
                dataLogsUpdateHelper.registerStateAndUpdateLogs(new OpenLogState(instance));
            } else {
                WarningDialog warningDialog = WarningDialog.newInstance(getString(R.string.no_data_logs_to_open), getString(R.string.no_data_logs_to_open_details), R.drawable.round_orange_button_bottom);
                warningDialog.show(getSupportFragmentManager(), "tag");
            }
        }
    };


    private TextView.OnClickListener sendLogClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(Constants.LOG_TAG, "DataLogsActivity.onClickSendLog");
            if (workingDataSet != null) {
                Log.d(Constants.LOG_TAG, "onClickTextSendLog");
                EmailDialog emailDialog = EmailDialog.newInstance(workingDataSet);
                emailDialog.show(getSupportFragmentManager(), "tag");
            } else {
                WarningDialog warningDialog = WarningDialog.newInstance(getString(R.string.select_a_data_log), getString(R.string.select_a_data_log_details), R.drawable.round_orange_button_bottom);
                warningDialog.show(getSupportFragmentManager(), "tag");
            }
        }
    };


    private TextView.OnClickListener cleanUpClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(Constants.LOG_TAG, "DataLogsActivity.onClickCleanUp");
            if (dataLogsUpdateHelper.getDataSetOnFlutter() != null || dataLogsUpdateHelper.getDataSetsOnDevice().length > 0) {
                globalHandler.sessionHandler.createProgressDialog(instance);
                globalHandler.sessionHandler.updateProgressDialogMessage(getString(R.string.loading_data));
                dataLogsUpdateHelper.registerStateAndUpdateLogs(new CleanUpBeforeState(instance));
            } else {
                WarningDialog warningDialog = WarningDialog.newInstance(getString(R.string.no_data_logs_to_clean_up), getString(R.string.no_data_logs_to_clean_up_details), R.drawable.round_orange_button_bottom);
                warningDialog.show(getSupportFragmentManager(), "tag");
            }
        }
    };


    private TextView.OnClickListener recordDataClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(Constants.LOG_TAG, "DataLogsActivity.onClickRecordData");

            if (!globalHandler.melodySmartDeviceHandler.isConnected()) {
                TextView flutterStatusText = (TextView)findViewById(R.id.text_flutter_connection_status);
                ImageView flutterStatusIcon = (ImageView)findViewById(R.id.image_flutter_status_icon);
                NoFlutterConnectedDialog.displayDialog(DataLogsActivity.this, R.string.no_flutter_data_logs);
                flutterStatusText.setText(R.string.connection_disconnected);
                flutterStatusText.setTextColor(Color.GRAY);
                flutterStatusIcon.setImageResource(R.drawable.flutterdisconnectgraphic);
            } else {
                globalHandler.sessionHandler.createProgressDialog(instance);
                globalHandler.sessionHandler.updateProgressDialogMessage("Loading data log information...");

                globalHandler.dataLoggingHandler.populatePointsAvailable(new DataLoggingHandler.DataSetPointsListener() {
                    @Override
                    public void onDataSetPointsPopulated(boolean isSuccess) {
                        globalHandler.sessionHandler.dismissProgressDialog();
                        if (globalHandler.dataLoggingHandler.isLogging()) {
                            String dataLogName = globalHandler.dataLoggingHandler.getDataName();
                            DataLogDetails dataLogDetails = globalHandler.dataLoggingHandler.loadDataLogDetails(instance);
                            RecordingWarningDataDialog recordingWarningDataDialog = RecordingWarningDataDialog.newInstance(
                                    instance, dataLogName, dataLogDetails.getIntervalInt(), dataLogDetails.getIntervalString(), dataLogDetails.getTimePeriodInt(), dataLogDetails.getTimePeriodString()
                            );
                            recordingWarningDataDialog.show(getSupportFragmentManager(), "tag");
                        } else {
                            RecordDataLoggingDialog recordDataLoggingDialog = RecordDataLoggingDialog.newInstance(instance, R.drawable.round_orange_button_bottom_right);
                            recordDataLoggingDialog.show(getSupportFragmentManager(), "tag");
                        }
                    }
                });
            }
        }
    };


    private AdapterView.OnItemClickListener onDataLogClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());
            globalHandler.sessionHandler.createProgressDialog(instance);
            globalHandler.sessionHandler.updateProgressDialogMessage(getString(R.string.loading_data));
            loadDataSet(dataLogsUpdateHelper.getDataSetsOnDevice()[i]);
        }
    };

    private AdapterView.OnItemClickListener onDataInstanceClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            ArrayList<String> keys = workingDataSet.getKeys();
            if (workingDataPoint == null) {
                workingDataPointImage = (ImageView) view.findViewById(R.id.image_selector);
                workingDataPointImage.setImageResource(R.drawable.check_mark);
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
                    workingDataPointImage.setImageResource(R.drawable.check_mark);
                    workingDataPoint = workingDataSet.getData().get(keys.get(i));
                }
            }
            updateDynamicViews();
        }
    };


    private RelativeLayout.OnClickListener dataSetOnFlutterListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(Constants.LOG_TAG, "DataLogsActivity.onClickDataSetOnFlutter");
            if (!globalHandler.dataLoggingHandler.isLogging()) {
                SaveToKindleDialog dialog = SaveToKindleDialog.newInstance(instance, globalHandler.dataLoggingHandler.getDataName(), globalHandler.sessionHandler.getSession().getFlutter().getName());
                dialog.show(getSupportFragmentManager(), "tag");
            } else {
                if (dataLogsUpdateHelper.getDataSetOnFlutter() != null)
                    loadDataSet(dataLogsUpdateHelper.getDataSetOnFlutter());
            }
        }
    };


    // I am not including MAX and MIN because they behave differently since they can be on at the same time
    private void updateStats(Sensor[] sensors, int[] positions, Stat[] stats, Constants.STATS type, Button button, int drawableBorderId, int drawableId) {
        if (statState == type) {
            removeStats(stats, button, drawableBorderId);
            statState = Constants.STATS.NONE;
        } else {
            statState = type;
            addStats(sensors, stats, positions, button, drawableId);
        }
    }


    private void addStats(Sensor[] sensors, Stat[] stats, int[] positions, Button button, int drawableId) {
        for (int i = 0; i < sensors.length; i++) {
            if (sensors[i].getSensorType() != NOT_SET)
                statsRelativeLayouts[i].add(stats[i], positions[i]);
        }

        button.setBackground(ContextCompat.getDrawable(instance, drawableId));
        button.setTextColor(getResources().getColor(R.color.white));
    }


    private void removeStats(Stat[] stats, Button button, int drawableId) {
        for (int i = 0; i < stats.length; i++) {
            statsRelativeLayouts[i].remove(stats[i]);
        }
        button.setBackground(ContextCompat.getDrawable(this, drawableId));
        button.setTextColor(getResources().getColor(R.color.orange));
    }


    private void removeAllStats() {
        removeStats(means, buttonMean, R.drawable.orange_button_border_left);
        removeStats(medians, buttonMedian, R.drawable.orange_button_border_middle);
        removeStats(modes, buttonMode, R.drawable.orange_button_border_right);
        removeStats(maxs, buttonMax, R.drawable.orange_button_border);
        removeStats(mins, buttonMin, R.drawable.orange_button_border);
    }


    private Button.OnClickListener meanClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(Constants.LOG_TAG, "DataLogsActivity.meanClickListener");
            int[] positions = workingDataSet.getMeans();
            Sensor[] sensors = workingDataSet.getSensors();
            removeStats(medians, buttonMedian, R.drawable.orange_button_border_middle);
            removeStats(modes, buttonMode, R.drawable.orange_button_border_right);
            updateStats(sensors, positions, means, Constants.STATS.MEAN, buttonMean, R.drawable.orange_button_border_left, R.drawable.orange_button_left);
        }
    };


    private Button.OnClickListener medianClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(Constants.LOG_TAG, "DataLogsActivity.medianClickListener");
            int[] positions = workingDataSet.getMedians();
            Sensor[] sensors = workingDataSet.getSensors();
            removeStats(means, buttonMean, R.drawable.orange_button_border_left);
            removeStats(modes, buttonMode, R.drawable.orange_button_border_right);
            updateStats(sensors, positions, medians, Constants.STATS.MEDIAN, buttonMedian, R.drawable.orange_button_border_middle, R.drawable.orange_button_middle);
        }
    };


    private Button.OnClickListener modeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(Constants.LOG_TAG, "DataLogsActivity.modeClickListener");
            int[] positions = workingDataSet.getModes();
            Sensor[] sensors = workingDataSet.getSensors();
            removeStats(means, buttonMean, R.drawable.orange_button_border_left);
            removeStats(medians, buttonMedian, R.drawable.orange_button_border_middle);
            updateStats(sensors, positions, modes, Constants.STATS.MODE, buttonMode, R.drawable.orange_button_border_right, R.drawable.orange_button_right);
        }
    };


    private Button.OnClickListener maxClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(Constants.LOG_TAG, "DataLogsActivity.maxClickListener");
            int[] positions = workingDataSet.getMaximums();
            Sensor[] sensors = workingDataSet.getSensors();

            isMax = !isMax;
            if (isMax) {
                addStats(sensors, maxs, positions, buttonMax, R.drawable.orange_button);
            } else {
                removeStats(maxs, buttonMax, R.drawable.orange_button_border);
            }
        }
    };


    private Button.OnClickListener minClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(Constants.LOG_TAG, "DataLogsActivity.minClickListener");
            int[] positions = workingDataSet.getMinimums();
            Sensor[] sensors = workingDataSet.getSensors();

            isMin = !isMin;
            if (isMin) {
                addStats(sensors, mins, positions, buttonMin, R.drawable.orange_button);
            } else {
                removeStats(mins, buttonMin, R.drawable.orange_button_border);
            }
        }
    };


    // Activity methods


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_logs);
        ButterKnife.bind(this);
        globalHandler = GlobalHandler.getInstance(this);
        instance = this;
        dataLogsUpdateHelper = new DataLogsUpdateHelper(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.tab_b_g_data));
        toolbar.setContentInsetsAbsolute(0,0);
        setSupportActionBar(toolbar);

        TextView flutterStatusText = (TextView)findViewById(R.id.text_flutter_connection_status);
        ImageView flutterStatusIcon = (ImageView)findViewById(R.id.image_flutter_status_icon);

        // Menu icon and text
        TextView datalogMenuEntry = (TextView)findViewById(R.id.text_menu_datalog);
        datalogMenuEntry.setTextColor(getResources().getColor(R.color.white));
        datalogMenuEntry.setCompoundDrawablesWithIntrinsicBounds(R.drawable.menu_icon_datalog, 0, 0, 0);

        if (!globalHandler.melodySmartDeviceHandler.isConnected()) {
            flutterStatusText.setText(R.string.connection_disconnected);
            flutterStatusText.setTextColor(Color.GRAY);
            flutterStatusIcon.setImageResource(R.drawable.flutterdisconnectgraphic);
            findViewById(R.id.relative_flutter_log).setVisibility(View.GONE);
        } else {
            // Flutter status icon (upper right)
            String flutterName = globalHandler.sessionHandler.getSession().getFlutter().getName();
            TextView flutterStatusButtonName = (TextView)findViewById(R.id.text_connected_flutter_name);
            flutterStatusButtonName.setText(flutterName);
            flutterStatusText.setText(R.string.connection_connected);
            flutterStatusText.setTextColor(getResources().getColor(R.color.fluttergreen));
            flutterStatusIcon.setImageResource(R.drawable.flutterconnectgraphic);
        }

        noLogsFlutterTextView = (TextView) findViewById(R.id.text_no_log_flutter);
        noLogsDeviceTextView = (TextView) findViewById(R.id.text_no_logs_device);
        dataOnFlutterRelativeContainer = (RelativeLayout) findViewById(R.id.relative_flutter_log);

        progressSensor1 = (ProgressBar) findViewById(R.id.progress_sensor_1);
        progressSensor2 = (ProgressBar) findViewById(R.id.progress_sensor_2);
        progressSensor3 = (ProgressBar) findViewById(R.id.progress_sensor_3);
        statsRelativeLayouts = new StatsRelativeLayout[3];
        statsRelativeLayouts[0] = (StatsRelativeLayout) findViewById(R.id.relative_stats_1);
        statsRelativeLayouts[1] = (StatsRelativeLayout) findViewById(R.id.relative_stats_2);
        statsRelativeLayouts[2] = (StatsRelativeLayout) findViewById(R.id.relative_stats_3);

        buttonMean = (Button) findViewById(R.id.button_mean);
        buttonMedian = (Button) findViewById(R.id.button_median);
        buttonMode = (Button) findViewById(R.id.button_mode);
        buttonMax = (Button) findViewById(R.id.button_max);
        buttonMin = (Button) findViewById(R.id.button_min);

        openLogTextView = (TextView) findViewById(R.id.text_open_log);
        sendLogTextView = (TextView) findViewById(R.id.text_send_log);
        cleanUpTextView = (TextView) findViewById(R.id.text_clean_up);
        recordDataTextView = (TextView) findViewById(R.id.text_record_data);

        imageSensor1 = (ImageView) findViewById(R.id.image_sensor_1);
        imageSensor2 = (ImageView) findViewById(R.id.image_sensor_2);
        imageSensor3 = (ImageView) findViewById(R.id.image_sensor_3);

        buttonMean.setOnClickListener(meanClickListener);
        buttonMedian.setOnClickListener(medianClickListener);
        buttonMode.setOnClickListener(modeClickListener);
        buttonMax.setOnClickListener(maxClickListener);
        buttonMin.setOnClickListener(minClickListener);

        openLogTextView.setOnClickListener(openLogClickListener);
        sendLogTextView.setOnClickListener(sendLogClickListener);
        cleanUpTextView.setOnClickListener(cleanUpClickListener);
        recordDataTextView.setOnClickListener(recordDataClickListener);

        dataOnFlutterRelativeContainer.setOnClickListener(dataSetOnFlutterListener);

        dataLogListAdapter = new DataLogListAdapter(getLayoutInflater());
        dataInstanceListAdapter = new DataInstanceListAdapter(getLayoutInflater());

        listDataLogsOnDevice = (ListView) findViewById(R.id.list_data_logs);
        listDataLogsOnDevice.setAdapter(dataLogListAdapter);
        listDataLogsOnDevice.setOnItemClickListener(onDataLogClickListener);

        listDataInstance = (ListView) findViewById(R.id.list_data_instance);
        listDataInstance.setAdapter(dataInstanceListAdapter);
        listDataInstance.setOnItemClickListener(onDataInstanceClickListener);

        statState = Constants.STATS.NONE;

        means = new Stat[3];
        for (int i = 0; i < means.length; i++)
            means[i] = new Stat("Mean", this);
        medians = new Stat[3];
        for (int i = 0; i < medians.length; i++)
            medians[i] = new Stat("Median", this);
        modes = new Stat[3];
        for (int i = 0; i < modes.length; i++)
            modes[i] = new Stat("Mode", this);
        maxs = new Stat[3];
        for (int i = 0; i < maxs.length; i++)
            maxs[i] = new Stat("Max", this);
        mins = new Stat[3];
        for (int i = 0; i < mins.length; i++)
            mins[i] = new Stat("Min", this);

        dataRecordingTimer = new DataRecordingTimer(5000, this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        globalHandler.sessionHandler.createProgressDialog(instance);
        globalHandler.sessionHandler.updateProgressDialogMessage(getString(R.string.loading_data));
        dataLogsUpdateHelper.registerStateAndUpdateLogs(new ResumeState(this));
    }

    @Override
    public void onBackPressed() {
        if (workingDataSet == null) {
            if (this.isTaskRoot())
                return;
            else
                super.onBackPressed();
        }
        else {
            workingDataSet = null;
            globalHandler.sessionHandler.createProgressDialog(instance);
            globalHandler.sessionHandler.updateProgressDialogMessage(getString(R.string.loading_data));
            dataLogsUpdateHelper.registerStateAndUpdateLogs(new ResumeState(this));
        }
    }


    @Override
    public void onOpenedLog(DataSet dataSet) {
        Log.d(Constants.LOG_TAG, "DataLogsActivity.onOpenedLog");
        if (dataLogsUpdateHelper.getDataSetOnFlutter() == dataSet) {
            if (!globalHandler.dataLoggingHandler.isLogging()) {
                SaveToKindleDialog dialog = SaveToKindleDialog.newInstance(instance, globalHandler.dataLoggingHandler.getDataName(), globalHandler.sessionHandler.getSession().getFlutter().getName());
                dialog.show(getSupportFragmentManager(), "tag");
            } else {
                loadDataSet(dataLogsUpdateHelper.getDataSetOnFlutter());
            }
        } else {
            loadDataSet(dataSet);
        }
    }


    @Override
    public void onRecordData(String name, int interval, int sample) {
        Log.d(Constants.LOG_TAG, "onDataRecord");
        GlobalHandler.getInstance(getApplicationContext()).dataLoggingHandler.startLogging(interval, sample, name);
    }


    @Override
    public void onSaveToKindle() {
        Log.d(Constants.LOG_TAG, "DataLogsActivity.onSaveToKindle");
        globalHandler.sessionHandler.createProgressDialog(this);
        String dataSetName = dataLogsUpdateHelper.getDataSetOnFlutter().getDataName();
        if (!globalHandler.dataLoggingHandler.isLogging()) {
            FileHandler.saveDataSetToFile(globalHandler, dataLogsUpdateHelper.getDataSetOnFlutter());
            globalHandler.dataLoggingHandler.deleteLog();
        }
        dataLogsUpdateHelper.registerStateAndUpdateLogs(new SaveToKindleState(this, dataSetName));
    }


    @Override
    public void onDialogDismissed() {
        globalHandler.sessionHandler.createProgressDialog(this);
        globalHandler.sessionHandler.updateProgressDialogMessage(getString(R.string.loading_data));
        dataLogsUpdateHelper.registerStateAndUpdateLogs(new ResumeState(this));
    }


    public void checkIfLogging() {
        if (globalHandler.dataLoggingHandler.isLogging()) {
            dataRecordingTimer.stopTimer();
            timerExpired();
        }
    }


    @Override
    public void timerExpired() {
        if (globalHandler.melodySmartDeviceHandler.isConnected()) {
            globalHandler.dataLoggingHandler.populatePointsAvailable(new DataLoggingHandler.DataSetPointsListener() {
                @Override
                public void onDataSetPointsPopulated(boolean isSuccess) {
                    if (isSuccess) {
                        if (globalHandler.dataLoggingHandler.isLogging()) {
                            dataRecordingTimer.startTimer();
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    globalHandler.sessionHandler.createProgressDialog(instance);
                                    globalHandler.sessionHandler.updateProgressDialogMessage(getString(R.string.done_recording));
                                    dataLogsUpdateHelper.registerStateAndUpdateLogs(new ResumeState(instance));
                                }
                            });
                        }
                    }
                }
            });
        }
    }


    // getters


    public DataSet getWorkingDataSet() { return this.workingDataSet; }
    public DataPoint getWorkingDataPoint() { return this.workingDataPoint; }
    public DataLogsUpdateHelper getDataLogsUpdateHelper() { return this.dataLogsUpdateHelper; }


    // setters


    public void setWorkingDataSet(DataSet dataSet) {
        this.workingDataSet = dataSet;
    }
    public void setWorkingDataPoint(DataPoint dataPoint) {
        this.workingDataPoint = dataPoint;
    }

}
