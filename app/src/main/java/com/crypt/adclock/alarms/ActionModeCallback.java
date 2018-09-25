package com.crypt.adclock.alarms;

import android.content.Context;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.crypt.adclock.R;

import androidx.recyclerview.selection.SelectionTracker;

public class ActionModeCallback implements ActionMode.Callback {

    private final Context context;
    private final SelectionTracker selectionTracker;
    private OnActionItemClickedListener mClickListener;

    public interface OnActionItemClickedListener {

        void removeButtonClicked();

        void cancelSelectionButtonClicked();

    }


    public ActionModeCallback(Context context, SelectionTracker selectionTracker,
                              OnActionItemClickedListener clickListener) {
        this.context = context;
        this.selectionTracker = selectionTracker;
        this.mClickListener = clickListener;
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        actionMode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.item1:
                mClickListener.removeButtonClicked();
                break;
            case R.id.item2:
                mClickListener.cancelSelectionButtonClicked();
                break;
        }
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        selectionTracker.clearSelection();
    }

}
