package com.mokelay.base.util.text;

import com.mokelay.base.util.io.Marshallable;
import com.mokelay.base.util.lang.Empty;
import com.mokelay.base.util.DataUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Date;

/**
 * 只有小时分钟和秒的时间
 *
 * @author system
 * @version Dec 25, 2002 5:36:15 PM
 */
public class SimpleTime
    implements Marshallable, Comparable
{
    private int hour;
    private int minute;
    private int second;

    private static Calendar calendar = Calendar.getInstance();

    public SimpleTime(int hour, int minute, int second)
    {
        setHour(hour);
        setMinute(minute);
        setSecond(second);
    }

    public SimpleTime(SimpleDateTime time)
    {
        this(time.getDate());
    }

    public SimpleTime(Date date)
    {
        synchronized (calendar) {
            calendar.setTime(date);
            setFields(calendar);
        }
    }

    public SimpleTime(Calendar calendar)
    {
        setFields(calendar);
    }

    public SimpleTime()
    {
        this(new Date());
    }

    private void setFields(Calendar calendar)
    {
        setHour(calendar.get(Calendar.HOUR_OF_DAY));
        setMinute(calendar.get(Calendar.MINUTE));
        setSecond(calendar.get(Calendar.SECOND));
    }


    public SimpleTime(long time)
    {
        this(new Date(time));
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
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
    }

    private transient int hashCode = 0;

    public int hashCode()
    {
        if (hashCode == 0) {
            hashCode = hour + minute + second;
        }
        return hashCode;
    }

    public boolean equals(Object obj)
    {
        if (obj == null) {
            return false;
        }

        if (obj instanceof SimpleTime) {
            SimpleTime time = (SimpleTime)obj;
            return hour == time.hour && minute == time.minute && second == time.second;
        }
        return false;
    }


    public int compareTo(SimpleTime time)
    {
        if (time == null) {
            return -1;
        }
        if (hour > time.hour) {
            return 1;
        }
        else if (hour < time.hour) {
            return -1;
        }

        if (minute > time.minute) {
            return 1;
        }
        else if (minute < time.minute) {
            return -1;
        }
        return second - time.second;
    }

    public int compareTo(Object obj)
    {
        if (obj == null) {
            return -1;
        }

        if (obj instanceof SimpleTime) {
            return compareTo((SimpleTime)obj);
        }
        return -1;
    }

    /**
     * 将字符串解析成简单时间（HH-mm-ss）
     *
     * @param str 字符串
     */
    public static SimpleTime parse(String str)
    {
        int len = str.length();
        if (!(len == 8 || len == 6)) {
            throw new IllegalArgumentException("The time must be like HH-mm-ss");
        }

        if (len == 6) {
            return parse0(str);
        }
        else {
            return parse1(str);
        }
    }

    private static SimpleTime parse0(String str)
    {
        String s = str.substring(0, 2);
        int hour = DataUtil.getInt(s, -1);
        if (hour == -1) {
            throw new IllegalArgumentException("Invalid hour:" + s);
        }
        s = str.substring(2, 4);
        int minute = DataUtil.getInt(s, -1);
        if (minute == -1) {
            throw new IllegalArgumentException("Invalid minute:" + s);
        }
        s = str.substring(4, 6);
        int second = DataUtil.getInt(s, -1);
        if (second == -1) {
            throw new IllegalArgumentException("Invalid second:" + s);
        }

        return new SimpleTime(hour, minute, second);
    }

    private static SimpleTime parse1(String str)
    {
        String s = str.substring(0, 2);
        int hour = DataUtil.getInt(s, -1);
        if (hour == -1) {
            throw new IllegalArgumentException("Invalid hour:" + s);
        }
        s = str.substring(3, 5);
        int minute = DataUtil.getInt(s, -1);
        if (minute == -1) {
            throw new IllegalArgumentException("Invalid minute:" + s);
        }
        s = str.substring(6, 8);
        int second = DataUtil.getInt(s, -1);
        if (second == -1) {
            throw new IllegalArgumentException("Invalid second:" + s);
        }

        return new SimpleTime(hour, minute, second);
    }

    private transient char sep = ':';
    private transient String str;

    public StringBuffer format(StringBuffer sb, char sep)
    {
        if (hour < 10) {
            sb.append('0');
        }
        sb.append(hour);

        if (sep != 0) {
            sb.append(sep);
        }

        if (minute < 10) {
            sb.append('0');
        }
        sb.append(minute);

        if (sep != 0) {
            sb.append(sep);
        }

        if (second < 10) {
            sb.append('0');
        }
        sb.append(second);
        return sb;
    }

    public String toString(char sep)
    {
        if (str == null || this.sep != sep) {
            StringBuffer sb = new StringBuffer(8);
            format(sb, sep);
            this.sep = sep;
            this.str = sb.toString();
        }
        return str;
    }

    public String toString()
    {
        return toString(':');
    }

    public SimpleTime(Empty empty)
    {
    }

    public void marshal(ObjectOutputStream oos)
        throws IOException
    {
        oos.writeInt(hour);
        oos.writeInt(minute);
        oos.writeInt(second);
    }

    public void demarshal(ObjectInputStream ois)
        throws IOException
    {
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

