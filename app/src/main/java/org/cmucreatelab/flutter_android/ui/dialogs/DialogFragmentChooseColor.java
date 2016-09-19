package org.cmucreatelab.flutter_android.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

/**
 * Created by Steve on 9/7/2016.
 */
public abstract class DialogFragmentChooseColor extends DialogFragment implements View.OnClickListener, DialogInterface.OnClickListener {

    private int h,s,l;
    private FrameLayout frameFinalColor;
    private SeekBar seekBarHue;
    private SeekBar seekBarSaturation;
    private SeekBar seekBarLightness;


    private static float hueToRgb(float p, float q, float t) {
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


    private static int[] hslToRgb(float h, float s, float l) {
        float r,g,b;

        h = (h-0) / 359;
        s = (s-0) / 100;
        l = (l-0) / 100;

        if (s == 0f) {
            r = g = b = 1;
        } else {
            float q = l < 0.5f ? l * (1 + s) : l + s - l * s;
            float p = 2 * l - q;
            r = hueToRgb(p, q, h + 1f/3f);
            g = hueToRgb(p, q, h);
            b = hueToRgb(p, q, h - 1f/3f);
        }
        int[] result = {(int) (r * 255), (int) (g * 255), (int) (b * 255)};
        return result;
    }


    private void updateColor() {
        // TODO - update the color

        Drawable drawable = new Drawable() {
            private Paint paint;

            @Override
            public void draw(Canvas canvas) {
                paint = new Paint();
                float width = frameFinalColor.getWidth();
                float height = frameFinalColor.getHeight();
                float radius = width > height ? height/2 : width/2;
                float centerX = width/2;
                float centerY = height/2;

                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.BLACK);
                int[] rgb = hslToRgb(h,s,l);
                canvas.drawRGB(rgb[0], rgb[1], rgb[2]);
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
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            Log.d(Constants.LOG_TAG, "onProgressChanged");
            h = i;
            updateColor();
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


    protected SeekBar.OnSeekBarChangeListener lightnessSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            l = i;
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
        seekBarLightness = (SeekBar) view.findViewById(R.id.seek_lightness);
        seekBarHue.setOnSeekBarChangeListener(hueSeekBarChangeListener);
        seekBarSaturation.setOnSeekBarChangeListener(saturationSeekBarChangeListener);
        seekBarLightness.setOnSeekBarChangeListener(lightnessSeekBarChangeListener);

        h = 0;
        s = 0;
        l = 0;

        updateColor();

        return builder.create();
    }


    @Override
    public void onClick(View view) {
        // TODO -
    }

}
