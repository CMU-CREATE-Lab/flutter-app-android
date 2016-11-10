package org.cmucreatelab.flutter_android.helpers.static_classes;

import org.cmucreatelab.flutter_android.classes.outputs.Led;
import org.cmucreatelab.flutter_android.classes.outputs.Output;
import org.cmucreatelab.flutter_android.classes.settings.Settings;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;

/**
 * Created by Steve on 8/23/2016.
 *
 * MessageConstructor
 *
 * A static class which will generate the strings depending on the output passed into the appropriate methods
 *
 */
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
        hexBuilder.setLength(sizeOfIntInHalfBytes);
        for (int i = sizeOfIntInHalfBytes - 1; i >= 0; --i)
        {
            int j = dec & halfByte;
            hexBuilder.setCharAt(i, hexDigits[j]);
            dec >>= numberOfBitsInAHalfByte;
        }
        return hexBuilder.toString();
    }


    public static String getRemoveLinkMessage(Output output) {
        StringBuilder result = new StringBuilder();
        result.append("x" + output.getSettings().getType() + output.getPortNumber());
        return result.toString();
    }


    public static String getLinkedMessage(Output output) {
        StringBuilder result = new StringBuilder();
        Settings settings = output.getSettings();
        String servoNumber = String.valueOf(output.getPortNumber());

        Relationship.Type relationshipType = settings.getRelationship().getRelationshipType();
        String inputMax = decToHex(settings.getInputMax());
        String inputMin = decToHex(settings.getInputMin());
        String outputMax = decToHex(settings.getOutputMax());
        String outputMin = decToHex(settings.getOutputMin());

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

        result.append(settings.getType() + servoNumber + "," + outputMin + "," + outputMax + "," + String.valueOf(settings.getSensor().getPortNumber()) + "," + inputMin + "," + inputMax);

        return result.toString();
    }

}
