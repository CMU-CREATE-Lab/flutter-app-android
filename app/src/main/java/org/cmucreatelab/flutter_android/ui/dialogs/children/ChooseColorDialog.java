package org.cmucreatelab.flutter_android.ui.dialogs.children;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Steve on 9/7/2016.
 *
 * ChooseColorDialog
 *
 * An abstract Dialog that handles the color picking.
 */
public abstract class ChooseColorDialog extends BaseResizableDialog implements DialogInterface.OnClickListener {


    protected static final String COLOR_KEY = "color_key";

    private static final int PREMIXED_TOP[] = { Color.rgb(208,2,27), Color.rgb(248,231,28), Color.rgb(74,189,226), Color.rgb(245,166,35)};
    private static final int PREMIXED_BOTTOM[] = { Color.rgb(99,189,107), Color.rgb(166,103,222), Color.WHITE, Color.BLACK};

    private float h,s, v;
    private FrameLayout frameFinalColor;
    private SeekBar seekBarHue;
    private SeekBar seekBarSaturation;
    private SeekBar seekBarValue;

    protected int[] finalRGB;


    /*private static float hueToRgb(float p, float q, float t) {
        if (t < 0f)
            t += 1f;
        if (t > 1f)
            t -= 1f;
        if (t < 1f/6f)
            return p + (q - p) * 6f * t;
        if (t < 1f/2f)
            return q;
        if (t < 2f/3f)
            return p + (q - p) * (2f/3f - t) * 6f;
        return p;
    }


    private static int[] hslToRgb(float h, float s, float v) {
        float r,g,b;

        h = (h-0) / 359;
        s = (s-0) / 99;
        v = (v-0) / 99;

        if (s == 0f) {
            r = g = b = 1;
        } else {
            float q = v < 0.5f ? v * (1 + s) : v + s - v * s;
            float p = 2 * v - q;
            r = hueToRgb(p, q, h + 1f/3f);
            g = hueToRgb(p, q, h);
            b = hueToRgb(p, q, h - 1f/3f);
        }
        int[] result = {(int) (r * 255), (int) (g * 255), (int) (b * 255)};
        return result;
    }*/


    private static int[] intToRGB(int color) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        return new int[] {r, g, b};
    }


    private void updateColor() {
        final int color = Color.HSVToColor(new float[] {h, s/99, v /99});
        finalRGB = intToRGB(color);

        Drawable drawable = new Drawable() {

            @Override
            public void draw(Canvas canvas) {
                // divided by 99 because the docs only want the saturation and value to be between 0-1
                canvas.drawColor(color);
            }

            @Override
            public void setAlpha(int i) {

            }

            @Override
            public void setColorFilter(ColorFilter colorFilter) {

            }

            @Override
            public int getOpacity() {
                return 0;
            }
        };
        frameFinalColor.setBackground(drawable);

    }


    protected SeekBar.OnSeekBarChangeListener hueSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean bool) {
            Log.d(Constants.LOG_TAG, "onHueChanged");
            h = i;
            updateColor();

            // update the thumb
            //Drawable thumb = seekBar.getThumb();
            //thumb.setColorFilter(Color.HSVToColor(new float[] {h,s, v}), PorterDuff.Mode.SRC_IN);

            // update the saturation
            LinearGradient linearGradient = new LinearGradient(0.f, 0.f, seekBarSaturation.getWidth(), 0.0f,
                    Color.HSVToColor(new float[] {h, 100, 100}), Color.WHITE, Shader.TileMode.CLAMP);
            ShapeDrawable shape = new ShapeDrawable(new RectShape());
            shape.getPaint().setShader(linearGradient);
            seekBarSaturation.setProgressDrawable(shape);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };


    protected SeekBar.OnSeekBarChangeListener saturationSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            Log.d(Constants.LOG_TAG, "onSaturationChanged");
            s = 99 - i;
            updateColor();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };


    protected SeekBar.OnSeekBarChangeListener valueSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            Log.d(Constants.LOG_TAG, "onLightnessChanged");
            v = i;
            updateColor();
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
        final View view = inflater.inflate(R.layout.dialog_choose_color, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        builder.setPositiveButton(R.string.set_color, this);
        ButterKnife.bind(this, view);

        frameFinalColor = (FrameLayout) view.findViewById(R.id.frame_final_color);
        seekBarHue = (SeekBar) view.findViewById(R.id.seek_hue);
        seekBarSaturation = (SeekBar) view.findViewById(R.id.seek_saturation);
        seekBarValue = (SeekBar) view.findViewById(R.id.seek_value);
        seekBarHue.setOnSeekBarChangeListener(hueSeekBarChangeListener);
        seekBarSaturation.setOnSeekBarChangeListener(saturationSeekBarChangeListener);
        seekBarValue.setOnSeekBarChangeListener(valueSeekBarChangeListener);

        seekBarSaturation.setProgress(0);
        seekBarValue.setProgress(99);

        h = 0;
        s = 99;
        v = 99;

        updateColor();

        // initialize the color gradient for the hue
        seekBarHue.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.d(Constants.LOG_TAG, "hue " + seekBarHue.getWidth());
                seekBarHue.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                LinearGradient linearGradient = new LinearGradient(0.f, 0.f, seekBarHue.getWidth(), 0.0f,
                        new int[] {
                                Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.RED
                        }, null, Shader.TileMode.CLAMP);
                ShapeDrawable shape = new ShapeDrawable(new RectShape());
                shape.getPaint().setShader(linearGradient);
                seekBarHue.setProgressDrawable(shape);
            }
        });

        // initialize the color gradient for the lightness
        seekBarSaturation.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                seekBarSaturation.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                LinearGradient linearGradient = new LinearGradient(0.f, 0.f, seekBarSaturation.getWidth(), 0.0f,
                        Color.HSVToColor(new float[] {h,s, v}), Color.WHITE, Shader.TileMode.CLAMP);
                ShapeDrawable shape = new ShapeDrawable(new RectShape());
                shape.getPaint().setShader(linearGradient);
                seekBarSaturation.setProgressDrawable(shape);
            }
        });

        // initialize the color gradient for the lightness
        seekBarValue.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.d(Constants.LOG_TAG, "light " + seekBarValue.getWidth());
                seekBarValue.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                LinearGradient linearGradient = new LinearGradient(0.f, 0.f, seekBarValue.getWidth(), 0.0f,
                        Color.BLACK, Color.WHITE, Shader.TileMode.CLAMP);
                ShapeDrawable shape = new ShapeDrawable(new RectShape());
                shape.getPaint().setShader(linearGradient);
                seekBarValue.setProgressDrawable(shape);
            }
        });

        return builder.create();
    }

    // premixed click listeners

    @OnClick(R.id.frame_red)
    public void onClickRed(View view) {
        Log.d(Constants.LOG_TAG, "onClickRed");
        finalRGB = intToRGB(PREMIXED_TOP[0]);
        frameFinalColor.setBackgroundColor(PREMIXED_TOP[0]);
    }

    @OnClick(R.id.frame_yellow)
    public void onClickYellow(View view) {
        Log.d(Constants.LOG_TAG, "onClickYellow");
        finalRGB = intToRGB(PREMIXED_TOP[1]);
        frameFinalColor.setBackgroundColor(PREMIXED_TOP[1]);
    }

    @OnClick(R.id.frame_blue)
    public void onClickBlue(View view) {
        Log.d(Constants.LOG_TAG, "onClickBlue");
        finalRGB = intToRGB(PREMIXED_TOP[2]);
        frameFinalColor.setBackgroundColor(PREMIXED_TOP[2]);
    }


    @OnClick(R.id.frame_orange)
    public void onClickOrange(View view) {
        Log.d(Constants.LOG_TAG, "onClickOrange");
        finalRGB = intToRGB(PREMIXED_TOP[3]);
        frameFinalColor.setBackgroundColor(PREMIXED_TOP[3]);
    }

    @OnClick(R.id.frame_green)
    public void onClickGreen(View view) {
        Log.d(Constants.LOG_TAG, "onClickGreen");
        finalRGB = intToRGB(PREMIXED_BOTTOM[0]);
        frameFinalColor.setBackgroundColor(PREMIXED_BOTTOM[0]);
    }

    @OnClick(R.id.frame_purple)
    public void onClickPurple(View view) {
        Log.d(Constants.LOG_TAG, "onClickPurple");
        finalRGB = intToRGB(PREMIXED_BOTTOM[1]);
        frameFinalColor.setBackgroundColor(PREMIXED_BOTTOM[1]);
    }

    @OnClick(R.id.frame_white)
    public void onClickWhite(View view) {
        Log.d(Constants.LOG_TAG, "onClickWhite");
        finalRGB = intToRGB(PREMIXED_BOTTOM[2]);
        frameFinalColor.setBackgroundColor(PREMIXED_BOTTOM[2]);
    }

    @OnClick(R.id.frame_black)
    public void onClickBlack(View view) {
        Log.d(Constants.LOG_TAG, "onClickBlack");
        finalRGB = intToRGB(PREMIXED_BOTTOM[3]);
        frameFinalColor.setBackgroundColor(PREMIXED_BOTTOM[3]);
    }

}
