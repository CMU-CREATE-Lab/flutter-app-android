package org.cmucreatelab.flutter_android.classes.flutters;

/**
 * Created by Steve on 8/23/2016.
 *
 * FlutterMessageListener
 *
 * An interface for an activity that will be sending messages to and from the flutter.
 */
public interface FlutterMessageListener {
    public void onMessageReceived(final String output);
}
