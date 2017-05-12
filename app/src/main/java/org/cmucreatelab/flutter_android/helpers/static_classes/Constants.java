package org.cmucreatelab.flutter_android.helpers.static_classes;

import android.graphics.Color;

import org.cmucreatelab.flutter_android.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Steve on 5/26/2016.
 *
 * Constants
 *
 * A class that handles global constants that is used throughout the app.
 *
 */
public final class Constants {

    public static final String PACKAGE_NAME = "org.cmucreatelab.flutter_android";

    public static final String LOG_TAG = "FlutterAndroid";

    public static final String FLUTTER_MAC_ADDRESS = "20:FA:BB";

    public static final String EMAIL_SUBJECT = "Flutter Data Log";

    public static final String PREFERENCES = "preferences";

    public final class MusicNoteFrequencies {
        public static final int C_4 = 262;
        public static final int D_4 = 294;
        public static final int E_4 = 330;
        public static final int F_4 = 349;
        public static final int G_4 = 392;
        public static final int A_4 = 440;
        public static final int B_4 = 494;
        public static final int C_5 = 523;
        public static final int D_5 = 587;
        public static final int E_5 = 659;
        public static final int F_5 = 698;
        public static final int G_5 = 784;
        public static final int A_5 = 880;
        public static final int B_5 = 988;
        public static final int C_6 = 1047;
    }

    public static final class ColorSwatches {
        public static final int RED = Color.parseColor("#ff0000");
        public static final int ORANGE = Color.parseColor("#ff7f00");
        public static final int YELLOW = Color.parseColor("#ffff00");
        public static final int CHARTREUSE_GREEN = Color.parseColor("#7fff00");
        public static final int GREEN = Color.parseColor("#00ff00");
        public static final int SPRING_GREEN = Color.parseColor("#00ff7f");
        public static final int CYAN = Color.parseColor("#00ffff");
        public static final int AZURE = Color.parseColor("#007fff");
        public static final int BLUE = Color.parseColor("#0000ff");
        public static final int VIOLET = Color.parseColor("#7f00ff");
        public static final int MAGENTA = Color.parseColor("#ff00ff");
        public static final int ROSE = Color.parseColor("#ff007f");
        public static final int WHITE = Color.parseColor("#ffffff");
        public static final int BLACK = Color.parseColor("#000000");
    }

    public static final HashMap<Integer,Integer> COLOR_RES = new HashMap(){{
        put(ColorSwatches.RED, R.drawable.swatch_red_selected);
        put(ColorSwatches.ORANGE, R.drawable.swatch_orange_selected);
        put(ColorSwatches.YELLOW, R.drawable.swatch_yellow_selected);
        put(ColorSwatches.CHARTREUSE_GREEN, R.drawable.swatch_chartreuse_green_selected);
        put(ColorSwatches.GREEN, R.drawable.swatch_green_selected);
        put(ColorSwatches.SPRING_GREEN, R.drawable.swatch_spring_green_selected);
        put(ColorSwatches.CYAN, R.drawable.swatch_cyan_selected);
        put(ColorSwatches.AZURE, R.drawable.swatch_azure_selected);
        put(ColorSwatches.BLUE, R.drawable.swatch_blue_selected);
        put(ColorSwatches.VIOLET, R.drawable.swatch_violet_selected);
        put(ColorSwatches.MAGENTA, R.drawable.swatch_magenta_selected);
        put(ColorSwatches.ROSE, R.drawable.swatch_rose_selected);
        put(ColorSwatches.WHITE, R.drawable.swatch_white_selected);
        put(ColorSwatches.BLACK, R.drawable.swatch_black_selected);
    }};

    public static final HashMap<Integer,String> COLOR_NAMES = new HashMap(){{
        put(ColorSwatches.RED, "Red");
        put(ColorSwatches.ORANGE, "Orange");
        put(ColorSwatches.YELLOW, "Yellow");
        put(ColorSwatches.CHARTREUSE_GREEN, "Chartreuse Green");
        put(ColorSwatches.GREEN, "Green");
        put(ColorSwatches.SPRING_GREEN, "Spring Green");
        put(ColorSwatches.CYAN, "Cyan");
        put(ColorSwatches.AZURE, "Azure");
        put(ColorSwatches.BLUE, "Blue");
        put(ColorSwatches.VIOLET, "Violet");
        put(ColorSwatches.MAGENTA, "Magenta");
        put(ColorSwatches.ROSE, "Rose");
        put(ColorSwatches.WHITE, "White");
        put(ColorSwatches.BLACK, "Black");
    }};

    public static final HashMap<Integer,Integer> RES_CIRCLE = new HashMap(){{
        put(ColorSwatches.RED, R.drawable.circle_red);
        put(ColorSwatches.ORANGE, R.drawable.circle_orange);
        put(ColorSwatches.YELLOW, R.drawable.circle_yellow);
        put(ColorSwatches.CHARTREUSE_GREEN, R.drawable.circle_chartreuse_green);
        put(ColorSwatches.GREEN, R.drawable.circle_green);
        put(ColorSwatches.SPRING_GREEN, R.drawable.circle_spring_green);
        put(ColorSwatches.CYAN, R.drawable.circle_cyan);
        put(ColorSwatches.AZURE, R.drawable.circle_azure);
        put(ColorSwatches.BLUE, R.drawable.circle_blue);
        put(ColorSwatches.VIOLET, R.drawable.circle_violet);
        put(ColorSwatches.MAGENTA, R.drawable.circle_magenta);
        put(ColorSwatches.ROSE, R.drawable.circle_rose);
        put(ColorSwatches.WHITE, R.drawable.circle_white);
        put(ColorSwatches.BLACK, R.drawable.circle_black);
    }};

    public static final HashMap<Integer,Integer> RES_HALFCIRCLE = new HashMap(){{
        put(ColorSwatches.RED, R.drawable.halfcircle_red);
        put(ColorSwatches.ORANGE, R.drawable.halfcircle_orange);
        put(ColorSwatches.YELLOW, R.drawable.halfcircle_yellow);
        put(ColorSwatches.CHARTREUSE_GREEN, R.drawable.halfcircle_chartreuse_green);
        put(ColorSwatches.GREEN, R.drawable.halfcircle_green);
        put(ColorSwatches.SPRING_GREEN, R.drawable.halfcircle_spring_green);
        put(ColorSwatches.CYAN, R.drawable.halfcircle_cyan);
        put(ColorSwatches.AZURE, R.drawable.halfcircle_azure);
        put(ColorSwatches.BLUE, R.drawable.halfcircle_blue);
        put(ColorSwatches.VIOLET, R.drawable.halfcircle_violet);
        put(ColorSwatches.MAGENTA, R.drawable.halfcircle_magenta);
        put(ColorSwatches.ROSE, R.drawable.halfcircle_rose);
        put(ColorSwatches.WHITE, R.drawable.halfcircle_white);
        put(ColorSwatches.BLACK, R.drawable.halfcircle_black);
    }};

    // forces MessageQueue to throw away all "r" requests
    public static final boolean IGNORE_READ_SENSORS = false;

    // determines how we want to send emails
    public enum MailerType {
        INTENT, HTTP_REQUEST
    }
    public static final String MAIL_SERVER_URL= "http://fluttermail.createlab.org/send_log";
    public static final MailerType SEND_EMAIL_AS = MailerType.HTTP_REQUEST;

    // for activities

    public static final int FLUTTER_WAITING_TIMEOUT_IN_MILLISECONDS = 2000;
    public static final int FLUTTER_WAITING_TIMEOUT_ONE_MINUTE = 60000;
    public static final int FLUTTER_WAITING_PROMPT_TIMEOUT_IN_MILLISECONDS = 15000;

    public static final class SerializableKeys {
        public static final String DIALOG_LED = "dialog_led";
        public static final String DIALOG_SERVO = "dialog_servo";
        public static final String DIALOG_SPEAKER = "dialog_speaker";
        public static String SENSOR_KEY = "sensor_key";
        public static String SENSOR_PORT_KEY = "sensor_text_key";
        public static String RELATIONSHIP_KEY = "relationship_key";
    }

    public static final class SensorTypeWords {
        public static final String ANALOG_OR_UNKNOWN = "Analog / Unknown";
        public static final String BAROMETRIC_PRESSURE = "Barometric Pressure";
        public static final String DISTANCE = "Distance";
        public static final String HUMIDITY = "Humidity";
        public static final String LIGHT = "Light";
        public static final String NO_SENSOR = "No Sensor";
        public static final String SOIL_MOISTURE = "Soil Moisture";
        public static final String SOUND = "Sound";
        public static final String TEMPERATURE = "Temperature";
        public static final String AIR_QUALITY = "Air Particles";
    }

    public static enum STATS {
        NONE,
        MEAN,
        MEDIAN,
        MODE,
        MAX,
        MIN
    }

    public static final String scanningText[] = {
            "SCANNING FOR FLUTTERS",
            "SCANNING FOR FLUTTERS.",
            "SCANNING FOR FLUTTERS..",
            "SCANNING FOR FLUTTERS..."
    };

    // Put any Melody Smart MAC addresses that you don't want to show up in the scan list here.
    public static final ArrayList<String> addressBlackList = new ArrayList<String>() {
        {
            /* Groovy Indigo Grasshopper: Josh's non-Flutter device */
            add("20:FA:BB:04:96:62");
        }
    };

}
