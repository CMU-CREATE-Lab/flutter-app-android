package org.cmucreatelab.flutter_android.ui.progressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

/**
 * Created by Steve on 2/20/2017.
 */

public class MeanMedianModeProgressBar extends ProgressBar {

    private static final int START_MIN = 6;
    private static final int END_MAX = 90;

    private int currentPosition;
    private Constants.MATH_STATES currentType;
    private Paint textPaint;
    private String currentText;

    private int maxPosition;
    private int minPosition;

    private String meanText;
    private String medianText;
    private String modeText;
    private String maxText;
    private String minText;
    private boolean isMax;
    private boolean isMin;


    private void drawIt(Canvas canvas, String text) {
        if (currentPosition < START_MIN)
            currentPosition = START_MIN;
        if (currentPosition > END_MAX)
            currentPosition = END_MAX;
        if (minPosition < START_MIN)
            minPosition = START_MIN;
        if (maxPosition > END_MAX)
            maxPosition = END_MAX;

        // mean, meadian or mode
        double position = currentPosition;
        double decimalNumber = position/99;

        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        double d = getWidth() * decimalNumber - bounds.centerX();
        int x = (int) Math.round(d);
        int y = getHeight() - 10 - bounds.centerY();
        canvas.drawText(text, x, y, textPaint);

        // max and min
        if (isMax) {
            position = maxPosition;
            decimalNumber = position / 99;
            bounds = new Rect();
            textPaint.getTextBounds(text, 0, text.length(), bounds);
            d = getWidth() * decimalNumber - bounds.centerX();
            x = (int) Math.round(d);
            y = getHeight() - 10 - bounds.centerY();
            canvas.drawText(maxText, x, y, textPaint);
        }
        if (isMin) {
            position = minPosition;
            decimalNumber = position / 99;
            bounds = new Rect();
            textPaint.getTextBounds(text, 0, text.length(), bounds);
            d = getWidth() * decimalNumber - bounds.centerX();
            x = (int) Math.round(d);
            y = getHeight() - 10 - bounds.centerY();
            canvas.drawText(minText, x, y, textPaint);
        }
    }


    public MeanMedianModeProgressBar(Context context) {
        super(context);
        textPaint = new Paint();
        textPaint.setColor(context.getResources().getColor(R.color.orange));
        currentType = Constants.MATH_STATES.NONE;
        currentText = "";
    }


    public MeanMedianModeProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        textPaint = new Paint();
        textPaint.setColor(context.getResources().getColor(R.color.orange));
        currentType = Constants.MATH_STATES.NONE;
        currentText = "";
    }


    public MeanMedianModeProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        textPaint = new Paint();
        textPaint.setColor(context.getResources().getColor(R.color.orange));
        currentType = Constants.MATH_STATES.NONE;
        currentText = "";
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawIt(canvas, currentText);
    }

    public void placeStringAtPosition(Constants.MATH_STATES type, int position) {
        currentType = type;
        switch (currentType) {
            case MEAN:
                meanText = "Mean: " + position;
                currentPosition = position;
                currentText = meanText;
                break;
            case MEDIAN:
                medianText = "Median: " + position;
                currentPosition = position;
                currentText = medianText;
                break;
            case MODE:
                modeText = "Mode: " + position;
                currentPosition = position;
                currentText = modeText;
                break;
            case MAX:
                maxText = "Max: " + position;
                maxPosition = position;
                isMax = true;
                break;
            case MIN:
                minText = "Min: " + position;
                minPosition = position;
                isMin = true;
                break;
        }
        invalidate();
    }

    public void removeString(Constants.MATH_STATES type, int position) {
        currentType = type;
        switch (type){
            case MEAN:
                meanText = "";
                currentPosition = position;
                currentText = meanText;
                break;
            case MEDIAN:
                medianText = "";
                currentPosition = position;
                currentText = medianText;
                break;
            case MODE:
                modeText = "";
                currentPosition = position;
                currentText = modeText;
                break;
            case MIN:
                minText = "";
                minPosition = position;
                isMin = false;
                break;
            case MAX:
                maxText = "";
                maxPosition = position;
                isMax = false;
                break;
        }
        invalidate();
    }

}
