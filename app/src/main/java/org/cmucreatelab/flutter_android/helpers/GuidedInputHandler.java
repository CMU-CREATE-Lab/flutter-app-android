package org.cmucreatelab.flutter_android.helpers;

import android.app.Activity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
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
    private String outputType;


    // Class methods


    private String decToHex(String in) {
        String result = "";

        Integer dec = Integer.valueOf(in);
        StringBuilder hexBuilder = new StringBuilder(8);
        hexBuilder.setLength(8);
        for (int i = 8 - 1; i >= 0; --i)
        {
            int j = dec & 0x0f;
            hexBuilder.setCharAt(i, Constants.HEX_ALPHABET[j]);
            dec >>= 4;
        }

        for (char c : hexBuilder.toString().toCharArray()) {
            if (c != '0') {
                result = result.concat(Character.toString(c));
            }
        }
        if (result.length() == 0) {
            result = "0";
        }
        return result;
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
        mTitle.setText("Which input?");
        TextView textView = (TextView) mActivity.getLayoutInflater().inflate(R.layout.base_guided_input, null);
        textView.setText("1, 2 or 3");
        mContainer.addView(textView);
        globalHandler.appState.currentState = GuidedInputStates.INPUT_PROMPT;
    }


    private void showInputValuePrompt(String text) {
        final GlobalHandler globalHandler = GlobalHandler.newInstance(mActivity);
        mContainer.removeAllViews();
        mTitle.setText("What would you like to set the " + text + " to?");
        TextView textView = (TextView) mActivity.getLayoutInflater().inflate(R.layout.base_guided_input, null);
        textView.setText("Range: 0-100");
        mContainer.addView(textView);
        globalHandler.appState.currentState = GuidedInputStates.INPUT_VALUE_PROMPT;
    }


    private void showOutputValuePrompt(String text) {
        if (proportionalOutputCount == 0) {
            final GlobalHandler globalHandler = GlobalHandler.newInstance(mActivity);
            globalHandler.appState.currentState = GuidedInputStates.OUTPUT_VALUE_PROMPT;
            outputType = mResult.substring(mResult.length()-2, mResult.length()-1);
            if (outputType.equals("1") || outputType.equals("2") || outputType.equals("3")) {
                outputType = mResult.substring(mResult.length()-3, mResult.length()-1);
            }
        }

        TextView textView = (TextView) mActivity.getLayoutInflater().inflate(R.layout.base_guided_input, null);
        if (outputType.equals("v")) {
            mTitle.setText("What would you like to set the volume " + text + " to?");
            textView.setText("Range: 0-100");
        } else if (outputType.equals("f")) {
            mTitle.setText("What would you like to set the frequency " + text + " to?");
            textView.setText("Range: 0-20000");
        } else if (outputType.equals("s1") || outputType.equals("s2") || outputType.equals("s3")) {
            mTitle.setText("What would you like to set the servo " + text + " to?");
            textView.setText("Range: 0-180");
        } else if (outputType.equals("r1") || outputType.equals("g1") || outputType.equals("b1")
                || outputType.equals("r2") || outputType.equals("g2") || outputType.equals("b2")
                || outputType.equals("r3") || outputType.equals("g3") || outputType.equals("b3")) {
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
        outputType = "";

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


    private InputFilter[] changeMaxCharLength(int length) {
        InputFilter[] result = new InputFilter[1];
        result[0] = new InputFilter.LengthFilter(length);
        return result;
    }


    private InputFilter[] onlyNumericInput(int length) {
        InputFilter numbers = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isDigit(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        InputFilter l = new InputFilter.LengthFilter(length);

        return new InputFilter[]{numbers, l};
    }


    private int proportionalOutputCount = 0;
    private int proportionalInputCount = 0;
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
                mContainer.setVisibility(View.INVISIBLE);
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
                mContainer.setVisibility(View.INVISIBLE);
            } else {
                // TODO - handle wrong command
            }
        } else {
            GuidedInputStates current = globalHandler.appState.currentState;

            if (globalHandler.appState.rootState == GuidedInputStates.SET_OUTPUT) {
                if (current == GuidedInputStates.OUTPUT_PROMPT) {
                    if (entry.equals("s") || entry.equals("r") || entry.equals("g") || entry.equals("b")) {
                        mResult = mResult.concat(entry);
                        editable.setFilters(onlyNumericInput(1));
                        showWhichOnePrompt();
                    } else if (entry.equals("v")) {
                        mResult = mResult.concat(entry + ",");
                        editable.setFilters(changeMaxCharLength(3));
                        showOutputValuePrompt("value");
                    } else if(entry.equals("f")) {
                        mResult = mResult.concat(entry + ",");
                        editable.setFilters(changeMaxCharLength(5));
                        showOutputValuePrompt("value");
                    }
                } else if (current == GuidedInputStates.WHICH_ONE) {
                    if (entry.equals("1") || entry.equals("2") || entry.equals("3")) {
                        mResult = mResult.concat(entry + ",");
                        editable.setFilters(onlyNumericInput(3));
                        showOutputValuePrompt("value");
                    }
                } else if (current == GuidedInputStates.OUTPUT_VALUE_PROMPT) {
                    // TODO - may need to convert decimal to hexadecimal
                    // TODO - check to make sure they entered numbers in
                    String hexVal = decToHex(entry);
                    Log.d(Constants.LOG_TAG, hexVal);
                    mResult = mResult.concat(hexVal);
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
                        editable.setFilters(onlyNumericInput(1));
                        showWhichOnePrompt();
                    }  else if (entry.equals("v")) {
                        mResult = mResult.concat(entry + ",");
                        editable.setFilters(changeMaxCharLength(3));
                        showOutputValuePrompt("minimum");
                    } else if(entry.equals("f")) {
                        mResult = mResult.concat(entry + ",");
                        editable.setFilters(changeMaxCharLength(5));
                        showOutputValuePrompt("minimum");
                    }
                } else if (current == GuidedInputStates.WHICH_ONE) {
                    if (entry.equals("1") || entry.equals("2") || entry.equals("3")) {
                        mResult = mResult.concat(entry + ",");
                        editable.setFilters(onlyNumericInput(3));
                        showOutputValuePrompt("minimum");
                    }
                } else if (current == GuidedInputStates.OUTPUT_VALUE_PROMPT) {
                    // TODO - may need to convert decimal to hexadecimal
                    if (proportionalOutputCount == 0) {
                        proportionalOutputCount++;
                        String test = mResult.substring(mResult.length()-2, mResult.length()-1);
                        String extraCharacter = mResult.substring(mResult.length()-3, mResult.length()-1);
                        Integer numTest = Integer.valueOf(entry);
                        if (test.equals("v") || extraCharacter.equals("r1") || extraCharacter.equals("r2") || extraCharacter.equals("r3")) {
                            if (numTest >= 0 && numTest <= 100) {
                                String hexVal = decToHex(entry);
                                Log.d(Constants.LOG_TAG, hexVal);
                                mResult = mResult.concat(hexVal + ",");
                                showOutputValuePrompt("maximum");
                            }
                        } else if (test.equals("f")) {
                            if (numTest >= 0 && numTest <= 20000) {
                                String hexVal = decToHex(entry);
                                Log.d(Constants.LOG_TAG, hexVal);
                                mResult = mResult.concat(hexVal + ",");
                                showOutputValuePrompt("maximum");
                            }
                        } else if (extraCharacter.equals("s1") || extraCharacter.equals("s2") || extraCharacter.equals("s3")){
                            if (numTest >= 0 && numTest <= 180) {
                                String hexVal = decToHex(entry);
                                Log.d(Constants.LOG_TAG, hexVal);
                                mResult = mResult.concat(hexVal + ",");
                                showOutputValuePrompt("maximum");
                            }
                        } else {
                            // TODO - incorrect input
                        }
                    } else {
                        Integer numTest = Integer.valueOf(entry);
                        if (outputType.equals("v") || outputType.equals("r1") || outputType.equals("r2") || outputType.equals("r3")) {
                            if (numTest >= 0 && numTest <= 100) {
                                String hexVal = decToHex(entry);
                                Log.d(Constants.LOG_TAG, hexVal);
                                mResult = mResult.concat(hexVal + ",");
                                editable.setFilters(onlyNumericInput(1));
                                showInputPrompt();
                            }
                        } else if (outputType.equals("f")) {
                            if (numTest >= 0 && numTest <= 20000) {
                                String hexVal = decToHex(entry);
                                Log.d(Constants.LOG_TAG, hexVal);
                                mResult = mResult.concat(hexVal + ",");
                                editable.setFilters(onlyNumericInput(1));
                                showInputPrompt();
                            }
                        } else if (outputType.equals("s1") || outputType.equals("s2") || outputType.equals("s3")){
                            if (numTest >= 0 && numTest <= 180) {
                                String hexVal = decToHex(entry);
                                Log.d(Constants.LOG_TAG, hexVal);
                                mResult = mResult.concat(hexVal + ",");
                                editable.setFilters(onlyNumericInput(1));
                                showInputPrompt();
                            }
                        } else {
                            // TODO - incorrect input
                        }
                    }
                } else if (current == GuidedInputStates.INPUT_PROMPT) {
                    if (entry.equals("1") || entry.equals("2") || entry.equals("3")) {
                        mResult = mResult.concat(entry + ",");
                        editable.setFilters(onlyNumericInput(3));
                        showInputValuePrompt("minimum");
                    }
                } else if (current == GuidedInputStates.INPUT_VALUE_PROMPT) {
                    if (proportionalInputCount == 0) {
                        proportionalInputCount++;
                        Integer test = Integer.valueOf(entry);
                        if (test >= 0 && test <= 100) {
                            String hexVal = decToHex(entry);
                            Log.d(Constants.LOG_TAG, hexVal);
                            mResult = mResult.concat(hexVal + ",");
                            showInputValuePrompt("maximum");
                        }
                    } else {
                        Integer test = Integer.valueOf(entry);
                        if (test >= 0 && test <= 100) {
                            String hexVal = decToHex(entry);
                            Log.d(Constants.LOG_TAG, hexVal);
                            mResult = mResult.concat(hexVal);
                            globalHandler.appState.currentState = GuidedInputStates.READY_TO_SEND;
                            mTitle.setText("Lets set the relationship! Click 'Next'");
                            mContainer.removeAllViews();
                            mContainer.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            } else if (globalHandler.appState.rootState == GuidedInputStates.REMOVE_RELATIONSHIP) {
                if (current == GuidedInputStates.OUTPUT_PROMPT) {
                    if (entry.equals("s") || entry.equals("r") || entry.equals("g") || entry.equals("b")) {
                        mResult = mResult.concat(entry);
                        editable.setFilters(onlyNumericInput(1));
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
