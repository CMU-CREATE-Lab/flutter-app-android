package org.cmucreatelab.flutter_android.ui.dialogs;

/**
 * Created by Steve on 1/17/2017.
 */
public class BaseDataLoggingDialog extends BaseResizableDialog {


    protected int timeToSeconds(String time) {
        int result = 0;

        if (time.equals("minute") || time.equals("minutes")) {
            result = 60;
        } else if(time.equals("hour") || time.equals("hours")) {
            result = 3600;
        } else if (time.equals("day") || time.equals("days")) {
            result = 86400;
        } else if(time.equals("week") || time.equals("weeks")) {
            result = 604800;
        }

        return result;
    }

}
