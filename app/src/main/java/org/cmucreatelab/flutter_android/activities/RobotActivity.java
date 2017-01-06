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

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseSensorReadingActivity;
import org.cmucreatelab.flutter_android.classes.Session;
import org.cmucreatelab.flutter_android.classes.outputs.Output;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.outputs.Speaker;
import org.cmucreatelab.flutter_android.classes.outputs.TriColorLed;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.NoFlutterConnectedDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.parents.LedDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.parents.ServoDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.parents.SpeakerDialog;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

// TODO - make a message reconstruct class to determine what kind of message it is
// TODO - then call the appropriate method depending on what kind of message was received
public class RobotActivity extends BaseSensorReadingActivity implements ServoDialog.DialogServoListener, LedDialog.DialogLedListener, SpeakerDialog.DialogSpeakerListener {

    private Session session;
    private boolean isUsingSensorData = true;


    private void updateStaticViews() {
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

                if (sensors[0].getSensorType() != Sensor.Type.NO_SENSOR) {
                    sensorReadingText = (TextView) findViewById(R.id.text_sensor_1_reading);
                    sensorReadingText.setText(String.valueOf(sensors[0].getSensorReading()));
                }
                if (sensors[1].getSensorType() != Sensor.Type.NO_SENSOR) {
                    sensorReadingText = (TextView) findViewById(R.id.text_sensor_2_reading);
                    sensorReadingText.setText(String.valueOf(sensors[1].getSensorReading()));
                }
                if (sensors[2].getSensorType() != Sensor.Type.NO_SENSOR) {
                    sensorReadingText = (TextView) findViewById(R.id.text_sensor_3_reading);
                    sensorReadingText.setText(String.valueOf(sensors[2].getSensorReading()));
                }
            }
        });
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
                    sensor.setImageResource(outputs[i].getSettings().getSensor().getGreyImageIdSm());
                }
            } else {
                if (currentLayout != null && questionMark != null) {
                    currentLayout.setVisibility(View.INVISIBLE);
                    questionMark.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, final int i, boolean b) {
            Log.d(Constants.LOG_TAG, "onProgressChanged");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Sensor[] sensors = session.getFlutter().getSensors();
                    TextView sensorReadingText;

                    if (sensors[0].getSensorType() != Sensor.Type.NO_SENSOR) {
                        sensorReadingText = (TextView) findViewById(R.id.text_sensor_1_reading);
                        sensorReadingText.setText(String.valueOf(i) + "%");
                    }
                    if (sensors[1].getSensorType() != Sensor.Type.NO_SENSOR) {
                        sensorReadingText = (TextView) findViewById(R.id.text_sensor_2_reading);
                        sensorReadingText.setText(String.valueOf(i) + "%");
                    }
                    if (sensors[2].getSensorType() != Sensor.Type.NO_SENSOR) {
                        sensorReadingText = (TextView) findViewById(R.id.text_sensor_3_reading);
                        sensorReadingText.setText(String.valueOf(i) + "%");
                    }
                }
            });
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };


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

        if (!globalHandler.melodySmartDeviceHandler.isConnected()) {
            NoFlutterConnectedDialog.displayDialog(this, R.string.no_flutter_robot);
        } else {
            this.session = globalHandler.sessionHandler.getSession();
            SeekBar simulatedSeekbar = (SeekBar) findViewById(R.id.seekbar_simulated_data);
            simulatedSeekbar.setOnSeekBarChangeListener(seekBarChangeListener);

            updateStaticViews();
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
            if (isUsingSensorData) startSensorReading();
        }
    }


    @Override
    public void onServoLinkListener(String message) {
        GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());

        Log.d(Constants.LOG_TAG, "onServoLinkListener");
        globalHandler.melodySmartDeviceHandler.addMessage(message);
        updateLinkedViews();
    }


    @Override
    public void onLedLinkListener(ArrayList<String> msgs) {
        GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());

        Log.d(Constants.LOG_TAG, "onLedLinkCreated");
        for (String message : msgs) {
            globalHandler.melodySmartDeviceHandler.addMessage(message);
        }
        updateLinkedViews();
    }


    @Override
    public void onSpeakerLinkListener(ArrayList<String> msgs) {
        GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());

        Log.d(Constants.LOG_TAG, "onSpeakerLinkCreated");
        for (String message : msgs) {
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
        if (!isUsingSensorData) {
            Button sensorData = (Button) findViewById(R.id.button_sensor_data);
            sensorData.setBackground(ContextCompat.getDrawable(this, R.drawable.round_green_button_left));
            sensorData.setTextColor(Color.WHITE);

            Button simulateData = (Button) findViewById(R.id.button_simulate_data);
            simulateData.setBackground(ContextCompat.getDrawable(this, R.drawable.round_gray_white_right));
            simulateData.setTextColor(Color.GRAY);

            isUsingSensorData = true;
            startSensorReading();

            SeekBar simulatedSeekbar = (SeekBar) findViewById(R.id.seekbar_simulated_data);
            simulatedSeekbar.setVisibility(View.INVISIBLE);
        }
    }


    @OnClick(R.id.button_simulate_data)
    public void onClickSimulateData() {
        Log.d(Constants.LOG_TAG, "onClickSimulateData");
        if (isUsingSensorData) {
            Button sensorData = (Button) findViewById(R.id.button_sensor_data);
            sensorData.setBackground(ContextCompat.getDrawable(this, R.drawable.round_gray_white_left));
            sensorData.setTextColor(Color.GRAY);

            Button simulateData = (Button) findViewById(R.id.button_simulate_data);
            simulateData.setBackground(ContextCompat.getDrawable(this, R.drawable.round_green_button_right));
            simulateData.setTextColor(Color.WHITE);

            isUsingSensorData = false;
            stopSensorReading();

            SeekBar simulatedSeekbar = (SeekBar) findViewById(R.id.seekbar_simulated_data);
            simulatedSeekbar.setVisibility(View.VISIBLE);
            simulatedSeekbar.setProgress(0);
        }
    }


    // FlutterMessageListener implementation


    @Override
    public void onFlutterMessageReceived(String request, String response) {
        Sensor[] sensors = session.getFlutter().getSensors();

        if (response.substring(0,1).equals("r") && !response.equals("OK") && !response.equals("FAIL")) {
            response = response.substring(2, response.length());
            String sensor1 = response.substring(0, response.indexOf(','));
            response = response.substring(response.indexOf(',')+1, response.length());
            String sensor2 = response.substring(0, response.indexOf(','));
            response = response.substring(response.indexOf(',')+1, response.length());
            String sensor3 = response;
            sensors[0].setSensorReading(Integer.valueOf(sensor1));
            sensors[1].setSensorReading(Integer.valueOf(sensor2));
            sensors[2].setSensorReading(Integer.valueOf(sensor3));
            updateDynamicViews();
        }
    }

}
