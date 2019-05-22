package com.tianque.warn.common;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.tianque.warn.R;
import com.tianque.warn.common.util.DateUtils;

import java.util.Calendar;

/**
 * @author ctrun on 2017/4/7.
 */
public class DatePickerDialog extends AlertDialog implements DialogInterface.OnClickListener, DatePicker.OnDateChangedListener {
    public static final String TITLE = "title";
    public static final String CLEAR = "clear";

    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String DAY = "day";

    private final DatePicker mDatePicker;

    private OnDateSetListener mDateSetListener;

    public DatePickerDialog(Context context, OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth, boolean clear) {
        this(context, 0, listener, null, year, monthOfYear, dayOfMonth, clear);
    }

    public DatePickerDialog(Context context, OnDateSetListener listener, String title, int year, int monthOfYear, int dayOfMonth, boolean clear) {
        this(context, 0, listener, title, year, monthOfYear, dayOfMonth, clear);
    }


    public DatePickerDialog(Context context, int theme, OnDateSetListener listener, String title, int year, int monthOfYear,
                            int dayOfMonth, boolean clear) {
        super(context, theme);

        mDateSetListener = listener;

        if(clear) {
            setButton(BUTTON_NEUTRAL, "清除", this);
        }
        setButton(BUTTON_POSITIVE, "确定", this);
        setButton(BUTTON_NEGATIVE, "取消", this);
        setIcon(0);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.common_dialog_date_picker, null);
        setView(view);
        mDatePicker = view.findViewById(R.id.datePicker);
        mDatePicker.init(year, monthOfYear, dayOfMonth, this);

        LinearLayout layoutParent = (LinearLayout) mDatePicker.getChildAt(0);
        LinearLayout layout = (LinearLayout) layoutParent.getChildAt(0);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View v = layout.getChildAt(i);
            if (v instanceof NumberPicker) {
                setNumberPicker((NumberPicker) v);
            }
        }

        setTitle(title);
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        mDatePicker.init(year, month, day, this);
    }

    public void setOnDateSetListener(OnDateSetListener listener) {
        mDateSetListener = listener;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                if (mDateSetListener != null) {
                    // Clearing focus forces the dialog to commit any pending
                    // changes, e.g. typed text in a NumberPicker.
                    mDatePicker.clearFocus();

                    final Calendar c = Calendar.getInstance();
                    c.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
                    mDateSetListener.onDateSet(DateUtils.dateFromTime(c.getTimeInMillis()), false);
                }
                break;
            case BUTTON_NEGATIVE:
                //Log.d("Date", "BUTTON_NEGATIVE");
                break;
            case BUTTON_NEUTRAL:
                //Log.d("Date", "BUTTON_NEUTRAL");
                if (mDateSetListener != null) {
                    mDateSetListener.onDateSet("", true);
                }
                break;
            default:
                break;
        }
    }

    public DatePicker getDatePicker() {
        return mDatePicker;
    }

    public void updateDate(int year, int month, int dayOfMonth) {
        mDatePicker.updateDate(year, month, dayOfMonth);
    }

    @Override
    public Bundle onSaveInstanceState() {
        final Bundle state = super.onSaveInstanceState();
        state.putInt(YEAR, mDatePicker.getYear());
        state.putInt(MONTH, mDatePicker.getMonth());
        state.putInt(DAY, mDatePicker.getDayOfMonth());
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int year = savedInstanceState.getInt(YEAR);
        final int month = savedInstanceState.getInt(MONTH);
        final int day = savedInstanceState.getInt(DAY);
        mDatePicker.init(year, month, day, this);
    }

    public interface OnDateSetListener {
        void onDateSet(String date, boolean clear);
    }

    //这个方法是通过反射拿到mSelectionDivider属性，然后给他设置上颜色值。（此方法在NumberPicker 的 setDisplayedValues后调用）
    public void setNumberPicker(NumberPicker spindle) {
        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    pf.set(spindle, ContextCompat.getDrawable(getContext(), R.drawable.common_line_blue));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

}
