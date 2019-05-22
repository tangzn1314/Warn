package com.tianque.warn.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tianque.warn.AccountManager;
import com.tianque.warn.R;
import com.tianque.warn.common.SimpleTextWatcher;

/**
 * @author ctrun
 * 登录账号自动补全控件
 */
@SuppressWarnings("all")
public class LoginAutoCompleteEdit extends AppCompatAutoCompleteTextView {
    private static final String TAG = "LoginAutoCompleteEdit";

    private boolean mDisableAuto = false;

    public LoginAutoCompleteEdit(Context context) {
        super(context);
        init(context, null);
    }

    public LoginAutoCompleteEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LoginAutoCompleteEdit(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attrs) {
        boolean useDark = false;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Crossed, 0, 0);
        try {
            useDark = a.getBoolean(R.styleable.Crossed_darkness, false);
        } finally {
            a.recycle();
        }

        // 添加删除箭头
        int crossedRes = useDark ? R.drawable.common_icon_delete_edit : R.drawable.common_icon_delete_edit;
        drawable = getResources().getDrawable(crossedRes);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                displayDelete(s.length() > 0);
            }
        });

        String[] relogins = AccountManager.loadAllRelogininfo(context);

        this.setAdapter(new AutoCompleteAdapter(context, R.layout.login_auto_complete_item, relogins));

        this.setThreshold(1);
    }

    private Drawable drawable;

    private void displayDelete(boolean show) {
        if (show) {
            setDrawableRight(drawable);
        } else {
            setDrawableRight(null);
        }
    }

    private void setDrawableRight(Drawable drawable) {
        setCompoundDrawables(null, null, drawable, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));

                if (touchable) {
                    this.setText("");
                }
            }
        }

        return super.onTouchEvent(event);
    }

    /*@Override
    protected void replaceText(CharSequence text) {
        String t = this.getText().toString();
        int index = t.indexOf("@");
        if (index != -1) {
            super.replaceText(t.substring(0, index) + text);
        } else {
            super.replaceText(text);
        }
    }*/

    public void setDisableAuto(boolean mDisableAuto) {
        this.mDisableAuto = mDisableAuto;
    }

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        //adapter中数据的前半部分，那么adapter中的这条数据将会在下拉框中出现
//        Log.i(TAG + " performFiltering", text.toString() + "   " + keyCode);
        if (mDisableAuto) {
            return;
        }
        super.performFiltering(text, keyCode);
    }

    private class AutoCompleteAdapter extends ArrayAdapter<String> {

        public AutoCompleteAdapter(Context context, int textViewResourceId, String[] email_s) {
            super(context, textViewResourceId, email_s);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            Log.i(TAG, "in GetView");
            View v = convertView;
            if (v == null) {
                v = LayoutInflater.from(getContext()).inflate(R.layout.login_auto_complete_item, null);
            }
            TextView tv = v.findViewById(R.id.tv);

            tv.setText(getItem(position));
            return v;
        }
    }

}
