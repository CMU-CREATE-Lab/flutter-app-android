package org.cmucreatelab.flutter_android.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Steve on 3/14/2017.
 */

public class PositionTextView extends TextView {

    private int position;
    private boolean isVisible;


    private void init(Context context) {
        this.position = 0;
        this.isVisible = false;
    }


    public PositionTextView(Context context) {
        super(context);
        init(context);
    }


    public PositionTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public PositionTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    // getters


    public int getPosition() { return this.position; }
    public boolean isVisible() { return this.isVisible; }


    // setters


    public void setPosition(int position) {
        if (position >= 0)
            this.position = position;
    }
    public void setVisible(boolean isVisible) { this.isVisible = isVisible; }
}
