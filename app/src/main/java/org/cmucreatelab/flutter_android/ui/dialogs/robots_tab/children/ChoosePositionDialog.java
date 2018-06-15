package org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.children;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Steve on 10/21/2016.
 *
 * ChoosePositionDialog
 *
 * An abstract dialog that Max and Min Position Dialog extends off of since they are similar.
 */
public abstract class ChoosePositionDialog extends BaseResizableDialog {

    public static String POSITION_LISTENER_KEY = "position_listener";
    public static String POSITION_KEY = "position";
    private ImageView pointer;
    private TextView curentPosition;
    private SeekBar seekBarMaxMin;

    protected SetPositionListener setPositionListener;
    protected int finalPosition;

    private void updatePointer() {
        RotateAnimation rotateAnimation = new RotateAnimation(finalPosition-1, finalPosition, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setFillEnabled(true);
        rotateAnimation.setFillAfter(true);
        pointer.startAnimation(rotateAnimation);
    }


    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            Log.v(Constants.LOG_TAG, "onProgressChanged");
            finalPosition = i;
            curentPosition.setText(String.valueOf(finalPosition) + (char) 0x00B0);
            updatePointer();
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
        final View view = inflater.inflate(R.layout.dialog_position, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ButterKnife.bind(this, view);
        pointer = (ImageView) view.findViewById(R.id.image_servo_pointer);
        curentPosition = (TextView) view.findViewById(R.id.text_current_angle);
        seekBarMaxMin = (SeekBar) view.findViewById(R.id.seek_position);
        seekBarMaxMin.setOnSeekBarChangeListener(seekBarChangeListener);

        finalPosition = (Integer) getArguments().getSerializable(POSITION_KEY);
        seekBarMaxMin.setProgress(finalPosition);
        curentPosition.setText(String.valueOf(finalPosition) + (char) 0x00B0);
        updatePointer();

        return builder.create();
    }


    @OnClick(R.id.button_set_position)
    public void onClickSetPosition() {
        setPositionListener.onSetPosition();
    }


    public interface SetPositionListener {
        public void onSetPosition();
    }

}
