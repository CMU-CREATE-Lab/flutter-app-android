package org.cmucreatelab.flutter_android.ui.dialogs.error_dialogs;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Parv.
 *
 * ErrorConfirmationDialog
 *
 * A dialog for error screens that has a confirmation and cancel button.
 */

public abstract class ErrorConfirmationDialog extends BaseResizableDialog {
    protected static String ERROR_TITLE_KEY = "error_title";
    protected static String ERROR_TEXT_KEY = "error_text";
    protected static String ERROR_IMAGE_KEY = "error_image";
    protected static String BUTTON_TEXT_KEY = "button_text";

    private String errorTitle, errorText, buttonText;
    private int errorImage;


    @Override
    public Dialog onCreateDialog(Bundle savedInstances) {
        errorTitle = getString(getArguments().getInt(ERROR_TITLE_KEY));
        errorText = getString(getArguments().getInt(ERROR_TEXT_KEY));
        errorImage = getArguments().getInt(ERROR_IMAGE_KEY);
        try {
            buttonText = getString(getArguments().getInt(BUTTON_TEXT_KEY));
        }
        catch (Resources.NotFoundException e) {
            buttonText = null;
        }

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_error_flutter, null);

        ((TextView) view.findViewById(R.id.error_title)).setText(errorTitle);
        ((TextView) view.findViewById(R.id.text_error_description)).setText(errorText);
        ((ImageView) view.findViewById(R.id.error_image)).setImageResource(errorImage);

        setCancelable(false);

        Button buttonOkOne = (Button) view.findViewById(R.id.button_ok_one);
        buttonOkOne.setVisibility(View.GONE);

        Button buttonOkTwo = (Button) view.findViewById(R.id.button_ok_two);
        if (buttonText != null) {
            buttonOkTwo.setText(buttonText);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ButterKnife.bind(this, view);
        return builder.create();
    }


    @OnClick(R.id.button_ok_two)
    public void onClickConfirm() {
        flutterAudioPlayer.stop();
    }


    @OnClick(R.id.button_cancel)
    public void onClickCancel() {
        flutterAudioPlayer.stop();
    }
}

