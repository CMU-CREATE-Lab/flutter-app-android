package org.cmucreatelab.flutter_android.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Steve on 3/20/2017.
 *
 * ExtendedHeightListView
 *
 *
 */
public class ExtendedHeightListView extends ListView {


    public ExtendedHeightListView(Context context) {
        super(context);
    }


    public ExtendedHeightListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public ExtendedHeightListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
