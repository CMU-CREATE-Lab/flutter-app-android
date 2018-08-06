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
        public static final int RED = Color.parseColor("#ff3333");
        public static final int ORANGE = Color.parseColor("#ff9933");
        public static final int YELLOW = Color.parseColor("#ffff33");
        public static final int CHARTREUSE_GREEN = Color.parseColor("#99ff33");
        public static final int GREEN = Color.parseColor("#33ff33");
        public static final int SPRING_GREEN = Color.parseColor("#33ff99");
        public static final int CYAN = Color.parseColor("#33ffff");
        public static final int AZURE = Color.parseColor("#3399ff");
        public static final int BLUE = Color.parseColor("#3333ff");
        public static final int VIOLET = Color.parseColor("#9933ff");
        public static final int MAGENTA = Color.parseColor("#ff33ff");
        public static final int ROSE = Color.parseColor("#ff3399");
        public static final int WHITE = Color.parseColor("#f2f2f2");
        public static final int BLACK = Color.parseColor("#333333");

        public static final int WHITE_DEFAULT = Color.parseColor("#ffffff");
        public static final int BLACK_DEFAULT = Color.parseColor("#000000");
    }

    public static final class TrueColorRGB {
        public static final Integer[] RED = new Integer[]{255, 0, 0};
        public static final Integer[] ORANGE = new Integer[]{255, 56, 0};
        public static final Integer[] YELLOW = new Integer[]{255, 217, 0};
        public static final Integer[] CHARTREUSE_GREEN = new Integer[]{128, 255, 0};
        public static final Integer[] GREEN = new Integer[]{0, 255, 0};
        public static final Integer[] SPRING_GREEN = new Integer[]{0, 255, 41};
        public static final Integer[] CYAN = new Integer[]{0, 255, 217};
        public static final Integer[] AZURE = new Integer[]{0, 122, 255};
        public static final Integer[] BLUE = new Integer[]{0, 0, 255};
        public static final Integer[] VIOLET = new Integer[]{64, 0, 255};
        public static final Integer[] MAGENTA = new Integer[]{255, 0, 255};
        public static final Integer[] ROSE = new Integer[]{255, 0, 41};
        public static final Integer[] WHITE = new Integer[]{245, 255, 204};
        public static final Integer[] BLACK = new Integer[]{0, 0, 0};
    }

    public static final class TrueColorHex {
        public static final int RED = Color.parseColor("#ff0000");
        public static final int ORANGE = Color.parseColor("#ff3800");
        public static final int YELLOW = Color.parseColor("#ffd900");
        public static final int CHARTREUSE_GREEN = Color.parseColor("#80ff00");
        public static final int GREEN = Color.parseColor("#00ff00");
        public static final int SPRING_GREEN = Color.parseColor("#00ff29");
        public static final int CYAN = Color.parseColor("#00ffd9");
        public static final int AZURE = Color.parseColor("#007aff");
        public static final int BLUE = Color.parseColor("#0000ff");
        public static final int VIOLET = Color.parseColor("#4000ff");
        public static final int MAGENTA = Color.parseColor("#ff00ff");
        public static final int ROSE = Color.parseColor("#ff0029");
        public static final int WHITE = Color.parseColor("#f5ffcc");
        public static final int BLACK = Color.parseColor("#000000");

        public static final int WHITE_DEFAULT = Color.parseColor("#ffffff");
        public static final int BLACK_DEFAULT = Color.parseColor("#000000");
    }

    public static final HashMap<Integer, Integer[]> COLOR_PICKER_FLUTTER_RGB = new HashMap() {{
        put(ColorSwatches.RED, TrueColorRGB.RED);
        put(ColorSwatches.ORANGE, TrueColorRGB.ORANGE);
        put(ColorSwatches.YELLOW, TrueColorRGB.YELLOW);
        put(ColorSwatches.CHARTREUSE_GREEN, TrueColorRGB.CHARTREUSE_GREEN);
        put(ColorSwatches.GREEN, TrueColorRGB.GREEN);
        put(ColorSwatches.SPRING_GREEN, TrueColorRGB.SPRING_GREEN);
        put(ColorSwatches.CYAN, TrueColorRGB.CYAN);
        put(ColorSwatches.AZURE, TrueColorRGB.AZURE);
        put(ColorSwatches.BLUE, TrueColorRGB.BLUE);
        put(ColorSwatches.VIOLET, TrueColorRGB.VIOLET);
        put(ColorSwatches.MAGENTA, TrueColorRGB.MAGENTA);
        put(ColorSwatches.ROSE, TrueColorRGB.ROSE);
        put(ColorSwatches.WHITE, TrueColorRGB.WHITE);
        put(ColorSwatches.BLACK, TrueColorRGB.BLACK);
    }};

    public static final HashMap<Integer, Integer> TRUE_HEX_TO_SWATCH_HEX = new HashMap() {{
        put(TrueColorHex.RED, ColorSwatches.RED);
        put(TrueColorHex.ORANGE, ColorSwatches.ORANGE);
        put(TrueColorHex.YELLOW, ColorSwatches.YELLOW);
        put(TrueColorHex.CHARTREUSE_GREEN, ColorSwatches.CHARTREUSE_GREEN);
        put(TrueColorHex.GREEN, ColorSwatches.GREEN);
        put(TrueColorHex.SPRING_GREEN, ColorSwatches.SPRING_GREEN);
        put(TrueColorHex.CYAN, ColorSwatches.CYAN);
        put(TrueColorHex.AZURE, ColorSwatches.AZURE);
        put(TrueColorHex.BLUE, ColorSwatches.BLUE);
        put(TrueColorHex.VIOLET, ColorSwatches.VIOLET);
        put(TrueColorHex.MAGENTA, ColorSwatches.MAGENTA);
        put(TrueColorHex.ROSE, ColorSwatches.ROSE);
        put(TrueColorHex.WHITE, ColorSwatches.WHITE);
        put(TrueColorHex.BLACK, ColorSwatches.BLACK);
        put(TrueColorHex.WHITE_DEFAULT, ColorSwatches.WHITE_DEFAULT);
        put(TrueColorHex.BLACK_DEFAULT, ColorSwatches.BLACK_DEFAULT);
    }};

    public static final HashMap<Integer, Integer> COLOR_RES = new HashMap() {{
        put(TrueColorHex.RED, R.drawable.swatch_red_selected);
        put(TrueColorHex.ORANGE, R.drawable.swatch_orange_selected);
        put(TrueColorHex.YELLOW, R.drawable.swatch_yellow_selected);
        put(TrueColorHex.CHARTREUSE_GREEN, R.drawable.swatch_chartreuse_green_selected);
        put(TrueColorHex.GREEN, R.drawable.swatch_green_selected);
        put(TrueColorHex.SPRING_GREEN, R.drawable.swatch_spring_green_selected);
        put(TrueColorHex.CYAN, R.drawable.swatch_cyan_selected);
        put(TrueColorHex.AZURE, R.drawable.swatch_azure_selected);
        put(TrueColorHex.BLUE, R.drawable.swatch_blue_selected);
        put(TrueColorHex.VIOLET, R.drawable.swatch_violet_selected);
        put(TrueColorHex.MAGENTA, R.drawable.swatch_magenta_selected);
        put(TrueColorHex.ROSE, R.drawable.swatch_rose_selected);
        put(TrueColorHex.WHITE, R.drawable.swatch_white_selected);
        put(TrueColorHex.BLACK, R.drawable.swatch_black_selected);
        put(TrueColorHex.WHITE_DEFAULT, R.drawable.swatch_white_selected);
        put(TrueColorHex.BLACK_DEFAULT, R.drawable.swatch_black_selected);
    }};

    public static final HashMap<Integer, String> COLOR_NAMES = new HashMap() {{
        put(TrueColorHex.RED, "Red");
        put(TrueColorHex.ORANGE, "Orange");
        put(TrueColorHex.YELLOW, "Yellow");
        put(TrueColorHex.CHARTREUSE_GREEN, "Chartreuse Green");
        put(TrueColorHex.GREEN, "Green");
        put(TrueColorHex.SPRING_GREEN, "Spring Green");
        put(TrueColorHex.CYAN, "Cyan");
        put(TrueColorHex.AZURE, "Azure");
        put(TrueColorHex.BLUE, "Blue");
        put(TrueColorHex.VIOLET, "Violet");
        put(TrueColorHex.MAGENTA, "Magenta");
        put(TrueColorHex.ROSE, "Rose");
        put(TrueColorHex.WHITE, "White");
        put(TrueColorHex.BLACK, "Black");
        put(TrueColorHex.WHITE_DEFAULT, "White");
        put(TrueColorHex.BLACK_DEFAULT, "Black");
    }};

    public static final HashMap<Integer, Integer> RES_CIRCLE = new HashMap() {{
        put(TrueColorHex.RED, R.drawable.circle_red);
        put(TrueColorHex.ORANGE, R.drawable.circle_orange);
        put(TrueColorHex.YELLOW, R.drawable.circle_yellow);
        put(TrueColorHex.CHARTREUSE_GREEN, R.drawable.circle_chartreuse_green);
        put(TrueColorHex.GREEN, R.drawable.circle_green);
        put(TrueColorHex.SPRING_GREEN, R.drawable.circle_spring_green);
        put(TrueColorHex.CYAN, R.drawable.circle_cyan);
        put(TrueColorHex.AZURE, R.drawable.circle_azure);
        put(TrueColorHex.BLUE, R.drawable.circle_blue);
        put(TrueColorHex.VIOLET, R.drawable.circle_violet);
        put(TrueColorHex.MAGENTA, R.drawable.circle_magenta);
        put(TrueColorHex.ROSE, R.drawable.circle_rose);
        put(TrueColorHex.WHITE, R.drawable.circle_white);
        put(TrueColorHex.BLACK, R.drawable.circle_black);
        put(TrueColorHex.WHITE_DEFAULT, R.drawable.circle_white);
        put(TrueColorHex.BLACK_DEFAULT, R.drawable.circle_black);
    }};

    public static final HashMap<Integer, Integer> RES_HALFCIRCLE = new HashMap() {{
        put(TrueColorHex.RED, R.drawable.halfcircle_red);
        put(TrueColorHex.ORANGE, R.drawable.halfcircle_orange);
        put(TrueColorHex.YELLOW, R.drawable.halfcircle_yellow);
        put(TrueColorHex.CHARTREUSE_GREEN, R.drawable.halfcircle_chartreuse_green);
        put(TrueColorHex.GREEN, R.drawable.halfcircle_green);
        put(TrueColorHex.SPRING_GREEN, R.drawable.halfcircle_spring_green);
        put(TrueColorHex.CYAN, R.drawable.halfcircle_cyan);
        put(TrueColorHex.AZURE, R.drawable.halfcircle_azure);
        put(TrueColorHex.BLUE, R.drawable.halfcircle_blue);
        put(TrueColorHex.VIOLET, R.drawable.halfcircle_violet);
        put(TrueColorHex.MAGENTA, R.drawable.halfcircle_magenta);
        put(TrueColorHex.ROSE, R.drawable.halfcircle_rose);
        put(TrueColorHex.WHITE, R.drawable.halfcircle_white);
        put(TrueColorHex.BLACK, R.drawable.halfcircle_black);
        put(TrueColorHex.WHITE_DEFAULT, R.drawable.halfcircle_white);
        put(TrueColorHex.BLACK_DEFAULT, R.drawable.halfcircle_black);
    }};

    // forces MessageQueue to throw away all "r" requests
    public static final boolean IGNORE_READ_SENSORS = false;

    // determines how we want to send emails
    public enum MailerType {
        INTENT, HTTP_REQUEST
    }

    public static final String MAIL_SERVER_URL = "http://fluttermail.createlab.org/send_log";
    public static final MailerType SEND_EMAIL_AS = MailerType.HTTP_REQUEST;

    // for activities

    public static final int FLUTTER_WAITING_TIMEOUT_IN_MILLISECONDS = 2000;
    public static final int FLUTTER_WAITING_TIMEOUT_ONE_MINUTE = 60000;
    public static final int FLUTTER_WAITING_PROMPT_TIMEOUT_IN_MILLISECONDS = 12000;

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

    public enum RECORD_DATA_WIZARD_TYPE {
        SENSORS_TAB,
        DATA_LOGS_TAB
    }

    public static final HashMap<RECORD_DATA_WIZARD_TYPE, Integer> WIZARD_TYPE_TO_CANCEL_BACKGROUND = new HashMap() {{
        put(RECORD_DATA_WIZARD_TYPE.SENSORS_TAB, R.drawable.round_blue_button_bottom_left);
        put(RECORD_DATA_WIZARD_TYPE.DATA_LOGS_TAB, R.drawable.round_orange_button_bottom_left);
    }};
    public static final HashMap<RECORD_DATA_WIZARD_TYPE, Integer> WIZARD_TYPE_TO_CANCEL_TEXT = new HashMap() {{
        put(RECORD_DATA_WIZARD_TYPE.SENSORS_TAB, R.color.blue);
        put(RECORD_DATA_WIZARD_TYPE.DATA_LOGS_TAB, R.color.orange);
    }};
    public static final HashMap<RECORD_DATA_WIZARD_TYPE, Integer> WIZARD_TYPE_TO_NEXT_BACKGROUND = new HashMap() {{
        put(RECORD_DATA_WIZARD_TYPE.SENSORS_TAB, R.drawable.round_blue_button_bottom_right);
        put(RECORD_DATA_WIZARD_TYPE.DATA_LOGS_TAB, R.drawable.round_orange_button_bottom_right);
    }};
    public static final HashMap<RECORD_DATA_WIZARD_TYPE, Integer> WIZARD_TYPE_TO_REVIEW_BUTTON = new HashMap() {{
        put(RECORD_DATA_WIZARD_TYPE.SENSORS_TAB, R.drawable.round_blue_button_bottom);
        put(RECORD_DATA_WIZARD_TYPE.DATA_LOGS_TAB, R.drawable.round_orange_button_bottom);
    }};
    public static final HashMap<RECORD_DATA_WIZARD_TYPE, Integer> WIZARD_TYPE_TO_CONFIRM_BUTTON = new HashMap() {{
        put(RECORD_DATA_WIZARD_TYPE.SENSORS_TAB, R.drawable.round_blue_button_bottom_right);
        put(RECORD_DATA_WIZARD_TYPE.DATA_LOGS_TAB, R.drawable.round_orange_button_bottom_right);
    }};

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
