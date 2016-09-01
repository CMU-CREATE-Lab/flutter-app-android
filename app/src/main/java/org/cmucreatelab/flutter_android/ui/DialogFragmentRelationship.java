package org.cmucreatelab.flutter_android.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.SensorsActivity;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseServoLedActivity;
import org.cmucreatelab.flutter_android.classes.relationships.Amplitutude;
import org.cmucreatelab.flutter_android.classes.relationships.Change;
import org.cmucreatelab.flutter_android.classes.relationships.Cumulative;
import org.cmucreatelab.flutter_android.classes.relationships.Frequency;
import org.cmucreatelab.flutter_android.classes.relationships.NoRelationship;
import org.cmucreatelab.flutter_android.classes.relationships.Proportional;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.io.Serializable;

/**
 * Created by Steve on 9/1/2016.
 */
public class DialogFragmentRelationship extends DialogFragment implements View.OnClickListener {


    private DialogRelationshipListener relationshipListener;


    public static DialogFragmentRelationship newInstance(Serializable serializable) {
        DialogFragmentRelationship dialogFragmentSensor = new DialogFragmentRelationship();

        Bundle args = new Bundle();
        args.putSerializable(BaseServoLedActivity.BASE_SERVO_LED_ACTIVITY_KEY, serializable);
        dialogFragmentSensor.setArguments(args);

        return dialogFragmentSensor;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstances) {
        relationshipListener = (DialogRelationshipListener) getArguments().getSerializable(BaseServoLedActivity.BASE_SERVO_LED_ACTIVITY_KEY);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_relationships, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setMessage(getString(R.string.choose_relationship)).setView(view);

        // bind click listeners
        view.findViewById(R.id.image_proportional).setOnClickListener(this);
        view.findViewById(R.id.image_frequency).setOnClickListener(this);
        view.findViewById(R.id.image_amplitude).setOnClickListener(this);
        view.findViewById(R.id.image_cumulative).setOnClickListener(this);
        view.findViewById(R.id.image_change).setOnClickListener(this);

        return builder.create();
    }


    @Override
    public void onClick(View view) {
        Relationship relationship = new NoRelationship();
        switch (view.getId()) {
            case R.id.image_proportional:
                Log.d(Constants.LOG_TAG, "onClickLightSensor");
                relationship = new Proportional();
                break;
            case R.id.image_frequency:
                Log.d(Constants.LOG_TAG, "onClickSoilMoistureSensor");
                relationship = new Frequency();
                break;
            case R.id.image_amplitude:
                Log.d(Constants.LOG_TAG, "onClickDistanceSensor");
                relationship = new Amplitutude();
                break;
            case R.id.image_cumulative:
                Log.d(Constants.LOG_TAG, "onClickSoundSensor");
                relationship = new Cumulative();
                break;
            case R.id.image_change:
                Log.d(Constants.LOG_TAG, "onClickWindSpeedSensor");
                relationship = new Change();
                break;
        }
        relationshipListener.onRelationshipChosen(relationship);
        this.dismiss();
    }


    // interface for an activity to listen for a choice
    public interface DialogRelationshipListener {
        public void onRelationshipChosen(Relationship relationship);
    }

}
