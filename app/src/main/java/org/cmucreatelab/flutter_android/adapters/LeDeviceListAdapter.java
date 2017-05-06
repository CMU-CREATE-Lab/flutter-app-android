package org.cmucreatelab.flutter_android.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.flutters.Flutter;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Steve on 5/26/2016.
 *
 * LeDeviceListAdapter
 *
 * An adapter that handles displaying a various amount of flutters to the AppLandingActivity.
 *
 */
public class LeDeviceListAdapter extends BaseAdapter {

    private ArrayList<HashMap<Flutter, Long>> mFlutters;
    private LayoutInflater mInflater;

    private static class ViewHolder {
        public TextView deviceName;
    }


    public LeDeviceListAdapter(LayoutInflater inflater) {
        super();
        mFlutters = new ArrayList<>();
        mInflater = inflater;
    }


    public void addDevice(Flutter flutter) {
        Log.d(Constants.LOG_TAG, "Found device " + flutter.getName());
        HashMap hm = new HashMap<>();
        hm.put(flutter, System.currentTimeMillis());
        mFlutters.add(hm);
        notifyDataSetChanged();
    }

    public void removeDevice(int position) {
        mFlutters.remove(position);
        notifyDataSetChanged();
    }

    public long getLastBroadcastTime(int position) {
        HashMap hm = mFlutters.get(position);
        return (Long) hm.values().iterator().next();
    }

    public void updateLastBroadcastTime(int position, long time) {
        HashMap hm = mFlutters.get(position);
        Flutter flutter = (Flutter) hm.keySet().iterator().next();
        hm.put(flutter, time);
    }

    public void clearDevices() {
        mFlutters.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mFlutters.size();
    }

    @Override
    public Object getItem(int position) {
        HashMap hm = mFlutters.get(position);
        return hm.keySet().iterator().next();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_device, null);
            viewHolder = new ViewHolder();
            viewHolder.deviceName = (TextView) convertView.findViewById(R.id.device_name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Flutter flutter = (Flutter) getItem(position);

        final String deviceName = flutter.getName();
        if (deviceName != null && deviceName.length() > 0) {
            viewHolder.deviceName.setText(deviceName);
        } else {
            viewHolder.deviceName.setText(R.string.unknown_device);
        }

        return convertView;
    }
}
