package com.greatbee.base.util;

import com.greatbee.base.util.lang.Octet;

/**
 * �ַ���صĹ�����
 *
 * @author system
 * @version 1.00 2005-2-7 13:43:11
 */
public class CharUtil
{
    public static final byte BYTE_SPACE = ' ';
    public static final char CHAR_SPACE = ' ';
    public static final byte BYTE_DOT = '.';
    public static final char CHAR_DOT = '.';
    public static final byte BYTE_CR = '\r';
    public static final char CHAR_CR = '\r';
    public static final byte BYTE_LF = '\n';
    public static final char CHAR_LF = '\n';

    public static final byte BYTE_ZERO = '0';
    public static final char CHAR_ZERO = '0';
    public static final byte BYTE_ONE = '1';
    public static final char CHAR_ONE = '1';
    public static final byte BYTE_TWO = '2';
    public static final char CHAR_TWO = '2';
    public static final byte BYTE_THREE = '3';
    public static final char CHAR_THREE = '3';
    public static final byte BYTE_FOUR = '4';
    public static final char CHAR_FOUR = '4';
    public static final byte BYTE_FIVE = '5';
    public static final char CHAR_FIVE = '5';
    public static final byte BYTE_SIX = '6';
    public static final char CHAR_SIX = '6';
    public static final byte BYTE_SEVEN = '7';
    public static final char CHAR_SEVEN = '7';
    public static final byte BYTE_EIGHT = '8';
    public static final char CHAR_EIGHT = '8';
    public static final byte BYTE_NINE = '9';
    public static final char CHAR_NINE = '9';

    /**
     * Returns the upper case equivalent of the specified ASCII character.
     */
    public static final int toUpper(int c)
    {
        return Octet.toUpper(c);
    }

    /**
     * Returns the lower case equivalent of the specified ASCII character.
     */
    public static final int toLower(int c)
    {
        return Octet.toLower(c);
    }

    /**
     * Returns true if the specified ASCII character is upper case.
     */
    public static final boolean isUpper(int c)
    {
        return Octet.isUpper(c);
    }

    /**
     * Returns true if the specified ASCII character is lower case.
     */
    public static final boolean isLower(int c)
    {
        return Octet.isLower(c);
    }

    /**
     * Returns true if the specified ASCII character is white space.
     */
    public static final boolean isWhite(int c)
    {
        return Octet.isWhite(c);
    }


    /**
     * Convert the bytes within the specified range of the given byte
     * array into a String. The range extends from <code>start</code>
     * till, but not including <code>end</code>. <p>
     */
    public static final String toString(byte[] bytes, int start, int end)
    {
        int len = end - start;
        StringBuilder sb = new StringBuilder(len);
        int ch = 0;
        for (int i = start; i < end; i++) {
            ch = (int)bytes[i] & 0xFF;
            sb.append((char)ch);
        }
        return sb.toString();
    }

    /**
     * Convert the bytes within the specified range of the given byte
     * array into a String. The range extends from <code>start</code>
     * till, but not including <code>end</code>. <p>
     */
    public static final String toString(char[] chars, int start, int end)
    {
        return new String(chars, start, end - start);
    }

    /**
     * Convert the bytes within the specified range of the given byte
     * array into a signed integer . The range extends from
     * <code>start</code> till, but not including <code>end</code>. <p>
     */
    public static final int parseInt(byte[] b)
        throws NumberFormatException
    {
        return parseInt(b, 0, b.length);
    }

    /**
     * Convert the bytes within the specified range of the given byte
     * array into a signed integer . The range extends from
     * <code>start</code> till, but not including <code>end</code>. <p>
     */
    public static final int parseInt(byte[] b, int start, int end)
        throws NumberFormatException
    {
        return parseInt(b, start, end, 10);
    }

    /**
     * Convert the bytes within the specified range of the given byte
     * array into a signed integer in the given radix . The range extends
     * from <code>start</code> till, but not including <code>end</code>. <p>
     *
     * Based on java.lang.Integer.parseInt()
     */
    public static final int parseInt(byte[] b, int start, int end, int radix)
        throws NumberFormatException
    {
        if (b == null) {
            throw new NumberFormatException("null");
        }

        int result = 0;
        boolean negative = false;
        int i = start;
        int limit;
        int multmin;
        int digit;

        if (end > 0) {
            if (b[i] == '-') {
                negative = true;
                limit = Integer.MIN_VALUE;
                i++;
            }
            else {
                limit = -Integer.MAX_VALUE;
            }
            multmin = limit / radix;
            if (i < end) {
                digit = Octet.digit((char)b[i++], radix);
                if (digit < 0) {
                    throw new NumberFormatException("Illegal number: " + toString(b, start, end));
                }
                else {
                    result = -digit;
                }
            }
            while (i < end) {
                // Accumulating negatively avoids surprises near MAX_VALUE
                digit = Octet.digit((char)b[i++], radix);
                if (digit < 0) {
                    throw new NumberFormatException("illegal number");
                }
                if (result < multmin) {
                    throw new NumberFormatException("illegal number");
                }
                result *= radix;
                if (result < limit + digit) {
                    throw new NumberFormatException("illegal number");
                }
                result -= digit;
            }
        }
        else {
            throw new NumberFormatException("illegal number");
        }
        if (negative) {
            if (i > 1) {
                return result;
            }
            else {   /* Only got "-" */
                throw new NumberFormatException("illegal number");
            }
        }
        else {
            return -result;
        }
    }

    /**
     * Convert the bytes within the specified range of the given byte
     * array into a signed integer . The range extends from
     * <code>start</code> till, but not including <code>end</code>. <p>
     */
    public static final int parseInt(char[] c)
        throws NumberFormatException
    {
        return parseInt(c, 0, c.length);
    }

    /**
     * Convert the bytes within the specified range of the given byte
     * array into a signed integer . The range extends from
     * <code>start</code> till, but not including <code>end</code>. <p>
     */
    public static final int parseInt(char[] c, int start, int end)
        throws NumberFormatException
    {
        return parseInt(c, start, end, 10);
    }

    /**
     * Convert the bytes within the specified range of the given byte
     * array into a signed integer in the given radix . The range extends
     * from <code>start</code> till, but not including <code>end</code>. <p>
     *
     * Based on java.lang.Integer.parseInt()
     */
    public static final int parseInt(char[] b, int start, int end, int radix)
        throws NumberFormatException
    {
        if (b == null) {
            throw new NumberFormatException("null");
        }

        int result = 0;
        boolean negative = false;
        int i = start;
        int limit;
        int multmin;
        int digit;

        if (end > 0) {
            if (b[i] == '-') {
                negative = true;
                limit = Integer.MIN_VALUE;
                i++;
            }
            else {
                limit = -Integer.MAX_VALUE;
            }
            multmin = limit / radix;
            if (i < end) {
                digit = Character.digit(b[i++], radix);
                if (digit < 0) {
                    throw new NumberFormatException("illegal number: " + toString(b, start, end));
                }
                else {
                    result = -digit;
                }
            }
            while (i < end) {
                // Accumulating negatively avoids surprises near MAX_VALUE
                digit = Character.digit(b[i++], radix);
                if (digit < 0) {
                    throw new NumberFormatException("illegal number");
                }
                if (result < multmin) {
                    throw new NumberFormatException("illegal number");
                }
                result *= radix;
                if (result < limit + digit) {
                    throw new NumberFormatException("illegal number");
                }
                result -= digit;
            }
        }
        else {
            throw new NumberFormatException("illegal number");
        }
        if (negative) {
            if (i > 1) {
                return result;
            }
            else {   /* Only got "-" */
                throw new NumberFormatException("illegal number");
            }
        }
        else {
            return -result;
        }
    }

    /**
     * Convert the bytes within the specified range of the given byte
     * array into a signed long . The range extends from
     * <code>start</code> till, but not including <code>end</code>. <p>
     */
    public static final long parseLong(byte[] b)
        throws NumberFormatException
    {
        return parseLong(b, 0, b.length);
    }

    /**
     * Convert the bytes within the specified range of the given byte
     * array into a signed long . The range extends from
     * <code>start</code> till, but not including <code>end</code>. <p>
     */
    public static final long parseLong(byte[] b, int start, int end)
        throws NumberFormatException
    {
        return parseLong(b, start, end, 10);
    }

    /**
     * Convert the bytes within the specified range of the given byte
     * array into a signed long in the given radix . The range extends
     * from <code>start</code> till, but not including <code>end</code>. <p>
     *
     * Based on java.lang.Long.parseLong()
     */
    public static final long parseLong(byte[] b, int start, int end, int radix)
        throws NumberFormatException
    {
        if (b == null) {
            throw new NumberFormatException("null");
        }

        long result = 0;
        boolean negative = false;
        int i = start;
        long limit;
        long multmin;
        int digit;

        if (end > 0) {
            if (b[i] == '-') {
                negative = true;
                limit = Long.MIN_VALUE;
                i++;
            }
            else {
                limit = -Long.MAX_VALUE;
            }
            multmin = limit / radix;
            if (i < end) {
                digit = Octet.digit((char)b[i++], radix);
                if (digit < 0) {
                    throw new NumberFormatException("illegal number: " + toString(b, start, end));
                }
                else {
                    result = -digit;
                }
            }
            while (i < end) {
                // Accumulating negatively avoids surprises near MAX_VALUE
                digit = Octet.digit((char)b[i++], radix);
                if (digit < 0) {
                    throw new NumberFormatException("illegal number");
                }
                if (result < multmin) {
                    throw new NumberFormatException("illegal number");
                }
                result *= radix;
                if (result < limit + digit) {
                    throw new NumberFormatException("illegal number");
                }
                result -= digit;
            }
        }
        else {
            throw new NumberFormatException("illegal number");
        }
        if (negative) {
            if (i > 1) {
                return result;
            }
            else {   /* Only got "-" */
                throw new NumberFormatException("illegal number");
            }
        }
        else {
            return -result;
        }
    }

    /**
     * Convert the bytes within the specified range of the given byte
     * array into a signed long . The range extends from
     * <code>start</code> till, but not including <code>end</code>. <p>
     */
    public static final long parseLong(char[] c)
        throws NumberFormatException
    {
        return parseLong(c, 0, c.length);
    }

    /**
     * Convert the bytes within the specified range of the given byte
     * array into a signed long . The range extends from
     * <code>start</code> till, but not including <code>end</code>. <p>
     */
    public static final long parseLong(char[] c, int start, int end)
        throws NumberFormatException
    {
        return parseLong(c, start, end, 10);
    }

    /**
     * Convert the bytes within the specified range of the given byte
     * array into a signed long in the given radix . The range extends
     * from <code>start</code> till, but not including <code>end</code>. <p>
     *
     * Based on java.lang.Long.parseLong()
     */
    public static final long parseLong(char[] c, int start, int end, int radix)
        throws NumberFormatException
    {
        if (c == null) {
            throw new NumberFormatException("null");
        }

        long result = 0;
        boolean negative = false;
        int i = start;
        long limit;
        long multmin;
        int digit;

        if (end > 0) {
            if (c[i] == '-') {
                negative = true;
                limit = Long.MIN_VALUE;
                i++;
            }
            else {
                limit = -Long.MAX_VALUE;
            }
            multmin = limit / radix;
            if (i < end) {
                digit = Character.digit(c[i++], radix);
                if (digit < 0) {
                    throw new NumberFormatException("illegal number: " + new String(c, start, end - start));
                }
                else {
                    result = -digit;
                }
            }
            while (i < end) {
                // Accumulating negatively avoids surprises near MAX_VALUE
                digit = Character.digit(c[i++], radix);
                if (digit < 0) {
                    throw new NumberFormatException("illegal number");
                }
                if (result < multmin) {
                    throw new NumberFormatException("illegal number");
                }
                result *= radix;
                if (result < limit + digit) {
                    throw new NumberFormatException("illegal number");
                }
                result -= digit;
            }
        }
        else {
            throw new NumberFormatException("illegal number");
        }
        if (negative) {
            if (i > 1) {
                return result;
            }
            else {   /* Only got "-" */
                throw new NumberFormatException("illegal number");
            }
        }
        else {
            return -result;
        }
    }


    /**
     * ����"xxxx.xxxx"��ʽ�������Float���͵����
     *
     * @param bytes ����
     * @return ���
     * @throws NumberFormatException ����Ч�ַ�������ֳ�����
     */
    public static final float parseFloat(byte[] bytes)
        throws NumberFormatException
    {
        return parseFloat(bytes, 0, bytes.length);
    }

    /**
     * ����"xxxx.xxxx"��ʽ�������Float���͵����
     *
     * @param bytes ����
     * @param begin ��ʼλ��
     * @param end   ����λ��
     * @return ���
     * @throws NumberFormatException ����Ч�ַ�������ֳ�����
     */
    public static final float parseFloat(byte[] bytes, int begin, int end)
        throws NumberFormatException
    {
        return parseFloat(bytes, begin, end, 10);
    }

    /**
     * ����"xxxx.xxxx"��ʽ�������Float���͵����
     *
     * @param bytes ����
     * @param begin ��ʼλ��
     * @param end   ����λ��
     * @param radix ����
     * @return ���
     * @throws NumberFormatException ����Ч�ַ�������ֳ�����
     */
    public static final float parseFloat(byte[] bytes, int begin, int end, int radix)
        throws NumberFormatException
    {
        return (float)parseDouble(bytes, begin, end, radix);
    }


    /**
     * ����"xxxx.xxxx"��ʽ�������Float���͵����
     *
     * @param chars ����
     * @return ���
     * @throws NumberFormatException ����Ч�ַ�������ֳ�����
     */
    public static final float parseFloat(char[] chars)
        throws NumberFormatException
    {
        return parseFloat(chars, 0, chars.length);
    }


    /**
     * ����"xxxx.xxxx"��ʽ�������Float���͵����
     *
     * @param chars ����
     * @param begin ��ʼλ��
     * @param end   ����λ��
     * @return ���
     * @throws NumberFormatException ����Ч�ַ�������ֳ�����
     */
    public static final float parseFloat(char[] chars, int begin, int end)
        throws NumberFormatException
    {
        return parseFloat(chars, begin, end, 10);
    }


    /**
     * ����"xxxx.xxxx"��ʽ�������Float���͵����
     *
     * @param chars ����
     * @param begin ��ʼλ��
     * @param end   ����λ��
     * @param radix ����
     * @return ���
     * @throws NumberFormatException ����Ч�ַ�������ֳ�����
     */
    public static final float parseFloat(char[] chars, int begin, int end, int radix)
        throws NumberFormatException
    {
        return (float)parseDouble(chars, begin, end, radix);
    }


    /**
     * ����"xxxx.xxxx"��ʽ�������Double���͵����
     *
     * @param bytes ����
     * @return ���
     * @throws NumberFormatException ����Ч�ַ�������ֳ�����
     */
    public static final double parseDouble(byte[] bytes)
        throws NumberFormatException
    {
        return parseDouble(bytes, 0, bytes.length);
    }

    /**
     * ����"xxxx.xxxx"��ʽ�������Double���͵����
     *
     * @param bytes ����
     * @param begin ��ʼλ��
     * @param end   ����λ��
     * @return ���
     * @throws NumberFormatException ����Ч�ַ�������ֳ�����
     */
    public static final double parseDouble(byte[] bytes, int begin, int end)
        throws NumberFormatException
    {
        return parseDouble(bytes, begin, end, 10);
    }

    /**
     * ����"xxxx.xxxx"��ʽ�������Double���͵����
     *
     * @param bytes ����
     * @param begin ��ʼλ��
     * @param end   ����λ��
     * @param radix ����
     * @return ���
     * @throws NumberFormatException ����Ч�ַ�������ֳ�����
     */
    public static final double parseDouble(byte[] bytes, int begin, int end, int radix)
        throws NumberFormatException
    {
        if (bytes == null) {
            throw new NumberFormatException("null");
        }

        double result = 0.0d;
        boolean negative = false;
        int i = begin;
        double limit;
        double multmin;
        int digit;
        byte b;

        if (end > 0) {
            if (bytes[i] == '-') {
                negative = true;
                limit = -Double.MAX_VALUE;
                i++;
            }
            else {
                limit = -Double.MAX_VALUE;
            }
            multmin = limit / radix;
            if (i < end) {
                digit = Octet.digit(bytes[i++], radix);
                if (digit < 0) {
                    throw new NumberFormatException("illegal number: " + new String(bytes, begin, end - begin));
                }
                else {
                    result = -digit;
                }
            }
            while (i < end) {
                // Accumulating negatively avoids surprises near MAX_VALUE
                b = bytes[i++];
                digit = Octet.digit(b, radix);
                if (digit < 0) {
                    if (b == '.') {
                        result += parseDouble0(bytes, i + 1, end, radix);
                        break;
                    }
                    else {
                        throw new NumberFormatException("Illegal number:" + b);
                    }
                }
                if (result < multmin) {
                    throw new NumberFormatException("Illegal number");
                }
                result *= radix;
                if (result < limit + digit) {
                    throw new NumberFormatException("Illegal number");
                }
                result -= digit;
            }
        }
        else {
            throw new NumberFormatException("illegal number");
        }

        if (negative) {
            if (i > 1) {
                return result;
            }
            else {   /* Only got "-" */
                throw new NumberFormatException("illegal number");
            }
        }
        else {
            return -result;
        }
    }

    /**
     * С����
     */
    private static double parseDouble0(byte[] bytes, int start, int end, int radix)
    {
        double value = 0.0;
        final double p = 1 / radix;
        double d = p;
        int i = start;
        int v = 0;
        for (; i < end; i++) {
            byte b = bytes[i];
            v = Octet.digit(b, radix);
            if (v == -1) {
                throw new NumberFormatException("Number format exception:" + b);
            }

            value += v * d;
            d *= p;
        }
        return value;
    }


    /**
     * ����"xxxx.xxxx"��ʽ�������Double���͵����
     *
     * @param chars ����
     * @return ���
     * @throws NumberFormatException ����Ч�ַ�������ֳ�����
     */
    public static final double parseDouble(char[] chars)
        throws NumberFormatException
    {
        return parseDouble(chars, 0, chars.length);
    }


    /**
     * ����"xxxx.xxxx"��ʽ�������Double���͵����
     *
     * @param chars ����
     * @param begin ��ʼλ��
     * @param end   ����λ��
     * @return ���
     * @throws NumberFormatException ����Ч�ַ�������ֳ�����
     */
    public static final double parseDouble(char[] chars, int begin, int end)
        throws NumberFormatException
    {
        return parseDouble(chars, begin, end, 10);
    }


    /**
     * ����"xxxx.xxxx"��ʽ�������Double���͵����
     *
     * @param chars ����
     * @param begin ��ʼλ��
     * @param end   ����λ��
     * @param radix ����
     * @return ���
     * @throws NumberFormatException ����Ч�ַ�������ֳ�����
     */
    public static final double parseDouble(char[] chars, int begin, int end, int radix)
        throws NumberFormatException
    {
        if (chars == null) {
            throw new NumberFormatException("null");
        }

        double result = 0.0d;
        boolean negative = false;
        int i = begin;
        double limit;
        double multmin;
        int digit;
        char c;

        if (end > 0) {
            if (chars[i] == '-') {
                negative = true;
                limit = -Double.MAX_VALUE;
                i++;
            }
            else {
                limit = -Double.MAX_VALUE;
            }
            multmin = limit / radix;
            if (i < end) {
                digit = Octet.digit(chars[i++], radix);
                if (digit < 0) {
                    throw new NumberFormatException("illegal number: " + new String(chars, begin, end - begin));
                }
                else {
                    result = -digit;
                }
            }
            while (i < end) {
                c = chars[i++];
                // Accumulating negatively avoids surprises near MAX_VALUE
                digit = Octet.digit(c, radix);
                if (digit < 0) {
                    if (c == '.') {
                        result += parseDouble0(chars, i + 1, end, radix);
                        break;
                    }
                    else {
                        throw new NumberFormatException("Illegal number:" + c);
                    }
                }
                if (result < multmin) {
                    throw new NumberFormatException("Illegal number");
                }
                result *= radix;
                if (result < limit + digit) {
                    throw new NumberFormatException("Illegal number");
                }
                result -= digit;
            }
        }
        else {
            throw new NumberFormatException("illegal number");
        }

        if (negative) {
            if (i > 1) {
                return result;
            }
            else {   /* Only got "-" */
                throw new NumberFormatException("illegal number");
            }
        }
        else {
            return -result;
        }
    }


    /**
     * С����
     */
    private static double parseDouble0(char[] chars, int start, int end, int radix)
    {
        double value = 0.0;
        final double p = 1 / radix;
        double d = p;
        int i = start;
        int v = 0;
        for (; i < end; i++) {
            char b = chars[i];
            v = Character.digit(b, radix);
            if (v == -1) {
                throw new NumberFormatException("Number format exception:" + b);
            }

            value += v * d;
            d *= p;
        }
        return value;
    }

    /**
     * The function is not same as Character.isDigit(char)
     */
    public static final boolean isDigit(int c)
    {
        return Octet.isDigit(c);
    }

    /**
     * Determine whether a character is a hexadecimal character.
     *
     * @return true if the char is betweeen '0' and '9', 'a' and 'f'
     *         or 'A' and 'F', false otherwise
     */
    public static final boolean isHex(int c)
    {
        return (Octet.isDigit(c) ||
                (c >= 'a' && c <= 'f') ||
                (c >= 'A' && c <= 'F'));
    }

    /**
     * Determine whether a char is an alphabetic character: a-z or A-Z
     *
     * @return true if the char is alphabetic, false otherwise
     */
    public static final boolean isAlpha(int c)
    {
        return Octet.isAlpha(c);
    }

    /**
     * Determine whether a char is an alphanumeric: 0-9, a-z or A-Z
     *
     * @return true if the char is alphanumeric, false otherwise
     */
    public static final boolean isAlphanum(int c)
    {
        return Octet.isLetterOrDigit(c);
    }

    /**
     * Determine whether a char is ' ' or '\t'
     */
    public static final boolean isSpace(int c)
    {
        return c == ' ' || c == '\t';
    }



    public static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");
    public static final byte[] LINE_SEPARATOR_BYTES = LINE_SEPARATOR.getBytes();
    public static final char[] LINE_SEPARATOR_CHARS = LINE_SEPARATOR.toCharArray();

    public static final String CRLF = "\r\n";
    public static final byte[] CRLF_BYTES = CRLF.getBytes();
    public static final char[] CRLF_CHARS = CRLF.toCharArray();


    public static final char toUnicodeChar(String str, int begin)
    {
        if (str == null || (str.length() - begin) < 6) {
            throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
        }

        int i = begin;

        char c = str.charAt(i++);
        if (c != '\\') {
            throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
        }
        c = str.charAt(i++);

        if (c != 'u' || c != 'U') {
            throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
        }

        int value = 0;
        for (; i < begin + 6; i++) {
            c = str.charAt(i);
            if (c >= '0' && c <= '9') {
                value = (value << 4) + c - '0';
            }
            else if (c >= 'a' && c <= 'f') {
                value = (value << 4) + 10 + c - 'a';
            }
            else if (c >= 'A' && c <= 'F') {
                value = (value << 4) + 10 + c - 'A';
            }
            else {
                throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
            }
        }
        return (char)value;
    }


    /**
     * @param ch1
     * @param ch2
     */
    public static final boolean equals(char ch1, char ch2)
    {
        return ch1 == ch2;
    }

    public static final boolean equalsIgnoreCase(char ch1, char ch2)
    {
        return ch1 == ch2 || ch1 == Character.toLowerCase(ch2)
               || ch1 == Character.toUpperCase(ch2);
    }

}
