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
import org.cmucreatelab.flutter_android.helpers.DataLoggingHandler;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.FileHandler;
import org.cmucreatelab.flutter_android.ui.dialogs.CleanUpLogsDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.EmailDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.NoFlutterConnectedDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.OpenLogDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.RecordDataLoggingDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.RecordingWarningDataDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.SaveToKindleDialog;
import org.cmucreatelab.flutter_android.ui.progressbar.MeanMedianModeProgressBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static org.cmucreatelab.flutter_android.helpers.static_classes.FlutterProtocol.InputTypes.NOT_SET;

public class DataLogsActivity extends BaseNavigationActivity implements Serializable, RecordDataLoggingDialog.DialogRecordDataLoggingListener, Flutter.PopulatedDataSetListener,
        DataLoggingHandler.DataSetPointsListener, OpenLogDialog.OpenLogListener, SaveToKindleDialog.SaveToKindleListener {

    public static final String DATA_LOGS_ACTIVITY_KEY = "data_logging_key";

    private GlobalHandler globalHandler;
    private DataLogsActivity instance;
    private DataLoggingHandler dataLoggingHandler;
    private DataLogListAdapter dataLogListAdapter;
    private DataInstanceListAdapter dataInstanceListAdapter;
    private DataSet dataSetOnFlutter, workingDataSet;
    private DataSet[] dataSetsOnDevice;

    private Constants.MATH_STATES mathState;
    private boolean isDataLogSelected, isMax, isMin;

    private DataPoint workingDataPoint;
    private ImageView workingDataPointImage, imageSensor1, imageSensor2, imageSensor3;
    private TextView sendLogTextView;
    private LinearLayout dataOnFlutterContainer, dataOnDeviceContainer;
    private MeanMedianModeProgressBar progressSensor1, progressSensor2, progressSensor3;
    private Button buttonMean, buttonMedian, buttonMode, buttonMax, buttonMin;


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

        if (workingDataSet.getSensors()[0].getSensorType() != NOT_SET)
            imageSensor1.setImageDrawable(ContextCompat.getDrawable(this, workingDataSet.getSensors()[0].getOrangeImageIdMd()));
        else
            imageSensor1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.grey_question_mark));

        if (workingDataSet.getSensors()[1].getSensorType() != NOT_SET)
            imageSensor2.setImageDrawable(ContextCompat.getDrawable(this, workingDataSet.getSensors()[1].getOrangeImageIdMd()));
        else
            imageSensor2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.grey_question_mark));

        if (workingDataSet.getSensors()[2].getSensorType() != NOT_SET)
            imageSensor3.setImageDrawable(ContextCompat.getDrawable(this, workingDataSet.getSensors()[2].getOrangeImageIdMd()));
        else
            imageSensor3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.grey_question_mark));
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

            updateViews();
        }
    };


    private void updateViews() {
        TextView sensorHigh;
        TextView sensorLow;

        if (workingDataSet != null && workingDataPoint != null) {
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
        }
    }


    private void mathStateHelper() {
        switch (mathState) {
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


    // OnClick Listeners


    private Button.OnClickListener meanClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(Constants.LOG_TAG, "DataLogsActivity.meanClickListener");
            mathStateHelper();
            int[] means = workingDataSet.getMeans();
            Sensor[] sensors = workingDataSet.getSensors();

            if (mathState == Constants.MATH_STATES.MEAN) {
                mathState = Constants.MATH_STATES.NONE;
                if (sensors[0].getSensorType() != NOT_SET) {
                    progressSensor1.removeString(Constants.MATH_STATES.MEAN, means[0]);
                }
                if (sensors[1].getSensorType() != NOT_SET) {
                    progressSensor2.removeString(Constants.MATH_STATES.MEAN, means[1]);
                }
                if (sensors[2].getSensorType() != NOT_SET) {
                    progressSensor3.removeString(Constants.MATH_STATES.MEAN, means[2]);
                }
            } else {
                mathState = Constants.MATH_STATES.MEAN;
                buttonMean.setBackground(ContextCompat.getDrawable(instance, R.drawable.orange_button_left));
                buttonMean.setTextColor(getResources().getColor(R.color.white));
                if (sensors[0].getSensorType() != NOT_SET) {
                    progressSensor1.placeStringAtPosition(Constants.MATH_STATES.MEAN, means[0]);
                }
                if (sensors[1].getSensorType() != NOT_SET) {
                    progressSensor2.placeStringAtPosition(Constants.MATH_STATES.MEAN, means[1]);
                }
                if (sensors[2].getSensorType() != NOT_SET) {
                    progressSensor3.placeStringAtPosition(Constants.MATH_STATES.MEAN, means[2]);
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

            if (mathState == Constants.MATH_STATES.MEDIAN) {
                mathState = Constants.MATH_STATES.NONE;
                if (sensors[0].getSensorType() != NOT_SET) {
                    progressSensor1.removeString(Constants.MATH_STATES.MEDIAN, medians[0]);
                }
                if (sensors[1].getSensorType() != NOT_SET) {
                    progressSensor2.removeString(Constants.MATH_STATES.MEDIAN, medians[1]);
                }
                if (sensors[2].getSensorType() != NOT_SET) {
                    progressSensor3.removeString(Constants.MATH_STATES.MEDIAN, medians[2]);
                }
            } else {
                mathState = Constants.MATH_STATES.MEDIAN;
                buttonMedian.setBackground(ContextCompat.getDrawable(instance, R.drawable.orange_button_middle));
                buttonMedian.setTextColor(getResources().getColor(R.color.white));
                if (sensors[0].getSensorType() != NOT_SET) {
                    progressSensor1.placeStringAtPosition(mathState, medians[0]);
                }
                if (sensors[1].getSensorType() != NOT_SET) {
                    progressSensor2.placeStringAtPosition(mathState, medians[1]);
                }
                if (sensors[2].getSensorType() != NOT_SET) {
                    progressSensor3.placeStringAtPosition(mathState, medians[2]);
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

            if (mathState == Constants.MATH_STATES.MODE) {
                mathState = Constants.MATH_STATES.NONE;
                if (sensors[0].getSensorType() != NOT_SET) {
                    progressSensor1.removeString(Constants.MATH_STATES.MODE, modes[0]);
                }
                if (sensors[1].getSensorType() != NOT_SET) {
                    progressSensor2.removeString(Constants.MATH_STATES.MODE, modes[1]);
                }
                if (sensors[2].getSensorType() != NOT_SET) {
                    progressSensor3.removeString(Constants.MATH_STATES.MODE, modes[2]);
                }
            } else {
                mathState = Constants.MATH_STATES.MODE;
                buttonMode.setBackground(ContextCompat.getDrawable(instance, R.drawable.orange_button_right));
                buttonMode.setTextColor(getResources().getColor(R.color.white));
                if (sensors[0].getSensorType() != NOT_SET) {
                    progressSensor1.placeStringAtPosition(mathState, modes[0]);
                }
                if (sensors[1].getSensorType() != NOT_SET) {
                    progressSensor2.placeStringAtPosition(mathState, modes[1]);
                }
                if (sensors[2].getSensorType() != NOT_SET) {
                    progressSensor3.placeStringAtPosition(mathState, modes[2]);
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
                    progressSensor1.placeStringAtPosition(Constants.MATH_STATES.MAX, maxs[0]);
                }
                if (sensors[1].getSensorType() != NOT_SET) {
                    progressSensor2.placeStringAtPosition(Constants.MATH_STATES.MAX, maxs[1]);
                }
                if (sensors[2].getSensorType() != NOT_SET) {
                    progressSensor3.placeStringAtPosition(Constants.MATH_STATES.MAX, maxs[2]);
                }
            } else {
                buttonMax.setBackground(ContextCompat.getDrawable(instance, R.drawable.orange_button_border));
                buttonMax.setTextColor(getResources().getColor(R.color.orange));

                if (sensors[0].getSensorType() != NOT_SET) {
                    progressSensor1.removeString(Constants.MATH_STATES.MAX, maxs[0]);
                }
                if (sensors[1].getSensorType() != NOT_SET) {
                    progressSensor2.removeString(Constants.MATH_STATES.MAX, maxs[1]);
                }
                if (sensors[2].getSensorType() != NOT_SET) {
                    progressSensor3.removeString(Constants.MATH_STATES.MAX, maxs[2]);
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
                    progressSensor1.placeStringAtPosition(Constants.MATH_STATES.MIN, mins[0]);
                }
                if (sensors[1].getSensorType() != NOT_SET) {
                    progressSensor2.placeStringAtPosition(Constants.MATH_STATES.MIN, mins[1]);
                }
                if (sensors[2].getSensorType() != NOT_SET) {
                    progressSensor3.placeStringAtPosition(Constants.MATH_STATES.MIN, mins[2]);
                }
            } else {
                buttonMin.setBackground(ContextCompat.getDrawable(instance, R.drawable.orange_button_border));
                buttonMin.setTextColor(getResources().getColor(R.color.orange));

                if (sensors[0].getSensorType() != NOT_SET) {
                    progressSensor1.removeString(Constants.MATH_STATES.MIN, mins[0]);
                }
                if (sensors[1].getSensorType() != NOT_SET) {
                    progressSensor2.removeString(Constants.MATH_STATES.MIN, mins[1]);
                }
                if (sensors[2].getSensorType() != NOT_SET) {
                    progressSensor3.removeString(Constants.MATH_STATES.MIN, mins[2]);
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

        progressSensor1 = (MeanMedianModeProgressBar) findViewById(R.id.progress_sensor_1);
        progressSensor2 = (MeanMedianModeProgressBar) findViewById(R.id.progress_sensor_2);
        progressSensor3 = (MeanMedianModeProgressBar) findViewById(R.id.progress_sensor_3);
        buttonMean = (Button) findViewById(R.id.button_mean);
        buttonMedian = (Button) findViewById(R.id.button_median);
        buttonMode = (Button) findViewById(R.id.button_mode);
        buttonMax = (Button) findViewById(R.id.button_max);
        buttonMin = (Button) findViewById(R.id.button_min);

        imageSensor1 = (ImageView) findViewById(R.id.image_sensor_1);
        imageSensor2 = (ImageView) findViewById(R.id.image_sensor_2);
        imageSensor3 = (ImageView) findViewById(R.id.image_sensor_3);

        buttonMean.setOnClickListener(meanClickListener);
        buttonMedian.setOnClickListener(medianClickListener);
        buttonMode.setOnClickListener(modeClickListener);
        buttonMax.setOnClickListener(maxClickListener);
        buttonMin.setOnClickListener(minClickListener);

        mathState = Constants.MATH_STATES.NONE;
    }


    @Override
    public void onResume() {
        Log.d(Constants.LOG_TAG, "onResume");
        super.onResume();
        globalHandler = GlobalHandler.getInstance(getApplicationContext());
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

            updateViews();
        }
    }

    @Override
    public void onDismissed() {
        onResume();
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
        globalHandler.sessionHandler.createProgressDialog(this);
        globalHandler.sessionHandler.updateProgressDialogMessage("Loading data log information...");

        globalHandler.dataLoggingHandler.populatePointsAvailable(new DataLoggingHandler.DataSetPointsListener() {
            @Override
            public void onDataSetPointsPopulated(boolean isSuccess) {
                globalHandler.sessionHandler.dismissProgressDialog();
                if (globalHandler.dataLoggingHandler.getIsLogging()) {
                    String dataLogName = globalHandler.dataLoggingHandler.getDataName();
                    DataLogDetails dataLogDetails = globalHandler.dataLoggingHandler.loadDataLogdeatils(instance);
                    RecordingWarningDataDialog recordingWarningDataDialog = RecordingWarningDataDialog.newInstance(
                            dataLogName, dataLogDetails.getIntervalInt(), dataLogDetails.getIntervalString(), dataLogDetails.getTimePeriodInt(), dataLogDetails.getTimePeriodString()
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
        SaveToKindleDialog dialog = SaveToKindleDialog.newInstance(this, globalHandler.dataLoggingHandler.getDataName(), globalHandler.sessionHandler.getSession().getFlutter().getName());
        dialog.show(getSupportFragmentManager(), "tag");
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
        workingDataPoint = null;
        updateViews();
    }


    @Override
    public void onSaveToKindle() {
        Log.d(Constants.LOG_TAG, "DataLogsActivity.onSaveToKindle");
        loadFlutterDataLog();
    }

}
