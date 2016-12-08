package org.cmucreatelab.flutter_android.ui.dialogs;

import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.Output;
import org.cmucreatelab.flutter_android.classes.sensors.NoSensor;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.classes.settings.Settings;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

/**
 * Created by Steve on 11/4/2016.
 */
public abstract class BaseResizableDialog extends DialogFragment {


    private int convertDpToPx(int dp){
        return Math.round(dp*(getResources().getDisplayMetrics().xdpi/ DisplayMetrics.DENSITY_DEFAULT));
    }


    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(convertDpToPx(350), ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    protected void updateViews(View view, Output output) {
        if (output.getSettings() != null) {
            Log.d(Constants.LOG_TAG, "updateViews");
            Settings settings = output.getSettings();

            // sensor
            if (output.getSettings().getSensor().getSensorType() != Sensor.Type.NO_SENSOR) {
                ImageView sensorImage = (ImageView) view.findViewById(R.id.image_sensor);
                sensorImage.setImageResource(settings.getSensor().getGreenImageId());
                TextView sensorText = (TextView) view.findViewById(R.id.text_sensor_link);
                sensorText.setText(R.string.linked_sensor);
                TextView sensorType = (TextView) view.findViewById(R.id.text_sensor_type);
                sensorType.setText(getString(settings.getSensor().getSensorTypeId()));
                Button saveButton = (Button) view.findViewById(R.id.button_save_settings);
                saveButton.setEnabled(true);
            }

            // relationship
            ImageView relationshipImage = (ImageView) view.findViewById(R.id.image_relationship);
            relationshipImage.setImageResource(settings.getRelationship().getGreenImageIdMd());
            TextView relationshipText = (TextView) view.findViewById(R.id.text_relationship);
            relationshipText.setText(R.string.relationship);
            TextView relationshipType = (TextView) view.findViewById(R.id.text_relationship_type);
            relationshipType.setText(settings.getRelationship().getRelationshipType().toString());
        }
    }

}
