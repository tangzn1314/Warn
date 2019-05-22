package com.tianque.warn.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tianque.warn.R;

/**
 * @author ctrun
 */
@SuppressWarnings("unused")
public class ErrorView extends FrameLayout {
    private View mIcon;
    private TextView mTvMessage;
    private Button mBtnRetry;

    public ErrorView(Context context) {
        this(context, null);
    }

    public ErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View v = View.inflate(getContext(), R.layout.common_error_view, this);
        mIcon = v.findViewById(R.id.icon);
        mTvMessage = v.findViewById(R.id.tv_message);
        mBtnRetry = v.findViewById(R.id.btn_retry);
    }

    private void setMessage(int msg, int iconRes) {
        mIcon.setBackgroundResource(iconRes);
        mTvMessage.setText(msg);
    }

    public void setMessage(int msg) {
        if (msg == -1) {
            setMessage(R.string.common_load_failure, R.drawable.common_icon_no_network);
        } else {
            setMessage(msg, R.drawable.common_icon_no_network);
        }
    }

    public void setOnClickRetryListener(OnClickListener listener) {
        mBtnRetry.setOnClickListener(listener);
    }

    public void showRetryButton() {
        mBtnRetry.setVisibility(View.VISIBLE);
    }

    public void hideRetryButton() {
        mBtnRetry.setVisibility(View.GONE);
    }
}
