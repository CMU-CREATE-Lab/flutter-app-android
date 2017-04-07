package org.cmucreatelab.flutter_android.classes.outputs;

import android.graphics.Color;
import android.util.Log;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.flutters.Flutter;
import org.cmucreatelab.flutter_android.classes.relationships.Amplitude;
import org.cmucreatelab.flutter_android.classes.relationships.Constant;
import org.cmucreatelab.flutter_android.classes.relationships.Proportional;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.classes.settings.AdvancedSettings;
import org.cmucreatelab.flutter_android.classes.settings.Settings;
import org.cmucreatelab.flutter_android.classes.settings.SettingsAmplitude;
import org.cmucreatelab.flutter_android.classes.settings.SettingsConstant;
import org.cmucreatelab.flutter_android.classes.settings.SettingsProportional;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

/**
 * Created by Steve on 12/13/2016.
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
            if (oldInstance.getRedLed().getSettings().getClass() == SettingsConstant.class) {
                newInstance.getRedLed().setSettings(SettingsConstant.newInstance(oldInstance.getRedLed().getSettings()));
                newInstance.getGreenLed().setSettings(SettingsConstant.newInstance(oldInstance.getGreenLed().getSettings()));
                newInstance.getBlueLed().setSettings(SettingsConstant.newInstance(oldInstance.getBlueLed().getSettings()));
            } else if (oldInstance.getRedLed().getSettings().getClass() == SettingsProportional.class) {
                newInstance.getRedLed().setSettings(SettingsProportional.newInstance(oldInstance.getRedLed().getSettings()));
                newInstance.getGreenLed().setSettings(SettingsProportional.newInstance(oldInstance.getGreenLed().getSettings()));
                newInstance.getBlueLed().setSettings(SettingsProportional.newInstance(oldInstance.getBlueLed().getSettings()));
            } else if (oldInstance.getRedLed().getSettings().getClass() == SettingsAmplitude.class) {
                newInstance.getRedLed().setSettings(SettingsAmplitude.newInstance(oldInstance.getRedLed().getSettings()));
                newInstance.getGreenLed().setSettings(SettingsAmplitude.newInstance(oldInstance.getGreenLed().getSettings()));
                newInstance.getBlueLed().setSettings(SettingsAmplitude.newInstance(oldInstance.getBlueLed().getSettings()));
            } else {
                Log.e(Constants.LOG_TAG, "TriColorLed.newInstance: Relationship not implemented");
            }
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
        String value = Integer.toHexString((int)((float)percent/100.0 * 255));
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
            } else {
                Log.e(Constants.LOG_TAG,"Tried to make max hex color but relationship not implemented.");
            }
        } else {
            Log.e(Constants.LOG_TAG,"Tried to make max hex color but RGB LEDs are not the same relationship.");
        }
        return "#000000";
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
            } else {
                Log.e(Constants.LOG_TAG,"Tried to make min hex color but relationship not implemented.");
            }
        } else {
            Log.e(Constants.LOG_TAG,"Tried to make min hex color but RGB LEDs are not the same relationship.");
        }
        return "#000000";
    }


    public int getMaxSwatch() {
        int color = Color.parseColor(getMaxColorHex());
        if (Constants.COLOR_RES.containsKey(color)) {
            return Constants.COLOR_RES.get(color);
        }
        // default to black if color isn't in COLOR_RES
        return R.drawable.swatch_black_selected;
    }


    public String getMaxColorText() {
        String result;
        int color = Color.parseColor(getMaxColorHex());

        if (Constants.COLOR_NAMES.containsKey(color)) {
            result = Constants.COLOR_NAMES.get(color);
        } else {
            result = "Red: " + String.valueOf(Color.red(color)) + " Green: " + String.valueOf(Color.green(color)) + " Blue: " + String.valueOf(Color.blue(color));
        }

        return result;
    }


    public int getMinSwatch() {
        int color = Color.parseColor(getMinColorHex());
        if (Constants.COLOR_RES.containsKey(color)) {
            return Constants.COLOR_RES.get(color);
        }
        // default to black if color isn't in COLOR_RES
        return R.drawable.swatch_black_selected;
    }


    public String getMinColorText() {
        String result;
        int color = Color.parseColor(getMinColorHex());

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


    private int getProportionalValue(float value, float maxValue, float newMaxValue) {
        float ratio = value / maxValue;
        int result = (int) Math.ceil(ratio*newMaxValue);
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
            } else {
                Log.e(Constants.LOG_TAG, "TriColorLed.setSensorPortNumber: relationship not implemented");
            }
        } else {
            Log.e(Constants.LOG_TAG, "TriColorLed.setSensorPortNumber: RGB setting types do not match and not sure how to proceed.");
        }
    }


    public void setRelationship(Relationship relationship) {
        if (relationship.getClass() == Proportional.class) {
            getRedLed().setSettings(SettingsProportional.newInstance(getRedLed().getSettings()));
            getGreenLed().setSettings(SettingsProportional.newInstance(getGreenLed().getSettings()));
            getBlueLed().setSettings(SettingsProportional.newInstance(getBlueLed().getSettings()));
        } else if (relationship.getClass() == Constant.class) {
            getRedLed().setSettings(SettingsConstant.newInstance(getRedLed().getSettings()));
            getGreenLed().setSettings(SettingsConstant.newInstance(getGreenLed().getSettings()));
            getBlueLed().setSettings(SettingsConstant.newInstance(getBlueLed().getSettings()));
        } else if (relationship.getClass() == Amplitude.class) {
            getRedLed().setSettings(SettingsAmplitude.newInstance(getRedLed().getSettings()));
            getGreenLed().setSettings(SettingsAmplitude.newInstance(getGreenLed().getSettings()));
            getBlueLed().setSettings(SettingsAmplitude.newInstance(getBlueLed().getSettings()));
        } else {
            Log.e(Constants.LOG_TAG, "TriColorLed.setRelationship: relationship not implemented");
        }
    }


    public void setOutputMax(int red, int green, int blue) {
        Settings r,g,b;
        r = getRedLed().getSettings();
        g = getGreenLed().getSettings();
        b = getBlueLed().getSettings();
        if (r.getClass() == g.getClass() && g.getClass() == b.getClass()) {
            if (r.getClass() == SettingsProportional.class) {
                ((SettingsProportional) r).setOutputMax(getProportionalValue(red, 255, getRedLed().getMax()));
                ((SettingsProportional) g).setOutputMax(getProportionalValue(green, 255, getGreenLed().getMax()));
                ((SettingsProportional) b).setOutputMax(getProportionalValue(blue, 255, getBlueLed().getMax()));
            } else if (r.getClass() == SettingsConstant.class) {
                ((SettingsConstant) r).setValue(getProportionalValue(red, 255, getRedLed().getMax()));
                ((SettingsConstant) g).setValue(getProportionalValue(green, 255, getGreenLed().getMax()));
                ((SettingsConstant) b).setValue(getProportionalValue(blue, 255, getBlueLed().getMax()));
            } else if (r.getClass() == SettingsAmplitude.class) {
                ((SettingsAmplitude) r).setOutputMax(getProportionalValue(red, 255, getRedLed().getMax()));
                ((SettingsAmplitude) g).setOutputMax(getProportionalValue(green, 255, getGreenLed().getMax()));
                ((SettingsAmplitude) b).setOutputMax(getProportionalValue(blue, 255, getBlueLed().getMax()));
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
                ((SettingsProportional)r).setOutputMin( getProportionalValue(red, 255, getRedLed().getMax()) );
                ((SettingsProportional)g).setOutputMin( getProportionalValue(green, 255, getGreenLed().getMax()) );
                ((SettingsProportional)b).setOutputMin( getProportionalValue(blue, 255, getBlueLed().getMax()) );
            } else if (r.getClass() == SettingsConstant.class) {
                ((SettingsConstant) r).setValue(getProportionalValue(red, 255, getRedLed().getMax()));
                ((SettingsConstant) g).setValue(getProportionalValue(green, 255, getGreenLed().getMax()));
                ((SettingsConstant) b).setValue(getProportionalValue(blue, 255, getBlueLed().getMax()));
            } else if (r.getClass() == SettingsAmplitude.class) {
                ((SettingsAmplitude)r).setOutputMin( getProportionalValue(red, 255, getRedLed().getMax()) );
                ((SettingsAmplitude)g).setOutputMin( getProportionalValue(green, 255, getGreenLed().getMax()) );
                ((SettingsAmplitude)b).setOutputMin( getProportionalValue(blue, 255, getBlueLed().getMax()) );
            } else {
                Log.e(Constants.LOG_TAG, "TriColorLed.setOutputMin: relationship not implemented");
            }
        } else {
            Log.e(Constants.LOG_TAG, "TriColorLed.setOutputMin: RGB setting types do not match and not sure how to proceed.");
        }
    }

}
