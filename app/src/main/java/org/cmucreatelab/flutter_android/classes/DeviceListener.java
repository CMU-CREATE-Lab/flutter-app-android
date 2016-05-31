package org.cmucreatelab.flutter_android.classes;

/**
 * Created by Steve on 5/31/2016.
 */
public interface DeviceListener {
    public void onConnected(final boolean connected);
    public void onMessageSent(final String output);
}
