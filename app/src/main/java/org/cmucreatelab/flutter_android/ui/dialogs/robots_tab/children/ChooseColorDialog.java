package org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.children;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.color_picker.dialog.ColorPickerDialogFragment;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialog;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by Steve on 9/7/2016.
 *
 * ChooseColorDialog
 *
 * An abstract Dialog that handles the color picking.
 */
public abstract class ChooseColorDialog extends BaseResizableDialog implements ColorPickerDialogFragment.ColorPickerDialogListener, Serializable {
    protected static final String COLOR_KEY = "color_listener";
    protected static final String PORT_NUMBER_KEY = "port_number";
    protected static final String SELECTED_COLOR_KEY = "selected_color";
    private FrameLayout frameFinalColor;
    protected Integer[] finalRGB;
    protected SetColorListener setColorListener;

    public static class DrawableColor {
        final int color, imageView, swatch, swatchSelected;

        DrawableColor(int color, int imageView, int swatch, int swatchSelected) {
            this.color = color;
            this.imageView = imageView;
            this.swatch = swatch;
            this.swatchSelected = swatchSelected;
        }
    }

    private SparseArray<DrawableColor> colorSwatches = new SparseArray() {{
        put(Constants.ColorSwatches.RED, new DrawableColor(Constants.ColorSwatches.RED, R.id.imageView_red, R.drawable.swatch_red, R.drawable.swatch_red_selected));
        put(Constants.ColorSwatches.ORANGE, new DrawableColor(Constants.ColorSwatches.ORANGE, R.id.imageView_orange, R.drawable.swatch_orange, R.drawable.swatch_orange_selected));
        put(Constants.ColorSwatches.YELLOW, new DrawableColor(Constants.ColorSwatches.YELLOW, R.id.imageView_yellow, R.drawable.swatch_yellow, R.drawable.swatch_yellow_selected));
        put(Constants.ColorSwatches.GREEN, new DrawableColor(Constants.ColorSwatches.GREEN, R.id.imageView_green, R.drawable.swatch_green, R.drawable.swatch_green_selected));
        put(Constants.ColorSwatches.SPRING_GREEN, new DrawableColor(Constants.ColorSwatches.SPRING_GREEN, R.id.imageView_spring_green, R.drawable.swatch_spring_green, R.drawable.swatch_spring_green_selected));
        put(Constants.ColorSwatches.CYAN, new DrawableColor(Constants.ColorSwatches.CYAN, R.id.imageView_cyan, R.drawable.swatch_cyan, R.drawable.swatch_cyan_selected));
        put(Constants.ColorSwatches.BLUE, new DrawableColor(Constants.ColorSwatches.BLUE, R.id.imageView_blue, R.drawable.swatch_blue, R.drawable.swatch_blue_selected));
        put(Constants.ColorSwatches.VIOLET, new DrawableColor(Constants.ColorSwatches.VIOLET, R.id.imageView_violet, R.drawable.swatch_violet, R.drawable.swatch_violet_selected));
        put(Constants.ColorSwatches.MAGENTA, new DrawableColor(Constants.ColorSwatches.MAGENTA, R.id.imageView_magenta, R.drawable.swatch_magenta, R.drawable.swatch_magenta_selected));
        put(Constants.ColorSwatches.ROSE, new DrawableColor(Constants.ColorSwatches.ROSE, R.id.imageView_rose, R.drawable.swatch_rose, R.drawable.swatch_rose_selected));
        put(Constants.ColorSwatches.WHITE, new DrawableColor(Constants.ColorSwatches.WHITE, R.id.imageView_white, R.drawable.swatch_white, R.drawable.swatch_white_selected));
        put(Constants.ColorSwatches.BLACK, new DrawableColor(Constants.ColorSwatches.BLACK, R.id.imageView_black, R.drawable.swatch_black, R.drawable.swatch_black_selected));
        put(Constants.ColorSwatches.WHITE, new DrawableColor(Constants.ColorSwatches.WHITE, R.id.imageView_white, R.drawable.swatch_white, R.drawable.swatch_white_selected));
        put(Constants.ColorSwatches.BLACK, new DrawableColor(Constants.ColorSwatches.BLACK, R.id.imageView_black, R.drawable.swatch_black, R.drawable.swatch_black_selected));
        put(Constants.ColorSwatches.WHITE_DEFAULT, new DrawableColor(Constants.ColorSwatches.WHITE, R.id.imageView_white, R.drawable.swatch_white, R.drawable.swatch_white_selected));
        put(Constants.ColorSwatches.BLACK_DEFAULT, new DrawableColor(Constants.ColorSwatches.BLACK, R.id.imageView_black, R.drawable.swatch_black, R.drawable.swatch_black_selected));
    }};
    private DrawableColor currentlySelected = colorSwatches.get(Constants.ColorSwatches.WHITE);
    protected View dialogView;


    private static int[] intToRGB(int color) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        return new int[]{r, g, b};
    }


    private void selectColor(DrawableColor drawableColor) {
        if (dialogView == null) {
            Log.e(Constants.LOG_TAG, "cannot selectColor when dialogView is null");
            return;
        }
        if (currentlySelected != null && currentlySelected.imageView != -1) {
            ((ImageView) dialogView.findViewById(currentlySelected.imageView)).setBackgroundResource(0);
        }
        this.currentlySelected = drawableColor;
        ((ImageView) dialogView.findViewById(currentlySelected.imageView)).setBackgroundResource(R.drawable.rectangle_green_border);

        finalRGB = Constants.COLOR_PICKER_FLUTTER_RGB.get(currentlySelected.color);
        frameFinalColor.setBackgroundColor(currentlySelected.color);
    }

    private void selectCustomColor(int color) {
        if (currentlySelected != null && currentlySelected.imageView != -1) {
            ((ImageView) dialogView.findViewById(currentlySelected.imageView)).setBackgroundResource(0);
        }
        this.currentlySelected = new DrawableColor(color, -1, -1, -1);

        int[] rgb = intToRGB(color);
        int r = rgb[0];
        int g = rgb[1];
        int b = rgb[2];

        finalRGB = new Integer[]{r, g, b};
        frameFinalColor.setBackgroundColor(color);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_choose_color, null);
        this.dialogView = view;
        this.frameFinalColor = (FrameLayout) view.findViewById(R.id.frame_final_color);
        String selectedColor = (String) getArguments().getSerializable(SELECTED_COLOR_KEY);
        Log.d(Constants.LOG_TAG, "color: " + selectedColor);
        int color = Color.parseColor(selectedColor);

        Integer swatchHex = Constants.TRUE_HEX_TO_SWATCH_HEX.get(color);
        if (swatchHex != null && colorSwatches.indexOfKey(swatchHex) >= 0) {
            selectColor(colorSwatches.get(Constants.TRUE_HEX_TO_SWATCH_HEX.get(color)));
        } else {
            selectCustomColor(color);
        }

        view.findViewById(R.id.link_buttons_wizard).setVisibility(View.GONE);

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ButterKnife.bind(this, view);

        TextView textTitle = (TextView) view.findViewById(R.id.text_output_title);
        textTitle.setText(R.string.choose_color);

        return builder.create();
    }


    // OnClickListener implementation (ButterKnife)


    @OnClick(R.id.button_set_color)
    @Optional
    public void onClickSetColor() {
        setColorListener.onSetColor(currentlySelected.swatchSelected);
    }

    @OnClick(R.id.image_advanced_settings)
    public void onClickAdvancedSettings() {
        ColorPickerDialogFragment f = ColorPickerDialogFragment
                .newInstance(0, "Advanced Color Picker", null, currentlySelected.color, false, this);
        f.show(getActivity().getFragmentManager(), "d");
    }

    public void onColorSelected(int dialogId, int color) {
        selectCustomColor(color);
    }

    @OnClick(R.id.imageView_red)
    public void onClickRed(View view) {
        Log.d(Constants.LOG_TAG, "onClickRed");
        selectColor(colorSwatches.get(Constants.ColorSwatches.RED));
    }

    @OnClick(R.id.imageView_orange)
    public void onClickOrange(View view) {
        Log.d(Constants.LOG_TAG, "onClickOrange");
        selectColor(colorSwatches.get(Constants.ColorSwatches.ORANGE));
    }

    @OnClick(R.id.imageView_yellow)
    public void onClickYellow(View view) {
        Log.d(Constants.LOG_TAG, "onClickYellow");
        selectColor(colorSwatches.get(Constants.ColorSwatches.YELLOW));
    }

    @OnClick(R.id.imageView_green)
    public void onClickGreen(View view) {
        Log.d(Constants.LOG_TAG, "onClickGreen");
        selectColor(colorSwatches.get(Constants.ColorSwatches.GREEN));
    }

    @OnClick(R.id.imageView_spring_green)
    public void onClickSpringGreen(View view) {
        Log.d(Constants.LOG_TAG, "onClickSpringGreen");
        selectColor(colorSwatches.get(Constants.ColorSwatches.SPRING_GREEN));
    }

    @OnClick(R.id.imageView_cyan)
    public void onClickCyan(View view) {
        Log.d(Constants.LOG_TAG, "onClickCyan");
        selectColor(colorSwatches.get(Constants.ColorSwatches.CYAN));
    }

    @OnClick(R.id.imageView_blue)
    public void onClickBlue(View view) {
        Log.d(Constants.LOG_TAG, "onClickBlue");
        selectColor(colorSwatches.get(Constants.ColorSwatches.BLUE));
    }

    @OnClick(R.id.imageView_violet)
    public void onClickViolet(View view) {
        Log.d(Constants.LOG_TAG, "onClickViolet");
        selectColor(colorSwatches.get(Constants.ColorSwatches.VIOLET));
    }

    @OnClick(R.id.imageView_magenta)
    public void onClickMagenta(View view) {
        Log.d(Constants.LOG_TAG, "onClickMagenta");
        selectColor(colorSwatches.get(Constants.ColorSwatches.MAGENTA));
    }

    @OnClick(R.id.imageView_rose)
    public void onClickRose(View view) {
        Log.d(Constants.LOG_TAG, "onClickRose");
        selectColor(colorSwatches.get(Constants.ColorSwatches.ROSE));
    }

    @OnClick(R.id.imageView_white)
    public void onClickWhite(View view) {
        Log.d(Constants.LOG_TAG, "onClickWhite");
        selectColor(colorSwatches.get(Constants.ColorSwatches.WHITE));
    }

    @OnClick(R.id.imageView_black)
    public void onClickBlack(View view) {
        Log.d(Constants.LOG_TAG, "onClickBlack");
        selectColor(colorSwatches.get(Constants.ColorSwatches.BLACK));
    }

    @OnClick(R.id.button_close)
    public void onClickClose() {
        dismiss();
    }


    // interface for children

    public interface SetColorListener {
        public void onSetColor(int selectedSwatch);
    }

}
