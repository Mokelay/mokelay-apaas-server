package com.mokelay.base.util;

import com.mokelay.base.util.io.CharBufferPool;
import com.mokelay.base.util.text.NumberFormat;
import com.mokelay.base.util.text.SimpleDateTime;

import java.nio.CharBuffer;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * StringUtil
 * @author system
 * @version 1.00 2005-2-5 21:01:09
 */
public class StringUtil
{
    public static final String NULL = "null";
    public static final char[] NULL_CHARS = NULL.toCharArray();
    /**
     */
    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    /**
     */
    public static final String EMPTY_STRING = "";

    /**
     * Check if the string value is valid
     *
     * @param value
     */
    public static final boolean isValid(String value)
    {
        return value != null && value.length() > 0;
    }

    public static final boolean isValidAfterTrim(String value)
    {
        value = value.trim();
        return value != null && value.length() > 0;
    }

    public static final boolean isValidFileName(String value)
    {
    	//regular expression to filter ../
    	String reg = "(\\.\\./)";
    	Pattern p = Pattern.compile(reg,Pattern.CASE_INSENSITIVE);
    	Matcher m = p.matcher(value);
    	if(m.find()){
    		return false;
    	}
    	else{
    		return true;
    	}
    }

    /**
     * Check if the string value is invalid
     *
     * @param value
     */
    public static final boolean isInvalid(String value)
    {
        return value == null || value.length() == 0;
    }

    /**
     * Return string value from an object
     * (String String[0] object.toString)
     *
     * @param object
     */
    public static final String getString(Object object)
    {
        return DataUtil.getString(object, null);
    }

    /**
     *
     */
    public static final String getString(Object object, String defValue)
    {
        return DataUtil.getString(object, defValue);
    }

    /**
     */
    public static final String[] getStringArray(Object object)
    {
        return DataUtil.getStringArray(object);
    }

    /**
     * 获取Email的帐号
     *
     * @param email
     * @return
     */
    public static final String getEmailAccount(String email)
    {
        if (StringUtil.isValid(email)) {
            int pos = email.indexOf("@");
            if (pos >= 0) {
                return email.substring(0, pos);
            }
            else {
                return email;
            }
        }
        return null;
    }

    /**
     * Email Domain
     * iamcarlchen@gmail.com
     *
     * @param email
     * @return
     */
    public static final String getEmailDomain(String email)
    {
        if (isValidEmail(email)) {
            int pos = email.indexOf("@");
            if (pos >= 0) {
                return email.substring(pos + 1);
            }
        }
        return null;
    }

    /**
     * 是否是合法的Email
     *
     * @param email
     * @return
     */
    public static final boolean isValidEmail(String email)
    {
        if (StringUtil.isValid(email)) {
            int pos1 = email.indexOf("@");
            int pos2 = email.lastIndexOf(".");

            return pos1 > 0 && pos2 > pos1;
        }
        return false;
    }

    /**
     */
    public static final boolean equals(String str1, String str2)
    {
        if (str1 == null) {
            return str2 == null;
        }

        return str1.equals(str2);
    }

    /**
     */
    public static final boolean equalsIgnoreCase(String str1,
                                                 String str2)
    {
        if (str1 == null) {
            return str2 == null;
        }
        return str1.equalsIgnoreCase(str2);
    }

    /**
     */
    public static final int hashCode(String str)
    {
        if (str == null) {
            return 0;
        }
        return str.hashCode();
    }

    /**
     */
    public static final int hashCodeIgnoreCase(String str)
    {
        if (str == null) {
            return 0;
        }
        int len = str.length();
        int hash = 0;
        for (int i = 0; i < len; i++) {
            hash = 31 * hash + Character.toLowerCase(str.charAt(i));
        }
        return hash;
    }

    /**
     */
    public static final String[] toStringArray(String src, char sep)
    {
        if (src == null) {
            return null;
        }
        int length = src.length();
        if (length == 0) {
            return EMPTY_STRING_ARRAY;
        }

        int count = countChar(src, sep);
        String[] array = new String[count + 1];
        int begin = 0;
        int end = 0;
        for (int i = 0; i <= count; i++) {
            end = src.indexOf(sep, begin);
            if (end == -1) {
                array[i] = begin == length ? "" : src.substring(begin);
                break;
            }
            else {
                array[i] = src.substring(begin, end);
                begin = end + 1;
            }
        }
        return array;
    }

    /**
     */
    public static final String[] toStringArray(String src, char sep, boolean ignore)
    {
        String[] array = toStringArray(src, sep);
        if (ignore && ArrayUtil.isValid(array)) {
            int j = 0;
            for (int i = 0; i < array.length; i++) {
                if (StringUtil.isValid(array[i])) {
                    array[j++] = array[i];
                }
            }
            if (j < array.length) {
                String[] newArray = new String[j];
                System.arraycopy(array, 0, newArray, 0, j);
                array = newArray;
            }
        }
        return array;
    }

    /**
     */
    public static final String toString(String[] array, int off,
                                        int len, char sep)
    {
        return toString((Object[])array, off, len, sep);
    }

    /**
     */
    public static final String toString(Object[] array, char sep)
    {
        if (array == null) {
            return null;
        }
        return toString(array, 0, array.length, sep);
    }

    /**
     */
    public static final String toString(Object[] array, int off,
                                        int len, char sep)
    {
        if (array == null) {
            return null;
        }

        if (off < 0) {
            throw new ArrayIndexOutOfBoundsException("Off=" + off);
        }
        if (len < 0) {
            throw new ArrayIndexOutOfBoundsException("Len=" + len);
        }

        int end = off + len;
        if (end > array.length) {
            throw new ArrayIndexOutOfBoundsException("Invalid off=" + off + " or len=" + len
                                                     + " array length=" + array.length);
        }

        if (len == 0) {
            return EMPTY_STRING;
        }
        else if (len == 1) {
            return String.valueOf(array[off]);
        }
        else {
            StringBuilder sb = new StringBuilder();
            sb.append(array[off]);
            for (int i = off + 1; i < end; i++) {
                sb.append(sep);
                sb.append(array[i]);
            }
            return sb.toString();
        }
    }

    public static final String toString(String[] strs, char sep)
    {
        if (strs == null) {
            return null;
        }
        return toString(strs, 0, strs.length, sep);
    }

    /**
     */
    public static final String toString(boolean[] array, int off,
                                        int len, char sep)
    {
        if (array == null) {
            return null;
        }

        if (off < 0) {
            throw new ArrayIndexOutOfBoundsException("Off=" + off);
        }
        if (len < 0) {
            throw new ArrayIndexOutOfBoundsException("Len=" + len);
        }

        int end = off + len;
        if (end > array.length) {
            throw new ArrayIndexOutOfBoundsException("Invalid off=" + off + " or len=" + len
                                                     + " array length=" + array.length);
        }

        if (len == 0) {
            return EMPTY_STRING;
        }
        else if (len == 1) {
            return String.valueOf(array[off]);
        }
        else {
            StringBuilder sb = new StringBuilder();
            sb.append(array[off]);
            for (int i = off + 1; i < end; i++) {
                sb.append(sep);
                sb.append(array[i]);
            }
            return sb.toString();
        }
    }

    public static final String toString(boolean[] array, char sep)
    {
        if (array == null) {
            return null;
        }
        return toString(array, 0, array.length, sep);
    }

    /**
     */
    public static final String toString(byte[] array, int off,
                                        int len, char sep)
    {
        if (array == null) {
            return null;
        }

        if (off < 0) {
            throw new ArrayIndexOutOfBoundsException("Off=" + off);
        }
        if (len < 0) {
            throw new ArrayIndexOutOfBoundsException("Len=" + len);
        }
        int end = off + len;
        if (end > array.length) {
            throw new ArrayIndexOutOfBoundsException("Invalid off=" + off + " or len=" + len
                                                     + " array length=" + array.length);
        }

        if (len == 0) {
            return EMPTY_STRING;
        }
        else if (len == 1) {
            return String.valueOf(array[off]);
        }
        else {
            StringBuilder sb = new StringBuilder();
            sb.append(array[off]);
            for (int i = off + 1; i < end; i++) {
                sb.append(sep);
                sb.append(array[i]);
            }
            return sb.toString();
        }
    }

    public static final String toString(byte[] array, char sep)
    {
        if (array == null) {
            return null;
        }
        return toString(array, 0, array.length, sep);
    }

    /**
     */
    public static final String toString(char[] array, int off,
                                        int len, char sep)
    {
        if (array == null) {
            return null;
        }

        if (off < 0) {
            throw new ArrayIndexOutOfBoundsException("Off=" + off);
        }
        if (len < 0) {
            throw new ArrayIndexOutOfBoundsException("Len=" + len);
        }
        int end = off + len;
        if (end > array.length) {
            throw new ArrayIndexOutOfBoundsException("Invalid off=" + off + " or len=" + len
                                                     + " array length=" + array.length);
        }

        if (len == 0) {
            return EMPTY_STRING;
        }
        else if (len == 1) {
            return String.valueOf(array[off]);
        }
        else {
            StringBuilder sb = new StringBuilder();
            sb.append(array[off]);
            for (int i = off + 1; i < end; i++) {
                sb.append(sep);
                sb.append(array[i]);
            }
            return sb.toString();
        }
    }

    public static final String toString(char[] array, char sep)
    {
        if (array == null) {
            return null;
        }
        return toString(array, 0, array.length, sep);
    }

    /**
     */
    public static final String toString(int[] array, int off,
                                        int len, char sep)
    {
        if (array == null) {
            return null;
        }

        if (off < 0) {
            throw new ArrayIndexOutOfBoundsException("Off=" + off);
        }
        if (len < 0) {
            throw new ArrayIndexOutOfBoundsException("Len=" + len);
        }
        int end = off + len;
        if (end > array.length) {
            throw new ArrayIndexOutOfBoundsException("Invalid off=" + off + " or len=" + len
                                                     + " array length=" + array.length);
        }

        if (len == 0) {
            return EMPTY_STRING;
        }
        else if (len == 1) {
            return String.valueOf(array[off]);
        }
        else {
            StringBuilder sb = new StringBuilder();
            sb.append(array[off]);
            for (int i = off + 1; i < end; i++) {
                sb.append(sep);
                sb.append(array[i]);
            }
            return sb.toString();
        }
    }

    public static final String toString(int[] array, char sep)
    {
        if (array == null) {
            return null;
        }
        return toString(array, 0, array.length, sep);
    }

    /**
     */
    public static final String toString(long[] array, int off,
                                        int len, char sep)
    {
        if (array == null) {
            return null;
        }

        if (off < 0) {
            throw new ArrayIndexOutOfBoundsException("Off=" + off);
        }
        if (len < 0) {
            throw new ArrayIndexOutOfBoundsException("Len=" + len);
        }
        int end = off + len;
        if (end > array.length) {
            throw new ArrayIndexOutOfBoundsException("Invalid off=" + off + " or len=" + len
                                                     + " array length=" + array.length);
        }

        if (len == 0) {
            return EMPTY_STRING;
        }
        else if (len == 1) {
            return String.valueOf(array[off]);
        }
        else {
            StringBuilder sb = new StringBuilder();
            sb.append(array[off]);
            for (int i = off + 1; i < end; i++) {
                sb.append(sep);
                sb.append(array[i]);
            }
            return sb.toString();
        }
    }

    public static final String toString(long[] array, char sep)
    {
        if (array == null) {
            return null;
        }
        return toString(array, 0, array.length, sep);
    }

    /**
     */
    public static final String toString(float[] array, int off,
                                        int len, char sep, int fraction)
    {
        if (array == null) {
            return null;
        }

        if (off < 0) {
            throw new ArrayIndexOutOfBoundsException("Off=" + off);
        }
        if (len < 0) {
            throw new ArrayIndexOutOfBoundsException("Len=" + len);
        }
        int end = off + len;
        if (end > array.length) {
            throw new ArrayIndexOutOfBoundsException("Invalid off=" + off + " or len=" + len
                                                     + " array length=" + array.length);
        }

        if (len == 0) {
            return EMPTY_STRING;
        }
        else if (len == 1) {
            return NumberFormat.format(array[off], fraction);
        }
        else {
            StringBuilder sb = new StringBuilder();
            NumberFormat.format(sb, array[off], fraction);
            for (int i = off + 1; i < end; i++) {
                sb.append(sep);
                NumberFormat.format(sb, array[i], fraction);
            }
            return sb.toString();
        }
    }

    public static final String toString(float[] array, char sep, int fraction)
    {
        if (array == null) {
            return null;
        }
        return toString(array, 0, array.length, sep, fraction);
    }

    /**
     */
    public static final String toString(double[] array, int off,
                                        int len, char sep, int fraction)
    {
        if (array == null) {
            return null;
        }

        if (off < 0) {
            throw new ArrayIndexOutOfBoundsException("Off=" + off);
        }
        if (len < 0) {
            throw new ArrayIndexOutOfBoundsException("Len=" + len);
        }
        int end = off + len;
        if (end > array.length) {
            throw new ArrayIndexOutOfBoundsException("Invalid off=" + off + " or len=" + len
                                                     + " array length=" + array.length);
        }

        if (len == 0) {
            return EMPTY_STRING;
        }
        else if (len == 1) {
            return NumberFormat.format(array[off], fraction);
        }
        else {
            StringBuilder sb = new StringBuilder();
            NumberFormat.format(sb, array[off], fraction);
            for (int i = off + 1; i < end; i++) {
                sb.append(sep);
                NumberFormat.format(sb, array[i], fraction);
            }
            return sb.toString();
        }
    }

    public static final String toString(double[] array, char sep, int fraction)
    {
        if (array == null) {
            return null;
        }
        return toString(array, 0, array.length, sep, fraction);
    }

    public static final String toString(byte[] bytes, int off, int len, String encoding)
    {
        String result = null;
        if (bytes == null) {
            return null;
        }
        else if (encoding == null) {
            result = new String(bytes, off, len);
        }
        else {
            try {
                result = new String(bytes, off, len, encoding);
            }
            catch (Exception e) {
                result = new String(bytes, off, len);
            }
        }
        return result;
    }

    public static final String toString(byte[] bytes, String encoding)
    {
        if (bytes == null) {
            return null;
        }
        return toString(bytes, 0, bytes.length, encoding);
    }

    public static final String toString(byte[] bytes)
    {
        return toString(bytes, null);
    }

    public static final byte[] toBytes(String str)
    {
        return toBytes(str, null);
    }

    public static final byte[] toBytes(String str, String encoding)
    {
        byte[] bytes = null;
        if (str == null) {
            return null;
        }
        else if (str.length() == 0) {
            return DataUtil.EMPTY_BYTE_ARRAY;
        }
        else if (encoding == null) {
            bytes = str.getBytes();
        }
        else {
            try {
                bytes = str.getBytes(encoding);
            }
            catch (Exception e) {
                bytes = str.getBytes();
            }
        }
        return bytes;
    }

    /**
     */
    public static final int[] toIntArray(String src, char sep)
    {
        if (src == null) {
            return null;
        }
        int length = src.length();
        if (length == 0) {
            return new int[]{0};
        }

        int count = countChar(src, sep);
        int[] array = new int[count + 1];
        int i = 0;  //index of array
//        int begin = 0;
        int value = 0;
        boolean neg = false;
        int index = 0; //index of src
        char c = 0;
        while (index < length) {
            c = src.charAt(index);
            if (CharUtil.isDigit(c)) {
                value = 10 * value + (c - '0');
            }
            else if (c == sep) {
                array[i++] = neg ? -value : value;
                value = 0;
                neg = false;
            }
            else if (c == '-') {
                if (value == 0) {
                    neg = true;
                }
                else {
                    //��ʽ���󣬷���
                    return null;
                }
            }
            else if (c == ' ' || c == '\t') {
                index++;
                continue;
            }
            else {
                return null;
            }
            index++;
        }
        array[i] = neg ? -value : value;
        return array;
    }

    /**
     */
    public static String addString(String target, String add, char sep)
    {
        if (isValid(add)) {
            target += sep + add;
            return target;
        }
        else {
            return add;
        }
    }

    /**
     */
    public static boolean checkIfContainerString(String target, String checkStr)
    {
        return target != null && checkStr != null && target.indexOf(checkStr) >= 0;
    }

    /**
     * Return the count of the char in the String
     * if src == null return -1;
     * if src.length() == 0 return 0;
     */
    public static final int countChar(String src, char c)
    {
        if (src == null) {
            return -1;
        }
        int length = src.length();
        if (length == 0) {
            return 0;
        }

        int count = 0;
        int ch = 0;
        for (int i = 0; i < length; i++) {
            ch = src.charAt(i);
            if (ch == c) {
                count++;
            }
        }
        return count;
    }

    /**
     * use specific string to replace specific char
     */
    public static final String replace(String str, char src, String dst)
    {
        if (isInvalid(str)) {
            return str;
        }
        if (dst == null) {
            throw new IllegalArgumentException("Null destination string");
        }

        int strLen = str.length();
        int size = strLen + (strLen / 20) * dst.length() + 20;

        StringBuilder sb = new StringBuilder(size);
        int offset = 0;
        int index = 0;
        while (offset < strLen && (index = str.indexOf((int)src, offset)) != -1) {
            sb.append(str.substring(offset, index));
            sb.append(dst);
            offset = ++index;
        }
        if (offset < strLen) {
            sb.append(str.substring(offset));
        }
        return sb.toString();
    }

    public static final String replace(String str, String src, String dst)
    {
        if (isInvalid(str)) {
            return str;
        }
        if (isInvalid(src)) {
            throw new IllegalArgumentException("Invalid source string:" + src);
        }
        if (dst == null) {
            throw new IllegalArgumentException("Invalid destination string:" + dst);
        }

        int srcLen = src.length();
        if (srcLen == 1) {
            return replace(str, src.charAt(0), dst);
        }


        int strLen = str.length();
        StringBuilder sb = new StringBuilder(strLen);
        int offset = 0;
        int index = 0;

        while (offset < strLen && (index = str.indexOf(src, offset)) != -1) {
            sb.append(str.substring(offset, index));
            sb.append(dst);
            offset = index + srcLen;
        }
        if (offset < strLen) {
            sb.append(str.substring(offset));
        }
        return sb.toString();
    }

    /**
     */
    public static final String replace(String str, char[] src, String[] dst)
    {
        if (isInvalid(str)) {
            return str;
        }
        if (ArrayUtil.isInvalid(src)) {
            throw new IllegalArgumentException("Invalid source string:" + src);
        }
        if (dst == null) {
            throw new IllegalArgumentException("Invalid destination string:" + dst);
        }
        if (src.length != dst.length) {
            throw new IllegalArgumentException("Not same size of two arrays, source:"
                                               + src + " destination:" + dst);
        }

        if (src.length == 1) {
            return replace(str, src[0], dst[0]);
        }

        int length = str.length();
        StringBuilder sb = new StringBuilder(length + 64);

        for (int i = 0; i < length; i++) {
            char c = str.charAt(i);
            int matched = -1;
            for (int j = 0; j < src.length; j++) {
                if (c == src[j]) {
                    matched = j;
                    break;
                }
            }
            if (matched >= 0) {
                sb.append(dst[matched]);
            }
            else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     */
    public static final boolean startsWith(String str, char ch)
    {
        return isValid(str) && str.charAt(0) == ch;
    }

    /**
     */
    public static final boolean endsWith(String str, char ch)
    {
        return isValid(str) && str.charAt(str.length() - 1) == ch;
    }

    private static final Locale DEFAULT = Locale.getDefault();

    /**
     */
    public static final String toUpperFirstCase(String str)
    {
        if (isValid(str)) {
            int len = str.length();
            StringBuffer s = new StringBuffer();
            s.append(str.substring(0, 1).toUpperCase());
            if (len >= 2) {
                s.append(str.substring(1, len));
            }
            return s.toString();
        }
        else {
            return str;
        }
    }

    /**
     */
    public static final String toUpperCase(String str)
    {
        return toUpperCase(str, DEFAULT);
    }

    /**
     */
    public static final String toUpperCase(String str, Locale locale)
    {
        if (isInvalid(str)) {
            return str;
        }
        int len = str.length();
        if (len > CharBufferPool.DEFAULT_BUFFER_SIZE) {
            return str.toUpperCase(locale);
        }
        else {
            return toUpperCase(str, len, locale);
        }
    }


    private static final String toUpperCase(String str, int len, Locale locale)
    {
        CharBuffer buffer = CharBufferPool.allocate();
        char[] val = buffer.array();
        str.getChars(0, len, val, 0);
        int firstLower;
        /* Now check if there are any characters that need changing. */
        scan:
        {
            for (firstLower = 0; firstLower < len; firstLower++) {
                char c = val[firstLower];
                if (Character.isLowerCase(c)) {
                    break scan;
                }
            }
            CharBufferPool.recycle(buffer);
            return str;
        }

        /* val grows, so i+resultOffset * is the write location in val */
        if (locale.getLanguage().equals("tr")) {
            // special loop for Turkey
            for (int i = firstLower; i < len; ++i) {
                char ch = val[i];
                if (ch == 'i') {
                    val[i] = '\u0130';  // dotted cap i
                    continue;
                }
                if (ch == '\u0131') {                   // dotless i
                    val[i] = 'I';       // cap I
                    continue;
                }
                val[i] = Character.toUpperCase(ch);
            }
        }
        else {
            // normal, fast loop
            for (int i = firstLower; i < len; ++i) {
                char ch = val[i];
                val[i] = Character.toUpperCase(ch);
            }
        }
        String newStr = new String(val, 0, len);
        CharBufferPool.recycle(buffer);
        return newStr;
    }

    /**
     */
    public static final char[] toCharArray(String text, boolean caseSensitive)
    {
        if (text == null) {
            return DataUtil.EMPTY_CHAR_ARRAY;
        }
        if (caseSensitive) {
            return text.toCharArray();
        }
        else {
            return text.toUpperCase().toCharArray();
        }
    }

    /**
     */
    public static final String toLowerCase(String str)
    {
        return toLowerCase(str, DEFAULT);
    }

    /**
     */
    public static final String toLowerCase(String str, Locale locale)
    {
        if (isInvalid(str)) {
            return str;
        }
        int len = str.length();
        if (len > CharBufferPool.DEFAULT_BUFFER_SIZE) {
            return str.toLowerCase(locale);
        }
        else {
            return toLowerCase(str, len, locale);
        }
    }

    /**
     * @param str
     * @param len
     * @param locale
     * @return Сд
     */
    private static final String toLowerCase(String str, int len, Locale locale)
    {
        CharBuffer buffer = CharBufferPool.allocate();
        char[] val = buffer.array();
        str.getChars(0, len, val, 0);
        int firstUpper;
        /* Now check if there are any characters that need changing. */
        scan:
        {
            for (firstUpper = 0; firstUpper < len; firstUpper++) {
                char c = val[firstUpper];
                if (Character.isUpperCase(c)) {
                    break scan;
                }
            }
            CharBufferPool.recycle(buffer);
            return str;
        }

        if (locale.getLanguage().equals("tr")) {
            // special loop for Turkey
            for (int i = firstUpper; i < len; ++i) {
                char ch = val[i];
                if (ch == 'I') {
                    val[i] = '\u0131'; // dotless small i
                    continue;
                }
                if (ch == '\u0130') {       // dotted I
                    val[i] = 'i';      // dotted i
                    continue;
                }
                val[i] = Character.toLowerCase(ch);
            }
        }
        else {
            // normal, fast loop
            for (int i = firstUpper; i < len; ++i) {
                val[i] = Character.toLowerCase(val[i]);
            }
        }
        String newStr = new String(val, 0, len);
        CharBufferPool.recycle(buffer);
        return newStr;
    }


    /**
     * Checks whether the String contains only digit characters.
     * Null and blank string will return false.
     *
     * @param str the string to check
     * @return boolean contains only unicode numeric
     */
    public static final boolean isDigits(String str)
    {
        if (isInvalid(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     */
    public static final String[] toStringArray(List<String> list)
    {
        if (list == null) {
            return null;
        }
        int size = list.size();
        if (size == 0) {
            return EMPTY_STRING_ARRAY;
        }
        String[] array = new String[size];
        list.toArray(array);
        return array;
    }

    /**
     */
    public static final boolean isISO88591(String str)
    {
        if (isInvalid(str)) {
            return true;
        }
        for (int i = 0, len = str.length(); i < len; i++) {
            if (!isISO88591(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     */
    public static final boolean isISO88591(char[] chars)
    {
        if (ArrayUtil.isInvalid(chars)) {
            return true;
        }
        for (int i = 0, len = chars.length; i < len; i++) {
            if (!isISO88591(chars[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     */
    public static final boolean isISO88591(char ch)
    {
        return ch <= '\u00FF';
    }

    /**
     */
    public static final boolean isAscii(String str)
    {
        if (isInvalid(str)) {
            return true;
        }

        for (int i = 0, len = str.length(); i < len; i++) {
            if (str.charAt(i) >= 0x80) {
                return false;
            }
        }
        return true;
    }

    /**
     */
    public static final boolean isAscii(char[] chars)
    {
        if (ArrayUtil.isInvalid(chars)) {
            return true;
        }
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] >= 0x80) {
                return false;
            }
        }
        return true;
    }


    /**
     */
    public static final String convert(String string,
                                       String from,
                                       String to)
    {
        if (isInvalid(string)) {
            return string;
        }
        if (isInvalid(from)) {
            from = Charset.getFileEncoding();
        }
        if (isInvalid(to)) {
            to = Charset.getFileEncoding();
        }
        try {
            byte[] bytes = null;
            if (isInvalid(from)) {
                bytes = string.getBytes();
            }
            else {
                bytes = string.getBytes(from);
            }

            String str = null;
            if (isInvalid(to)) {
                str = new String(bytes);
            }
            else {
                str = new String(bytes, to);
            }

            return str;
        }
        catch (Exception e) {
            return string;
        }
    }

    /**
     * ��ȡ�ַ�
     *
     * @param target
     * @param cutSize
     * @return
     */
    public static final String intercept(String target, int cutSize)
    {
        if (isValid(target)) {
            int len = target.length();
            if (len >= cutSize) {
                return target.substring(0, cutSize);
            }
            else {
                return target;
            }
        }
        return target;
    }

    /**
     * Get Current Timestao
     *
     * @return
     */
    public static final String getCurrentTimestamp()
    {
        return getCurrentTimestamp(null, null);
    }

    /**
     */
    public static final String getCurrentTimestamp(String prefix, String suffix)
    {
        StringBuffer s = new StringBuffer();
        if (isValid(prefix)) {
            s.append(prefix);
        }

        SimpleDateTime dt = new SimpleDateTime(System.currentTimeMillis());

        s.append(dt.getYear());
        String month = String.valueOf(dt.getMonth());
        if (month.length() == 1) {
            month = "0" + month;
        }
        s.append(month);
        String day = String.valueOf(dt.getDay());
        if (day.length() == 1) {
            day = "0" + day;
        }
        s.append(day);

        s.append(dt.getHour());
        s.append(dt.getMinute());
        s.append(dt.getSecond());

        if (isValid(suffix)) {
            s.append(suffix);
        }
        return s.toString();
    }

    /**
     * Get Timestamp String
     *
     * @param prefix
     * @param suffix
     * @return
     */
    public static String buildTimestampString(String prefix, String suffix)
    {
        StringBuilder s = new StringBuilder();
        s.append(prefix);
        s.append(System.currentTimeMillis());
        s.append(suffix != null ? suffix : "");
        return s.toString();
    }

    /**
     * Build Data String
     *
     * @return
     */
    public static String buildDateString()
    {
        SimpleDateTime dt = new SimpleDateTime(System.currentTimeMillis());

        StringBuilder s = new StringBuilder();

        s.append(dt.getYear());
        String month = String.valueOf(dt.getMonth());
        if (month.length() == 1) {
            month = "0" + month;
        }
        s.append(month);
        String day = String.valueOf(dt.getDay());
        if (day.length() == 1) {
            day = "0" + day;
        }
        s.append(day);

        return s.toString();
    }

    /**
     * @param value
     * @return
     * @author scott.gao
     */
    private static String decode(String value)
    {
        //ASCII替换
        value = value.replace("", "");
        value = value.replace("\001", "");
        value = value.replace("\002", "");
        value = value.replace("\003", "");
        value = value.replace("\004", "");
        value = value.replace("\005", "");
        value = value.replace("\006", "");
        value = value.replace("\007", "");
        value = value.replace("\b", "");
        value = value.replace("\t", "");
        value = value.replace("\n", "");
        value = value.replace("\013", "");
        value = value.replace("\f", "");
        value = value.replace("\r", "");
        value = value.replace("\016", "");
        value = value.replace("\017", "");
        value = value.replace("\020", "");
        value = value.replace("\021", "");
        value = value.replace("\022", "");
        value = value.replace("\023", "");
        value = value.replace("\024", "");
        value = value.replace("\025", "");
        value = value.replace("\026", "");
        value = value.replace("\027", "");
        value = value.replace("\030", "");
        value = value.replace("\031", "");
        value = value.replace("\032", "");
        value = value.replace("\033", "");
        value = value.replace("\034", "");
        value = value.replace("\035", "");
        value = value.replace("\036", "");
        value = value.replace("\037", "");
        return value;
    }

    /**
     * @param validateContent
     * @return
     */
    public static String xssFilter(String validateContent )
    {
    	String finalContent = null;
    	validateContent = decode(validateContent);
    	//正则表达式过滤
    	String reg="<script.*?>.*?</script>";
        String regsrc="script|javascript|mocha|eval|vbscript|livescript|expression|embed|object" +
        		"|layer|style|meta|iframe|frame|link|import|xml";
        Pattern p = Pattern.compile(reg,Pattern.CASE_INSENSITIVE);
        Pattern psrc = Pattern.compile(regsrc,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(validateContent);
        String tmpContent = m.replaceAll("");
        Matcher msrc = psrc.matcher(tmpContent);
        finalContent = msrc.replaceAll("");
    	return finalContent;
    }
}
