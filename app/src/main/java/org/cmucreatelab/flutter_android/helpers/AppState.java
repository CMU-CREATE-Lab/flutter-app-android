package org.cmucreatelab.flutter_android.helpers;

/**
 * Created by Steve on 6/10/2016.
 * The root state is important because that will determine what to do within the states
 * that will handle displaying a prompt.
 *
 * For example, the output_prompt can be the last thing you enter
 * if your root is 'set_output,' whereas in 'set_proportion,' the guide will continue moving on past the
 * output_prompt. So just knowing you are in the output_prompt is not good enough...
 */
public class AppState {


    public GuidedInputStates currentState;
    public GuidedInputStates rootState;


    public AppState(GuidedInputStates current, GuidedInputStates root) {
        currentState = current;
        rootState = root;
    }
}
