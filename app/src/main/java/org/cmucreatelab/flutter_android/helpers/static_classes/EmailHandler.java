package org.cmucreatelab.flutter_android.helpers.static_classes;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.opencsv.CSVReader;

import org.cmucreatelab.android.volleycreatelab.StringFormRequest;
import org.cmucreatelab.flutter_android.R;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Steve on 8/16/2016.
 *
 * EmailHandler
 *
 * A class that handles the process of sending an email.
 *
 */
public class EmailHandler {


    private static ArrayList<String[]> readCsv(File file) throws IOException {
        CSVReader csvReader = null;
        csvReader = new CSVReader(new FileReader(file.getPath()));
        ArrayList<String[]> list = (ArrayList<String[]>) csvReader.readAll();
        csvReader.close();

        return list;
    }


    private static String convertCsvFileToTabDelimitedString(File file) throws IOException {
        String result,tmp;
        result = "";
        ArrayList<String[]> list = readCsv(file);

        for (String[] line: list) {
            tmp = "";
            if (line.length > 0) {
                for (int i = 0; i < line.length; i++) {
                    tmp = tmp.concat(line[i] + ",");
                }
                tmp = tmp.substring(0, tmp.length() - 2);
                result = result.concat(tmp);
                result = result.concat("\t");
            }
        }
        if (list.size() > 0) {
            result = result.substring(0, result.length() - 2);
        }

        return result;
    }


    public static void sendEmailIntent(Activity activity, String email, String message, File currentDataLog) {
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


    public static void sendEmailServer(Activity activity, String email, String message, File dataLog, String flutterName) {
        // TODO @tasota check for internet connection?
        int method = Request.Method.POST;
        String url = "http://fluttermail.cmucreatelab.org/";
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // TODO @tasota email successful
                Log.d(Constants.LOG_TAG, "sendEmailServer.onResponse");
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO @tasota error message dialog
                Log.d(Constants.LOG_TAG, "sendEmailServer.onErrorResponse");
            }
        };

        try {
            StringFormRequest httpRequest = new StringFormRequest(method,url,listener,errorListener);
            Map<String, String> params = httpRequest.getParams();
            params.put("flutter_name", flutterName);
            params.put("to", email);
            params.put("body", message);
            params.put("data", convertCsvFileToTabDelimitedString(dataLog));
            Volley.newRequestQueue(activity).add(httpRequest);
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG, "sendEmailServer caught exception:");
            e.printStackTrace();
        }
    }

}
