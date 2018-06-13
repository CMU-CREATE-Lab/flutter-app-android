package org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.children;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.io.Serializable;

/**
 * Created by Steve on 9/22/2016.
 *
 * MinColorDialog
 *
 * A Dialog that prompts the user to choose a low color.
 */
public class LedThreeColorDialog extends ChooseColorDialog implements ChooseColorDialog.SetColorListener {

    private DialogColorThreeListener colorListener;


    public static LedThreeColorDialog newInstance(String color, Serializable serializable) {
        LedThreeColorDialog ledThreeColorDialog = new LedThreeColorDialog();

        Bundle args = new Bundle();
        args.putSerializable(SELECTED_COLOR_KEY, color);
        args.putSerializable(COLOR_KEY, serializable);
        ledThreeColorDialog.setArguments(args);

        return ledThreeColorDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "onCreateDialog");
        colorListener = (DialogColorThreeListener) getArguments().getSerializable(COLOR_KEY);
        setColorListener = this;
        return super.onCreateDialog(savedInstanceState);
    }


    @Override
    public void onSetColor(int swatch) {
        colorListener.onLed3ColorChosen(finalRGB, swatch);
        dismiss();
    }


    public interface DialogColorThreeListener {
        void onLed3ColorChosen(Integer[] rgb, int swatch);
    }

}
