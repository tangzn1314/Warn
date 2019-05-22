package com.tianque.warn.common;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author ctrun on 15/06/29.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public static final String PARAM_MAX_TODAY = "PARAM_MAX_TODAY";
    public static final String PARAM_MIN_TOMORROW = "PARAM_MIN_TOMORROW";
    public static final String PARAM_MAX_DATE = "PARAM_MAX_DATE";
    public static final String PARAM_MIN_DATE = "PARAM_MIN_DATE";
    public static final String PARAM_DATE = "date";
    //    SetTimeType mTimeType = SetTimeType.Cannel;
// 小米手机不管按那个按钮都会调用 onDataSet，只好在click事件里面做标记
//    enum SetTimeType {
//        Cannel, Set, Clear;
//    };
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String dateString = getArguments().getString(PARAM_DATE);
        if (dateString.isEmpty()) {
            dateString = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTimeInMillis());
        }

        String title = getArguments().getString(DatePickerDialog.TITLE);
        boolean clear = getArguments().getBoolean(DatePickerDialog.CLEAR, false);
        boolean minTomorrow = getArguments().getBoolean(DatePickerFragment.PARAM_MIN_TOMORROW, false);
        boolean maxToday = getArguments().getBoolean(DatePickerFragment.PARAM_MAX_TODAY, false);

        String[] date = dateString.split("-");
        int year = Integer.valueOf(date[0]);
        int month = Integer.valueOf(date[1]) - 1;
        int day = Integer.valueOf(date[2]);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, title, year, month, day, clear);

        if(minTomorrow) {
            Calendar cal = Calendar.getInstance();
            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
            datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
        }

        if(maxToday) {
            datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        }

        String minDateString = getArguments().getString(PARAM_MIN_DATE);
        if (!TextUtils.isEmpty(minDateString)) {
            String[] minDate = minDateString.split("-");
            int minYear = Integer.valueOf(minDate[0]);
            int minMonth = Integer.valueOf(minDate[1]) - 1;
            int minDay = Integer.valueOf(minDate[2]);

            Calendar cal = Calendar.getInstance();
            cal.set(minYear, minMonth, minDay, 0, 0, 0);
            datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
        }

        String maxDateString = getArguments().getString(PARAM_MAX_DATE);
        if (!TextUtils.isEmpty(maxDateString)) {
            String[] maxDate = maxDateString.split("-");
            int maxYear = Integer.valueOf(maxDate[0]);
            int maxMonth = Integer.valueOf(maxDate[1]) - 1;
            int maxDay = Integer.valueOf(maxDate[2]);

            Calendar cal = Calendar.getInstance();
            cal.set(maxYear, maxMonth, maxDay, 0, 0, 0);
            datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
        }

        return datePickerDialog;
    }

    public void setOnDateSetListener(DatePickerDialog.OnDateSetListener listener) {
        mDateSetListener = listener;
    }

    @Override
    public void onDateSet(String date, boolean clear) {
        if(mDateSetListener != null) {
            mDateSetListener.onDateSet(date, clear);
        }
    }
}
