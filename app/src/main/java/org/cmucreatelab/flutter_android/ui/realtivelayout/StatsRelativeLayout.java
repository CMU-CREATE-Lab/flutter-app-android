package org.cmucreatelab.flutter_android.ui.realtivelayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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

    private ArrayList<PositionTextView> views;

    private Context context;
    private StatsRelativeLayout instance;


    private int positionToPixels(int width, int position) {
        int result = 0;
        float temp = (float) position / 100;
        result = (int) (temp * width);
        return result;
    }


    private boolean isViewOverlapping(View v1, View v2) {
        return (v1.getTop() >= v2.getTop() &&
                v1.getLeft() >= v2.getLeft() &&
                v1.getRight() <= v2.getRight() &&
                v1.getBottom() <= v2.getBottom());
    }


    private void update() {
        this.removeAllViews();
        views = new ArrayList<>();

        if (textMean.isVisible()) views.add(textMean);
        if (textMedian.isVisible()) views.add(textMedian);
        if (textMode.isVisible()) views.add(textMode);
        if (textMax.isVisible()) views.add(textMax);
        if (textMin.isVisible()) views.add(textMin);

        Collections.sort(views, new Comparator<PositionTextView>() {
            @Override
            public int compare(PositionTextView positionTextView, PositionTextView t1) {
                return ((Integer)positionTextView.getPosition()).compareTo(t1.getPosition());
            }
        });

        for (int i = 0; i < views.size(); i++) {
            PositionTextView view = views.get(i);
            view.setId(i+100);
            view.setPadding(2,2,2,2);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (100 - view.getPosition() >= 90)
                params.addRule(RelativeLayout.ALIGN_PARENT_START);
            else if (100 - view.getPosition() <= 10)
                params.addRule(RelativeLayout.ALIGN_PARENT_END);
            else
                params.setMargins(positionToPixels(instance.getWidth(), view.getPosition()) - view.getWidth() / 2, 0, 0, 0);

            view.setLayoutParams(params);

            this.addView(view);
        }

        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                for (int i = 0; i < views.size(); i++) {
                    PositionTextView view = views.get(i);
                    RelativeLayout.LayoutParams params = (LayoutParams) view.getLayoutParams();

                    if (i > 0) {
                        if (isViewOverlapping(view, views.get(i-1))) {
                            params.addRule(RelativeLayout.BELOW, views.get(i - 1).getId());
                        }
                    }

                    view.setLayoutParams(params);
                }
                instance.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }


    private void init(Context context) {
        textMean = new PositionTextView(context);
        textMedian = new PositionTextView(context);
        textMode = new PositionTextView(context);
        textMax = new PositionTextView(context);
        textMin = new PositionTextView(context);
        views = new ArrayList<>();
        this.context = context;
        this.instance = this;

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
