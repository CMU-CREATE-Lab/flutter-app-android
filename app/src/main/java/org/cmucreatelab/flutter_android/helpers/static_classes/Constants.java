package org.cmucreatelab.flutter_android.helpers.static_classes;

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

    public static final String LOG_TAG = "FlutterAndroid";

    public static final String APP_VERSION = "1.0";

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

    // forces MessageQueue to throw away all "r" requests
    public static final boolean IGNORE_READ_SENSORS = false;

    // for activities

    // TODO - not sure if we will actually use these since messaging has been refactored
    public static final class PreferencesKeys {
        public static final String dataloggingSensor1 = "data_logging_sensor_1";
        public static final String dataloggingSensor2 = "data_logging_sensor_2";
        public static final String dataloggingSensor3 = "data_logging_sensor_3";
        public static final String dataloggingName = "date_logging_name";
    }

    public static final HashMap<String, Object> DEFAULT_SETTINGS = new HashMap(){{
        put(PreferencesKeys.dataloggingSensor1, "No Sensor");
        put(PreferencesKeys.dataloggingSensor2, "No Sensor");
        put(PreferencesKeys.dataloggingSensor3, "No Sensor");
    }};

    // for activities

    public static final String SERIALIZABLE_KEY = "serializable_key";

    public static final int NUMBER_OF_SENSORS = 3;

    public static final int NUMBER_OF_SERVOS = 3;

    public static final int NUMBER_OF_TRI_COLOR_LEDS = 3;

    public static final int NUMBER_OF_SPEAKERS = 1;

    public static final char[] HEX_ALPHABET = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

}
