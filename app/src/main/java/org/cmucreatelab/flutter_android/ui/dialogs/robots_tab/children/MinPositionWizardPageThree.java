package org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.children;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialogWizard;
import org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs.servo.ServoDialogStateHelper;

import java.io.Serializable;

import butterknife.ButterKnife;
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
    private Servo currentServo;


    public static MinPositionWizardPageThree newInstance(Servo servo, Serializable serializable) {
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
        minPositionListener.onMinPosChosen(finalPosition);
        this.dismiss();
    }

    @OnClick(R.id.button_next_page)
    public void onClickSetPosition() {
        MaxPositionWizardPageFour dialog = MaxPositionWizardPageFour.newInstance(currentServo, null);
        dialog.show(getFragmentManager(), "tag");
        this.dismiss();
    }



    public interface DialogMinPositionListener {
        public void onMinPosChosen(int min);
    }



}