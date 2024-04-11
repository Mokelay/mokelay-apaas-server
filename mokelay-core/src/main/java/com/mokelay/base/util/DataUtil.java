package com.mokelay.base.util;

import com.mokelay.base.util.text.SimpleDate;
import com.mokelay.base.util.text.SimpleDateTime;
import com.mokelay.base.util.text.SimpleTime;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * ��ݹ�����
 *
 * @author system
 * @version 1.00 2005-2-6 21:05:23
 */
public class DataUtil
{
    /**
     * Convert value to boolean
     *
     * @param value the object to convert from
     * @return the converted boolean
     */
    public static final boolean getBoolean(Object value, boolean defValue)
    {
        if (value == null) {
            return defValue;
        }

        boolean result = defValue;
        if (value instanceof Boolean) {
            result = ((Boolean)value).booleanValue();
        }
        else if (value instanceof String) {
            Boolean bool = BooleanUtil.toBoolean((String)value);
            result = bool != null ? bool.booleanValue() : defValue;
        }
        else if (value instanceof String[]) {
            String[] strs = (String[])value;
            if (strs.length > 0) {
                result = getBoolean(strs[0], defValue);
            }
            else {
                result = defValue;
            }
        }
        else if (value instanceof boolean[]) {
            boolean[] bools = (boolean[])value;
            result = bools.length > 0 ? bools[0] : defValue;
        }
        else if (value instanceof Number) {
            long l = ((Number)value).longValue();
            result = l != 0;
        }
        else {
            result = defValue;
        }
        return result;
    }

    /**
     * Convert value to byte
     *
     * @param value the object to convert from
     * @return the converted byte
     */
    public static final byte getByte(Object value, byte defValue)
    {
        if (value == null) {
            return defValue;
        }

        byte result = defValue;
        if (value instanceof Number) {
            result = ((Number)value).byteValue();
        }
        else if (value instanceof String) {
            String str = (String)value;
            try {
                int radix = 10;
                if (str.length() > 2 && str.charAt(0) == '0') {
                    char c = str.charAt(1);
                    switch (c) {
                        case'x':
                        case'X':
                            radix = 16;
                            break;
                        case'o':
                        case'O':
                            radix = 8;
                            break;
                        case'b':
                        case'B':
                            radix = 2;
                            break;
                    }
                    if (radix != 10) {
                        str = str.substring(2);
                    }
                }
                result = (byte)Integer.parseInt(str, radix);
            }
            catch (Exception ex) {
                result = defValue;
            }
        }
        else if (value instanceof String[]) {
            String[] strs = (String[])value;
            if (strs.length > 0) {
                result = getByte(strs[0], defValue);
            }
            else {
                result = defValue;
            }
        }
        else if (value instanceof Boolean) {
            result = ((Boolean)value).booleanValue() ? (byte)1 : (byte)0;
        }
        else {
            result = defValue;
        }

        return result;
    }


    /**
     * Convert value to short
     *
     * @param value the object to convert from
     * @return the converted short
     */
    public static final short getShort(Object value, short defValue)
    {
        if (value == null) {
            return defValue;
        }

        short result = defValue;
        if (value instanceof Number) {
            result = ((Number)value).shortValue();
        }
        else if (value instanceof String) {
            String str = (String)value;
            try {
                int radix = 10;
                if (str.length() > 2 && str.charAt(0) == '0') {
                    char c = str.charAt(1);
                    switch (c) {
                        case'x':
                        case'X':
                            radix = 16;
                            break;
                        case'o':
                        case'O':
                            radix = 8;
                            break;
                        case'b':
                        case'B':
                            radix = 2;
                            break;
                    }
                    if (radix != 10) {
                        str = str.substring(2);
                    }
                }
                result = Short.parseShort(str, radix);
            }
            catch (Exception ex) {
                result = defValue;
            }
        }
        else if (value instanceof String[]) {
            String[] strs = (String[])value;
            if (strs.length > 0) {
                result = getShort(strs[0], defValue);
            }
            else {
                result = defValue;
            }
        }
        else {
            result = defValue;
        }

        return result;
    }

    /**
     * Convert value to char
     *
     * @param value the object to convert from
     * @return the converted char
     */
    public static final char getChar(Object value, char defValue)
    {
        char result = '\0';
        if (value != null) {
            if (value instanceof Character) {
                result = ((Character)value).charValue();
            }
            else if (value instanceof String) {
                String str = (String)value;
                if (str.length() > 0) {
                    result = str.charAt(0);
                }
                else {
                    result = defValue;
                }
            }
            else if (value instanceof Boolean) {
                boolean bool = ((Boolean)value).booleanValue();
                result = bool ? 'Y' : 'N';
            }
            else {
                result = defValue;
            }
        }
        else {
            result = defValue;
        }
        return result;
    }

    /**
     * Convert value to int
     *
     * @param value the object to convert from
     * @return the converted int
     */
    public static final int getInt(Object value, int defValue)
    {
        if (value == null) {
            return defValue;
        }

        int result = defValue;
        if (value instanceof Number) {
            result = ((Number)value).intValue();
        }
        else if (value instanceof String) {
            String str = (String)value;
            try {
                int radix = 10;
                if (str.length() > 2 && str.charAt(0) == '0') {
                    char c = str.charAt(1);
                    switch (c) {
                        case'x':
                        case'X':
                            radix = 16;
                            break;
                        case'o':
                        case'O':
                            radix = 8;
                            break;
                        case'b':
                        case'B':
                            radix = 2;
                            break;
                    }
                    if (radix != 10) {
                        str = str.substring(2);
                    }
                }
                result = Integer.parseInt(str, radix);
            }
            catch (Exception ex) {
                result = defValue;
            }
        }
        else if (value instanceof String[]) {
            String[] strs = (String[])value;
            if (strs.length > 0) {
                result = getInt(strs[0], defValue);
            }
            else {
                result = defValue;
            }
        }
        else if (value instanceof Boolean) {
            Boolean b = (Boolean)value;
            return b.booleanValue() ? 1 : 0;
        }
        else {
            result = defValue;
        }

        return result;
    }


    /**
     * Convert value to long
     *
     * @param value the object to convert from
     * @return the converted long
     */
    public static final long getLong(Object value, long defValue)
    {
        if (value == null) {
            return defValue;
        }

        long result = defValue;
        if (value instanceof Number) {
            result = ((Number)value).longValue();
        }
        else if (value instanceof String) {
            String str = (String)value;
            try {
                int radix = 10;
                if (str.length() > 2 && str.charAt(0) == '0') {
                    char c = str.charAt(1);
                    switch (c) {
                        case'x':
                        case'X':
                            radix = 16;
                            break;
                        case'o':
                        case'O':
                            radix = 8;
                            break;
                        case'b':
                        case'B':
                            radix = 2;
                            break;
                    }
                    if (radix != 10) {
                        str = str.substring(2);
                    }
                }
                result = Long.parseLong(str, radix);
            }
            catch (Exception ex) {
                result = defValue;
            }
        }
        else if (value instanceof String[]) {
            String[] strs = (String[])value;
            if (strs.length > 0) {
                result = getLong(strs[0], defValue);
            }
            else {
                result = defValue;
            }
        }
        else if (value instanceof Boolean) {
            Boolean b = (Boolean)value;
            return b.booleanValue() ? 1l : 0l;
        }
        else {
            result = defValue;
        }

        return result;
    }

    /**
     * Convert value to float
     *
     * @param value the object to convert from
     * @return the converted float
     */
    public static final float getFloat(Object value, float defValue)
    {
        if (value == null) {
            return defValue;
        }

        float result = defValue;
        if (value instanceof Number) {
            result = ((Number)value).floatValue();
        }
        else if (value instanceof String) {
            String str = (String)value;
            try {
                result = Float.parseFloat(str);
            }
            catch (Exception ex) {
                result = defValue;
            }
        }
        else if (value instanceof String[]) {
            String[] strs = (String[])value;
            if (strs.length > 0) {
                result = getFloat(strs[0], defValue);
            }
            else {
                result = defValue;
            }
        }
        else if (value instanceof Boolean) {
            Boolean b = (Boolean)value;
            return b.booleanValue() ? 1f : 0f;
        }
        else {
            result = defValue;
        }

        return result;
    }

    /**
     * Convert value to double
     *
     * @param value the object to convert from
     * @return the converted double
     */
    public static final double getDouble(Object value, double defValue)
    {
        if (value == null) {
            return defValue;
        }

        double result = defValue;
        if (value instanceof Number) {
            result = ((Number)value).doubleValue();
        }
        else if (value instanceof String) {
            String str = (String)value;
            try {
                result = Double.parseDouble(str);
            }
            catch (Exception ex) {
                result = defValue;
            }
        }
        else if (value instanceof String[]) {
            String[] strs = (String[])value;
            if (strs.length > 0) {
                result = getDouble(strs[0], defValue);
            }
            else {
                result = defValue;
            }
        }
        else if (value instanceof Boolean) {
            Boolean b = (Boolean)value;
            return b.booleanValue() ? 1d : 0d;
        }
        else {
            result = defValue;
        }

        return result;
    }

    /**
     * Convert value to String
     *
     * @param value the object to convert from
     * @return the converted String
     */
    public static final String getString(Object value, String defValue)
    {
        if (value == null) {
            return defValue;
        }

        String str = defValue;
        if (value instanceof String) {
            str = (String)value;
        }
        else if (value instanceof String[]) {
            String[] values = (String[])value;
            str = values.length > 0 ? values[0] : value.toString();
        }
        else {
            str = value.toString();
        }
        return str;
    }

    /**
     * Convert value to byte[]
     *
     * @param value the value to convert from. This must be a byte array, or
     *              null
     * @return a copy of the supplied array, or null
     */
    public static final byte[] getByteArray(Object value)
    {
        return getBytes(value, null);
    }

    /**
     * Convert value to byte[]
     *
     * @param value the value to convert from. This must be a byte array, or
     *              null
     * @return a copy of the supplied array, or null
     */
    public static final byte[] getByteArray(Object value, byte[] defValue)
    {
        return getBytes(value, defValue);
    }

    /**
     * Convert value to byte[]
     *
     * @param value the value to convert from. This must be a byte array, or
     *              null
     * @return a copy of the supplied array, or null
     */
    public static final byte[] getBytes(Object value, byte[] defValue)
    {
        if (value == null) {
            return defValue;
        }

        byte[] result = null;
        if (value instanceof byte[]) {
            result = (byte[])value;
        }
        else if (value instanceof String) {
            result = StringUtil.toBytes((String)value);
        }
        else if (value instanceof Number) {
            result = new byte[]{getByte(value, (byte)0)};
        }
        else if (value instanceof Object[]) {
            Object[] array = (Object[])value;
            result = new byte[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = getByte(array[i], (byte)0);
            }
        }
        else if (value.getClass().isArray()) {
            int len = Array.getLength(value);
            result = new byte[len];
            for (int i = 0; i < len; i++) {
                result[i] = getByte(Array.get(value, i), (byte)0);
            }
        }
        else if (value instanceof Boolean) {
            result = new byte[]{getByte(value, (byte)0)};
        }
        else {
            result = defValue;
        }
        return result;
    }

    /**
     * Convert value to byte[]
     *
     * @param value the value to convert from. This must be a byte array, or
     *              null
     * @return a copy of the supplied array, or null
     */
    public static final char[] getCharArray(Object value)
    {
        return getChars(value, null);
    }

    /**
     * Convert value to byte[]
     *
     * @param value the value to convert from. This must be a byte array, or
     *              null
     * @return a copy of the supplied array, or null
     */
    public static final char[] getCharArray(Object value, char[] defValue)
    {
        return getChars(value, defValue);
    }

    /**
     * Convert value to byte[]
     *
     * @param value the value to convert from. This must be a byte array, or
     *              null
     * @return a copy of the supplied array, or null
     */
    public static final char[] getChars(Object value, char[] defValue)
    {
        char[] result = null;

        if (value instanceof char[]) {
            result = (char[])value;
        }
        else if (value instanceof String) {
            result = ((String)value).toCharArray();
        }
        else {
            result = defValue;
        }
        return result;
    }

    /**
     * 把Object转换为字符数组
     *
     */
    public static final String[] getStringArray(Object object)
    {
        return getStringArray(object, StringUtil.EMPTY_STRING_ARRAY);
    }

    /**
     * 把Object转换为字符数组
     *
     * @param object
     * @param defValue
     * @return
     */
    public static final String[] getStringArray(Object object, String[] defValue)
    {
        if (object == null) {
            return null;
        }

        String[] result = null;
        if (object instanceof String[]) {
            result = (String[])object;
        }
        else if (object instanceof String) {
            result = new String[]{(String)object};
        }
        else if (object instanceof Object[]) {
            Object[] array = (Object[])object;
            result = new String[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = getString(array[i], null);
            }
        }
        else if (object.getClass().isArray()) {
            //����Array
            int len = Array.getLength(object);
            result = new String[len];
            for (int i = 0; i < len; i++) {
                result[i] = getString(Array.get(object, i), null);
            }
        }
        else if (object instanceof Number
                 || object instanceof Boolean
                 || object instanceof Character) {
            result = new String[]{object.toString()};
        }
        else {
            result = defValue;
        }
        return result;
    }

    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public static final char[] EMPTY_CHAR_ARRAY = new char[0];
    public static final short[] EMPTY_SHORT_ARRAY = new short[0];
    public static final int[] EMPTY_INT_ARRAY = new int[0];
    public static final long[] EMPTY_LONG_ARRAY = new long[0];
    public static final float[] EMPTY_FLOAT_ARRAY = new float[0];
    public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
    public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];

    /**
     * 获取Boolean 数组
     *
     * @param object
     * @param defValue
     * @return
     */
    public static final boolean[] getBooleanArray(Object object, boolean[] defValue)
    {
        if (object == null) {
            return defValue;
        }

        boolean[] result = null;
        if (object instanceof boolean[]) {
            result = (boolean[])object;
        }
        else if (object instanceof Boolean) {
            result = new boolean[]{((Boolean)object).booleanValue()};
        }
        else if (object instanceof Number) {
            result = new boolean[]{getBoolean(object, false)};
        }
        else if (object instanceof Object[]) {
            Object[] array = (Object[])object;
            result = new boolean[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = getBoolean(array[i], false);
            }
        }
        else if (object.getClass().isArray()) {
            int len = Array.getLength(object);
            result = new boolean[len];
            for (int i = 0; i < len; i++) {
                result[i] = getBoolean(Array.get(object, i), false);
            }
        }
        else if (object instanceof String) {
            String[] array = StringUtil.toStringArray((String)object, ',');
            result = new boolean[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = getBoolean(array[i], false);
            }
        }
        else {
            result = defValue;
        }
        return result;
    }

    /**
     * Boolean 数组
     *
     * @param object
     * @return
     */
    public static final boolean[] getBooleanArray(Object object)
    {
        return getBooleanArray(object, null);
    }

    /**
     * Int Array
     *
     * @param object
     * @param defValue
     * @return
     */
    public static final int[] getIntArray(Object object, int[] defValue)
    {
        if (object == null) {
            return defValue;
        }

        int[] result = null;
        if (object instanceof int[]) {
            result = (int[])object;
        }
        else if (object instanceof String) {
            String[] array = StringUtil.toStringArray((String)object, ',');
            result = new int[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = getInt(array[i], 0);
            }
        }
        else if (object instanceof Number) {
            result = new int[]{getInt(object, 0)};
        }
        else if (object instanceof Object[]) {
            Object[] array = (Object[])object;
            result = new int[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = getInt(array[i], 0);
            }
        }
        else if (object.getClass().isArray()) {
            int len = Array.getLength(object);
            result = new int[len];
            for (int i = 0; i < len; i++) {
                result[i] = getInt(Array.get(object, i), 0);
            }
        }
        else if (object instanceof Boolean) {
            result = new int[]{getInt(object, 0)};
        }
        else {
            result = defValue;
        }
        return result;
    }

    /**
     * Get In Array
     *
     * @param object
     * @return
     */
    public static final int[] getIntArray(Object object)
    {
        return getIntArray(object, null);
    }

    /**
     * Get Short Array
     *
     * @param object
     * @param defValue
     * @return
     */
    public static final short[] getShortArray(Object object, short[] defValue)
    {
        if (object == null) {
            return defValue;
        }

        short[] result = null;
        if (object instanceof short[]) {
            result = (short[])object;
        }
        else if (object instanceof String) {
            String[] array = StringUtil.toStringArray((String)object, ',');
            result = new short[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = getShort(array[i], (short)0);
            }
        }
        else if (object instanceof Number) {
            result = new short[]{getShort(object, (short)0)};
        }
        else if (object instanceof Object[]) {
            Object[] array = (Object[])object;
            result = new short[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = getShort(array[i], (short)0);
            }
        }
        else if (object.getClass().isArray()) {
            int len = Array.getLength(object);
            result = new short[len];
            for (int i = 0; i < len; i++) {
                result[i] = getShort(Array.get(object, i), (short)0);
            }
        }
        else if (object instanceof Boolean) {
            result = new short[]{getShort(object, (short)0)};
        }
        else {
            result = defValue;
        }
        return result;
    }

    /**
     * ���ض���������
     *
     * @param object ���� �����޷�ת����ʱ�򷵻�<code>null</code>
     */
    public static final short[] getShortArray(Object object)
    {
        return getShortArray(object, null);
    }

    /**
     * ���س���������
     *
     * @param object   ���������޷�ת����ʱ�򷵻�Ĭ��ֵ
     * @param defValue Ĭ��ֵ
     */
    public static final long[] getLongArray(Object object, long[] defValue)
    {
        if (object == null) {
            return defValue;
        }

        long[] result = null;
        if (object instanceof long[]) {
            result = (long[])object;
        }
        else if (object instanceof String) {
            String[] array = StringUtil.toStringArray((String)object, ',');
            result = new long[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = getLong(array[i], 0);
            }
        }
        else if (object instanceof Number) {
            result = new long[]{getLong(object, 0)};
        }
        else if (object instanceof Object[]) {
            Object[] array = (Object[])object;
            result = new long[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = getLong(array[i], 0);
            }
        }
        else if (object.getClass().isArray()) {
            int len = Array.getLength(object);
            result = new long[len];
            for (int i = 0; i < len; i++) {
                result[i] = getLong(Array.get(object, i), 0l);
            }
        }
        else if (object instanceof Boolean) {
            result = new long[]{getLong(object, 0)};
        }
        else {
            result = defValue;
        }
        return result;
    }

    /**
     * ���س���������
     *
     * @param object ���� �����޷�ת����ʱ�򷵻�<code>null</code>
     */
    public static final long[] getLongArray(Object object)
    {
        return getLongArray(object, null);
    }

    /**
     * ���ص;��ȸ���������
     *
     * @param object   ���������޷�ת����ʱ�򷵻�Ĭ��ֵ
     * @param defValue Ĭ��ֵ
     */
    public static final float[] getFloatArray(Object object, float[] defValue)
    {
        if (object == null) {
            return defValue;
        }

        float[] result = null;
        if (object instanceof float[]) {
            result = (float[])object;
        }
        else if (object instanceof String) {
            String[] array = StringUtil.toStringArray((String)object, ',');
            result = new float[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = getFloat(array[i], 0.0f);
            }
        }
        else if (object instanceof Number) {
            result = new float[]{getFloat(object, 0.0f)};
        }
        else if (object instanceof Object[]) {
            Object[] array = (Object[])object;
            result = new float[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = getFloat(array[i], 0.0f);
            }
        }
        else if (object.getClass().isArray()) {
            int len = Array.getLength(object);
            result = new float[len];
            for (int i = 0; i < len; i++) {
                result[i] = getFloat(Array.get(object, i), 0.0f);
            }
        }
        else if (object instanceof Boolean) {
            result = new float[]{getFloat(object, 0.0f)};
        }
        else {
            result = defValue;
        }
        return result;
    }

    /**
     * ���ص;��ȸ���������
     *
     * @param object ���� �����޷�ת����ʱ�򷵻�<code>null</code>
     */
    public static final float[] getFloatArray(Object object)
    {
        return getFloatArray(object, null);
    }

    /**
     * ���ظ߾��ȸ���������
     *
     * @param object   ���������޷�ת����ʱ�򷵻�Ĭ��ֵ
     * @param defValue Ĭ��ֵ
     */
    public static final double[] getDoubleArray(Object object, double[] defValue)
    {
        if (object == null) {
            return defValue;
        }

        double[] result = null;
        if (object instanceof double[]) {
            result = (double[])object;
        }
        else if (object instanceof String) {
            String[] array = StringUtil.toStringArray((String)object, ',');
            result = new double[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = getDouble(array[i], 0.0d);
            }
        }
        else if (object instanceof Number) {
            result = new double[]{getDouble(object, 0.0d)};
        }
        else if (object instanceof Object[]) {
            Object[] array = (Object[])object;
            result = new double[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = getDouble(array[i], 0.0d);
            }
        }
        else if (object.getClass().isArray()) {
            int len = Array.getLength(object);
            result = new double[len];
            for (int i = 0; i < len; i++) {
                result[i] = getDouble(Array.get(object, i), 0.0d);
            }
        }
        else if (object instanceof Boolean) {
            result = new double[]{getDouble(object, 0.0d)};
        }
        else {
            result = defValue;
        }
        return result;
    }

    /**
     * ���ظ߾��ȸ���������
     *
     * @param object ���� �����޷�ת����ʱ�򷵻�<code>null</code>
     */
    public static final double[] getDoubleArray(Object object)
    {
        return getDoubleArray(object, null);
    }

    private static DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static DateFormat DEFAULT_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * ����ʱ������
     *
     * @param obj
     * @return
     */
    public static final Date getDateTime(Object obj)
    {
        return getDateTime(obj, null);
    }

    /**
     * Formate Date
     *
     * @param obj
     * @return
     */
    public static final String formatDate(Object obj)
    {
        return formatDate(obj, DEFAULT_DATE_FORMAT);
    }

    /**
     * Format Date Time
     *
     * @param obj
     * @return
     */
    public static final String formatDateTime(Object obj)
    {
        return formatDate(obj, DEFAULT_DATE_TIME_FORMAT);
    }

    /**
     * Format
     *
     * @param obj
     * @param dateFormat
     * @return
     */
    public static final String formatDate(Object obj, DateFormat dateFormat)
    {
        if (obj == null) {
            return null;
        }

        if (obj instanceof String) {
            return (String)obj;
        }

        if (obj instanceof Date) {
            synchronized (dateFormat) {
                try {
                    return dateFormat.format((Date)obj);
                }
                catch (Exception e) {
                }
            }
        }

        return String.valueOf(obj);
    }

    /**
     * ����ʱ������
     *
     * @param obj
     * @param defValue
     * @return
     */
    public static final Date getDateTime(Object obj, Date defValue)
    {
        return getDateTime(obj, DEFAULT_DATE_TIME_FORMAT, defValue);
    }

    /**
     * ����ʱ������
     *
     * @param obj
     * @param format
     * @param defValue
     * @return
     */
    public static final Date getDateTime(Object obj, DateFormat format, Date defValue)
    {
        return getDate(obj, format, defValue);
    }

    /**
     * �������ڣ�Ĭ����null
     *
     * @param obj ����
     */
    public static final Date getDate(Object obj)
    {
        return getDate(obj, DEFAULT_DATE_FORMAT, null);
    }

    /**
     * ��ݶ��󷵻�����
     *
     * @param obj      ����
     * @param defValue Ĭ��ֵ
     */
    public static final Date getDate(Object obj, Date defValue)
    {
        return getDate(obj, DEFAULT_DATE_FORMAT, defValue);
    }

    /**
     * �������ڣ�Ĭ����null
     *
     * @param obj ����
     */
    public static final Date getDate(Object obj, DateFormat format)
    {
        return getDate(obj, format, null);
    }

    /**
     * ��ݶ��󷵻�����
     *
     * @param obj      ����
     * @param defValue Ĭ��ֵ
     */
    public static final Date getDate(Object obj, DateFormat format, Date defValue)
    {
        if (obj instanceof Date) {
            return (Date)obj;
        }
        else {
            if (obj instanceof Timestamp) {
                Timestamp timestamp = (Timestamp)obj;
                return new Date(timestamp.getTime()
                                + timestamp.getNanos() / 1000000);
            }
            else if (obj instanceof Long) {
                return new Date(((Long)obj).longValue());
            }
            else if (obj instanceof String) {
                synchronized (format) {
                    try {
                        return format.parse((String)obj);
                    }
                    catch (Exception e) {
                        //TODO
                        try {
                            long t = Long.valueOf((String)obj);
                            if (t > 0) {
                                return new Date(t);
                            }
                            else {
                                return defValue;
                            }
                        }
                        catch (Throwable t) {
                            return defValue;
                        }
                    }
                }
            }
            else if (obj instanceof String[]) {
                String[] strs = (String[])obj;
                if (strs.length > 0) {
                    return getDate(strs[0], defValue);
                }
                else {
                    return defValue;
                }
            }
            else if (obj instanceof SimpleDate) {
                return ((SimpleDate)obj).getDate();
            }
            else if (obj instanceof SimpleDateTime) {
                return ((SimpleDateTime)obj).getDate();
            }
            return defValue;
        }
    }

    /**
     * ������ת����ʱ���
     */
    public static final Timestamp getTimestamp(Object obj)
    {
        return getTimestamp(obj, null);
    }

    /**
     * ������ת����ʱ���
     */
    public static final Timestamp getTimestamp(Object obj, Timestamp defValue)
    {
        if (obj instanceof Timestamp) {
            return (Timestamp)obj;
        }
        else if (obj instanceof Date) {
            return new Timestamp(((Date)obj).getTime());
        }
        else if (obj instanceof Long) {
            return new Timestamp(((Long)obj).longValue());
        }
        else if (obj instanceof String) {
            try {
                return Timestamp.valueOf((String)obj);
            }
            catch (Exception e) {
                return defValue;
            }
        }
        else if (obj instanceof SimpleDate) {
            Date date = ((SimpleDate)obj).getDate();
            return new Timestamp(date.getTime());
        }
        else if (obj instanceof SimpleDateTime) {
            Date date = ((SimpleDateTime)obj).getDate();
            return new Timestamp(date.getTime());
        }
        return defValue;
    }

    /**
     * ������ת����ʱ���
     */
    public static final Time getTime(Object obj)
    {
        return getTime(obj, null);
    }

    /**
     * ������ת����ʱ���
     */
    public static final Time getTime(Object obj, Time defValue)
    {
        if (obj instanceof Time) {
            return (Time)obj;
        }
        else if (obj instanceof Date) {
            return new Time(((Date)obj).getTime());
        }
        else if (obj instanceof Long) {
            return new Time(((Long)obj).longValue());
        }
        else if (obj instanceof String) {
            try {
                return Time.valueOf((String)obj);
            }
            catch (Exception e) {
                return defValue;
            }
        }
        else if (obj instanceof SimpleTime) {
            SimpleTime time = (SimpleTime)obj;
            Calendar calendar = Calendar.getInstance();
            time.copyTo(calendar);
            return new Time(calendar.getTime().getTime());
        }
        return defValue;
    }

    //--------------------------------------------------------------------

    // must handle Long, Float, Integer, Float, Short,
    //                  BigDecimal, BigInteger and Byte
    // useful methods:
    // Byte.decode(String)
    // Byte.valueOf(String,int radix)
    // Byte.valueOf(String)
    // Double.valueOf(String)
    // Float.valueOf(String)
    // new Float(String)
    // Integer.valueOf(String,int radix)
    // Integer.valueOf(String)
    // Integer.decode(String)
    // Integer.getInteger(String)
    // Integer.getInteger(String,int val)
    // Integer.getInteger(String,Integer val)
    // new Integer(String)
    // new Double(String)
    // new Byte(String)
    // new Long(String)
    // Long.getLong(String)
    // Long.getLong(String,int)
    // Long.getLong(String,Integer)
    // Long.valueOf(String,int)
    // Long.valueOf(String)
    // new Short(String)
    // Short.decode(String)
    // Short.valueOf(String,int)
    // Short.valueOf(String)
    // new BigDecimal(String)
    // new BigInteger(String)
    // new BigInteger(String,int radix)
    // Possible inputs:
    // plus minus everything. Prolly more. A lot are not separable.

    /**
     *
     * Turns a string value into a java.lang.Number.
     * First, the value is examined for a type qualifier on the end
     * (<code>'f','F','d','D','l','L'</code>).  If it is found, it starts
     * trying to create succissively larger types from the type specified
     * until one is found that can hold the value.
     *
     * If a type specifier is not found, it will check for a decimal point
     * and then try successively larger types from Integer to BigInteger
     * and from Float to BigDecimal.
     *
     * If the string starts with "0x" or "-0x", it will be interpreted as a
     * hexadecimal integer.  Values with leading 0's will not be interpreted
     * as octal.
     *
     * @param val String containing a number
     *            Number created from the string
     * @throws NumberFormatException if the value cannot be converted
     */
    public static final Number getNumber(String val) throws NumberFormatException
    {
        if (val == null) {
            return null;
        }
        if (val.length() == 0) {
            throw new NumberFormatException("\"\" is not a valid number.");
        }
        if (val.startsWith("--")) {
            // this is protection for poorness in java.lang.BigDecimal.
            // it accepts this as a legal value, but it does not appear
            // to be in specification of class. OS X Java parses it to
            // a wrong value.
            return null;
        }
        if (val.startsWith("0x") || val.startsWith("-0x")) {
            return Integer.valueOf(val);
        }
        char lastChar = val.charAt(val.length() - 1);
        String mant;
        String dec;
        String exp;
        int decPos = val.indexOf('.');
        int expPos = val.indexOf('e') + val.indexOf('E') + 1;

        if (decPos > -1) {
            if (expPos > -1) {
                if (expPos < decPos) {
                    throw new NumberFormatException(val + " is not a valid number.");
                }
                dec = val.substring(decPos + 1, expPos);
            }
            else {
                dec = val.substring(decPos + 1);
            }
            mant = val.substring(0, decPos);
        }
        else {
            if (expPos > -1) {
                mant = val.substring(0, expPos);
            }
            else {
                mant = val;
            }
            dec = null;
        }
        if (!Character.isDigit(lastChar)) {
            if (expPos > -1 && expPos < val.length() - 1) {
                exp = val.substring(expPos + 1, val.length() - 1);
            }
            else {
                exp = null;
            }
            //Requesting a specific type..
            String numeric = val.substring(0, val.length() - 1);
            boolean allZeros = isAllZeros(mant) && isAllZeros(exp);
            switch (lastChar) {
                case'l':
                case'L':
                    if (dec == null
                        && exp == null
                        && StringUtil.isDigits(numeric.substring(1))
                        && (numeric.charAt(0) == '-' || Character.isDigit(numeric.charAt(0)))) {
                        try {
                            return Long.valueOf(numeric);
                        }
                        catch (NumberFormatException nfe) {
                            //Too big for a long
                        }
                        return new BigInteger(numeric);
                    }
                    throw new NumberFormatException(val + " is not a valid number.");
                case'f':
                case'F':
                    try {
                        Float f = Float.valueOf(numeric);
                        if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                            //If it's too big for a float or the float value = 0 and the string
                            //has non-zeros in it, then float doens't have the presision we want
                            return f;
                        }

                    }
                    catch (NumberFormatException nfe) {
                    }
                    //Fall through
                case'd':
                case'D':
                    try {
                        Double d = Double.valueOf(numeric);
                        if (!(d.isInfinite() || (d.floatValue() == 0.0D && !allZeros))) {
                            return d;
                        }
                    }
                    catch (NumberFormatException nfe) {
                    }
                    try {
                        return new BigDecimal(numeric);
                    }
                    catch (NumberFormatException e) {
                    }
                    //Fall through
                default:
                    throw new NumberFormatException(val + " is not a valid number.");

            }
        }
        else {
            //User doesn't have a preference on the return type, so let's start
            //small and go from there...
            if (expPos > -1 && expPos < val.length() - 1) {
                exp = val.substring(expPos + 1, val.length());
            }
            else {
                exp = null;
            }
            if (dec == null && exp == null) {
                //Must be an int,long,bigint
                try {
                    return Integer.valueOf(val);
                }
                catch (NumberFormatException nfe) {
                }
                try {
                    return Long.valueOf(val);
                }
                catch (NumberFormatException nfe) {
                }
                return new BigInteger(val);
            }
            else {
                //Must be a float,double,BigDec
                boolean allZeros = isAllZeros(mant) && isAllZeros(exp);
                try {
                    Float f = Float.valueOf(val);
                    if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                        return f;
                    }
                }
                catch (NumberFormatException nfe) {
                }
                try {
                    Double d = Double.valueOf(val);
                    if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
                        return d;
                    }
                }
                catch (NumberFormatException nfe) {
                }

                return new BigDecimal(val);

            }

        }
    }

    /**
     * Utility method for createNumber.  Returns true if s is null
     *
     * @param s the String to check
     *          if it is all zeros or null
     */
    private static boolean isAllZeros(String s)
    {
        if (s == null) {
            return true;
        }
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) != '0') {
                return false;
            }
        }
        return s.length() > 0;
    }

    /**
     * Returns true if the given class is Byte, Short, Integer, Long,
     * Float, Double, BigInteger, or BigDecimal
     */
    public static final boolean isNumberClass(Class clazz)
    {
        return
            clazz == Byte.class ||
            clazz == Byte.TYPE ||
            clazz == Short.class ||
            clazz == Short.TYPE ||
            clazz == Integer.class ||
            clazz == Integer.TYPE ||
            clazz == Long.class ||
            clazz == Long.TYPE ||
            clazz == Float.class ||
            clazz == Float.TYPE ||
            clazz == Double.class ||
            clazz == Double.TYPE ||
            clazz == BigInteger.class ||
            clazz == BigDecimal.class;
    }

    /**
     * Returns true if the given Object is of a floating point type
     */
    public static final boolean isFloatingPointType(Object pObject)
    {
        return
            pObject != null &&
            isFloatingPointType(pObject.getClass());
    }

    /**
     * Returns true if the given class is of a floating point type
     */
    public static final boolean isFloatingPointType(Class pClass)
    {
        return
            pClass == Float.class ||
            pClass == Float.TYPE ||
            pClass == Double.class ||
            pClass == Double.TYPE;
    }

    /**
     * Returns true if the given string might contain a floating point
     * number - i.e., it contains ".", "e", or "E"
     */
    public static final boolean isFloatingPointString(Object pObject)
    {
        if (pObject instanceof String) {
            String str = (String)pObject;
            int len = str.length();
            for (int i = 0; i < len; i++) {
                char ch = str.charAt(i);
                if (ch == '.' ||
                    ch == 'e' ||
                    ch == 'E') {
                    return true;
                }
            }
            return false;
        }
        else {
            return false;
        }
    }

    /**
     * Returns true if the given Object is of an integer type
     */
    public static final boolean isIntegerType(Object pObject)
    {
        return
            pObject != null &&
            isIntegerType(pObject.getClass());
    }

    /**
     * Returns true if the given class is of an integer type
     */
    public static final boolean isIntegerType(Class pClass)
    {
        return
            pClass == Byte.class ||
            pClass == Byte.TYPE ||
            pClass == Short.class ||
            pClass == Short.TYPE ||
            pClass == Character.class ||
            pClass == Character.TYPE ||
            pClass == Integer.class ||
            pClass == Integer.TYPE ||
            pClass == Long.class ||
            pClass == Long.TYPE;
    }

    //-------------------------------------

    /**
     * Returns true if the given object is BigInteger.
     *
     * @param pObject - Object to evaluate
     *                - true if the given object is BigInteger
     */
    public static final boolean isBigInteger(Object pObject)
    {
        return
            pObject != null && pObject instanceof BigInteger;
    }

    /**
     * Returns true if the given object is BigDecimal.
     *
     * @param pObject - Object to evaluate
     *                - true if the given object is BigDecimal
     */
    public static final boolean isBigDecimal(Object pObject)
    {
        return
            pObject != null && pObject instanceof BigDecimal;
    }

    public static final String TYPE_STR = "str";
    public static final String TYPE_INT = "int";
    public static final String TYPE_BOOL = "bool";
    public static final String TYPE_BYTE = "byte";
    public static final String TYPE_CHAR = "char";
    public static final String TYPE_STRING = "string";
    public static final String TYPE_INTEGER = "integer";
    public static final String TYPE_BOOLEAN = "boolean";
    public static final String TYPE_CHARACTER = "character";
    public static final String TYPE_LONG = "long";
    public static final String TYPE_FLOAT = "float";
    public static final String TYPE_DOUBLE = "double";
    public static final String TYPE_SHORT = "short";

    //����
    public static final String TYPE_DATE = "date";
    public static final String TYPE_TIME = "time";
    public static final String TYPE_TIMESTAMP = "timestamp";
    public static final String TYPE_SIMPLE_DATE = "simple_date";
    public static final String TYPE_SIMPLE_TIME = "simple_time";
    public static final String TYPE_SIMPLE_DATE_TIME = "simple_date_time";

    //����
    public static final String TYPE_INTS = "ints";
    public static final String TYPE_INT_ARRAY = "int_array";
    public static final String TYPE_INT_JAVA_ARRAY = "int[]";
    public static final String TYPE_INTEGER_ARRAY = "integer_array";
    public static final String TYPE_BYTES = "bytes";
    public static final String TYPE_BYTE_JAVA_ARRAY = "byte[]";
    public static final String TYPE_BYTE_ARRAY = "byte_array";
    public static final String TYPE_CHARS = "chars";
    public static final String TYPE_CHAR_JAVA_ARRAY = "char[]";
    public static final String TYPE_CHAR_ARRAY = "char_array";
    public static final String TYPE_SHORT_ARRAY = "short_array";
    public static final String TYPE_SHORT_JAVA_ARRAY = "short[]";
    public static final String TYPE_LONG_ARRAY = "long_array";
    public static final String TYPE_LONG_JAVA_ARRAY = "long[]";
    public static final String TYPE_FLOAT_ARRAY = "float_array";
    public static final String TYPE_FLOAT_JAVA_ARRAY = "float[]";
    public static final String TYPE_DOUBLE_ARRAY = "double_array";
    public static final String TYPE_DOUBLE_JAVA_ARRAY = "double[]";
    public static final String TYPE_STRS = "strs";
    public static final String TYPE_STRINGS = "strings";
    public static final String TYPE_STRING_JAVA_ARRAY = "String[]";
    public static final String TYPE_STRING_ARRAY = "string_array";

    public static final String TYPE_BOOL_ARRAY = "bool_array";
    public static final String TYPE_BOOLEAN_JAVA_ARRAY = "boolean[]";
    public static final String TYPE_BOOLEAN_ARRAY = "boolean_array";

    public static final String TYPE_OBJECT = "object";
    public static final String TYPE_OBJECTS = "objects";
    public static final String TYPE_OBJECT_JAVA_ARRAY = "Object[]";
    public static final String TYPE_OBJECT_ARRAY = "object_array";

    //InetAddress
    public static final String TYPE_INET_ADDRESS = "inet_address";

    /**
     * ���BeanKey��ȡBeanData�е�BeanValue
     *
     * @param beanData
     * @param beanKey
     * @return
     */
    public static Object getBeanValue(Map<String, Object> beanData, String beanKey)
    {
        Object value = beanData.get(beanKey);
        if (value instanceof Object[]) {
            return ((Object[])value)[0];
        }
        return value;
    }

}
