package org.cmucreatelab.flutter_android.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
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
import android.widget.SeekBar;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

/**
 * Created by Steve on 9/7/2016.
 */
public abstract class DialogFragmentChooseColor extends DialogFragment implements View.OnClickListener, DialogInterface.OnClickListener {

    private float h,s, v;
    private FrameLayout frameFinalColor;
    private SeekBar seekBarHue;
    private SeekBar seekBarSaturation;
    private SeekBar seekBarValue;


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


    private void updateColor() {
        Drawable drawable = new Drawable() {

            @Override
            public void draw(Canvas canvas) {
                // divided by 99 because the docs only want the saturation and value to be between 0-1
                canvas.drawColor(Color.HSVToColor(new float[] {h,s/99, v /99}));
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
        frameFinalColor.setBackground(null);
        frameFinalColor.setBackground(drawable);
    }


    protected SeekBar.OnSeekBarChangeListener hueSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean bool) {
            Log.d(Constants.LOG_TAG, "onHueChanged");
            h = i;
            updateColor();

            // update the thumb
            Drawable thumb = seekBar.getThumb();
            thumb.setColorFilter(Color.HSVToColor(new float[] {h,s, v}), PorterDuff.Mode.SRC_IN);

            // update the saturation
            LinearGradient linearGradient = new LinearGradient(0.f, 0.f, seekBarSaturation.getWidth(), 0.0f,
                    Color.BLACK, Color.HSVToColor(new float[] {h, 100, 100}), Shader.TileMode.CLAMP);
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
            s = i;
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
        builder.setMessage(getString(R.string.choose_color)).setView(view);
        builder.setPositiveButton(R.string.set_color, this);

        frameFinalColor = (FrameLayout) view.findViewById(R.id.frame_final_color);
        seekBarHue = (SeekBar) view.findViewById(R.id.seek_hue);
        seekBarSaturation = (SeekBar) view.findViewById(R.id.seek_saturation);
        seekBarValue = (SeekBar) view.findViewById(R.id.seek_value);
        seekBarHue.setOnSeekBarChangeListener(hueSeekBarChangeListener);
        seekBarSaturation.setOnSeekBarChangeListener(saturationSeekBarChangeListener);
        seekBarValue.setOnSeekBarChangeListener(valueSeekBarChangeListener);

        seekBarSaturation.setProgress(99);
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
                        Color.WHITE, Color.HSVToColor(new float[] {h,s, v}), Shader.TileMode.CLAMP);
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


    @Override
    public void onClick(View view) {
        // TODO -
    }

}
