package org.cmucreatelab.flutter_android.helpers;

import android.content.Context;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

/**
 * Created by Steve on 6/9/2016.
 *
 * NamingHandler
 *
 * A class that handles naming flutters.
 *
 */
public class NamingHandler {


    private Context context;
    private Hashtable<String, String> firstNames;
    private Hashtable<String, String> middleNames;
    private Hashtable<String, String> lastNames;


    public NamingHandler(Context context) {
        this.context = context;
        this.firstNames = new Hashtable<>();
        this.middleNames = new Hashtable<>();
        this.lastNames = new Hashtable<>();

        // init tables
        InputStream first = context.getResources().openRawResource(R.raw.first_names);
        InputStream middle = context.getResources().openRawResource(R.raw.middle_names);
        InputStream last = context.getResources().openRawResource(R.raw.last_names);
        BufferedReader brFirst = new BufferedReader(new InputStreamReader(first));
        BufferedReader brMiddle = new BufferedReader(new InputStreamReader(middle));
        BufferedReader brLast = new BufferedReader(new InputStreamReader(last));
        try {
            for (char i : Constants.HEX_ALPHABET) {
                for (char j : Constants.HEX_ALPHABET) {
                    String ith = Character.toString(i);
                    String jth = Character.toString(j);
                    String hexString = ith.concat(jth);
                    String name;
                    try {
                        if ((name = brFirst.readLine()) != null) {
                            firstNames.put(hexString, name);
                        }
                        if ((name = brMiddle.readLine()) != null) {
                            middleNames.put(hexString, name);
                        }
                        if ((name = brLast.readLine()) != null) {
                            lastNames.put(hexString, name);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            brFirst.close();
            brMiddle.close();
            brLast.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String generateName(String mac) {
        String result = "unknown";

        mac = mac.substring(9);
        String first;
        String middle;
        String last;

        first = mac.substring(0,2);
        middle = mac.substring(3,5);
        last = mac.substring(6);
        first = firstNames.get(first);
        middle = middleNames.get(middle);
        last = lastNames.get(last);

        if (first != null && middle != null && last != null) {
            first = first.concat(" ");
            result = result.replace(result, first);
            middle = middle.concat(" ");
            result = result.concat(middle);
            result = result.concat(last);
        }

        return result;
    }

}
