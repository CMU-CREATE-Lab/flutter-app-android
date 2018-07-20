package org.cmucreatelab.flutter_android.classes.outputs;

import android.graphics.Color;
import android.util.Log;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.flutters.Flutter;
import org.cmucreatelab.flutter_android.classes.relationships.Constant;
import org.cmucreatelab.flutter_android.classes.settings.AdvancedSettings;
import org.cmucreatelab.flutter_android.classes.settings.Settings;
import org.cmucreatelab.flutter_android.classes.settings.SettingsAmplitude;
import org.cmucreatelab.flutter_android.classes.settings.SettingsChange;
import org.cmucreatelab.flutter_android.classes.settings.SettingsConstant;
import org.cmucreatelab.flutter_android.classes.settings.SettingsCumulative;
import org.cmucreatelab.flutter_android.classes.settings.SettingsFrequency;
import org.cmucreatelab.flutter_android.classes.settings.SettingsProportional;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

/**
 * Created by Steve on 12/13/2016.
 *
 * TriColorLed
 *
 * A class t
 */
public class TriColorLed implements FlutterOutput {

    public static final String LED_KEY = "led_key";
    private static final int NUMBER_OF_OUTPUTS = 3;

    private int portNumber;
    private Output[] outputs;
    private Flutter flutter;

    // getters
    public RedLed getRedLed() { return (RedLed)outputs[0]; }
    public GreenLed getGreenLed() { return  (GreenLed)outputs[1]; }
    public BlueLed getBlueLed() { return (BlueLed)outputs[2]; }
    public int getPortNumber() { return this.portNumber; }


    public TriColorLed(int portNumber, Flutter flutter) {
        this.portNumber = portNumber;
        this.outputs = new Output[NUMBER_OF_OUTPUTS];
        outputs[0] = new RedLed(this.portNumber, flutter);
        outputs[1] = new GreenLed(this.portNumber, flutter);
        outputs[2] = new BlueLed(this.portNumber, flutter);
        this.flutter = flutter;
    }


    public static TriColorLed newInstance(TriColorLed oldInstance) {
        TriColorLed newInstance = new TriColorLed(oldInstance.portNumber,oldInstance.flutter);

        // settings
        if (oldInstance.getRedLed().getSettings().getClass() == oldInstance.getGreenLed().getSettings().getClass() && oldInstance.getGreenLed().getSettings().getClass() == oldInstance.getBlueLed().getSettings().getClass()) {
            newInstance.getRedLed().setSettings(Settings.newInstance(oldInstance.getRedLed().getSettings()));
            newInstance.getGreenLed().setSettings(Settings.newInstance(oldInstance.getGreenLed().getSettings()));
            newInstance.getBlueLed().setSettings(Settings.newInstance(oldInstance.getBlueLed().getSettings()));

            newInstance.getRedLed().setIsLinked(oldInstance.getRedLed().isLinked(), oldInstance.getRedLed());
            newInstance.getGreenLed().setIsLinked(oldInstance.getGreenLed().isLinked(), oldInstance.getGreenLed());
            newInstance.getBlueLed().setIsLinked(oldInstance.getBlueLed().isLinked(), oldInstance.getBlueLed());
        } else {
            Log.e(Constants.LOG_TAG, "TriColorLed.newInstance: RGB setting types do not match; returning new instance as is.");
        }

        return newInstance;
    }


    // NOTE: this doesn't handle every hex value from 00 to ff (since it is using values from 0 to 100)
    private static String getHexValue(int percent) {
        String value = Integer.toHexString(Math.round(percent/100f * 255f));
        value = value.length() < 2 ? "0".concat(value) : value;
        return value;
    }


    public String getMaxColorHex() {
        if (getRedLed().getSettings().getClass() == getGreenLed().getSettings().getClass() && getGreenLed().getSettings().getClass() == getBlueLed().getSettings().getClass()) {
            String r,g,b;
            Class mClass = getRedLed().getSettings().getClass();

            if (mClass == SettingsProportional.class) {
                r = getHexValue(((SettingsProportional)getRedLed().getSettings()).getOutputMax());
                g = getHexValue(((SettingsProportional)getGreenLed().getSettings()).getOutputMax());
                b = getHexValue(((SettingsProportional)getBlueLed().getSettings()).getOutputMax());
                return "#".concat(r.concat(g).concat(b));
            } else if (mClass == SettingsConstant.class) {
                r = getHexValue(((SettingsConstant) getRedLed().getSettings()).getValue());
                g = getHexValue(((SettingsConstant) getGreenLed().getSettings()).getValue());
                b = getHexValue(((SettingsConstant) getBlueLed().getSettings()).getValue());
                return "#".concat(r.concat(g).concat(b));
            } else if (mClass == SettingsAmplitude.class) {
                r = getHexValue(((SettingsAmplitude)getRedLed().getSettings()).getOutputMax());
                g = getHexValue(((SettingsAmplitude)getGreenLed().getSettings()).getOutputMax());
                b = getHexValue(((SettingsAmplitude)getBlueLed().getSettings()).getOutputMax());
                return "#".concat(r.concat(g).concat(b));
            } else if (mClass == SettingsFrequency.class) {
                r = getHexValue(((SettingsFrequency)getRedLed().getSettings()).getOutputMax());
                g = getHexValue(((SettingsFrequency)getGreenLed().getSettings()).getOutputMax());
                b = getHexValue(((SettingsFrequency)getBlueLed().getSettings()).getOutputMax());
                return "#".concat(r.concat(g).concat(b));
            } else if (mClass == SettingsChange.class) {
                r = getHexValue(((SettingsChange)getRedLed().getSettings()).getOutputMax());
                g = getHexValue(((SettingsChange)getGreenLed().getSettings()).getOutputMax());
                b = getHexValue(((SettingsChange)getBlueLed().getSettings()).getOutputMax());
                return "#".concat(r.concat(g).concat(b));
            } else if (mClass == SettingsCumulative.class) {
                r = getHexValue(((SettingsCumulative)getRedLed().getSettings()).getOutputMax());
                g = getHexValue(((SettingsCumulative)getGreenLed().getSettings()).getOutputMax());
                b = getHexValue(((SettingsCumulative)getBlueLed().getSettings()).getOutputMax());
                return "#".concat(r.concat(g).concat(b));
            } else {
                Log.e(Constants.LOG_TAG,"Tried to make max hex color but relationship not implemented.");
            }
        } else {
            Log.e(Constants.LOG_TAG,"Tried to make max hex color but RGB LEDs are not the same relationship.");
        }
        return "#000000";
    }

    public static String convertRgbToHex(Integer[] rgb)
    {
        return String.format("#%02x%02x%02x", rgb[0], rgb[1], rgb[2]);
    }

    public String getMinColorHex() {
        if (getRedLed().getSettings().getClass() == getGreenLed().getSettings().getClass() && getGreenLed().getSettings().getClass() == getBlueLed().getSettings().getClass()) {
            String r,g,b;
            Class mClass = getRedLed().getSettings().getClass();

            if (mClass == SettingsProportional.class) {
                r = getHexValue(((SettingsProportional) getRedLed().getSettings()).getOutputMin());
                g = getHexValue(((SettingsProportional) getGreenLed().getSettings()).getOutputMin());
                b = getHexValue(((SettingsProportional) getBlueLed().getSettings()).getOutputMin());
                return "#".concat(r.concat(g).concat(b));
            } else if (mClass == SettingsAmplitude.class) {
                r = getHexValue(((SettingsAmplitude) getRedLed().getSettings()).getOutputMin());
                g = getHexValue(((SettingsAmplitude) getGreenLed().getSettings()).getOutputMin());
                b = getHexValue(((SettingsAmplitude) getBlueLed().getSettings()).getOutputMin());
                return "#".concat(r.concat(g).concat(b));
            } else if (mClass == SettingsFrequency.class) {
                r = getHexValue(((SettingsFrequency) getRedLed().getSettings()).getOutputMin());
                g = getHexValue(((SettingsFrequency) getGreenLed().getSettings()).getOutputMin());
                b = getHexValue(((SettingsFrequency) getBlueLed().getSettings()).getOutputMin());
                return "#".concat(r.concat(g).concat(b));
            } else if (mClass == SettingsChange.class) {
                r = getHexValue(((SettingsChange) getRedLed().getSettings()).getOutputMin());
                g = getHexValue(((SettingsChange) getGreenLed().getSettings()).getOutputMin());
                b = getHexValue(((SettingsChange) getBlueLed().getSettings()).getOutputMin());
                return "#".concat(r.concat(g).concat(b));
            } else if (mClass == SettingsCumulative.class) {
                r = getHexValue(((SettingsCumulative) getRedLed().getSettings()).getOutputMin());
                g = getHexValue(((SettingsCumulative) getGreenLed().getSettings()).getOutputMin());
                b = getHexValue(((SettingsCumulative) getBlueLed().getSettings()).getOutputMin());
                return "#".concat(r.concat(g).concat(b));
            } else {
                Log.e(Constants.LOG_TAG,"Tried to make min hex color but relationship not implemented.");
            }
        } else {
            Log.e(Constants.LOG_TAG,"Tried to make min hex color but RGB LEDs are not the same relationship.");
        }
        return "#000000";
    }


    public static int getCircleFromColor(String colorHex) {
        int color = Color.parseColor(colorHex);

        if (Constants.RES_CIRCLE.containsKey(color)) {
            return Constants.RES_CIRCLE.get(color);
        }
        // default to black if color isn't in RES_CIRCLE
        return R.drawable.circle_black;
    }


    public static int getHalfCircleFromColor(String colorHex) {
        int color = Color.parseColor(colorHex);

        if (Constants.RES_HALFCIRCLE.containsKey(color)) {
            return Constants.RES_HALFCIRCLE.get(color);
        }
        // default to black if color isn't in RES_HALFCIRCLE
        return R.drawable.halfcircle_black;
    }


    public static int getSwatchFromColor(String colorHex) {
        int color = Color.parseColor(colorHex);

        if (Constants.COLOR_RES.containsKey(color)) {
            return Constants.COLOR_RES.get(color);
        }
        // default to black if color isn't in COLOR_RES
        return R.drawable.swatch_black_selected;
    }

    public static boolean isSwatchInExistingSelection(String colorHex) {
        int color = Color.parseColor(colorHex);

        return Constants.COLOR_RES.containsKey(color);
    }

    public static String getTextFromColor(String colorHex) {
        String result;
        int color = Color.parseColor(colorHex);

        if (Constants.COLOR_NAMES.containsKey(color)) {
            result = Constants.COLOR_NAMES.get(color);
        } else {
            result = "Red: " + String.valueOf(Color.red(color)) + " Green: " + String.valueOf(Color.green(color)) + " Blue: " + String.valueOf(Color.blue(color));
        }

        return result;
    }


    @Override
    public Output[] getOutputs() {
        return this.outputs;
    }


    // helpers for TriColorLed settings; we assume all LEDs share the same settings


    private int getScaledValue(float value, float maxValue, float newMaxValue) {
        if (maxValue == 0.0) {
            Log.w(Constants.LOG_TAG,"getScaledValue attempted division by 0; returning 0.");
            return 0;
        }
        float ratio = value / maxValue;
        int result = Math.round(ratio*newMaxValue);
        return result;
    }


    public void setAdvancedSettings (AdvancedSettings advancedSettings) {
        Settings r,g,b;
        r = getRedLed().getSettings();
        g = getGreenLed().getSettings();
        b = getBlueLed().getSettings();
        if (r.getClass() == g.getClass() && g.getClass() == b.getClass()) {
            if (r.hasAdvancedSettings()) {
                if (r.getClass() == SettingsProportional.class) {
                    ((SettingsProportional) r).setAdvancedSettings(advancedSettings);
                    ((SettingsProportional) g).setAdvancedSettings(advancedSettings);
                    ((SettingsProportional) b).setAdvancedSettings(advancedSettings);
                } else if (r.getClass() == SettingsAmplitude.class) {
                    ((SettingsAmplitude) r).setAdvancedSettings(advancedSettings);
                    ((SettingsAmplitude) g).setAdvancedSettings(advancedSettings);
                    ((SettingsAmplitude) b).setAdvancedSettings(advancedSettings);
                } else if (r.getClass() == SettingsFrequency.class) {
                    ((SettingsFrequency) r).setAdvancedSettings(advancedSettings);
                    ((SettingsFrequency) g).setAdvancedSettings(advancedSettings);
                    ((SettingsFrequency) b).setAdvancedSettings(advancedSettings);
                } else if (r.getClass() == SettingsChange.class) {
                    ((SettingsChange) r).setAdvancedSettings(advancedSettings);
                    ((SettingsChange) g).setAdvancedSettings(advancedSettings);
                    ((SettingsChange) b).setAdvancedSettings(advancedSettings);
                } else if (r.getClass() == SettingsCumulative.class) {
                    ((SettingsCumulative) r).setAdvancedSettings(advancedSettings);
                    ((SettingsCumulative) g).setAdvancedSettings(advancedSettings);
                    ((SettingsCumulative) b).setAdvancedSettings(advancedSettings);
                } else {
                    Log.e(Constants.LOG_TAG, "TriColorLed.setAdvancedSettings: relationship not implemented");
                }
            } else {
                Log.w(Constants.LOG_TAG, "called setAdvancedSettings in TriColorLed but current setting does not use advanced settings; ignoring.");
            }
        } else {
            Log.e(Constants.LOG_TAG, "TriColorLed.setAdvancedSettings: RGB setting types do not match and not sure how to proceed.");
        }
    }


    public void setSensorPortNumber(int sensorPortNumber) {
        Settings r,g,b;
        r = getRedLed().getSettings();
        g = getGreenLed().getSettings();
        b = getBlueLed().getSettings();
        if (r.getClass() == g.getClass() && g.getClass() == b.getClass()) {
            if (r.getClass() == SettingsProportional.class) {
                ((SettingsProportional) r).setSensorPortNumber(sensorPortNumber);
                ((SettingsProportional) g).setSensorPortNumber(sensorPortNumber);
                ((SettingsProportional) b).setSensorPortNumber(sensorPortNumber);
            } else if (r.getClass() == SettingsAmplitude.class) {
                ((SettingsAmplitude) r).setSensorPortNumber(sensorPortNumber);
                ((SettingsAmplitude) g).setSensorPortNumber(sensorPortNumber);
                ((SettingsAmplitude) b).setSensorPortNumber(sensorPortNumber);
            } else if (r.getClass() == SettingsFrequency.class) {
                ((SettingsFrequency) r).setSensorPortNumber(sensorPortNumber);
                ((SettingsFrequency) g).setSensorPortNumber(sensorPortNumber);
                ((SettingsFrequency) b).setSensorPortNumber(sensorPortNumber);
            } else if (r.getClass() == SettingsChange.class) {
                ((SettingsChange) r).setSensorPortNumber(sensorPortNumber);
                ((SettingsChange) g).setSensorPortNumber(sensorPortNumber);
                ((SettingsChange) b).setSensorPortNumber(sensorPortNumber);
            } else if (r.getClass() == SettingsCumulative.class) {
                ((SettingsCumulative) r).setSensorPortNumber(sensorPortNumber);
                ((SettingsCumulative) g).setSensorPortNumber(sensorPortNumber);
                ((SettingsCumulative) b).setSensorPortNumber(sensorPortNumber);
            } else {
                Log.e(Constants.LOG_TAG, "TriColorLed.setSensorPortNumber: relationship not implemented");
            }
        } else {
            Log.e(Constants.LOG_TAG, "TriColorLed.setSensorPortNumber: RGB setting types do not match and not sure how to proceed.");
        }
    }


    public void setOutputMax(int red, int green, int blue) {
        Settings r,g,b;
        r = getRedLed().getSettings();
        g = getGreenLed().getSettings();
        b = getBlueLed().getSettings();
        if (r.getClass() == g.getClass() && g.getClass() == b.getClass()) {
            if (r.getClass() == SettingsProportional.class) {
                ((SettingsProportional) r).setOutputMax(getScaledValue(red, 255, getRedLed().getMax()));
                ((SettingsProportional) g).setOutputMax(getScaledValue(green, 255, getGreenLed().getMax()));
                ((SettingsProportional) b).setOutputMax(getScaledValue(blue, 255, getBlueLed().getMax()));
            } else if (r.getClass() == SettingsConstant.class) {
                ((SettingsConstant) r).setValue(getScaledValue(red, 255, getRedLed().getMax()));
                ((SettingsConstant) g).setValue(getScaledValue(green, 255, getGreenLed().getMax()));
                ((SettingsConstant) b).setValue(getScaledValue(blue, 255, getBlueLed().getMax()));
            } else if (r.getClass() == SettingsAmplitude.class) {
                ((SettingsAmplitude) r).setOutputMax(getScaledValue(red, 255, getRedLed().getMax()));
                ((SettingsAmplitude) g).setOutputMax(getScaledValue(green, 255, getGreenLed().getMax()));
                ((SettingsAmplitude) b).setOutputMax(getScaledValue(blue, 255, getBlueLed().getMax()));
            } else if (r.getClass() == SettingsFrequency.class) {
                ((SettingsFrequency) r).setOutputMax(getScaledValue(red, 255, getRedLed().getMax()));
                ((SettingsFrequency) g).setOutputMax(getScaledValue(green, 255, getGreenLed().getMax()));
                ((SettingsFrequency) b).setOutputMax(getScaledValue(blue, 255, getBlueLed().getMax()));
            } else if (r.getClass() == SettingsChange.class) {
                ((SettingsChange) r).setOutputMax(getScaledValue(red, 255, getRedLed().getMax()));
                ((SettingsChange) g).setOutputMax(getScaledValue(green, 255, getGreenLed().getMax()));
                ((SettingsChange) b).setOutputMax(getScaledValue(blue, 255, getBlueLed().getMax()));
            } else if (r.getClass() == SettingsCumulative.class) {
                ((SettingsCumulative) r).setOutputMax(getScaledValue(red, 255, getRedLed().getMax()));
                ((SettingsCumulative) g).setOutputMax(getScaledValue(green, 255, getGreenLed().getMax()));
                ((SettingsCumulative) b).setOutputMax(getScaledValue(blue, 255, getBlueLed().getMax()));
            } else {
                Log.e(Constants.LOG_TAG, "TriColorLed.setOutputMax: relationship not implemented");
            }
        } else {
            Log.e(Constants.LOG_TAG, "TriColorLed.setOutputMax: RGB setting types do not match and not sure how to proceed.");
        }
    }


    public void setOutputMin(int red, int green, int blue) {
        Settings r,g,b;
        r = getRedLed().getSettings();
        g = getGreenLed().getSettings();
        b = getBlueLed().getSettings();
        if (r.getClass() == g.getClass() && g.getClass() == b.getClass()) {
            if (r.getClass() == SettingsProportional.class) {
                ((SettingsProportional)r).setOutputMin( getScaledValue(red, 255, getRedLed().getMax()) );
                ((SettingsProportional)g).setOutputMin( getScaledValue(green, 255, getGreenLed().getMax()) );
                ((SettingsProportional)b).setOutputMin( getScaledValue(blue, 255, getBlueLed().getMax()) );
            } else if (r.getClass() == SettingsConstant.class) {
                ((SettingsConstant) r).setValue(getScaledValue(red, 255, getRedLed().getMax()));
                ((SettingsConstant) g).setValue(getScaledValue(green, 255, getGreenLed().getMax()));
                ((SettingsConstant) b).setValue(getScaledValue(blue, 255, getBlueLed().getMax()));
            } else if (r.getClass() == SettingsAmplitude.class) {
                ((SettingsAmplitude)r).setOutputMin( getScaledValue(red, 255, getRedLed().getMax()) );
                ((SettingsAmplitude)g).setOutputMin( getScaledValue(green, 255, getGreenLed().getMax()) );
                ((SettingsAmplitude)b).setOutputMin( getScaledValue(blue, 255, getBlueLed().getMax()) );
            } else if (r.getClass() == SettingsFrequency.class) {
                ((SettingsFrequency)r).setOutputMin( getScaledValue(red, 255, getRedLed().getMax()) );
                ((SettingsFrequency)g).setOutputMin( getScaledValue(green, 255, getGreenLed().getMax()) );
                ((SettingsFrequency)b).setOutputMin( getScaledValue(blue, 255, getBlueLed().getMax()) );
            } else if (r.getClass() == SettingsChange.class) {
                ((SettingsChange)r).setOutputMin( getScaledValue(red, 255, getRedLed().getMax()) );
                ((SettingsChange)g).setOutputMin( getScaledValue(green, 255, getGreenLed().getMax()) );
                ((SettingsChange)b).setOutputMin( getScaledValue(blue, 255, getBlueLed().getMax()) );
            } else if (r.getClass() == SettingsCumulative.class) {
                ((SettingsCumulative)r).setOutputMin( getScaledValue(red, 255, getRedLed().getMax()) );
                ((SettingsCumulative)g).setOutputMin( getScaledValue(green, 255, getGreenLed().getMax()) );
                ((SettingsCumulative)b).setOutputMin( getScaledValue(blue, 255, getBlueLed().getMax()) );
            } else {
                Log.e(Constants.LOG_TAG, "TriColorLed.setOutputMin: relationship not implemented");
            }
        } else {
            Log.e(Constants.LOG_TAG, "TriColorLed.setOutputMin: RGB setting types do not match and not sure how to proceed.");
        }
    }

}
