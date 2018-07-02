package org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.led;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.TriColorLed;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.settings.AdvancedSettings;

/**
 * Created by mike on 4/13/17.
 */
public abstract class LedDialogStateHelperWithAdvancedSettings extends LedDialogStateHelper {


    LedDialogStateHelperWithAdvancedSettings(TriColorLed triColorLed) {
        super(triColorLed);
    }


    @Override
    public void updateView(LedDialog dialog) {
        LinearLayout linkedSensor,minColorLayout;
        ImageView advancedSettingsView = (ImageView) dialog.dialogView.findViewById(R.id.image_advanced_settings);
        linkedSensor = (LinearLayout) dialog.dialogView.findViewById(R.id.linear_set_linked_sensor);
        minColorLayout = (LinearLayout) dialog.dialogView.findViewById(R.id.linear_set_min_color);

        // advanced settings
        advancedSettingsView.setVisibility(View.VISIBLE);

        // sensor
        linkedSensor.setVisibility(View.VISIBLE);

        // max
        ImageView maxColorImg = (ImageView) dialog.dialogView.findViewById(R.id.image_max_color);
        maxColorImg.setVisibility(View.GONE);
        if (!TriColorLed.isSwatchInExistingSelection(getTriColorLed().getMaxColorHex()))
            dialog.maxColor.setImageDrawable(getCustomSwatchWithBorder(getTriColorLed().getMaxColorHex(), dialog));
        else
            dialog.maxColor.setImageResource(TriColorLed.getSwatchFromColor(getTriColorLed().getMaxColorHex()));
        dialog.maxColor.setImageResource(TriColorLed.getSwatchFromColor(getTriColorLed().getMaxColorHex()));
        dialog.maxColor.setVisibility(View.VISIBLE);
        TextView maxColorTxt = (TextView) dialog.dialogView.findViewById(R.id.text_max_color);
        TextView maxColorValue = (TextView) dialog.dialogView.findViewById(R.id.text_max_color_value);
        maxColorTxt.setText(dialog.getString(getTriColorLed().getRedLed().getSettings().getSensor().getHighTextId())+" Color");
        maxColorValue.setText(TriColorLed.getTextFromColor(getTriColorLed().getMaxColorHex()));

        // min
        minColorLayout.setVisibility(View.VISIBLE);
        ImageView minColorImg = (ImageView) dialog.dialogView.findViewById(R.id.image_min_color);
        minColorImg.setVisibility(View.GONE);
        if (!TriColorLed.isSwatchInExistingSelection(getTriColorLed().getMinColorHex()))
            dialog.minColor.setImageDrawable(getCustomSwatchWithBorder(getTriColorLed().getMinColorHex(), dialog));
        else
            dialog.minColor.setImageResource(TriColorLed.getSwatchFromColor(getTriColorLed().getMinColorHex()));
        dialog.minColor.setVisibility(View.VISIBLE);
        TextView minColorTxt = (TextView) dialog.dialogView.findViewById(R.id.text_min_color);
        TextView minColorValue = (TextView) dialog.dialogView.findViewById(R.id.text_min_color_value);
        minColorTxt.setText(dialog.getString(getTriColorLed().getRedLed().getSettings().getSensor().getLowTextId())+" Color");
        minColorValue.setText(TriColorLed.getTextFromColor(getTriColorLed().getMinColorHex()));
    }

    private LayerDrawable getCustomSwatchWithBorder(String hexColor, LedDialog dialog)
    {
        LayerDrawable layerDrawable = (LayerDrawable) dialog.getActivity().getApplicationContext().getResources().getDrawable(R.drawable.universal_swatch);

        ((GradientDrawable) layerDrawable.findDrawableByLayerId(R.id.color_main_swatch)).setColor(Color.parseColor(hexColor));

        return layerDrawable;
    }
    @Override
    public void setAdvancedSettings(AdvancedSettings advancedSettings) {
        getTriColorLed().setAdvancedSettings(advancedSettings);
    }


    @Override
    public void setLinkedSensor(Sensor sensor) {
        getTriColorLed().setSensorPortNumber(sensor.getPortNumber());
    }


    @Override
    public void setMinimumColor(int red, int green, int blue) {
        getTriColorLed().setOutputMin(red, green, blue);
    }


    @Override
    public void setMaximumColor(int red, int green, int blue) {
        getTriColorLed().setOutputMax(red, green, blue);
    }

}
