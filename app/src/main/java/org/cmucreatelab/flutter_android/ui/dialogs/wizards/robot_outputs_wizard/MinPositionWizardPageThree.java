package org.cmucreatelab.flutter_android.ui.dialogs.wizards.robot_outputs_wizard;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.servo.ServoUpdatedWithWizard;

import java.io.Serializable;

import butterknife.OnClick;

/**
 * Created by Mohit on 6/7/2018.
 *
 * MinPosition Dialog
 *
 */
public class MinPositionWizardPageThree extends ChoosePositionDialogWizard
        implements ChoosePositionDialogWizard.SetPositionListener, Serializable {

    private DialogMinPositionListener minPositionListener;
    private static Serializable robotAct;
    private Servo currentServo;


    public static MinPositionWizardPageThree newInstance(Servo servo, Serializable serializable, Serializable robotActs) {
        robotAct = robotActs;
        MinPositionWizardPageThree minPositionDialog = new MinPositionWizardPageThree();

        Bundle args = new Bundle();
        args.putSerializable(POSITION_LISTENER_KEY, serializable);
        args.putSerializable(Servo.SERVO_KEY, servo);
        minPositionDialog.setArguments(args);

        return minPositionDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "onCreateDialog");
        currentServo = Servo.newInstance((Servo) getArguments().getSerializable(Servo.SERVO_KEY));
        minPositionListener = (DialogMinPositionListener) getArguments().getSerializable(POSITION_LISTENER_KEY);
        setPositionListener = this;
        return super.onCreateDialog(savedInstanceState);
    }


    @Override
    public void onSetPosition() {
        Log.d(Constants.LOG_TAG, "MinPositionDialog.onSetPosition");
        ServoUpdatedWithWizard.add("minPosition", null, null, finalPosition, 9999);
        //minPositionListener.onMinPosChosen(finalPosition);
    }

    @OnClick(R.id.button_next_page)
    public void onClickSetPosition() {
        setPositionListener.onSetPosition();
        MaxPositionWizardPageFour dialog = MaxPositionWizardPageFour.newInstance(currentServo, null, robotAct);
        dialog.show(getFragmentManager(), "tag");
        this.dismiss();
    }




    public interface DialogMinPositionListener {
        public void onMinPosChosen(int min);
    }



}