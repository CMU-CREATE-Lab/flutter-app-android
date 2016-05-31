package org.cmucreatelab.flutter_android.helpers;

import android.content.Context;

/**
 * Created by Steve on 5/26/2016.
 */
public class GlobalHandler {

    private static GlobalHandler classInstance;
    private Context appContext;


    public static GlobalHandler newInstance(Context context) {
        if (classInstance == null) {
            classInstance = new GlobalHandler(context);
        }
        return classInstance;
    }


    private GlobalHandler(Context context) {
        this.appContext = context;
    }

}
