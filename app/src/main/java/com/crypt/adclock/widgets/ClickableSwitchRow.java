package com.crypt.adclock.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crypt.adclock.R;

public class ClickableSwitchRow extends LinearLayout
        implements CompoundButton.OnCheckedChangeListener,
        View.OnClickListener {
    private TextView mTextViewTitle;
    private SwitchCompat mSwitchValue;

    private String mTitle;

    public ClickableSwitchRow(Context context) {
        super(context);
        initUi();
    }

    public ClickableSwitchRow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        initUi();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray mTypedArray = getContext().obtainStyledAttributes(attrs,
                R.styleable.ClickableSwitchRow, 0, 0);
        try {
            mTitle = mTypedArray.getString(R.styleable.ClickableSwitchRow_sw_title);
        } finally {
            mTypedArray.recycle();
        }
    }

    private void initUi() {
        LayoutInflater.from(getContext()).inflate(R.layout.clickable_switch_row, this, true);
        mSwitchValue = findViewById(R.id.settings_switch);
        mTextViewTitle = findViewById(R.id.settings_button);

        mTextViewTitle.setText(mTitle);
        mSwitchValue.setOnCheckedChangeListener(this);
        mTextViewTitle.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        setChecked(isChecked);
        this.performClick();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings_button:
                setChecked(!mSwitchValue.isChecked());
                this.performClick();
        }
    }

    public void setTitle(@StringRes int res) {
        setTitle(getResources().getString(res));
    }

    public void setTitle(String title) {
        mTitle = title;
        mTextViewTitle.setText(mTitle);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setChecked(boolean isChecked) {
        mSwitchValue.setChecked(isChecked);
    }

    public boolean isChecked() {
        return mSwitchValue.isChecked();
    }
}
