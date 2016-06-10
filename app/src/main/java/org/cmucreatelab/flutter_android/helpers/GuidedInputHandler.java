package org.cmucreatelab.flutter_android.helpers;

import android.app.Activity;
import android.text.Editable;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.util.ArrayList;

/**
 * Created by Steve on 6/7/2016.
 */
public class GuidedInputHandler {

    // TODO - when about to send...display the string you are going to send in the container

    private ArrayList<String> mTextMainPrompt;
    private ArrayList<String> mTextOutputPrompt;
    private ArrayList<String> mTextInputPrompt;
    private ArrayList<String> mTextInputValuePrompt;
    private TextView mTitle;
    private LinearLayout mContainer;
    private Activity mActivity;
    private String mResult;


    // Class methods


    private String getPreviousEntry() {
        return mResult.substring(mResult.length()-1, mResult.length());
    }


    private void showMainPrompt() {
        final GlobalHandler globalHandler = GlobalHandler.newInstance(mActivity);
        mContainer.removeAllViews();
        // TODO
        mTitle.setText("What would you like to do?");
        for (String s : mTextMainPrompt) {
            TextView textView = (TextView) mActivity.getLayoutInflater().inflate(R.layout.base_guided_input, null);
            textView.setText(s);
            mContainer.addView(textView);
        }
        globalHandler.appState.rootState = null;
        globalHandler.appState.currentState = GuidedInputStates.MAIN_PROMPT;
    }


    private void showOutputPrompt() {
        final GlobalHandler globalHandler = GlobalHandler.newInstance(mActivity);
        mContainer.removeAllViews();
        // TODO
        mTitle.setText("What type of output?");
        for (String s : mTextOutputPrompt) {
            TextView textView = (TextView) mActivity.getLayoutInflater().inflate(R.layout.base_guided_input, null);
            textView.setText(s);
            mContainer.addView(textView);
        }
        globalHandler.appState.currentState = GuidedInputStates.OUTPUT_PROMPT;
    }


    private void showInputPrompt() {
        final GlobalHandler globalHandler = GlobalHandler.newInstance(mActivity);
        mContainer.removeAllViews();
        // TODO
        mTitle.setText("Which input?");
    }


    private void showInputValuePrompt(String text) {
        final GlobalHandler globalHandler = GlobalHandler.newInstance(mActivity);
        mContainer.removeAllViews();
        // TODO
        mTitle.setText("What would you like to set the " + text + " to? (range 0-100");
    }


    private void showOutputValuePrompt(String text) {
        final GlobalHandler globalHandler = GlobalHandler.newInstance(mActivity);
        globalHandler.appState.currentState = GuidedInputStates.OUTPUT_VALUE_PROMPT;
        String type = mResult.substring(mResult.length()-2, mResult.length()-1);
        if (type.equals("1") || type.equals("2") || type.equals("3")) {
            type = mResult.substring(mResult.length()-3, mResult.length()-1);
        }

        TextView textView = (TextView) mActivity.getLayoutInflater().inflate(R.layout.base_guided_input, null);
        if (type.equals("v")) {
            mTitle.setText("What would you like to set the volume " + text + " to?");
            textView.setText("Range: 0-100");
        } else if (type.equals("f")) {
            mTitle.setText("What would you like to set the frequency " + text + " to?");
            textView.setText("Range: 0-20000");
        } else if (type.equals("s1") || type.equals("s2") || type.equals("s3")) {
            mTitle.setText("What would you like to set the servo " + text + " to?");
            textView.setText("Range: 0-180");
        } else if (type.equals("r1") || type.equals("g1") || type.equals("b1")
                || type.equals("r2") || type.equals("g2") || type.equals("b2")
                || type.equals("r3") || type.equals("g3") || type.equals("b3")) {
            mTitle.setText("What would you like to set the led " + text + " to?");
            textView.setText("Range: 0-100");
        }
        mContainer.removeAllViews();
        mContainer.addView(textView);
    }


    private void showWhichOnePrompt() {
        final  GlobalHandler globalHandler = GlobalHandler.newInstance(mActivity);
        globalHandler.appState.currentState = GuidedInputStates.WHICH_ONE;
        mContainer.removeAllViews();
        mTitle.setText("Which one?");
        TextView textView = (TextView) mActivity.getLayoutInflater().inflate(R.layout.base_guided_input, null);
        textView.setText("1, 2 or 3");
        mContainer.addView(textView);
    }


    public GuidedInputHandler(TextView title, LinearLayout container) {
        mTitle = title;
        mContainer = container;
        mTextMainPrompt = new ArrayList<>();
        mTextOutputPrompt = new ArrayList<>();
        mTextInputPrompt = new ArrayList<>();
        mTextInputValuePrompt = new ArrayList<>();
        mResult = "";

        // init main prompt
        String temp = "'r': read sensors";
        mTextMainPrompt.add(temp);
        temp = "'R': stream sensor readings for 10 seconds";
        mTextMainPrompt.add(temp);
        temp = "'s': set an output";
        mTextMainPrompt.add(temp);
        temp = "'p': set a proportional relationship";
        mTextMainPrompt.add(temp);
        temp = "'x': remove a relationship";
        mTextMainPrompt.add(temp);
        temp = "'X': remove relationships for all outputs";
        mTextMainPrompt.add(temp);

        // init output prompt
        temp = "'s': servo";
        mTextOutputPrompt.add(temp);
        temp = "'r': red led";
        mTextOutputPrompt.add(temp);
        temp = "'g': green led";
        mTextOutputPrompt.add(temp);
        temp = "'b': blue led";
        mTextOutputPrompt.add(temp);
        temp = "'v': volume of the buzzer";
        mTextOutputPrompt.add(temp);
        temp = "'f': frequency of the buzzer";
        mTextOutputPrompt.add(temp);

        // init input prompt
        temp = "1, 2 or 3";
        mTextInputPrompt.add(temp);

        // init input value prompt
        temp = "Range: 0-100";
        mTextInputValuePrompt.add(temp);
    }


    public void choosePrompt(Activity activity, Editable editable) {
        String entry = "";
        if (editable != null) {
             entry = editable.toString();
        }
        mActivity = activity;
        final GlobalHandler globalHandler = GlobalHandler.newInstance(activity);

        if (editable == null) {
            showMainPrompt();
        } else if (globalHandler.appState.rootState == null){
            if (entry.equals("r")) {
                mResult = mResult.concat(entry);
                globalHandler.appState.rootState = GuidedInputStates.READ_SENSORS;
                globalHandler.appState.currentState = GuidedInputStates.READY_TO_SEND;
                mTitle.setText("Lets read the sensors! Click 'Next'");
                mContainer.removeAllViews();
                mContainer.setVisibility(View.INVISIBLE);
            } else if (entry.equals("R")) {
                mResult = mResult.concat(entry);
                globalHandler.appState.rootState = GuidedInputStates.STREAM_SENSORS;
                globalHandler.appState.currentState = GuidedInputStates.READY_TO_SEND;
                mTitle.setText("Lets stream the sensors for 10 seconds! Click 'Next'");
                mContainer.removeAllViews();
                // TODO - add sensor readings somewhere
            } else if (entry.equals("s")) {
                mResult = mResult.concat(entry);
                globalHandler.appState.rootState = GuidedInputStates.SET_OUTPUT;
                showOutputPrompt();
            } else if (entry.equals("p")) {
                mResult = mResult.concat(entry);
                globalHandler.appState.rootState = GuidedInputStates.SET_PROPORTION;
                showOutputPrompt();
            } else if (entry.equals("x")) {
                mResult = mResult.concat(entry);
                globalHandler.appState.rootState = GuidedInputStates.REMOVE_RELATIONSHIP;
                showOutputPrompt();
            } else if (entry.equals("X")) {
                mResult = mResult.concat(entry);
                globalHandler.appState.rootState = GuidedInputStates.REMOVE_ALL_RELATIONSHIPS;
                globalHandler.appState.currentState = GuidedInputStates.READY_TO_SEND;
                mTitle.setText("Lets remove all of the relationships! Click 'Next'");
                mContainer.removeAllViews();
            } else {
                // TODO - handle wrong command
            }
        } else {
            String previousEntry = getPreviousEntry();
            GuidedInputStates current = globalHandler.appState.currentState;

            if (globalHandler.appState.rootState == GuidedInputStates.SET_OUTPUT) {
                if (current == GuidedInputStates.OUTPUT_PROMPT) {
                    if (entry.equals("s") || entry.equals("r") || entry.equals("g") || entry.equals("b")) {
                        mResult = mResult.concat(entry);
                        showWhichOnePrompt();
                    } else if (entry.equals("v")) {
                        mResult = mResult.concat(entry + ",");
                        InputFilter[] filters = new InputFilter[1];
                        filters[0] = new InputFilter.LengthFilter(3);
                        editable.setFilters(filters);
                        showOutputValuePrompt("value");
                    } else if(entry.equals("f")) {
                        mResult = mResult.concat(entry + ",");
                        InputFilter[] filters = new InputFilter[1];
                        filters[0] = new InputFilter.LengthFilter(5);
                        editable.setFilters(filters);
                        showOutputValuePrompt("value");
                    }
                } else if (current == GuidedInputStates.WHICH_ONE) {
                    if (entry.equals("1") || entry.equals("2") || entry.equals("3")) {
                        mResult = mResult.concat(entry + ",");
                        InputFilter[] filters = new InputFilter[1];
                        filters[0] = new InputFilter.LengthFilter(3);
                        editable.setFilters(filters);
                        showOutputValuePrompt("value");
                    }
                } else if (current == GuidedInputStates.OUTPUT_VALUE_PROMPT) {
                    mResult = mResult.concat(entry);
                    globalHandler.appState.currentState = GuidedInputStates.READY_TO_SEND;
                    mTitle.setText("Lets set the output! Click 'Next'");
                    mContainer.removeAllViews();
                    mContainer.setVisibility(View.INVISIBLE);
                }

            } else if (globalHandler.appState.rootState == GuidedInputStates.SET_PROPORTION) {
                // TODO - finish handling proportion.
                if (current == GuidedInputStates.OUTPUT_PROMPT) {
                    if (entry.equals("s") || entry.equals("r") || entry.equals("g") || entry.equals("b")) {
                        mResult = mResult.concat(entry);
                        showWhichOnePrompt();
                    } else if ((entry.equals("v") || entry.equals("f"))) {
                        // TODO - handle choice
                    }
                } else if (current == GuidedInputStates.WHICH_ONE) {
                    if (entry.equals("1") || entry.equals("2") || entry.equals("3")) {
                        mResult = mResult.concat(entry + ",");
                        showOutputValuePrompt("minimum");
                    }
                }
            } else if (globalHandler.appState.rootState == GuidedInputStates.REMOVE_RELATIONSHIP) {
                if (current == GuidedInputStates.OUTPUT_PROMPT) {
                    if (entry.equals("s") || entry.equals("r") || entry.equals("g") || entry.equals("b")) {
                        mResult = mResult.concat(entry);
                        showWhichOnePrompt();
                    } else if ((entry.equals("v") || entry.equals("f")) ) {
                        mResult = mResult.concat(entry);
                        globalHandler.appState.currentState = GuidedInputStates.READY_TO_SEND;
                        mTitle.setText("Lets remove the relationship! Click 'Next'");
                        mContainer.removeAllViews();
                        mContainer.setVisibility(View.INVISIBLE);
                    }
                } else if (current == GuidedInputStates.WHICH_ONE) {
                    if (entry.equals("1") || entry.equals("2") || entry.equals("3")) {
                        mResult = mResult.concat(entry);
                        globalHandler.appState.currentState = GuidedInputStates.READY_TO_SEND;
                        mTitle.setText("Lets remove the relationship! Click 'Next'");
                        mContainer.removeAllViews();
                        mContainer.setVisibility(View.INVISIBLE);
                    }
                }
            } else {
                // TODO - handle bad state
            }
        }
    }


    public String getFinalString() {
        return mResult;
    }

}
