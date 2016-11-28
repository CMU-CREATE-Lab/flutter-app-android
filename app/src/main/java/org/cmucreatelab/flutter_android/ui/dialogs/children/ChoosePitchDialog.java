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


    private static int MINIMUM_PITCH = 260;

    private TextView currentPitch;
    private SeekBar seekBarPitch;

    protected int finalPitch;


    private int getFrequency(int progress) {
        int result = 0;

        switch (progress) {
            case 0:
                result = Constants.C_4;
                break;
            case 1:
                result = Constants.D_4;
                break;
            case 2:
                result = Constants.E_4;
                break;
            case 3:
                result = Constants.F_4;
                break;
            case 4:
                result = Constants.G_4;
                break;
            case 5:
                result = Constants.A_4;
                break;
            case 6:
                result = Constants.B_4;
                break;
            case 7:
                result = Constants.C_5;
                break;
            case 8:
                result = Constants.D_5;
                break;
            case 9:
                result = Constants.E_5;
                break;
            case 10:
                result = Constants.F_5;
                break;
            case 11:
                result = Constants.G_5;
                break;
            case 12:
                result = Constants.A_5;
                break;
            case 13:
                result = Constants.B_5;
                break;
            case 14:
                result = Constants.C_6;
                break;
        }

        return result;
    }


    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            Log.d(Constants.LOG_TAG, "onProgressChanged");
            finalPitch = i + MINIMUM_PITCH;
            currentPitch.setText(String.valueOf(finalPitch) + " " + getString(R.string.hz));
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

        finalPitch = MINIMUM_PITCH;
        currentPitch = (TextView) view.findViewById(R.id.text_current_pitch);
        seekBarPitch = (SeekBar) view.findViewById(R.id.seek_pitch);
        seekBarPitch.setOnSeekBarChangeListener(seekBarChangeListener);
        currentPitch.setText(String.valueOf(MINIMUM_PITCH) + " " + getString(R.string.hz));

        return builder.create();
    }

}
