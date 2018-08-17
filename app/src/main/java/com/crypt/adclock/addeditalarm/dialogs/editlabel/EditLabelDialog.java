package com.crypt.adclock.addeditalarm.dialogs.editlabel;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.crypt.adclock.R;

import javax.inject.Inject;


public class EditLabelDialog extends AppCompatDialogFragment implements
        EditLabelContract.View {
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

        mEditText = new AppCompatEditText(getActivity());
        // Views must have IDs set to automatically save instance state
        mEditText.setId(R.id.label);
        mEditText.setText(mInitialText);
        mEditText.setInputType(
                EditorInfo.TYPE_CLASS_TEXT // Needed or else we won't get automatic spacing
                        | EditorInfo.TYPE_TEXT_FLAG_CAP_SENTENCES);         // between words

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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
                })
                .setTitle(R.string.label)
                .setView(mEditText);

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