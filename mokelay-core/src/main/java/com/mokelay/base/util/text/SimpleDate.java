package com.mokelay.base.util.text;

import com.mokelay.base.util.io.Marshallable;
import com.mokelay.base.util.lang.Empty;
import com.mokelay.base.util.DataUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 只有年月日的日期
 *
 * @author system
 * @version Dec 25, 2002 5:36:15 PM
 */
public class SimpleDate
    implements Marshallable, Comparable
{
    private int year;
    private int month;
    private int day;

    private transient Date date;
    private transient long time;

    private static Calendar calendar = Calendar.getInstance();

    public static final int YEAR = Calendar.YEAR;
    public static final int MONTH = Calendar.MONTH;
    public static final int DAY = Calendar.DAY_OF_MONTH;

    public SimpleDate(int year, int month, int day)
    {
        setYear(year);
        setMonth(month);
        setDay(day);
    }

    public SimpleDate(Calendar calendar)
    {
        //this.date = calendar.getTime();
        //this.time = date.getTime();
        setFields(calendar);
    }

    public SimpleDate()
    {
        this(new Date());
    }

    public SimpleDate(Date date)
    {
        //this.date = date;
        //this.time = date.getTime();

        synchronized (calendar) {
            calendar.setTime(date);
            setFields(calendar);
        }
    }

    public SimpleDate(SimpleDateTime dateTime)
    {
        this(dateTime.getDate());
    }

    private void setFields(Calendar calendar)
    {
        setYear(calendar.get(Calendar.YEAR));
        setMonth(calendar.get(Calendar.MONTH) + 1);
        setDay(calendar.get(Calendar.DAY_OF_MONTH));
    }

    public SimpleDate(long time)
    {
        this(new Date(time));
    }

    public int getYear()
    {
        return year;
    }

    protected void setYear(int year)
    {
        this.year = year;
    }

    public int getMonth()
    {
        return month;
    }

    protected void setMonth(int month)
    {
        this.month = month;
    }

    public int getDay()
    {
        return day;
    }

    protected void setDay(int day)
    {
        this.day = day;
    }

    /**
     * 修改当前的日期，增加多少年，多少月，还有多少天，
     * 如果0表示不改变，负数表示减少
     *
     * @param years  年
     * @param months 月
     * @param days   日
     * @return
     */
    public SimpleDate add(int years, int months, int days)
    {
        synchronized (calendar) {
            copyTo(calendar);
            calendar.add(YEAR, years);
            calendar.add(MONTH, months);
            calendar.add(DAY, days);
            return new SimpleDate(calendar);
        }
    }

    /**
     * 获取后面几天几个月或者几个年的日期，单个字段增加适用，否则调用#add
     *
     * @return 下一天日期.
     */
    public SimpleDate next(int field, int value)
    {
        return next0(field, value);
    }

    /**
     * 获取前面几天几个月或者几个年的日期
     *
     * @return 下一天日期.
     */
    public SimpleDate prev(int field, int value)
    {
        return next0(field, -value);
    }

    /**
     * 添加几天，几个月或者几年
     *
     * @param value
     * @return
     */
    private SimpleDate next0(int field, int value)
    {
        synchronized (calendar) {
            copyTo(calendar);
            calendar.add(field, value);
            return new SimpleDate(calendar);
        }
    }


    public void copyTo(Calendar calendar)
    {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
    }

    public Calendar toCalendar()
    {
        Calendar calendar = Calendar.getInstance();
        copyTo(calendar);
        return calendar;
    }

    private transient int hashCode = 0;

    public int hashCode()
    {
        if (hashCode == 0) {
            hashCode = year + month + day;
        }
        return hashCode;
    }

    public boolean equals(Object obj)
    {
        if (obj == null) {
            return false;
        }

        if (obj instanceof SimpleDate) {
            SimpleDate date = (SimpleDate) obj;
            return year == date.year && month == date.month && day == date.day;
        }
        return false;
    }

    public int compareTo(SimpleDate date)
    {
        if (date == null) {
            return -1;
        }
        if (year > date.year) {
            return 1;
        }
        else if (year < date.year) {
            return -1;
        }

        if (month > date.month) {
            return 1;
        }
        else if (month < date.month) {
            return -1;
        }
        return day - date.day;
    }

    /**
     *
     * @param date
     * @return
     */
    public boolean before(SimpleDate date)
    {
        return compareTo(date) < 0;
    }

    /**
     * @param date
     * @return
     */
    public boolean after(SimpleDate date)
    {
        return compareTo(date) > 0;
    }

    public int compareTo(Object obj)
    {
        if (obj == null) {
            return -1;
        }

        if (obj instanceof SimpleDate) {
            return compareTo((SimpleDate) obj);
        }
        else if (obj instanceof Date) {
            return getDate().compareTo((Date) obj);
        }
        return -1;
    }

    public Date getDate()
    {
        if (date == null) {
            synchronized (calendar) {
                calendar.set(year, month - 1, day, 0, 0, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                date = calendar.getTime();
            }
        }
        return date;
    }

    public long getTime()
    {
        if (time == 0) {
            time = getDate().getTime();
            // clear millisecond.
            time = (time / 1000) * 1000;
        }
        return time;
    }

    /**
     * 将字符串解析成简单日期（yyyy-MM-dd）
     *
     * @param str 字符串
     */
    public static SimpleDate parse(String str)
    {
        int len = str.length();
        if (!(len == 10 || len == 8 || len == 6)) {
            throw new IllegalArgumentException("The date must be like yyyy-MM-dd");
        }

        if (len == 10) {
            return parse1(str, true);
        }
        else if (len == 6) {
            return parse0(str, false);
        }
        else if (str.indexOf('-') > 0) {
            return parse1(str, false);
        }
        else {
            return parse0(str, true);
        }
    }

    private static SimpleDate parse0(String str, boolean longYear)
    {
        int off = longYear ? 4 : 2;
        String s = str.substring(0, off);
        int year = DataUtil.getInt(s, -1);
        if (year == -1) {
            throw new IllegalArgumentException("Invalid year:" + s);
        }
        year = longYear(year, longYear);

        s = str.substring(off, (off += 2));
        int month = DataUtil.getInt(s, -1);
        if (month == -1) {
            throw new IllegalArgumentException("Invalid month:" + s);
        }
        s = str.substring(off, off + 2);
        int day = DataUtil.getInt(s, -1);
        if (day == -1) {
            throw new IllegalArgumentException("Invalid day:" + s);
        }
        return new SimpleDate(year, month, day);
    }

    private static int longYear(int year, boolean longYear)
    {
        if (!longYear) {
            if (year <= 30) {
                return 2000 + year;
            }
            else {
                return 1900 + year;
            }
        }
        return year;
    }

    private static SimpleDate parse1(String str, boolean longYear)
    {
        int off = longYear ? 4 : 2;
        String s = str.substring(0, off++);
        int year = DataUtil.getInt(s, -1);
        if (year == -1) {
            throw new IllegalArgumentException("Invalid year:" + s);
        }
        year = longYear(year, longYear);

        s = str.substring(off, (off += 2));
        int month = DataUtil.getInt(s, -1);
        if (month == -1) {
            throw new IllegalArgumentException("Invalid month:" + s);
        }
        s = str.substring(++off, off + 2);
        int day = DataUtil.getInt(s, -1);
        if (day == -1) {
            throw new IllegalArgumentException("Invalid day:" + s);
        }
        return new SimpleDate(year, month, day);
    }

    /**
     * 根据pattern解析字符串。
     */
    public static SimpleDate parse(String str, String pattern)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return parse(str, dateFormat);
    }

    /**
     * 根据pattern解析字符串。
     */
    public static SimpleDate parse(String str, DateFormat dateFormat)
    {
        try {
            Date date = dateFormat.parse(str);
            return new SimpleDate(date);
        }
        catch (Exception ex) {
            throw new IllegalArgumentException("Invalid parameters:" + str);
        }
    }

    public String toString(String pattern)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return toString(dateFormat);
    }

    public String toString(DateFormat dateFormat)
    {
        return dateFormat.format(getDate());
    }

    private transient char sep = '-';
    private transient String str;

    public StringBuilder format(StringBuilder sb, char sep)
    {
        sb.append(year);
        if (sep != 0) {
            sb.append(sep);
        }

        if (month < 10) {
            sb.append('0');
        }

        sb.append(month);
        if (sep != 0) {
            sb.append(sep);
        }

        if (day < 10) {
            sb.append('0');
        }
        sb.append(day);
        return sb;
    }


    public StringBuffer format(StringBuffer sb, char sep)
    {
        sb.append(year);
        if (sep != 0) {
            sb.append(sep);
        }

        if (month < 10) {
            sb.append('0');
        }

        sb.append(month);
        if (sep != 0) {
            sb.append(sep);
        }

        if (day < 10) {
            sb.append('0');
        }
        sb.append(day);
        return sb;
    }

    public String toString(char sep)
    {
        if (str == null || this.sep != sep) {
            StringBuffer sb = new StringBuffer(10);
            format(sb, sep);
            this.sep = sep;
            this.str = sb.toString();
        }
        return str;
    }

    public String toString()
    {
        return toString('-');
    }


    public SimpleDate(Empty empty)
    {
    }

    public void marshal(ObjectOutputStream oos)
        throws IOException
    {
        oos.writeInt(year);
        oos.writeInt(month);
        oos.writeInt(day);
    }

    public void demarshal(ObjectInputStream ois)
        throws IOException
    {
        year = ois.readInt();
        month = ois.readInt();
        day = ois.readInt();
    }

    private void writeObject(ObjectOutputStream oos)
        throws IOException
    {
        marshal(oos);
    }

    private void readObject(ObjectInputStream ois)
        throws IOException
    {
        demarshal(ois);
    }
}
