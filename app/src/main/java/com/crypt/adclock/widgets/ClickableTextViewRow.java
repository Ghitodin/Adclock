package com.crypt.adclock.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crypt.adclock.R;

public class ClickableTextViewRow extends LinearLayout {
    private TextView mAdditionalText;
    private Button mBtnTitle;
    private ImageView mSomeImage;

    private String mText;
    private String mTitle;

    public ClickableTextViewRow(Context context) {
        super(context);
    }

    public ClickableTextViewRow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        initUi();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray mTypedArray = getContext().obtainStyledAttributes(attrs,
                R.styleable.ClickableTextViewRow, 0, 0);

        try {
            mText = mTypedArray.getString(R.styleable.ClickableTextViewRow_additional_text);
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
    }

    public void setTitle(@StringRes int res) {
        setTitle(getResources().getString(res));
    }

    public void setTitle(String title) {
        mTitle = title;
        mBtnTitle.setText(mTitle);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setText(@StringRes int res) {
        setTitle(getResources().getString(res));
    }

    public void setText(String value) {
        mText = value;
        mAdditionalText.setText(mText);
    }

    public String getText() {
        return mText;
    }
}