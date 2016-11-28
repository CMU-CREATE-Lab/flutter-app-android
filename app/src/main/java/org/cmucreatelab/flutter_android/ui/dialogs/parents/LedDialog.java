package org.cmucreatelab.flutter_android.ui.dialogs.parents;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
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
import org.cmucreatelab.flutter_android.classes.outputs.Led;
import org.cmucreatelab.flutter_android.classes.outputs.Output;
import org.cmucreatelab.flutter_android.classes.settings.Settings;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.MessageConstructor;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.AdvancedSettingsDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.MaxColorDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.MinColorDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.RelationshipOutputDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.children.SensorOutputDialog;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Steve on 10/17/2016.
 *
 * LedDialog
 *
 * A Dialog that shows the options for creating a link between Led and a Sensor
 */
public class LedDialog extends BaseResizableDialog implements Serializable,
        AdvancedSettingsDialog.DialogAdvancedSettingsListener,
        SensorOutputDialog.DialogSensorListener,
        RelationshipOutputDialog.DialogRelationshipListener,
        MaxColorDialog.DialogHighColorListener,
        MinColorDialog.DialogLowColorListener {


    private DialogLedListener dialogLedListener;

    private Serializable serializable;
    private DialogFragment dialogFragment;

    private ImageView currentImageView;
    private TextView currentTextViewDescrp;
    private TextView currentTextViewItem;

    private View maxColor;
    private View minColor;

    private Settings redSettings;
    private Settings greenSettings;
    private Settings blueSettings;
    private Led led;


    private int getProportionalValue(float value, float maxValue, float newMaxValue) {
        int result = 0;

        float ratio = value / maxValue;
        float temp = ratio*newMaxValue;
        result = (int) (ratio*newMaxValue);

        return result;
    }


    public static LedDialog newInstance(Led led, Serializable activity) {
        LedDialog ledDialog = new LedDialog();

        Bundle args = new Bundle();
        args.putSerializable(Led.LED_KEY, led);
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

        led = (Led) getArguments().getSerializable(Led.LED_KEY);
        dialogLedListener = (DialogLedListener) getArguments().getSerializable(RobotActivity.SERIALIZABLE_KEY);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_leds, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.text_output_title)).setText(getString(R.string.set_up_led) + " " +  String.valueOf(led.getPortNumber()));
        ButterKnife.bind(this, view);

        redSettings = new Settings("r");
        greenSettings = new Settings("g");
        blueSettings = new Settings("b");
        led.setRedSettings(redSettings);
        led.setGreenSettings(greenSettings);
        led.setBlueSettings(blueSettings);
        led.setSettings(redSettings);

        maxColor = view.findViewById(R.id.view_max_color);
        minColor = view.findViewById(R.id.view_min_color);

        return builder.create();
    }


    // onClick listeners


    @OnClick(R.id.button_save_settings)
    public void onClickSaveSettings() {
        Log.d(Constants.LOG_TAG, "onClickSaveSettings");
        ArrayList<String> msg = new ArrayList<>();
        led.setSettings(redSettings);
        msg.add(MessageConstructor.getRemoveLinkMessage(led));
        led.setSettings(greenSettings);
        msg.add(MessageConstructor.getRemoveLinkMessage(led));
        led.setSettings(blueSettings);
        msg.add(MessageConstructor.getRemoveLinkMessage(led));
        led.setSettings(redSettings);
        msg.add(MessageConstructor.getLinkedMessage(led));
        led.setSettings(greenSettings);
        msg.add(MessageConstructor.getLinkedMessage(led));
        led.setSettings(blueSettings);
        msg.add(MessageConstructor.getLinkedMessage(led));
        led.setIsLinked(true);
        dialogLedListener.onLedLinkListener(msg);
        this.dismiss();
    }


    @OnClick(R.id.button_remove_link)
    public void onClickRemoveLink() {
        Log.d(Constants.LOG_TAG, "onClickRemoveLink");
        ArrayList<String> msg = new ArrayList<>();
        led.setSettings(led.getRedSettings());
        msg.add( MessageConstructor.getRemoveLinkMessage(led));
        led.setSettings(led.getGreenSettings());
        msg.add( MessageConstructor.getRemoveLinkMessage(led));
        led.setSettings(led.getBlueSettings());
        msg.add( MessageConstructor.getRemoveLinkMessage(led));
        led.setIsLinked(false);
        dialogLedListener.onLedLinkListener(msg);
        this.dismiss();
    }


    @OnClick(R.id.image_advanced_settings)
    public void onClickAdvancedSettings() {
        Log.d(Constants.LOG_TAG, "onClickAdvancedSettings");
        DialogFragment dialog = AdvancedSettingsDialog.newInstance(this, led);
        dialog.show(dialogFragment.getFragmentManager(), "tag");
    }


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
        DialogFragment dialog = MaxColorDialog.newInstance(serializable);
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
        DialogFragment dialog = MinColorDialog.newInstance(serializable);
        dialog.show(dialogFragment.getFragmentManager(), "tag");
    }


    // option listeners


    @Override
    public void onAdvancedSettingsSet() {
        Log.d(Constants.LOG_TAG, "onAdvancedSettingsSet");
        // believe me
    }


    @Override
    public void onSensorChosen(Sensor sensor) {
        Log.d(Constants.LOG_TAG, "onSensorChosen");
        currentImageView.setImageResource(sensor.getGreenImageId());
        currentTextViewDescrp.setText(R.string.linked_sensor);
        currentTextViewItem.setText(sensor.getSensorType().toString());
        redSettings.setSensor(sensor);
        greenSettings.setSensor(sensor);
        blueSettings.setSensor(sensor);
    }

    @Override
    public void onRelationshipChosen(Relationship relationship) {
        Log.d(Constants.LOG_TAG, "onRelationshipChosen");
        currentImageView.setImageResource(relationship.getGreenImageIdMd());
        currentTextViewDescrp.setText(R.string.relationship);
        currentTextViewItem.setText(relationship.getRelationshipType().toString());
        redSettings.setRelationship(relationship);
        greenSettings.setRelationship(relationship);
        blueSettings.setRelationship(relationship);
    }


    @Override
    public void onHighColorChosen(int[] rgb) {
        Log.d(Constants.LOG_TAG, "onHighColorChosen");
        ((GradientDrawable) maxColor.getBackground()).setColor(Color.rgb(rgb[0], rgb[1], rgb[2]));
        maxColor.setVisibility(View.VISIBLE);
        currentImageView.setVisibility(View.GONE);
        currentTextViewDescrp.setText(R.string.maximum_color);
        currentTextViewItem.setText("Red: " + String.valueOf(rgb[0]) + " Blue: " + String.valueOf(rgb[1]) + " Green: " + String.valueOf(rgb[2]));
        int max = getProportionalValue(rgb[0], 255, Led.MAXIMUM);
        redSettings.setOutputMax(max);
        max = getProportionalValue(rgb[1], 255, Led.MAXIMUM);
        greenSettings.setOutputMax(max);
        max = getProportionalValue(rgb[2], 255, Led.MAXIMUM);
        blueSettings.setOutputMax(max);
    }


    @Override
    public void onLowColorChosen(int[] rgb) {
        Log.d(Constants.LOG_TAG, "onLowColorChosen");
        ((GradientDrawable) minColor.getBackground()).setColor(Color.rgb(rgb[0], rgb[1], rgb[2]));
        minColor.setVisibility(View.VISIBLE);
        currentImageView.setVisibility(View.GONE);
        currentTextViewDescrp.setText(R.string.minimum_color);
        currentTextViewItem.setText("Red: " + String.valueOf(rgb[0]) + " Blue: " + String.valueOf(rgb[1]) + " Green: " + String.valueOf(rgb[2]));
        int min = getProportionalValue(rgb[0], 255, Led.MAXIMUM);
        redSettings.setOutputMin(min);
        min = getProportionalValue(rgb[1], 255, Led.MAXIMUM);
        greenSettings.setOutputMin(min);
        min = getProportionalValue(rgb[2], 255, Led.MAXIMUM);
        blueSettings.setOutputMin(min);
    }


    public interface DialogLedListener {
        public void onLedLinkListener(ArrayList<String> msgs);
    }

}
