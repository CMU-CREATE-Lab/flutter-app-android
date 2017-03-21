package org.cmucreatelab.flutter_android.ui.dialogs.parents;

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
import org.cmucreatelab.flutter_android.classes.flutters.Flutter;
import org.cmucreatelab.flutter_android.classes.relationships.Constant;
import org.cmucreatelab.flutter_android.classes.relationships.Proportional;
import org.cmucreatelab.flutter_android.classes.sensors.NoSensor;
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
import org.cmucreatelab.flutter_android.ui.dialogs.BaseOutputDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.AdvancedSettingsDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.MaxPositionDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.MaxPositionDialog.DialogMaxPositionListener;
import org.cmucreatelab.flutter_android.ui.dialogs.children.MinPositionDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.RelationshipOutputDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.SensorOutputDialog;

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
public class ServoDialog extends BaseOutputDialog implements Serializable,
        AdvancedSettingsDialog.DialogAdvancedSettingsListener,
        SensorOutputDialog.DialogSensorListener,
        RelationshipOutputDialog.DialogRelationshipListener,
        DialogMaxPositionListener,
        MinPositionDialog.DialogMinPositionListener {

    private View dialogView;
    private DialogServoListener dialogServoListener;
    private ImageView currentImageView;
    private TextView currentTextViewDescrp;
    private TextView currentTextViewItem;
    private Button saveButton;
    private Button removeButton;
    private Servo servo;


    private void updateViews(View view) {
        Flutter flutter = GlobalHandler.getInstance(view.getContext().getApplicationContext()).sessionHandler.getSession().getFlutter();
        super.updateViews(view, servo);

        LinearLayout linkedSensor,minPosLayout;
        ImageView advancedSettingsView = (ImageView) view.findViewById(R.id.image_advanced_settings);
        linkedSensor = (LinearLayout) view.findViewById(R.id.linear_set_linked_sensor);
        minPosLayout = (LinearLayout) view.findViewById(R.id.linear_set_min_pos);
        Relationship relationship = servo.getSettings().getRelationship();

        if (servo.getSettings().getClass() == SettingsConstant.class) {
            SettingsConstant settings = (SettingsConstant) servo.getSettings();

            // save
            saveButton.setEnabled(true);

            // advanced settings
            advancedSettingsView.setVisibility(View.GONE);

            // sensor
            linkedSensor.setVisibility(View.GONE);

            // max
            ImageView maxPosImg = (ImageView) view.findViewById(R.id.image_max_pos);
            RotateAnimation rotateAnimation = new RotateAnimation(0, settings.getValue(), Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setFillEnabled(true);
            rotateAnimation.setFillAfter(true);
            rotateAnimation.setDuration(0);
            maxPosImg.startAnimation(rotateAnimation);
            TextView maxPosTxt = (TextView) view.findViewById(R.id.text_max_pos);
            TextView maxPosValue = (TextView) view.findViewById(R.id.text_max_pos_value);
            maxPosTxt.setText("Value");
            maxPosValue.setText(String.valueOf(settings.getValue()));

            // min
            minPosLayout.setVisibility(View.GONE);
        } else if (servo.getSettings().getClass() == SettingsProportional.class) {
            SettingsProportional settings = (SettingsProportional) servo.getSettings();
            Sensor sensor;
            if (settings.getSensorPortNumber() != 0) {
                sensor = flutter.getSensors()[settings.getSensorPortNumber()-1];
            }  else {
                sensor = new NoSensor(0);
            }

            if (servo.getSettings().getRelationship().getClass() != Proportional.class) {
                Log.e(Constants.LOG_TAG,"tried to run ServoDialog.updateViews on unimplemented relationship.");
            }
            // save
            if (sensor.getSensorType() != FlutterProtocol.InputTypes.NOT_SET) {
                saveButton.setEnabled(true);
            } else {
                saveButton.setEnabled(false);
            }

            // advanced settings
            advancedSettingsView.setVisibility(View.VISIBLE);

            // sensor
            linkedSensor.setVisibility(View.VISIBLE);

            // max
            ImageView maxPosImg = (ImageView) view.findViewById(R.id.image_max_pos);
            RotateAnimation rotateAnimation = new RotateAnimation(0, settings.getOutputMax(), Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setFillEnabled(true);
            rotateAnimation.setFillAfter(true);
            rotateAnimation.setDuration(0);
            maxPosImg.startAnimation(rotateAnimation);
            TextView maxPosTxt = (TextView) view.findViewById(R.id.text_max_pos);
            TextView maxPosValue = (TextView) view.findViewById(R.id.text_max_pos_value);
            maxPosTxt.setText(sensor.getHighTextId());
            maxPosValue.setText(String.valueOf(settings.getOutputMax()));

            // min
            minPosLayout.setVisibility(View.VISIBLE);
            ImageView minPosImg = (ImageView) view.findViewById(R.id.image_min_pos);
            rotateAnimation = new RotateAnimation(0, settings.getOutputMin(), Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setFillEnabled(true);
            rotateAnimation.setFillAfter(true);
            rotateAnimation.setDuration(0);
            minPosImg.startAnimation(rotateAnimation);
            TextView minPosTxt = (TextView) view.findViewById(R.id.text_min_pos);
            TextView minPosValue = (TextView) view.findViewById(R.id.text_min_pos_value);
            minPosTxt.setText(sensor.getLowTextId());
            minPosValue.setText(String.valueOf(settings.getOutputMin()));
        } else {
            Log.e(Constants.LOG_TAG,"ServoDialog.updateViews: unimplemented Relationship/Settings.");
        }

        saveButton.setEnabled(true);
        removeButton.setEnabled(true);
        if (relationship.getClass() != Constant.class && servo.getSettings().getSensor().getSensorType() == FlutterProtocol.InputTypes.NOT_SET) {
            saveButton.setEnabled(false);
            removeButton.setEnabled(false);
        }
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
        // NO!
        servo = Servo.newInstance((Servo) getArguments().getSerializable(Servo.SERVO_KEY));

        dialogServoListener = (DialogServoListener) getArguments().getSerializable(Constants.SerializableKeys.DIALOG_SERVO);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_servos, null);
        this.dialogView = view;
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.text_output_title)).setText(getString(R.string.set_up_servo) + " " +  String.valueOf(servo.getPortNumber()));
        ((ImageView) view.findViewById(R.id.text_output_title_icon)).setImageResource(R.drawable.servo_icon);

        ButterKnife.bind(this, view);
        saveButton = (Button) view.findViewById(R.id.button_save_link);
        removeButton = (Button) view.findViewById(R.id.button_remove_link);

        updateViews(view);
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
        currentImageView = (ImageView) ((ViewGroup) view).getChildAt(0);
        View layout = ((ViewGroup) view).getChildAt(1);
        currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);
        DialogFragment dialog = SensorOutputDialog.newInstance(this);
        dialog.show(this.getFragmentManager(), "tag");
    }


    @OnClick(R.id.linear_set_relationship)
    public void onClickSetRelationship(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetRelationship");
        currentImageView = (ImageView) ((ViewGroup) view).getChildAt(0);
        View layout = ((ViewGroup) view).getChildAt(1);
        currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);
        DialogFragment dialog = RelationshipOutputDialog.newInstance(this);
        dialog.show(this.getFragmentManager(), "tag");
    }


    @OnClick(R.id.linear_set_max_pos)
    public void onClickSetMaximumPosition(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetMaximumPosition");
        View layout = ((ViewGroup) view).getChildAt(0);
        currentImageView = (ImageView) ((ViewGroup) layout).getChildAt(0);
        layout = ((ViewGroup) view).getChildAt(1);
        currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);
        if (servo.getSettings().getClass() == SettingsProportional.class) {
            DialogFragment dialog = MaxPositionDialog.newInstance(Integer.valueOf(((SettingsProportional)servo.getSettings()).getOutputMax()), this);
            dialog.show(this.getFragmentManager(), "tag");
        } else if (servo.getSettings().getClass() == SettingsConstant.class) {
            DialogFragment dialog = MaxPositionDialog.newInstance(Integer.valueOf(((SettingsConstant)servo.getSettings()).getValue()), this);
            dialog.show(this.getFragmentManager(), "tag");
        } else {
            Log.e(Constants.LOG_TAG,"ServoDialog.onClickSetMaximumPosition: unimplemented Relationship/Settings.");
        }
    }


    @OnClick(R.id.linear_set_min_pos)
    public void onclickSetMinimumPosition(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetMinimumPosition");
        View layout = ((ViewGroup) view).getChildAt(0);
        currentImageView = (ImageView) ((ViewGroup) layout).getChildAt(0);
        layout = ((ViewGroup) view).getChildAt(1);
        currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);
        if (servo.getSettings().getClass() == SettingsProportional.class) {
            DialogFragment dialog = MinPositionDialog.newInstance(Integer.valueOf(((SettingsProportional)servo.getSettings()).getOutputMin()), this);
            dialog.show(this.getFragmentManager(), "tag");
        } else if (servo.getSettings().getClass() == SettingsConstant.class) {
            DialogFragment dialog = MinPositionDialog.newInstance(Integer.valueOf(((SettingsConstant)servo.getSettings()).getValue()), this);
            dialog.show(this.getFragmentManager(), "tag");
        } else {
            Log.e(Constants.LOG_TAG,"ServoDialog.onclickSetMinimumPosition: unimplemented Relationship/Settings.");
        }
    }


    // Option listeners


    @Override
    public void onAdvancedSettingsSet(AdvancedSettings advancedSettings) {
        Log.d(Constants.LOG_TAG, "onAdvancedSettingsSet");
        if (servo.getSettings().hasAdvancedSettings()) {
            if (servo.getSettings().getClass() == SettingsProportional.class) {
                ((SettingsProportional)servo.getSettings()).setAdvancedSettings(advancedSettings);
            }
        }
    }


    @Override
    public void onSensorChosen(Sensor sensor) {
        if (sensor.getSensorType() != FlutterProtocol.InputTypes.NOT_SET) {
            Log.d(Constants.LOG_TAG, "onSensorChosen");
            currentImageView.setImageResource(sensor.getGreenImageId());
            currentTextViewDescrp.setText(R.string.linked_sensor);
            currentTextViewItem.setText(sensor.getSensorTypeId());
            if (servo.getSettings().getClass() == SettingsProportional.class) {
                ((SettingsProportional)servo.getSettings()).setSensorPortNumber(sensor.getPortNumber());
            } else {
                Log.e(Constants.LOG_TAG,"ServoDialog.onSensorChosen: unimplemented Relationship/Settings.");
            }
        }
        updateViews(dialogView);
    }

    @Override
    public void onRelationshipChosen(Relationship relationship) {
        Log.d(Constants.LOG_TAG, "onRelationshipChosen");
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
        updateViews(dialogView);
    }


    @Override
    public void onMaxPosChosen(int max) {
        Log.d(Constants.LOG_TAG, "onMaxPosChosen");
        RotateAnimation rotateAnimation = new RotateAnimation(0, max, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setFillEnabled(true);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(0);
        currentImageView.startAnimation(rotateAnimation);
        if (servo.getSettings().getClass() == SettingsProportional.class) {
            SettingsProportional settings = (SettingsProportional) servo.getSettings();
            currentTextViewDescrp.setText(settings.getSensor().getHighTextId());
            currentTextViewItem.setText(String.valueOf(max) + (char) 0x00B0);
            settings.setOutputMax(max);
        } else if (servo.getSettings().getClass() == SettingsConstant.class) {
            SettingsConstant settings = (SettingsConstant) servo.getSettings();
            currentTextViewDescrp.setText(settings.getSensor().getHighTextId());
            currentTextViewItem.setText(String.valueOf(max) + (char) 0x00B0);
            settings.setValue(max);
        } else {
            Log.e(Constants.LOG_TAG,"ServoDialog.onMaxPosChosen: unimplemented Relationship/Settings.");
        }
    }


    @Override
    public void onMinPosChosen(int min) {
        Log.d(Constants.LOG_TAG, "onMinPosChosen");
        RotateAnimation rotateAnimation = new RotateAnimation(0, min, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setFillEnabled(true);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(0);
        currentImageView.startAnimation(rotateAnimation);
        if (servo.getSettings().getClass() == SettingsProportional.class) {
            SettingsProportional settings = (SettingsProportional) servo.getSettings();
            currentTextViewDescrp.setText(settings.getSensor().getLowTextId());
            currentTextViewItem.setText(String.valueOf(min) + (char) 0x00B0);
            settings.setOutputMin(min);
        } else if (servo.getSettings().getClass() == SettingsConstant.class) {
            SettingsConstant settings = (SettingsConstant) servo.getSettings();
            currentTextViewDescrp.setText(settings.getSensor().getLowTextId());
            currentTextViewItem.setText(String.valueOf(min) + (char) 0x00B0);
            settings.setValue(min);
        } else {
            Log.e(Constants.LOG_TAG,"ServoDialog.onMinPosChosen: unimplemented Relationship/Settings.");
        }
    }


    public interface DialogServoListener {
        public void onServoLinkListener(MelodySmartMessage message);
    }

}
