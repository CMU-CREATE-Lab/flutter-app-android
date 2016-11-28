package org.cmucreatelab.flutter_android.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.cmucreatelab.flutter_android.R;
import org.cmucreatelab.flutter_android.activities.AppLandingActivity;

/**
 * Created by Steve on 10/4/2016.
 *
 * NoFlutterConnectedDialog
 *
 * A Dialog that shows the user that they cannot navigate the app until they connect to a Flutter.
 */
public class NoFlutterConnectedDialog extends DialogFragment {

    private static final String noFlutterKey = "NO_FLUTTER_KEY";

    public static NoFlutterConnectedDialog newInstance(int description) {
        NoFlutterConnectedDialog noFlutterConnectedDialog = new NoFlutterConnectedDialog();

        Bundle args = new Bundle();
        args.putInt(noFlutterKey, description);
        noFlutterConnectedDialog.setArguments(args);

        return noFlutterConnectedDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        int resourceId = getArguments().getInt(noFlutterKey);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_no_flutter, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setView(view);
        builder.setPositiveButton(R.string.connect_flutter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getActivity(), AppLandingActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        TextView text = (TextView) view.findViewById(R.id.text_no_flutter);
        text.setText(resourceId);

        return builder.create();
    }

}
