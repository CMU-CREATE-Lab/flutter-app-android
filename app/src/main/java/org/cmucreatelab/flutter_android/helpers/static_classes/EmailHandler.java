package org.cmucreatelab.flutter_android.helpers.static_classes;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * Created by Steve on 8/16/2016.
 *
 * EmailHandler
 *
 * A class that handles the process of sending an email.
 *
 */
public class EmailHandler {


    public static void sendEmail(Activity activity, String email, String message, File currentDataLog) {
        if (currentDataLog != null) {
            Intent i = new Intent(Intent.ACTION_SENDTO);
            i.setData(Uri.parse("mailto: " + email));
            i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(currentDataLog));
            i.putExtra(Intent.EXTRA_SUBJECT, Constants.EMAIL_SUBJECT);
            i.putExtra(Intent.EXTRA_TEXT, message);
            if (i.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivity(Intent.createChooser(i, "Send Email"));
            }
        } else {
            // TODO - alert user to select a data log
        }
    }

}
