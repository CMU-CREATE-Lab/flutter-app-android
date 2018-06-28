package org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.outputs.Speaker;
import org.cmucreatelab.flutter_android.classes.outputs.TriColorLed;
import org.cmucreatelab.flutter_android.classes.sensors.NoSensor;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.BaseResizableDialogWizardOld;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.servo.ServoDialogStateHelper;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.servo.ServoUpdatedWithWizard;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mohit on 6/4/2018.
 *
 * SensorWizardPageTwo
 *
 * A Dialog that shows which sensor is to be linked with an output.
 * Called by the relationship dialog after clicking the "Next" button.
 */
public class SensorWizardPageTwo extends BaseResizableDialogWizardOld implements
        View.OnClickListener, Serializable, MinPositionWizardPageThree.DialogMinPositionListener  {


    private DialogSensorListener dialogSensorListener;
    private static Serializable robotAct;
    private Button nextButton;

    private Servo currentServo;
    private TriColorLed currentLed;

    private static ServoDialogStateHelper stateHelper;

    // boolean variables for determining which type of button the user clicked on
    private static boolean servoChosen = false;
    private static boolean ledChosen = false;
    private static boolean speakerChosen = false;

    public static SensorWizardPageTwo newInstance(Servo servo, TriColorLed led, Speaker speaker, Serializable serializable, Serializable robotActs) {
        robotAct = robotActs;
        Bundle args = new Bundle();
        if (servo != null) {
            servoChosen = true;
            ledChosen = false;
            speakerChosen = false;
            args.putSerializable(Servo.SERVO_KEY, servo);
        }
        else if (led != null) {
            servoChosen = false;
            ledChosen = true;
            speakerChosen = false;
            args.putSerializable(TriColorLed.LED_KEY, led);
        }
        else if (speaker != null) {
            servoChosen = false;
            ledChosen = false;
            speakerChosen = true;
            args.putSerializable(Speaker.SPEAKER_KEY, speaker);
        }
        SensorWizardPageTwo sensorDialog = new SensorWizardPageTwo();

        args.putSerializable(Constants.SerializableKeys.SENSOR_KEY, serializable);
        sensorDialog.setArguments(args);

        return sensorDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstances) {
        super.onCreateDialog(savedInstances);
        dialogSensorListener = (DialogSensorListener) getArguments().getSerializable(Constants.SerializableKeys.SENSOR_KEY);
        if (servoChosen) {
            currentServo = Servo.newInstance((Servo) getArguments().getSerializable(Servo.SERVO_KEY));
        }
        else if (ledChosen) {
            currentLed = TriColorLed.newInstance((TriColorLed) getArguments().getSerializable(TriColorLed.LED_KEY));
        }
//        if (stateHelper == null) {
//            stateHelper = ServoDialogStateHelper.newInstance(currentServo);
//        }
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_sensor_choice_wizard, null);
        nextButton = (Button) view.findViewById(R.id.button_next_page);
        if (servoChosen) {
            ((TextView) view.findViewById(R.id.text_output_title)).setText(getString(R.string.set_up_servo) + " " + String.valueOf(currentServo.getPortNumber()));
            ((ImageView) view.findViewById(R.id.text_output_title_icon)).setImageResource(R.drawable.servo_icon);
            ((TextView) view.findViewById(R.id.text_question_sensor)).setText("Which sensor should servo " + String.valueOf(currentServo.getPortNumber()) + " react to?");

        }
        else if (ledChosen) {
            ((TextView) view.findViewById(R.id.text_output_title)).setText(getString(R.string.set_up_led) + " " +  String.valueOf(currentLed.getPortNumber()));
            ((ImageView) view.findViewById(R.id.text_output_title_icon)).setImageResource(R.drawable.led);
            ((TextView) view.findViewById(R.id.text_question_sensor)).setText("Which sensor should led " + String.valueOf(currentLed.getPortNumber()) + " react to?");
        }
        else if (speakerChosen) {
            ((TextView) view.findViewById(R.id.text_output_title)).setText(getString(R.string.set_up_speaker));
            ((ImageView) view.findViewById(R.id.text_output_title_icon)).setImageResource(R.drawable.speaker);
            ((TextView) view.findViewById(R.id.text_question_sensor)).setText("Which sensor should the speaker react to?");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ButterKnife.bind(this, view);

        Sensor sensors[] = GlobalHandler.getInstance(this.getActivity()).sessionHandler.getSession().getFlutter().getSensors();

        // bind click listeners
        ImageView sensor1 = (ImageView) view.findViewById(R.id.image_sensor_1);
        ImageView sensor2 = (ImageView) view.findViewById(R.id.image_sensor_2);
        ImageView sensor3 = (ImageView) view.findViewById(R.id.image_sensor_3);
        TextView textSensor1 = (TextView) view.findViewById(R.id.text_sensor_1);
        TextView textSensor2 = (TextView) view.findViewById(R.id.text_sensor_2);
        TextView textSensor3 = (TextView) view.findViewById(R.id.text_sensor_3);
        LinearLayout layout1 = (LinearLayout) view.findViewById(R.id.linear_sensor_1);
        LinearLayout layout2 = (LinearLayout) view.findViewById(R.id.linear_sensor_2);
        LinearLayout layout3 = (LinearLayout) view.findViewById(R.id.linear_sensor_3);

        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);

        sensor1.setImageResource(sensors[0].getGreenImageId());
        sensor2.setImageResource(sensors[1].getGreenImageId());
        sensor3.setImageResource(sensors[2].getGreenImageId());
        textSensor1.setText(sensors[0].getSensorTypeId());
        textSensor2.setText(sensors[1].getSensorTypeId());
        textSensor3.setText(sensors[2].getSensorTypeId());

        return builder.create();
    }


    @Override
    public void onClick(View view) {
        GlobalHandler globalHandler = GlobalHandler.getInstance(view.getContext());
        Sensor sensor = new NoSensor(0);
        switch (view.getId()) {
            case R.id.linear_sensor_1:
                sensor = globalHandler.sessionHandler.getSession().getFlutter().getSensors()[0];
                nextButton.setEnabled(true);
                break;
            case R.id.linear_sensor_2:
                sensor = globalHandler.sessionHandler.getSession().getFlutter().getSensors()[1];
                nextButton.setEnabled(true);
                break;
            case R.id.linear_sensor_3:
                sensor = globalHandler.sessionHandler.getSession().getFlutter().getSensors()[2];
                nextButton.setEnabled(true);
                break;

        }
        dialogSensorListener.onSensorChosen(sensor);

    }


    @OnClick(R.id.button_next_page)
    public void onClickNextPage(View view) {
        // send an intent to the wet position dialog
        // TODO differentiate between the different min position dialogs that should appear at this point of time.
        // Wet Position Dialogs that will display the degree should appear only when the servo button has been clicked.
        // Color position dialogs that display a color selection should appear only when the led button has been clicked.
        // Volume position dialogs that display a volume selection should only appear when the speaker button has been clicked.
        if (servoChosen) {
            MinPositionWizardPageThree dialog = MinPositionWizardPageThree.newInstance(currentServo, this, robotAct);
            dialog.show(getFragmentManager(), "tag");
        }
        else if (ledChosen) {

        }
        else if (speakerChosen) {

        }
        this.dismiss();
    }

    public void onMinPosChosen(int min) {
        Log.d(Constants.LOG_TAG, "onMinPosChosen");
        ServoUpdatedWithWizard.add("minPosition", null, null, min, 9999);

//        View view,layout;
//        ImageView currentImageView;
//        TextView currentTextViewDescrp,currentTextViewItem;
//        RotateAnimation rotateAnimation;
//
//        view = dialogView.findViewById(R.id.linear_set_min_pos);
//        layout = ((ViewGroup) view).getChildAt(0);
//        currentImageView = (ImageView) ((ViewGroup) layout).getChildAt(0);
//        layout = ((ViewGroup) view).getChildAt(1);
//        currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
//        currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);
//
//        rotateAnimation = new RotateAnimation(0, min, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
//        rotateAnimation.setFillEnabled(true);
//        rotateAnimation.setFillAfter(true);
//        rotateAnimation.setDuration(0);
//        currentImageView.startAnimation(rotateAnimation);
//
//        stateHelper.setMinimumPosition(min, currentTextViewDescrp, currentTextViewItem);
    }

    @OnClick(R.id.button_close)
    public void onClickClose() {
        Dialog dialog = getDialog();
        dialog.dismiss();
    }



    // interface for an activity to listen for a choice
    public interface DialogSensorListener {
        public void onSensorChosen(Sensor sensor);
    }

}
