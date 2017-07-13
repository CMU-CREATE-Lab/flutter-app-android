package org.cmucreatelab.flutter_android.ui.dialogs.DataLogsTab;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ListView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.DataLogsActivity;
import org.cmucreatelab.flutter_android.adapters.DataLogListAdapterCleanUp;
import org.cmucreatelab.flutter_android.classes.datalogging.DataSet;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.datalogging.CleanUpAfterState;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.helpers.static_classes.FileHandler;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialog;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Steve on 2/1/2017.
 */
public class CleanUpConfirmationDialog extends BaseResizableDialog {

    private static final String ACTIVITY_KEY = "activity_key";
    private static final String DATA_LOGS_KEY = "data_logs_key";

    private GlobalHandler globalHandler;
    private DataLogListAdapterCleanUp confirmationAdapter;
    private ListView logsToDelete;

    private DataLogsActivity dataLogsActivity;
    private DataSet[] dataSets;

    private DataSet deletedDataSet;


    public static CleanUpConfirmationDialog newInstance(Serializable activity, Serializable[] dataSets) {
        CleanUpConfirmationDialog result = new CleanUpConfirmationDialog();

        Bundle args = new Bundle();
        args.putSerializable(ACTIVITY_KEY, activity);
        args.putSerializable(DATA_LOGS_KEY, dataSets);
        result.setArguments(args);

        return result;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_clean_up_logs_confirmation, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ButterKnife.bind(this, view);
        globalHandler = GlobalHandler.getInstance(getActivity());

        dataLogsActivity = (DataLogsActivity) getArguments().getSerializable(ACTIVITY_KEY);
        dataSets = (DataSet[]) getArguments().getSerializable(DATA_LOGS_KEY);
        confirmationAdapter = new DataLogListAdapterCleanUp(inflater);
        logsToDelete = (ListView) view.findViewById(R.id.list_logs_to_delete);
        logsToDelete.setAdapter(confirmationAdapter);

        for (DataSet dataSet : dataSets) {
            confirmationAdapter.addDataLog(dataSet);
        }

        // A little ugly but I didn't want to make a whole new adapter just to list DataSets that have the circle_selected image instead of circle_not_selected
        logsToDelete.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                for (int i = 0; i < logsToDelete.getChildCount(); i++) {
                    View temp = logsToDelete.getChildAt(i);
                    ((ImageView) temp.findViewById(R.id.image_selector)).setImageResource(R.drawable.check_mark);
                }
            }
        });

        return builder.create();
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        globalHandler.sessionHandler.createProgressDialog(dataLogsActivity);
        globalHandler.sessionHandler.updateProgressDialogMessage(dataLogsActivity, getString(R.string.loading_data));
        dataLogsActivity.getDataLogsUpdateHelper().registerStateAndUpdatePoints(new CleanUpAfterState(dataLogsActivity, deletedDataSet));
    }

    @OnClick(R.id.button_cancel)
    public void onClickCancel() {
        Log.d(Constants.LOG_TAG, "CleanUpConfirmationDialog.onClickCancel");
        this.dismiss();
    }


    @OnClick(R.id.button_delete_logs)
    public void onClickDeleteLogs() {
        Log.d(Constants.LOG_TAG, "CleanUpConfirmationDialog.onClickDeleteLogs");
        for (DataSet dataSet : dataSets) {
            // if the dataSet is equal to the one on the flutter, then use DataLoggingHandler to communicate with the flutter.
            if (dataSet.equals(dataLogsActivity.getDataLogsUpdateHelper().getDataSetOnFlutter())) {
                globalHandler.dataLoggingHandler.deleteLog();
                deletedDataSet = dataLogsActivity.getDataLogsUpdateHelper().getDataSetOnFlutter();
            } else {
                FileHandler.deleteFile(globalHandler, dataSet);
                deletedDataSet = dataSet;
            }
        }
        this.dismiss();
    }

}
