package org.cmucreatelab.flutter_android.ui;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.RobotActivity;
import org.cmucreatelab.flutter_android.classes.Session;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.settings.Settings;
import org.cmucreatelab.flutter_android.classes.settings.SettingsAmplitude;
import org.cmucreatelab.flutter_android.classes.settings.SettingsChange;
import org.cmucreatelab.flutter_android.classes.settings.SettingsConstant;
import org.cmucreatelab.flutter_android.classes.settings.SettingsCumulative;
import org.cmucreatelab.flutter_android.classes.settings.SettingsFrequency;
import org.cmucreatelab.flutter_android.classes.settings.SettingsProportional;

/**
 * Created by Parv on 6/13/2018.
 *
 * ServoAngleDrawable
 *
 * Represents the icon displayed for a Servo with varying angles. Also contains a static helper for refreshing views on RobotActivity
 *
 */
public class ServoAngleDrawable extends Drawable {

    private Paint paint;
    private RectF rectF;
    private int minAngle, maxAngle;


    private ServoAngleDrawable(int color, int minAngle, int maxAngle, Activity activity) {
        this.minAngle = minAngle;
        this.maxAngle = maxAngle;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setPaintColor(color, activity);
        paint.setStyle(Paint.Style.FILL);
        rectF = new RectF();
    }


    private void setPaintColor(int color, Activity activity) {
        paint.setColor(activity.getResources().getColor(color));
    }


    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        Rect bounds = getBounds();
        canvas.scale(1, 2);
        rectF.set(bounds);
        canvas.drawArc(rectF, minAngle - 180, maxAngle - minAngle, true, paint);
    }


    @Override
    public void setAlpha(int alpha) {

    }


    @Override
    public void setColorFilter(ColorFilter c) {

    }


    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }


    public static void updateServoIndicators(Session session, RobotActivity activity) {
        Servo[] servos = session.getFlutter().getServos();
        int minPos[] = new int[servos.length];
        int maxPos[] = new int[servos.length];
        boolean[] constant = {false, false, false};

        for (int i = 0; i < servos.length; i++) {
            if (servos[i].isLinked()) {
                Settings settings = servos[i].getSettings();
                if (settings.getClass() == SettingsProportional.class && ((SettingsProportional) settings).getSensorPortNumber() != 0) {
                    minPos[i] = ((SettingsProportional) settings).getOutputMin();
                    maxPos[i] = ((SettingsProportional) settings).getOutputMax();
                } else if (settings.getClass() == SettingsAmplitude.class && ((SettingsAmplitude) settings).getSensorPortNumber() != 0) {
                    minPos[i] = ((SettingsAmplitude) settings).getOutputMin();
                    maxPos[i] = ((SettingsAmplitude) settings).getOutputMax();
                } else if (settings.getClass() == SettingsFrequency.class && ((SettingsFrequency) settings).getSensorPortNumber() != 0) {
                    minPos[i] = ((SettingsFrequency) settings).getOutputMin();
                    maxPos[i] = ((SettingsFrequency) settings).getOutputMax();
                } else if (settings.getClass() == SettingsChange.class && ((SettingsChange) settings).getSensorPortNumber() != 0) {
                    minPos[i] = ((SettingsChange) settings).getOutputMin();
                    maxPos[i] = ((SettingsChange) settings).getOutputMax();
                } else if (settings.getClass() == SettingsCumulative.class && ((SettingsCumulative) settings).getSensorPortNumber() != 0) {
                    minPos[i] = ((SettingsCumulative) settings).getOutputMin();
                    maxPos[i] = ((SettingsCumulative) settings).getOutputMax();
                } else if (settings.getClass() == SettingsConstant.class) {
                    constant[i] = true;
                    maxPos[i] = ((SettingsConstant) settings).getValue();
                }
            }
        }

        if (servos[0].isLinked()) {
            TextView servo1MinPosText = (TextView) activity.findViewById(R.id.text_servo_1_min_pos);
            TextView servo1MaxPosText = (TextView) activity.findViewById(R.id.text_servo_1_max_pos);
            ImageView servo1GreenIndicator = (ImageView) activity.findViewById(R.id.servo_1_foreground_green_indicator);

            if (constant[0]) {
                servo1MaxPosText.setText(maxPos[0] + "°");
                servo1MinPosText.setVisibility(View.INVISIBLE);
                ServoAngleDrawable servoAngleDrawable;
                if (maxPos[0] >= 5) {
                    servoAngleDrawable = new ServoAngleDrawable(
                            R.color.fluttergreen, maxPos[0] - 5, maxPos[0], activity);
                }
                else
                {
                    servoAngleDrawable = new ServoAngleDrawable(
                            R.color.fluttergreen, maxPos[0], maxPos[0] + 5, activity);
                }
                servo1GreenIndicator.setImageDrawable(servoAngleDrawable);
            } else {
                servo1MinPosText.setVisibility(View.VISIBLE);
                ServoAngleDrawable servoAngleDrawable = new ServoAngleDrawable(
                        R.color.fluttergreen, minPos[0], maxPos[0], activity);
                servo1GreenIndicator.setImageDrawable(servoAngleDrawable);
                servo1MinPosText.setText(minPos[0] + "°");
                servo1MaxPosText.setText(maxPos[0] + "°");
            }
        }
        if (servos[1].isLinked()) {
            TextView servo2MinPosText = (TextView) activity.findViewById(R.id.text_servo_2_min_pos);
            TextView servo2MaxPosText = (TextView) activity.findViewById(R.id.text_servo_2_max_pos);
            ImageView servo2GreenIndicator = (ImageView) activity.findViewById(R.id.servo_2_foreground_green_indicator);

            if (constant[1]) {
                servo2MaxPosText.setText(maxPos[1] + "°");
                servo2MinPosText.setVisibility(View.INVISIBLE);
                ServoAngleDrawable servoAngleDrawable;
                if (maxPos[1] >= 5) {
                    servoAngleDrawable = new ServoAngleDrawable(R.color.fluttergreen, maxPos[1] - 5, maxPos[1], activity);
                }
                else {
                    servoAngleDrawable = new ServoAngleDrawable(R.color.fluttergreen, maxPos[1], maxPos[1] + 5, activity);
                }
                servo2GreenIndicator.setImageDrawable(servoAngleDrawable);
            } else {
                servo2MinPosText.setVisibility(View.VISIBLE);
                ServoAngleDrawable servoAngleDrawable = new ServoAngleDrawable(
                        R.color.fluttergreen, minPos[1], maxPos[1], activity);
                servo2GreenIndicator.setImageDrawable(servoAngleDrawable);
                servo2MinPosText.setText(minPos[1] + "°");
                servo2MaxPosText.setText(maxPos[1] + "°");
            }
        }
        if (servos[2].isLinked()) {
            TextView servo3MinPosText = (TextView) activity.findViewById(R.id.text_servo_3_min_pos);
            TextView servo3MaxPosText = (TextView) activity.findViewById(R.id.text_servo_3_max_pos);
            ImageView servo3GreenIndicator = (ImageView) activity.findViewById(R.id.servo_3_foreground_green_indicator);

            if (constant[2]) {
                servo3MaxPosText.setText(maxPos[2] + "°");
                servo3MinPosText.setVisibility(View.INVISIBLE);
                ServoAngleDrawable servoAngleDrawable;
                if (maxPos[2] >= 5) {
                    servoAngleDrawable = new ServoAngleDrawable(R.color.fluttergreen, maxPos[2] - 5, maxPos[2], activity);
                }
                else {
                    servoAngleDrawable = new ServoAngleDrawable(R.color.fluttergreen, maxPos[2], maxPos[2] + 5, activity);
                }
                servo3GreenIndicator.setImageDrawable(servoAngleDrawable);
            } else {
                servo3MinPosText.setVisibility(View.VISIBLE);
                ServoAngleDrawable servoAngleDrawable = new ServoAngleDrawable(R.color.fluttergreen, minPos[2], maxPos[2], activity);
                servo3GreenIndicator.setImageDrawable(servoAngleDrawable);
                servo3MinPosText.setText(minPos[2] + "°");
                servo3MaxPosText.setText(maxPos[2] + "°");
            }
        }
    }

}