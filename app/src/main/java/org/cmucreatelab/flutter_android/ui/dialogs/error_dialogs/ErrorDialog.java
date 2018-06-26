package org.cmucreatelab.flutter_android.ui.dialogs.error_dialogs;

import android.app.Dialog;
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
import org.cmucreatelab.flutter_android.ui.dialogs.InformationDialog;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mohit on 6/12/2018.
 * <p>
 * ErrorNotificationDialog
 * <p>
 * A Dialog that shows a small pop up when there is something wrong with how the device is set up.
 * Supports all error messages. Just change imageView and TextView based on the error.
 */

public abstract class ErrorDialog extends BaseResizableDialog {
    protected static String ERROR_TITLE_KEY = "error_title";
    protected static String ERROR_TEXT_KEY = "error_text";
    protected static String ERROR_IMAGE_KEY = "error_image";

    private String errorTitle, errorText;
    private int errorImage;

    @Override
    public Dialog onCreateDialog(Bundle savedInstances) {
        errorTitle = getString(getArguments().getInt(ERROR_TITLE_KEY));
        errorText = getString(getArguments().getInt(ERROR_TEXT_KEY));
        errorImage = getArguments().getInt(ERROR_IMAGE_KEY);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_error_flutter, null);

        ((TextView) view.findViewById(R.id.error_title)).setText(errorTitle);
        ((TextView) view.findViewById(R.id.text_error_description)).setText(errorText);
        ((ImageView) view.findViewById(R.id.error_image)).setImageResource(errorImage);

        setCancelable(false);

        view.findViewById(R.id.horizontal_two_button_container).setVisibility(View.GONE);

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ButterKnife.bind(this, view);
        return builder.create();
    }

    @OnClick(R.id.button_ok_one)
    public abstract void onClickToDismiss();
}

