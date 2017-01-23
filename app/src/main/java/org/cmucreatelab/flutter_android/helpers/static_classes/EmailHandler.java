package org.cmucreatelab.flutter_android.helpers.static_classes;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.cmucreatelab.flutter_android.R;

import java.io.File;
import java.util.List;

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
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
            intent.putExtra(Intent.EXTRA_SUBJECT, Constants.EMAIL_SUBJECT);
            intent.putExtra(Intent.EXTRA_TEXT, message);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(currentDataLog));

            List<ResolveInfo> resolveInfos = activity.getPackageManager().queryIntentActivities(intent, 0);
            if (resolveInfos.size() == 0) {
                new AlertDialog.Builder(activity)
                        .setMessage(R.string.no_mail_app)
                        .setPositiveButton(R.string.ok, null)
                        .show();
            } else {
                String packageName = resolveInfos.get(0).activityInfo.packageName;
                String name = resolveInfos.get(0).activityInfo.name;

                intent.setAction(Intent.ACTION_SEND);
                intent.setComponent(new ComponentName(packageName, name));

                activity.startActivity(intent);
            }
        } else {
            // TODO - alert user to select a data log
        }
    }

}
