package org.cmucreatelab.flutter_android.ui.dialogs.children;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialog;

/**
 * Created by Steve on 10/21/2016.
 *
 * ChoosePositionDialog
 *
 * An abstract dialog that Max and Min Position Dialog extends off of since they are similar.
 */
public abstract class ChoosePositionDialog extends BaseResizableDialog implements DialogInterface.OnClickListener {


    private ImageView pointer;
    private TextView curentPosition;
    private SeekBar seekBarMaxMin;

    protected int finalPosition;

    private void updatePointer() {
        Matrix matrix = new Matrix();
        pointer.setScaleType(ImageView.ScaleType.MATRIX);   //required
        matrix.postRotate((float) finalPosition, pointer.getWidth(), pointer.getHeight()/2);
        pointer.setImageMatrix(matrix);
    }


    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            Log.d(Constants.LOG_TAG, "onProgressChanged");
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
        builder.setPositiveButton(R.string.save, this);
        builder.setView(view);

        pointer = (ImageView) view.findViewById(R.id.image_servo_pointer);
        curentPosition = (TextView) view.findViewById(R.id.text_current_angle);
        seekBarMaxMin = (SeekBar) view.findViewById(R.id.seek_position);
        seekBarMaxMin.setOnSeekBarChangeListener(seekBarChangeListener);
        curentPosition.setText("0" + (char) 0x00B0);

        return builder.create();
    }

}
