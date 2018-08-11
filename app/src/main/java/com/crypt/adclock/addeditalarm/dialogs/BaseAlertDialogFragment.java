package com.crypt.adclock.addeditalarm.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import com.crypt.adclock.R;

public abstract class BaseAlertDialogFragment extends AppCompatDialogFragment {
    // We need successors to override only positive action, as the negative action is assumed to
    // cause dialog to dismiss in all cases

    protected abstract void onOkPressed();

    @NonNull
    @Override
    public final Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // So we just call parent`s method here
                        dismiss();
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onOkPressed();
                    }
                });
        return createFrom(builder);
    }

    protected AlertDialog createFrom(AlertDialog.Builder builder) {
        return builder.create();
    }
}
