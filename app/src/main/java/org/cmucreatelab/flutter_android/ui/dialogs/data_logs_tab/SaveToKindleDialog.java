package org.cmucreatelab.flutter_android.ui.dialogs.data_logs_tab;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialog;

import java.io.Serializable;

/**
 * Created by Steve on 3/1/2017.
 */

public class SaveToKindleDialog extends BaseResizableDialog {

    private static final String ACTIVITY_KEY = "activity_key";
    private static final String DATA_NAME_KEY = "data_name_key";
    private static final String FLUTTER_NAME_KEY = "flutter_name_key";

    private SaveToKindleListener saveToKindleListener;


    public static SaveToKindleDialog newInstance(Serializable activity, String dataName, String kindleName) {
        SaveToKindleDialog saveToKindleDialog = new SaveToKindleDialog();

        Bundle args = new Bundle();
        args.putSerializable(ACTIVITY_KEY, activity);
        args.putString(DATA_NAME_KEY, dataName);
        args.putString(FLUTTER_NAME_KEY, kindleName);
        saveToKindleDialog.setArguments(args);

        return saveToKindleDialog;
    }


    // OnClick Listeners


    private View.OnClickListener buttonSaveListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(Constants.LOG_TAG, "SaveToKindleDialog.onClickButtonSave");
            saveToKindleListener.onSaveToKindle();
            dismiss();
        }
    };


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        saveToKindleListener = (SaveToKindleListener) getArguments().getSerializable(ACTIVITY_KEY);
        String dataName = getArguments().getString(DATA_NAME_KEY);
        String flutterName = getArguments().getString(FLUTTER_NAME_KEY);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_save_to_kindle, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);

        TextView details = (TextView) view.findViewById(R.id.text_details);
        details.setText("\"" + dataName + "\" is currently stored on " + flutterName + ".");

        Button buttonSave = (Button) view.findViewById(R.id.button_save);
        buttonSave.setOnClickListener(buttonSaveListener);

        return builder.create();
    }


    public interface SaveToKindleListener {
        public void onSaveToKindle();
    }
}
