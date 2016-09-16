package org.cmucreatelab.flutter_android.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import org.cmucreatelab.flutter_android.R;

/**
 * Created by Steve on 9/7/2016.
 */
public abstract class DialogFragmentChooseColor extends DialogFragment implements View.OnClickListener, DialogInterface.OnClickListener {

    private int h,s,l;
    private FrameLayout frameFinalColor;
    private SeekBar seekBarHue;
    private SeekBar seekBarSaturation;
    private SeekBar seekBarLightness;


    private void updateColor() {
        // TODO - update the color
    }


    protected SeekBar.OnSeekBarChangeListener hueSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            h = i;
            updateColor();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };


    protected SeekBar.OnSeekBarChangeListener saturationSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            s = i;
            updateColor();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };


    protected SeekBar.OnSeekBarChangeListener lightnessSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            l = i;
            updateColor();
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
        final View view = inflater.inflate(R.layout.dialog_choose_color, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setMessage(getString(R.string.choose_color)).setView(view);
        builder.setPositiveButton(R.string.set_color, this);

        frameFinalColor = (FrameLayout) view.findViewById(R.id.frame_final_color);

        /*seekBarHue = (SeekBar) view.findViewById(R.id.seek_hue);
        seekBarSaturation = (SeekBar) view.findViewById(R.id.seek_saturation);
        seekBarLightness = (SeekBar) view.findViewById(R.id.seek_lightness);
        seekBarHue.setOnSeekBarChangeListener(hueSeekBarChangeListener);
        seekBarSaturation.setOnSeekBarChangeListener(saturationSeekBarChangeListener);
        seekBarLightness.setOnSeekBarChangeListener(lightnessSeekBarChangeListener);*/

        h = 0;
        s = 0;
        l = 0;

        updateColor();

        return builder.create();
    }


    @Override
    public void onClick(View view) {
        // TODO -
    }

}
