package org.cmucreatelab.flutter_android.ui.dialogs.robots_tab.outputs;

/**
 * Created by mike on 3/21/17.
 *
 * An interface for updating/tracking views in a BaseOutputDialog and whether the link can be saved/removed
 *
 */
public interface DialogStateHelper<T extends BaseOutputDialog> {

    /**
     * This method is to be called every time the dialog view is updated.
     *
     * @param dialog instance of BaseOutputDialog for which the StateHelper is tracking.
     */
    void updateView(T dialog);

    /**
     * Represents whether the dialog should allow remove link button to be enabled.
     *
     * @return true if the settings for a link can be removed, false otherwise
     */
    boolean canRemoveLink();

    /**
     * Represents whether the dialog should allow save link button to be enabled.
     *
     * @return true if the settings for a link can be saved, false otherwise
     */
    boolean canSaveLink();

}
