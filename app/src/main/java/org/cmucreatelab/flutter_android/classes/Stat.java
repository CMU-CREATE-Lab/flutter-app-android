package org.cmucreatelab.flutter_android.classes;

import android.content.Context;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.ui.PositionTextView;

/**
 * Created by Steve on 3/29/2017.
 *
 * Stat
 *
 * A class to represent statistics of the data set for the data logging tab.
 *
 */

public class Stat {

    private String statName;
    private PositionTextView positionTextView;

    public Stat(String statName, Context context) {
        this.statName = statName;
        positionTextView = new PositionTextView(context);
        positionTextView.setTextColor(context.getResources().getColor(R.color.orange));
    }

    // getters
    public String getStatName() { return statName; }
    public PositionTextView getPositionTextView() { return positionTextView; }

    // setters
    public void setText(String text) { positionTextView.setText(text); }
    public void setPosition(int position) { positionTextView.setPosition(position); }
    public void setVisible(boolean visible) {
        positionTextView.setVisible(visible);
    }
}
