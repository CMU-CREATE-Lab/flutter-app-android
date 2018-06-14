package org.cmucreatelab.flutter_android.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;

import org.cmucreatelab.flutter_android.R;

/**
 * Created by Parv on 6/13/2018.
 */

public class ServoAngleDrawable extends Drawable {

    private Paint paint;
    private RectF rectF;
    private int color;
    private int minAngle, maxAngle;

    public ServoAngleDrawable(int color, int minAngle, int maxAngle, Activity activity) {
        this.color = color;
        this.minAngle = minAngle;
        this.maxAngle = maxAngle;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setColor(color, activity);
        paint.setStyle(Paint.Style.FILL);
        rectF = new RectF();
    }

    public void setColor(int color, Activity activity) {
        paint.setColor(activity.getResources().getColor(color));
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();

        Rect bounds = getBounds();
/*
        if(angle == Direction.LEFT || angle == Direction.RIGHT)
        {
            canvas.scale(2, 1);
            if(angle == Direction.RIGHT)
            {
                canvas.translate(-(bounds.right / 2), 0);
            }
        }
        else
        {
            canvas.scale(1, 2);
            if(angle == Direction.BOTTOM)
            {
                canvas.translate(0, -(bounds.bottom / 2));
            }
        }*/

        canvas.scale(1, 2);

        rectF.set(bounds);

/*        if (angle == Direction.LEFT)
            canvas.drawArc(rectF, 90, 180, true, paint);
        else if (angle == Direction.TOP)
            canvas.drawArc(rectF, -180, 180, true, paint);
        else if (angle == Direction.RIGHT)
            canvas.drawArc(rectF, 270, 180, true, paint);
        else if (angle == Direction.BOTTOM)
            canvas.drawArc(rectF, 0, 180, true, paint);*/

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