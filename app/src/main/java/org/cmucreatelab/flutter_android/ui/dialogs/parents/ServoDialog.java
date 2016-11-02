package org.cmucreatelab.flutter_android.ui.dialogs.parents;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.RobotActivity;
import org.cmucreatelab.flutter_android.classes.Settings;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.MessageConstructor;
import org.cmucreatelab.flutter_android.ui.dialogs.children.MaxPositionDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.MinPositionDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.RelationshipDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.SensorDialog;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Steve on 10/17/2016.
 *
 * ServoDialog
 *
 * A Dialog that shows the options for creating a link between Servo and a Sensor
 */
public class ServoDialog extends DialogFragment implements Serializable, DialogInterface.OnClickListener,
        SensorDialog.DialogSensorListener,
        RelationshipDialog.DialogRelationshipListener,
        MaxPositionDialog.DialogMaxPositionListener,
        MinPositionDialog.DialogMinPositionListener {


    private DialogServoListener dialogServoListener;
    private String servoNumber;

    private Serializable serializable;
    private DialogFragment dialogFragment;

    private LinearLayout setLinkedSensor;
    private LinearLayout setRelationship;
    private LinearLayout setMaximumPos;
    private LinearLayout setMinimumPos;

    private ImageView currentImageView;
    private TextView currentTextViewDescrp;
    private TextView currentTextViewItem;

    private Settings servoSettings;
    private Servo servo;


    private int convertDpToPx(int dp){
        return Math.round(dp*(getResources().getDisplayMetrics().xdpi/DisplayMetrics.DENSITY_DEFAULT));

    }


    public static ServoDialog newInstance(Servo servo, String servoNumber, Serializable activity) {
        ServoDialog servoDialog = new ServoDialog();

        Bundle args = new Bundle();
        args.putSerializable(Servo.SERVO_KEY, servo);
        args.putString(Servo.SERVO_NUMBER_KEY, servoNumber);
        args.putSerializable(RobotActivity.SERIALIZABLE_KEY, activity);
        servoDialog.setArguments(args);

        return servoDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "onCreateDialog");
        super.onCreateDialog(savedInstanceState);
        serializable = this;
        dialogFragment = this;

        servo = (Servo) getArguments().getSerializable(Servo.SERVO_KEY);
        servoNumber = getArguments().getString(Servo.SERVO_NUMBER_KEY);
        dialogServoListener = (DialogServoListener) getArguments().getSerializable(RobotActivity.SERIALIZABLE_KEY);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_servos, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        builder.setPositiveButton(R.string.save_settings, this);
        ((TextView) view.findViewById(R.id.text_servo_title)).setText(getString(R.string.set_up_servo) + servoNumber);
        ButterKnife.bind(this, view);

        servoSettings = new Settings();

        return builder.create();
    }


    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(convertDpToPx(350), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        Log.d(Constants.LOG_TAG, "onClickSave");
        servo.setSettings(servoSettings);
        String msg = MessageConstructor.getServoLinkMessage(servo, servoNumber);
        Log.d(Constants.LOG_TAG, msg);
        dialogServoListener.onServoLinkCreated(msg);
    }


    // OnClickListeners

    // TODO - change the textview structure
    @OnClick(R.id.linear_set_linked_sensor)
    public void onClickSetLinkedSensor(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetLinkedSensor");
        currentImageView = (ImageView) ((ViewGroup) view).getChildAt(0);
        View layout = ((ViewGroup) view).getChildAt(1);
        currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);
        DialogFragment dialog = SensorDialog.newInstance(serializable);
        dialog.show(dialogFragment.getFragmentManager(), "tag");
    }


    @OnClick(R.id.linear_set_relationship)
    public void onClickSetRelationship(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetRelationship");
        currentImageView = (ImageView) ((ViewGroup) view).getChildAt(0);
        View layout = ((ViewGroup) view).getChildAt(1);
        currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);
        DialogFragment dialog = RelationshipDialog.newInstance(serializable);
        dialog.show(dialogFragment.getFragmentManager(), "tag");
    }


    @OnClick(R.id.linear_set_max_pos)
    public void onClickSetMaximumPosition(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetMaximumPosition");
        currentImageView = (ImageView) ((ViewGroup) view).getChildAt(0);
        View layout = ((ViewGroup) view).getChildAt(1);
        currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);
        DialogFragment dialog = MaxPositionDialog.newInstance(serializable);
        dialog.show(dialogFragment.getFragmentManager(), "tag");
    }


    @OnClick(R.id.linear_set_min_pos)
    public void onclickSetMinimumPosition(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetMinimumPosition");
        currentImageView = (ImageView) ((ViewGroup) view).getChildAt(0);
        View layout = ((ViewGroup) view).getChildAt(1);
        currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);
        DialogFragment dialog = MinPositionDialog.newInstance(serializable);
        dialog.show(dialogFragment.getFragmentManager(), "tag");
    }


    @Override
    public void onSensorChosen(Sensor sensor) {
        Log.d(Constants.LOG_TAG, "onSensorChosen");
        currentImageView.setImageResource(sensor.getGreenImageId());
        currentTextViewDescrp.setText(R.string.linked_sensor);
        currentTextViewItem.setText(sensor.getSensorType().toString());
        servoSettings.setSensor(sensor);
    }


    @Override
    public void onRelationshipChosen(Relationship relationship) {
        Log.d(Constants.LOG_TAG, "onRelationshipChosen");
        currentImageView.setImageResource(relationship.getGreenImageIdMd());
        currentTextViewDescrp.setText(R.string.relationship);
        currentTextViewItem.setText(relationship.getRelationshipType().toString());
        servoSettings.setRelationship(relationship);
    }


    @Override
    public void onMaxPosChosen(int max) {
        Log.d(Constants.LOG_TAG, "onMaxPosChosen");
        currentTextViewDescrp.setText(servoSettings.getSensor().getHighTextId());
        currentTextViewItem.setText(String.valueOf(max) + (char) 0x00B0);
        servoSettings.setOutputMax(max);
    }


    @Override
    public void onMinPosChosen(int min) {
        Log.d(Constants.LOG_TAG, "onMinPosChosen");
        currentTextViewDescrp.setText(servoSettings.getSensor().getLowTextId());
        currentTextViewItem.setText(String.valueOf(min) + (char) 0x00B0);
        servoSettings.setOutputMin(min);

    }


    public interface DialogServoListener {
        public void onServoLinkCreated(String message);
    }

}
