package org.cmucreatelab.flutter_android.adapters;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.datalogging.DataSet;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.util.ArrayList;

/**
 * Created by Steve on 1/3/2017.
 */
// TODO - Use this once we have a database to populate the data logs saved on a device
public class DataLogListAdapter extends BaseAdapter {


    private ArrayList<DataSet> dataLogs;
    private LayoutInflater layoutInflater;


    private static class ViewHolder {
        public TextView dataLogName;
    }


    public DataLogListAdapter(LayoutInflater inflater) {
        super();
        dataLogs = new ArrayList<>();
        layoutInflater = inflater;
    }


    public void addDevice(DataSet dataSet) {
        Log.d(Constants.LOG_TAG, "Found device " + dataSet.getDataName());
        dataLogs.add(dataSet);
        notifyDataSetChanged();
    }


    public void clearDevices() {
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
            view = layoutInflater.inflate(R.layout.list_item_device, null);
            viewHolder = new ViewHolder();
            viewHolder.dataLogName = (TextView) view.findViewById(R.id.device_name);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        DataSet dataSet = dataLogs.get(i);

        final String dataLogName = dataSet.getDataName();
        if (dataLogName != null && dataLogName.length() > 0) {
            viewHolder.dataLogName.setText(dataLogName);
        } else {
            //viewHolder.dataLogName.setText(R.string.unknown_log);
        }
        viewHolder.dataLogName.setGravity(Gravity.CENTER);

        return view;
    }

}
