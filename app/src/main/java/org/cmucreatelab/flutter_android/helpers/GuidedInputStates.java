package org.cmucreatelab.flutter_android.helpers;

/**
 * Created by Steve on 6/10/2016.
 */
// TODO - scrap this eventually
public enum GuidedInputStates {
    // chooses what to actually display
    MAIN_PROMPT,
    OUTPUT_PROMPT,
    INPUT_PROMPT,
    INPUT_VALUE_PROMPT,
    OUTPUT_VALUE_PROMPT,
    WHICH_ONE,

    // roots
    READ_SENSORS, STREAM_SENSORS, SET_OUTPUT, SET_PROPORTION, REMOVE_RELATIONSHIP, REMOVE_ALL_RELATIONSHIPS,

    READY_TO_SEND
}
