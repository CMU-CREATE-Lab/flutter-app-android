package org.cmucreatelab.flutter_android.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseSensorReadingActivity;
import org.cmucreatelab.flutter_android.classes.Session;
import org.cmucreatelab.flutter_android.classes.datalogging.DataLogDetails;
import org.cmucreatelab.flutter_android.classes.flutters.Flutter;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.datalogging.DataLoggingHandler;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.FlutterProtocol;
import org.cmucreatelab.flutter_android.ui.dialogs.record_data_wizard.FlutterSampleDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.record_data_wizard.ReviewRecordingDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.sensors_tab.BlueSensorTypeDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.DismissDialogListener;
import org.cmucreatelab.flutter_android.ui.dialogs.NoFlutterConnectedDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.sensors_tab.DataSnapshotDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.sensors_tab.RecordingWarningSensorDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.SensorTypeDialog;

import java.util.Calendar;

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
public class SensorsActivity extends BaseSensorReadingActivity implements SensorTypeDialog.DialogSensorTypeListener, ReviewRecordingDialog.DialogRecordListener, DismissDialogListener {

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

    // animations
    private AlphaAnimation blinkAnimation;

    // state
    private Session session;


    private void updateDynamicViews() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Sensor[] sensors = session.getFlutter().getSensors();
                if (sensors[0].getSensorType() == FlutterProtocol.InputTypes.NOT_SET) {
                    textSensor1Reading.setText("");
                } else {
                    textSensor1Reading.setText(String.valueOf(sensors[0].getSensorReading()));
                }
                progress1.setProgress(sensors[0].getSensorReading());

                if (sensors[1].getSensorType() == FlutterProtocol.InputTypes.NOT_SET) {
                    textSensor2Reading.setText("");
                } else {
                    textSensor2Reading.setText(String.valueOf(sensors[1].getSensorReading()));
                }
                progress2.setProgress(sensors[1].getSensorReading());

                    if (sensors[2].getSensorType() == FlutterProtocol.InputTypes.NOT_SET) {
                    textSensor3Reading.setText("");
                } else {
                    textSensor3Reading.setText(String.valueOf(sensors[2].getSensorReading()));
                }
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

        final ImageView sensorOneHighlight = (ImageView) findViewById(R.id.image_sensor_1_highlight);
        final ImageView sensorTwoHighlight = (ImageView) findViewById(R.id.image_sensor_2_highlight);
        final ImageView sensorThreeHighlight = (ImageView) findViewById(R.id.image_sensor_3_highlight);

        if (sensors[0].getSensorType() != FlutterProtocol.InputTypes.NOT_SET || session.wasPortSetThisSession(1)) {
            selectedImage = (ImageView) findViewById(R.id.image_sensor_1);
            highText = (TextView) findViewById(R.id.text_high_1);
            lowText = (TextView) findViewById(R.id.text_low_1);
            typeText = (TextView) findViewById(R.id.text_sensor_1);
            sensor = sensors[0];
            updateStaticViewsForSensor(sensor,selectedImage,highText,lowText,typeText);
            sensorOneHighlight.clearAnimation();
        } else {
            sensorOneHighlight.startAnimation(blinkAnimation);
        }

        if (sensors[1].getSensorType() != FlutterProtocol.InputTypes.NOT_SET || session.wasPortSetThisSession(2)) {
            selectedImage = (ImageView) findViewById(R.id.image_sensor_2);
            highText = (TextView) findViewById(R.id.text_high_2);
            lowText = (TextView) findViewById(R.id.text_low_2);
            typeText = (TextView) findViewById(R.id.text_sensor_2);
            sensor = sensors[1];
            updateStaticViewsForSensor(sensor,selectedImage,highText,lowText,typeText);
            sensorTwoHighlight.clearAnimation();
        } else {
            sensorTwoHighlight.startAnimation(blinkAnimation);
        }

        if (sensors[2].getSensorType() != FlutterProtocol.InputTypes.NOT_SET || session.wasPortSetThisSession(3)) {
            selectedImage = (ImageView) findViewById(R.id.image_sensor_3);
            highText = (TextView) findViewById(R.id.text_high_3);
            lowText = (TextView) findViewById(R.id.text_low_3);
            typeText = (TextView) findViewById(R.id.text_sensor_3);
            sensor = sensors[2];
            updateStaticViewsForSensor(sensor,selectedImage,highText,lowText,typeText);
            sensorThreeHighlight.clearAnimation();
        } else {
            sensorThreeHighlight.startAnimation(blinkAnimation);
        }
    }


    // after pause/resume, determine if we should start sensor readings via 'isPlayingSensors' flag
    private void handleSensorReadingState() {
        if (!session.isSimulatingData()) {
            startSensorReading();
            //session.setFlutterMessageListener(this);
        } else {
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

        // Menu icon and text
        TextView sensorMenuEntry = (TextView)findViewById(R.id.text_menu_sensor);
        sensorMenuEntry.setTextColor(getResources().getColor(R.color.white));
        sensorMenuEntry.setCompoundDrawablesWithIntrinsicBounds(R.drawable.menu_icon_sensor, 0, 0, 0);

        if (!globalHandler.melodySmartDeviceHandler.isConnected()) {
            NoFlutterConnectedDialog.displayDialog(this, R.string.no_flutter_sensor);
        } else {
            this.session = globalHandler.sessionHandler.getSession();
            //session.setFlutterMessageListener(this);

            // set title
            String flutterName = session.getFlutter().getName();
            if (flutterName != null && flutterName.length() > 0)
                toolbar.setTitle(flutterName);
            else
                toolbar.setTitle(R.string.unknown_device);

            // init views
            textSensor1Reading = (TextView) findViewById(R.id.text_sensor_1_reading);
            textSensor2Reading = (TextView) findViewById(R.id.text_sensor_2_reading);
            textSensor3Reading = (TextView) findViewById(R.id.text_sensor_3_reading);
            progress1 = (ProgressBar) findViewById(R.id.progress_sensor_1);
            progress2 = (ProgressBar) findViewById(R.id.progress_sensor_2);
            progress3 = (ProgressBar) findViewById(R.id.progress_sensor_3);

            // Create animation to highlight a sensor that's never been set
            blinkAnimation = new AlphaAnimation((float)0.8, 0);
            blinkAnimation.setDuration(900);
            blinkAnimation.setStartOffset(150);
            blinkAnimation.setRepeatCount(Animation.INFINITE);
            blinkAnimation.setRepeatMode(Animation.REVERSE);

            // update views
            // NOTE: Must be last thing called
            updateDynamicViews();
            updateStaticViews();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        TextView flutterStatusText = (TextView)findViewById(R.id.text_flutter_connection_status);
        ImageView flutterStatusIcon = (ImageView)findViewById(R.id.image_flutter_status_icon);

        if (!globalHandler.melodySmartDeviceHandler.isConnected()) {
            NoFlutterConnectedDialog.displayDialog(this, R.string.no_flutter_sensor);

            // Flutter status icon (upper right)
            flutterStatusText.setText(R.string.connection_disconnected);
            flutterStatusText.setTextColor(Color.GRAY);
            flutterStatusIcon.setImageResource(R.drawable.flutterdisconnectgraphic);
        } else {
            String flutterName = session.getFlutter().getName();

            // Flutter status icon (upper right)
            TextView flutterStatusButtonName = (TextView) findViewById(R.id.text_connected_flutter_name);
            flutterStatusButtonName.setText(flutterName);
            flutterStatusText.setText(R.string.connection_connected);
            flutterStatusText.setTextColor(getResources().getColor(R.color.fluttergreen));
            flutterStatusIcon.setImageResource(R.drawable.flutterconnectgraphic);

            //session.setFlutterMessageListener(this);
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


    @OnClick(R.id.set_sensor_1)
    public void onClickSensor1() {
        Log.d(Constants.LOG_TAG, "onClickSensor1");
        this.selectedView = (ImageView) findViewById(R.id.image_sensor_1);
        currentHigh = (TextView) findViewById(R.id.text_high_1);
        currentLow = (TextView) findViewById(R.id.text_low_1);
        currentSensorType = (TextView) findViewById(R.id.text_sensor_1);
        SensorTypeDialog sensorTypeDialog = BlueSensorTypeDialog.newInstance(1, this);
        sensorTypeDialog.show(getSupportFragmentManager(), "tag");
    }


    @OnClick(R.id.set_sensor_2)
    public void onClickSensor2() {
        Log.d(Constants.LOG_TAG, "onClickSensor2");
        this.selectedView = (ImageView) findViewById(R.id.image_sensor_2);
        currentHigh = (TextView) findViewById(R.id.text_high_2);
        currentLow = (TextView) findViewById(R.id.text_low_2);
        currentSensorType = (TextView) findViewById(R.id.text_sensor_2);
        SensorTypeDialog sensorTypeDialog = BlueSensorTypeDialog.newInstance(2, this);
        sensorTypeDialog.show(getSupportFragmentManager(), "tag");
    }


    @OnClick(R.id.set_sensor_3)
    public void onClickSensor3() {
        Log.d(Constants.LOG_TAG, "onClickSensor3");
        this.selectedView = (ImageView) findViewById(R.id.image_sensor_3);
        currentHigh = (TextView) findViewById(R.id.text_high_3);
        currentLow = (TextView) findViewById(R.id.text_low_3);
        currentSensorType = (TextView) findViewById(R.id.text_sensor_3);
        SensorTypeDialog sensorTypeDialog = BlueSensorTypeDialog.newInstance(3, this);
        sensorTypeDialog.show(getSupportFragmentManager(), "tag");
    }


    @OnClick(R.id.button_data_snapshot)
    public void onClickDataSnapShot() {
        Log.d(Constants.LOG_TAG, "onClickDataSnapshot");
        Flutter flutter = GlobalHandler.getInstance(this).sessionHandler.getSession().getFlutter();
        Sensor sensor1 = flutter.getSensors()[0];
        Sensor sensor2 = flutter.getSensors()[1];
        Sensor sensor3 = flutter.getSensors()[2];
        DataSnapshotDialog dataSnapshotDialog = DataSnapshotDialog.newInstance(
                sensor1.getSensorReading(), sensor1,
                sensor2.getSensorReading(), sensor2,
                sensor3.getSensorReading(), sensor3,
                Calendar.getInstance().getTime().getTime()
        );
        dataSnapshotDialog.show(getSupportFragmentManager(), "tag");
    }


    @OnClick(R.id.button_record)
    public void onClickRecordData() {
        Log.d(Constants.LOG_TAG, "onClickRecordData");
        final SensorsActivity instance = this;
        globalHandler.sessionHandler.createProgressDialog(this);
        globalHandler.sessionHandler.updateProgressDialogMessage(SensorsActivity.this, "Loading data log information...");

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
                    FlutterSampleDialog flutterSampleDialog = FlutterSampleDialog.newInstance(new DataLogDetails(), instance, Constants.RECORD_DATA_WIZARD_TYPE.SENSORS_TAB, false);
                    flutterSampleDialog.show(getSupportFragmentManager(), "tag");
                }
            }
        });
    }


    // SensorTypeDialog.DialogSensorTypeListener implementation


    @Override
    public void onSensorTypeChosen(Sensor sensor) {
        int portNumber = sensor.getPortNumber();
        Log.d(Constants.LOG_TAG, "onSensorTypeChosen; PORT #"+portNumber);

        // update sensor
        GlobalHandler.getInstance(this).sessionHandler.getSession().getFlutter().updateSensorAtPort(GlobalHandler.getInstance(this).melodySmartDeviceHandler, portNumber, sensor);

        selectedView.setImageResource(sensor.getBlueImageId());
        currentSensorType.setText(getString(sensor.getSensorTypeId()));

        if (sensor.getSensorType() != FlutterProtocol.InputTypes.NOT_SET) {
            currentHigh.setText(getString(sensor.getHighTextId()));
            currentLow.setText(getString(sensor.getLowTextId()));
        } else {
            currentHigh.setText("");
            currentLow.setText("");
        }

        updateDynamicViews();
        updateStaticViews();
    }


    // RecordDataSensorDialog.DialogRecordDataSensorListener implementation


    @Override
    public void onRecordData(String name, int interval, int sample) {
        Log.d(Constants.LOG_TAG, "SensorsActivity.onRecordData");
        globalHandler.dataLoggingHandler.startLogging(interval, sample, name);
    }


    // FlutterMessageListener implementation


    @Override
    public void updateSensorViews() {
        updateDynamicViews();
    }


    @Override
    public void onDialogDismissed() {
        Log.d(Constants.LOG_TAG, "SensorsActivity.onDialogDismissed");
        onResume();
    }

}
