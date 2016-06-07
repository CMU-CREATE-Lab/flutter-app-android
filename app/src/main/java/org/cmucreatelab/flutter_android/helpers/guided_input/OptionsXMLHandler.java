package org.cmucreatelab.flutter_android.helpers.guided_input;

import android.content.Context;
import android.util.Log;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

/**
 * Created by Steve on 6/7/2016.
 */
public class OptionsXMLHandler {


    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ?:'_-";
    public static final String NAME = "Name";
    public static final String TITLE = "Title";
    public static final String OPTION = "Option";
    public static final String PARENT = "Parent";
    public static final String CHILD = "Child";

    private Hashtable<String, OptionsNode> table;
    private XmlPullParser xpp;


    private Hashtable<String, OptionsNode> generate() throws XmlPullParserException, IOException {
        int eventType = xpp.getEventType();
        String currentTag = "";
        OptionsNode tempOptionsNode = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if(eventType == XmlPullParser.START_DOCUMENT) {
                Log.d(Constants.LOG_TAG, "Starting to read xml file");
            } else if(eventType == XmlPullParser.START_TAG) {

                String tag = xpp.getName();
                if (tag.equals(NAME) || tag.equals(TITLE) || tag.equals(OPTION) || tag.equals(PARENT) || tag.equals(CHILD)) {
                    currentTag = tag;
                }
            } else if(eventType == XmlPullParser.TEXT) {
                String text = xpp.getText();

                if (!text.contains("\t")) {
                    switch (currentTag) {
                        case NAME:
                            tempOptionsNode = new OptionsNode();
                            tempOptionsNode.setName(text);
                            //Log.d(Constants.LOG_TAG, text);
                            break;
                        case TITLE:
                            tempOptionsNode.setTitle(text);
                            table.put(tempOptionsNode.getName(), tempOptionsNode);
                            //Log.d(Constants.LOG_TAG, text);
                            break;
                        case OPTION:
                            tempOptionsNode.addOption(text);
                            //Log.d(Constants.LOG_TAG, text);
                            break;
                        case PARENT:
                            tempOptionsNode = table.get(text);
                            //Log.d(Constants.LOG_TAG, text);
                            break;
                        case CHILD:
                            tempOptionsNode.link(table.get(text));
                            //Log.d(Constants.LOG_TAG, text);
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

        return table;
    }


    public OptionsXMLHandler(Context context) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        xpp = factory.newPullParser();
        InputStream is = context.getResources().openRawResource(R.raw.options);
        xpp.setInput(new InputStreamReader(is));
        table = new Hashtable<>();
        generate();
    }


    public OptionsNode getStart() {
        return table.get("main_prompt");
    }


}
