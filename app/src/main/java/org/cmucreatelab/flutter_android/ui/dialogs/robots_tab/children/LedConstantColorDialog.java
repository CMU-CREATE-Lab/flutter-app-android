package org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.children;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.io.Serializable;

/**
 * Created by Parv on 6/15/2018.
 *
 * LedConstantColorDialog
 *
 * A Dialog that prompts the user to choose a color.
 */
public class LedConstantColorDialog extends ChooseColorDialog implements ChooseColorDialog.SetColorListener {

    private DialogColorConstantListener colorListener;
    private int portNumber;

    public static LedConstantColorDialog newInstance(String color, int portNumber, Serializable serializable) {
        LedConstantColorDialog ledConstantColorDialog = new LedConstantColorDialog();

        Bundle args = new Bundle();
        args.putSerializable(SELECTED_COLOR_KEY, color);
        args.putSerializable(PORT_NUMBER_KEY, portNumber);
        args.putSerializable(COLOR_KEY, serializable);
        ledConstantColorDialog.setArguments(args);

        return ledConstantColorDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "onCreateDialog");
        colorListener = (DialogColorConstantListener) getArguments().getSerializable(COLOR_KEY);
        portNumber = (int) getArguments().getSerializable(PORT_NUMBER_KEY);
        setColorListener = this;
        return super.onCreateDialog(savedInstanceState);
    }


    @Override
    public void onSetColor(int swatch) {
        colorListener.onLedConstantColorChosen(finalRGB, portNumber, swatch);
        dismiss();
    }


    public interface DialogColorConstantListener {
        void onLedConstantColorChosen(Integer[] rgb, int portNumber, int swatch);
    }

}
