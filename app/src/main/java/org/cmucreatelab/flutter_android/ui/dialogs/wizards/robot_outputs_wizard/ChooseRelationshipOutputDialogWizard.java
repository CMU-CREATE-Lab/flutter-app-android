package org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mike on 6/27/18.
 *
 * ChooseRelationshipOutputDialogWizard
 *
 * This is the base class for choosing a relationship for the wizard.
 * This can be extended by the other outputs for their own specific key words and audio.
 */
public abstract class ChooseRelationshipOutputDialogWizard extends BaseResizableDialogWizard {

    private View dialogView;
    protected Button nextButton;


    protected void clearSelection() {
        int[] viewIds = {R.id.linear_proportional, R.id.linear_cumulative, R.id.linear_change, R.id.linear_frequency, R.id.linear_amplitude, R.id.linear_constant};
        for (int id : viewIds) {
            dialogView.findViewById(id).setBackground(null);
        }
    }


    protected void selectedView(View view) {
        clearSelection();
        view.setBackground(ContextCompat.getDrawable(dialogView.getContext(), R.drawable.rectangle_green_border));
    }


    protected Relationship getRelationshipFromId(int id) {
        switch (id) {
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
            default:
                Log.w(Constants.LOG_TAG, "found no relationship from getRelationshipFromId");
        }
        return NoRelationship.getInstance();
    }


    protected View getViewFromRelationship(Relationship relationship) {
        if (relationship.equals(Proportional.getInstance())) {
            return dialogView.findViewById(R.id.linear_proportional);
        } else if (relationship.equals(Cumulative.getInstance())) {
            return dialogView.findViewById(R.id.linear_cumulative);
        } else if (relationship.equals(Change.getInstance())) {
            return dialogView.findViewById(R.id.linear_change);
        } else if (relationship.equals(Frequency.getInstance())) {
            return dialogView.findViewById(R.id.linear_frequency);
        } else if (relationship.equals(Amplitude.getInstance())) {
            return dialogView.findViewById(R.id.linear_amplitude);
        } else if (relationship.equals(Constant.getInstance())) {
            return dialogView.findViewById(R.id.linear_constant);
        } else if (relationship.equals(Switch.getInstance())) {
            return dialogView.findViewById(R.id.linear_switch);
        }
        return null;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_choose_relationship_wizard, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ButterKnife.bind(this, view);
        this.dialogView = view;
        nextButton = (Button) view.findViewById(R.id.button_next);

        //have to update wizard state before updating others
        updateWizardState();
        updateViewWithOptions();
        updateText(view);

        return builder.create();
    }


    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(convertDpToPx(400), ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    public abstract void updateViewWithOptions();

    public abstract void updateRelationshipType(View view);

    public abstract void updateText(View view);

    public abstract void updateWizardState();


    @OnClick({R.id.linear_proportional, R.id.linear_cumulative, R.id.linear_change, R.id.linear_frequency, R.id.linear_amplitude, R.id.linear_constant, R.id.linear_switch})
    public void onClickRelationship(View view) {
        Log.v(Constants.LOG_TAG, "ChooseRelationshipOutputDialogWizard.onClickRelationship");
        updateRelationshipType(view);
        updateViewWithOptions();
    }


    @OnClick(R.id.button_cancel)
    public void onClickCancel() {
        wizard.changeDialog(null);
    }


    @OnClick(R.id.button_next)
    public abstract void onClickNext();


    @OnClick(R.id.button_close)
    public void onClickClose() {
        wizard.changeDialog(null);
    }

}
