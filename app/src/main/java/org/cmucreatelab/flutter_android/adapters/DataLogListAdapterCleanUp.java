package org.cmucreatelab.flutter_android.adapters;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.datalogging.DataSet;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.util.ArrayList;

/**
 * Created by Steve on 2/1/2017.
 *
 * DataLogListAdapterCleanUp
 *
 * An adapter to display the logs that can be cleaned up.
 */
public class DataLogListAdapterCleanUp extends BaseAdapter {

    private ArrayList<DataSet> dataLogs;
    private LayoutInflater layoutInflater;


    private static class ViewHolder {
        public ImageView selector;
        public TextView dataLogName;
        public TextView numberOfPoints;
    }


    public DataLogListAdapterCleanUp(LayoutInflater inflater) {
        super();
        dataLogs = new ArrayList<>();
        layoutInflater = inflater;
    }


    public void addDataLog(DataSet dataSet) {
        Log.d(Constants.LOG_TAG, "Found data set " + dataSet.getDataName());
        dataLogs.add(dataSet);
        notifyDataSetChanged();
    }


    public void clearDataLogs() {
        dataLogs.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return dataLogs.size();
    }


    @Override
    public Object getItem(int i) {
        return dataLogs.get(i);
    }


    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_data_log_clean_up, null);
            viewHolder = new ViewHolder();
            viewHolder.selector = (ImageView) view.findViewById(R.id.image_selector);
            viewHolder.dataLogName = (TextView) view.findViewById(R.id.text_current_log_name);
            viewHolder.numberOfPoints = (TextView) view.findViewById(R.id.text_num_points);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        DataSet dataSet = dataLogs.get(i);

        viewHolder.selector.setImageResource(R.drawable.circle_not_selected);
        final String dataLogName = dataSet.getDataName();
        final int numPoints = dataSet.getData().size();
        if (dataLogName != null && dataLogName.length() > 0) {
            viewHolder.dataLogName.setText(dataLogName);
            viewHolder.numberOfPoints.setText(String.valueOf(numPoints));
        } else {
            //viewHolder.dataLogName.setText(R.string.unknown_log);
        }
        viewHolder.dataLogName.setGravity(Gravity.CENTER);

        return view;
    }

}
