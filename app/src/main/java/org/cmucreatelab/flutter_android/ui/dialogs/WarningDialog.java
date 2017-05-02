package org.cmucreatelab.flutter_android.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.io.Serializable;

/**
 * Created by Steve on 3/13/2017.
 */

public class WarningDialog extends BaseResizableDialog {

    private static final String TITLE_KEY = "title_key";
    private static final String DETAILS_KEY = "details_key";
    private static final String POSITIVE_ID_KEY = "positive_id_key";
    private static final String NEGATIVE_ID_KEY = "negative_id_key";
    private static final String DISMISS_KEY = "dismiss_key";

    private DismissAndCancelWarningListener dismissAndCancelWarningListener;
    private String title;
    private String details;
    private int positiveButtonDrawableId;
    private int negativeButtonDrawableId;


    // On Button click events


    private View.OnClickListener positiveButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (dismissAndCancelWarningListener != null) {
                WarningDialog.super.dismiss();
                dismissAndCancelWarningListener.onPositiveButton();
            }
        }
    };


    private View.OnClickListener negativeButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dismiss();
        }
    };


    /**
     * Creates a warning dialog to the prompt the user of something.
     * @param title The title of the dialog.
     * @param details The details of the warning.
     * @param positiveButtonDrawableId The drawableId of the positive button.
     * @return The warning dialog instance.
     */
    public static WarningDialog newInstance(String title, String details, int positiveButtonDrawableId) {
        WarningDialog warningDialog = new WarningDialog();
        Bundle args = new Bundle();
        args.putString(TITLE_KEY, title);
        args.putString(DETAILS_KEY, details);
        args.putInt(POSITIVE_ID_KEY, positiveButtonDrawableId);
        warningDialog.setArguments(args);
        return warningDialog;
    }


    /**
     * Creates a warning dialog to the prompt the user of something.
     * @param title The title of the dialog.
     * @param details The details of the warning.
     * @param positiveButtonDrawableId The drawableId of the positive button.
     * @param negativeButtonDrawableId The drawableId of the negative button.
     * @param dismissAndCancelWarningListener The listener for what to do when this dialog is dismissed or canceled.
     * @return The warning dialog instance.
     */
    public static WarningDialog newInstance(String title, String details, int positiveButtonDrawableId, int negativeButtonDrawableId, Serializable dismissAndCancelWarningListener) {
        Log.d(Constants.LOG_TAG, "making the instance...");
        WarningDialog warningDialog = new WarningDialog();
        Bundle args = new Bundle();
        args.putString(TITLE_KEY, title);
        args.putString(DETAILS_KEY, details);
        args.putInt(POSITIVE_ID_KEY, positiveButtonDrawableId);
        args.putInt(NEGATIVE_ID_KEY, negativeButtonDrawableId);
        args.putSerializable(DISMISS_KEY, dismissAndCancelWarningListener);
        warningDialog.setArguments(args);
        return warningDialog;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "creating the dialog");
        super.onCreateDialog(savedInstanceState);
        title = getArguments().getString(TITLE_KEY);
        details = getArguments().getString(DETAILS_KEY);
        dismissAndCancelWarningListener = (DismissAndCancelWarningListener) getArguments().getSerializable(DISMISS_KEY);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_send_data_failed, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);

        ((TextView) view.findViewById(R.id.text_title)).setText(title);
        ((TextView) view.findViewById(R.id.text_details)).setText(details);
        Button buttonOk1 = (Button) view.findViewById(R.id.button_ok_1);
        Button buttonOk2 = (Button) view.findViewById(R.id.button_ok_2);
        Button negativeButton = (Button) view.findViewById(R.id.button_cancel);
        buttonOk1.setOnClickListener(positiveButtonClick);
        buttonOk2.setOnClickListener(positiveButtonClick);
        negativeButton.setOnClickListener(negativeButtonClick);


        positiveButtonDrawableId = getArguments().getInt(POSITIVE_ID_KEY);
        if (getArguments().getInt(NEGATIVE_ID_KEY) != 0) {
            negativeButtonDrawableId = getArguments().getInt(NEGATIVE_ID_KEY);
            buttonOk1.setVisibility(View.GONE);
            buttonOk2.setBackground(ContextCompat.getDrawable(getActivity(), positiveButtonDrawableId));
            negativeButton.setBackground(ContextCompat.getDrawable(getActivity(), negativeButtonDrawableId));
        } else {
            buttonOk1.setBackground(ContextCompat.getDrawable(getActivity(), positiveButtonDrawableId));
            buttonOk1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            view.findViewById(R.id.linear_button_container).setVisibility(View.GONE);
        }

        return builder.create();
    }


    /**
     * This defines what should happen on dismiss and in onCancel.
     * This listener is not required.
     */
    public interface DismissAndCancelWarningListener extends Serializable {
        public void onPositiveButton();
    }
}
