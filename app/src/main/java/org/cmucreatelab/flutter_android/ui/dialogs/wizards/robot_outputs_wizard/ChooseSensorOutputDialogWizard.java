package org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.outputs.Speaker;
import org.cmucreatelab.flutter_android.classes.outputs.TriColorLed;
import org.cmucreatelab.flutter_android.classes.sensors.Sensor;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.wizards.BaseResizableDialogWizard;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mike on 6/27/18.
 */

public class ChooseSensorOutputDialogWizard extends BaseResizableDialogWizard {

    private View dialogView;
    private ServoWizard.State wizardState;
//    private int selectedSensorPort = -1;
//
//    public static final String SELECTED_SENSOR = "selected_sensor";


    public static ChooseSensorOutputDialogWizard newInstance(ServoWizard wizard, ServoWizard.State wizardState) {
        Bundle args = new Bundle();
        ChooseSensorOutputDialogWizard dialogWizard = new ChooseSensorOutputDialogWizard();
        args.putSerializable(BaseResizableDialogWizard.KEY_WIZARD, wizard);
        args.putSerializable(ServoWizard.STATE_KEY, wizardState);
        dialogWizard.setArguments(args);

        return dialogWizard;
    }


    private void populateSensors(View view) {
        Sensor sensors[] = GlobalHandler.getInstance(this.getActivity()).sessionHandler.getSession().getFlutter().getSensors();
        ImageView sensor1 = (ImageView) view.findViewById(R.id.image_sensor_1);
        ImageView sensor2 = (ImageView) view.findViewById(R.id.image_sensor_2);
        ImageView sensor3 = (ImageView) view.findViewById(R.id.image_sensor_3);
        TextView textSensor1 = (TextView) view.findViewById(R.id.text_sensor_1);
        TextView textSensor2 = (TextView) view.findViewById(R.id.text_sensor_2);
        TextView textSensor3 = (TextView) view.findViewById(R.id.text_sensor_3);

        sensor1.setImageResource(sensors[0].getGreenImageId());
        sensor2.setImageResource(sensors[1].getGreenImageId());
        sensor3.setImageResource(sensors[2].getGreenImageId());
        textSensor1.setText(sensors[0].getSensorTypeId());
        textSensor2.setText(sensors[1].getSensorTypeId());
        textSensor3.setText(sensors[2].getSensorTypeId());
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_sensor_choice_wizard, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ButterKnife.bind(this, view);
        this.dialogView = view;
        this.wizardState = (ServoWizard.State)(getArguments().getSerializable(ServoWizard.STATE_KEY));
        populateSensors(view);

        return builder.create();
    }


    private void clearSelection() {
        int[] viewIds = { R.id.linear_sensor_1, R.id.linear_sensor_2, R.id.linear_sensor_3 };
        for (int id: viewIds)
            dialogView.findViewById(id).setBackground(null);
    }

    private void selectedView(View view) {
        clearSelection();
        view.setBackground(ContextCompat.getDrawable(dialogView.getContext(), R.drawable.rectangle_green_border));
    }

    private int getSensorPortFromId(int id) {
        switch(id) {
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

    @OnClick({R.id.linear_sensor_1, R.id.linear_sensor_2, R.id.linear_sensor_3})
    public void onClickSensor(View view) {
        // TODO @tasota actions
        Log.v(Constants.LOG_TAG, "ChooseSensorOutputDialogWizard.onClickSensor");
        selectedView(view);
        wizardState.selectedSensorPort = getSensorPortFromId(view.getId());
    }

    @OnClick(R.id.button_next_page)
    public void onClickSave() {
        Log.v(Constants.LOG_TAG, "ChooseSensorOutputDialogWizard.onClickSave");
        Bundle args = new Bundle();
        args.putInt("page",3);
        args.putSerializable(ServoWizard.STATE_KEY, wizardState);
        changeDialog(args);
    }

}
