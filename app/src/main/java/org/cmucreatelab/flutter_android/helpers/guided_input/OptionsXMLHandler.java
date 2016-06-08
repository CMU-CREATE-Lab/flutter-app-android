package org.cmucreatelab.flutter_android.helpers.guided_input;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

/**
 * Created by Steve on 6/7/2016.
 */
public class OptionsXMLHandler {


    // our option keys
    private static final String KEY_OUTPUT = "output_options";
    private static final String KEY_WHICH_ONE = "which_one";
    private static final String KEY_RANGE_V = "range_v";
    private static final String KEY_RANGE_F = "range_f";

    // our main object keys
    private static final String ID = "Id";
    private static final String TEXT = "Text";
    public static final String NAME = "Name";
    public static final String TITLE = "Title";
    public static final String OPTION = "Option";
    public static final String PARENT = "Parent";
    public static final String CHILD = "Child";

    private Hashtable<String, OptionsNode> table;
    private XmlPullParser xpp;

    private Hashtable<String, ArrayList<String>> optionsTable;


    private Hashtable<String, OptionsNode> generate() throws XmlPullParserException, IOException {
        int eventType = xpp.getEventType();
        String currentTag = "";
        OptionsNode tempOptionsNode = null;
        ArrayList<String> tempList = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if(eventType == XmlPullParser.START_DOCUMENT) {
                Log.d(Constants.LOG_TAG, "Starting to read xml file");
            } else if(eventType == XmlPullParser.START_TAG) {

                String tag = xpp.getName();
                if (tag.equals(NAME) || tag.equals(TITLE) || tag.equals(OPTION) || tag.equals(PARENT) || tag.equals(CHILD) || tag.equals(ID) || tag.equals(TEXT)) {
                    currentTag = tag;
                }
            } else if(eventType == XmlPullParser.TEXT) {
                String text = xpp.getText();

                // filter out empty text elements
                if (!text.contains("\t")) {
                    switch (currentTag) {
                        case ID:
                            tempList = new ArrayList<>();
                            // You can add as many global options as you want...just add another conditional
                            if (text.equals(KEY_OUTPUT))
                                optionsTable.put(KEY_OUTPUT, tempList);
                            else if (text.equals(KEY_WHICH_ONE))
                                optionsTable.put(KEY_WHICH_ONE, tempList);
                            else if (text.equals(KEY_RANGE_V))
                                optionsTable.put(KEY_RANGE_V, tempList);
                            else if (text.equals(KEY_RANGE_F))
                                optionsTable.put(KEY_RANGE_F, tempList);
                            break;
                        case TEXT:
                            tempList.add(text);
                            break;
                        case NAME:
                            tempOptionsNode = new OptionsNode();
                            tempOptionsNode.setName(text);
                            break;
                        case TITLE:
                            tempOptionsNode.setTitle(text);
                            table.put(tempOptionsNode.getName(), tempOptionsNode);
                            break;
                        case OPTION:
                            // You can add as many global options as you want...just add another conditional
                            if (text.equals(KEY_OUTPUT)) {
                                for (String string : optionsTable.get(KEY_OUTPUT)) {
                                    tempOptionsNode.addOption(string);
                                }
                            } else if (text.equals(KEY_WHICH_ONE)) {
                                for (String string : optionsTable.get(KEY_WHICH_ONE)) {
                                    tempOptionsNode.addOption(string);
                                }
                            } else if (text.equals(KEY_RANGE_V)) {
                                for (String string : optionsTable.get(KEY_RANGE_V)) {
                                    tempOptionsNode.addOption(string);
                                }
                            }  else if (text.equals(KEY_RANGE_F)) {
                                for (String string : optionsTable.get(KEY_RANGE_F)) {
                                    tempOptionsNode.addOption(string);
                                }
                            }else {
                                tempOptionsNode.addOption(text);
                            }
                            break;
                        case PARENT:
                            tempOptionsNode = table.get(text);
                            break;
                        case CHILD:
                            tempOptionsNode.link(table.get(text));
                            break;
                        default:
                            break;
                    }
                }
            }
            eventType = xpp.next();
            if (eventType == XmlPullParser.END_DOCUMENT) {
                Log.d(Constants.LOG_TAG, "Finished reading xml file");
            }
        }

        // List all objects
        Log.d(Constants.LOG_TAG, String.valueOf(table.size()));
        Set<String> KEYS = table.keySet();
        for (String key : KEYS) {
            Log.d(Constants.LOG_TAG, "Child: " + table.get(key).getName());
            if (table.get(key).getParent() != null) {
                Log.d(Constants.LOG_TAG, "Parent: " + table.get(key).getParent().getName());
            }
        }

        return table;
    }


    public OptionsXMLHandler(Context context) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        xpp = factory.newPullParser();
        InputStream is = context.getResources().openRawResource(R.raw.options);
        xpp.setInput(new InputStreamReader(is));
        table = new Hashtable<>();
        optionsTable = new Hashtable<>();
        generate();
    }


    public OptionsNode getStart() {
        return table.get("main_prompt");
    }


}
