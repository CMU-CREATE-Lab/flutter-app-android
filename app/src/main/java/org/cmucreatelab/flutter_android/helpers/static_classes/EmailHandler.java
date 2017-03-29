package org.cmucreatelab.flutter_android.helpers.static_classes;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v4.content.FileProvider;
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
            Uri uri = FileProvider.getUriForFile(activity, "org.cmucreatelab.flutter_android.fileprovider", currentDataLog);
            Intent intent = new Intent();

            intent.setAction(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{ email });
            intent.putExtra(Intent.EXTRA_SUBJECT, Constants.EMAIL_SUBJECT);
            intent.putExtra(Intent.EXTRA_TEXT, message);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            List<ResolveInfo> resolveInfos = activity.getPackageManager().queryIntentActivities(intent, 0);
            if (resolveInfos.size() == 0) {
                new AlertDialog.Builder(activity)
                        .setMessage(R.string.no_mail_app)
                        .setPositiveButton(R.string.ok, null)
                        .show();
            } else {
                String packageName = resolveInfos.get(0).activityInfo.packageName;
                String name = resolveInfos.get(0).activityInfo.name;
                intent.setComponent(new ComponentName(packageName, name));
                activity.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                activity.startActivity(intent);
            }
        }
    }

}
