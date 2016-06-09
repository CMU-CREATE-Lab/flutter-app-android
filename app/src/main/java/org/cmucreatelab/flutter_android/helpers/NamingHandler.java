package org.cmucreatelab.flutter_android.helpers;

import android.content.Context;
import android.util.Log;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Steve on 6/9/2016.
 */
public class NamingHandler implements Serializable {


    private Context context;
    private Hashtable<Integer, String> namesTable;


    private void loadTableFromFile() {
        ObjectInputStream in = null;

        try {
            in = new ObjectInputStream(new FileInputStream(new File(new File(context.getFilesDir(),"")+File.separator+Constants.NAMES_TABLE_FILE)));
            namesTable = (Hashtable<Integer, String>) in.readObject();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    private void writeTableToFile() {
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(new File(context.getFilesDir(), "")+File.separator+Constants.NAMES_TABLE_FILE));
            out.writeObject(namesTable);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String readName() {
        String result = "unknown";

        InputStream is = context.getResources().openRawResource(R.raw.names);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line;
        try {
            while ((line = br.readLine()) != null) {
                Set<Integer> names = namesTable.keySet();
                if (names.size() > 0) {
                    for (Integer i : names) {
                        if (!line.equals(namesTable.get(i))) {
                            result = line;
                            break;
                        }
                    }
                } else {
                    result = line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


    public NamingHandler(Context context) {
        this.context = context;
        loadTableFromFile();
        if (namesTable == null)
            namesTable = new Hashtable<>();
    }

    public String generateName(Integer hashCode) {
        String result = "unknown";

        if (namesTable.get(hashCode) == null) {
            result = readName();
            namesTable.put(hashCode, result);
            writeTableToFile();
        } else {
            result = namesTable.get(hashCode);
        }

        return result;
    }

}
