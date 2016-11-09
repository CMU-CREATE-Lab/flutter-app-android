package org.cmucreatelab.flutter_android.ui.dialogs.children;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.io.Serializable;

/**
 * Created by Steve on 9/16/2016.
 *
 * ColorHighDialog
 *
 * A Dialog that prompts the user to choose a high color.
 */
public class ColorHighDialog extends ChooseColorDialog {


    private DialogHighColorListener highColorListener;


    public static ColorHighDialog newInstance(Serializable serializable) {
        ColorHighDialog colorHighDialog = new ColorHighDialog();

        Bundle args = new Bundle();
        args.putSerializable(COLOR_KEY, serializable);
        colorHighDialog.setArguments(args);

        return colorHighDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "onCreateDialog");
        highColorListener = (DialogHighColorListener) getArguments().getSerializable(COLOR_KEY);
        return super.onCreateDialog(savedInstanceState);
    }


    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        Log.d(Constants.LOG_TAG, "onClickSetColor");
        highColorListener.onHighColorChosen(finalRGB);
    }


    public interface DialogHighColorListener {
        public void onHighColorChosen(int[] rgb);
    }

}
