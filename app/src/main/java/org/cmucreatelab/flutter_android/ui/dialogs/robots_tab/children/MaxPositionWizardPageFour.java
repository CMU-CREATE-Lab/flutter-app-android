package org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.children;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.servo.ServoDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.servo.ServoUpdatedWithWizard;

import java.io.Serializable;

import butterknife.OnClick;

/**
 * Created by Mohit on 6/15/2018.
 *
 * MaxPositionDialog
 *
 * A Dialog that prompts the user to choose a max position for a servo.
 */
public class MaxPositionWizardPageFour extends ChoosePositionDialogWizard implements ChoosePositionDialogWizard.SetPositionListener {

    private DialogMaxPositionListener maxPositionListener;
    private static Serializable robotAct;
    private Servo currentServo;

    public static MaxPositionWizardPageFour newInstance(Servo servo, Serializable serializable, Serializable robotActs) {
        robotAct = robotActs;
        MaxPositionWizardPageFour maxPositionDialog = new MaxPositionWizardPageFour();

        Bundle args = new Bundle();
        args.putSerializable(POSITION_LISTENER_KEY, serializable);
        args.putSerializable(Servo.SERVO_KEY, servo);
        maxPositionDialog.setArguments(args);

        return maxPositionDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(Constants.LOG_TAG, "onCreateDialog");
        //((TextView) view.findViewById(R.id.set_wet)).setText("Set the dry position");
        currentServo = Servo.newInstance((Servo) getArguments().getSerializable(Servo.SERVO_KEY));
        maxPositionListener = (DialogMaxPositionListener) getArguments().getSerializable(POSITION_LISTENER_KEY);
        setPositionListener = this;
        return super.onCreateDialog(savedInstanceState);
    }

    @OnClick(R.id.button_next_page)
    public void onClickSetPosition() {
        setPositionListener.onSetPosition();
        ServoDialog dialog = ServoDialog.newInstance(currentServo, robotAct, true);
        dialog.show(getFragmentManager(), "tag");
        this.dismiss();
    }

    @Override
    public void onSetPosition() {
        Log.d(Constants.LOG_TAG, "MaxPositionDialog.onSetPosition");
        //maxPositionListener.onMaxPosChosen(finalPosition);
        ServoUpdatedWithWizard.add("maxPosition",null, null, 9999, finalPosition);
    }



    public interface DialogMaxPositionListener {
        public void onMaxPosChosen(int max);
    }

}

