package org.cmucreatelab.flutter_android.helpers.static_classes;

import android.content.Context;
import org.cmucreatelab.flutter_android.R;

/**
 * Created by Steve on 6/9/2016.
 *
 * NamingHandler
 *
 * A class that handles naming flutters.
 */
public class NamingHandler {


    public static String generateName(Context context, String mac) {
        String result = "unknown";
        long mid;
        int i,j,k,offset;
        String[] firstNames,middleNames,lastNames;
        firstNames = context.getResources().getStringArray(R.array.first_names);
        middleNames = context.getResources().getStringArray(R.array.middle_names);
        lastNames = context.getResources().getStringArray(R.array.last_names);

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

        first = firstNames[i];
        middle = middleNames[j];
        last = lastNames[k];

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
