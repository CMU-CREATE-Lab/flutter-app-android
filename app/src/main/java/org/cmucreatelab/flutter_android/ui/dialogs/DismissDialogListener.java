package org.cmucreatelab.flutter_android.ui.dialogs;

/**
 * Created by Steve on 3/13/2017.
 *
 * DismissDialogListener
 *
 * A listener to implement for doing actions after a dialog is dismissed.
 */
public interface DismissDialogListener {
    public String DISMISS_KEY = "dismiss_key";
    public void onDialogDismissed();
}
