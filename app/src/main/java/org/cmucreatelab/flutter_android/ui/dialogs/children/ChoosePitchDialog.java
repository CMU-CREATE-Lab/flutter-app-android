package org.cmucreatelab.flutter_android.ui.dialogs.children;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialog;

/**
 * Created by Steve on 11/14/2016.
 */
public abstract class ChoosePitchDialog extends BaseResizableDialog implements DialogInterface.OnClickListener{


    private TextView currentPitch;
    private SeekBar seekBarPitch;

    protected int finalPitch;


    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            Log.d(Constants.LOG_TAG, "onProgressChanged");
            finalPitch = i;
            currentPitch.setText(String.valueOf(finalPitch));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_pitch, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setPositiveButton(R.string.save, this);
        builder.setView(view);

        currentPitch = (TextView) view.findViewById(R.id.text_current_pitch);
        seekBarPitch = (SeekBar) view.findViewById(R.id.seek_pitch);
        seekBarPitch.setOnSeekBarChangeListener(seekBarChangeListener);
        currentPitch.setText("0");

        return builder.create();
    }

}
