package org.cmucreatelab.flutter_android.ui.dialogs.children;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import org.cmucreatelab.flutter_android.R;
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
import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialog;

import java.io.Serializable;

/**
 * Created by Steve on 9/1/2016.
 *
 * RelationshipOutputDialog
 *
 * A Dialog that shows which relationship is to be linked between sensor and output.
 */
// TODO - refactor the onClickListeners to look like the ServoDialog
// TODO - limit the dimensions so when you choose different images the dimensions remain constant
public class RelationshipOutputDialog extends BaseResizableDialog implements View.OnClickListener {


    private DialogRelationshipListener relationshipListener;


    public static RelationshipOutputDialog newInstance(Serializable serializable) {
        RelationshipOutputDialog relationshipDialog = new RelationshipOutputDialog();

        Bundle args = new Bundle();
        args.putSerializable(Constants.SerializableKeys.RELATIONSHIP_KEY, serializable);
        relationshipDialog.setArguments(args);

        return relationshipDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstances) {
        relationshipListener = (DialogRelationshipListener) getArguments().getSerializable(Constants.SerializableKeys.RELATIONSHIP_KEY);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_relationships, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);

        // bind click listeners
        view.findViewById(R.id.linear_proportional).setOnClickListener(this);
        view.findViewById(R.id.linear_constant).setOnClickListener(this);
        /*view.findViewById(R.id.linear_proportional).setOnClickListener(this);
        view.findViewById(R.id.linear_frequency).setOnClickListener(this);
        view.findViewById(R.id.linear_amplitude).setOnClickListener(this);
        view.findViewById(R.id.linear_cumulative).setOnClickListener(this);
        view.findViewById(R.id.linear_change).setOnClickListener(this);
        view.findViewById(R.id.linear_switch).setOnClickListener(this);
        view.findViewById(R.id.linear_constant).setOnClickListener(this);*/
        // TODO @tasota hidden for now; implement later
        //view.findViewById(R.id.linear_proportional).setVisibility(View.GONE);
        view.findViewById(R.id.linear_frequency).setVisibility(View.GONE);
        view.findViewById(R.id.linear_amplitude).setVisibility(View.GONE);
        view.findViewById(R.id.linear_cumulative).setVisibility(View.GONE);
        view.findViewById(R.id.linear_change).setVisibility(View.GONE);
        view.findViewById(R.id.linear_switch).setVisibility(View.GONE);
        //view.findViewById(R.id.linear_constant).setVisibility(View.GONE);

        return builder.create();
    }


    @Override
    public void onClick(View view) {
        Relationship relationship = NoRelationship.getInstance();
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
        relationshipListener.onRelationshipChosen(relationship);
        this.dismiss();
    }


    // interface for an activity to listen for a choice
    public interface DialogRelationshipListener {
        public void onRelationshipChosen(Relationship relationship);
    }

}
