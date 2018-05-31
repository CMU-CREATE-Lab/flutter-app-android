package org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.children;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.relationships.Amplitude;
import org.cmucreatelab.flutter_android.classes.relationships.Change;
import org.cmucreatelab.flutter_android.classes.relationships.Constant;
import org.cmucreatelab.flutter_android.classes.relationships.Cumulative;
import org.cmucreatelab.flutter_android.classes.relationships.Frequency;
import org.cmucreatelab.flutter_android.classes.relationships.NoRelationship;
import org.cmucreatelab.flutter_android.classes.relationships.Proportional;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.relationships.Switch;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialogWizard;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.OnClick;

/* Created by Mohit. This class greatly resembles RelationshipOutputDialog.java, but is used to
   be a starting line for setting up the servos. This will only apply if no linked sensor is set
   up.
 */

public class RelationshipWizardPageOne extends BaseResizableDialogWizard implements View.OnClickListener {
    private Relationship relationship;

    private RelationshipWizardPageOne.DialogRelationshipListener relationshipListener;


    public static RelationshipWizardPageOne newInstance(Servo servo, Serializable serializable) {
        RelationshipWizardPageOne relationshipDialog = new RelationshipWizardPageOne();

        Bundle args = new Bundle();
        args.putSerializable(Servo.SERVO_KEY, servo);
        args.putSerializable(Constants.SerializableKeys.RELATIONSHIP_KEY, serializable);
        relationshipDialog.setArguments(args);

        return relationshipDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstances) {
        relationshipListener = (RelationshipWizardPageOne.DialogRelationshipListener) getArguments().getSerializable(Constants.SerializableKeys.RELATIONSHIP_KEY);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_wizard, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ButterKnife.bind(this, view);

        // bind click listeners
        view.findViewById(R.id.linear_proportional).setOnClickListener(this);
        view.findViewById(R.id.linear_constant).setOnClickListener(this);
        view.findViewById(R.id.linear_amplitude).setOnClickListener(this);
        view.findViewById(R.id.linear_frequency).setOnClickListener(this);
        view.findViewById(R.id.linear_cumulative).setOnClickListener(this);
        view.findViewById(R.id.linear_change).setOnClickListener(this);
        /*view.findViewById(R.id.linear_proportional).setOnClickListener(this);
        view.findViewById(R.id.linear_frequency).setOnClickListener(this);
        view.findViewById(R.id.linear_amplitude).setOnClickListener(this);
        view.findViewById(R.id.linear_cumulative).setOnClickListener(this);
        view.findViewById(R.id.linear_change).setOnClickListener(this);
        view.findViewById(R.id.linear_switch).setOnClickListener(this);
        view.findViewById(R.id.linear_constant).setOnClickListener(this);*/
        // TODO @tasota hidden for now; implement later
        //view.findViewById(R.id.linear_proportional).setVisibility(View.GONE);
        //view.findViewById(R.id.linear_frequency).setVisibility(View.GONE);
        //view.findViewById(R.id.linear_amplitude).setVisibility(View.GONE);
        //view.findViewById(R.id.linear_cumulative).setVisibility(View.GONE);
        //view.findViewById(R.id.linear_change).setVisibility(View.GONE);
        //view.findViewById(R.id.linear_switch).setVisibility(View.GONE); // Please uncomment this line.
        //view.findViewById(R.id.linear_constant).setVisibility(View.GONE);

        return builder.create();
    }


    @Override
    public void onClick(View view) {
        relationship = NoRelationship.getInstance();
        switch (view.getId()) {
            case R.id.linear_proportional:
                Log.d(Constants.LOG_TAG, "onClickProportional");
                relationship = Proportional.getInstance();
                break;
            case R.id.linear_frequency:
                Log.d(Constants.LOG_TAG, "onClickFrequency");
                relationship = Frequency.getInstance();
                break;
            case R.id.linear_amplitude:
                Log.d(Constants.LOG_TAG, "onClickAmplitude");
                relationship = Amplitude.getInstance();
                break;
            case R.id.linear_cumulative:
                Log.d(Constants.LOG_TAG, "onClickImageCumulative");
                relationship = Cumulative.getInstance();
                break;
            case R.id.linear_change:
                Log.d(Constants.LOG_TAG, "onClickChange");
                relationship = Change.getInstance();
                break;
            case R.id.linear_switch:
                Log.d(Constants.LOG_TAG, "onClickSwitch");
                relationship = Switch.getInstance();
                break;
            case R.id.linear_constant:
                Log.d(Constants.LOG_TAG, "onClickConstant");
                relationship = Constant.getInstance();
                break;
        }

    }

    @OnClick(R.id.button_next_page)
    public void onClickSetRelationship() {
        relationshipListener.onRelationshipChosen(relationship);
        // send an intent to the sensor dialog
        // if relationship = constant, then skip sensor dialog
        if (relationship == Constant.getInstance()) {
            // send an intent to the wet position
        }
        else {
            // send an intent to the sensor dialog
        }
    }


    // interface for an activity to listen for a choice
    public interface DialogRelationshipListener {
        public void onRelationshipChosen(Relationship relationship);
    }
}
