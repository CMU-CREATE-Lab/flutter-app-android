package org.cmucreatelab.flutter_android.activities;

import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
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
import org.cmucreatelab.flutter_android.classes.relationships.Constant;
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
                GlobalHandler.getInstance(getApplicationContext()).melodySmartDeviceHandler.addMessage(MessageConstructor.constructSimulateData(seekBarValue,seekBarValue,seekBarValue));
            }
        }
    };


    private void updateLedCircleColors(final int ledNumber, final TriColorLed triColorLed) {
        Log.v(Constants.LOG_TAG, "updateLedCircleColors");
        final View[] circle_views = new View[] {
                findViewById(R.id.view_color_1),
                findViewById(R.id.view_color_2),
                findViewById(R.id.view_color_3)
        };
        final View[] halfcircle_views = new View[] {
                findViewById(R.id.view_halfcolor_1),
                findViewById(R.id.view_halfcolor_2),
                findViewById(R.id.view_halfcolor_3)
        };
        if (ledNumber > circle_views.length || ledNumber <= 0) {
            Log.e(Constants.LOG_TAG, "updateLedCircleColors: received bad ledNumber="+ledNumber);
            return;
        }
        if (!triColorLed.getRedLed().isLinked() && !triColorLed.getGreenLed().isLinked() && !triColorLed.getBlueLed().isLinked()) {
            Log.e(Constants.LOG_TAG, "updateLedCircleColors: one of LEDs is currently not linked.");
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View circleView = circle_views[ledNumber-1];
                View halfCircleView = halfcircle_views[ledNumber-1];
                int minCircle = TriColorLed.getHalfCircleFromColor(triColorLed.getMinColorHex());
                int maxCircle = TriColorLed.getCircleFromColor(triColorLed.getMaxColorHex());

                // set the full circle's background
                circleView.setBackground( getResources().getDrawable(maxCircle) );

                // NOTE: ClipDrawable levels range from 0-10000, from completely clipped to no clip
                // set the half circle's background
                ClipDrawable clipDrawable = (ClipDrawable) getResources().getDrawable( minCircle );
                halfCircleView.setBackground(clipDrawable);
                clipDrawable.setLevel(5000);
                // if the link uses Constant relationship, do not display a minimum color
                if (triColorLed.getRedLed().getSettings().getRelationship() == Constant.getInstance() ||
                        triColorLed.getGreenLed().getSettings().getRelationship() == Constant.getInstance() ||
                        triColorLed.getBlueLed().getSettings().getRelationship() == Constant.getInstance() ) {
                    clipDrawable.setLevel(0);
                }
            }
        });
    }


    private void updateSensorViews() {
        Sensor[] sensors = session.getFlutter().getSensors();
        TextView sensorText;

        sensorText = (TextView) findViewById(R.id.text_sensor_1);
        sensorText.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, sensors[0].getWhiteImageIdSm()), null, null);
        sensorText.setText(sensors[0].getTypeTextId());

        sensorText = (TextView) findViewById(R.id.text_sensor_2);
        sensorText.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, sensors[1].getWhiteImageIdSm()), null, null);
        sensorText.setText(sensors[1].getTypeTextId());

        sensorText = (TextView) findViewById(R.id.text_sensor_3);
        sensorText.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, sensors[2].getWhiteImageIdSm()), null, null);
        sensorText.setText(sensors[2].getTypeTextId());
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
        for (int i = 0; i < servos.length + triColorLeds.length + 2; i++) {
            Output[] outputs = new Output[8];
            RelativeLayout currentLayout = null;
            ViewGroup linkAndSensor;
            ImageView questionMark = null;
            ImageView link;
            ImageView sensor;
            int ledNumber = 0;

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
                    ledNumber = 1;
                    break;
                case 4:
                    currentLayout = (RelativeLayout) findViewById(R.id.relative_led_2);
                    questionMark = (ImageView) findViewById(R.id.image_led_2);
                    outputs[4] = triColorLeds[1].getRedLed();
                    ledNumber = 2;
                    break;
                case 5:
                    currentLayout = (RelativeLayout) findViewById(R.id.relative_led_3);
                    questionMark = (ImageView) findViewById(R.id.image_led_3);
                    outputs[5] = triColorLeds[2].getRedLed();
                    ledNumber = 3;
                    break;
                case 6:
                    currentLayout = (RelativeLayout) findViewById(R.id.relative_speaker);
                    questionMark = (ImageView) findViewById(R.id.image_speaker);
                    outputs[6] = speaker.getVolume();
                    break;
                case 7:
                    currentLayout = (RelativeLayout) findViewById(R.id.relative_speaker2);
                    questionMark = (ImageView) findViewById(R.id.image_speaker);
                    outputs[7] = speaker.getPitch();
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

                    if (ledNumber > 0) {
                        TriColorLed led = triColorLeds[ledNumber-1];
                        updateLedCircleColors(ledNumber, led);
                    }

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


    private void onClickServo(int portNumber) {
        Log.d(Constants.LOG_TAG, "RobotActivity.onClickServo " + portNumber);
        Log.d(Constants.LOG_TAG, "onClickServo1");
        Servo[] servos = session.getFlutter().getServos();

        if (portNumber >= 0 || portNumber <= 2){
            ServoDialog dialog = ServoDialog.newInstance(servos[portNumber-1], this);
            dialog.show(getSupportFragmentManager(), "tag");
        }
    }
    private View.OnClickListener servo3FrameClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onClickServo(3);
        }
    };
    private View.OnClickListener servo2FrameClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onClickServo(2);
        }
    };
    private View.OnClickListener servo1FrameClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onClickServo(1);
        }
    };


    private void onClickLed(int portNumber) {
        Log.d(Constants.LOG_TAG, "RobotActivity.onClickLed " + portNumber);
        TriColorLed[] triColorLeds = session.getFlutter().getTriColorLeds();

        LedDialog dialog = LedDialog.newInstance(triColorLeds[portNumber-1], this);
        dialog.show(getSupportFragmentManager(), "tag");
    }
    private View.OnClickListener led1FrameClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onClickLed(1);
        }
    };
    private View.OnClickListener led2FrameClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onClickLed(2);
        }
    };
    private View.OnClickListener led3FrameClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onClickLed(3);
        }
    };


    private void onClickSpeaker() {
        Log.d(Constants.LOG_TAG, "onClickSpeaker");
        Speaker speaker = session.getFlutter().getSpeaker();

        SpeakerDialog dialog = SpeakerDialog.newInstance(speaker, this);
        dialog.show(getSupportFragmentManager(), "tag");
    }
    private View.OnClickListener speakerFrameClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           onClickSpeaker();
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

        // Menu icon and text
        TextView robotMenuEntry = (TextView)findViewById(R.id.text_menu_robot);
        robotMenuEntry.setTextColor(getResources().getColor(R.color.white));
        robotMenuEntry.setCompoundDrawablesWithIntrinsicBounds(R.drawable.menu_icon_robot, 0, 0, 0);

        if (!globalHandler.melodySmartDeviceHandler.isConnected()) {
            NoFlutterConnectedDialog.displayDialog(this, R.string.no_flutter_robot);
        } else {
            instance = this;
            this.session = globalHandler.sessionHandler.getSession();
            SeekBar simulatedSeekbar = (SeekBar) findViewById(R.id.seekbar_simulated_data);
            simulatedSeekbar.setOnSeekBarChangeListener(seekBarChangeListener);

            TextView sensor1Text = (TextView) findViewById(R.id.text_sensor_1);
            TextView sensor2Text = (TextView) findViewById(R.id.text_sensor_2);
            TextView sensor3Text = (TextView) findViewById(R.id.text_sensor_3);
            sensor1Text.setOnClickListener(sensor1OnClickListener);
            sensor2Text.setOnClickListener(sensor2OnClickListener);
            sensor3Text.setOnClickListener(sensor3OnClickListener);

            FrameLayout frameServo1, frameServo2, frameServo3, frameled1, frameled2, frameled3, frameSpeaker;
            frameServo1 = (FrameLayout) findViewById(R.id.frame_servo_1);
            frameServo2 = (FrameLayout) findViewById(R.id.frame_servo_2);
            frameServo3 = (FrameLayout) findViewById(R.id.frame_servo_3);
            frameled1 = (FrameLayout) findViewById(R.id.frame_led_1);
            frameled2 = (FrameLayout) findViewById(R.id.frame_led_2);
            frameled3 = (FrameLayout) findViewById(R.id.frame_led_3);
            frameSpeaker = (FrameLayout) findViewById(R.id.frame_speaker);
            frameServo1.setOnClickListener(servo1FrameClickListener);
            frameServo2.setOnClickListener(servo2FrameClickListener);
            frameServo3.setOnClickListener(servo3FrameClickListener);
            frameled1.setOnClickListener(led1FrameClickListener);
            frameled2.setOnClickListener(led2FrameClickListener);
            frameled3.setOnClickListener(led3FrameClickListener);
            frameSpeaker.setOnClickListener(speakerFrameClickListener);

            updateSensorViews();
            updateDynamicViews();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());
        TextView flutterStatusText = (TextView)findViewById(R.id.text_flutter_connection_status);
        ImageView flutterStatusIcon = (ImageView)findViewById(R.id.image_flutter_status_icon);

        if (!globalHandler.melodySmartDeviceHandler.isConnected()) {
            NoFlutterConnectedDialog.displayDialog(this, R.string.no_flutter_robot);

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


    @OnClick(R.id.image_servo_1)
    public void onClickServo1Image() {
        onClickServo(1);
    }
    @OnClick(R.id.relative_servo_1)
    public void onClickServo1Relative() {
        onClickServo(1);
    }


    @OnClick(R.id.image_servo_2)
    public void onClickServo2Image() {
        onClickServo(2);
    }
    @OnClick(R.id.relative_servo_2)
    public void onClickServo2Relative() {
        onClickServo(2);
    }


    @OnClick(R.id.image_servo_3)
    public void onClickServo3Image() {
        onClickServo(3);
    }
    @OnClick(R.id.relative_servo_3)
    public void onclickServo3Relative() {
        onClickServo(3);
    }


    @OnClick(R.id.image_led_1)
    public void onClickLed1Image() {
        onClickLed(1);
    }
    @OnClick(R.id.relative_led_1)
    public void onClickLed1Relative() {
        onClickLed(1);
    }


    @OnClick(R.id.image_led_2)
    public void onClickLed2Image() {
        onClickLed(2);
    }
    @OnClick(R.id.relative_led_2)
    public void onClickLed2Relative() {
        onClickLed(2);
    }


    @OnClick(R.id.image_led_3)
    public void onClickLed3Image() {
        onClickLed(3);
    }
    @OnClick(R.id.relative_led_3)
    public void onClickLed3Relative() {
        onClickLed(3);
    }


    @OnClick(R.id.image_speaker)
    public void onClickSpeakerImage() {
        onClickSpeaker();
    }
    @OnClick(R.id.relative_speaker)
    public void onClickSpeakerRelative() {
        onClickSpeaker();
    }
    @OnClick(R.id.relative_speaker2)
    public void onClickSpeakerRelative2() {
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

        // update sensor
        GlobalHandler.getInstance(this).sessionHandler.getSession().getFlutter().updateSensorAtPort(GlobalHandler.getInstance(this).melodySmartDeviceHandler, portNumber, sensor);

        updateSensorViews();
        updateLinkedViews();
    }

}
