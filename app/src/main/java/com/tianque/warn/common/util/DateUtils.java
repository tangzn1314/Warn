package com.tianque.warn.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author ctrun on 2015/6/4.
 */
@SuppressWarnings("all")
public class DateUtils {

    public static final SimpleDateFormat DATE_YMDHM = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static final SimpleDateFormat DATE_YMDHM_CHINESE = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    public static final SimpleDateFormat DATE_MDHM = new SimpleDateFormat("MM-dd HH:mm");
    public static final SimpleDateFormat DATE_HM = new SimpleDateFormat("HH:mm");

    private static SimpleDateFormat sDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static String dateFromTime(long time) {
        return sDateFormat.format(time);
    }

    public static String timeFromTime(String dateTime) {
        try {
            long time = longFromDateTime(dateTime);
            return timeFromTime(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String timeFromTime(long time) {
        return DATE_HM.format(time);
    }

    public static long longFromDateTime(String datetime) throws ParseException {
        return sDateTimeFormat.parse(datetime).getTime();
    }

    public static String minuteToHour(long minute) {
        if (minute < 60) {
            return minute + "分钟";
        }

        return (minute % 60 == 0 ? minute/60 : String.format("%.1f", minute/60f)) + "小时";
    }


    /**
     * 获取本周第一天的日期
     */
    public static String beginDateOfThisWeek() {
        Calendar weekBegin = Calendar.getInstance();
        weekBegin.setFirstDayOfWeek(Calendar.MONDAY);

        weekBegin.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        return dateFromTime(weekBegin.getTimeInMillis());
    }

    /**
     * 获取本周最后一天的日期
     */
    public static String endDateOfThisWeek() {
        Calendar weekEnd = Calendar.getInstance();
        weekEnd.setFirstDayOfWeek(Calendar.MONDAY);

        weekEnd.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        weekEnd.add(Calendar.DAY_OF_WEEK, 6);

        return dateFromTime(weekEnd.getTimeInMillis());
    }

    /**
     * 获取本月第一天的日期
     */
    public static String beginDateForThisMonth() {
        Calendar monthBegin = Calendar.getInstance();
        monthBegin.set(Calendar.DAY_OF_MONTH, 1);

        return dateFromTime(monthBegin.getTimeInMillis());
    }

    /**
     * 获取本月最后一天的日期
     */
    public static String endDateForThisMonth() {
        Calendar monthEnd = Calendar.getInstance();
        monthEnd.add(Calendar.MONTH, 1);
        monthEnd.set(Calendar.DAY_OF_MONTH, 1);
        monthEnd.add(Calendar.DATE, -1);

        return dateFromTime(monthEnd.getTimeInMillis());
    }

    /**
     * 判断时间是否为本月
     */
    public static boolean isThisMonth(String startDate, String endDate) {
        try {
            Calendar monthBegin = Calendar.getInstance();
            monthBegin.set(Calendar.DAY_OF_MONTH, 1);

            Calendar monthEnd = Calendar.getInstance();
            monthEnd.add(Calendar.MONTH, 1);
            monthEnd.set(Calendar.DAY_OF_MONTH, 1);
            monthEnd.add(Calendar.DATE, -1);

            Calendar dateBegin = Calendar.getInstance();
            dateBegin.setTimeInMillis(sDateFormat.parse(startDate).getTime());

            Calendar dateEnd = Calendar.getInstance();
            dateEnd.setTimeInMillis(sDateFormat.parse(endDate).getTime());

            return monthBegin.get(Calendar.YEAR)==dateBegin.get(Calendar.YEAR) &&
                    monthBegin.get(Calendar.DAY_OF_YEAR)==dateBegin.get(Calendar.DAY_OF_YEAR) &&
                    monthEnd.get(Calendar.YEAR)==dateEnd.get(Calendar.YEAR) &&
                    monthEnd.get(Calendar.DAY_OF_YEAR) == dateEnd.get(Calendar.DAY_OF_YEAR);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 判断时间是否为本周
     */
    public static boolean isThisWeek(String startDate, String endDate) {
        try {
            Calendar weekBegin = Calendar.getInstance();
            weekBegin.setFirstDayOfWeek(Calendar.MONDAY);
            weekBegin.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

            Calendar weekEnd = Calendar.getInstance();
            weekEnd.setFirstDayOfWeek(Calendar.MONDAY);
            weekEnd.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            weekEnd.add(Calendar.DAY_OF_WEEK, 6);

            Calendar dateBegin = Calendar.getInstance();
            dateBegin.setTimeInMillis(sDateFormat.parse(startDate).getTime());

            Calendar dateEnd = Calendar.getInstance();
            dateEnd.setTimeInMillis(sDateFormat.parse(endDate).getTime());

            return weekBegin.get(Calendar.YEAR) == dateBegin.get(Calendar.YEAR) &&
                    weekBegin.get(Calendar.DAY_OF_YEAR) == dateBegin.get(Calendar.DAY_OF_YEAR) &&
                    weekEnd.get(Calendar.YEAR) == dateEnd.get(Calendar.YEAR) &&
                    weekEnd.get(Calendar.DAY_OF_YEAR) == dateEnd.get(Calendar.DAY_OF_YEAR);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 判断时间是否为当日
     */
    public static boolean isToday(String startDate, String endDate) {
        Calendar today = Calendar.getInstance();
        return sDateFormat.format(today.getTimeInMillis()).equals(startDate) &&
                sDateFormat.format(today.getTimeInMillis()).equals(endDate);
    }

    /**
     * 判断当前时间是否是今年
     * @param time 当前时间
     */
    public static boolean isThisYear(long time) {
        Calendar calendar = Calendar.getInstance();
        int thisYear = calendar.get(Calendar.YEAR);
        calendar.setTimeInMillis(time);
        int currentYear = calendar.get(Calendar.YEAR);
        return thisYear==currentYear;
    }

}
