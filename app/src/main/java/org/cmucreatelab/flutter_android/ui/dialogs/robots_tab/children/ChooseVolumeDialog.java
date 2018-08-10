package org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.children;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Steve on 11/14/2016.
 *
 * ChooseVolumeDialog
 *
 * An abstract Dialog that handles the volume choosing.
 */
public abstract class ChooseVolumeDialog extends BaseResizableDialog {

    public static String VOLUME_LISTENER_KEY = "volume_listener";
    public static String VOLUME_KEY = "volume";
    private TextView currentVolume;
    private SeekBar seekBarVolume;

    protected int finalVolume;
    protected SetVolumeListener setVolumeListener;


    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            Log.v(Constants.LOG_TAG, "onProgressChanged");
            finalVolume = i;
            currentVolume.setText(String.valueOf(finalVolume));
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
        final View view = inflater.inflate(R.layout.dialog_choose_volume_wizard, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ButterKnife.bind(this, view);

        currentVolume = (TextView) view.findViewById(R.id.text_current_volume);
        seekBarVolume = (SeekBar) view.findViewById(R.id.seek_volume);
        seekBarVolume.getProgressDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
        seekBarVolume.setOnSeekBarChangeListener(seekBarChangeListener);
        finalVolume = (Integer) getArguments().getSerializable(VOLUME_KEY);
        seekBarVolume.setProgress(finalVolume);
        currentVolume.setText(String.valueOf(finalVolume));

        ((TextView) view.findViewById(R.id.text_output_title)).setText("Set the Volume");

        view.findViewById(R.id.image_advanced_settings).setVisibility(View.GONE);
        view.findViewById(R.id.link_buttons_wizard).setVisibility(View.GONE);
        view.findViewById(R.id.text_set_volume).setVisibility(View.GONE);

        flutterAudioPlayer.addAudio(R.raw.audio_16);
        flutterAudioPlayer.playAudio();

        return builder.create();
    }


    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(convertDpToPx(390), ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    @OnClick(R.id.button_set_volume)
    public void onClickSetVolume() {
        setVolumeListener.onSetVolume();
    }


    public interface SetVolumeListener {
        public void onSetVolume();
    }

    @OnClick(R.id.button_close)
    public void onClickClose() {
        dismiss();
    }
}
