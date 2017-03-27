package org.cmucreatelab.flutter_android.ui.dialogs.outputs.servo;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.cmucreatelab.android.melodysmart.models.MelodySmartMessage;
import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.relationships.Constant;
import org.cmucreatelab.flutter_android.classes.relationships.Proportional;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.settings.AdvancedSettings;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.settings.SettingsConstant;
import org.cmucreatelab.flutter_android.classes.settings.SettingsProportional;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.FlutterProtocol;
import org.cmucreatelab.flutter_android.helpers.static_classes.MessageConstructor;
import org.cmucreatelab.flutter_android.ui.dialogs.children.AdvancedSettingsDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.MaxPositionDialog.DialogMaxPositionListener;
import org.cmucreatelab.flutter_android.ui.dialogs.children.MinPositionDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.RelationshipOutputDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.SensorOutputDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.outputs.BaseOutputDialog;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Steve on 10/17/2016.
 *
 * ServoDialog
 *
 * A Dialog that shows the options for creating a link between Servo and a Sensor
 *
 */
public class ServoDialog extends BaseOutputDialog implements Serializable,
        AdvancedSettingsDialog.DialogAdvancedSettingsListener,
        SensorOutputDialog.DialogSensorListener,
        RelationshipOutputDialog.DialogRelationshipListener,
        DialogMaxPositionListener,
        MinPositionDialog.DialogMinPositionListener {

    public View dialogView;
    public LinearLayout linkedSensor,minPosLayout;
    public ImageView advancedSettingsView;

    private ServoDialogStateHelper stateHelper;
    private DialogServoListener dialogServoListener;
    private Servo servo;


    private void updateViews() {
        super.updateViews(dialogView, servo);

        this.advancedSettingsView = (ImageView) dialogView.findViewById(R.id.image_advanced_settings);
        this.linkedSensor = (LinearLayout) dialogView.findViewById(R.id.linear_set_linked_sensor);
        this.minPosLayout = (LinearLayout) dialogView.findViewById(R.id.linear_set_min_pos);

        stateHelper.updateView(this);

        Button saveButton = (Button) dialogView.findViewById(R.id.button_save_link);
        Button removeButton = (Button) dialogView.findViewById(R.id.button_remove_link);
        saveButton.setEnabled(stateHelper.canSaveLink());
        removeButton.setEnabled(stateHelper.canRemoveLink());
    }


    public static ServoDialog newInstance(Servo servo, Serializable activity) {
        ServoDialog servoDialog = new ServoDialog();

        Bundle args = new Bundle();
        args.putSerializable(Servo.SERVO_KEY, servo);
        args.putSerializable(Constants.SerializableKeys.DIALOG_SERVO, activity);
        servoDialog.setArguments(args);

        return servoDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "onCreateDialog");
        super.onCreateDialog(savedInstanceState);

        // clone old object
        servo = Servo.newInstance((Servo) getArguments().getSerializable(Servo.SERVO_KEY));
        stateHelper = ServoDialogStateHelper.newInstance(servo);
        dialogServoListener = (DialogServoListener) getArguments().getSerializable(Constants.SerializableKeys.DIALOG_SERVO);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_servos, null);
        this.dialogView = view;
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.text_output_title)).setText(getString(R.string.set_up_servo) + " " +  String.valueOf(servo.getPortNumber()));
        ((ImageView) view.findViewById(R.id.text_output_title_icon)).setImageResource(R.drawable.servo_icon);

        ButterKnife.bind(this, view);

        updateViews();
        return builder.create();
    }


    // OnClickListeners


    @OnClick(R.id.button_save_link)
    public void onClickSaveSettings() {
        Log.d(Constants.LOG_TAG, "onClickSaveSettings");

        servo.setSettings(servo.getSettings());
        MelodySmartMessage msg = MessageConstructor.constructRelationshipMessage(servo, servo.getSettings());
        servo.setIsLinked(true, servo);

        // overwrite old object
        GlobalHandler.getInstance(getActivity()).sessionHandler.getSession().getFlutter().getServos()[servo.getPortNumber()-1] = servo;

        dialogServoListener.onServoLinkListener(msg);
        this.dismiss();
    }


    @OnClick(R.id.button_remove_link)
    public void onClickRemoveLink() {
        Log.d(Constants.LOG_TAG, "onClickRemoveLink");

        MelodySmartMessage msg = MessageConstructor.constructRemoveRelation(servo);
        servo.setIsLinked(false, servo);

        // overwrite old object
        GlobalHandler.getInstance(getActivity()).sessionHandler.getSession().getFlutter().getServos()[servo.getPortNumber()-1] = servo;

        dialogServoListener.onServoLinkListener(msg);
        this.dismiss();
    }


    @OnClick(R.id.image_advanced_settings)
    public void onClickAdvancedSettings() {
        Log.d(Constants.LOG_TAG, "onClickAdvancedSettings");

        DialogFragment dialog = AdvancedSettingsDialog.newInstance(this, servo);
        dialog.show(this.getFragmentManager(), "tag");
    }


    @OnClick(R.id.linear_set_linked_sensor)
    public void onClickSetLinkedSensor(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetLinkedSensor");

        DialogFragment dialog = SensorOutputDialog.newInstance(this);
        dialog.show(this.getFragmentManager(), "tag");
    }


    @OnClick(R.id.linear_set_relationship)
    public void onClickSetRelationship(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetRelationship");

        DialogFragment dialog = RelationshipOutputDialog.newInstance(this);
        dialog.show(this.getFragmentManager(), "tag");
    }


    @OnClick(R.id.linear_set_max_pos)
    public void onClickSetMaximumPosition(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetMaximumPosition");
        stateHelper.clickMax(this);
    }


    @OnClick(R.id.linear_set_min_pos)
    public void onclickSetMinimumPosition(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetMinimumPosition");
        stateHelper.clickMin(this);
    }


    // Option listeners


    @Override
    public void onAdvancedSettingsSet(AdvancedSettings advancedSettings) {
        Log.d(Constants.LOG_TAG, "onAdvancedSettingsSet");
        stateHelper.setAdvancedSettings(advancedSettings);
    }


    @Override
    public void onSensorChosen(Sensor sensor) {
        View view,layout;
        ImageView currentImageView;
        TextView currentTextViewDescrp,currentTextViewItem;

        view = dialogView.findViewById(R.id.linear_set_linked_sensor);
        currentImageView = (ImageView) ((ViewGroup) view).getChildAt(0);
        layout = ((ViewGroup) view).getChildAt(1);
        currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);

        if (sensor.getSensorType() != FlutterProtocol.InputTypes.NOT_SET) {
            Log.d(Constants.LOG_TAG, "onSensorChosen");
            currentImageView.setImageResource(sensor.getGreenImageId());
            currentTextViewDescrp.setText(R.string.linked_sensor);
            currentTextViewItem.setText(sensor.getSensorTypeId());

            stateHelper.setLinkedSensor(sensor);
        }
        updateViews();
    }


    @Override
    public void onRelationshipChosen(Relationship relationship) {
        Log.d(Constants.LOG_TAG, "onRelationshipChosen");
        View view,layout;
        ImageView currentImageView;
        TextView currentTextViewDescrp,currentTextViewItem;

        view = dialogView.findViewById(R.id.linear_set_relationship);
        currentImageView = (ImageView) ((ViewGroup) view).getChildAt(0);
        layout = ((ViewGroup) view).getChildAt(1);
        currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);

        currentImageView.setImageResource(relationship.getGreenImageIdMd());
        currentTextViewDescrp.setText(R.string.relationship);
        currentTextViewItem.setText(relationship.getRelationshipType().toString());

        if (relationship.getClass() == Proportional.class) {
            servo.setSettings(SettingsProportional.newInstance(servo.getSettings()));
        } else if (relationship.getClass() == Constant.class) {
            servo.setSettings(SettingsConstant.newInstance(servo.getSettings()));
        } else {
            Log.e(Constants.LOG_TAG,"ServoDialog.onRelationshipChosen: unimplemented Relationship/Settings.");
        }
        stateHelper = ServoDialogStateHelper.newInstance(servo);

        updateViews();
    }


    @Override
    public void onMaxPosChosen(int max) {
        Log.d(Constants.LOG_TAG, "onMaxPosChosen");
        View view,layout;
        ImageView currentImageView;
        TextView currentTextViewDescrp,currentTextViewItem;
        RotateAnimation rotateAnimation;

        view = dialogView.findViewById(R.id.linear_set_max_pos);
        layout = ((ViewGroup) view).getChildAt(0);
        currentImageView = (ImageView) ((ViewGroup) layout).getChildAt(0);
        layout = ((ViewGroup) view).getChildAt(1);
        currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);

        rotateAnimation = new RotateAnimation(0, max, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setFillEnabled(true);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(0);
        currentImageView.startAnimation(rotateAnimation);

        stateHelper.setMaximumPosition(max, currentTextViewDescrp, currentTextViewItem);
    }


    @Override
    public void onMinPosChosen(int min) {
        Log.d(Constants.LOG_TAG, "onMinPosChosen");
        View view,layout;
        ImageView currentImageView;
        TextView currentTextViewDescrp,currentTextViewItem;
        RotateAnimation rotateAnimation;

        view = dialogView.findViewById(R.id.linear_set_min_pos);
        layout = ((ViewGroup) view).getChildAt(0);
        currentImageView = (ImageView) ((ViewGroup) layout).getChildAt(0);
        layout = ((ViewGroup) view).getChildAt(1);
        currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);

        rotateAnimation = new RotateAnimation(0, min, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setFillEnabled(true);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(0);
        currentImageView.startAnimation(rotateAnimation);

        stateHelper.setMinimumPosition(min, currentTextViewDescrp, currentTextViewItem);
    }


    public interface DialogServoListener {
        public void onServoLinkListener(MelodySmartMessage message);
    }

}
