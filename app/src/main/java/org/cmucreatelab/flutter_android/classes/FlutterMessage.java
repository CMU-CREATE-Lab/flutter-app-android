package org.cmucreatelab.flutter_android.classes;

/**
 * Created by mike on 1/5/17.
 */

public class FlutterMessage {

    private String request;
    // TODO @tasota this tracks how many times we tried sending this message; in the future we could make it better encapsulated
    public int numberOfAttemptedSends = 0;

    public String getRequest() { return request; }


    public FlutterMessage(String request) {
        this.request = request;
    }

}
