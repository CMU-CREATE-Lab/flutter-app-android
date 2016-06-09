package org.cmucreatelab.flutter_android.helpers;

import android.content.Context;

import org.cmucreatelab.flutter_android.helpers.guided_input.OptionsXMLHandler;

/**
 * Created by Steve on 5/26/2016.
 */
public class GlobalHandler {


    private OptionsXMLHandler optionsXMLHandler;
    public Context appContext;
    public NamingHandler namingHandler;
    public SessionHandler sessionHandler;
    public GuidedInputHandler guidedInputHandler;


    // Singleton Implementation


    private static GlobalHandler classInstance;


    public static synchronized GlobalHandler newInstance(Context context) {
        if (classInstance == null) {
            classInstance = new GlobalHandler(context);
        }
        return classInstance;
    }


    private GlobalHandler(Context context) {
        this.appContext = context;
        this.sessionHandler = new SessionHandler();
        this.namingHandler = new NamingHandler(appContext);
        try {
            this.optionsXMLHandler = new OptionsXMLHandler(appContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.guidedInputHandler = new GuidedInputHandler(optionsXMLHandler.getStart());
    }

}
