package org.cmucreatelab.flutter_android.helpers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

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

    private GlobalHandler globalHandler;


    public EmailHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
    }


    // TODO - populate address correctly
    public void sendEmail(Activity activity, File file) {
        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.setData(Uri.parse("mailto: sf.fulton.steve@gmail.com"));
        i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        i.putExtra(Intent.EXTRA_SUBJECT, Constants.EMAIL_SUBJECT);
        i.putExtra(Intent.EXTRA_TEXT, globalHandler.sessionHandler.getFlutterName() + Constants.EMAIL_CONTENT);
        if (i.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(Intent.createChooser(i, "Send Email"));
        }
    }

}
