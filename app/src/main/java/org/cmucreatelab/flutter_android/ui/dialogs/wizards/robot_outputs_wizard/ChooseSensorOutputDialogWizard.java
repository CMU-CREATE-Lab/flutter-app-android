package org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.FlutterProtocol;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.BaseResizableDialogWizard;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mike on 6/27/18.
 */

public abstract class ChooseSensorOutputDialogWizard extends BaseResizableDialogWizard {

    private View dialogView;
    protected Button nextButton;


    private void populateSensors(View view) {
        Sensor[] sensors = GlobalHandler.getInstance(this.getActivity()).sessionHandler.getSession().getFlutter().getSensors();
        ImageView sensor1 = (ImageView) view.findViewById(R.id.image_sensor_1);
        ImageView sensor2 = (ImageView) view.findViewById(R.id.image_sensor_2);
        ImageView sensor3 = (ImageView) view.findViewById(R.id.image_sensor_3);
        TextView textSensor1 = (TextView) view.findViewById(R.id.text_sensor_1);
        TextView textSensor2 = (TextView) view.findViewById(R.id.text_sensor_2);
        TextView textSensor3 = (TextView) view.findViewById(R.id.text_sensor_3);

        //Set the linear layouts of the sensors to not be clickable if the sensor is not set
        view.findViewById(R.id.linear_sensor_1).setClickable(sensors[0].getSensorType() != FlutterProtocol.InputTypes.NOT_SET);
        view.findViewById(R.id.linear_sensor_2).setClickable(sensors[1].getSensorType() != FlutterProtocol.InputTypes.NOT_SET);
        view.findViewById(R.id.linear_sensor_3).setClickable(sensors[2].getSensorType() != FlutterProtocol.InputTypes.NOT_SET);

        sensor1.setImageResource(sensors[0].getGreenImageId());
        sensor2.setImageResource(sensors[1].getGreenImageId());
        sensor3.setImageResource(sensors[2].getGreenImageId());
        textSensor1.setText(sensors[0].getSensorTypeId());
        textSensor2.setText(sensors[1].getSensorTypeId());
        textSensor3.setText(sensors[2].getSensorTypeId());
    }


    protected void clearSelection() {
        int[] viewIds = {R.id.linear_sensor_1, R.id.linear_sensor_2, R.id.linear_sensor_3};
        for (int id : viewIds) {
            dialogView.findViewById(id).setBackground(null);
        }
    }


    protected void selectedView(View view) {
        clearSelection();
        view.setBackground(ContextCompat.getDrawable(dialogView.getContext(), R.drawable.rectangle_green_border));
    }


    protected int getSensorPortFromId(int id) {
        switch (id) {
            case R.id.linear_sensor_1:
                return 1;
            case R.id.linear_sensor_2:
                return 2;
            case R.id.linear_sensor_3:
                return 3;
            default:
                Log.w(Constants.LOG_TAG, "could not match sensor port from id");
        }
        return 0;
    }


    protected View getViewFromSensorPort(int sensorPort) {
        switch (sensorPort) {
            case 1:
                return dialogView.findViewById(R.id.linear_sensor_1);
            case 2:
                return dialogView.findViewById(R.id.linear_sensor_2);
            case 3:
                return dialogView.findViewById(R.id.linear_sensor_3);
        }
        return null;
    }


    public abstract void updateViewWithOptions();

    public abstract void updateSelectedSensorPort(View view);

    public abstract void updateText(View view);


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_choose_sensor_wizard, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ButterKnife.bind(this, view);
        this.dialogView = view;
        populateSensors(view);
        nextButton = (Button) view.findViewById(R.id.button_next);

        //have to update wizard state before updating others
        updateWizardState();
        updateViewWithOptions();
        updateText(view);

        return builder.create();
    }


    public abstract void updateWizardState();


    @OnClick({R.id.linear_sensor_1, R.id.linear_sensor_2, R.id.linear_sensor_3})
    public void onClickSensor(View view) {
        Log.v(Constants.LOG_TAG, "ChooseSensorOutputDialogWizard.onClickSensor");
        updateSelectedSensorPort(view);
        updateViewWithOptions();
    }


    @OnClick(R.id.button_back)
    public abstract void onClickBack();


    @OnClick(R.id.button_next)
    public abstract void onClickNext();


    @OnClick(R.id.image_advanced_settings)
    public void onClickAdvancedSettings() {
        Log.i(Constants.LOG_TAG, "onClickAdvancedSettings");
        // TODO finish wizard, display summary/advanced dialog
    }


    @OnClick(R.id.button_close)
    public void onClickClose() {
        wizard.changeDialog(null);
    }
}
