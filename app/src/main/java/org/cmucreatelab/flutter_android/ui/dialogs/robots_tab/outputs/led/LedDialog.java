package org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.led;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.android.melodysmart.models.MelodySmartMessage;
import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.TriColorLed;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.settings.AdvancedSettings;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.settings.Settings;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.FlutterProtocol;
import org.cmucreatelab.flutter_android.helpers.static_classes.MessageConstructor;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.children.AdvancedSettingsDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.children.MaxColorDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.children.MinColorDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.children.RelationshipOutputDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.children.SensorOutputDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.BaseOutputDialog;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static org.cmucreatelab.flutter_android.helpers.static_classes.FlutterProtocol.InputTypes.NOT_SET;

/**
 * Created by Steve on 10/17/2016.
 *
 * LedDialog
 *
 * A Dialog that shows the options for creating a link between Led and a Sensor
 */
public class LedDialog extends BaseOutputDialog implements Serializable,
        AdvancedSettingsDialog.DialogAdvancedSettingsListener,
        SensorOutputDialog.DialogSensorListener,
        RelationshipOutputDialog.DialogRelationshipListener,
        MaxColorDialog.DialogHighColorListener,
        MinColorDialog.DialogLowColorListener {

    public View dialogView;
    public ImageView maxColor;
    public ImageView minColor;

    private LedDialogStateHelper stateHelper;
    private DialogLedListener dialogLedListener;
    private TriColorLed triColorLed;

    // animations
    private AlphaAnimation blinkAnimation;

    private void updateViews() {
        super.updateViews(dialogView, triColorLed.getRedLed());

        if (triColorLed.getRedLed().getSettings().getClass() != triColorLed.getGreenLed().getSettings().getClass() || triColorLed.getGreenLed().getSettings().getClass() != triColorLed.getBlueLed().getSettings().getClass()) {
            Log.w(Constants.LOG_TAG,"LedDialog.updateViews assumes same relationship for all Leds but they are not the same.");
        }
        stateHelper.updateView(this);

        if (triColorLed.getRedLed().getSettings().getSensor().getSensorType() == NOT_SET) {
            ImageView currentImageViewHighlight = (ImageView) dialogView.findViewById(R.id.image_sensor_highlight);
            currentImageViewHighlight.startAnimation(blinkAnimation);
        }

        Button saveButton = (Button) dialogView.findViewById(R.id.button_save_link);
        Button removeButton = (Button) dialogView.findViewById(R.id.button_remove_link);
        saveButton.setEnabled(stateHelper.canSaveLink());
        removeButton.setEnabled(stateHelper.canRemoveLink());
    }


    public static LedDialog newInstance(TriColorLed led, Serializable activity) {
        LedDialog ledDialog = new LedDialog();

        Bundle args = new Bundle();
        args.putSerializable(TriColorLed.LED_KEY, led);
        args.putSerializable(Constants.SerializableKeys.DIALOG_LED, activity);
        ledDialog.setArguments(args);

        return ledDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "onCreateDialog");
        super.onCreateDialog(savedInstanceState);

        // clone old object
        triColorLed = TriColorLed.newInstance((TriColorLed) getArguments().getSerializable(TriColorLed.LED_KEY));
        stateHelper = LedDialogStateHelper.newInstance(triColorLed);
        dialogLedListener = (DialogLedListener) getArguments().getSerializable(Constants.SerializableKeys.DIALOG_LED);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_leds, null);
        this.dialogView = view;
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.text_output_title)).setText(getString(R.string.set_up_led) + " " +  String.valueOf(triColorLed.getPortNumber()));
        ((ImageView) view.findViewById(R.id.text_output_title_icon)).setImageResource(R.drawable.led);
        ButterKnife.bind(this, view);

        maxColor = (ImageView) view.findViewById(R.id.view_max_color);
        minColor = (ImageView) view.findViewById(R.id.view_min_color);

        // Create animation to highlight a sensor that's never been linked
        blinkAnimation = new AlphaAnimation((float)0.8, 0);
        blinkAnimation.setDuration(900);
        blinkAnimation.setStartOffset(150);
        blinkAnimation.setRepeatCount(Animation.INFINITE);
        blinkAnimation.setRepeatMode(Animation.REVERSE);

        updateViews();
        return builder.create();
    }


    // onClick listeners


    @OnClick(R.id.button_save_link)
    public void onClickSaveSettings() {
        Log.d(Constants.LOG_TAG, "onClickSaveSettings");
        ArrayList<MelodySmartMessage> msg = new ArrayList<>();
        msg.add(MessageConstructor.constructRemoveRelation(triColorLed.getRedLed()));
        msg.add(MessageConstructor.constructRemoveRelation(triColorLed.getGreenLed()));
        msg.add(MessageConstructor.constructRemoveRelation(triColorLed.getBlueLed()));

        msg.add(MessageConstructor.constructRelationshipMessage(triColorLed.getRedLed(), triColorLed.getRedLed().getSettings()));
        msg.add(MessageConstructor.constructRelationshipMessage(triColorLed.getGreenLed(), triColorLed.getGreenLed().getSettings()));
        msg.add(MessageConstructor.constructRelationshipMessage(triColorLed.getBlueLed(), triColorLed.getBlueLed().getSettings()));

        triColorLed.getRedLed().setIsLinked(true, triColorLed.getRedLed());
        triColorLed.getGreenLed().setIsLinked(true, triColorLed.getGreenLed());
        triColorLed.getBlueLed().setIsLinked(true, triColorLed.getBlueLed());

        // overwrite old object
        GlobalHandler.getInstance(getActivity()).sessionHandler.getSession().getFlutter().getTriColorLeds()[triColorLed.getPortNumber()-1] = triColorLed;

        dialogLedListener.onLedLinkListener(msg);
        this.dismiss();
    }


    @OnClick(R.id.button_remove_link)
    public void onClickRemoveLink() {
        ArrayList<MelodySmartMessage> msg = new ArrayList<>();
        Log.d(Constants.LOG_TAG, "onClickRemoveLink");

        msg.add(MessageConstructor.constructRemoveRelation(triColorLed.getRedLed()));
        msg.add(MessageConstructor.constructRemoveRelation(triColorLed.getGreenLed()));
        msg.add(MessageConstructor.constructRemoveRelation(triColorLed.getBlueLed()));

        triColorLed.getRedLed().setIsLinked(false, triColorLed.getRedLed());
        triColorLed.getGreenLed().setIsLinked(false, triColorLed.getGreenLed());
        triColorLed.getBlueLed().setIsLinked(false, triColorLed.getBlueLed());

        // overwrite old object
        GlobalHandler.getInstance(getActivity()).sessionHandler.getSession().getFlutter().getTriColorLeds()[triColorLed.getPortNumber()-1] = triColorLed;

        dialogLedListener.onLedLinkListener(msg);
        this.dismiss();
    }


    @OnClick(R.id.image_advanced_settings)
    public void onClickAdvancedSettings() {
        Log.d(Constants.LOG_TAG, "onClickAdvancedSettings");
        DialogFragment dialog = AdvancedSettingsDialog.newInstance(this, triColorLed);
        dialog.show(this.getFragmentManager(), "tag");
    }

    @OnClick(R.id.button_close)
    public void onClickClose() {
        Dialog dialog = getDialog();
        dialog.dismiss();
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


    @OnClick(R.id.linear_set_max_color)
    public void onClickSetMaximumColor(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetMaximumColor");
        DialogFragment dialog = MaxColorDialog.newInstance(triColorLed.getMaxColorHex(),this);
        dialog.show(this.getFragmentManager(), "tag");
    }


    @OnClick(R.id.linear_set_min_color)
    public void onclickSetMinimumColor(View view) {
        Log.d(Constants.LOG_TAG, "onClickSetMinimumColor");
        DialogFragment dialog = MinColorDialog.newInstance(triColorLed.getMinColorHex(),this);
        dialog.show(this.getFragmentManager(), "tag");
    }


    // option listeners


    @Override
    public void onAdvancedSettingsSet(AdvancedSettings advancedSettings) {
        Log.d(Constants.LOG_TAG, "onAdvancedSettingsSet");
        stateHelper.setAdvancedSettings(advancedSettings);
    }


    @Override
    public void onSensorChosen(Sensor sensor) {
        ImageView currentImageView = (ImageView) dialogView.findViewById(R.id.image_sensor);
        ImageView currentImageViewHighlight = (ImageView) dialogView.findViewById(R.id.image_sensor_highlight);
        TextView currentTextViewDescrp = (TextView) dialogView.findViewById(R.id.text_sensor_link);
        TextView currentTextViewItem = (TextView) dialogView.findViewById(R.id.text_sensor_type);

        if (sensor.getSensorType() != FlutterProtocol.InputTypes.NOT_SET) {
            Log.d(Constants.LOG_TAG, "onSensorChosen");
            currentImageView.setImageResource(sensor.getGreenImageId());
            currentImageViewHighlight.clearAnimation();
            currentTextViewDescrp.setText(R.string.linked_sensor);
            currentTextViewItem.setText(sensor.getSensorTypeId());

            stateHelper.setLinkedSensor(sensor);
        }
        updateViews();
    }


    @Override
    public void onRelationshipChosen(Relationship relationship) {
        Log.d(Constants.LOG_TAG, "onRelationshipChosen");
        View view = dialogView.findViewById(R.id.linear_set_relationship);
        ImageView currentImageView = (ImageView) ((ViewGroup) view).getChildAt(0);
        View layout = ((ViewGroup) view).getChildAt(1);
        TextView currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        TextView currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);

        currentImageView.setImageResource(relationship.getGreenImageIdMd());
        currentTextViewDescrp.setText(R.string.relationship);
        currentTextViewItem.setText(relationship.getRelationshipTypeId());

        triColorLed.getRedLed().setSettings(Settings.newInstance(triColorLed.getRedLed().getSettings(), relationship));
        triColorLed.getGreenLed().setSettings(Settings.newInstance(triColorLed.getGreenLed().getSettings(), relationship));
        triColorLed.getBlueLed().setSettings(Settings.newInstance(triColorLed.getBlueLed().getSettings(), relationship));
        stateHelper = LedDialogStateHelper.newInstance(triColorLed);
        updateViews();
    }


    @Override
    public void onHighColorChosen(Integer[] rgb, int swatch) {
        Log.d(Constants.LOG_TAG, "onHighColorChosen");
        View view = dialogView.findViewById(R.id.linear_set_max_color);
        ViewGroup viewGroup = (ViewGroup) ((ViewGroup) view).getChildAt(0);
        ImageView currentImageView = (ImageView) viewGroup.getChildAt(0);
        View layout = ((ViewGroup) view).getChildAt(1);
        TextView currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        TextView currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);

        stateHelper.setMaximumColor(rgb[0], rgb[1], rgb[2]);

        currentImageView.setVisibility(View.GONE);
        currentTextViewDescrp.setText(R.string.maximum_color);
        if (!TriColorLed.isSwatchInExistingSelection(triColorLed.getMaxColorHex()))
            maxColor.setImageDrawable(getCustomSwatchWithBorder(triColorLed.getMaxColorHex()));
        else
            maxColor.setImageResource(TriColorLed.getSwatchFromColor(triColorLed.getMaxColorHex()));
        maxColor.setVisibility(View.VISIBLE);
        currentTextViewItem.setText(TriColorLed.getTextFromColor(triColorLed.getMaxColorHex()));
    }


    @Override
    public void onLowColorChosen(Integer[] rgb, int swatch) {
        Log.d(Constants.LOG_TAG, "onLowColorChosen");
        View view = dialogView.findViewById(R.id.linear_set_min_color);
        ViewGroup viewGroup = (ViewGroup) ((ViewGroup) view).getChildAt(0);
        ImageView currentImageView = (ImageView) viewGroup.getChildAt(0);
        View layout = ((ViewGroup) view).getChildAt(1);
        TextView currentTextViewDescrp = (TextView) ((ViewGroup) layout).getChildAt(0);
        TextView currentTextViewItem = (TextView) ((ViewGroup) layout).getChildAt(1);

        stateHelper.setMinimumColor(rgb[0], rgb[1], rgb[2]);

        currentImageView.setVisibility(View.GONE);
        currentTextViewDescrp.setText(R.string.minimum_color);
        if (!TriColorLed.isSwatchInExistingSelection(triColorLed.getMinColorHex()))
            minColor.setImageDrawable(getCustomSwatchWithBorder(triColorLed.getMinColorHex()));
        else
            minColor.setImageResource(TriColorLed.getSwatchFromColor(triColorLed.getMinColorHex()));

        minColor.setVisibility(View.VISIBLE);
        currentTextViewItem.setText(TriColorLed.getTextFromColor(triColorLed.getMinColorHex()));
    }

    public LayerDrawable getCustomSwatchWithBorder(String hexColor)
    {
        LayerDrawable layerDrawable = (LayerDrawable) getResources().getDrawable(R.drawable.universal_swatch);

        ((GradientDrawable) layerDrawable.findDrawableByLayerId(R.id.color_main_swatch)).setColor(Color.parseColor(hexColor));

        return layerDrawable;
    }

    public interface DialogLedListener {
        public void onLedLinkListener(ArrayList<MelodySmartMessage> msgs);
    }

}
