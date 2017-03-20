package org.cmucreatelab.flutter_android.ui.realtivelayout;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.PositionTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Steve on 3/14/2017.
 */

public class StatsRelativeLayout extends RelativeLayout {

    private PositionTextView textMean;
    private PositionTextView textMedian;
    private PositionTextView textMode;
    private PositionTextView textMax;
    private PositionTextView textMin;

    private Context context;


    private int positionToPixels(int width, int position) {
        int result = 0;

        Log.d(Constants.LOG_TAG, String.valueOf(width));
        float temp = (float) position / 100;
        Log.d(Constants.LOG_TAG, String.valueOf(temp));
        result = (int) (temp * width);
        Log.d(Constants.LOG_TAG, String.valueOf(result));

        // A dumb hack to keep it in bounds
        if ((width - result) < 80) result = width - 80;
        if (result < 0) result = 0;
        Log.d(Constants.LOG_TAG, String.valueOf(result));
        return result;
    }


    private void update() {
        ArrayList<PositionTextView> positionTextViews = new ArrayList<>();
        this.removeAllViews();

        if (textMean.isVisible()) positionTextViews.add(textMean);
        if (textMedian.isVisible()) positionTextViews.add(textMedian);
        if (textMode.isVisible()) positionTextViews.add(textMode);
        if (textMax.isVisible()) positionTextViews.add(textMax);
        if (textMin.isVisible()) positionTextViews.add(textMin);

        Collections.sort(positionTextViews, new Comparator<PositionTextView>() {
            @Override
            public int compare(PositionTextView positionTextView, PositionTextView t1) {
                return ((Integer)positionTextView.getPosition()).compareTo(t1.getPosition());
            }
        });

        for (int i = 0; i < positionTextViews.size(); i++) {
            PositionTextView view = positionTextViews.get(i);
            view.setId(i+100);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            if (i == 0) {
                params.addRule(RelativeLayout.ALIGN_PARENT_START);
                params.setMargins(positionToPixels(this.getWidth(), view.getPosition()-5), 0, 0, 0);
            } else {
                if (view.getPosition() -  positionTextViews.get(i-1).getPosition() < 25) {
                    params.addRule(RelativeLayout.BELOW, positionTextViews.get(i-1).getId());
                    params.setMargins(positionToPixels(this.getWidth(), view.getPosition()-5), 0, 0, 0);
                } else {
                    params.addRule(RelativeLayout.ALIGN_PARENT_START);
                    params.setMargins(positionToPixels(this.getWidth(), view.getPosition()-5), 0, 0, 0);
                }
            }

            view.setLayoutParams(params);
            view.setPadding(2,2,2,2);
            this.addView(positionTextViews.get(i));
        }

        invalidate();
    }


    private void init(Context context) {
        textMean = new PositionTextView(context);
        textMedian = new PositionTextView(context);
        textMode = new PositionTextView(context);
        textMax = new PositionTextView(context);
        textMin = new PositionTextView(context);
        this.context = context;

        textMean.setTextColor(context.getResources().getColor(R.color.orange));
        textMedian.setTextColor(context.getResources().getColor(R.color.orange));
        textMode.setTextColor(context.getResources().getColor(R.color.orange));
        textMax.setTextColor(context.getResources().getColor(R.color.orange));
        textMin.setTextColor(context.getResources().getColor(R.color.orange));
    }


    public StatsRelativeLayout(Context context) {
        super(context);
        init(context);
    }


    public StatsRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public StatsRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    public void add(Constants.STATS type, int position) {
        switch (type) {
            case MEAN:
                textMean.setText("Mean: " + position);
                textMean.setPosition(position);
                textMean.setVisible(true);
                remove(Constants.STATS.MEDIAN);
                remove(Constants.STATS.MODE);
                break;
            case MEDIAN:
                textMedian.setText("Median: " + position);
                textMedian.setPosition(position);
                textMedian.setVisible(true);
                remove(Constants.STATS.MEAN);
                remove(Constants.STATS.MODE);
                break;
            case MODE:
                textMode.setText("Mode: " + position);
                textMode.setPosition(position);
                textMode.setVisible(true);
                remove(Constants.STATS.MEAN);
                remove(Constants.STATS.MEDIAN);
                break;
            case MAX:
                textMax.setText("Max: " + position);
                textMax.setPosition(position);
                textMax.setVisible(true);
                break;
            case MIN:
                textMin.setText("Min: " + position);
                textMin.setPosition(position);
                textMin.setVisible(true);
                break;
        }
        update();
    }


    public void remove(Constants.STATS type) {
        switch (type) {
            case MEAN:
                textMean.setText("");
                textMean.setPosition(0);
                textMean.setVisible(false);
                break;
            case MEDIAN:
                textMedian.setText("");
                textMedian.setPosition(0);
                textMedian.setVisible(false);
                break;
            case MODE:
                textMode.setText("");
                textMode.setPosition(0);
                textMode.setVisible(false);
                break;
            case MAX:
                textMax.setText("");
                textMax.setPosition(0);
                textMax.setVisible(false);
                break;
            case MIN:
                textMin.setText("");
                textMin.setPosition(0);
                textMin.setVisible(false);
                break;
        }
        update();
    }
}
