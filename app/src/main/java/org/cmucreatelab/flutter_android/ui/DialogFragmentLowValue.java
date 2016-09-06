package org.cmucreatelab.flutter_android.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

/**
 * Created by Steve on 9/2/2016.
 */
public class DialogFragmentLowValue extends DialogFragment implements View.OnClickListener {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_low_value, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setMessage(getString(R.string.choose_position)).setView(view);

        // bind click listeners

        return builder.create();
    }


    @Override
    public void onClick(View view) {
        Log.d(Constants.LOG_TAG, view.toString());
    }


    // interface for an activity to listen for a choice
    public interface DialogLowValueListener {
        public void onLowValueChosen(int lowValue);
    }

}
