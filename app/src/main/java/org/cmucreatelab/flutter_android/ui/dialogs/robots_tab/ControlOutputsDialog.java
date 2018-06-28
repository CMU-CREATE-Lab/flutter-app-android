package org.cmucreatelab.flutter_android.ui.dialogs.robots_tab;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.cmucreatelab.android.melodysmart.models.MelodySmartMessage;
import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.flutters.Flutter;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.outputs.Speaker;
import org.cmucreatelab.flutter_android.classes.outputs.TriColorLed;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.MessageConstructor;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.children.LedConstantColorDialog;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Parv on 6/12/2018.
 */

public class ControlOutputsDialog extends DialogFragment implements Serializable,
        LedConstantColorDialog.DialogColorConstantListener {

    private GlobalHandler globalHandler;

    private Servo[] servos;
    private SeekBar seekBarServo1, seekBarServo2, seekBarServo3;
    private TextView textViewServo1Value, textViewServo2Value, textViewServo3Value;
    private boolean isServo1Changed = false, isServo2Changed = false, isServo3Changed = false;

    private TriColorLed[] leds;
    private ImageView led1SwatchImage, led2SwatchImage, led3SwatchImage;
    private Integer led1RGB[];
    private Integer led2RGB[];
    private Integer led3RGB[];
    private boolean isLed1Changed = false, isLed2Changed = false, isLed3Changed = false;

    private Speaker speaker;
    private int finalPitch;
    private String currentNote;
    private SeekBar seekBarVolume, seekBarPitch;
    private TextView textViewVolume, textViewNotePitch;
    private ImageView sheetMusic;
    private boolean isVolumeChanged = false;

    // Seekbar change listeners

    private SeekBar.OnSeekBarChangeListener seekBarServo1Listener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            textViewServo1Value.setText(String.format(String.valueOf(i) + "°"));
            isServo1Changed = true;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            MelodySmartMessage message = MessageConstructor.constructSetOutput(servos[0], seekBar.getProgress());
            globalHandler.melodySmartDeviceHandler.addMessage(message);
        }
    };

    private SeekBar.OnSeekBarChangeListener seekBarServo2Listener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            textViewServo2Value.setText(String.format(String.valueOf(i) + "°"));
            isServo2Changed = true;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            MelodySmartMessage message = MessageConstructor.constructSetOutput(servos[1], seekBar.getProgress());
            globalHandler.melodySmartDeviceHandler.addMessage(message);
        }
    };

    private SeekBar.OnSeekBarChangeListener seekBarServo3Listener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            textViewServo3Value.setText(String.format(String.valueOf(i) + "°"));
            isServo3Changed = true;
        }


        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            MelodySmartMessage message = MessageConstructor.constructSetOutput(servos[2], seekBar.getProgress());
            globalHandler.melodySmartDeviceHandler.addMessage(message);
        }
    };

    private SeekBar.OnSeekBarChangeListener seekBarVolumeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            textViewVolume.setText(String.format("Volume: " + String.valueOf(i)));
            isVolumeChanged = true;
        }


        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            MelodySmartMessage message = MessageConstructor.constructSetOutput(speaker.getVolume(), seekBar.getProgress());
            globalHandler.melodySmartDeviceHandler.addMessage(message);
        }
    };

    private SeekBar.OnSeekBarChangeListener seekBarPitchListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            Log.v(Constants.LOG_TAG, "onProgressChanged");
            finalPitch = getFrequency(i);
            textViewNotePitch.setText(currentNote + " - " + String.valueOf(finalPitch) + " " + getString(R.string.hz));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            MelodySmartMessage message = MessageConstructor.constructSetOutput(speaker.getPitch(), getFrequency(seekBar.getProgress()));
            globalHandler.melodySmartDeviceHandler.addMessage(message);
        }
    };

    private int getFrequency(int progress) {
        int result = 0;

        switch (progress) {
            case 0:
                currentNote = "C";
                sheetMusic.setImageResource(R.drawable.c1);
                result = Constants.MusicNoteFrequencies.C_4;
                break;
            case 1:
                currentNote = "D";
                sheetMusic.setImageResource(R.drawable.d1);
                result = Constants.MusicNoteFrequencies.D_4;
                break;
            case 2:
                currentNote = "E";
                sheetMusic.setImageResource(R.drawable.e1);
                result = Constants.MusicNoteFrequencies.E_4;
                break;
            case 3:
                currentNote = "F";
                sheetMusic.setImageResource(R.drawable.f1);
                result = Constants.MusicNoteFrequencies.F_4;
                break;
            case 4:
                currentNote = "G";
                sheetMusic.setImageResource(R.drawable.g1);
                result = Constants.MusicNoteFrequencies.G_4;
                break;
            case 5:
                currentNote = "A";
                sheetMusic.setImageResource(R.drawable.a1);
                result = Constants.MusicNoteFrequencies.A_4;
                break;
            case 6:
                currentNote = "B";
                sheetMusic.setImageResource(R.drawable.b1);
                result = Constants.MusicNoteFrequencies.B_4;
                break;
            case 7:
                currentNote = "C";
                sheetMusic.setImageResource(R.drawable.c2);
                result = Constants.MusicNoteFrequencies.C_5;
                break;
            case 8:
                currentNote = "D";
                sheetMusic.setImageResource(R.drawable.d2);
                result = Constants.MusicNoteFrequencies.D_5;
                break;
            case 9:
                currentNote = "E";
                sheetMusic.setImageResource(R.drawable.e2);
                result = Constants.MusicNoteFrequencies.E_5;
                break;
            case 10:
                currentNote = "F";
                sheetMusic.setImageResource(R.drawable.f2);
                result = Constants.MusicNoteFrequencies.F_5;
                break;
            case 11:
                currentNote = "G";
                sheetMusic.setImageResource(R.drawable.g2);
                result = Constants.MusicNoteFrequencies.G_5;
                break;
            case 12:
                currentNote = "A";
                sheetMusic.setImageResource(R.drawable.a2);
                result = Constants.MusicNoteFrequencies.A_5;
                break;
            case 13:
                currentNote = "B";
                sheetMusic.setImageResource(R.drawable.b2);
                result = Constants.MusicNoteFrequencies.B_5;
                break;
            case 14:
                currentNote = "C";
                sheetMusic.setImageResource(R.drawable.c3);
                result = Constants.MusicNoteFrequencies.C_6;
                break;
        }

        return result;
    }


    //onclick listeners for LEDs

    private Button.OnClickListener imageLed1SwatchOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String hex;
            if (isLed1Changed)
                hex = convertRgbToHex(led1RGB);
            else
                hex = leds[0].getMaxColorHex();
            DialogFragment dialog = LedConstantColorDialog.newInstance(hex, leds[0].getPortNumber(),ControlOutputsDialog.this);
            dialog.show(getFragmentManager(), "tag");
        }
    };

    private Button.OnClickListener imageLed2SwatchOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String hex;
            if (isLed2Changed)
                hex = convertRgbToHex(led2RGB);
            else
                hex = leds[1].getMaxColorHex();
            DialogFragment dialog = LedConstantColorDialog.newInstance(hex, leds[1].getPortNumber(),ControlOutputsDialog.this);
            dialog.show(getFragmentManager(), "tag");
        }
    };

    private Button.OnClickListener imageLed3SwatchOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String hex;
            if (isLed3Changed)
                hex = convertRgbToHex(led3RGB);
            else
                hex = leds[2].getMaxColorHex();
            DialogFragment dialog = LedConstantColorDialog.newInstance(hex, leds[2].getPortNumber(),ControlOutputsDialog.this);
            dialog.show(getFragmentManager(), "tag");
        }
    };

    private String convertRgbToHex(Integer[] rgb)
    {
        return String.format("#%02x%02x%02x", rgb[0], rgb[1], rgb[2]);
    }

    //after color is chosen for LEDs

    public void onLedConstantColorChosen(Integer[] rgb, int port, int swatch) {
        switch (port) {
            case 1:
                isLed1Changed = true;
                if (!TriColorLed.isSwatchInExistingSelection(convertRgbToHex(rgb)))
                    led1SwatchImage.setColorFilter(Color.parseColor(convertRgbToHex(rgb)));
                else {
                    led1SwatchImage.clearColorFilter();
                    led1SwatchImage.setImageResource(swatch);
                }
                led1RGB = rgb;
                break;
            case 2:
                isLed2Changed = true;
                if (!TriColorLed.isSwatchInExistingSelection(convertRgbToHex(rgb)))
                    led2SwatchImage.setColorFilter(Color.parseColor(convertRgbToHex(rgb)));
                else {
                    led2SwatchImage.clearColorFilter();
                    led2SwatchImage.setImageResource(swatch);
                }
                led2RGB = rgb;
                break;
            case 3:
                isLed3Changed = true;
                if (!TriColorLed.isSwatchInExistingSelection(convertRgbToHex(rgb)))
                    led3SwatchImage.setColorFilter(Color.parseColor(convertRgbToHex(rgb)));
                else {
                    led3SwatchImage.clearColorFilter();
                    led3SwatchImage.setImageResource(swatch);
                }
                led3RGB = rgb;
                break;
        }

        ArrayList<MelodySmartMessage> ledMessages = new ArrayList<>();
        ledMessages.add(MessageConstructor.constructSetOutput(leds[port - 1].getRedLed(), rgb[0]));
        ledMessages.add(MessageConstructor.constructSetOutput(leds[port - 1].getGreenLed(), rgb[1]));
        ledMessages.add(MessageConstructor.constructSetOutput(leds[port - 1].getBlueLed(), rgb[2]));

        for (MelodySmartMessage message : ledMessages)
            globalHandler.melodySmartDeviceHandler.addMessage(message);
    }

    private Button.OnClickListener doneClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            revertRelationships();
            dismiss();
        }
    };

    //reverts every relationship to what it was prior to opening the dialog
    private void revertRelationships() {
        ArrayList<MelodySmartMessage> messages = new ArrayList<>();
        if (isServo1Changed) {
            messages.add(MessageConstructor.constructRemoveRelation(servos[0]));

            messages.add(MessageConstructor.constructRelationshipMessage(
                    servos[0], servos[0].getSettings()));
            Log.i("Changed", "SERVO 1");
        }
        if (isServo2Changed) {
            messages.add(MessageConstructor.constructRemoveRelation(servos[1]));

            messages.add(MessageConstructor.constructRelationshipMessage(
                    servos[1], servos[1].getSettings()));
            Log.i("Changed", "SERVO 2");
        }
        if (isServo3Changed) {
            messages.add(MessageConstructor.constructRemoveRelation(servos[2]));

            messages.add(MessageConstructor.constructRelationshipMessage(
                    servos[2], servos[2].getSettings()));
            Log.i("Changed", "SERVO 3");
        }

        if (isLed1Changed) {
            messages.add(MessageConstructor.constructRemoveRelation(leds[0].getRedLed()));
            messages.add(MessageConstructor.constructRemoveRelation(leds[0].getGreenLed()));
            messages.add(MessageConstructor.constructRemoveRelation(leds[0].getBlueLed()));

            messages.add(MessageConstructor.constructRelationshipMessage(
                    leds[0].getRedLed(), leds[0].getRedLed().getSettings()));
            messages.add(MessageConstructor.constructRelationshipMessage(
                    leds[0].getGreenLed(), leds[0].getGreenLed().getSettings()));
            messages.add(MessageConstructor.constructRelationshipMessage(
                    leds[0].getBlueLed(), leds[0].getBlueLed().getSettings()));
            Log.i("Changed", "LED 1");
        }

        if (isLed2Changed) {
            messages.add(MessageConstructor.constructRemoveRelation(leds[1].getRedLed()));
            messages.add(MessageConstructor.constructRemoveRelation(leds[1].getGreenLed()));
            messages.add(MessageConstructor.constructRemoveRelation(leds[1].getBlueLed()));

            messages.add(MessageConstructor.constructRelationshipMessage(
                    leds[1].getRedLed(), leds[1].getRedLed().getSettings()));
            messages.add(MessageConstructor.constructRelationshipMessage(
                    leds[1].getGreenLed(), leds[1].getGreenLed().getSettings()));
            messages.add(MessageConstructor.constructRelationshipMessage(
                    leds[1].getBlueLed(), leds[1].getBlueLed().getSettings()));
            Log.i("Changed", "LED 2");
        }

        if (isLed3Changed) {
            messages.add(MessageConstructor.constructRemoveRelation(leds[2].getRedLed()));
            messages.add(MessageConstructor.constructRemoveRelation(leds[2].getGreenLed()));
            messages.add(MessageConstructor.constructRemoveRelation(leds[2].getBlueLed()));

            messages.add(MessageConstructor.constructRelationshipMessage(
                    leds[2].getRedLed(), leds[2].getRedLed().getSettings()));
            messages.add(MessageConstructor.constructRelationshipMessage(
                    leds[2].getGreenLed(), leds[2].getGreenLed().getSettings()));
            messages.add(MessageConstructor.constructRelationshipMessage(
                    leds[2].getBlueLed(), leds[2].getBlueLed().getSettings()));
            Log.i("Changed", "LED 3");
        }

        if (isVolumeChanged) {
            messages.add(MessageConstructor.constructRemoveRelation(speaker.getVolume()));

            messages.add(MessageConstructor.constructRelationshipMessage(
                    speaker.getVolume(), speaker.getVolume().getSettings()));
            Log.i("Changed", "VOLUME");
        }

        //always updates pitch as it is always changed in init
        messages.add(MessageConstructor.constructRemoveRelation(speaker.getPitch()));

        messages.add(MessageConstructor.constructRelationshipMessage(
                speaker.getPitch(), speaker.getPitch().getSettings()));
        Log.i("Changed", "PITCH");

        for (MelodySmartMessage message : messages)
            globalHandler.melodySmartDeviceHandler.addMessage(message);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "onCreateDialog");
        super.onCreateDialog(savedInstanceState);
        globalHandler = GlobalHandler.getInstance(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_control_outputs, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        setCancelable(false);

        Flutter flutter = globalHandler.sessionHandler.getSession().getFlutter();
        servos = flutter.getServos();
        leds = flutter.getTriColorLeds();
        speaker = flutter.getSpeaker();

        //servos

        seekBarServo1 = (SeekBar) view.findViewById(R.id.seek_servo_1);
        seekBarServo1.setOnSeekBarChangeListener(seekBarServo1Listener);

        seekBarServo2 = (SeekBar) view.findViewById(R.id.seek_servo_2);
        seekBarServo2.setOnSeekBarChangeListener(seekBarServo2Listener);

        seekBarServo3 = (SeekBar) view.findViewById(R.id.seek_servo_3);
        seekBarServo3.setOnSeekBarChangeListener(seekBarServo3Listener);

        textViewServo1Value = (TextView) view.findViewById(R.id.text_current_servo_pos_1);
        textViewServo1Value.setText(String.format(String.valueOf(seekBarServo1.getProgress()) + "°"));

        textViewServo2Value = (TextView) view.findViewById(R.id.text_current_servo_pos_2);
        textViewServo2Value.setText(String.format(String.valueOf(seekBarServo2.getProgress()) + "°"));

        textViewServo3Value = (TextView) view.findViewById(R.id.text_current_servo_pos_3);
        textViewServo3Value.setText(String.format(String.valueOf(seekBarServo3.getProgress()) + "°"));

        //speaker

        textViewVolume = (TextView) view.findViewById(R.id.text_volume);

        seekBarVolume = (SeekBar) view.findViewById(R.id.seek_volume);
        seekBarVolume.setOnSeekBarChangeListener(seekBarVolumeListener);

        seekBarPitch = (SeekBar) view.findViewById(R.id.seek_pitch);
        seekBarPitch.setOnSeekBarChangeListener(seekBarPitchListener);

        sheetMusic = (ImageView) view.findViewById(R.id.image_sheet_music);
        textViewNotePitch = (TextView) view.findViewById(R.id.text_current_note_pitch);
        finalPitch = getFrequency(0);
        textViewNotePitch.setText(currentNote + " - " + String.valueOf(finalPitch) + " " + getString(R.string.hz));
        MelodySmartMessage message = MessageConstructor.constructSetOutput(speaker.getPitch(), finalPitch);
        globalHandler.melodySmartDeviceHandler.addMessage(message);

        //led

        led1SwatchImage = (ImageView) view.findViewById(R.id.image_led_color_1);
        led1SwatchImage.setImageResource(TriColorLed.getSwatchFromColor(leds[0].getMaxColorHex()));
        led1SwatchImage.setOnClickListener(imageLed1SwatchOnClickListener);

        led2SwatchImage = (ImageView) view.findViewById(R.id.image_led_color_2);
        led2SwatchImage.setImageResource(TriColorLed.getSwatchFromColor(leds[1].getMaxColorHex()));
        led2SwatchImage.setOnClickListener(imageLed2SwatchOnClickListener);

        led3SwatchImage = (ImageView) view.findViewById(R.id.image_led_color_3);
        led3SwatchImage.setImageResource(TriColorLed.getSwatchFromColor(leds[2].getMaxColorHex()));
        led3SwatchImage.setOnClickListener(imageLed3SwatchOnClickListener);

        led1RGB = new Integer[3];
        led2RGB = new Integer[3];
        led3RGB = new Integer[3];

        //exiting out of dialog

        view.findViewById(R.id.button_done).setOnClickListener(doneClickListener);
        view.findViewById(R.id.button_close).setOnClickListener(doneClickListener);

        return builder.create();
    }


    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
