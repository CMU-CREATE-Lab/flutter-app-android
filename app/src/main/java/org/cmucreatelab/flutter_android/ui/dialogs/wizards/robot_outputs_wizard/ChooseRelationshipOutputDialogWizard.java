package org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.BaseResizableDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.servo.ChoosePositionServoDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.servo.ServoWizard;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mike on 6/27/18.
 */

public class ChooseRelationshipOutputDialogWizard extends BaseResizableDialogWizard {

    private View dialogView;


    public static ChooseRelationshipOutputDialogWizard newInstance(OutputWizard wizard) {
        Bundle args = new Bundle();
        ChooseRelationshipOutputDialogWizard dialogWizard = new ChooseRelationshipOutputDialogWizard();
        args.putSerializable(BaseResizableDialogWizard.KEY_WIZARD, wizard);
        dialogWizard.setArguments(args);

        return dialogWizard;
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


    private Relationship getRelationshipFromId(int id) {
        switch(id) {
            case R.id.linear_proportional:
                Log.w(Constants.LOG_TAG, "proportional");
                return Proportional.getInstance();
            case R.id.linear_cumulative:
                return Cumulative.getInstance();
            case R.id.linear_change:
                return Change.getInstance();
            case R.id.linear_frequency:
                return Frequency.getInstance();
            case R.id.linear_amplitude:
                return Amplitude.getInstance();
            case R.id.linear_constant:
                return Constant.getInstance();
            case R.id.linear_switch:
                return Switch.getInstance();
            default:
                Log.w(Constants.LOG_TAG, "found no relationship from getRelationshipFromId");
        }
        return NoRelationship.getInstance();
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


    @OnClick({ R.id.linear_proportional, R.id.linear_cumulative, R.id.linear_change,
            R.id.linear_frequency, R.id.linear_amplitude, R.id.linear_constant,
            R.id.linear_switch })
    public void onClickRelationship(View view) {
        ServoWizard.State wizardState = wizard.getCurrentState();
        Log.v(Constants.LOG_TAG, "ChooseRelationshipOutputDialogWizard.onClickRelationship");
        selectedView(view);
        wizardState.relationshipType = getRelationshipFromId(view.getId());
    }


    @OnClick(R.id.button_remove_link)
    public void onClickBack() {
        ServoWizard.State wizardState = wizard.getCurrentState();
        wizard.changeDialog(null);
    }


    @OnClick(R.id.button_save_link)
    public void onClickSave() {
        ServoWizard.State wizardState = wizard.getCurrentState();
        Log.v(Constants.LOG_TAG, "ChooseRelationshipOutputDialogWizard.onClickSave");
        if (wizardState.relationshipType == Constant.getInstance()) {
            wizard.changeDialog(ChoosePositionServoDialogWizard.newInstance(wizard, ChoosePositionServoDialogWizard.OUTPUT_TYPE.MAX));
        } else {
            wizard.changeDialog(ChooseSensorOutputDialogWizard.newInstance(wizard));
        }
    }


    @OnClick(R.id.button_close)
    public void onClickClose() {
        Bundle args = new Bundle();
        wizard.changeDialog(null);
    }

}
