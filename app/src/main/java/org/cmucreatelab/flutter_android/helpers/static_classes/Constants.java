package org.cmucreatelab.flutter_android.helpers.static_classes;

import android.graphics.Color;
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

    public static final String APP_VERSION = "1.0.0";

    public static final String FLUTTER_MAC_ADDRESS = "20:FA:BB";

    public static final String NAMES_TABLE_FILE = "names_table.txt";

    public static final String DATA_SET_PREFIX = "DATA_SET_";

    public static final String EMAIL_SUBJECT = "Flutter Data Log";

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

    // forces MessageQueue to throw away all "r" requests
    public static final boolean IGNORE_READ_SENSORS = false;

    // for activities

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
        public static final String WIND_SPEED = "Wind Speed";
    }

    // for activities

    public static final String SERIALIZABLE_KEY = "serializable_key";

    public static final int NUMBER_OF_SENSORS = 3;

    public static final int NUMBER_OF_SERVOS = 3;

    public static final int NUMBER_OF_TRI_COLOR_LEDS = 3;

    public static final int NUMBER_OF_SPEAKERS = 1;

    public static final char[] HEX_ALPHABET = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

}
