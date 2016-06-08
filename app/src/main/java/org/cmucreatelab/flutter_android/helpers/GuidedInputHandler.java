package org.cmucreatelab.flutter_android.helpers;

import android.app.Activity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.helpers.guided_input.OptionsNode;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.util.ArrayList;

/**
 * Created by Steve on 6/7/2016.
 */
public class GuidedInputHandler {


    public static final String PARENT_PROMPT = "parent";

    private OptionsNode finger;


    private void updateFinger(OptionsNode node) {
        finger = node;
    }


    private void displayOptions(Activity activity, LinearLayout container, TextView title) {
        container.removeAllViews();
        title.setText(finger.getTitle());
        for (String option : finger.getOptions()) {
            TextView textView = (TextView) activity.getLayoutInflater().inflate(R.layout.base_guided_input, null);
            textView.setText(option);
            container.addView(textView);
        }
    }


    public GuidedInputHandler(OptionsNode node) {
        finger = node;
    }


    public boolean choosePrompt(Activity activity, String input, LinearLayout container, TextView title) {
        boolean result = false;

        Log.d(Constants.LOG_TAG, "from input: " + input);
        if (input.equals(PARENT_PROMPT)) {
            if (finger.getParent() != null){
                updateFinger((OptionsNode) finger.getParent());
            }
            displayOptions(activity, container, title);
        }

        if (input.length() > 0 && !input.equals(PARENT_PROMPT)) {
            ArrayList<String> options = finger.getOptions();
            for (int i = 0; i < options.size(); i++) {
                String s = options.get(i);
                Log.d(Constants.LOG_TAG, s);
                s = s.substring(1,2);
                Log.d(Constants.LOG_TAG, "from options: " + s);
                if (input.equals(s)) {
                    // we have a match
                    result = true;
                    updateFinger((OptionsNode) finger.getChild(i));
                    displayOptions(activity, container, title);
                }
            }
        }

        return result;
    }


}
