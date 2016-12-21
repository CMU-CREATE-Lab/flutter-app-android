package org.cmucreatelab.flutter_android.helpers.static_classes;

import android.util.Log;

import org.cmucreatelab.flutter_android.classes.outputs.Output;
import org.cmucreatelab.flutter_android.classes.settings.Settings;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;

/**
 * Created by Steve on 8/23/2016.
 *
 * MessageConstructor
 *
 * A static class which will generate the strings depending on the output passed into the appropriate methods
 *
 */

// TODO - I may get rid of this class and make handlers for this kind of thing.
    // Similar to how the DataLoggingHandler is.
public class MessageConstructor {


    public static final String READ_SENSOR = "r";
    public static final String REMOVE_ALL_LINKS = "X";

    private static final int sizeOfIntInHalfBytes = 2;
    private static final int numberOfBitsInAHalfByte = 4;
    private static final int halfByte = 0x0F;
    private static final char[] hexDigits = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'e'
    };

    private static String decToHex(int dec) {
        StringBuilder hexBuilder = new StringBuilder(sizeOfIntInHalfBytes);

        hexBuilder.append(Integer.toHexString(dec));
        if (hexBuilder.length() > 2) {
            hexBuilder.replace(1,2,"");
        }

        return hexBuilder.toString();
    }


    public static String getRemoveLinkMessage(Output output) {
        StringBuilder result = new StringBuilder();
        if (output.getOutputType() != Output.Type.PITCH && output.getOutputType() != Output.Type.VOLUME) {
            result.append("x" + output.getSettings().getType() + output.getPortNumber());
        } else {
            result.append("x" + output.getSettings().getType());
        }
        return result.toString();
    }


    public static String getLinkedMessage(Output output) {
        StringBuilder result = new StringBuilder();
        Settings settings = output.getSettings();
        String portNumber = String.valueOf(output.getPortNumber());

        if (portNumber.equals("0")) {
            portNumber = "";
        }

        Relationship.Type relationshipType = settings.getRelationship().getRelationshipType();
        String inputMax = decToHex(settings.getAdvancedSettings().getInputMax());
        String inputMin = decToHex(settings.getAdvancedSettings().getInputMin());
        String outputMax = decToHex(settings.getOutputMax());
        String outputMin = decToHex(settings.getOutputMin());
        Log.d(Constants.LOG_TAG, outputMax);
        Log.d(Constants.LOG_TAG, String.valueOf(settings.getOutputMax()));
        Log.d(Constants.LOG_TAG, outputMin);
        Log.d(Constants.LOG_TAG, String.valueOf(settings.getOutputMin()));

        switch (relationshipType) {
            case AMPLITUDE:
                break;
            case CHANGE:
                break;
            case CONSTANT:
                break;
            case CUMULATIVE:
                break;
            case FREQUENCY:
                break;
            case NO_RELATIONSHIP:
                break;
            case PROPORTIONAL:
                result.append("p");
                break;
            case SWITCH:
                break;
        }

        result.append(settings.getType() + portNumber + "," + outputMin + "," + outputMax + "," + String.valueOf(settings.getSensor().getPortNumber()) + "," + inputMin + "," + inputMax);

        return result.toString();
    }

}
