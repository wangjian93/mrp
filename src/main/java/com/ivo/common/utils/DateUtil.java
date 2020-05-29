package com.ivo.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期工具类
 * @author wj
 * @version 1.0
 */
public class DateUtil {

    /**
     * 默认的星期几格式
     */
    public static final String[] DEFAULT_WEEK_DAY_FORMAT = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};

    /**
     * 获取某月的每一天日期
     * @return
     */
    public static List<Date> getMonthEveryDays(Date date){
        List<Date> dateList = new ArrayList<>();
        int num = getDaysByYearMonth(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        for (int i = 1; i <= num; i++) {
            Calendar a = Calendar.getInstance();
            a.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), i);
            dateList.add(a.getTime());
        }
        return dateList;
    }

    /**
     * 获取月份天数
     */
    public static int getDaysByYearMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取指定年月的第一天
     * @param year
     * @param month
     * @return
     */
    public static Date getFirstDayOfMonth1(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最小天数
        int firstDay = cal.getMinimum(Calendar.DATE);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH,firstDay);
        return cal.getTime();
    }

    /**
     * 获取指定年月的最后一天
     * @param year
     * @param month
     * @return
     */
    public static Date getLastDayOfMonth1(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DATE);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        return cal.getTime();
    }




    /**
     * 根据日期获取星期几，并指定星期几的格式
     * @param date 日期
     * @param weekDayFormat 星期几格式
     * @return 星期几
     */
    public static String getWeekDay(Date date, String[] weekDayFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return weekDayFormat[calendar.get(Calendar.DAY_OF_WEEK)-1];
    }

    /**
     * 根据日期获取星期几
     * @param date 日期
     * @return 星期几
     */
    public static String getWeekDay(Date date) {
        return getWeekDay(date, DEFAULT_WEEK_DAY_FORMAT);
    }

    /**
     * 获取日期是星期几
     * @param dateList 日期集合
     * @return String[]
     */
    public static String[] getWeekDay(List<Date> dateList) {
        String[] weeks = new String[dateList.size()];
        for(int i=0; i<dateList.size(); i++) {
            weeks[i] = getWeekDay(dateList.get(i));
        }
        return weeks;
    }

    public static String[] getWeekDay_(List<Date> dateList) {
        String[] weeks = new String[dateList.size()];
        for(int i=0; i<dateList.size(); i++) {
            weeks[i] = getWeekDay(dateList.get(i));
        }
        return weeks;
    }

    /**
     * 获取两个日期之间的日历
     * @param fromDate 开始日期
     * @param toDate 结束日期
     * @return List<Date>
     */
    public static List<Date> getCalendar(Date fromDate, Date toDate) {
        List<Date> days = new ArrayList<>();
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(fromDate);
        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(toDate);
        tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
        while (tempStart.before(tempEnd)) {
            days.add(tempStart.getTime());
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        return days;
    }

    /**
     * 日期格式化字符串
     * @return String
     */
    public static String format(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 日期格式化字符串
     * @return List<String>
     */
    public static List<String> format(List<Date> dateList) {
        List<String> list = new ArrayList<>();
        for(Date date : dateList) {
            list.add(format(date));
        }
        return list;
    }

    public static List<String> format_(List<Date> dateList) {
        List<String> list = new ArrayList<>();
        for(Date date : dateList) {
            list.add(format(date));
        }
        return list;
    }

    /**
     * 字符串转换日期
     * @param dateStr 日期
     * @return Date
     */
    public static Date parse(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 开始时间结束时间取月份集合
     * @param minDate 开始日期
     * @param maxDate 结束日期
     * @return List<String>
     */
    public static List<String> getMonthBetween(Date minDate, Date maxDate) {
        ArrayList<String> result = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(minDate);
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

        max.setTime(maxDate);
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

        Calendar curr = min;
        while (curr.before(max)) {
            result.add(sdf.format(curr.getTime()));
            curr.add(Calendar.MONTH, 1);
        }

        return result;
    }
}
