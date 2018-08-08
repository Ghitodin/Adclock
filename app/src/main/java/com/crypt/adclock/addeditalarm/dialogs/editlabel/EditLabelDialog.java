package com.crypt.adclock.addeditalarm.dialogs.editlabel;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.crypt.adclock.R;
import com.crypt.adclock.addeditalarm.dialogs.BaseAlertDialogFragment;


public class EditLabelDialog extends BaseAlertDialogFragment{
    private EditText mEditText;
    private OnLabelSetListener mOnLabelSetListener;
    private CharSequence mInitialText;

    public interface OnLabelSetListener {
        void onLabelSet(String label);
    }

    public static EditLabelDialog newInstance(OnLabelSetListener l, CharSequence text) {
        EditLabelDialog dialog = new EditLabelDialog();
        dialog.mOnLabelSetListener = l;
        dialog.mInitialText = text;
        return dialog;
    }

    @Override
    protected AlertDialog createFrom(AlertDialog.Builder builder) {
        mEditText = new AppCompatEditText(getActivity());
        // Views must have IDs set to automatically save instance state
        mEditText.setId(R.id.label);
        mEditText.setText(mInitialText);
        mEditText.setInputType(
                EditorInfo.TYPE_CLASS_TEXT // Needed or else we won't get automatic spacing
                        | EditorInfo.TYPE_TEXT_FLAG_CAP_SENTENCES);         // between words

        builder.setTitle(R.string.label)
                .setView(mEditText);

        AlertDialog dialog = super.createFrom(builder);
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

    @Override
    protected void onOkPressed() {
        if (mOnLabelSetListener != null) {
            // If we passed the text back as an Editable (subtype of CharSequence
            // used in EditText), then there may be text formatting left in there,
            // which we don't want.
            mOnLabelSetListener.onLabelSet(mEditText.getText().toString());
        }
        dismiss();
    }

    public void setOnLabelSetListener(OnLabelSetListener l) {
        mOnLabelSetListener = l;
    }

}