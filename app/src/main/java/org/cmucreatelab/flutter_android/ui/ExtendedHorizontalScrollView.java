package org.cmucreatelab.flutter_android.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * Created by Paul on 4/22/2017.
 */

public class ExtendedHorizontalScrollView extends HorizontalScrollView {

    public OnScrollChangedListener mOnScrollChangedListener;

    public ExtendedHorizontalScrollView(Context context) {
        super(context);
    }

    public ExtendedHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExtendedHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (mOnScrollChangedListener != null) {
            mOnScrollChangedListener.onScrollChanged(l, t, oldl, oldt);
        }
    }

    public void setOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener){
        this.mOnScrollChangedListener = onScrollChangedListener;
    }

    public interface OnScrollChangedListener{
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }

}
