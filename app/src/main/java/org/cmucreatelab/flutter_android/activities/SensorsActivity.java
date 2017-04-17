package org.cmucreatelab.flutter_android.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseSensorReadingActivity;
import org.cmucreatelab.flutter_android.classes.Session;
import org.cmucreatelab.flutter_android.classes.datalogging.DataLogDetails;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.datalogging.DataLoggingHandler;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.FlutterProtocol;
import org.cmucreatelab.flutter_android.helpers.static_classes.MessageConstructor;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseDataLoggingDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.BlueSensorTypeDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.DismissDialogListener;
import org.cmucreatelab.flutter_android.ui.dialogs.NoFlutterConnectedDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.RecordDataSensorDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.RecordingWarningSensorDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.SensorTypeDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Steve on dunno...it wasn't automatically generated. (I wonder what I did differently.)
 *
 * SensorsActivity
 *
 * An activity which handles the Sensors tab on the navigation bar.
 *
 */
public class SensorsActivity extends BaseSensorReadingActivity implements SensorTypeDialog.DialogSensorTypeListener, BaseDataLoggingDialog.DialogRecordListener, DismissDialogListener {

    public static final String SENSORS_ACTIVITY_KEY = "sensors_activity_key";

    private GlobalHandler globalHandler;

    // views
    private ImageView selectedView;
    private TextView currentSensorType;
    private TextView textSensor1Reading;
    private TextView textSensor2Reading;
    private TextView textSensor3Reading;
    private TextView currentHigh;
    private TextView currentLow;
    private ProgressBar progress1;
    private ProgressBar progress2;
    private ProgressBar progress3;

    private Session session;


    private void updateDynamicViews() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Sensor[] sensors = session.getFlutter().getSensors();
                textSensor1Reading.setText(String.valueOf(sensors[0].getSensorReading()));
                progress1.setProgress(sensors[0].getSensorReading());
                textSensor2Reading.setText(String.valueOf(sensors[1].getSensorReading()));
                progress2.setProgress(sensors[1].getSensorReading());
                textSensor3Reading.setText(String.valueOf(sensors[2].getSensorReading()));
                progress3.setProgress(sensors[2].getSensorReading());

            }
        });
    }


    private void updateStaticViewsForSensor(Sensor sensor, ImageView selectedImage, TextView highText, TextView lowText, TextView typeText) {
        selectedImage.setImageResource(sensor.getBlueImageId());
        highText.setText(sensor.getHighTextId());
        lowText.setText(sensor.getLowTextId());
        typeText.setText(sensor.getSensorTypeId());
    }


    private void updateStaticViews() {
        ImageView selectedImage;
        TextView highText;
        TextView lowText;
        TextView typeText;
        Sensor sensor;
        Sensor[] sensors = session.getFlutter().getSensors();

        if (sensors[0].getSensorType() != FlutterProtocol.InputTypes.NOT_SET) {
            selectedImage = (ImageView) findViewById(R.id.image_sensor_1);
            highText = (TextView) findViewById(R.id.text_high_1);
            lowText = (TextView) findViewById(R.id.text_low_1);
            typeText = (TextView) findViewById(R.id.text_sensor_1);
            sensor = sensors[0];
            updateStaticViewsForSensor(sensor,selectedImage,highText,lowText,typeText);
        }

        if (sensors[1].getSensorType() != FlutterProtocol.InputTypes.NOT_SET) {
            selectedImage = (ImageView) findViewById(R.id.image_sensor_2);
            highText = (TextView) findViewById(R.id.text_high_2);
            lowText = (TextView) findViewById(R.id.text_low_2);
            typeText = (TextView) findViewById(R.id.text_sensor_2);
            sensor = sensors[1];
            updateStaticViewsForSensor(sensor,selectedImage,highText,lowText,typeText);
        }

        if (sensors[2].getSensorType() != FlutterProtocol.InputTypes.NOT_SET) {
            selectedImage = (ImageView) findViewById(R.id.image_sensor_3);
            highText = (TextView) findViewById(R.id.text_high_3);
            lowText = (TextView) findViewById(R.id.text_low_3);
            typeText = (TextView) findViewById(R.id.text_sensor_3);
            sensor = sensors[2];
            updateStaticViewsForSensor(sensor,selectedImage,highText,lowText,typeText);
        }
    }


    // after pause/resume, determine if we should start sensor readings via 'isPlayingSensors' flag
    private void handleSensorReadingState() {
        Button button = (Button) findViewById(R.id.button_play_pause);
        if (!session.isSimulatingData()) {
            button.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.button_icon_pause), null, null, null);
            button.setText(R.string.pause_sensors);
            startSensorReading();
            session.setFlutterMessageListener(this);
        } else {
            button.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.button_icon_play), null, null, null);
            button.setText(R.string.play_sensors);
            stopSensorReading();
        }
    }


    // Event Listeners


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);
        ButterKnife.bind(this);
        globalHandler = GlobalHandler.getInstance(getApplicationContext());

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.tab_b_g_sensor));
        toolbar.setContentInsetsAbsolute(0,0);
        setSupportActionBar(toolbar);

        TextView flutterStatusText = (TextView)findViewById(R.id.text_flutter_connection_status);
        ImageView flutterStatusIcon = (ImageView)findViewById(R.id.image_flutter_status_icon);

        if (!globalHandler.melodySmartDeviceHandler.isConnected()) {
            NoFlutterConnectedDialog.displayDialog(this, R.string.no_flutter_sensor);
            flutterStatusText.setText(R.string.connection_disconnected);
            flutterStatusText.setTextColor(Color.GRAY);
            flutterStatusIcon.setImageResource(R.drawable.flutterdisconnectgraphic);
        } else {
            this.session = globalHandler.sessionHandler.getSession();
            session.setFlutterMessageListener(this);

            // set title
            String flutterName = session.getFlutter().getName();
            if (flutterName != null && flutterName.length() > 0)
                toolbar.setTitle(flutterName);
            else
                toolbar.setTitle(R.string.unknown_device);

            TextView flutterStatusButtonName = (TextView)findViewById(R.id.text_connected_flutter_name);
            flutterStatusButtonName.setText(flutterName);
            flutterStatusText.setText(R.string.connection_connected);
            flutterStatusText.setTextColor(getResources().getColor(R.color.fluttergreen));
            flutterStatusIcon.setImageResource(R.drawable.flutterconnectgraphic);

            // init views
            textSensor1Reading = (TextView) findViewById(R.id.text_sensor_1_reading);
            textSensor2Reading = (TextView) findViewById(R.id.text_sensor_2_reading);
            textSensor3Reading = (TextView) findViewById(R.id.text_sensor_3_reading);
            progress1 = (ProgressBar) findViewById(R.id.progress_sensor_1);
            progress2 = (ProgressBar) findViewById(R.id.progress_sensor_2);
            progress3 = (ProgressBar) findViewById(R.id.progress_sensor_3);

            updateDynamicViews();
            updateStaticViews();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        globalHandler = GlobalHandler.getInstance(getApplicationContext());

        if (globalHandler.melodySmartDeviceHandler.isConnected()) {
            session.setFlutterMessageListener(this);
            handleSensorReadingState();
        }
    }


    @Override
    public void onBackPressed() {
        if (this.isTaskRoot()) {
            return;
        } else {
            super.onBackPressed();
        }
    }


    @OnClick({R.id.image_sensor_1, R.id.image_sensor_1_2})
    public void onClickSensor1() {
        Log.d(Constants.LOG_TAG, "onClickSensor1");
        this.selectedView = (ImageView) findViewById(R.id.image_sensor_1);
        currentHigh = (TextView) findViewById(R.id.text_high_1);
        currentLow = (TextView) findViewById(R.id.text_low_1);
        currentSensorType = (TextView) findViewById(R.id.text_sensor_1);
        SensorTypeDialog sensorTypeDialog = BlueSensorTypeDialog.newInstance(1, this);
        sensorTypeDialog.show(getSupportFragmentManager(), "tag");
    }


    @OnClick({R.id.image_sensor_2, R.id.image_sensor_2_2})
    public void onClickSensor2() {
        Log.d(Constants.LOG_TAG, "onClickSensor2");
        this.selectedView = (ImageView) findViewById(R.id.image_sensor_2);
        currentHigh = (TextView) findViewById(R.id.text_high_2);
        currentLow = (TextView) findViewById(R.id.text_low_2);
        currentSensorType = (TextView) findViewById(R.id.text_sensor_2);
        SensorTypeDialog sensorTypeDialog = BlueSensorTypeDialog.newInstance(2, this);
        sensorTypeDialog.show(getSupportFragmentManager(), "tag");
    }


    @OnClick({R.id.image_sensor_3, R.id.image_sensor_3_2})
    public void onClickSensor3() {
        Log.d(Constants.LOG_TAG, "onClickSensor3");
        this.selectedView = (ImageView) findViewById(R.id.image_sensor_3);
        currentHigh = (TextView) findViewById(R.id.text_high_3);
        currentLow = (TextView) findViewById(R.id.text_low_3);
        currentSensorType = (TextView) findViewById(R.id.text_sensor_3);
        SensorTypeDialog sensorTypeDialog = BlueSensorTypeDialog.newInstance(3, this);
        sensorTypeDialog.show(getSupportFragmentManager(), "tag");
    }


    @OnClick(R.id.button_play_pause)
    public void onClickPlayPause() {
        Log.d(Constants.LOG_TAG, "onClickPlayPause");
        session.setSimulatingData(!session.isSimulatingData());
        handleSensorReadingState();
    }


    @OnClick(R.id.button_record)
    public void onClickRecordData() {
        Log.d(Constants.LOG_TAG, "onClickRecordData");
        final SensorsActivity instance = this;
        globalHandler.sessionHandler.createProgressDialog(this);
        globalHandler.sessionHandler.updateProgressDialogMessage("Loading data log information...");

        globalHandler.dataLoggingHandler.populatePointsAvailable(new DataLoggingHandler.DataSetPointsListener() {
            @Override
            public void onDataSetPointsPopulated(boolean isSuccess) {
                globalHandler.sessionHandler.dismissProgressDialog();
                if (globalHandler.dataLoggingHandler.isLogging()) {
                    String dataLogName = globalHandler.dataLoggingHandler.getDataName();
                    DataLogDetails dataLogDetails = globalHandler.dataLoggingHandler.loadDataLogDetails(instance);
                    RecordingWarningSensorDialog recordingWarningSensorDialog = RecordingWarningSensorDialog.newInstance(
                            instance, dataLogName, dataLogDetails.getIntervalInt(), dataLogDetails.getIntervalString(), dataLogDetails.getTimePeriodInt(), dataLogDetails.getTimePeriodString()
                    );
                    recordingWarningSensorDialog.show(getSupportFragmentManager(), "tag");
                }
                else {
                    RecordDataSensorDialog recordDataSensorDialog = RecordDataSensorDialog.newInstance(instance, R.drawable.round_blue_button_bottom_right);
                    recordDataSensorDialog.show(getSupportFragmentManager(), "tag");
                }
            }
        });
    }


    // SensorTypeDialog.DialogSensorTypeListener implementation


    @Override
    public void onSensorTypeChosen(Sensor sensor) {
        int portNumber = sensor.getPortNumber();
        Log.d(Constants.LOG_TAG, "onSensorTypeChosen; PORT #"+portNumber);
        Sensor[] sensors = session.getFlutter().getSensors();

        // update references
        sensors[portNumber-1] = sensor;

        selectedView.setImageResource(sensor.getBlueImageId());
        currentSensorType.setText(getString(sensor.getSensorTypeId()));

        if (sensors[portNumber-1].getSensorType() != FlutterProtocol.InputTypes.NOT_SET) {
            currentHigh.setText(getString(sensor.getHighTextId()));
            currentLow.setText(getString(sensor.getLowTextId()));
        } else {
            currentHigh.setText("");
            currentLow.setText("");
        }

        // send message to flutter with sensor type
        short inputType = sensor.getSensorType();
        GlobalHandler.getInstance(this).melodySmartDeviceHandler.addMessage(MessageConstructor.constructSetInputType(sensor, inputType));

        updateDynamicViews();
    }


    // RecordDataSensorDialog.DialogRecordDataSensorListener implementation


    @Override
    public void onRecordData(String name, int interval, int sample) {
        Log.d(Constants.LOG_TAG, "SensorsActivity.onRecordData");
        globalHandler.dataLoggingHandler.startLogging(interval, sample, name);
    }


    // FlutterMessageListener implementation


    @Override
    public void onFlutterMessageReceived(String request, String response) {
        updateDynamicViews();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateStaticViews();
            }
        });
    }


    @Override
    public void onDialogDismissed() {
        Log.d(Constants.LOG_TAG, "SensorsActivity.onDialogDismissed");
        onResume();
    }

}
