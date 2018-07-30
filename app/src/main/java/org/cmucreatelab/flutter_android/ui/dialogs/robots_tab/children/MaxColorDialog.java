package org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.children;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
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
    protected static final String HIGH_TEXT_KEY = "high_text";
    private String highText;

    public static MaxColorDialog newInstance(String color, String highText, Serializable serializable) {
        MaxColorDialog maxColorDialog = new MaxColorDialog();

        Bundle args = new Bundle();
        args.putSerializable(SELECTED_COLOR_KEY, color);
        args.putSerializable(COLOR_KEY, serializable);
        args.putString(HIGH_TEXT_KEY, highText);
        maxColorDialog.setArguments(args);

        return maxColorDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "onCreateDialog");
        highColorListener = (DialogHighColorListener) getArguments().getSerializable(COLOR_KEY);
        highText = getArguments().getString(HIGH_TEXT_KEY);
        setColorListener = this;
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        TextView textTitle = (TextView) dialogView.findViewById(R.id.text_output_title);
        textTitle.setText("Choose " + highText + " Color");
        return dialog;
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
