package org.cmucreatelab.flutter_android.classes;

/**
 * Created by mike on 1/5/17.
 */

public class FlutterMessage {

    private String request;
    private int numberOfAttemptedSends = 0;

    // getters
    public String getRequest() { return request; }
    public int getNumberOfAttemptedSends() { return numberOfAttemptedSends; }
    // setters
    public void setNumberOfAttemptedSends(int numberOfAttemptedSends) { this.numberOfAttemptedSends = numberOfAttemptedSends; }


    public FlutterMessage(String request) {
        this.request = request;
    }

}
