package org.cmucreatelab.flutter_android.ui.dialogs.wizards;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ViewGroup;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard.OutputWizard;

import butterknife.OnClick;

/**
 * Created by Mohit on 6/27/18.
 *
 * BaseResizableDialogWizard
 *
 * A base class for the wizards that holds the current OutputWizard and resizes the dialog to a bigger size.
 */
public class BaseResizableDialogWizard extends BaseResizableDialog {

    public OutputWizard wizard;
    public static String KEY_WIZARD = "key_wizard";


    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(convertDpToPx(450), ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.wizard = (OutputWizard)(getArguments().getSerializable(KEY_WIZARD));
        setCancelable(false);
        return super.onCreateDialog(savedInstanceState);
    }

    @OnClick(R.id.image_advanced_settings)
    public void onClickAdvancedSettings() {
        Log.i(Constants.LOG_TAG, "onClickAdvancedSettings");
        wizard.finish();
    }

    @OnClick(R.id.button_close)
    public void onClickClose() {
        flutterAudioPlayer.stop();
        wizard.changeDialog(null);
    }
}
