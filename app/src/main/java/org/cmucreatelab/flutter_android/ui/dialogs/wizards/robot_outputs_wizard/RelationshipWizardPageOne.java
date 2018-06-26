package org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.outputs.Speaker;
import org.cmucreatelab.flutter_android.classes.outputs.TriColorLed;
import org.cmucreatelab.flutter_android.classes.relationships.Amplitude;
import org.cmucreatelab.flutter_android.classes.relationships.Change;
import org.cmucreatelab.flutter_android.classes.relationships.Constant;
import org.cmucreatelab.flutter_android.classes.relationships.Cumulative;
import org.cmucreatelab.flutter_android.classes.relationships.Frequency;
import org.cmucreatelab.flutter_android.classes.relationships.NoRelationship;
import org.cmucreatelab.flutter_android.classes.relationships.Proportional;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.relationships.Switch;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.children.AdvancedSettingsDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.servo.ServoUpdatedWithWizard;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.OnClick;

/* Created by Mohit.
 *
 * The first page to the wizard. The start of it all! :)
 * Is called by all three: servo, led, and speaker
 *
 */

public class RelationshipWizardPageOne extends BaseResizableDialogWizard implements View.OnClickListener, Serializable, SensorWizardPageTwo.DialogSensorListener, MinPositionWizardPageThree.DialogMinPositionListener {

    private Relationship relationship;
    private DialogRelationshipListener relationshipListener;
    private Button nextButton;
    private static Serializable robotAct;

    private Servo currentServo;
    private TriColorLed currentLed;

    // boolean variables for determining which type of button the user clicked on
    private static boolean servoChosen = false;
    private static boolean ledChosen = false;
    private static boolean speakerChosen = false;


    public static RelationshipWizardPageOne newInstance(Servo servo, TriColorLed led, Speaker speaker, Serializable serializable) {
        Bundle args = new Bundle();

        if (servo != null) {
            servoChosen = true;
            args.putSerializable(Servo.SERVO_KEY, servo);

            ledChosen = false;
            speakerChosen = false;
        }
        else if (speaker != null) {
            speakerChosen = true;
            args.putSerializable(Speaker.SPEAKER_KEY, speaker);

            servoChosen = false;
            ledChosen = false;
        }
        else if (led != null) {
            ledChosen = true;
            args.putSerializable(TriColorLed.LED_KEY, led);

            servoChosen = false;
            speakerChosen = false;
        }
        RelationshipWizardPageOne relationshipDialog = new RelationshipWizardPageOne();
        args.putSerializable(Constants.SerializableKeys.RELATIONSHIP_KEY, serializable);
        robotAct = serializable;
        relationshipDialog.setArguments(args);

        return relationshipDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        relationshipListener = (DialogRelationshipListener) getArguments().getSerializable(Constants.SerializableKeys.RELATIONSHIP_KEY);
        super.onCreateDialog(savedInstanceState);
        if (servoChosen) {
            currentServo = Servo.newInstance((Servo) getArguments().getSerializable(Servo.SERVO_KEY));
        }
        else if (ledChosen) {
            currentLed = TriColorLed.newInstance((TriColorLed) getArguments().getSerializable(TriColorLed.LED_KEY));
        }
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_wizard, null);
        nextButton = (Button) view.findViewById(R.id.button_save_link);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);

        if (servoChosen) {
            ((TextView) view.findViewById(R.id.text_output_title)).setText(getString(R.string.set_up_servo) + " " + String.valueOf(currentServo.getPortNumber()));
            ((ImageView) view.findViewById(R.id.text_output_title_icon)).setImageResource(R.drawable.servo_icon);
        }
        else if (ledChosen) {
            ((TextView) view.findViewById(R.id.text_output_title)).setText(getString(R.string.set_up_led) + " " +  String.valueOf(currentLed.getPortNumber()));
            ((ImageView) view.findViewById(R.id.text_output_title_icon)).setImageResource(R.drawable.led);
        }
        else if (speakerChosen) {
            ((TextView) view.findViewById(R.id.text_output_title)).setText(getString(R.string.set_up_speaker));
            ((ImageView) view.findViewById(R.id.text_output_title_icon)).setImageResource(R.drawable.speaker);
        }
        ButterKnife.bind(this, view);

        // bind click listeners
        view.findViewById(R.id.linear_proportional).setOnClickListener(this);
        view.findViewById(R.id.linear_constant).setOnClickListener(this);
        view.findViewById(R.id.linear_amplitude).setOnClickListener(this);
        view.findViewById(R.id.linear_frequency).setOnClickListener(this);
        view.findViewById(R.id.linear_cumulative).setOnClickListener(this);
        view.findViewById(R.id.linear_change).setOnClickListener(this);
        /*view.findViewById(R.id.linear_proportional).setOnClickListener(this);
        view.findViewById(R.id.linear_frequency).setOnClickListener(this);
        view.findViewById(R.id.linear_amplitude).setOnClickListener(this);
        view.findViewById(R.id.linear_cumulative).setOnClickListener(this);
        view.findViewById(R.id.linear_change).setOnClickListener(this);
        view.findViewById(R.id.linear_switch).setOnClickListener(this);
        view.findViewById(R.id.linear_constant).setOnClickListener(this);*/
        // TODO @tasota hidden for now; implement later
        //view.findViewById(R.id.linear_proportional).setVisibility(View.GONE);
        //view.findViewById(R.id.linear_frequency).setVisibility(View.GONE);
        //view.findViewById(R.id.linear_amplitude).setVisibility(View.GONE);
        //view.findViewById(R.id.linear_cumulative).setVisibility(View.GONE);
        //view.findViewById(R.id.linear_change).setVisibility(View.GONE);
        //view.findViewById(R.id.linear_switch).setVisibility(View.GONE); // Please uncomment this line.
        //view.findViewById(R.id.linear_constant).setVisibility(View.GONE);

        return builder.create();
    }


    @Override
    public void onClick(View view) {
        relationship = NoRelationship.getInstance();
        switch (view.getId()) {
            case R.id.linear_proportional:
                Log.d(Constants.LOG_TAG, "onClickProportional");
                relationship = Proportional.getInstance();
                nextButton.setEnabled(true);
                break;
            case R.id.linear_frequency:
                Log.d(Constants.LOG_TAG, "onClickFrequency");
                relationship = Frequency.getInstance();
                nextButton.setEnabled(true);
                break;
            case R.id.linear_amplitude:
                Log.d(Constants.LOG_TAG, "onClickAmplitude");
                relationship = Amplitude.getInstance();
                nextButton.setEnabled(true);
                break;
            case R.id.linear_cumulative:
                Log.d(Constants.LOG_TAG, "onClickImageCumulative");
                relationship = Cumulative.getInstance();
                nextButton.setEnabled(true);
                break;
            case R.id.linear_change:
                Log.d(Constants.LOG_TAG, "onClickChange");
                relationship = Change.getInstance();
                nextButton.setEnabled(true);
                break;
            case R.id.linear_switch:
                Log.d(Constants.LOG_TAG, "onClickSwitch");
                relationship = Switch.getInstance();
                nextButton.setEnabled(false); // not yet implemented, so set button to false
                break;
            case R.id.linear_constant:
                Log.d(Constants.LOG_TAG, "onClickConstant");
                relationship = Constant.getInstance();
                nextButton.setEnabled(true);
                break;
        }

    }


    @OnClick(R.id.button_save_link)
    public void onClickSetRelationship() {
        relationshipListener.onRelationshipChosen(relationship);
        // send an intent to the sensor dialog
        // if relationship = constant, then skip sensor dialog
        if (relationship == Constant.getInstance()) {
            // send an intent to the wet position (Page 3)
            if (servoChosen) {
                Servo servos = (Servo) getArguments().getSerializable(Servo.SERVO_KEY);
                MinPositionWizardPageThree dialogR = MinPositionWizardPageThree.newInstance(servos, this, robotAct);
                dialogR.show(getFragmentManager(), "tag");
            }
            else if (ledChosen) {

            }
            else if (speakerChosen) {

            }
        }
        else {
            // send an intent to the sensor dialog (Page 2)
            if (servoChosen) {
                Servo servos = (Servo) getArguments().getSerializable(Servo.SERVO_KEY);
                SensorWizardPageTwo dialogR = SensorWizardPageTwo.newInstance(servos, null, null, this, robotAct);
                dialogR.show(getActivity().getSupportFragmentManager(), "tag");
            }
            else if (ledChosen) {
                TriColorLed leds = (TriColorLed) getArguments().getSerializable(TriColorLed.LED_KEY);
                SensorWizardPageTwo dialogR = SensorWizardPageTwo.newInstance(null, leds, null, this, robotAct);
                dialogR.show(getActivity().getSupportFragmentManager(), "tag");

            }
            else if (speakerChosen) {
                Speaker speakers = (Speaker) getArguments().getSerializable(Speaker.SPEAKER_KEY);
                SensorWizardPageTwo dialogR = SensorWizardPageTwo.newInstance(null, null, speakers, this, robotAct);
                dialogR.show(getActivity().getSupportFragmentManager(), "tag");

            }
        }
        this.dismiss();
    }


    @Override
    public void onSensorChosen(Sensor sensor) {
        ServoUpdatedWithWizard.add("sensor", null, sensor, 9999, 9999);


//        ImageView currentImageView = (ImageView) dialogView.findViewById(R.id.image_sensor);
//        ImageView currentImageViewHighlight = (ImageView) dialogView.findViewById(R.id.image_sensor_highlight);
//        TextView currentTextViewDescrp = (TextView) dialogView.findViewById(R.id.text_sensor_link);
//        TextView currentTextViewItem = (TextView) dialogView.findViewById(R.id.text_sensor_type);
//
//        if (sensor.getSensorType() != NOT_SET) {
//            Log.d(Constants.LOG_TAG, "onSensorChosen");
//            currentImageView.setImageResource(sensor.getGreenImageId());
//            currentImageViewHighlight.clearAnimation();
//            currentTextViewDescrp.setText(R.string.linked_sensor);
//            currentTextViewItem.setText(sensor.getSensorTypeId());
//
//            stateHelper.setLinkedSensor(sensor);
//        }
//        updateServoViews();
    }

    public void onMinPosChosen(int min) {

    }


    // TODO This does not work (crashes)
    @OnClick(R.id.image_advanced_settings)
    public void onClickAdvancedSettings() {
        Log.d(Constants.LOG_TAG, "onClickAdvancedSettings");

        DialogFragment dialog = AdvancedSettingsDialog.newInstance(robotAct, currentServo);
        dialog.show(this.getFragmentManager(), "tag");
    }

    @OnClick(R.id.button_close)
    public void onClickClose() {
        Dialog dialog = getDialog();
        dialog.dismiss();
    }


    // interface for an activity to listen for a choice
    public interface DialogRelationshipListener {
        public void onRelationshipChosen(Relationship relationship);
    }

}
