package org.cmucreatelab.flutter_android.adapters;

import android.bluetooth.BluetoothDevice;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.classes.ScanResult;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.util.ArrayList;

/**
 * Created by Steve on 5/26/2016.
 */
public class LeDeviceListAdapter extends BaseAdapter {

    private ArrayList<ScanResult> mScanResults;
    private LayoutInflater mInflater;


    private static class ViewHolder {
        public TextView deviceName;
        public TextView deviceAddress;
        public TextView scanRecord;
    }


    public LeDeviceListAdapter(LayoutInflater inflater) {
        super();
        mScanResults = new ArrayList<>();
        mInflater = inflater;
    }


    public void addDevice(ScanResult scanResult) {
        for (ScanResult result : mScanResults) {
            if (result.device.equals(scanResult.device)) {
                result.device = scanResult.device;
                notifyDataSetChanged();
                return;
            }
        }
        Log.d(Constants.LOG_TAG, "Found device " + scanResult.device.getName());
        mScanResults.add(scanResult);
        notifyDataSetChanged();
    }


    public void clearDevices() {
        mScanResults.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mScanResults.size();
    }

    @Override
    public Object getItem(int position) {
        return mScanResults.get(position);
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

        BluetoothDevice device = mScanResults.get(position).device;

        final String deviceName = device.getName();
        if (deviceName != null && deviceName.length() > 0) {
            viewHolder.deviceName.setText(deviceName);
        } else {
            viewHolder.deviceName.setText("Unknown Device");
        }
        viewHolder.deviceAddress.setText(device.getAddress());

        return convertView;
    }
}
