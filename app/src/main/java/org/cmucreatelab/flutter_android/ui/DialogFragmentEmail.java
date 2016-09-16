package org.cmucreatelab.flutter_android.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

/**
 * Created by Steve on 9/7/2016.
 */
public class DialogFragmentEmail extends DialogFragment implements DialogInterface.OnClickListener {

    private GlobalHandler globalHandler;
    private EditText email;
    private EditText message;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        globalHandler = GlobalHandler.newInstance(this.getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_email, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setMessage(getString(R.string.send_data_log)).setView(view);
        builder.setPositiveButton(R.string.send, this);
        email = (EditText) view.findViewById(R.id.edit_text_email);
        message = (EditText) view.findViewById(R.id.edit_text_message);
        return builder.create();
    }


    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        Log.d(Constants.LOG_TAG, "onClickSend");
        globalHandler.emailHandler.sendEmail(this.getActivity(), email.getText().toString(), message.getText().toString());
    }

}
