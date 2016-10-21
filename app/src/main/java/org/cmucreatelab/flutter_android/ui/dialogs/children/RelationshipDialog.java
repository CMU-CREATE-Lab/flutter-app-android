package org.cmucreatelab.flutter_android.ui.dialogs.children;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseServoLedActivity;
import org.cmucreatelab.flutter_android.classes.relationships.Amplitutude;
import org.cmucreatelab.flutter_android.classes.relationships.Change;
import org.cmucreatelab.flutter_android.classes.relationships.Constant;
import org.cmucreatelab.flutter_android.classes.relationships.Cumulative;
import org.cmucreatelab.flutter_android.classes.relationships.Frequency;
import org.cmucreatelab.flutter_android.classes.relationships.NoRelationship;
import org.cmucreatelab.flutter_android.classes.relationships.Proportional;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.relationships.Switch;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.io.Serializable;

/**
 * Created by Steve on 9/1/2016.
 */
// TODO - refactor the onClickListeners to look like the ServoDialog
// TODO - limit the dimensions so when you choose different images the dimensions remain constant
public class RelationshipDialog extends DialogFragment implements View.OnClickListener {


    private DialogRelationshipListener relationshipListener;


    public static RelationshipDialog newInstance(Serializable serializable) {
        RelationshipDialog relationshipDialog = new RelationshipDialog();

        Bundle args = new Bundle();
        args.putSerializable(BaseServoLedActivity.BASE_SERVO_LED_ACTIVITY_KEY, serializable);
        relationshipDialog.setArguments(args);

        return relationshipDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstances) {
        relationshipListener = (DialogRelationshipListener) getArguments().getSerializable(BaseServoLedActivity.BASE_SERVO_LED_ACTIVITY_KEY);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_relationships, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setTitle(getString(R.string.choose_relationship)).setView(view);

        // bind click listeners
        view.findViewById(R.id.linear_proportional).setOnClickListener(this);
        view.findViewById(R.id.linear_frequency).setOnClickListener(this);
        view.findViewById(R.id.linear_amplitude).setOnClickListener(this);
        view.findViewById(R.id.linear_cumulative).setOnClickListener(this);
        view.findViewById(R.id.linear_change).setOnClickListener(this);
        view.findViewById(R.id.linear_switch).setOnClickListener(this);
        view.findViewById(R.id.linear_constant).setOnClickListener(this);

        return builder.create();
    }


    @Override
    public void onClick(View view) {
        Relationship relationship = new NoRelationship();
        switch (view.getId()) {
            case R.id.linear_proportional:
                Log.d(Constants.LOG_TAG, "onClickProportional");
                relationship = new Proportional();
                break;
            case R.id.linear_frequency:
                Log.d(Constants.LOG_TAG, "onClickFrequency");
                relationship = new Frequency();
                break;
            case R.id.linear_amplitude:
                Log.d(Constants.LOG_TAG, "onClickAmplitude");
                relationship = new Amplitutude();
                break;
            case R.id.linear_cumulative:
                Log.d(Constants.LOG_TAG, "onClickImageCumulative");
                relationship = new Cumulative();
                break;
            case R.id.linear_change:
                Log.d(Constants.LOG_TAG, "onClickChange");
                relationship = new Change();
                break;
            case R.id.linear_switch:
                Log.d(Constants.LOG_TAG, "onClickSwitch");
                relationship = new Switch();
                break;
            case R.id.linear_constant:
                Log.d(Constants.LOG_TAG, "onClickConstant");
                relationship = new Constant();
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
