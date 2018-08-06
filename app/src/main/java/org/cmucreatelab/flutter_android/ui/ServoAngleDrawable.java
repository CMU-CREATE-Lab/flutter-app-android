package org.cmucreatelab.flutter_android.ui;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.RobotActivity;
import org.cmucreatelab.flutter_android.classes.Session;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.settings.Settings;
import org.cmucreatelab.flutter_android.classes.settings.SettingsAmplitude;
import org.cmucreatelab.flutter_android.classes.settings.SettingsChange;
import org.cmucreatelab.flutter_android.classes.settings.SettingsConstant;
import org.cmucreatelab.flutter_android.classes.settings.SettingsCumulative;
import org.cmucreatelab.flutter_android.classes.settings.SettingsFrequency;
import org.cmucreatelab.flutter_android.classes.settings.SettingsProportional;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

/**
 * Created by Parv on 6/13/2018.
 *
 * ServoAngleDrawable
 *
 * Represents the icon displayed for a Servo with varying angles. Also contains a static helper for refreshing views on RobotActivity
 */
public class ServoAngleDrawable extends Drawable {

    private Paint paint;
    private RectF rectF;
    private int minAngle, maxAngle;


    public ServoAngleDrawable(int color, int minAngle, int maxAngle, Activity activity) {
        this.minAngle = minAngle;
        this.maxAngle = maxAngle;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setPaintColor(color, activity);
        paint.setStyle(Paint.Style.FILL);
        rectF = new RectF();
    }


    private void setPaintColor(int color, Activity activity) {
        paint.setColor(activity.getResources().getColor(color));
    }


    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        Rect bounds = getBounds();
        canvas.scale(1, 2);
        rectF.set(bounds);
        canvas.drawArc(rectF, minAngle - 180, maxAngle - minAngle, true, paint);
    }


    @Override
    public void setAlpha(int alpha) {

    }


    @Override
    public void setColorFilter(ColorFilter c) {

    }


    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }
}