package org.cmucreatelab.flutter_android.helpers;

import android.content.Context;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
    private ArrayList<String> firstNames,middleNames,lastNames;


    public NamingHandler(Context context) {
        this.context = context;
        firstNames = new ArrayList<>();
        middleNames = new ArrayList<>();
        lastNames = new ArrayList<>();

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
                    String name;
                    try {
                        if ((name = brFirst.readLine()) != null) {
                            firstNames.add(name);
                        }
                        if ((name = brMiddle.readLine()) != null) {
                            middleNames.add(name);
                        }
                        if ((name = brLast.readLine()) != null) {
                            lastNames.add(name);
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
        long mid;
        int i,j,k,offset;

        // expected input: "aa:bb:cc:dd:ee:ff" => "d:ee:ff"
        mac = mac.substring(9);
        String first;
        String middle;
        String last;

        // grab bits from the MAC address (6 bits, 6 bits, 8 bits => last, middle, first)
        i = Integer.parseInt(mac.substring(6),16);
        mid = Long.parseLong(mac.substring(1,2).concat(mac.substring(3,5)),16);
        k = (int)(mid / 64);
        j = (int)(mid % 64);

        // use the last 4 bits to "shift" all of the indices.
        offset = i % 16;
        i += offset;
        j += offset;
        k += offset;

        first = firstNames.get(i);
        middle = middleNames.get(j);
        last = lastNames.get(k);

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
