package org.cmucreatelab.flutter_android.ui.dialogs.RecordDataWizard;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.rockerhieu.emojicon.EmojiconEditText;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.datalogging.DataLogDetails;
import org.cmucreatelab.flutter_android.classes.datalogging.DataSet;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.FileHandler;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialog;

import java.io.Serializable;

/**
 * Created by Steve on 7/12/2017.
 */

public class FlutterNameRecordingDialog extends BaseResizableDialog {

    private static final String DATA_LOG_DETAILS_KEY = "data_log_details_key";
    private static final String SERIALIZABLE_KEY = "serializable_key";
    private static final String WIZARD_ENUM_KEY = "wizard_enum_key";
    private static final String REVIEW_ENABLED_KEY = "review_enabled_key";
    private static final int MAX_BYTES = 15;

    private GlobalHandler globalHandler;
    private Serializable dismissAndDialogRecordListener;
    private DataLogDetails dataLogDetails;
    private boolean isReviewEnabled;
    private Constants.RECORD_DATA_WIZARD_TYPE wizardType;

    protected EmojiconEditText dataSetNameText;


    public static FlutterNameRecordingDialog newInstance(Serializable dataLogDetails, Serializable serializable, Constants.RECORD_DATA_WIZARD_TYPE wizardType, boolean isReviewEnabled) {
        FlutterNameRecordingDialog flutterNameRecordingDialog = new FlutterNameRecordingDialog();
        Bundle args = new Bundle();
        args.putSerializable(DATA_LOG_DETAILS_KEY, dataLogDetails);
        args.putSerializable(SERIALIZABLE_KEY, serializable);
        args.putSerializable(WIZARD_ENUM_KEY, wizardType);
        args.putBoolean(REVIEW_ENABLED_KEY, isReviewEnabled);
        flutterNameRecordingDialog.setArguments(args);
        return flutterNameRecordingDialog;
    }


    private boolean testName(String name) {
        boolean result = true;

        for (DataSet dataSet : FileHandler.loadDataSetsFromFile(globalHandler)) {
            if (name.equals(dataSet.getDataName())) {
                dataSetNameText.setError("This name has already been used, use a different name.");
                result = false;
            }
        }
        if (name.equals(globalHandler.dataLoggingHandler.getDataName())) {
            dataSetNameText.setError("This name has already been used, use a different name.");
            result = false;
        }
        if(name.equals("")) {
            dataSetNameText.setError(getString(R.string.empty_data_name));
            result = false;
        }
        if (name.getBytes().length > MAX_BYTES) {
            dataSetNameText.setError(getString(R.string.too_many_bits));
            result = false;
        }

        return result;
    }


    /* OnClick Listeners */


    private View.OnClickListener backOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FlutterTimeRecordDialog flutterTimeRecordDialog = FlutterTimeRecordDialog.newInstance(
                    dataLogDetails, dismissAndDialogRecordListener, wizardType, false
            );
            flutterTimeRecordDialog.show(getActivity().getSupportFragmentManager(), "tag");
            dismiss();
        }
    };


    private View.OnClickListener nextOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (testName(dataSetNameText.getText().toString())) {
                dataLogDetails.setDataLogName(dataSetNameText.getText().toString());
                ReviewRecordingDialog reviewRecordingDialog = ReviewRecordingDialog.newInstance(dataLogDetails, dismissAndDialogRecordListener, wizardType);
                reviewRecordingDialog.show(getActivity().getSupportFragmentManager(), "tag");
                dismiss();
            }
        }
    };


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_flutter_name_recording, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);

        globalHandler = GlobalHandler.getInstance(getActivity());
        dismissAndDialogRecordListener = getArguments().getSerializable(SERIALIZABLE_KEY);
        dataLogDetails = (DataLogDetails) getArguments().getSerializable(DATA_LOG_DETAILS_KEY);
        wizardType = (Constants.RECORD_DATA_WIZARD_TYPE) getArguments().getSerializable(WIZARD_ENUM_KEY);
        isReviewEnabled = getArguments().getBoolean(REVIEW_ENABLED_KEY);

        int backButtonId = Constants.WIZARD_TYPE_TO_CANCEL_BACKGROUND.get(wizardType);
        int backButtonTextId = Constants.WIZARD_TYPE_TO_CANCEL_TEXT.get(wizardType);
        int nextButtonId = Constants.WIZARD_TYPE_TO_NEXT_BACKGROUND.get(wizardType);

        dataSetNameText = (EmojiconEditText) view.findViewById(R.id.edit_data_set_name);
        dataSetNameText.setText(dataLogDetails.getDataLogName());

        dataSetNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().getBytes().length > MAX_BYTES){
                    dataSetNameText.setError(getString(R.string.too_many_bits));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Button back = (Button) view.findViewById(R.id.button_back_cancel);
        Button next = (Button) view.findViewById(R.id.button_next);
        back.setText(getString(R.string.back));
        back.setTextColor(getResources().getColor(backButtonTextId));
        next.setText(isReviewEnabled ? getString(R.string.review_recording) : getString(R.string.next));
        back.setBackground(ContextCompat.getDrawable(getActivity(), backButtonId));
        next.setBackground(ContextCompat.getDrawable(getActivity(), nextButtonId));
        back.setOnClickListener(backOnClick);
        next.setOnClickListener(nextOnClick);

        return builder.create();
    }
}
