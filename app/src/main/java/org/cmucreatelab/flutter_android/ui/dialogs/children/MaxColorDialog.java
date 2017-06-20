package org.cmucreatelab.flutter_android.ui.dialogs.children;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.io.Serializable;

/**
 * Created by Steve on 9/16/2016.
 *
 * MaxColorDialog
 *
 * A Dialog that prompts the user to choose a high color.
 */
public class MaxColorDialog extends ChooseColorDialog implements ChooseColorDialog.SetColorListener {


    private DialogHighColorListener highColorListener;


    public static MaxColorDialog newInstance(String color, Serializable serializable) {
        MaxColorDialog maxColorDialog = new MaxColorDialog();

        Bundle args = new Bundle();
        args.putSerializable(SELECTED_COLOR_KEY, color);
        args.putSerializable(COLOR_KEY, serializable);
        maxColorDialog.setArguments(args);

        return maxColorDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "onCreateDialog");
        highColorListener = (DialogHighColorListener) getArguments().getSerializable(COLOR_KEY);
        setColorListener = this;
        return super.onCreateDialog(savedInstanceState);
    }


    @Override
    public void onSetColor(int swatch) {
        Log.d(Constants.LOG_TAG, "MaxColorDialog.onSetColor");
        highColorListener.onHighColorChosen(finalRGB, swatch);
        dismiss();
    }


    public interface DialogHighColorListener {
        public void onHighColorChosen(Integer[] rgb, int swatch);
    }

}
