package org.cmucreatelab.flutter_android.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;

/**
 * Created by Steve on 3/13/2017.
 */

public class WarningDialog extends BaseResizableDialog {

    private static final String TITLE_KEY = "title_key";
    private static final String DETAILS_KEY = "details_key";
    private static final String DRAWABLE_ID_KEY = "drawable_id_key";

    private String title;
    private String details;
    private int positiveButtonDrawableId;


    public static WarningDialog newInstance(String title, String details, int positiveButtonDrawableId) {
        WarningDialog warningDialog = new WarningDialog();
        Bundle args = new Bundle();
        args.putString(TITLE_KEY, title);
        args.putString(DETAILS_KEY, details);
        args.putInt(DRAWABLE_ID_KEY, positiveButtonDrawableId);
        warningDialog.setArguments(args);
        return warningDialog;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        title = getArguments().getString(TITLE_KEY);
        details = getArguments().getString(DETAILS_KEY);
        positiveButtonDrawableId = getArguments().getInt(DRAWABLE_ID_KEY);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_send_data_failed, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);

        ((TextView) view.findViewById(R.id.text_title)).setText(title);
        ((TextView) view.findViewById(R.id.text_details)).setText(details);
        Button buttonOk = (Button) view.findViewById(R.id.button_ok);
        buttonOk.setBackground(ContextCompat.getDrawable(getActivity(), positiveButtonDrawableId));
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return builder.create();
    }
}
