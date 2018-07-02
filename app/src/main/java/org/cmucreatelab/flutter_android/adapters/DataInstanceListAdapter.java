package org.cmucreatelab.flutter_android.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.datalogging.DataPoint;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.util.ArrayList;

/**
 * Created by Steve on 1/3/2017.
 *
 * DataInstanceListAdapter
 *
 * An adapter to handle displaying the data points in the data log screen.
 *
 */
public class DataInstanceListAdapter extends BaseAdapter {

    private ArrayList<DataPoint> dataPoints;
    private LayoutInflater layoutInflater;


    private static class ViewHolder {
        public ImageView selector;
        public TextView date;
        public TextView time;
        public TextView sensor1Value;
        public TextView sensor2Value;
        public TextView sensor3Value;
    }


    public DataInstanceListAdapter(LayoutInflater inflater) {
        super();
        dataPoints = new ArrayList<>();
        layoutInflater = inflater;
    }


    public void addDataPoint(DataPoint dataPoint) {
        Log.d(Constants.LOG_TAG, "Found  data point " + dataPoint.getTime());
        dataPoints.add(dataPoint);
        notifyDataSetChanged();
    }


    public void clearDataPoints() {
        dataPoints.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return dataPoints.size();
    }


    @Override
    public Object getItem(int i) {
        return dataPoints.get(i);
    }


    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_data_log_instance, null);
            viewHolder = new ViewHolder();
            viewHolder.selector = (ImageView) view.findViewById(R.id.image_selector);
            viewHolder.date = (TextView) view.findViewById(R.id.text_date);
            viewHolder.time = (TextView) view.findViewById(R.id.text_time);
            viewHolder.sensor1Value = (TextView) view.findViewById(R.id.text_sensor_1_value);
            viewHolder.sensor2Value = (TextView) view.findViewById(R.id.text_sensor_2_value);
            viewHolder.sensor3Value = (TextView) view.findViewById(R.id.text_sensor_3_value);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        DataPoint dataPoint = dataPoints.get(i);

        viewHolder.selector.setImageResource(R.drawable.circle_not_selected);
        final String date = dataPoint.getDate();
        final String time = dataPoint.getTime();
        final String sensor1Value = dataPoint.getSensor1Value();
        final String sensor2Value = dataPoint.getSensor2Value();
        final String sensor3Value = dataPoint.getSensor3Value();
        if (date != null && date.length() > 0) {
            viewHolder.date.setText(date);
            viewHolder.time.setText(time);
            viewHolder.sensor1Value.setText(sensor1Value);
            viewHolder.sensor2Value.setText(sensor2Value);
            viewHolder.sensor3Value.setText(sensor3Value);
        }

        return view;
    }

}
