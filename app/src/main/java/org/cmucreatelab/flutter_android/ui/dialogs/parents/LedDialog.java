package org.cmucreatelab.flutter_android.ui.dialogs.parents;

import android.app.Dialog;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.TriColorLed;
import org.cmucreatelab.flutter_android.classes.settings.AdvancedSettings;
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
    private Button saveButton;

    private View maxColor;
    private View minColor;

    private Settings redSettings;
    private Settings greenSettings;
    private Settings blueSettings;
    private TriColorLed triColorLed;


    private void updateViews(View view) {
        updateViews(view, triColorLed.getRedLed());

        int redMax = getOutputToRgb(redSettings.getOutputMax());
        int redMin = getOutputToRgb(redSettings.getOutputMin());
        int greenMax = getOutputToRgb(greenSettings.getOutputMax());
        int greenMin = getOutputToRgb(greenSettings.getOutputMin());
        int blueMax = getOutputToRgb(blueSettings.getOutputMax());
        int blueMin = getOutputToRgb(blueSettings.getOutputMin());

        // max
        ImageView maxColorImg = (ImageView) view.findViewById(R.id.image_max_color);
        maxColorImg.setVisibility(View.GONE);
        ((GradientDrawable) maxColor.getBackground()).setColor(Color.rgb(redMax, greenMax, blueMax));
        maxColor.setVisibility(View.VISIBLE);
        TextView maxColorTxt = (TextView) view.findViewById(R.id.text_max_color);
        TextView maxColorValue = (TextView) view.findViewById(R.id.text_max_color_value);
        maxColorTxt.setText(R.string.maximum_color);
        maxColorValue.setText("Red: " + String.valueOf(redMax) + " Blue: " + String.valueOf(greenMax) + " Green: " + String.valueOf(blueMax));

        // min
        ImageView minColorImg = (ImageView) view.findViewById(R.id.image_min_color);
        minColorImg.setVisibility(View.GONE);
        ((GradientDrawable) minColor.getBackground()).setColor(Color.rgb(redMin, greenMin, blueMin));
        minColor.setVisibility(View.VISIBLE);
        TextView minColorTxt = (TextView) view.findViewById(R.id.text_min_color);
        TextView minColorValue = (TextView) view.findViewById(R.id.text_min_color_value);
        minColorTxt.setText(R.string.minimum_color);
        minColorValue.setText("Red: " + String.valueOf(redMin) + " Blue: " + String.valueOf(greenMin) + " Green: " + String.valueOf(blueMin));
    }


    private int getOutputToRgb(int value) {
        float result = 0;

        float ratio = 255.0f / 100.0f;
        Log.d(Constants.LOG_TAG, String.valueOf(ratio));
        result = (ratio*value);

        return (int) result;
    }


    private int getProportionalValue(float value, float maxValue, float newMaxValue) {
        int result = 0;

        float ratio = value / maxValue;
        result = (int) (ratio*newMaxValue);

        return result;
    }


    public static LedDialog newInstance(TriColorLed led, Serializable activity) {
        LedDialog ledDialog = new LedDialog();

        Bundle args = new Bundle();
        args.putSerializable(TriColorLed.LED_KEY, led);
        args.putSerializable(Constants.SERIALIZABLE_KEY, activity);
        ledDialog.setArguments(args);

        return ledDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "onCreateDialog");
        super.onCreateDialog(savedInstanceState);

        serializable = this;
        dialogFragment = this;

        triColorLed = (TriColorLed) getArguments().getSerializable(TriColorLed.LED_KEY);
        dialogLedListener = (DialogLedListener) getArguments().getSerializable(Constants.SERIALIZABLE_KEY);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_leds, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.text_output_title)).setText(getString(R.string.set_up_led) + " " +  String.valueOf(triColorLed.getPortNumber()));
        ButterKnife.bind(this, view);

        redSettings = triColorLed.getRedLed().getSettings();
        greenSettings = triColorLed.getGreenLed().getSettings();
        blueSettings = triColorLed.getBlueLed().getSettings();

        maxColor = view.findViewById(R.id.view_max_color);
        minColor = view.findViewById(R.id.view_min_color);

        updateViews(view);
        saveButton = (Button) view.findViewById(R.id.button_save_settings);

        return builder.create();
    }


    // onClick listeners


    @OnClick(R.id.button_save_settings)
    public void onClickSaveSettings() {
        Log.d(Constants.LOG_TAG, "onClickSaveSettings");
        ArrayList<String> msg = new ArrayList<>();
        msg.add(MessageConstructor.getRemoveLinkMessage(triColorLed.getRedLed()));
        msg.add(MessageConstructor.getRemoveLinkMessage(triColorLed.getGreenLed()));
        msg.add(MessageConstructor.getRemoveLinkMessage(triColorLed.getBlueLed()));

        msg.add(MessageConstructor.getLinkedMessage(triColorLed.getRedLed()));
        msg.add(MessageConstructor.getLinkedMessage(triColorLed.getGreenLed()));
        msg.add(MessageConstructor.getLinkedMessage(triColorLed.getBlueLed()));

        triColorLed.getRedLed().setIsLinked(true, triColorLed.getRedLed());
        triColorLed.getGreenLed().setIsLinked(true, triColorLed.getGreenLed());
        triColorLed.getBlueLed().setIsLinked(true, triColorLed.getBlueLed());

        dialogLedListener.onLedLinkListener(msg);
        this.dismiss();
    }


    @OnClick(R.id.button_remove_link)
    public void onClickRemoveLink() {
        ArrayList<String> msg = new ArrayList<>();
        Log.d(Constants.LOG_TAG, "onClickRemoveLink");

        msg.add(MessageConstructor.getRemoveLinkMessage(triColorLed.getRedLed()));
        msg.add(MessageConstructor.getRemoveLinkMessage(triColorLed.getGreenLed()));
        msg.add(MessageConstructor.getRemoveLinkMessage(triColorLed.getBlueLed()));

        triColorLed.getRedLed().setIsLinked(false, triColorLed.getRedLed());
        redSettings.setOutputMax(triColorLed.getRedLed().getMax());
        redSettings.setOutputMin(triColorLed.getRedLed().getMin());
        triColorLed.getGreenLed().setIsLinked(false, triColorLed.getGreenLed());
        greenSettings.setOutputMax(triColorLed.getGreenLed().getMax());
        greenSettings.setOutputMin(triColorLed.getGreenLed().getMin());
        triColorLed.getBlueLed().setIsLinked(false, triColorLed.getBlueLed());
        blueSettings.setOutputMax(triColorLed.getBlueLed().getMax());
        blueSettings.setOutputMin(triColorLed.getBlueLed().getMin());

        for(int i = 0; i < msg.size(); i++) {
            Log.d("this tag", msg.get(i));
        }

        dialogLedListener.onLedLinkListener(msg);

        this.dismiss();
    }


    @OnClick(R.id.image_advanced_settings)
    public void onClickAdvancedSettings() {
        Log.d(Constants.LOG_TAG, "onClickAdvancedSettings");
        DialogFragment dialog = AdvancedSettingsDialog.newInstance(this, triColorLed);
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
    public void onAdvancedSettingsSet(AdvancedSettings advancedSettings) {
        Log.d(Constants.LOG_TAG, "onAdvancedSettingsSet");
        redSettings.setAdvancedSettings(advancedSettings);
        greenSettings.setAdvancedSettings(advancedSettings);
        blueSettings.setAdvancedSettings(advancedSettings);
    }


    @Override
    public void onSensorChosen(Sensor sensor) {
        if (sensor.getSensorType() != Sensor.Type.NO_SENSOR) {
            Log.d(Constants.LOG_TAG, "onSensorChosen");
            currentImageView.setImageResource(sensor.getGreenImageId());
            currentTextViewDescrp.setText(R.string.linked_sensor);
            currentTextViewItem.setText(sensor.getSensorType().toString());
            redSettings.setSensor(sensor);
            greenSettings.setSensor(sensor);
            blueSettings.setSensor(sensor);
            saveButton.setEnabled(true);
        }
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
        int max = getProportionalValue(rgb[0], 255, triColorLed.getRedLed().getMax());
        Log.d(Constants.LOG_TAG, String.valueOf(max));
        redSettings.setOutputMax(max);
        max = getProportionalValue(rgb[1], 255, triColorLed.getGreenLed().getMax());
        Log.d(Constants.LOG_TAG, String.valueOf(max));
        greenSettings.setOutputMax(max);
        max = getProportionalValue(rgb[2], 255, triColorLed.getBlueLed().getMax());
        Log.d(Constants.LOG_TAG, String.valueOf(max));
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
        int min = getProportionalValue(rgb[0], 255, triColorLed.getRedLed().getMax());
        Log.d(Constants.LOG_TAG, String.valueOf(min));
        redSettings.setOutputMin(min);
        min = getProportionalValue(rgb[1], 255, triColorLed.getGreenLed().getMax());
        Log.d(Constants.LOG_TAG, String.valueOf(min));
        greenSettings.setOutputMin(min);
        min = getProportionalValue(rgb[2], 255, triColorLed.getBlueLed().getMax());
        Log.d(Constants.LOG_TAG, String.valueOf(min));
        blueSettings.setOutputMin(min);
    }


    public interface DialogLedListener {
        public void onLedLinkListener(ArrayList<String> msgs);
    }

}
