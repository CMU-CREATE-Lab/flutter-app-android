package org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.servo;

import android.os.Bundle;

import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;

import java.io.Serializable;

/*
* Created by Mohit on 6/15/18
*
* Used to save information in the servoDialog.
*/

public class ServoUpdatedWithWizard implements Serializable {
    private static Bundle args = new Bundle();
    public ServoUpdatedWithWizard() {

    }

    public static void add(String string, Relationship relationship, Sensor sensor,
                           int minPosition, int maxPosition) {
       if (relationship != null) {
           args.putSerializable(string, relationship);
       }
       else if (sensor != null) {
           args.putSerializable(string, sensor);
       }
       else if (minPosition != 9999) {
           args.putSerializable(string, minPosition);
       }
       else if (maxPosition != 9999) {
           args.putSerializable(string, maxPosition);
       }
    }

    public static Relationship getRelationship(String string) {
        return (Relationship) args.getSerializable(string);
    }

    public static Sensor getSensor(String string) {
        return (Sensor) args.getSerializable(string);
    }

    // gets the min and max position
    public static int getPosition(String string) {
        return (int) args.getSerializable(string);
    }
}
