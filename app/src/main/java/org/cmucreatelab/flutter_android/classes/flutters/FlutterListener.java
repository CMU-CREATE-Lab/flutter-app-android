package org.cmucreatelab.flutter_android.classes.flutters;

/**
 * Created by Steve on 5/31/2016.
 *
 * DeviceListener
 *
 * An interface that forms the structure of FlutterOG events.
 *
 */
public interface FlutterListener {
    public void onConnected(final boolean connected);
    public void onMessageSent(final String output);
}
