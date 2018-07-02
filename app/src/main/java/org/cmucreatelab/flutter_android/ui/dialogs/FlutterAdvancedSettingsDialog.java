package org.cmucreatelab.flutter_android.ui.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.cmucreatelab.android.melodysmart.models.MelodySmartMessage;
import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.Session;
import org.cmucreatelab.flutter_android.classes.outputs.Output;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.FlutterProtocol;
import org.cmucreatelab.flutter_android.helpers.static_classes.MessageConstructor;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by psdev1 on 5/26/2018.
 *
 * FlutterAdvancedSettingsDialog
 *
 * A dialog that shows the advanced settings of a Flutter.
 *
 */
public class FlutterAdvancedSettingsDialog extends BaseResizableDialog {

    private static final String flutterAdvancedSettingsKey = "FLUTTER_ADVANCED_SETTINGS_KEY";


    private static FlutterAdvancedSettingsDialog newInstance(int description) {
        FlutterAdvancedSettingsDialog flutterAdvancedSettingsDialog = new FlutterAdvancedSettingsDialog();

        Bundle args = new Bundle();
        args.putInt(flutterAdvancedSettingsKey, description);
        flutterAdvancedSettingsDialog.setArguments(args);

        return flutterAdvancedSettingsDialog;
    }


    public static void displayDialog(FlutterStatusDialog activity, int description) {
        FlutterAdvancedSettingsDialog flutterAdvancedSettingsDialog = FlutterAdvancedSettingsDialog.newInstance(description);
        flutterAdvancedSettingsDialog.show(activity.getFragmentManager(), "tag");
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_flutter_advanced_settings, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ButterKnife.bind(this, view);

        return builder.create();
    }


    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(convertDpToPx(500), ViewGroup.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }


    @Override
    public void onPause() {
        super.onPause();
        Dialog dialog = getDialog();
        if (dialog != null)
            dialog.dismiss();
    }


    @OnClick(R.id.button_flutter_reset)
    public void onClickReset() {
        Log.d(Constants.LOG_TAG, "onClickFlutterReset");

        InformationDialog firstConfirmationDialog = InformationDialog.newInstance(getString(R.string.flutter_reset_warning_title), getString(R.string.flutter_reset_warning_description), R.drawable.round_reddish_button_bottom_right, R.drawable.button_gray_left_bottom, null, firstConfirmationDialogListener);
        firstConfirmationDialog.show(this.getFragmentManager(), "tag");
    }

    protected InformationDialog.DismissAndCancelWarningListener firstConfirmationDialogListener = new InformationDialog.DismissAndCancelWarningListener() {
        @Override
        public void onPositiveButton() {
            GlobalHandler globalHandler = GlobalHandler.getInstance(getActivity().getApplicationContext());
            Session session = globalHandler.sessionHandler.getSession();
            ArrayList<MelodySmartMessage> messages = new ArrayList<>();

            // construct and send messages (remove relations, unset sensors)
            messages.add(MessageConstructor.constructRemoveAllRelations());
            for (int i = 0; i < session.getFlutter().getSensors().length; i++) {
                messages.add(MessageConstructor.constructSetInputType(session.getFlutter().getSensors()[i],
                        FlutterProtocol.InputTypes.NOT_SET));

                session.getFlutter().getSensors()[i] = FlutterProtocol.sensorFromInputType(i + 1, FlutterProtocol.InputTypes.NOT_SET);
            }
            for (MelodySmartMessage message : messages) {
                globalHandler.melodySmartDeviceHandler.addMessage(message);
            }

            // refresh ui
            for (Output output : session.getFlutter().getOutputs()) {
                output.setIsLinked(false, output);
            }

            Intent intent = new Intent(getActivity(), getActivity().getClass());
            startActivity(intent);
            getActivity().finish();
        }
    };

}
