package org.cmucreatelab.flutter_android.classes.flutters;

/**
 * Created by Steve on 8/23/2016.
 *
 * FlutterConnectListener
 *
 * An interface for an activity that will be connecting or disconnecting from a flutter.
 */
public interface FlutterConnectListener {
    public void onFlutterConnected(final boolean connected);
}
