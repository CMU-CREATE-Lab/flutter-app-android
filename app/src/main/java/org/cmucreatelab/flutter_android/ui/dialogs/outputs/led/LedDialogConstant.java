package org.cmucreatelab.flutter_android.ui.dialogs.outputs.led;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.TriColorLed;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.settings.AdvancedSettings;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

/**
 * Created by mike on 3/27/17.
 *
 * LedDialogStateHelper implementation with Constant relationship
 *
 */
public class LedDialogConstant extends LedDialogStateHelper {


    LedDialogConstant(TriColorLed triColorLed) {
        super(triColorLed);
    }


    public static LedDialogConstant newInstance(TriColorLed triColorLed) {
        return new LedDialogConstant(triColorLed);
    }


    @Override
    public void updateView(LedDialog dialog) {
        LinearLayout linkedSensor,minColorLayout;
        ImageView advancedSettingsView = (ImageView) dialog.dialogView.findViewById(R.id.image_advanced_settings);
        linkedSensor = (LinearLayout) dialog.dialogView.findViewById(R.id.linear_set_linked_sensor);
        minColorLayout = (LinearLayout) dialog.dialogView.findViewById(R.id.linear_set_min_color);

        // advanced settings
        advancedSettingsView.setVisibility(View.GONE);

        // sensor
        linkedSensor.setVisibility(View.GONE);

        // max
        ImageView maxColorImg = (ImageView) dialog.dialogView.findViewById(R.id.image_max_color);
        maxColorImg.setVisibility(View.GONE);
        dialog.maxColor.setImageResource(getTriColorLed().getMaxSwatch());
        dialog.maxColor.setVisibility(View.VISIBLE);
        TextView maxColorTxt = (TextView) dialog.dialogView.findViewById(R.id.text_max_color);
        TextView maxColorValue = (TextView) dialog.dialogView.findViewById(R.id.text_max_color_value);
        maxColorTxt.setText("Color");
        maxColorValue.setText(getTriColorLed().getMaxColorText());

        // min
        minColorLayout.setVisibility(View.GONE);
    }


    @Override
    public void setAdvancedSettings(AdvancedSettings advancedSettings) {
        Log.w(Constants.LOG_TAG,"LedDialogConstant.setAdvancedSettings: attribute not implemented");
    }


    @Override
    public void setLinkedSensor(Sensor sensor) {
        Log.w(Constants.LOG_TAG,"LedDialogConstant.setLinkedSensor: attribute not implemented");
    }


    @Override
    public void setMinimumColor(int red, int green, int blue) {
        Log.w(Constants.LOG_TAG,"LedDialogConstant.setMinimumColor: attribute not implemented");
    }


    @Override
    public void setMaximumColor(int red, int green, int blue) {
        getTriColorLed().setOutputMax(red, green, blue);
    }

}
