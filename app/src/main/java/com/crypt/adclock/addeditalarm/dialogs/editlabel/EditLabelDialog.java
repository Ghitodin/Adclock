package com.crypt.adclock.addeditalarm.dialogs.editlabel;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.crypt.adclock.R;

import javax.inject.Inject;


public class EditLabelDialog extends AppCompatDialogFragment implements
        EditLabelContract.View {
    private ConstraintLayout mMainDialog;
    private EditText mEditText;
    private CharSequence mInitialText;
    @Inject
    EditLabelPresenter mPresenter;
    @Inject
    FragmentManager mFragmentManager;

    @Inject
    public EditLabelDialog() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null)
            mPresenter.takeView(this);
    }

    @Override
    public void onDestroy() {
        if (mPresenter != null)
            mPresenter.dropView();
        super.onDestroy();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        mMainDialog = (ConstraintLayout) inflater.inflate(
                R.layout.label_dialog_layout,
                null
        );

        mEditText = mMainDialog.findViewById(R.id.label);
        mEditText.setText(mInitialText);

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onOkPressed();
                    }
                })
                .setTitle(R.string.label)
                .setView(mMainDialog);

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT);
                mEditText.setSelection(0, mEditText.length());
            }
        });
        return dialog;
    }

    private void onOkPressed() {
        if (mPresenter != null) {
            // If we passed the text back as an Editable (subtype of CharSequence
            // used in EditText), then there may be text formatting left in there,
            // which we don't want.
            mPresenter.setLabel(mEditText.getText().toString());
        }
        dismiss();
    }

    @Override
    public void show(String initialText) {
        mInitialText = initialText;
        super.show(mFragmentManager, "test_tag");
    }

    @Override
    public void setPresenter(EditLabelContract.Presenter presenter) {

    }
}