package com.tianque.warn.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.tianque.warn.R;
import com.tianque.warn.common.SimpleTextWatcher;

/**
 * @author ctrun
 * 带删除功能的EditText控件
 */
@SuppressWarnings("all")
public class DeletableEditText extends AppCompatEditText {
    private static final String TAG = "DeletableEdit";

    public DeletableEditText(Context context) {
        super(context);
        init(context, null);
    }

    public DeletableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DeletableEditText(Context context, AttributeSet attrs, int defStyle) {
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
        drawable = ContextCompat.getDrawable(context, crossedRes);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                displayDelete(s.length() > 0);
            }
        });

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
        Drawable leftDrawable = getCompoundDrawables()[0];
        setCompoundDrawables(leftDrawable, null, drawable, null);
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

}
