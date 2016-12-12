package com.luckyeve.elastic.common.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lixy on 2016/11/20.
 */
public class DateUtil {

    public static final String SYS_PAGE_ENCODE = "UTF-8";
    public static final String SYS_DATE_FORMAT = "yyyy-MM-dd";
    /**
     * 格式如下： yyyy-MM-dd HH:mm:ss
     */
    public final static int DATE_FORMAT_BY_TIME = 1;

    /**
     * 格式如下： yyyy-MM-dd
     */
    public final static int DATE_FORMAT_BY_DATE = 2;

    /**
     * 格式如下： yyyy/MM/dd HH:mm:ss
     */
    public final static int DATE_FORMAT_BY_OTHER_TIME = 3;
    /**
     * 格式如下： yyyy/MM/dd
     */
    public final static int DATE_FORMAT_BY_OTHER_DATE = 4;

    /**
     * 格式如下： yyyyMMddHHmmss
     */
    public final static int DATE_FORMAT_BY_SIAMPLE_TIME = 5;
    /**
     * 格式如下： yyyyMMdd
     */
    public final static int DATE_FORMAT_BY_SIAMPLE_DATE = 6;

    public static int nh = 60 * 60;// 一小时的秒数
    public static int nm = 60;// 一分钟的秒数

    public final static SimpleDateFormat TimeFormat = new SimpleDateFormat(
            FormatConfig.TIME);
    public final static SimpleDateFormat TimestampFormat = new SimpleDateFormat(FormatConfig.TIMESTAMP);
    public final static SimpleDateFormat OtherTimeFormat = new SimpleDateFormat(
            FormatConfig.OTHER_TIME);
    public final static SimpleDateFormat OtherDateFormat = new SimpleDateFormat(
            FormatConfig.OTHER_DATE);
    public final static SimpleDateFormat DateFormat = new SimpleDateFormat(
            FormatConfig.DATE);

    public final static SimpleDateFormat CNDateFormat = new SimpleDateFormat(
            FormatConfig.CN_DATE);

    public final static SimpleDateFormat SampleTimeFormat = new SimpleDateFormat(
            FormatConfig.SAMPLE_TIME);
    public final static SimpleDateFormat SampleDateFormat = new SimpleDateFormat(
            FormatConfig.SAMPLE_DATE);
    public final static SimpleDateFormat SampleDate1Format = new SimpleDateFormat(
            FormatConfig.SAMPLE1_DATE);

    public static SimpleDateFormat UtcTimeFormat = new SimpleDateFormat(
            FormatConfig.TIME_UTC);

    public final static SimpleDateFormat ShortSampleDateFormat = new SimpleDateFormat(
            FormatConfig.SHORTSAMPLE_DATE);
    public final static SimpleDateFormat OnlyTimeFormat = new SimpleDateFormat(
            FormatConfig.ONLY_TIME);

    public final static SimpleDateFormat ShortDateTimeFormat = new SimpleDateFormat(
            FormatConfig.SHORTDATETIME);

    public final static SimpleDateFormat OnlyShortTimeFormat = new SimpleDateFormat(
            FormatConfig.ONLY_SHORTTIME);

    public final static SimpleDateFormat SampleDateYear = new SimpleDateFormat(FormatConfig.SAMPLE_DATE_YEAR);

    public final static SimpleDateFormat MonthDayFormat = new SimpleDateFormat(
            FormatConfig.SAMPLE_MONTHDAY);

    public final static SimpleDateFormat TimeUtcH = new SimpleDateFormat(FormatConfig.TIME_UTC_H);

    private final static SimpleDateFormat rssDate = new SimpleDateFormat(
            "EEE, d MMM yyyy HH:mm:ss z", Locale.US);
    private final static SimpleTimeZone aZone = new SimpleTimeZone(8, "GMT");

    public final static SimpleDateFormat Cn_Format = new SimpleDateFormat(
            FormatConfig.CN_TIME1);

    public final static SimpleDateFormat RUNKEEP_Format = new SimpleDateFormat(
            FormatConfig.RUNKEEP_TIME, Locale.US);

    public static SimpleDateFormat UtcTimeFormat1 = new SimpleDateFormat(
            FormatConfig.TIME_UTC1);
    public static SimpleDateFormat UtcTimeFormatZ = new SimpleDateFormat(
            FormatConfig.TIME_UTCZ);

    public static SimpleDateFormat UtcTimeFormatForNike = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss");

    public static SimpleDateFormat EndomondoFormat = new SimpleDateFormat(
            FormatConfig.ENDOMONDO_TIME, Locale.ENGLISH);

    public static SimpleDateFormat simple_month = new SimpleDateFormat(FormatConfig.SIMPLE_MONTH);

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
    static {
        rssDate.setTimeZone(aZone);
    }

    // 2、取得时间偏移量：
    public static final int zoneOffset = Calendar.getInstance().get(
            Calendar.ZONE_OFFSET);
    // 3、取得夏令时差：
    public static final int dstOffset = Calendar.getInstance().get(
            Calendar.DST_OFFSET);

    /**
     * 将日期对象转换为RSS用的字符串日期
     *
     * @param date
     * @return
     */
    public static String dateToRssDate(Date date) {
        return rssDate.format(date);
    }

    public static String getMonth(Date date) {
        return simple_month.format(date);
    }

    public static Integer getYearMonth(Date date) {
        if (date == null) {
            return null;
        }
        String yearMonth = format(date, "yyyyMM");
        return Integer.parseInt(yearMonth);
    }

    public static Integer getCurrentYear(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, 0);
        return Integer.valueOf(formatYM(cal.getTime()));
    }

    public static Long getNextYear(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, 0);
        cal.add(Calendar.YEAR, 1);
        return Long.valueOf(formatYM(cal.getTime()));
    }

    public static Date getDate(Integer year, Integer month) {
        if (year == null || month == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DATE, 1);
        if (month != null) {
            cal.set(Calendar.MONTH, month - 1);
        }
        return cal.getTime();
    }

    public static Date getNextMonth(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        return calendar.getTime();
    }

    public static Integer nexYearMonth(Integer yearMonth) {
        if (yearMonth == null) return null;
        int year = yearMonth /100;
        int month = yearMonth % 100;
        if (++month > 12) {
            year ++;
            month = 1;
        }
        return year * 100 + month;
    }


    /**
     * 将RSS日期字符串转换为日期对象
     *
     * @param rssDate
     * @return
     */
    public static Date rssDateToDate(String rssDate) {
        Date d = null;
        Date t;
        try {
            t = DateUtil.rssDate.parse(rssDate);
            d = new Date(t.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return d;
    }

    /**
     * 将时间对象转换为yyyyMMddHHmmss格式用的字符串时间
     *
     * @param date
     * @return
     */
    public static String sampleTimeFormat(Date date) {
        return SampleTimeFormat.format(date);
    }

    /**
     * MM-DD HH:mm
     * @param date
     * @return
     */
    public static String SampleDateUtcHFormat(Date date){
        return TimeUtcH.format(date);
    }

    /**
     * 将时间对象转换为yyyyMMddHH格式用的字符串时间
     *
     * @param date
     * @return
     */
    public static String sampleDateFormatFormat(Date date) {
        return SampleDateFormat.format(date);
    }

    public static String SampleDate1Format(Date date) {
        return SampleDate1Format.format(date);
    }

    public static String SampleDateYearFormat(Date date){return SampleDateYear.format(date);}
    /**
     * 将时间对象转换为yyMMddHHhhmmss格式用的字符串时间
     *
     * @param date
     * @return
     */
    public static String shortSampleDateFormatFormat(Date date) {
        return ShortSampleDateFormat.format(date);
    }

    /**
     * 将时间对象转换为yyyy/MM/dd HH:mm:ss格式用的字符串时间
     *
     * @param date
     * @return
     */
    public static String otherTimeFormat(Date date) {
        return OtherTimeFormat.format(date);
    }

    /**
     * 将时间对象转换为yyyy-mm-dd HH:mm:ss格式用的字符串时间
     *
     * @param date
     * @return
     */
    public static String timeFormat(Date date) {
        return TimeFormat.format(date);
    }

    /**
     * 将日期对象转换为yyyy-mm-dd 格式用的字符串日期
     *
     * @param date
     * @return
     */
    public static String dateFormat(Date date) {
        return DateFormat.format(date);
    }

    public static String cnDateFormat(Date date) {
        return CNDateFormat.format(date);
    }

    public static String monthDayFormat(Date date) {
        return MonthDayFormat.format(date);
    }

    /**
     * 将日期对象转换为yyyy/mm/dd 格式用的字符串日期
     *
     * @param date
     * @return
     */
    public static String otherDateFormat(Date date) {
        return OtherDateFormat.format(date);
    }

    public static String getUtcTimeFormat(Date date) {

        long time = (date.getTime() - (zoneOffset + dstOffset));

        return UtcTimeFormat.format(new Date(time)) + "Z";
    }

    public static Date getUtcTimeFormat(String sb) {
        Date utc;
        try {
            if (sb != null) {
                sb = sb.trim();
                // System.out.println("getUtcTimeFormat" + sb +
                // "  "+sb.length());
                // System.out.println(sb.indexOf(":"));

                if (sb.length() - sb.trim().indexOf(":") < 5) {
                    sb = sb + ":00";
                } else if (sb.length() - sb.lastIndexOf(":") > 2) {
                    sb = sb.substring(0, sb.lastIndexOf(":") + 3);
                }
            }

            utc = UtcTimeFormat.parse(sb + ".000");
            return new Date(utc.getTime() + (zoneOffset + dstOffset));
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }

    }

    /**
     * 将格式为yyyy-mm-dd HH:mm:ss字符串时间转换成时间对象
     *
     * @param date
     * @return
     */
    public static Date parseTime(String date) {
        Date d = null;
        try {
            if (date == null || date.length() == 0) {
                return d;
            }
            d = TimeFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    public static Date parseRunKeeperTime(String date) {
        Date d = null;
        try {
            d = RUNKEEP_Format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    public static Date parserUtcTimeFormat1(String sb) {
        Date utc = new Date();
        try {
            if (sb != null && sb.indexOf("Z") > 0) {
                utc = UtcTimeFormatZ.parse(sb + ".000");
            } else if (sb != null && sb.indexOf("T") > 0) {
                utc = UtcTimeFormat1.parse(sb + ".000");
            }

            return new Date(utc.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date parserUtcTimeFormatForNike(String sb) {
        Date utc = new Date();
        try {
            if (sb != null && sb.indexOf("Z") > 0) {
                utc = UtcTimeFormatZ.parse(sb + ".000");
            } else if (sb != null && sb.indexOf("T") > 0) {
                utc = UtcTimeFormatForNike.parse(sb);
            }

            return new Date(utc.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date parserUtcTimeFormat(String sb) {
        if (sb != null && sb.indexOf("Z") > 0) {
            sb = sb.replace("Z", " ");
        }
        // else if (sb != null && sb.indexOf("T") > 0) {
        // sb=sb.replace("T", " ");
        // }
        Date utc;
        try {
            utc = UtcTimeFormat.parse(sb + ".000");
            return new Date(utc.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将格式为yyyyMMddHHmmss字符串时间转换成时间对象
     *
     * @param date
     * @return
     */
    public static Date parseSampleTime(String date) {
        Date d = null;
        try {
            d = SampleTimeFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * 将格式为yyyy/mm/dd HH:mm:ss字符串时间转换成时间对象
     *
     * @param date
     * @return
     */
    public static Date parseOtherTime(String date) {
        Date d = null;
        try {
            d = OtherTimeFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * 将格式为yyyy-mm-dd字符串日期转换成日期对象
     *
     * @param date
     * @return
     */
    public static Date parseDate(String date) {
        Date d = null;
        try {
            d = DateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    public static Date parserEndomondo(String data) {
        Date d = null;
        try {
            d = EndomondoFormat.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * YYYYMMDD
     *
     * @param date
     * @return
     */
    public static Date parseSampleDateFormat(String date) {
        Date d = null;
        try {
            d = SampleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * 将格式为yyyy/mm/dd字符串日期转换成日期对象
     *
     * @param date
     * @return
     */
    public static Date parseOtherDate(String date) {
        Date d = null;
        try {
            d = OtherDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * 获取当前系统时间yyyy/mm/dd HH:mm:ss格式表示的字符串
     *
     * @return
     */
    public static String getOtherNowTime() {
        return otherTimeFormat(new Date());
    }

    /**
     * 返回 HH:mm:ss格式表示的字符串
     *
     * @param date
     * @return
     */
    public static String getOnlyTimeFormat(Date date) {
        return OnlyTimeFormat.format(date);
    }

    /**
     * 获取当前系统时间yyyy-mm-dd HH:mm:ss格式表示的字符串
     *
     * @return
     */
    public static String getNowTime() {
        return timeFormat(new Date());
    }

    /**
     * 获取当前系统时间yyyy-mm-dd HH:mm:ss格式表示的字符串
     *
     * @return
     */
    public static String getTimeFormat(Date date) {
        if (date == null) return null;
        return timeFormat(date);
    }

    /**
     * 获取当前系统时期yyyy-mm-dd格式表示的字符串
     *
     * @return
     */
    public static String getNowDate() {
        return dateFormat(new Date());
    }

    /**
     * 获取当前系统时期yyyy-mm-dd格式表示的字符串
     *
     * @return
     */
    public static Date getDate() {
        return parseDate(getNowDate());
    }

    /**
     *
     * @return
     */
    public static SimpleDateFormat getTimeFormat() {
        return TimeFormat;
    }

    public static String getCnFormat(Date date) {
        return Cn_Format.format(date);
    }

    /**
     *
     * @return
     */
    public static SimpleDateFormat getDateFormat() {
        return DateFormat;
    }

    public static String sysdate(String format) {
        return format(new Date(), format);
    }

    public static String sysdate() {
        return sysdate(SYS_DATE_FORMAT);
    }

    public static String format(Date date, String format) {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(date);
    }

    public static String format(Date date) {
        return format(date, SYS_DATE_FORMAT);
    }

    public static String formatYM(Date date) {
        return format(date, "yyyyMM");
    }

    /**
     * 把时间置换到当前月的对应时间
     * @param date
     * @return
     */
    public static Date getCurrentMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar calendar1 = Calendar.getInstance();
        //考虑到转换月试 超范围
        if(calendar1.getActualMaximum(Calendar.DAY_OF_MONTH)<calendar.get(Calendar.DAY_OF_MONTH)){
            calendar.set(Calendar.DAY_OF_MONTH,calendar1.getActualMaximum(Calendar.DAY_OF_MONTH));
        }
        calendar.set(Calendar.YEAR, calendar1.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, calendar1.get(Calendar.MONTH));
        return calendar.getTime();
    }

    /**
     * 日期转字符串  格式：yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static String formatSimple(Date date) {
        return format(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date parse(String date, String format) {
        try {
            SimpleDateFormat sf = new SimpleDateFormat(format);
            return sf.parse(date);
        } catch (ParseException e) {
            throw  new RuntimeException(e);
        }
    }

    public static Date parse(String date) {
        return parse(date, SYS_DATE_FORMAT);
    }

    public static String getFrontDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date newDate = dateFormat.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(newDate);
            calendar.add(Calendar.DATE, -1);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DATE);
            String returnValue = year + "-" + month + "-" + day;
            return returnValue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Date frontDayEnd(final Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -1);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    /**
     * 获取本周第一天的日期 时分秒都为0
     *
     * @return
     */
    public static Date getLastDayOfBackwardWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.DAY_OF_WEEK, 2);
        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    public static Date getFirstDayOfNextWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.DAY_OF_WEEK, 2);
        cal.add(Calendar.DATE, 7);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 日期加天数
     *
     * @param date
     * @param days
     * @return
     */
    public static Date addDay(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, days);
        return c.getTime();
    }

    /**
     * 日期加年数
     *
     * @param date
     * @param years
     * @return
     */
    public static Date addYear(Date date, int years) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, years);
        return c.getTime();
    }

    /**
     * 日期加小时数
     *
     * @param date
     * @param hour
     * @return
     */
    public static Date addHour(Date date, int hour) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR_OF_DAY, hour);
        return c.getTime();
    }

    /**
     * 日期加分钟数
     *
     * @param date
     * @param minute
     * @return
     */
    public static Date addMinute(Date date, int minute) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, minute);
        return c.getTime();
    }

    /**
     * 日期加分钟数
     *
     * @param date
     * @return
     */
    public static int getDayHour(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 日期加分钟数
     *
     * @return
     */
    public static long getNowDateTime() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 1);
        return c.getTimeInMillis();
    }

    public static int[] getTimeDiff(long time) {

        int hour = (int) (time / nh);
        int diff = (int) (time % nh);
        int min = diff / nm;
        int sec = diff % nm;
        return new int[] { hour, min, sec };
    }

    public static int getPaceTime(String s) {
        String[] datas = s.split(":");
        int hour = Integer.parseInt(datas[0]);
        int minute = Integer.parseInt(datas[1]);
        int second = Integer.parseInt(datas[2]);
        int time = second + minute * 60 + hour * 3600;
        return time;
    }

    public static String shortDateTimeFormat(Date date) {
        return ShortDateTimeFormat.format(date);
    }

    public static String onlyShortTimeFormat(Date date) {
        return OnlyShortTimeFormat.format(date);
    }

    public static boolean judgeTodayOrYesterday(Date paramDate) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = format.format(date);
        // 得到今天零时零分零秒这一时刻
        Date today;
        try {
            today = format.parse(todayStr);
        } catch (ParseException e) {
            return false;
        }
        // 比较
        if (today.getTime() - paramDate.getTime() <= 0) {
            return true;
        } else if ((today.getTime() - paramDate.getTime()) > 0
                && (today.getTime() - paramDate.getTime()) < 86400000) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 获取本周第一天的日期 时分秒都为0
     *
     * @return
     */
    public static Date getFirstDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.DAY_OF_WEEK, 2);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取本月第一天的日期 时分秒都为0
     *
     * @return
     */
    public static Date getFirstDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 得到当前时间，没有特殊字符，使用yyyyMMddHHmmssSSS格式
     *
     * @return
     */
    public static String getNowTime(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return df.format(date);
    }

    /**
     * 当前时间，yyyyMMddHHmmss格式，避免使用字符运算带来的开销
     * @return
     */
    public static long getDateTime(){
        Calendar calendar = Calendar.getInstance();
        long dateTime = calendar.get(Calendar.YEAR);
        dateTime = dateTime*100 + calendar.get(Calendar.MONTH) + 1;
        dateTime = dateTime*100 + calendar.get(Calendar.DAY_OF_MONTH);
        dateTime = dateTime*100 + calendar.get(Calendar.HOUR_OF_DAY);
        dateTime = dateTime*100 + calendar.get(Calendar.MINUTE);
        dateTime = dateTime*100 + calendar.get(Calendar.SECOND);
        return dateTime;
    }

    /**
     * 当前时间+days ，yyyyMMddHHmmss格式，避免使用字符运算带来的开销
     * @return
     */
    public static long getDateTime(int days){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, days);
        long dateTime = calendar.get(Calendar.YEAR);
        dateTime = dateTime*100 + calendar.get(Calendar.MONTH) + 1;
        dateTime = dateTime*100 + calendar.get(Calendar.DAY_OF_MONTH);
        dateTime = dateTime*100 + calendar.get(Calendar.HOUR_OF_DAY);
        dateTime = dateTime*100 + calendar.get(Calendar.MINUTE);
        dateTime = dateTime*100 + calendar.get(Calendar.SECOND);
        return dateTime;
    }

    /**
     * 得到当前时间，没有特殊字符，使用yyyyMMddHHmmss格式
     *
     * @return
     */
    public static String parseDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return df.format(date);
    }
    public static Date toDate(String date){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            return df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 给一个时间加sec秒 返回 yyyyMMddHHmmss 格式日期
     *
     * @param date
     * @param sec
     * @return
     */
    public static String addDate(Date date, int sec) {
        Date d = new Date(date.getTime() + (sec * 1000));
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return df.format(d);
    }

    /**
     * 获取本周一凌晨0点
     * @return
     */
    public static Date getMinDateOfWeek(){
        Calendar cal =Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //获取本周一的日期
        return cal.getTime();
    }
    /**
     * 获取下周一凌晨0点
     * @return
     */
    public static Date getMinDateOfNextWeek(){
        Calendar cal =Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //获取本周一的日期
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        return cal.getTime();
    }
    /**
     * 获取本月一号凌晨0点
     * @return
     */
    public static Date getMinDateOfMonth(){
        Calendar cal =Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //获取本周一的日期
        return cal.getTime();
    }
    /**
     * 获取下月一号凌晨0点
     * @return
     */
    public static Date getMinDateOfNextMonth(){
        Calendar cal =Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //获取本周一的日期
        cal.add(Calendar.DAY_OF_WEEK, 1);
        return cal.getTime();
    }


    /**
     * date是否在今天中
     *
     * @param date
     * @return
     */
    public static boolean isToday(final Date date) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        return date.getTime() >= dayBegin(now).getTime()
                && date.getTime() <= dayEnd(now).getTime();
    }
    /**
     * 获取指定时间的那天 00:00:00.000 的时间
     *
     * @param date
     * @return
     */
    public static Date dayBegin(final Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }
    /**
     * 获取指定时间的那天 23:59:59.999 的时间
     *
     * @param date
     * @return
     */
    public static Date dayEnd(final Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    /**
     * 是否为本周
     * @param date
     * @return
     */
    public static boolean isWeek(final Date date) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        return date.getTime() >= weekBegin(now).getTime()
                && date.getTime() <= weekEnd(now).getTime();
    }

    /**
     * 是否为本月
     * @param date
     * @return
     */
    public static boolean isMonth(final Date date) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        return date.getTime() >= monthBegin(now).getTime()
                && date.getTime() <= monthEnd(now).getTime();
    }

    /**
     * 是否为本年
     * @param date
     * @return
     */
    public static boolean isYear(final Date date) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        return date.getTime() >= yearBegin(now).getTime()
                && date.getTime() <= yearEnd(now).getTime();
    }

    /**
     *
     *
     * @param date
     * @return
     */
    public static Date weekBegin(final Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }
    /**
     *
     *
     * @param date
     * @return
     */
    public static Date weekEnd(final Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }


    /**
     * 当月第一天
     * @return
     */
    private static String getFirstDay() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date theDate = calendar.getTime();

        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = df.format(gcLast.getTime());
        StringBuffer str = new StringBuffer().append(day_first).append(" 00:00:00");
        return str.toString();

    }
    /**
     *
     *
     * @param date
     * @return
     */
    public static Date monthBegin(final Date date) {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    /**
     *
     *
     * @param date
     * @return
     */
    public static Date monthEnd(final Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH,1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.DAY_OF_MONTH, -1);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }



    public static Date yearBegin(final Date date) {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date All(){
        Timestamp all = new Timestamp(1000l);
        return all;
    }
    /**
     *
     *
     * @param date
     * @return
     */
    public static Date yearEnd(final Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR,1);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.DAY_OF_MONTH, -1);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }


    /**
     * 解析 “时间、时间戳、日期”3种格式
     * @param date
     * @return
     */
    public static Date parse2(String date) {
        try {
            if (date == null || date.length() == 0 || !date.contains("-")) {
                return null;
            }
            if (date.length() == FormatConfig.TIME.length()) {
                return TimeFormat.parse(date);
            } else if (date.length() == FormatConfig.TIMESTAMP.length()) {
                return TimestampFormat.parse(date);
            } else if (date.length() == FormatConfig.SAMPLE1_DATE.length()) {
                return SampleDate1Format.parse(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getMin(Date start,Date end){
        return (int)(end.getTime()-start.getTime())/1000/60;
    }

}
