package com.crypt.adclock.addeditalarm.dialogs.ringtonepicker;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import com.crypt.adclock.R;
import com.crypt.adclock.di.ActivityScoped;
import com.crypt.adclock.util.RingtoneLoop;

import javax.inject.Inject;

@ActivityScoped
public class RingtonePickerDialog extends AppCompatDialogFragment implements
        RingtonePickerContract.View {
    private static final String KEY_RINGTONE_URI = "key_ringtone_uri";

    private RingtoneManager mRingtoneManager;
    private RingtoneLoop mRingtone;
    private Uri mRingtoneUri;
    @Inject
    RingtonePickerPresenter mPresenter;
    @Inject
    FragmentManager mFragmentManager;

    @Inject
    public RingtonePickerDialog() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mRingtoneUri = savedInstanceState.getParcelable(KEY_RINGTONE_URI);
        }
        mRingtoneManager = new RingtoneManager(getActivity());
        mRingtoneManager.setType(RingtoneManager.TYPE_ALARM);
    }

    @Override
    public final Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return createFrom(builder);
    }

    private AlertDialog createFrom(AlertDialog.Builder builder) {
        Cursor cursor = mRingtoneManager.getCursor();
        int checkedItem = mRingtoneManager.getRingtonePosition(mRingtoneUri);
        String labelColumn = cursor.getColumnName(RingtoneManager.TITLE_COLUMN_INDEX);

        builder.setTitle(R.string.ringtones)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        destroyLocalPlayer();
                        dismiss();
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onOkPressed();
                    }
                })
                .setSingleChoiceItems(cursor, checkedItem,
                        labelColumn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (mRingtone != null) {
                                    destroyLocalPlayer();
                                }
                                // Here, 'which' param refers to the position of the item clicked.
                                mRingtoneUri = mRingtoneManager.getRingtoneUri(which);
                                mRingtone = new RingtoneLoop(getActivity(), mRingtoneUri);
                                mRingtone.play();
                            }
                        });
        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_RINGTONE_URI, mRingtoneUri);
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

    private void onOkPressed() {
        //Stop playing selected ringtone
        destroyLocalPlayer();

        if (mPresenter != null) {
            mPresenter.selectRingtone(mRingtoneUri);
        }
        dismiss();
    }

    @Override
    public void setPresenter(RingtonePickerContract.Presenter presenter) {

    }

    private void destroyLocalPlayer() {
        if (mRingtone != null) {
            mRingtone.stop();
            mRingtone = null;
        }
    }

    @Override
    public void show() {
        super.show(mFragmentManager, "test_tag");
    }
}