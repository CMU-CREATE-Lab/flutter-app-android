package org.cmucreatelab.flutter_android.ui;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

/**
 * Created by Parv on 6/13/2018.
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