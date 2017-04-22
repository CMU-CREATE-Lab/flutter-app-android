package org.cmucreatelab.flutter_android.ui.realtivelayout;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import org.cmucreatelab.flutter_android.classes.Stat;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.PositionTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Steve on 3/14/2017.
 */

public class StatsRelativeLayout extends RelativeLayout {

    private ArrayList<PositionTextView> views;

    private Context context;
    private StatsRelativeLayout instance;


    private int positionToPixels(int width, int position) {
        int result = 0;
        float temp = (float) position / 100;
        result = (int) (temp * width);
        Log.d(Constants.LOG_TAG, "Position - " + result);
        return result;
    }


    private boolean isViewOverlapping(View v1, View v2) {
        Rect R1=new Rect(v1.getLeft(), v1.getTop(), v1.getRight(), v1.getBottom());
        Rect R2=new Rect(v2.getLeft(), v2.getTop(), v2.getRight(), v2.getBottom());
        return R1.intersect(R2);
    }


    private void update() {
        this.removeAllViews();
        this.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);

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
            /*if (100 - view.getPosition() >= 90)
                params.addRule(RelativeLayout.ALIGN_PARENT_START);
            else if (100 - view.getPosition() <= 10)
                params.addRule(RelativeLayout.ALIGN_PARENT_END);
            else
                params.setMargins(positionToPixels(instance.getWidth(), view.getPosition()) - view.getWidth() / 2, 0, 0, 0);*/

            view.setVisibility(INVISIBLE);
            view.setLayoutParams(params);

            this.addView(view);
        }
    }

    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            getViewTreeObserver().removeOnGlobalLayoutListener(this);

            Collections.sort(views, new Comparator<PositionTextView>() {
                @Override
                public int compare(PositionTextView positionTextView, PositionTextView t1) {
                    return ((Integer)positionTextView.getPosition()).compareTo(t1.getPosition());
                }
            });

            for (int i = 0; i < views.size(); i++) {
                PositionTextView view = views.get(i);
                view.setVisibility(VISIBLE);
                RelativeLayout.LayoutParams params = (LayoutParams) view.getLayoutParams();

                if (100 - view.getPosition() >= 90) {
                    params.addRule(RelativeLayout.ALIGN_PARENT_START);
                } else if (100 - view.getPosition() <= 10) {
                    params.addRule(RelativeLayout.ALIGN_PARENT_END);
                } else {
                    params.setMargins(positionToPixels(instance.getWidth(), view.getPosition()) - view.getWidth() / 2, 0, 0, 0);
                    Log.d(Constants.LOG_TAG, "Width - " + view.getWidth());
                }

                view.setLayoutParams(params);

                if (i > 0) {
                    if (isViewOverlapping(view, views.get(i - 1))) {
                        params.addRule(RelativeLayout.BELOW, views.get(i - 1).getId());
                    }
                }

                view.setLayoutParams(params);
            }
        }
    };


    private void init(Context context) {
        views = new ArrayList<>();
        this.context = context;
        this.instance = this;
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


    public void add(Stat stat, int position) {
        stat.setVisible(true);
        stat.setPosition(position);
        stat.setText(stat.getStatName() + ": " + stat.getPositionTextView().getPosition());
        views.add(stat.getPositionTextView());
        update();
    }


    public void remove(Stat stat) {
        stat.setVisible(false);
        views.remove(stat.getPositionTextView());
        update();
    }
}
