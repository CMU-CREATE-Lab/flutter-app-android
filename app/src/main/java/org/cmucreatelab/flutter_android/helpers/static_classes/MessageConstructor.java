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
public class MessageConstructor {


    public static final String READ_SENSOR = "r";


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
        String inputMax = Integer.toHexString(settings.getAdvancedSettings().getInputMax());
        String inputMin = Integer.toHexString(settings.getAdvancedSettings().getInputMin());
        String outputMax = Integer.toHexString(settings.getOutputMax());
        String outputMin = Integer.toHexString(settings.getOutputMin());

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
