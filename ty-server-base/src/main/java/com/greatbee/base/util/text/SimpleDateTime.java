package com.greatbee.base.util.text;

import com.greatbee.base.util.io.Marshallable;
import com.greatbee.base.util.lang.Empty;
import com.greatbee.base.util.DataUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Date;


/**
 * 年月日小时分钟秒
 *
 * @author system
 * @version Dec 25, 2002 5:36:15 PM
 */
public class SimpleDateTime
    implements Marshallable, Comparable
{
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;

    private transient Date date;
    private transient long time;

    private transient SimpleDate simpleDate;
    private transient SimpleTime simpleTime;

    private static Calendar calendar = Calendar.getInstance();

    public SimpleDateTime(int year, int month, int day,
                          int hour, int minute, int second)
    {
        setYear(year);
        setMonth(month);
        setDay(day);
        setHour(hour);
        setMinute(minute);
        setSecond(second);
    }

    public SimpleDateTime(Date date)
    {
        this.date = date;
        this.time = date.getTime();

        synchronized (calendar) {
            calendar.setTime(date);
            setFields(calendar);
        }
    }

    public SimpleDateTime(SimpleDate date)
    {
        this(date.getDate());
    }

    private void setFields(Calendar calendar)
    {
        setYear(calendar.get(Calendar.YEAR));
        setMonth(calendar.get(Calendar.MONTH) + 1);
        setDay(calendar.get(Calendar.DAY_OF_MONTH));

        setHour(calendar.get(Calendar.HOUR_OF_DAY));
        setMinute(calendar.get(Calendar.MINUTE));
        setSecond(calendar.get(Calendar.SECOND));
    }

    public SimpleDateTime(Calendar calendar)
    {
        this.date = calendar.getTime();
        this.time = date.getTime();
        setFields(calendar);
    }

    public SimpleDateTime()
    {
        this(new Date());
    }

    public SimpleDateTime(long time)
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


    public int getHour()
    {
        return hour;
    }

    protected void setHour(int hour)
    {
        this.hour = hour;
    }

    public int getMinute()
    {
        return minute;
    }

    protected void setMinute(int minute)
    {
        this.minute = minute;
    }

    public int getSecond()
    {
        return second;
    }

    protected void setSecond(int second)
    {
        this.second = second;
    }

    public void copyTo(Calendar calendar)
    {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public Calendar toCalendar()
    {
        Calendar calendar = Calendar.getInstance();
        copyTo(calendar);
        return calendar;
    }

    public SimpleDate getSimpleDate()
    {
        if(simpleDate == null){
            simpleDate = new SimpleDate(year,month,day);
        }
        return simpleDate;
    }

    public SimpleTime getSimpleTime()
    {
        if(simpleTime == null){
            simpleTime = new SimpleTime(hour,minute,second);
        }
        return simpleTime;
    }

    private transient int hashCode = 0;

    public int hashCode()
    {
        if (hashCode == 0) {
            hashCode = year + month + day + hour + minute + second;
        }
        return hashCode;
    }

    public boolean equals(Object obj)
    {
        if (obj == null) {
            return false;
        }

        if (obj instanceof SimpleDateTime) {
            SimpleDateTime date = (SimpleDateTime) obj;
            return year == date.year && month == date.month && day == date.day
                   && hour == date.hour && minute == date.minute && second == date.second;
        }
        return false;
    }

    public int compareTo(SimpleDateTime date)
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

        if (day > date.day) {
            return 1;
        }
        else if (day < date.day) {
            return -1;
        }

        if (hour > date.hour) {
            return 1;
        }
        else if (hour < date.hour) {
            return -1;
        }

        if (minute > date.minute) {
            return 1;
        }
        else if (minute < date.minute) {
            return -1;
        }

        return second - date.second;
    }

    public int compareTo(Object obj)
    {
        if (obj == null) {
            return -1;
        }

        if (obj instanceof SimpleDateTime) {
            return compareTo((SimpleDateTime) obj);
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
                calendar.set(year, month - 1, day, hour, minute, second);
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
        }
        return time;
    }

    /**
     * 将字符串解析成简单日期（yyyy-MM-dd HH:mm:ss）
     *
     * @param str 字符串
     */
    public static SimpleDateTime parse(String str)
    {
        if (str.length() != 19) {
            throw new IllegalArgumentException("The date must be like yyyy-MM-dd  HH:mm:ss");
        }

        String s = str.substring(0, 4);
        int year = DataUtil.getInt(s, -1);
        if (year == -1) {
            throw new IllegalArgumentException("Invalid year:" + s);
        }
        s = str.substring(5, 7);
        int month = DataUtil.getInt(s, -1);
        if (month == -1) {
            throw new IllegalArgumentException("Invalid month:" + s);
        }
        s = str.substring(8, 10);
        int day = DataUtil.getInt(s, -1);
        if (day == -1) {
            throw new IllegalArgumentException("Invalid day:" + s);
        }

        s = str.substring(11, 13);
        int hour = DataUtil.getInt(s, -1);
        if (hour == -1) {
            throw new IllegalArgumentException("Invalid hour:" + s);
        }
        s = str.substring(14, 16);
        int minute = DataUtil.getInt(s, -1);
        if (minute == -1) {
            throw new IllegalArgumentException("Invalid minute:" + s);
        }
        s = str.substring(17, 19);
        int second = DataUtil.getInt(s, -1);
        if (second == -1) {
            throw new IllegalArgumentException("Invalid second:" + s);
        }


        return new SimpleDateTime(year, month, day, hour, minute, second);
    }

    private transient char sep1 = '-';
    private transient char sep2 = ' ';
    private transient char sep3 = ':';

    private transient String str;

    public StringBuilder format(StringBuilder sb, char sep1,
                                char sep2, char sep3)
    {
        //yyyy-MM-dd
        sb.append(year);
        if (sep1 != 0) {
            sb.append(sep1);
        }

        if (month < 10) {
            sb.append('0');
        }
        sb.append(month);
        if (sep1 != 0) {
            sb.append(sep1);
        }

        if (day < 10) {
            sb.append('0');
        }
        sb.append(day);

        if (sep2 != 0) {
            sb.append(sep2);
        }

        //HH:mm:ss
        if (hour < 10) {
            sb.append('0');
        }
        sb.append(hour);

        if (sep3 != 0) {
            sb.append(sep3);
        }

        if (minute < 10) {
            sb.append('0');
        }
        sb.append(minute);

        if (sep3 != 0) {
            sb.append(sep3);
        }

        if (second < 10) {
            sb.append('0');
        }
        sb.append(second);
        return sb;
    }

    public StringBuffer format(StringBuffer sb, char sep1,
                               char sep2, char sep3)
    {
        //yyyy-MM-dd
        sb.append(year);
        if (sep1 != 0) {
            sb.append(sep1);
        }

        if (month < 10) {
            sb.append('0');
        }
        sb.append(month);
        if (sep1 != 0) {
            sb.append(sep1);
        }

        if (day < 10) {
            sb.append('0');
        }
        sb.append(day);

        if (sep2 != 0) {
            sb.append(sep2);
        }

        //HH:mm:ss
        if (hour < 10) {
            sb.append('0');
        }
        sb.append(hour);

        if (sep3 != 0) {
            sb.append(sep3);
        }

        if (minute < 10) {
            sb.append('0');
        }
        sb.append(minute);

        if (sep3 != 0) {
            sb.append(sep3);
        }

        if (second < 10) {
            sb.append('0');
        }
        sb.append(second);
        return sb;
    }

    public String toString(char sep1, char sep2, char sep3)
    {
        if (str == null || this.sep1 != sep1 ||
            this.sep2 != sep2 || this.sep3 != sep3) {

            StringBuffer sb = new StringBuffer(19);
            format(sb, sep1, sep2, sep3);
            this.sep1 = sep1;
            this.sep2 = sep2;
            this.sep3 = sep3;
            this.str = sb.toString();
        }
        return str;
    }

    public String toString()
    {
        return toString('-', ' ', ':');
    }

    public SimpleDateTime(Empty empty)
    {
    }

    public void marshal(ObjectOutputStream oos)
        throws IOException
    {
        oos.writeInt(year);
        oos.writeInt(month);
        oos.writeInt(day);
        oos.writeInt(hour);
        oos.writeInt(minute);
        oos.writeInt(second);
    }

    public void demarshal(ObjectInputStream ois)
        throws IOException
    {
        year = ois.readInt();
        month = ois.readInt();
        day = ois.readInt();
        hour = ois.readInt();
        minute = ois.readInt();
        second = ois.readInt();
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
