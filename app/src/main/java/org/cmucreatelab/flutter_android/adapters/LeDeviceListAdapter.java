package org.cmucreatelab.flutter_android.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.Flutter;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.util.ArrayList;

/**
 * Created by Steve on 5/26/2016.
 *
 * LeDeviceListAdapter
 *
 * An adapter that handles displaying a various amount of flutters to the ScanActivity.
 *
 */
public class LeDeviceListAdapter extends BaseAdapter {

    private ArrayList<Flutter> mFlutters;
    private LayoutInflater mInflater;


    private static class ViewHolder {
        public TextView deviceName;
        public TextView deviceAddress;
        public TextView scanRecord;
    }


    public LeDeviceListAdapter(LayoutInflater inflater) {
        super();
        mFlutters = new ArrayList<>();
        mInflater = inflater;
    }


    public void addDevice(Flutter flutter) {
        Log.d(Constants.LOG_TAG, "Found device " + flutter.getName());
        mFlutters.add(flutter);
        notifyDataSetChanged();
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
        return mFlutters.get(position);
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
            viewHolder.deviceAddress = (TextView) convertView.findViewById(R.id.device_address);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Flutter flutter = mFlutters.get(position);

        final String deviceName = flutter.getName();
        if (deviceName != null && deviceName.length() > 0) {
            viewHolder.deviceName.setText(deviceName);
        } else {
            viewHolder.deviceName.setText(R.string.unknown_device);
        }
        viewHolder.deviceAddress.setText(flutter.getDevice().getAddress());

        return convertView;
    }
}
