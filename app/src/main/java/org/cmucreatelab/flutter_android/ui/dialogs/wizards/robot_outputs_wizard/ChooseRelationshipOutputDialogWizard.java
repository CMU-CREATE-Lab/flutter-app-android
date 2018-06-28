package org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.outputs.Speaker;
import org.cmucreatelab.flutter_android.classes.outputs.TriColorLed;
import org.cmucreatelab.flutter_android.classes.relationships.Proportional;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.BaseResizableDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.Wizard;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mike on 6/27/18.
 */

public class ChooseRelationshipOutputDialogWizard extends BaseResizableDialogWizard {

    private View dialogView;
    private Relationship.Type selectedRelationshipType = Relationship.Type.NO_RELATIONSHIP;

    public static final String SELECTED_RELATIONSHIP_TYPE = "selected_relationship_type";


    public static ChooseRelationshipOutputDialogWizard newInstance(ServoWizard wizard) {
        Bundle args = new Bundle();
        ChooseRelationshipOutputDialogWizard dialogWizard = new ChooseRelationshipOutputDialogWizard();
//        args.putSerializable(Constants.SerializableKeys.RELATIONSHIP_KEY, serializable);
//        relationshipDialog.setArguments(args);
//        robotAct = serializable;
        args.putSerializable(BaseResizableDialogWizard.KEY_WIZARD, wizard);
        dialogWizard.setArguments(args);

        return dialogWizard;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_wizard, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ButterKnife.bind(this, view);
        this.dialogView = view;

        return builder.create();
    }

    private void clearSelection() {
        int[] viewIds = { R.id.linear_proportional, R.id.linear_cumulative, R.id.linear_change,
                R.id.linear_frequency, R.id.linear_amplitude, R.id.linear_constant,
                R.id.linear_switch
        };
        for (int id: viewIds)
            dialogView.findViewById(id).setBackground(null);
    }

    private void selectedView(View view) {
        clearSelection();
        view.setBackground(ContextCompat.getDrawable(dialogView.getContext(), R.drawable.rectangle_green_border));
    }

    private Relationship.Type getRelationshipFromId(int id) {
        switch(id) {
            case R.id.linear_proportional:
                Log.w(Constants.LOG_TAG, "proportional");
                return Relationship.Type.PROPORTIONAL;
            case R.id.linear_cumulative:
                return Relationship.Type.CUMULATIVE;
            case R.id.linear_change:
                return Relationship.Type.CHANGE;
            case R.id.linear_frequency:
                return Relationship.Type.FREQUENCY;
            case R.id.linear_amplitude:
                return Relationship.Type.AMPLITUDE;
            case R.id.linear_constant:
                return Relationship.Type.CONSTANT;
            case R.id.linear_switch:
                return Relationship.Type.SWITCH;
            default:
                Log.w(Constants.LOG_TAG, "found no relationship from getRelationshipFromId");
        }
        return Relationship.Type.NO_RELATIONSHIP;
    }

    // TODO @tasota actions
    @OnClick({ R.id.linear_proportional, R.id.linear_cumulative, R.id.linear_change,
            R.id.linear_frequency, R.id.linear_amplitude, R.id.linear_constant,
            R.id.linear_switch })
    public void onClickRelationship(View view) {
        Log.v(Constants.LOG_TAG, "ChooseRelationshipOutputDialogWizard.onClickRelationship");
        selectedView(view);
        this.selectedRelationshipType = getRelationshipFromId(view.getId());
    }


    @OnClick(R.id.button_save_link)
    public void onClickSave() {
        Log.v(Constants.LOG_TAG, "ChooseRelationshipOutputDialogWizard.onClickSave");
        Bundle args = new Bundle();
        args.putInt("page",2);
        args.putSerializable(SELECTED_RELATIONSHIP_TYPE, selectedRelationshipType);
        changeDialog(args);
    }

}
