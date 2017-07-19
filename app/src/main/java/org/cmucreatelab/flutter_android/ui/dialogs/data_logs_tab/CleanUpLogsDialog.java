package org.cmucreatelab.flutter_android.ui.dialogs.data_logs_tab;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.DataLogsActivity;
import org.cmucreatelab.flutter_android.adapters.DataLogListAdapterCleanUp;
import org.cmucreatelab.flutter_android.classes.datalogging.DataSet;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;
import org.cmucreatelab.flutter_android.ui.dialogs.BaseResizableDialog;
import org.cmucreatelab.flutter_android.ui.dialogs.DismissDialogListener;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Steve on 2/1/2017.
 */
public class CleanUpLogsDialog extends BaseResizableDialog {

    private static final String ACTIVITY_KEY = "activity_key";
    private static final String DATA_SET_FLUTTER_KEY = "data_set_flutter_key";
    private static final String DATA_SETS_DEVICE_KEY = "data_sets_device_key";
    private static final String DISMISS_DIALOG_LISTENER_KEY = "dismiss_dialog_listener_key";

    private static final double MILLISECONDS_IN_A_WEEK = 6.048e8;
    private static final double MILLISECONDS_IN_A_MONTH = 2.628e+9;
    private static final double MILLISECONDS_IN_YEAR = 3.154e+10;

    private DismissDialogListener dismissDialogListener;
    private DataLogsActivity baseNavigationActivity;
    private DataSet dataSetOnFlutter;
    private DataSet[] dataSetsOnDevice;

    private ArrayList<DataSet> thisWeeksDataSets, thisMonthsDataSets, thisYearsDataSets, yearPlusDataSets;

    private ArrayList<DataSet> logsToDelete;

    private ListView thisWeek, thisMonth, thisYear, yearPlus;
    private LinearLayout weekContainer, monthContainer, yearContainer, yearPlusContainer;
    private Button buttonDelete;

    private DataLogListAdapterCleanUp thisWeekAdapter, thisMonthAdapter, thisYearAdapter, yearPlusAdapter;


    private boolean isWithinThisWeek(DataSet dataSet) {
        boolean result = false;

        try {
            Date today = new Date();
            ArrayList<String> keys = dataSet.getKeys();
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            Date dataLogDate = df.parse(dataSet.getData().get(keys.get(0)).getDate());
            if (today.getTime() - dataLogDate.getTime() < MILLISECONDS_IN_A_WEEK) {
                result = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }


    private boolean isWithinThisMonth(DataSet dataSet) {
        boolean result = false;

        try {
            Date today = new Date();
            ArrayList<String> keys = dataSet.getKeys();
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            Date dataLogDate = df.parse(dataSet.getData().get(keys.get(0)).getDate());
            if (today.getTime() - dataLogDate.getTime() < MILLISECONDS_IN_A_MONTH) {
                result = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }


    private boolean isWithinThisYear(DataSet dataSet) {
        boolean result = false;

        try {
            Date today = new Date();
            ArrayList<String> keys = dataSet.getKeys();
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            Date dataLogDate = df.parse(dataSet.getData().get(keys.get(0)).getDate());
            if (today.getTime() - dataLogDate.getTime() < MILLISECONDS_IN_YEAR) {
                result = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }


    private void selectionHelper(ListView list, ArrayList<DataSet> dataSets, View view, int index) {
        if (list.isItemChecked(index)) {
            ((ImageView) view.findViewById(R.id.image_selector)).setImageResource(R.drawable.check_mark);
            logsToDelete.add(dataSets.get(index));
        } else {
            ((ImageView) view.findViewById(R.id.image_selector)).setImageResource(R.drawable.circle_not_selected);
            logsToDelete.remove(dataSets.get(index));
        }

        if (logsToDelete.size() > 0) {
            buttonDelete.setEnabled(true);
        } else {
            buttonDelete.setEnabled(false);
        }
    }


    private AdapterView.OnItemClickListener thisWeekClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            selectionHelper(thisWeek, thisWeeksDataSets, view, i);
        }
    };


    private AdapterView.OnItemClickListener thisMonthClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            selectionHelper(thisMonth, thisMonthsDataSets, view, i);
        }
    };


    private AdapterView.OnItemClickListener thisYearClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            selectionHelper(thisYear, thisYearsDataSets, view, i);
        }
    };


    private AdapterView.OnItemClickListener yearPlusClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            selectionHelper(yearPlus, thisYearsDataSets, view, i);
        }
    };


    public static CleanUpLogsDialog newInstance(Serializable activity, Serializable dataSetOnFlutter, Serializable[] dataSetsOnDevice) {
        CleanUpLogsDialog result = new CleanUpLogsDialog();

        Bundle args = new Bundle();
        args.putSerializable(ACTIVITY_KEY, activity);
        args.putSerializable(DATA_SET_FLUTTER_KEY, dataSetOnFlutter);
        args.putSerializable(DATA_SETS_DEVICE_KEY, dataSetsOnDevice);
        args.putSerializable(DISMISS_DIALOG_LISTENER_KEY, activity);
        result.setArguments(args);

        return result;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_clean_up_logs, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ButterKnife.bind(this, view);

        dismissDialogListener = (DismissDialogListener) getArguments().getSerializable(DISMISS_DIALOG_LISTENER_KEY);
        baseNavigationActivity = (DataLogsActivity) getArguments().getSerializable(ACTIVITY_KEY);
        dataSetOnFlutter = (DataSet) getArguments().getSerializable(DATA_SET_FLUTTER_KEY);
        dataSetsOnDevice = (DataSet[]) getArguments().getSerializable(DATA_SETS_DEVICE_KEY);

        thisWeek = (ListView) view.findViewById(R.id.list_this_week);
        thisMonth = (ListView) view.findViewById(R.id.list_this_month);
        thisYear = (ListView) view.findViewById(R.id.list_this_year);
        yearPlus = (ListView) view.findViewById(R.id.list_over_a_year);
        thisWeek.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        thisMonth.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        thisYear.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        yearPlus.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        weekContainer = (LinearLayout) view.findViewById(R.id.linear_week_container);
        monthContainer = (LinearLayout) view.findViewById(R.id.linear_month_container);
        yearContainer = (LinearLayout) view.findViewById(R.id.linear_year_container);
        yearPlusContainer = (LinearLayout) view.findViewById(R.id.linear_year_plus_container);

        thisWeekAdapter = new DataLogListAdapterCleanUp(inflater);
        thisMonthAdapter = new DataLogListAdapterCleanUp(inflater);
        thisYearAdapter = new DataLogListAdapterCleanUp(inflater);
        yearPlusAdapter = new DataLogListAdapterCleanUp(inflater);


        thisWeek.setAdapter(thisWeekAdapter);
        thisMonth.setAdapter(thisMonthAdapter);
        thisYear.setAdapter(thisYearAdapter);
        yearPlus.setAdapter(yearPlusAdapter);
        thisWeek.setOnItemClickListener(thisWeekClickListener);
        thisMonth.setOnItemClickListener(thisMonthClickListener);
        thisYear.setOnItemClickListener(thisYearClickListener);
        yearPlus.setOnItemClickListener(yearPlusClickListener);

        thisWeeksDataSets = new ArrayList<>();
        thisMonthsDataSets = new ArrayList<>();
        thisYearsDataSets = new ArrayList<>();
        yearPlusDataSets = new ArrayList<>();
        logsToDelete = new ArrayList<>();

        buttonDelete = (Button) view.findViewById(R.id.button_delete_logs);

        if (dataSetOnFlutter != null) {
            if (isWithinThisWeek(dataSetOnFlutter)) {
                weekContainer.setVisibility(View.VISIBLE);
                thisWeeksDataSets.add(dataSetOnFlutter);
                thisWeekAdapter.addDataLog(dataSetOnFlutter);
            } else if (isWithinThisMonth(dataSetOnFlutter)) {
                monthContainer.setVisibility(View.VISIBLE);
                thisMonthsDataSets.add(dataSetOnFlutter);
                thisMonthAdapter.addDataLog(dataSetOnFlutter);
            } else if (isWithinThisYear(dataSetOnFlutter)) {
                yearContainer.setVisibility(View.VISIBLE);
                thisYearsDataSets.add(dataSetOnFlutter);
                thisYearAdapter.addDataLog(dataSetOnFlutter);
            } else {
                yearPlusContainer.setVisibility(View.VISIBLE);
                yearPlusDataSets.add(dataSetOnFlutter);
                yearPlusAdapter.addDataLog(dataSetOnFlutter);
            }
        }

        for (DataSet dataSet : dataSetsOnDevice) {
            if (dataSet != null) {
                if (isWithinThisWeek(dataSet)) {
                    weekContainer.setVisibility(View.VISIBLE);
                    thisWeeksDataSets.add(dataSet);
                    thisWeekAdapter.addDataLog(dataSet);
                } else if (isWithinThisMonth(dataSet)) {
                    monthContainer.setVisibility(View.VISIBLE);
                    thisMonthsDataSets.add(dataSet);
                    thisMonthAdapter.addDataLog(dataSet);
                } else if (isWithinThisYear(dataSet)) {
                    yearContainer.setVisibility(View.VISIBLE);
                    thisYearsDataSets.add(dataSet);
                    thisYearAdapter.addDataLog(dataSet);
                } else {
                    yearPlusContainer.setVisibility(View.VISIBLE);
                    yearPlusDataSets.add(dataSet);
                    yearPlusAdapter.addDataLog(dataSet);
                }
            }
        }

        return builder.create();
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (dismissDialogListener != null)
            dismissDialogListener.onDialogDismissed();
    }

    @OnClick(R.id.button_delete_logs)
    public void onClickDeleteLogs() {
        if (logsToDelete.size() > 0) {
            Log.d(Constants.LOG_TAG, "CleanUpLogsDialog.onClickDeleteLogs");
            CleanUpConfirmationDialog cleanUpConfirmationDialog = CleanUpConfirmationDialog.newInstance(baseNavigationActivity, logsToDelete.toArray(new DataSet[logsToDelete.size()]));
            cleanUpConfirmationDialog.show(getFragmentManager(), "tag");
            dismissDialogListener = null;
            this.dismiss();
        }
    }

}
