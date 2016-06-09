package org.cmucreatelab.flutter_android.adapters;

import android.bluetooth.BluetoothDevice;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.Device;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.util.ArrayList;

/**
 * Created by Steve on 5/26/2016.
 */
public class LeDeviceListAdapter extends BaseAdapter {

    private ArrayList<Device> mDevices;
    private LayoutInflater mInflater;


    private static class ViewHolder {
        public TextView deviceName;
        public TextView deviceAddress;
        public TextView scanRecord;
    }


    public LeDeviceListAdapter(LayoutInflater inflater) {
        super();
        mDevices = new ArrayList<>();
        mInflater = inflater;
    }


    public void addDevice(Device device) {
        Log.d(Constants.LOG_TAG, "Found device " + device.getName());
        mDevices.add(device);
        notifyDataSetChanged();
    }


    public void clearDevices() {
        mDevices.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mDevices.size();
    }

    @Override
    public Object getItem(int position) {
        return mDevices.get(position);
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

        Device device = mDevices.get(position);

        final String deviceName = device.getName();
        if (deviceName != null && deviceName.length() > 0) {
            viewHolder.deviceName.setText(deviceName);
        } else {
            viewHolder.deviceName.setText(R.string.unknown_device);
        }
        viewHolder.deviceAddress.setText(device.getDevice().getAddress());

        return convertView;
    }
}
