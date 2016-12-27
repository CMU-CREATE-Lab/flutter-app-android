package org.cmucreatelab.flutter_android.helpers;

import android.content.Context;

/**
 * Created by Steve on 5/26/2016.
 *
 * GlobalHandler
 *
 * A class that gives access to needed elements throughout various activities.
 *
 */
public class GlobalHandler {

    public Context appContext;
    public SessionHandler sessionHandler;


    // Singleton Implementation


    private static GlobalHandler classInstance;


    public static synchronized GlobalHandler getInstance(Context context) {
        if (classInstance == null) {
            classInstance = new GlobalHandler(context);
        }
        return classInstance;
    }


    private GlobalHandler(Context context) {
        this.appContext = context;
        this.sessionHandler = new SessionHandler();
    }

}
