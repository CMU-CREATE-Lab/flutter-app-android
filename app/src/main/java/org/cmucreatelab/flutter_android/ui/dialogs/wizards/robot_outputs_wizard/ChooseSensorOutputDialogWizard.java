package org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.outputs.Speaker;
import org.cmucreatelab.flutter_android.classes.outputs.TriColorLed;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.BaseResizableDialogWizard;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mike on 6/27/18.
 */

public class ChooseSensorOutputDialogWizard extends BaseResizableDialogWizard {


    public static ChooseSensorOutputDialogWizard newInstance(ServoWizard wizard, Servo servo, TriColorLed led, Speaker speaker, Serializable serializable) {
        Bundle args = new Bundle();
        ChooseSensorOutputDialogWizard dialogWizard = new ChooseSensorOutputDialogWizard();
        args.putSerializable(BaseResizableDialogWizard.KEY_WIZARD, wizard);
        dialogWizard.setArguments(args);

        return dialogWizard;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_sensor_choice_wizard, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ButterKnife.bind(this, view);

        return builder.create();
    }


    @OnClick(R.id.linear_sensor_1)
    public void onClickSensor1() {
        // TODO @tasota actions
        Log.v(Constants.LOG_TAG, "ChooseSensorOutputDialogWizard.onClickSensor1");
    }

    @OnClick(R.id.button_next_page)
    public void onClickSave() {
        Log.v(Constants.LOG_TAG, "ChooseSensorOutputDialogWizard.onClickSave");
        Bundle args = new Bundle();
        args.putInt("page",1);
        changeDialog(args);
    }

}
