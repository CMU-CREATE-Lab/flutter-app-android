package org.cmucreatelab.flutter_android.ui.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.AppLandingActivity;
import org.cmucreatelab.flutter_android.activities.abstract_activities.BaseNavigationActivity;
import org.cmucreatelab.flutter_android.classes.Session;
import org.cmucreatelab.flutter_android.helpers.GlobalHandler;
import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pdille on 3/3/2017.
 * <p>
 * FlutterStatusDialog
 * <p>
 * A Dialog that shows the current connection status of a Flutter and allows the user to connect
 * or disconnect depending upon the current state.
 */
public class FlutterStatusDialog extends BaseResizableDialog {

    private static final String flutterStatusKey = "FLUTTER_STATUS_KEY";

    private GlobalHandler globalHandler;
    private Session session;

    private static FlutterStatusDialog newInstance(int description) {
        FlutterStatusDialog flutterStatusDialog = new FlutterStatusDialog();

        Bundle args = new Bundle();
        args.putInt(flutterStatusKey, description);
        flutterStatusDialog.setArguments(args);

        return flutterStatusDialog;
    }


    public static void displayDialog(BaseNavigationActivity activity, int description) {
        FlutterStatusDialog flutterStatusDialog = FlutterStatusDialog.newInstance(description);
        flutterStatusDialog.show(activity.getSupportFragmentManager(), "tag");
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        int resourceId = getArguments().getInt(flutterStatusKey);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_flutter_status, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        ButterKnife.bind(this, view);

        globalHandler = GlobalHandler.getInstance(getActivity().getApplicationContext());
        this.session = globalHandler.sessionHandler.getSession();

        TextView flutterStatusName = (TextView) view.findViewById(R.id.text_flutter_status_name);
        TextView flutterStatusText = (TextView) view.findViewById(R.id.text_flutter_status);
        ImageView flutterStatusIcon = (ImageView) view.findViewById(R.id.image_flutter_status_pic);
        Button flutterConnectDisconnect = (Button) view.findViewById(R.id.button_flutter_connect_disconnect);

        if (!globalHandler.melodySmartDeviceHandler.isConnected()) {
            flutterStatusText.setText(R.string.connection_disconnected);
            flutterStatusText.setTextColor(Color.GRAY);
            flutterStatusIcon.setImageResource(R.drawable.flutterdisconnectgraphic);
            flutterConnectDisconnect.setBackgroundResource(R.drawable.round_green_button);
            flutterConnectDisconnect.setText(R.string.connect_flutter);
        } else {
            flutterStatusText.setText(R.string.connection_connected);
            flutterStatusText.setTextColor(getResources().getColor(R.color.fluttergreen));
            String flutterName = session.getFlutter().getName();
            flutterStatusName.setText(flutterName);
            flutterStatusIcon.setImageResource(R.drawable.flutterconnectgraphic);
            flutterConnectDisconnect.setBackgroundResource(R.drawable.round_reddish_button);
            flutterConnectDisconnect.setText(R.string.disconnect);
        }

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(convertDpToPx(500), ViewGroup.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onPause() {
        super.onPause();
        Dialog dialog = getDialog();
        if (dialog != null)
            dialog.dismiss();
    }

    @OnClick(R.id.button_flutter_connect_disconnect)
    public void onClickConnectDisconnect() {
        Log.d(Constants.LOG_TAG, "onClickConnectDisconnect");
        if (globalHandler.melodySmartDeviceHandler.isConnected()) {
            globalHandler.melodySmartDeviceHandler.disconnect(false);
        } else {
            Intent intent = new Intent(getActivity(), AppLandingActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }
}
