package org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.led;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.TriColorLed;
import org.cmucreatelab.flutter_android.classes.relationships.Constant;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.children.ChooseColorDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.BaseResizableDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.OutputWizard;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Parv on 6/28/18.
 *
 * ChooseColorLedDialogWizard
 *
 * A class for choosing the led color in the wizard.
 * Does not extend BaseResizableDialogWizard as it needs
 * many of the functions and attributes in ChooseColorDialog.
 */
public class ChooseColorLedDialogWizard extends ChooseColorDialog {

    LedWizard.LedWizardState wizardState;

    private OUTPUT_TYPE outputType = OUTPUT_TYPE.MAX;

    public static final String DIALOG_TYPE = "dialog_type";

    public OutputWizard wizard;
    public static String KEY_WIZARD = "key_wizard";


    public enum OUTPUT_TYPE {
        MIN, MAX;
    }


    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(convertDpToPx(450), ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    public void updateWizardState() {
        wizardState = (LedWizard.LedWizardState) (wizard.getCurrentState());
    }


    public static ChooseColorLedDialogWizard newInstance(OutputWizard wizard, OUTPUT_TYPE type) {
        Bundle args = new Bundle();
        ChooseColorLedDialogWizard dialogWizard = new ChooseColorLedDialogWizard();
        args.putSerializable(BaseResizableDialogWizard.KEY_WIZARD, wizard);
        args.putSerializable(DIALOG_TYPE, type);
        if (type.equals(OUTPUT_TYPE.MIN)) {
            args.putSerializable(SELECTED_COLOR_KEY, TriColorLed.convertRgbToHex(((LedWizard.LedWizardState) wizard.getCurrentState()).outputsMin));
        } else {
            args.putSerializable(SELECTED_COLOR_KEY, TriColorLed.convertRgbToHex(((LedWizard.LedWizardState) wizard.getCurrentState()).outputsMax));
        }
        dialogWizard.setArguments(args);

        return dialogWizard;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        this.wizard = (OutputWizard) (getArguments().getSerializable(KEY_WIZARD));

        this.outputType = (OUTPUT_TYPE) (getArguments().getSerializable(DIALOG_TYPE));

        dialogView.findViewById(R.id.link_buttons_wizard).setVisibility(View.VISIBLE);
        dialogView.findViewById(R.id.button_next).setBackgroundResource(R.drawable.round_green_button_bottom_right);
        dialogView.findViewById(R.id.button_set_color).setVisibility(View.GONE);

        updateWizardState();

        updateTextViews();

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(dialogView);
        ButterKnife.bind(this, dialogView);

        return builder.create();
    }


    private void updateTextViews() {
        // views
        ((TextView) dialogView.findViewById(R.id.text_output_title)).setText("Set Up " + getPositionPrompt() + " Color for LED " + String.valueOf(((TriColorLed) wizard.getOutput()).getPortNumber()));
        ((ImageView) dialogView.findViewById(R.id.text_output_title_icon)).setImageResource(R.drawable.led);
        //((TextView) dialogView.findViewById(R.id.text_set_color)).setText(getPositionPrompt());
    }


    private String getPositionPrompt() {
        if (!(wizardState.relationshipType instanceof Constant)) {
            Sensor[] sensors = GlobalHandler.getInstance(this.getActivity()).sessionHandler.getSession().getFlutter().getSensors();

            switch (outputType) {
                case MIN:
                    return getString(sensors[wizardState.selectedSensorPort - 1].getLowTextId());
                default:
                    return getString(sensors[wizardState.selectedSensorPort - 1].getHighTextId());
            }
        } else {
            return "Constant";
        }
    }


    @OnClick(R.id.button_back)
    public void onClickBack()
    {
        if (this.outputType == OUTPUT_TYPE.MIN)
        {
            wizardState.outputsMin = finalRGB;
            wizard.changeDialog(ChooseSensorLedDialogWizard.newInstance(wizard));
        } else
        {
            wizardState.outputsMax = finalRGB;
            if (wizardState.relationshipType instanceof Constant)
            {
                wizard.changeDialog(ChooseRelationshipLedDialogWizard.newInstance(wizard));
            } else
            {
                wizard.changeDialog(ChooseColorLedDialogWizard.newInstance(wizard, ChooseColorLedDialogWizard.OUTPUT_TYPE.MIN));
            }
        }
    }


    @OnClick(R.id.button_next)
    public void onClickNext() {
        if (this.outputType == OUTPUT_TYPE.MIN) {
            wizardState.outputsMin = finalRGB;
            wizard.changeDialog(ChooseColorLedDialogWizard.newInstance(wizard, ChooseColorLedDialogWizard.OUTPUT_TYPE.MAX));
        } else {
            wizardState.outputsMax = finalRGB;
            wizard.finish();
        }
    }


    @OnClick(R.id.button_close)
    public void onClickClose() {
        wizard.changeDialog(null);
    }
}
