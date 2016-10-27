package org.cmucreatelab.flutter_android.helpers.static_classes;

import org.cmucreatelab.flutter_android.classes.Settings;
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
    private static final int sizeOfIntInHalfBytes = 2;
    private static final int numberOfBitsInAHalfByte = 4;
    private static final int halfByte = 0x0F;
    private static final char[] hexDigits = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'e'
    };

    public static String decToHex(int dec) {
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


    /*private static String percentToHex(Sensor sensor, int percent) {
        String result = "";
        int finalVal = 0;

        if (percent != 0) {
            int val = sensor.getMax();
            finalVal = val/percent;
        }

        result += decToHex(finalVal);
        result += "," + decToHex(finalVal);

        return result;
    }*/


    // TODO - get the output
    public static String getServoLinkMessage(Servo servo, String servoNumber) {
        StringBuilder result = new StringBuilder();
        Settings settings = servo.getSettings();

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
                result.append("ps");
                break;
            case SWITCH:
                break;
        }
        result.append(servoNumber + "," + outputMin + "," + outputMax + "," + String.valueOf(settings.getSensor().getPortNumber()) + "," + inputMin + "," + inputMax);

        return result.toString();
    }

}
