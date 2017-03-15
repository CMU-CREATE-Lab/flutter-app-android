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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseNavigationActivity;
import org.cmucreatelab.flutter_android.adapters.DataInstanceListAdapter;
import org.cmucreatelab.flutter_android.adapters.DataLogListAdapter;
import org.cmucreatelab.flutter_android.classes.datalogging.DataLogDetails;
import org.cmucreatelab.flutter_android.classes.datalogging.DataPoint;
import org.cmucreatelab.flutter_android.classes.datalogging.DataSet;
import org.cmucreatelab.flutter_android.classes.flutters.Flutter;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.datalogging.CleanUpAfterState;
import org.cmucreatelab.flutter_android.helpers.datalogging.CleanUpBeforeState;
import org.cmucreatelab.flutter_android.helpers.datalogging.DataLoggingHandler;
import org.cmucreatelab.flutter_android.helpers.datalogging.DataLogsHelper;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.datalogging.DataRecordingTimer;
import org.cmucreatelab.flutter_android.helpers.datalogging.OpenLogState;
import org.cmucreatelab.flutter_android.helpers.datalogging.ResumeState;
import org.cmucreatelab.flutter_android.helpers.datalogging.SaveToKindleState;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.FileHandler;
import org.cmucreatelab.flutter_android.ui.dialogs.CleanUpLogsDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.DismissDialogListener;
import org.cmucreatelab.flutter_android.ui.dialogs.EmailDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.OpenLogDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.RecordDataLoggingDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.RecordingWarningDataDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.SaveToKindleDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.SendDataLogFailedDialog;
import org.cmucreatelab.flutter_android.ui.progressbar.MeanMedianModeProgressBar;
import org.cmucreatelab.flutter_android.ui.realtivelayout.StatsRelativeLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import butterknife.ButterKnife;

import static org.cmucreatelab.flutter_android.helpers.static_classes.FlutterProtocol.InputTypes.NOT_SET;

public class DataLogsActivity extends BaseNavigationActivity implements Serializable, RecordDataLoggingDialog.DialogRecordDataLoggingListener,
        OpenLogDialog.OpenLogListener, SaveToKindleDialog.SaveToKindleListener, DismissDialogListener, DataRecordingTimer.TimeExpireListener,
        OpenLogState.OpenLogStateListener, ResumeState.ResumeStateListener, CleanUpBeforeState.CleanUpBeforeStateListener, CleanUpAfterState.CleanUpStateAfterListener, SaveToKindleState.SaveToKindleStateListener {

    public static final String DATA_LOGS_ACTIVITY_KEY = "data_logging_key";

    private GlobalHandler globalHandler;
    private DataLogsActivity instance;
    private DataLogsHelper dataLogsHelper;
    private DataSet workingDataSet;
    private DataPoint workingDataPoint;
    private DataRecordingTimer dataRecordingTimer;

    private Constants.STATS statState;
    private boolean isMax, isMin;

    private DataLogListAdapter dataLogListAdapter;
    private DataInstanceListAdapter dataInstanceListAdapter;
    private LinearLayout dataOnFlutterContainer, dataOnDeviceContainer;
    private RelativeLayout dataOnFlutterRealtiveContainer;
    private ListView listDataLogsOnDevice, listDataInstance;
    private MeanMedianModeProgressBar progressSensor2, progressSensor3;
    private ProgressBar progressSensor1;
    private StatsRelativeLayout statsRelativeLayout1;
    private ImageView workingDataPointImage, imageSensor1, imageSensor2, imageSensor3;
    private Button buttonMean, buttonMedian, buttonMode, buttonMax, buttonMin;
    private TextView openLogTextView, sendLogTextView, cleanUpTextView, recordDataTextView;


    // utility methods used by the class


    private void mathStateHelper() {
        switch (statState) {
            case NONE:
                break;
            case MEAN:
                buttonMean.setBackground(ContextCompat.getDrawable(this, R.drawable.orange_button_border_left));
                buttonMean.setTextColor(getResources().getColor(R.color.orange));
                break;
            case MEDIAN:
                buttonMedian.setBackground(ContextCompat.getDrawable(this, R.drawable.orange_button_border_middle));
                buttonMedian.setTextColor(getResources().getColor(R.color.orange));
                break;
            case MODE:
                buttonMode.setBackground(ContextCompat.getDrawable(this, R.drawable.orange_button_border_right));
                buttonMode.setTextColor(getResources().getColor(R.color.orange));
                break;
        }
    }


    private void updateDynamicViews() {
        Log.d(Constants.LOG_TAG, "DataLogsActivity.updateDynamicViews");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView sensorHigh;
                TextView sensorLow;

                if (workingDataSet == null) {
                    findViewById(R.id.include_data_log_landing).setVisibility(View.VISIBLE);
                    findViewById(R.id.include_data_log_selected).setVisibility(View.GONE);

                    if (dataLogsHelper.getDataSetsOnDevice().length > 0)
                        dataOnDeviceContainer.setVisibility(View.VISIBLE);
                    else
                        dataOnDeviceContainer.setVisibility(View.GONE);

                    if (dataLogsHelper.getDataSetOnFlutter() != null && dataLogsHelper.getDataSetOnFlutter().getData().size() > 0)
                        dataOnFlutterContainer.setVisibility(View.VISIBLE);
                    else
                        dataOnFlutterContainer.setVisibility(View.GONE);

                    TextView logTitle = (TextView) findViewById(R.id.text_current_device_title);
                    TextView textLogName = (TextView) findViewById(R.id.text_current_log_name);
                    TextView textLogPoints = (TextView) findViewById(R.id.text_num_points);
                    Flutter flutter = GlobalHandler.getInstance(getApplicationContext()).sessionHandler.getSession().getFlutter();

                    logTitle.setText(getString(R.string.on) + " " + flutter.getName() + " " + getString(R.string.flutter));
                    if (!globalHandler.dataLoggingHandler.getDataName().equals(null) && globalHandler.dataLoggingHandler.getNumberOfPoints() != 0) {
                        findViewById(R.id.relative_flutter_log).setVisibility(View.VISIBLE);
                        textLogName.setText(globalHandler.dataLoggingHandler.getDataName());
                        textLogPoints.setText(String.valueOf(globalHandler.dataLoggingHandler.getNumberOfPoints()));
                    } else {
                        findViewById(R.id.relative_flutter_log).setVisibility(View.GONE);
                    }

                    dataLogListAdapter.clearDataLogs();
                    for (DataSet dataSet : dataLogsHelper.getDataSetsOnDevice()) {
                        dataLogListAdapter.addDataLog(dataSet);
                    }

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

                } else if (workingDataPoint != null){
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
                }


            }
        });
    }


    private void loadDataSet(final DataSet dataSet) {
        Log.d(Constants.LOG_TAG, "DataLogsActivity.loadDataSet");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
                sensor1Type.setCompoundDrawables(null, ContextCompat.getDrawable(instance, dataSet.getSensors()[0].getOrangeImageIdSm()), null, null);
                sensor2Type.setCompoundDrawables(null, ContextCompat.getDrawable(instance, dataSet.getSensors()[1].getOrangeImageIdSm()), null, null);
                sensor3Type.setCompoundDrawables(null, ContextCompat.getDrawable(instance, dataSet.getSensors()[2].getOrangeImageIdSm()), null, null);

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

                updateDynamicViews();
            }
        });
    }


    // OnClick Listeners


    private TextView.OnClickListener openLogClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(Constants.LOG_TAG, "DataLogsActivity.onClickOpenLog");
            globalHandler.sessionHandler.createProgressDialog(instance);
            globalHandler.sessionHandler.updateProgressDialogMessage(getString(R.string.loading_data));
            dataLogsHelper.registerStateAndUpdateLogs(new OpenLogState(instance));
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
                SendDataLogFailedDialog sendDataLogFailedDialog = new SendDataLogFailedDialog();
                sendDataLogFailedDialog.show(getSupportFragmentManager(), "tag");
            }
        }
    };


    private TextView.OnClickListener cleanUpClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(Constants.LOG_TAG, "DataLogsActivity.onClickCleanUp");
            globalHandler.sessionHandler.createProgressDialog(instance);
            globalHandler.sessionHandler.updateProgressDialogMessage(getString(R.string.loading_data));
            dataLogsHelper.registerStateAndUpdateLogs(new CleanUpBeforeState(instance));
        }
    };


    private TextView.OnClickListener recordDataClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(Constants.LOG_TAG, "DataLogsActivity.onClickRecordData");
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
                    }
                    else {
                        RecordDataLoggingDialog recordDataLoggingDialog = RecordDataLoggingDialog.newInstance(instance);
                        recordDataLoggingDialog.show(getSupportFragmentManager(), "tag");
                    }
                }
            });
        }
    };


    private AdapterView.OnItemClickListener onDataLogClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());
            globalHandler.sessionHandler.createProgressDialog(instance);
            globalHandler.sessionHandler.updateProgressDialogMessage(getString(R.string.loading_data));
            loadDataSet(dataLogsHelper.getDataSetsOnDevice()[i]);
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
                if (dataLogsHelper.getDataSetOnFlutter() != null)
                    loadDataSet(dataLogsHelper.getDataSetOnFlutter());
            }
        }
    };


    private Button.OnClickListener meanClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(Constants.LOG_TAG, "DataLogsActivity.meanClickListener");
            mathStateHelper();
            int[] means = workingDataSet.getMeans();
            Sensor[] sensors = workingDataSet.getSensors();

            if (statState == Constants.STATS.MEAN) {
                statState = Constants.STATS.NONE;
                if (sensors[0].getSensorType() != NOT_SET) {
                    statsRelativeLayout1.remove(Constants.STATS.MEAN);
                    //progressSensor1.removeString(Constants.STATS.MEAN, means[0]);
                }
                if (sensors[1].getSensorType() != NOT_SET) {
                    progressSensor2.removeString(Constants.STATS.MEAN, means[1]);
                }
                if (sensors[2].getSensorType() != NOT_SET) {
                    progressSensor3.removeString(Constants.STATS.MEAN, means[2]);
                }
            } else {
                statState = Constants.STATS.MEAN;
                buttonMean.setBackground(ContextCompat.getDrawable(instance, R.drawable.orange_button_left));
                buttonMean.setTextColor(getResources().getColor(R.color.white));
                if (sensors[0].getSensorType() != NOT_SET) {
                    statsRelativeLayout1.add(statState, means[0]);
                    //progressSensor1.placeStringAtPosition(Constants.STATS.MEAN, means[0]);
                }
                if (sensors[1].getSensorType() != NOT_SET) {
                    progressSensor2.placeStringAtPosition(Constants.STATS.MEAN, means[1]);
                }
                if (sensors[2].getSensorType() != NOT_SET) {
                    progressSensor3.placeStringAtPosition(Constants.STATS.MEAN, means[2]);
                }
            }
        }
    };


    private Button.OnClickListener medianClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(Constants.LOG_TAG, "DataLogsActivity.medianClickListener");
            mathStateHelper();
            int[] medians = workingDataSet.getMedians();
            Sensor[] sensors = workingDataSet.getSensors();

            if (statState == Constants.STATS.MEDIAN) {
                statState = Constants.STATS.NONE;
                if (sensors[0].getSensorType() != NOT_SET) {
                    statsRelativeLayout1.remove(Constants.STATS.MEDIAN);
                    //progressSensor1.removeString(Constants.STATS.MEDIAN, medians[0]);
                }
                if (sensors[1].getSensorType() != NOT_SET) {
                    progressSensor2.removeString(Constants.STATS.MEDIAN, medians[1]);
                }
                if (sensors[2].getSensorType() != NOT_SET) {
                    progressSensor3.removeString(Constants.STATS.MEDIAN, medians[2]);
                }
            } else {
                statState = Constants.STATS.MEDIAN;
                buttonMedian.setBackground(ContextCompat.getDrawable(instance, R.drawable.orange_button_middle));
                buttonMedian.setTextColor(getResources().getColor(R.color.white));
                if (sensors[0].getSensorType() != NOT_SET) {
                    statsRelativeLayout1.add(statState, medians[0]);
                    //progressSensor1.placeStringAtPosition(statState, medians[0]);
                }
                if (sensors[1].getSensorType() != NOT_SET) {
                    progressSensor2.placeStringAtPosition(statState, medians[1]);
                }
                if (sensors[2].getSensorType() != NOT_SET) {
                    progressSensor3.placeStringAtPosition(statState, medians[2]);
                }
            }
        }
    };


    private Button.OnClickListener modeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(Constants.LOG_TAG, "DataLogsActivity.modeClickListener");
            mathStateHelper();
            int[] modes = workingDataSet.getModes();
            Sensor[] sensors = workingDataSet.getSensors();

            if (statState == Constants.STATS.MODE) {
                statState = Constants.STATS.NONE;
                if (sensors[0].getSensorType() != NOT_SET) {
                    statsRelativeLayout1.remove(Constants.STATS.MODE);
                    //progressSensor1.removeString(Constants.STATS.MODE, modes[0]);
                }
                if (sensors[1].getSensorType() != NOT_SET) {
                    progressSensor2.removeString(Constants.STATS.MODE, modes[1]);
                }
                if (sensors[2].getSensorType() != NOT_SET) {
                    progressSensor3.removeString(Constants.STATS.MODE, modes[2]);
                }
            } else {
                statState = Constants.STATS.MODE;
                buttonMode.setBackground(ContextCompat.getDrawable(instance, R.drawable.orange_button_right));
                buttonMode.setTextColor(getResources().getColor(R.color.white));
                if (sensors[0].getSensorType() != NOT_SET) {
                    statsRelativeLayout1.add(statState, modes[0]);
                    //progressSensor1.placeStringAtPosition(statState, modes[0]);
                }
                if (sensors[1].getSensorType() != NOT_SET) {
                    progressSensor2.placeStringAtPosition(statState, modes[1]);
                }
                if (sensors[2].getSensorType() != NOT_SET) {
                    progressSensor3.placeStringAtPosition(statState, modes[2]);
                }
            }
        }
    };


    private Button.OnClickListener maxClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(Constants.LOG_TAG, "DataLogsActivity.maxClickListener");
            int[] maxs = workingDataSet.getMaximums();
            Sensor[] sensors = workingDataSet.getSensors();

            isMax = !isMax;
            if (isMax) {
                buttonMax.setBackground(ContextCompat.getDrawable(instance, R.drawable.orange_button));
                buttonMax.setTextColor(getResources().getColor(R.color.white));

                if (sensors[0].getSensorType() != NOT_SET) {
                    statsRelativeLayout1.add(Constants.STATS.MAX, maxs[0]);
                    //progressSensor1.placeStringAtPosition(Constants.STATS.MAX, maxs[0]);
                }
                if (sensors[1].getSensorType() != NOT_SET) {
                    progressSensor2.placeStringAtPosition(Constants.STATS.MAX, maxs[1]);
                }
                if (sensors[2].getSensorType() != NOT_SET) {
                    progressSensor3.placeStringAtPosition(Constants.STATS.MAX, maxs[2]);
                }
            } else {
                buttonMax.setBackground(ContextCompat.getDrawable(instance, R.drawable.orange_button_border));
                buttonMax.setTextColor(getResources().getColor(R.color.orange));

                if (sensors[0].getSensorType() != NOT_SET) {
                    statsRelativeLayout1.remove(Constants.STATS.MAX);
                    //progressSensor1.removeString(Constants.STATS.MAX, maxs[0]);
                }
                if (sensors[1].getSensorType() != NOT_SET) {
                    progressSensor2.removeString(Constants.STATS.MAX, maxs[1]);
                }
                if (sensors[2].getSensorType() != NOT_SET) {
                    progressSensor3.removeString(Constants.STATS.MAX, maxs[2]);
                }
            }
        }
    };


    private Button.OnClickListener minClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(Constants.LOG_TAG, "DataLogsActivity.minClickListener");
            int[] mins = workingDataSet.getMinimums();
            Sensor[] sensors = workingDataSet.getSensors();

            isMin = !isMin;
            if (isMin) {
                buttonMin.setBackground(ContextCompat.getDrawable(instance, R.drawable.orange_button));
                buttonMin.setTextColor(getResources().getColor(R.color.white));

                if (sensors[0].getSensorType() != NOT_SET) {
                    statsRelativeLayout1.add(Constants.STATS.MIN, mins[0]);
                    //progressSensor1.placeStringAtPosition(Constants.STATS.MIN, mins[0]);
                }
                if (sensors[1].getSensorType() != NOT_SET) {
                    progressSensor2.placeStringAtPosition(Constants.STATS.MIN, mins[1]);
                }
                if (sensors[2].getSensorType() != NOT_SET) {
                    progressSensor3.placeStringAtPosition(Constants.STATS.MIN, mins[2]);
                }
            } else {
                buttonMin.setBackground(ContextCompat.getDrawable(instance, R.drawable.orange_button_border));
                buttonMin.setTextColor(getResources().getColor(R.color.orange));

                if (sensors[0].getSensorType() != NOT_SET) {
                    statsRelativeLayout1.remove(Constants.STATS.MIN);
                    //progressSensor1.removeString(Constants.STATS.MIN, mins[0]);
                }
                if (sensors[1].getSensorType() != NOT_SET) {
                    progressSensor2.removeString(Constants.STATS.MIN, mins[1]);
                }
                if (sensors[2].getSensorType() != NOT_SET) {
                    progressSensor3.removeString(Constants.STATS.MIN, mins[2]);
                }
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
        dataLogsHelper = new DataLogsHelper(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.tab_b_g_data));
        toolbar.setContentInsetsAbsolute(0,0);
        setSupportActionBar(toolbar);

        dataOnFlutterContainer = (LinearLayout) findViewById(R.id.linear_flutter_data_container);
        dataOnDeviceContainer = (LinearLayout) findViewById(R.id.linear_device_data_container);
        dataOnFlutterRealtiveContainer = (RelativeLayout) findViewById(R.id.relative_flutter_log);

        progressSensor1 = (ProgressBar) findViewById(R.id.progress_sensor_1);
        progressSensor2 = (MeanMedianModeProgressBar) findViewById(R.id.progress_sensor_2);
        progressSensor3 = (MeanMedianModeProgressBar) findViewById(R.id.progress_sensor_3);
        statsRelativeLayout1 = (StatsRelativeLayout) findViewById(R.id.relative_stats_1);

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

        dataOnFlutterRealtiveContainer.setOnClickListener(dataSetOnFlutterListener);

        dataLogListAdapter = new DataLogListAdapter(getLayoutInflater());
        dataInstanceListAdapter = new DataInstanceListAdapter(getLayoutInflater());

        listDataLogsOnDevice = (ListView) findViewById(R.id.list_data_logs);
        listDataLogsOnDevice.setAdapter(dataLogListAdapter);
        listDataLogsOnDevice.setOnItemClickListener(onDataLogClickListener);

        listDataInstance = (ListView) findViewById(R.id.list_data_instance);
        listDataInstance.setAdapter(dataInstanceListAdapter);
        listDataInstance.setOnItemClickListener(onDataInstanceClickListener);

        statState = Constants.STATS.NONE;

        dataRecordingTimer = new DataRecordingTimer(5000, this);
    }


    @Override
    protected void onResume() {
        super.onResume();

        TextView flutterStatusText = (TextView)findViewById(R.id.text_flutter_connection_status);
        ImageView flutterStatusIcon = (ImageView)findViewById(R.id.image_flutter_status_icon);
        if (!globalHandler.melodySmartDeviceHandler.isConnected()) {
            flutterStatusText.setText(R.string.connection_disconnected);
            flutterStatusText.setTextColor(Color.GRAY);
            flutterStatusIcon.setImageResource(R.drawable.flutterdisconnectgraphic);
            findViewById(R.id.linear_flutter_data_container).setVisibility(View.GONE);
            findViewById(R.id.text_record_data).setEnabled(false);
        } else {
            String flutterName = globalHandler.sessionHandler.getSession().getFlutter().getName();
            TextView flutterStatusButtonName = (TextView)findViewById(R.id.text_connected_flutter_name);
            flutterStatusButtonName.setText(flutterName);
            flutterStatusText.setText(R.string.connection_connected);
            flutterStatusText.setTextColor(getResources().getColor(R.color.fluttergreen));
            flutterStatusIcon.setImageResource(R.drawable.flutterconnectgraphic);
        }

        globalHandler.sessionHandler.createProgressDialog(instance);
        globalHandler.sessionHandler.updateProgressDialogMessage(getString(R.string.loading_data));
        dataLogsHelper.registerStateAndUpdateLogs(new ResumeState(this));
    }


    // update data log state listeners


    @Override
    public void updateFromOpenLog() {
        Log.d(Constants.LOG_TAG, "DataLogsActivity.updateFromOpenLogAfter");
        globalHandler.sessionHandler.dismissProgressDialog();
        OpenLogDialog openLogDialog = OpenLogDialog.newInstance(this, dataLogsHelper.getDataSetOnFlutter(), dataLogsHelper.getDataSetsOnDevice());
        openLogDialog.show(getSupportFragmentManager(), "tag");
        checkIfLogging();
    }


    @Override
    public void updateFromResume() {
        Log.d(Constants.LOG_TAG, "DataLogsActivity.updateFromResume");
        globalHandler.sessionHandler.dismissProgressDialog();
        updateDynamicViews();
        checkIfLogging();
    }


    @Override
    public void updateFromCleanUpBefore() {
        Log.d(Constants.LOG_TAG, "DataLogsActivity.updateFromCleanUpBefore");
        globalHandler.sessionHandler.dismissProgressDialog();
        CleanUpLogsDialog cleanUpLogsDialog = CleanUpLogsDialog.newInstance(this, dataLogsHelper.getDataSetOnFlutter(), dataLogsHelper.getDataSetsOnDevice());
        cleanUpLogsDialog.show(getSupportFragmentManager(), "tag");
    }


    @Override
    public void updateFromCleanUpAfter() {
        Log.d(Constants.LOG_TAG, "DataLogsActivity.updateFromCleanUpAfter");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                globalHandler.sessionHandler.createProgressDialog(instance);
                globalHandler.sessionHandler.updateProgressDialogMessage(getString(R.string.loading_data));
            }
        });

        // test if the current selected data log was one that got deleted
        if (workingDataSet != null) {
            if (workingDataSet.getDataName().equals(((CleanUpAfterState) dataLogsHelper.getUpdateDataLogsState()).getDeletedDataSet().getDataName())) {
                workingDataSet = null;
                workingDataPoint = null;
            }
        }
        dataLogsHelper.registerStateAndUpdateLogs(new ResumeState(this));
    }


    @Override
    public void updateFromSaveToKindle() {
        Log.d(Constants.LOG_TAG, "DataLogsActivity.updateFromSaveToKindle");
        globalHandler.sessionHandler.dismissProgressDialog();
        if (dataLogsHelper.getDataSetOnFlutter() != null) {
            loadDataSet(dataLogsHelper.getDataSetOnFlutter());
            checkIfLogging();
            return;
        }
        for (DataSet dataSet : dataLogsHelper.getDataSetsOnDevice()) {
            if (dataSet.getDataName().equals(((SaveToKindleState)dataLogsHelper.getUpdateDataLogsState()).getDataSetName())) {
                loadDataSet(dataSet);
                checkIfLogging();
                return;
            }
        }
        checkIfLogging();
    }


    // other listeners


    @Override
    public void onOpenedLog(DataSet dataSet) {
        Log.d(Constants.LOG_TAG, "DataLogsActivity.onOpenedLog");
        loadDataSet(dataSet);
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
        String dataSetName = dataLogsHelper.getDataSetOnFlutter().getDataName();
        if (!globalHandler.dataLoggingHandler.isLogging()) {
            FileHandler.saveDataSetToFile(globalHandler, dataLogsHelper.getDataSetOnFlutter());
            globalHandler.dataLoggingHandler.deleteLog();
        }
        dataLogsHelper.registerStateAndUpdateLogs(new SaveToKindleState(this, dataSetName));
    }


    @Override
    public void onDialogDismissed() {
        globalHandler.sessionHandler.createProgressDialog(this);
        globalHandler.sessionHandler.updateProgressDialogMessage(getString(R.string.loading_data));
        dataLogsHelper.registerStateAndUpdateLogs(new ResumeState(this));
    }


    private void checkIfLogging() {
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
                                    dataLogsHelper.registerStateAndUpdateLogs(new ResumeState(instance));
                                }
                            });
                        }
                    }
                }
            });
        }
    }


    // getters


    public DataLogsHelper getDataLogsHelper() { return this.dataLogsHelper; }

}
