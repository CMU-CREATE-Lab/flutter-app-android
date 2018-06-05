package org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.children;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.sensors.NoSensor;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.servo.ServoDialogStateHelper;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mohit on 6/4/2018.
 *
 * SensorWizardPageTwo
 *
 * A Dialog that shows which sensor is to be linked with an output.
 * Called by the relationship dialog after clicking the "Next" button.
 */
public class SensorWizardPageTwo extends BaseResizableDialogWizard implements View.OnClickListener, Serializable  {


    private DialogSensorListener dialogSensorListener;
    private Button nextButton;
    private Servo currentServo;
    private static ServoDialogStateHelper stateHelper;



    public static SensorWizardPageTwo newInstance(Servo servo, Serializable serializable) {
        SensorWizardPageTwo sensorDialog = new SensorWizardPageTwo();

        Bundle args = new Bundle();
        args.putSerializable(Servo.SERVO_KEY, servo);
        args.putSerializable(Constants.SerializableKeys.SENSOR_KEY, serializable);
        sensorDialog.setArguments(args);

        return sensorDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstances) {
        super.onCreateDialog(savedInstances);
        dialogSensorListener = (DialogSensorListener) getArguments().getSerializable(Constants.SerializableKeys.SENSOR_KEY);
        currentServo = Servo.newInstance((Servo) getArguments().getSerializable(Servo.SERVO_KEY));
        if (stateHelper == null) {
            stateHelper = ServoDialogStateHelper.newInstance(currentServo);
        }
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_sensor_choice_wizard, null);
        nextButton = (Button) view.findViewById(R.id.button_next_page);
        ((TextView) view.findViewById(R.id.text_output_title)).setText(getString(R.string.set_up_servo) + " " +  String.valueOf(currentServo.getPortNumber()));
        ((ImageView) view.findViewById(R.id.text_output_title_icon)).setImageResource(R.drawable.servo_icon);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        ((TextView) view.findViewById(R.id.text_question_sensor)).setText("Which sensor should servo " + String.valueOf(currentServo.getPortNumber()) + " react to?");
        builder.setView(view);
        ButterKnife.bind(this, view);

        Sensor sensors[] = GlobalHandler.getInstance(this.getActivity()).sessionHandler.getSession().getFlutter().getSensors();

        // bind click listeners
        ImageView sensor1 = (ImageView) view.findViewById(R.id.image_sensor_1);
        ImageView sensor2 = (ImageView) view.findViewById(R.id.image_sensor_2);
        ImageView sensor3 = (ImageView) view.findViewById(R.id.image_sensor_3);
        TextView textSensor1 = (TextView) view.findViewById(R.id.text_sensor_1);
        TextView textSensor2 = (TextView) view.findViewById(R.id.text_sensor_2);
        TextView textSensor3 = (TextView) view.findViewById(R.id.text_sensor_3);
        LinearLayout layout1 = (LinearLayout) view.findViewById(R.id.linear_sensor_1);
        LinearLayout layout2 = (LinearLayout) view.findViewById(R.id.linear_sensor_2);
        LinearLayout layout3 = (LinearLayout) view.findViewById(R.id.linear_sensor_3);

        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);

        sensor1.setImageResource(sensors[0].getGreenImageId());
        sensor2.setImageResource(sensors[1].getGreenImageId());
        sensor3.setImageResource(sensors[2].getGreenImageId());
        textSensor1.setText(sensors[0].getSensorTypeId());
        textSensor2.setText(sensors[1].getSensorTypeId());
        textSensor3.setText(sensors[2].getSensorTypeId());

        return builder.create();
    }


    @Override
    public void onClick(View view) {
        GlobalHandler globalHandler = GlobalHandler.getInstance(view.getContext());
        Sensor sensor = new NoSensor(0);
        switch (view.getId()) {
            case R.id.linear_sensor_1:
                sensor = globalHandler.sessionHandler.getSession().getFlutter().getSensors()[0];
                nextButton.setEnabled(true);
                break;
            case R.id.linear_sensor_2:
                sensor = globalHandler.sessionHandler.getSession().getFlutter().getSensors()[1];
                nextButton.setEnabled(true);
                break;
            case R.id.linear_sensor_3:
                sensor = globalHandler.sessionHandler.getSession().getFlutter().getSensors()[2];
                nextButton.setEnabled(true);
                break;

        }
        dialogSensorListener.onSensorChosen(sensor);

    }

    @OnClick(R.id.button_next_page)
    public void onClickNextPage(View view) {
        // send an intent to the wet position dialog
        //stateHelper.clickMin(this);
    }


    // interface for an activity to listen for a choice
    public interface DialogSensorListener {
        public void onSensorChosen(Sensor sensor);
    }

}
