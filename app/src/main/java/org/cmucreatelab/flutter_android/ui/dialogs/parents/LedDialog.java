package org.cmucreatelab.flutter_android.ui.dialogs.parents;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.RobotActivity;
import org.cmucreatelab.flutter_android.classes.Settings;
import org.cmucreatelab.flutter_android.classes.outputs.LED;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.MessageConstructor;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.ColorHighDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.ColorLowDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.RelationshipOutputDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.SensorOutputDialog;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Steve on 10/17/2016.
 *
 * LedDialog
 *
 * A Dialog that shows the options for creating a link between LED and a Sensor
 */
public class LedDialog extends BaseResizableDialog implements Serializable, DialogInterface.OnClickListener,
        SensorOutputDialog.DialogSensorListener,
        RelationshipOutputDialog.DialogRelationshipListener,
        ColorHighDialog.DialogHighColorListener,
        ColorLowDialog.DialogLowColorListener {


    private DialogLedListener dialogLedListener;

    private Serializable serializable;
    private DialogFragment dialogFragment;

    private ImageView currentImageView;
    private TextView currentTextViewDescrp;
    private TextView currentTextViewItem;

    private View maxColor;
    private View minColor;

    private Settings ledSettings;
    private LED led;


    public static LedDialog newInstance(LED led, Serializable activity) {
        LedDialog ledDialog = new LedDialog();

        Bundle args = new Bundle();
        args.putSerializable(LED.LED_KEY, led);
        args.putSerializable(RobotActivity.SERIALIZABLE_KEY, activity);
        ledDialog.setArguments(args);

        return ledDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "onCreateDialog");
        super.onCreateDialog(savedInstanceState);

        serializable = this;
        dialogFragment = this;

        led = (LED) getArguments().getSerializable(LED.LED_KEY);
        dialogLedListener = (DialogLedListener) getArguments().getSerializable(RobotActivity.SERIALIZABLE_KEY);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_leds, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        builder.setPositiveButton(R.string.save_settings, this);
        ((TextView) view.findViewById(R.id.text_output_title)).setText(getString(R.string.set_up_led) + " " +  String.valueOf(led.getPortNumber()));
        ButterKnife.bind(this, view);

        ledSettings = new Settings();
        maxColor = view.findViewById(R.id.view_max_color);
        minColor = view.findViewById(R.id.view_min_color);

        return builder.create();
    }


    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        Log.d(Constants.LOG_TAG, "onClickSave");
        led.setSettings(ledSettings);
        String msg = MessageConstructor.getLedLinkMessage(led);
        Log.d(Constants.LOG_TAG, msg);
        dialogLedListener.onLedLinkListener(msg);
    }


    // onClick listeners


    @OnClick(R.id.linear_set_linked_sensor)
    public void onClickSetLinkedSensor(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetLinkedSensor");
        currentImageView = (ImageView) ((ViewGroup) view).getChildAt(0);
        View layout = ((ViewGroup) view).getChildAt(1);
        currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);
        DialogFragment dialog = SensorOutputDialog.newInstance(serializable);
        dialog.show(dialogFragment.getFragmentManager(), "tag");
    }


    @OnClick(R.id.linear_set_relationship)
    public void onClickSetRelationship(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetRelationship");
        currentImageView = (ImageView) ((ViewGroup) view).getChildAt(0);
        View layout = ((ViewGroup) view).getChildAt(1);
        currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);
        DialogFragment dialog = RelationshipOutputDialog.newInstance(serializable);
        dialog.show(dialogFragment.getFragmentManager(), "tag");
    }


    @OnClick(R.id.linear_set_max_color)
    public void onClickSetMaximumColor(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetMaximumColor");
        ViewGroup viewGroup = (ViewGroup) ((ViewGroup) view).getChildAt(0);
        currentImageView = (ImageView) viewGroup.getChildAt(0);
        View layout = ((ViewGroup) view).getChildAt(1);
        currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);
        DialogFragment dialog = ColorHighDialog.newInstance(serializable);
        dialog.show(dialogFragment.getFragmentManager(), "tag");
    }


    @OnClick(R.id.linear_set_min_color)
    public void onclickSetMinimumColor(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetMinimumColor");
        ViewGroup viewGroup = (ViewGroup) ((ViewGroup) view).getChildAt(0);
        currentImageView = (ImageView) viewGroup.getChildAt(0);
        View layout = ((ViewGroup) view).getChildAt(1);
        currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);
        DialogFragment dialog = ColorLowDialog.newInstance(serializable);
        dialog.show(dialogFragment.getFragmentManager(), "tag");
    }


    // option listeners


    @Override
    public void onSensorChosen(Sensor sensor) {
        Log.d(Constants.LOG_TAG, "onSensorChosen");
        currentImageView.setImageResource(sensor.getGreenImageId());
        currentTextViewDescrp.setText(R.string.linked_sensor);
        currentTextViewItem.setText(sensor.getSensorType().toString());
        ledSettings.setSensor(sensor);
    }

    @Override
    public void onRelationshipChosen(Relationship relationship) {
        Log.d(Constants.LOG_TAG, "onRelationshipChosen");
        currentImageView.setImageResource(relationship.getGreenImageIdMd());
        currentTextViewDescrp.setText(R.string.relationship);
        currentTextViewItem.setText(relationship.getRelationshipType().toString());
        ledSettings.setRelationship(relationship);
    }


    @Override
    public void onHighColorChosen(int[] rgb) {
        Log.d(Constants.LOG_TAG, "onHighColorChosen");
        ((GradientDrawable) maxColor.getBackground()).setColor(Color.rgb(rgb[0], rgb[1], rgb[2]));
        maxColor.setVisibility(View.VISIBLE);
        currentImageView.setVisibility(View.GONE);
        currentTextViewDescrp.setText(R.string.maximum_color);
        currentTextViewItem.setText("Red: " + String.valueOf(rgb[0]) + " Blue: " + String.valueOf(rgb[1]) + " Green: " + String.valueOf(rgb[2]));
        ledSettings.setOutputMaxColor(rgb);
    }


    @Override
    public void onLowColorChosen(int[] rgb) {
        Log.d(Constants.LOG_TAG, "onLowColorChosen");
        ((GradientDrawable) minColor.getBackground()).setColor(Color.rgb(rgb[0], rgb[1], rgb[2]));
        minColor.setVisibility(View.VISIBLE);
        currentImageView.setVisibility(View.GONE);
        currentTextViewDescrp.setText(R.string.minimum_color);
        currentTextViewItem.setText("Red: " + String.valueOf(rgb[0]) + " Blue: " + String.valueOf(rgb[1]) + " Green: " + String.valueOf(rgb[2]));
        ledSettings.setOutputMinColor(rgb);
    }


    public interface DialogLedListener {
        public void onLedLinkListener(String message);
    }

}
