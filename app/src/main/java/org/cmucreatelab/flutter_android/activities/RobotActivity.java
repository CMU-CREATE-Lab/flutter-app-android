package org.cmucreatelab.flutter_android.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.cmucreatelab.android.melodysmart.models.MelodySmartMessage;
import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseSensorReadingActivity;
import org.cmucreatelab.flutter_android.classes.Session;
import org.cmucreatelab.flutter_android.classes.outputs.Output;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.outputs.Speaker;
import org.cmucreatelab.flutter_android.classes.outputs.TriColorLed;
import org.cmucreatelab.flutter_android.classes.sensors.NoSensor;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.settings.Settings;
import org.cmucreatelab.flutter_android.classes.settings.SettingsAmplitude;
import org.cmucreatelab.flutter_android.classes.settings.SettingsChange;
import org.cmucreatelab.flutter_android.classes.settings.SettingsCumulative;
import org.cmucreatelab.flutter_android.classes.settings.SettingsFrequency;
import org.cmucreatelab.flutter_android.classes.settings.SettingsProportional;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.FlutterProtocol;
import org.cmucreatelab.flutter_android.helpers.static_classes.MessageConstructor;
import org.cmucreatelab.flutter_android.ui.dialogs.GreenSensorTypeDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.NoFlutterConnectedDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.SensorTypeDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.outputs.led.LedDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.outputs.servo.ServoDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.outputs.speaker.SpeakerDialog;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RobotActivity extends BaseSensorReadingActivity implements ServoDialog.DialogServoListener, LedDialog.DialogLedListener, SpeakerDialog.DialogSpeakerListener,
        SensorTypeDialog.DialogSensorTypeListener{

    private RobotActivity instance;
    private Session session;

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        private int seekBarValue=0;

        @Override
        public void onProgressChanged(SeekBar seekBar, final int i, boolean b) {
            Log.d(Constants.LOG_TAG, "onProgressChanged");
            seekBarValue = i;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Sensor[] sensors = session.getFlutter().getSensors();
                    TextView sensorReadingText;

                    if (sensors[0].getSensorType() != FlutterProtocol.InputTypes.NOT_SET) {
                        sensorReadingText = (TextView) findViewById(R.id.text_sensor_1_reading);
                        sensorReadingText.setText(String.valueOf(i));
                    }
                    if (sensors[1].getSensorType() != FlutterProtocol.InputTypes.NOT_SET) {
                        sensorReadingText = (TextView) findViewById(R.id.text_sensor_2_reading);
                        sensorReadingText.setText(String.valueOf(i));
                    }
                    if (sensors[2].getSensorType() != FlutterProtocol.InputTypes.NOT_SET) {
                        sensorReadingText = (TextView) findViewById(R.id.text_sensor_3_reading);
                        sensorReadingText.setText(String.valueOf(i));
                    }
                }
            });
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (session.isSimulatingData()) {
                session.getFlutter().setSensorValues(seekBarValue,seekBarValue,seekBarValue);
            }
        }
    };


    private void updateSensorViews() {
        Sensor[] sensors = session.getFlutter().getSensors();
        TextView sensorText;

        sensorText = (TextView) findViewById(R.id.text_sensor_1);
        if (sensors[0].getSensorType() != FlutterProtocol.InputTypes.NOT_SET) {
            sensorText.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, sensors[0].getWhiteImageIdSm()), null, null);
            sensorText.setText(sensors[0].getTypeTextId());
        } else {
            sensorText.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.questionmark_white_sm20), null, null);
            sensorText.setText(R.string.set_sensor);
        }

        sensorText = (TextView) findViewById(R.id.text_sensor_2);
        if (sensors[1].getSensorType() != FlutterProtocol.InputTypes.NOT_SET) {
            sensorText.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, sensors[1].getWhiteImageIdSm()), null, null);
            sensorText.setText(sensors[1].getTypeTextId());
        } else {
            sensorText.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.questionmark_white_sm20), null, null);
            sensorText.setText(R.string.set_sensor);
        }

        sensorText = (TextView) findViewById(R.id.text_sensor_3);
        if (sensors[2].getSensorType() != FlutterProtocol.InputTypes.NOT_SET) {
            sensorText.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, sensors[2].getWhiteImageIdSm()), null, null);
            sensorText.setText(sensors[2].getTypeTextId());
        } else {
            sensorText.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.questionmark_white_sm20), null, null);
            sensorText.setText(R.string.set_sensor);
        }
    }


    private void updateDynamicViews() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Sensor[] sensors = session.getFlutter().getSensors();
                TextView sensorReadingText;

                sensorReadingText = (TextView) findViewById(R.id.text_sensor_1_reading);
                if (sensors[0].getSensorType() != FlutterProtocol.InputTypes.NOT_SET)
                    sensorReadingText.setText(String.valueOf(sensors[0].getSensorReading()));
                else
                    sensorReadingText.setText("");

                sensorReadingText = (TextView) findViewById(R.id.text_sensor_2_reading);
                if (sensors[1].getSensorType() != FlutterProtocol.InputTypes.NOT_SET)
                    sensorReadingText.setText(String.valueOf(sensors[1].getSensorReading()));
                else
                    sensorReadingText.setText("");

                sensorReadingText = (TextView) findViewById(R.id.text_sensor_3_reading);
                if (sensors[2].getSensorType() != FlutterProtocol.InputTypes.NOT_SET)
                    sensorReadingText.setText(String.valueOf(sensors[2].getSensorReading()));
                else
                    sensorReadingText.setText("");
            }
        });
    }


    private void updateSimulatedView() {
        if (session.isSimulatingData()) {
            Button sensorData = (Button) findViewById(R.id.button_sensor_data);
            sensorData.setBackground(ContextCompat.getDrawable(this, R.drawable.round_gray_white_left));
            sensorData.setTextColor(Color.GRAY);

            Button simulateData = (Button) findViewById(R.id.button_simulate_data);
            simulateData.setBackground(ContextCompat.getDrawable(this, R.drawable.round_green_button_right));
            simulateData.setTextColor(Color.WHITE);

            SeekBar simulatedSeekbar = (SeekBar) findViewById(R.id.seekbar_simulated_data);
            simulatedSeekbar.setVisibility(View.VISIBLE);
            simulatedSeekbar.setProgress(0);
        } else {
            Button sensorData = (Button) findViewById(R.id.button_sensor_data);
            sensorData.setBackground(ContextCompat.getDrawable(this, R.drawable.round_green_button_left));
            sensorData.setTextColor(Color.WHITE);

            Button simulateData = (Button) findViewById(R.id.button_simulate_data);
            simulateData.setBackground(ContextCompat.getDrawable(this, R.drawable.round_gray_white_right));
            simulateData.setTextColor(Color.GRAY);

            SeekBar simulatedSeekbar = (SeekBar) findViewById(R.id.seekbar_simulated_data);
            simulatedSeekbar.setVisibility(View.INVISIBLE);
        }
    }


    private void updateLinkedViews() {
        Log.d(Constants.LOG_TAG, "updateLinkedViews");
        Servo[] servos = session.getFlutter().getServos();
        TriColorLed[] triColorLeds = session.getFlutter().getTriColorLeds();
        Speaker speaker = session.getFlutter().getSpeaker();

        // servos link check
        for (int i = 0; i < servos.length + triColorLeds.length + 1; i++) {
            Output[] outputs = new Output[7];
            RelativeLayout currentLayout = null;
            ViewGroup linkAndSensor;
            ImageView questionMark = null;
            ImageView link;
            ImageView sensor;

            switch (i) {
                case 0:
                    currentLayout = (RelativeLayout) findViewById(R.id.relative_servo_1);
                    questionMark = (ImageView) findViewById(R.id.image_servo_1);
                    outputs[0] = servos[0];
                    break;
                case 1:
                    currentLayout = (RelativeLayout) findViewById(R.id.relative_servo_2);
                    questionMark = (ImageView) findViewById(R.id.image_servo_2);
                    outputs[1] = servos[1];
                    break;
                case 2:
                    currentLayout = (RelativeLayout) findViewById(R.id.relative_servo_3);
                    questionMark = (ImageView) findViewById(R.id.image_servo_3);
                    outputs[2] = servos[2];
                    break;
                case 3:
                    currentLayout = (RelativeLayout) findViewById(R.id.relative_led_1);
                    questionMark = (ImageView) findViewById(R.id.image_led_1);
                    outputs[3] = triColorLeds[0].getRedLed();
                    break;
                case 4:
                    currentLayout = (RelativeLayout) findViewById(R.id.relative_led_2);
                    questionMark = (ImageView) findViewById(R.id.image_led_2);
                    outputs[4] = triColorLeds[1].getRedLed();
                    break;
                case 5:
                    currentLayout = (RelativeLayout) findViewById(R.id.relative_led_3);
                    questionMark = (ImageView) findViewById(R.id.image_led_3);
                    outputs[5] = triColorLeds[2].getRedLed();
                    break;
                case 6:
                    currentLayout = (RelativeLayout) findViewById(R.id.relative_speaker);
                    questionMark = (ImageView) findViewById(R.id.image_speaker);
                    outputs[6] = speaker.getVolume();
                    break;
            }
            if (outputs[i].isLinked()) {
                if (currentLayout != null && questionMark != null) {
                    currentLayout.setVisibility(View.VISIBLE);
                    questionMark.setVisibility(View.INVISIBLE);
                    linkAndSensor = ((ViewGroup)currentLayout.getChildAt(0));
                    link = (ImageView) linkAndSensor.getChildAt(0);
                    sensor = (ImageView) linkAndSensor.getChildAt(1);
                    link.setImageResource(outputs[i].getSettings().getRelationship().getGreyImageIdSm());

                    Settings settings = outputs[i].getSettings();

                    // TODO @tasota handle finding the sensor more cleanly?
                    int imageRes = new NoSensor(0).getGreyImageIdSm();
                    if (settings.getClass() == SettingsProportional.class && ((SettingsProportional)settings).getSensorPortNumber() != 0) {
                        imageRes = session.getFlutter().getSensors()[((SettingsProportional) settings).getSensorPortNumber() - 1].getGreyImageIdSm();
                    } else if (settings.getClass() == SettingsAmplitude.class && ((SettingsAmplitude)settings).getSensorPortNumber() != 0) {
                        imageRes = session.getFlutter().getSensors()[((SettingsAmplitude) settings).getSensorPortNumber() - 1].getGreyImageIdSm();
                    } else if (settings.getClass() == SettingsFrequency.class && ((SettingsFrequency)settings).getSensorPortNumber() != 0) {
                        imageRes = session.getFlutter().getSensors()[((SettingsFrequency) settings).getSensorPortNumber() - 1].getGreyImageIdSm();
                    } else if (settings.getClass() == SettingsChange.class && ((SettingsChange)settings).getSensorPortNumber() != 0) {
                        imageRes = session.getFlutter().getSensors()[((SettingsChange) settings).getSensorPortNumber() - 1].getGreyImageIdSm();
                    } else if (settings.getClass() == SettingsCumulative.class && ((SettingsCumulative)settings).getSensorPortNumber() != 0) {
                        imageRes = session.getFlutter().getSensors()[((SettingsCumulative) settings).getSensorPortNumber() - 1].getGreyImageIdSm();
                    }
                    sensor.setImageResource(imageRes);
                }
            } else {
                if (currentLayout != null && questionMark != null) {
                    currentLayout.setVisibility(View.INVISIBLE);
                    questionMark.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    // OnClickListeners

    private void onClickSensor(int portNumber) {
        SensorTypeDialog sensorTypeDialog = GreenSensorTypeDialog.newInstance(portNumber, instance);
        sensorTypeDialog.show(getSupportFragmentManager(), "tag");
    }
    private View.OnClickListener sensor1OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onClickSensor(1);
        }
    };
    private View.OnClickListener sensor2OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onClickSensor(2);
        }
    };
    private View.OnClickListener sensor3OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onClickSensor(3);
        }
    };


    // Class methods


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot);
        ButterKnife.bind(this);
        GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.tab_b_g_robot));
        toolbar.setContentInsetsAbsolute(0,0);
        setSupportActionBar(toolbar);
        TextView flutterStatusText = (TextView)findViewById(R.id.text_flutter_connection_status);
        ImageView flutterStatusIcon = (ImageView)findViewById(R.id.image_flutter_status_icon);

        // Menu icon and text
        TextView robotMenuEntry = (TextView)findViewById(R.id.text_menu_robot);
        robotMenuEntry.setTextColor(getResources().getColor(R.color.white));
        robotMenuEntry.setCompoundDrawablesWithIntrinsicBounds(R.drawable.menu_icon_robot, 0, 0, 0);

        if (!globalHandler.melodySmartDeviceHandler.isConnected()) {
            NoFlutterConnectedDialog.displayDialog(this, R.string.no_flutter_robot);
            flutterStatusText.setText(R.string.connection_disconnected);
            flutterStatusText.setTextColor(Color.GRAY);
            flutterStatusIcon.setImageResource(R.drawable.flutterdisconnectgraphic);
        } else {
            instance = this;
            this.session = globalHandler.sessionHandler.getSession();
            SeekBar simulatedSeekbar = (SeekBar) findViewById(R.id.seekbar_simulated_data);
            simulatedSeekbar.setOnSeekBarChangeListener(seekBarChangeListener);

            // Flutter status icon (upper right)
            String flutterName = session.getFlutter().getName();
            TextView flutterStatusButtonName = (TextView)findViewById(R.id.text_connected_flutter_name);
            flutterStatusButtonName.setText(flutterName);
            flutterStatusText.setText(R.string.connection_connected);
            flutterStatusText.setTextColor(getResources().getColor(R.color.fluttergreen));
            flutterStatusIcon.setImageResource(R.drawable.flutterconnectgraphic);

            TextView sensor1Text = (TextView) findViewById(R.id.text_sensor_1);
            TextView sensor2Text = (TextView) findViewById(R.id.text_sensor_2);
            TextView sensor3Text = (TextView) findViewById(R.id.text_sensor_3);
            sensor1Text.setOnClickListener(sensor1OnClickListener);
            sensor2Text.setOnClickListener(sensor2OnClickListener);
            sensor3Text.setOnClickListener(sensor3OnClickListener);

            updateSensorViews();
            updateDynamicViews();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());
        if (globalHandler.melodySmartDeviceHandler.isConnected()) {
            this.session.setFlutterMessageListener(this);
            updateLinkedViews();
            updateSimulatedView();
            if (!session.isSimulatingData()) startSensorReading();
        }
    }


    // Dialog Listeners


    @Override
    public void onServoLinkListener(MelodySmartMessage message) {
        GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());

        Log.d(Constants.LOG_TAG, "onServoLinkListener");
        globalHandler.melodySmartDeviceHandler.addMessage(message);
        updateLinkedViews();
    }


    @Override
    public void onLedLinkListener(ArrayList<MelodySmartMessage> msgs) {
        GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());

        Log.d(Constants.LOG_TAG, "onLedLinkCreated");
        for (MelodySmartMessage message : msgs) {
            globalHandler.melodySmartDeviceHandler.addMessage(message);
        }
        updateLinkedViews();
    }


    @Override
    public void onSpeakerLinkListener(ArrayList<MelodySmartMessage> msgs) {
        GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());

        Log.d(Constants.LOG_TAG, "onSpeakerLinkCreated");
        for (MelodySmartMessage message : msgs) {
            globalHandler.melodySmartDeviceHandler.addMessage(message);
        }
        updateLinkedViews();
    }


    // onClick listeners


    private void onClickServo1() {
        Log.d(Constants.LOG_TAG, "onClickServo1");
        Servo[] servos = session.getFlutter().getServos();

        ServoDialog dialog = ServoDialog.newInstance(servos[0], this);
        dialog.show(getSupportFragmentManager(), "tag");
    }
    @OnClick(R.id.image_servo_1)
    public void onClickServo1Image() {
        onClickServo1();
    }
    @OnClick(R.id.relative_servo_1)
    public void onClickServo1Relative() {
        onClickServo1();
    }


    private void onClickServo2() {
        Log.d(Constants.LOG_TAG, "onClickServo2");
        Servo[] servos = session.getFlutter().getServos();

        ServoDialog dialog = ServoDialog.newInstance(servos[1], this);
        dialog.show(getSupportFragmentManager(), "tag");
    }
    @OnClick(R.id.image_servo_2)
    public void onClickServo2Image() {
        onClickServo2();
    }
    @OnClick(R.id.relative_servo_2)
    public void onClickServo2Relative() {
        onClickServo2();
    }


    private void onClickServo3() {
        Log.d(Constants.LOG_TAG, "onClickServo3");
        Servo[] servos = session.getFlutter().getServos();

        ServoDialog dialog = ServoDialog.newInstance(servos[2], this);
        dialog.show(getSupportFragmentManager(), "tag");
    }
    @OnClick(R.id.image_servo_3)
    public void onClickServo3Image() {
        onClickServo3();
    }
    @OnClick(R.id.relative_servo_3)
    public void onclickServo3Relative() {
        onClickServo3();
    }


    private void onClickLed1() {
        Log.d(Constants.LOG_TAG, "onClickLed1");
        TriColorLed[] triColorLeds = session.getFlutter().getTriColorLeds();

        LedDialog dialog = LedDialog.newInstance(triColorLeds[0], this);
        dialog.show(getSupportFragmentManager(), "tag");
    }
    @OnClick(R.id.image_led_1)
    public void onClickLed1Image() {
        onClickLed1();
    }
    @OnClick(R.id.relative_led_1)
    public void onClickLed1Relative() {
        onClickLed1();
    }


    private void onClickLed2() {
        Log.d(Constants.LOG_TAG, "onClickLed2");
        TriColorLed[] triColorLeds = session.getFlutter().getTriColorLeds();

        LedDialog dialog = LedDialog.newInstance(triColorLeds[1], this);
        dialog.show(getSupportFragmentManager(), "tag");
    }
    @OnClick(R.id.image_led_2)
    public void onClickLed2Image() {
        onClickLed2();
    }
    @OnClick(R.id.relative_led_2)
    public void onClickLed2Relative() {
        onClickLed2();
    }


    private void onClickLed3() {
        Log.d(Constants.LOG_TAG, "onClickLed3");
        TriColorLed[] triColorLeds = session.getFlutter().getTriColorLeds();

        LedDialog dialog = LedDialog.newInstance(triColorLeds[2], this);
        dialog.show(getSupportFragmentManager(), "tag");
    }
    @OnClick(R.id.image_led_3)
    public void onClickLed3Image() {
        onClickLed3();
    }
    @OnClick(R.id.relative_led_3)
    public void onClickLed3Relative() {
        onClickLed3();
    }


    private void onClickSpeaker() {
        Log.d(Constants.LOG_TAG, "onClickSpeaker");
        Speaker speaker = session.getFlutter().getSpeaker();

        SpeakerDialog dialog = SpeakerDialog.newInstance(speaker, this);
        dialog.show(getSupportFragmentManager(), "tag");
    }
    @OnClick(R.id.image_speaker)
    public void onClickSpeakerImage() {
        onClickSpeaker();
    }
    @OnClick(R.id.relative_speaker)
    public void onClickSpeakerRelative() {
        onClickSpeaker();
    }


    @OnClick(R.id.button_sensor_data)
    public void onClickSensorData() {
        Log.d(Constants.LOG_TAG, "onClickSensorData");
        if (session.isSimulatingData()) {
            session.setSimulatingData(false);
            updateSimulatedView();
            startSensorReading();
        }
    }


    @OnClick(R.id.button_simulate_data)
    public void onClickSimulateData() {
        Log.d(Constants.LOG_TAG, "onClickSimulateData");
        if (!session.isSimulatingData()) {
            session.getFlutter().setSensorValues(0,0,0);
            session.setSimulatingData(true);
            stopSensorReading();
            updateSimulatedView();
            updateSensorViews();
        }
    }


    // MelodySmartMessageListener implementation


    @Override
    public void onFlutterMessageReceived(String request, String response) {
        updateDynamicViews();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateSensorViews();
            }
        });
    }


    @Override
    public void onSensorTypeChosen(Sensor sensor) {
        int portNumber = sensor.getPortNumber();
        Log.d(Constants.LOG_TAG, "onSensorTypeChosen; PORT #"+portNumber);
        Sensor[] sensors = session.getFlutter().getSensors();

        // update references
        sensors[portNumber-1] = sensor;

        // send message to flutter with sensor type
        short inputType = sensor.getSensorType();
        GlobalHandler.getInstance(this).melodySmartDeviceHandler.addMessage(MessageConstructor.constructSetInputType(sensor, inputType));

        updateSensorViews();
        updateLinkedViews();
    }
}
