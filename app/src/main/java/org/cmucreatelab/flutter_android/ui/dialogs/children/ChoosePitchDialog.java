package org.cmucreatelab.flutter_android.ui.dialogs.children;

import android.app.Dialog;
import android.content.DialogInterface;
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

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Steve on 11/14/2016.
 */
public abstract class ChoosePitchDialog extends BaseResizableDialog {


    private static int MINIMUM_PITCH = 260;

    private TextView currentNote;
    private TextView currentPitch;
    private ImageView sheetMusic;
    private SeekBar seekBarPitch;

    protected int finalPitch;
    protected SetPitchListener setPitchListener;


    private int getFrequency(int progress) {
        int result = 0;

        switch (progress) {
            case 0:
                currentNote.setText("C");
                sheetMusic.setImageResource(R.drawable.c1);
                result = Constants.MusicNoteFrequencies.C_4;
                break;
            case 1:
                currentNote.setText("D");
                sheetMusic.setImageResource(R.drawable.d1);
                result = Constants.MusicNoteFrequencies.D_4;
                break;
            case 2:
                currentNote.setText("E");
                sheetMusic.setImageResource(R.drawable.e1);
                result = Constants.MusicNoteFrequencies.E_4;
                break;
            case 3:
                currentNote.setText("F");
                sheetMusic.setImageResource(R.drawable.f1);
                result = Constants.MusicNoteFrequencies.F_4;
                break;
            case 4:
                currentNote.setText("G");
                sheetMusic.setImageResource(R.drawable.g1);
                result = Constants.MusicNoteFrequencies.G_4;
                break;
            case 5:
                currentNote.setText("A");
                sheetMusic.setImageResource(R.drawable.a1);
                result = Constants.MusicNoteFrequencies.A_4;
                break;
            case 6:
                currentNote.setText("B");
                sheetMusic.setImageResource(R.drawable.b1);
                result = Constants.MusicNoteFrequencies.B_4;
                break;
            case 7:
                currentNote.setText("C");
                sheetMusic.setImageResource(R.drawable.c2);
                result = Constants.MusicNoteFrequencies.C_5;
                break;
            case 8:
                currentNote.setText("D");
                sheetMusic.setImageResource(R.drawable.d2);
                result = Constants.MusicNoteFrequencies.D_5;
                break;
            case 9:
                currentNote.setText("E");
                sheetMusic.setImageResource(R.drawable.e2);
                result = Constants.MusicNoteFrequencies.E_5;
                break;
            case 10:
                currentNote.setText("F");
                sheetMusic.setImageResource(R.drawable.f2);
                result = Constants.MusicNoteFrequencies.F_5;
                break;
            case 11:
                currentNote.setText("G");
                sheetMusic.setImageResource(R.drawable.g2);
                result = Constants.MusicNoteFrequencies.G_5;
                break;
            case 12:
                currentNote.setText("A");
                sheetMusic.setImageResource(R.drawable.a2);
                result = Constants.MusicNoteFrequencies.A_5;
                break;
            case 13:
                currentNote.setText("B");
                sheetMusic.setImageResource(R.drawable.b2);
                result = Constants.MusicNoteFrequencies.B_5;
                break;
            case 14:
                currentNote.setText("C");
                sheetMusic.setImageResource(R.drawable.c3);
                result = Constants.MusicNoteFrequencies.C_6;
                break;
        }

        return result;
    }


    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            Log.v(Constants.LOG_TAG, "onProgressChanged");
            finalPitch = getFrequency(i);
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
        builder.setView(view);
        ButterKnife.bind(this, view);

        finalPitch = MINIMUM_PITCH;
        currentNote = (TextView) view.findViewById(R.id.text_current_note);
        currentPitch = (TextView) view.findViewById(R.id.text_current_pitch);
        sheetMusic = (ImageView) view.findViewById(R.id.image_sheet_music);
        seekBarPitch = (SeekBar) view.findViewById(R.id.seek_pitch);
        seekBarPitch.setOnSeekBarChangeListener(seekBarChangeListener);
        currentNote.setText("C");
        currentPitch.setText(String.valueOf(MINIMUM_PITCH) + " " + getString(R.string.hz));
        sheetMusic.setImageResource(R.drawable.c1);

        return builder.create();
    }


    @OnClick(R.id.button_set_pitch)
    public void onClickSetPitch() {
        setPitchListener.onSetPitch();
    }


    public interface SetPitchListener {
        public void onSetPitch();
    }

}
