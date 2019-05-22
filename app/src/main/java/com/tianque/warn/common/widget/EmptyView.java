package com.tianque.warn.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tianque.warn.R;

/**
 * @author ctrun
 */
@SuppressWarnings("unused")
public class EmptyView extends FrameLayout {
    private View mIcon;
    private TextView mTvMessage;

    public EmptyView(Context context) {
        this(context, null);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View v = View.inflate(getContext(), R.layout.common_empty_view, this);
        mIcon = v.findViewById(R.id.icon);
        mTvMessage = v.findViewById(R.id.tv_message);
    }

    private void setMessage(int msg, int iconRes) {
        mIcon.setBackgroundResource(iconRes);
        mTvMessage.setText(msg);
    }

    public void setMessage(int msg) {
        if (msg == -1) {
            setMessage(R.string.common_no_data, R.drawable.common_icon_no_data);
        } else {
            setMessage(msg, R.drawable.common_icon_no_data);
        }
    }
}
