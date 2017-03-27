package org.cmucreatelab.flutter_android.ui.dialogs.outputs.led;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.TriColorLed;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.settings.AdvancedSettings;

/**
 * Created by mike on 3/27/17.
 *
 * LedDialogStateHelper implementation with Proportional relationship
 *
 */
public class LedDialogProportional extends LedDialogStateHelper {


    LedDialogProportional(TriColorLed triColorLed) {
        super(triColorLed);
    }


    public static LedDialogProportional newInstance(TriColorLed triColorLed) {
        return new LedDialogProportional(triColorLed);
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
        dialog.maxColor.setImageResource(getTriColorLed().getMaxSwatch());
        dialog.maxColor.setVisibility(View.VISIBLE);
        TextView maxColorTxt = (TextView) dialog.dialogView.findViewById(R.id.text_max_color);
        TextView maxColorValue = (TextView) dialog.dialogView.findViewById(R.id.text_max_color_value);
        maxColorTxt.setText(dialog.getString(getTriColorLed().getRedLed().getSettings().getSensor().getHighTextId())+" Color");
        maxColorValue.setText(getTriColorLed().getMaxColorText());

        // min
        minColorLayout.setVisibility(View.VISIBLE);
        ImageView minColorImg = (ImageView) dialog.dialogView.findViewById(R.id.image_min_color);
        minColorImg.setVisibility(View.GONE);
        dialog.minColor.setImageResource(getTriColorLed().getMinSwatch());
        dialog.minColor.setVisibility(View.VISIBLE);
        TextView minColorTxt = (TextView) dialog.dialogView.findViewById(R.id.text_min_color);
        TextView minColorValue = (TextView) dialog.dialogView.findViewById(R.id.text_min_color_value);
        minColorTxt.setText(dialog.getString(getTriColorLed().getRedLed().getSettings().getSensor().getLowTextId())+" Color");
        minColorValue.setText(getTriColorLed().getMinColorText());
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
