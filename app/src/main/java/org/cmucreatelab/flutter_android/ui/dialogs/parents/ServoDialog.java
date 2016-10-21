package org.cmucreatelab.flutter_android.ui.dialogs.parents;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.outputs.Servo;
import org.cmucreatelab.flutter_android.classes.relationships.Relationship;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.children.RelationshipDialog;

import java.io.Serializable;

/**
 * Created by Steve on 10/17/2016.
 */
public class ServoDialog extends DialogFragment implements Serializable, DialogInterface.OnClickListener, RelationshipDialog.DialogRelationshipListener {


    private Serializable serializable;
    private DialogFragment dialogFragment;

    private LinearLayout setLinkedSensor;
    private LinearLayout setRelationship;
    private LinearLayout setMaximumPos;
    private LinearLayout setMinimumPos;

    private ImageView currentImageView;
    private TextView currentTextView;


    public static ServoDialog newInstance(Servo servo, String servoNumber) {
        ServoDialog servoDialog = new ServoDialog();

        Bundle args = new Bundle();
        args.putSerializable(Servo.SERVO_KEY, servo);
        args.putString(Servo.SERVO_NUMBER_KEY, servoNumber);
        servoDialog.setArguments(args);

        return servoDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        serializable = this;
        dialogFragment = this;

        Servo servo = (Servo) getArguments().getSerializable(Servo.SERVO_KEY);
        String servoNumber = getArguments().getString(Servo.SERVO_NUMBER_KEY);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_servos, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setTitle(getString(R.string.set_up_servo) +  " " + servoNumber).setView(view);
        builder.setPositiveButton(R.string.save_settings, this);

        setLinkedSensor = (LinearLayout) view.findViewById(R.id.linear_set_linked_sensor);
        setRelationship = (LinearLayout) view.findViewById(R.id.linear_set_relationship);
        setMaximumPos = (LinearLayout) view.findViewById(R.id.linear_set_max_pos);
        setMinimumPos = (LinearLayout) view.findViewById(R.id.linear_set_min_pos);

        setLinkedSensor.setOnClickListener(linkedSensorListener);
        setRelationship.setOnClickListener(relationshipListener);
        setMaximumPos.setOnClickListener(maximumPosListener);
        setMinimumPos.setOnClickListener(minimumPosListener);

        return builder.create();
    }


    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }


    // OnClickListeners


    // TODO - add more dialogs for the various options
    // TODO - change the textview structure
    private View.OnClickListener linkedSensorListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(Constants.LOG_TAG, "onClickSetLinkedSensor");
            currentImageView = (ImageView) ((ViewGroup) view).getChildAt(0);
            currentTextView = (TextView) ((ViewGroup) view).getChildAt(1);
        }
    };


    private View.OnClickListener relationshipListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(Constants.LOG_TAG, "onClickSetRelationship");
            currentImageView = (ImageView) ((ViewGroup) view).getChildAt(0);
            currentTextView = (TextView) ((ViewGroup) view).getChildAt(1);
            DialogFragment dialog = RelationshipDialog.newInstance(serializable);
            dialog.show(dialogFragment.getFragmentManager(), "tag");
        }
    };


    private View.OnClickListener maximumPosListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(Constants.LOG_TAG, "onClickSetMaximumPosition");
        }
    };


    private View.OnClickListener minimumPosListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(Constants.LOG_TAG, "onClickSetMinimumPosition");
        }
    };


    @Override
    public void onRelationshipChosen(Relationship relationship) {
        Log.d(Constants.LOG_TAG, "onRelationshipChosen");
        currentImageView.setImageResource(relationship.getGreenImageIdMd());
        currentTextView.setText(relationship.getRelationshipType().toString());
    }

}
