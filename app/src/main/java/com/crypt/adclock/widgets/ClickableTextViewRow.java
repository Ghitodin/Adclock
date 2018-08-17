package com.crypt.adclock.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crypt.adclock.R;

import org.jetbrains.annotations.Contract;

public class ClickableTextViewRow extends LinearLayout implements View.OnClickListener {
    private TextView mAdditionalText;
    private Button mBtnTitle;
    private ImageView mSomeImage;

    private String mText;
    private String mTitle;


    public ClickableTextViewRow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        initUi();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.mState = mText;
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        mText = ((SavedState)state).mState;
        setText(mText);
        super.onRestoreInstanceState(((SavedState) state).getSuperState());
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray mTypedArray = getContext().obtainStyledAttributes(attrs,
                R.styleable.ClickableTextViewRow, 0, 0);

        try {
            mTitle = mTypedArray.getString(R.styleable.ClickableTextViewRow_txt_title);
        } finally {
            mTypedArray.recycle();
        }
    }

    private void initUi() {
        LayoutInflater.from(getContext()).inflate(R.layout.clickable_text_view_row, this, true);

        mAdditionalText = findViewById(R.id.tv_additional_text);
        mBtnTitle = findViewById(R.id.btn_title);
        mSomeImage = findViewById(R.id.iv_arrow_right);

        mAdditionalText.setText(mText);
        mBtnTitle.setText(mTitle);

        mAdditionalText.setOnClickListener(this);
        mBtnTitle.setOnClickListener(this);
    }

    public void setTitle(@StringRes int res) {
        setTitle(getResources().getString(res));
    }

    public void setTitle(@NonNull String title) {
        mTitle = title;
        mBtnTitle.setText(mTitle);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setText(@NonNull String value) {
        mText = value;
        mAdditionalText.setText(mText);
    }

    public String getText() {
        return mText;
    }

    @Override
    public void onClick(View v) {
        this.performClick();
    }

    public static class SavedState extends BaseSavedState {
        String mState;

        SavedState(Parcelable source) {
            super(source);
        }

        @Override
        public void writeToParcel(Parcel out, int flag) {
            super.writeToParcel(out, flag);
            out.writeString(mState);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @NonNull
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @NonNull
            @Contract(pure = true)
            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        private SavedState(Parcel source) {
            super(source);
            mState = source.readString();
        }
    }
}