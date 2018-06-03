package com.df.dfcalendar.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Danfeng on 2018/4/22.
 */

public class DateUtil {

    //Calendar 转化 String
    public static  String calendarToStr(Calendar calendar,String format) {

//    Calendar calendat = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return sdf.format(calendar.getTime());
    }


    //String 转化Calendar
    public static Calendar strToCalendar(String str,String format) {

//    String str = "2012-5-27";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        Calendar calendar = null;
        try {
            date = sdf.parse(str);
            calendar = Calendar.getInstance();
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }


    //    Date 转化String
    public static String dateTostr(Date date,String format) {

        SimpleDateFormat sdf = new SimpleDateFormat(format);
//    String dateStr = sdf.format(new Date());
        String dateStr = sdf.format(date);
        return dateStr;
    }


    //  String 转化Date
    public static Date strToDate(String str,String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    //Date 转化Calendar
    public static Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }


    //Calendar转化Date
    public static Date calendarToDate(Calendar calendar) {
        return calendar.getTime();
    }


    // String 转成    Timestamp

    public static Timestamp strToTimeStamp(String str) {

//    Timestamp ts = Timestamp.valueOf("2012-1-14 08:11:00");
        return Timestamp.valueOf(str);
    }


    //Date 转 TimeStamp
    public static Timestamp dateToTimeStamp(Date date,String format) {

        SimpleDateFormat df = new SimpleDateFormat(format);

        String time = df.format(new Date());

        Timestamp ts = Timestamp.valueOf(time);
        return ts;
    }
}
