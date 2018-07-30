package org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.children;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.io.Serializable;

/**
 * Created by Steve on 9/22/2016.
 *
 * MinColorDialog
 *
 * A Dialog that prompts the user to choose a low color.
 */
public class MinColorDialog extends ChooseColorDialog implements ChooseColorDialog.SetColorListener {
    private DialogLowColorListener lowColorListener;
    protected static final String LOW_TEXT_KEY = "low_text";
    private String lowText;

    public static MinColorDialog newInstance(String color, String lowText, Serializable serializable) {
        MinColorDialog minColorDialog = new MinColorDialog();

        Bundle args = new Bundle();
        args.putSerializable(SELECTED_COLOR_KEY, color);
        args.putSerializable(COLOR_KEY, serializable);
        args.putString(LOW_TEXT_KEY, lowText);
        minColorDialog.setArguments(args);

        return minColorDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "onCreateDialog");
        lowColorListener = (DialogLowColorListener) getArguments().getSerializable(COLOR_KEY);
        lowText = getArguments().getString(LOW_TEXT_KEY);
        setColorListener = this;
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        TextView textTitle = (TextView) dialogView.findViewById(R.id.text_output_title);
        textTitle.setText("Choose " + lowText + " Color");
        return dialog;
    }


    @Override
    public void onSetColor(int swatch) {
        Log.d(Constants.LOG_TAG, "MinColorDialog.onSetColor");
        lowColorListener.onLowColorChosen(finalRGB, swatch);
        dismiss();
    }


    public interface DialogLowColorListener {
        public void onLowColorChosen(Integer[] rgb, int swatch);
    }

}
