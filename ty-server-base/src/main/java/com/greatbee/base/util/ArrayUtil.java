package com.greatbee.base.util;

import com.greatbee.base.util.lang.Octet;

import java.lang.ref.SoftReference;
import java.lang.reflect.Array;
import java.util.*;

/**
 *
 * @author system
 * @version 1.00 2005-2-5 20:44:57
 */
public class ArrayUtil
{
    /**
     * @throws IllegalArgumentException
     * @throws ArrayIndexOutOfBoundsException
     */
    public static void rangeCheck(int length, int from, int to)
    {
        if (from >= to) {
            throw new IllegalArgumentException("from(" + from +
                                               ") > to(" + to + ")");
        }
        if (from < 0) {
            throw new ArrayIndexOutOfBoundsException(from);
        }
        if (to > length) {
            throw new ArrayIndexOutOfBoundsException(to);
        }
    }

    /**
     * @throws ArrayIndexOutOfBoundsException
     */
    public static void indexCheck(int off, int len, int index)
    {
        if (index < off || index >= off + len) {
            throw new ArrayIndexOutOfBoundsException("Index out of bound:" + index);
        }
    }

    /**
     */
    public static final int NOT_FOUND = -1;

    /**
     * @see #search(long[],int,int,long)
     */
    public static int search(long[] a, long key)
    {
        return search(a, 0, a.length, key);
    }

    /**
     * @see #search(long[],long)
     */
    public static int search(long[] a, int from, int to, long key)
    {
        for (int i = from; i < to; i++) {
            if (a[i] == key) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * @see #search(int[],int,int,int)
     */
    public static int search(int[] a, int key)
    {
        return search(a, 0, a.length, key);
    }

    /**
     * @see #search(int[],int)
     */
    public static int search(int[] a, int from, int to, int key)
    {
        for (int i = from; i < to; i++) {
            if (a[i] == key) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * @see #search(short[],int,int,short)
     */
    public static int search(short[] a, short key)
    {
        return search(a, 0, a.length, key);
    }

    /**
     * @see #search(short[],short)
     */
    public static int search(short[] a, int from, int to, short key)
    {
        for (int i = from; i < to; i++) {
            if (a[i] == key) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * @see #search(char[],int,int,char)
     */
    public static int search(char[] a, char key)
    {
        return search(a, 0, a.length, key);
    }

    /**
     * @see #search(char[],char)
     */
    public static int search(char[] a, int from, int to, char key)
    {
        for (int i = from; i < to; i++) {
            if (a[i] == key) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * @see #search(byte[],int,int,byte)
     */
    public static int indexOf(char[] a, char[] target)
    {
        return indexOf(a, 0, a.length, target);
    }

    /**
     * @see #search(byte[],byte)
     */
    public static int indexOf(char[] source, int from, int to, char[] target)
    {
        int sourceCount = to - from;
        int targetCount = target.length;
        if (from >= sourceCount) {
            return (targetCount == 0 ? sourceCount : -1);
        }
        if (from < 0) {
            from = 0;
        }
        if (targetCount == 0) {
            return from;
        }

        char first = target[0];
        int i = from;
        int max = sourceCount - targetCount + i;

        startSearchForFirstChar:
        while (true) {
            /* Look for first character. */
            while (i <= max && source[i] != first) {
                i++;
            }
            if (i > max) {
                return NOT_FOUND;
            }

            /* Found first character, now look at the rest of v2 */
            int j = i + 1;
            int end = j + targetCount - 1;
            int k = 1;
            while (j < end) {
                if (source[j++] != target[k++]) {
                    i++;
                    /* Look for str's first char again. */
                    continue startSearchForFirstChar;
                }
            }
            return i;    /* Found whole string. */
        }
    }


    /**
     * @see #search(byte[],int,int,byte)
     */
    public static int search(byte[] a, byte target)
    {
        return search(a, 0, a.length, target);
    }

    /**
     * @see #search(byte[],byte)
     */
    public static int search(byte[] a, int from, int to, byte target)
    {
        for (int i = from; i < to; i++) {
            if (a[i] == target) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * @see #search(byte[],int,int,byte)
     */
    public static int indexOf(byte[] a, byte[] target)
    {
        return indexOf(a, 0, a.length, target);
    }

    /**
     * @see #search(byte[],byte)
     */
    public static int indexOf(byte[] source, int from, int to, byte[] target)
    {
        int sourceCount = to - from;
        int targetCount = target.length;
        if (from >= sourceCount) {
            return (targetCount == 0 ? sourceCount : -1);
        }
        if (from < 0) {
            from = 0;
        }
        if (targetCount == 0) {
            return from;
        }

        byte first = target[0];
        int i = from;
        int max = sourceCount - targetCount + i;

        startSearchForFirstChar:
        while (true) {
            /* Look for first character. */
            while (i <= max && source[i] != first) {
                i++;
            }
            if (i > max) {
                return NOT_FOUND;
            }

            /* Found first character, now look at the rest of v2 */
            int j = i + 1;
            int end = j + targetCount - 1;
            int k = 1;
            while (j < end) {
                if (source[j++] != target[k++]) {
                    i++;
                    /* Look for str's first char again. */
                    continue startSearchForFirstChar;
                }
            }
            return i;    /* Found whole string. */
        }
    }

    /**
     * @see #search(double[],int,int,double)
     */
    public static int search(double[] a, double key)
    {
        return search(a, 0, a.length, key);
    }

    /**
     * @see #search(double[],double)
     */
    public static int search(double[] a, int from, int to, double key)
    {


        for (int i = from; i < to; i++) {
            if (a[i] == key) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * @see #search(float[],int,int,float)
     */
    public static int search(float[] a, float key)
    {
        return search(a, 0, a.length, key);
    }

    /**
     * @see #search(float[],float)
     */
    public static int search(float[] a, int from, int to, float key)
    {


        for (int i = from; i < to; i++) {
            if (a[i] == key) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * @see #search(Object[],int,int,Object)
     */
    public static <T> int search(T[] a, T key)
    {
        return search(a, 0, a.length, key);
    }

    /**
     * @see #search(Object[],Object)
     */
    public static <T> int search(T[] a, int from, int to, T key)
    {


        for (int i = from; i < to; i++) {
            if (key == a[i] || key.equals(a[i])) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * @see #search(Object[],int,int,Object)
     */
    public static <T> int search(T[] a, T key, Comparator<T> comparator)
    {
        return search(a, 0, a.length, key, comparator);
    }

    /**
     * @see #search(Object[],Object)
     */
    public static <T> int search(T[] a, int from, int to,
                                 T key, Comparator<T> comparator)
    {


        for (int i = from; i < to; i++) {
            if (comparator.compare(key, a[i]) == 0) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /****************************************************************************/

    /**
     */
    public static boolean isValid(long[] a)
    {
        return a != null && a.length > 0;
    }

    /**
     */
    public static boolean isInvalid(long[] a)
    {
        return a == null || a.length == 0;
    }

    /**
     */
    public static boolean isValid(int[] a)
    {
        return a != null && a.length > 0;
    }

    /**
     */
    public static boolean isInvalid(int[] a)
    {
        return a == null || a.length == 0;
    }


    /**
     */
    public static boolean isValid(short[] a)
    {
        return a != null && a.length > 0;
    }

    /**
     */
    public static boolean isInvalid(short[] a)
    {
        return a == null || a.length == 0;
    }


    /**
     */
    public static boolean isValid(char[] a)
    {
        return a != null && a.length > 0;
    }

    /**
     */
    public static boolean isInvalid(char[] a)
    {
        return a == null || a.length == 0;
    }

    /**
     */
    public static boolean isValid(byte[] a)
    {
        return a != null && a.length > 0;
    }

    /**
     */
    public static boolean isInvalid(byte[] a)
    {
        return a == null || a.length == 0;
    }

    /**
     */
    public static boolean isValid(boolean[] a)
    {
        return a != null && a.length > 0;
    }

    /**
     */
    public static boolean isInvalid(boolean[] a)
    {
        return a == null || a.length == 0;
    }

    /**
     */
    public static boolean isValid(double[] a)
    {
        return a != null && a.length > 0;
    }

    /**
     */
    public static boolean isInvalid(double[] a)
    {
        return a == null || a.length == 0;
    }

    /**
     */
    public static boolean isValid(float[] a)
    {
        return a != null && a.length > 0;
    }

    /**
     *  ʱЧ
     *
     * @param a 
     */
    public static boolean isInvalid(float[] a)
    {
        return a == null || a.length == 0;
    }

    /**
     *  ʱЧ
     *
     * @param a 
     */
    public static boolean isValid(Object[] a)
    {
        return a != null && a.length > 0;
    }

    /**
     *  ʱЧ
     *
     * @param a 
     */
    public static boolean isInvalid(Object[] a)
    {
        return a == null || a.length == 0;
    }

    //

    /**
     *
     * @param array 
     * @param obj   
     * @return 
     */
    public static <O> O[] add(O[] array, O obj)
    {
        Class clazz = array.getClass().getComponentType();
        int len = array.length;
        O[] newArray = (O[])Array.newInstance(clazz, len + 1);
        if (len > 0) {
            System.arraycopy(array, 0, newArray, 0, len);
        }
        newArray[len] = obj;
        return newArray;
    }

    /** 
     * @param obj
     * @param pos   ָòַ [0, array.length]
     * @return 
     */
    public static <O> O[] add(O[] array, int pos, O obj)
    {
        int len = array.length;
        if (pos < 0 || pos > len) {
            throw new IndexOutOfBoundsException("Invalid pos:" + pos + " length:" + len);
        }
        Class clazz = array.getClass().getComponentType();
        O[] newArray = (O[])Array.newInstance(clazz, len + 1);
        if (pos > 0) {
            System.arraycopy(array, 0, newArray, 0, pos);
        }
        int c = len - pos;
        if (c > 0) {
            System.arraycopy(array, pos, newArray, pos + 1, c);
        }
        newArray[pos] = obj;
        return newArray;
    }

    /**
     *
     * @param array 
     * @param obj   
     * @return ֱ֮ӷظ
     */
    public static <O> O[] remove(O[] array, O obj)
    {
        for (int i = 0; i < array.length; i++) {
            if (obj.equals(array[i])) {
                return remove0(array, i);
            }
        }
        return array;
    }

    /**
     */
    public static Object[] remove(Object[] array, int index)
    {
        if (index < 0 || index >= array.length) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        return remove0(array, index);
    }

    /**
     * ָɾֱ֮ӷظ
     *
     * @param array 
     * @param index 
     * @return ֱ֮ӷظ
     */
    private static <O> O[] remove0(O[] array, int index)
    {
        Class clazz = array.getClass().getComponentType();
        int size = array.length - 1;
        O[] newArray = (O[])Array.newInstance(clazz, size);
        if (index > 0) {
            System.arraycopy(array, 0, newArray, 0, index);
        }
        if (index < size) {
            System.arraycopy(array, index + 1, newArray, index, size - index);
        }
        return newArray;
    }

    /**
     * ַָ
     *
     * @param array ַ
     * @param str   ַ
     * @return 
     */
    public static String[] add(String[] array, String str)
    {
        int len = array.length;
        String[] newArray = new String[len + 1];
        if (len > 0) {
            System.arraycopy(array, 0, newArray, 0, len);
        }
        newArray[len] = str;
        return newArray;
    }

    /**
     * ַָ
     *
     * @param array ַ
     * @param str   ַ
     * @param pos   ָòַ [0, array.length]
     * @return 
     */
    public static String[] add(String[] array, int pos, String str)
    {
        int len = array.length;
        if (pos < 0 || pos > len) {
            throw new IndexOutOfBoundsException("Invalid pos:" + pos + " length:" + len);
        }
        String[] newArray = new String[len + 1];
        if (pos > 0) {
            System.arraycopy(array, 0, newArray, 0, pos);
        }
        int c = len - pos;
        if (c > 0) {
            System.arraycopy(array, pos, newArray, pos + 1, c);
        }
        newArray[pos] = str;
        return newArray;
    }

    /**
     * @param array
     */
    public static String[] remove(String[] array, String str)
    {
        for (int i = 0; i < array.length; i++) {
            if (str.equals(array[i])) {
                int size = array.length - 1;
                String[] newArray = new String[size];
                if (i > 0) {
                    System.arraycopy(array, 0, newArray, 0, i);
                }
                if (i < size) {
                    System.arraycopy(array, i + 1, newArray, i, size - i);
                }
                return newArray;
            }
        }
        return array;
    }

    /**
     * @param coll  
     * @param array 
     */
    public static <E> int addAll(Collection<E> coll, E[] array)
    {
        if (isValid(array)) {
            for (E anArray : array) {
                coll.add(anArray);
            }
            return array.length;
        }
        return 0;
    }

    /**
     * @param coll  
     * @param array 
     */
    public static <E> int addAll(List<E> coll, E[] array)
    {
        if (isValid(array)) {
            for (int i = 0, size = array.length; i < size; i++) {
                coll.add(array[i]);
            }
            return array.length;
        }
        return 0;
    }

    /**
     * @param list  б
     * @param array 
     */
    public static <E> void removeAll(List<E> list, E[] array)
    {
        if (isValid(array)) {
            for (int i = 0; i < array.length; i++) {
                list.remove(array[i]);
            }
        }
    }

    /**
     * @param array 
     * @param toAdd Ҫӵ
     */
    public static Object[] append(Object[] array, Object[] toAdd)
    {
        if (isValid(toAdd)) {
            Object[] newArray = new Object[array.length + toAdd.length];
            System.arraycopy(array, 0, newArray, 0, array.length);
            System.arraycopy(toAdd, 0, newArray, array.length, toAdd.length);
            return newArray;
        }
        return array;
    }

    //Matches

    /**
     * @return <tt>true</tt> Ƿƥ
     */
    public static boolean matches(long[] a1, int off1,
                                  long[] a2, int off2,
                                  int length)
    {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }

        return matches0(a1, off1, a2, off2, length);
    }

    /**
     * @return <tt>true</tt> Ƿƥ
     */
    private static boolean matches0(long[] a1, int off1,
                                    long[] a2, int off2,
                                    int length)
    {
        int i = off1, max = off1 + length;
        int j = off2;
        for (; i < max; i++, j++) {
            if (a1[i] != a2[j]) {
                return false;
            }
        }
        return true;
    }


    /**
     * @return <tt>true</tt> Ƿƥ
     */
    public static boolean matches(int[] a1, int off1,
                                  int[] a2, int off2,
                                  int length)
    {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }

        return matches0(a1, off1, a2, off2, length);
    }

    /**
     * @return <tt>true</tt> Ƿƥ
     */
    private static boolean matches0(int[] a1, int off1,
                                    int[] a2, int off2,
                                    int length)
    {
        int i = off1, max = off1 + length;
        int j = off2;
        for (; i < max; i++, j++) {
            if (a1[i] != a2[j]) {
                return false;
            }
        }
        return true;
    }


    /**
     * @param a1     1
     * @param off1   ʼ1
     * @param a2     2
     * @param off2   ʼ2
     * @param length Ƚϳ
     * @return <tt>true</tt> Ƿƥ
     */
    public static boolean matches(short[] a1, int off1,
                                  short[] a2, int off2,
                                  int length)
    {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }

        return matches0(a1, off1, a2, off2, length);
    }

    /**
     *
     * @param a1     1
     * @param off1   ʼ1
     * @param a2     2
     * @param off2   ʼ2
     * @param length Ƚϳ
     * @return <tt>true</tt> Ƿƥ
     */
    private static boolean matches0(short[] a1, int off1,
                                    short[] a2, int off2,
                                    int length)
    {
        int i = off1, max = off1 + length;
        int j = off2;
        for (; i < max; i++, j++) {
            if (a1[i] != a2[j]) {
                return false;
            }
        }
        return true;
    }


    /**
     * @param a1     1
     * @param off1   ʼ1
     * @param a2     2
     * @param off2   ʼ2
     * @param length Ƚϳ
     * @return <tt>true</tt> Ƿƥ
     */
    public static boolean matches(char[] a1, int off1,
                                  char[] a2, int off2,
                                  int length)
    {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }

        return matches0(a1, off1, a2, off2, length, false);
    }

    /**
     * @param a1         1
     * @param off1       ʼ1
     * @param a2         2
     * @param off2       ʼ2
     * @return <tt>true</tt> Ƿƥ
     */
    public static boolean matches(char[] a1, int off1,
                                  char[] a2, int off2,
                                  int length,
                                  boolean ignoreCase)
    {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }

        return matches0(a1, off1, a2, off2, length, ignoreCase);
    }

    /**
     * @return <tt>true</tt> Ƿƥ
     */
    private static boolean matches0(char[] a1, int off1,
                                    char[] a2, int off2,
                                    int length,
                                    boolean ignoreCase)
    {
        int i = off1, max = off1 + length;
        int j = off2;
        if (ignoreCase) {
            for (; i < max; i++, j++) {
                if (Character.toUpperCase(a1[i]) != Character.toUpperCase(a2[j])) {
                    return false;
                }
            }
        }
        else {
            for (; i < max; i++, j++) {
                if (a1[i] != a2[j]) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * @return <tt>true</tt> Ƿƥ
     */
    public static boolean matches(byte[] a1, int off1,
                                  byte[] a2, int off2,
                                  int length)
    {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }

        return matches0(a1, off1, a2, off2, length, false);
    }

    /**
     * @return <tt>true</tt> Ƿƥ
     */
    public static boolean matches(byte[] a1, int off1,
                                  byte[] a2, int off2,
                                  int length, boolean ignoreCase)
    {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }

        return matches0(a1, off1, a2, off2, length, ignoreCase);
    }

    /**
     * @return <tt>true</tt> Ƿƥ
     */
    private static boolean matches0(byte[] a1, int off1,
                                    byte[] a2, int off2,
                                    int length,
                                    boolean ignoreCase)
    {
        int i = off1, max = off1 + length;
        int j = off2;
        if (ignoreCase) {
            for (; i < max; i++, j++) {
                if (Octet.toUpperCase(a1[i]) != Octet.toUpperCase(a2[j])) {
                    return false;
                }
            }
        }
        else {
            for (; i < max; i++, j++) {
                if (a1[i] != a2[j]) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * @return <tt>true</tt> Ƿƥ
     */
    public static boolean matches(boolean[] a1, int off1,
                                  boolean[] a2, int off2,
                                  int length)
    {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }

        return matches0(a1, off1, a2, off2, length);
    }

    /**
     * @return <tt>true</tt> Ƿƥ
     */
    private static boolean matches0(boolean[] a1, int off1,
                                    boolean[] a2, int off2,
                                    int length)
    {
        int i = off1, max = off1 + length;
        int j = off2;
        for (; i < max; i++, j++) {
            if (a1[i] != a2[j]) {
                return false;
            }
        }
        return true;
    }


    /**
     * @return <tt>true</tt> Ƿƥ
     */
    public static boolean matches(double[] a1, int off1,
                                  double[] a2, int off2,
                                  int length)
    {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }

        return matches0(a1, off1, a2, off2, length);
    }

    /**
     * @return <tt>true</tt> Ƿƥ
     */
    private static boolean matches0(double[] a1, int off1,
                                    double[] a2, int off2,
                                    int length)
    {
        int i = off1, max = off1 + length;
        int j = off2;
        for (; i < max; i++, j++) {
            if (Double.doubleToLongBits(a1[i]) != Double.doubleToLongBits(a2[j])) {
                return false;
            }
        }
        return true;
    }


    /**
     * @return <tt>true</tt> Ƿƥ
     */
    public static boolean matches(float[] a1, int off1,
                                  float[] a2, int off2,
                                  int length)
    {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }

        return matches0(a1, off1, a2, off2, length);
    }

    /**
     * @return <tt>true</tt> Ƿƥ
     */
    private static boolean matches0(float[] a1, int off1,
                                    float[] a2, int off2,
                                    int length)
    {
        int i = off1, max = off1 + length;
        int j = off2;
        for (; i < max; i++, j++) {
            if (Float.floatToIntBits(a1[i]) != Float.floatToIntBits(a2[j])) {
                return false;
            }
        }
        return true;
    }


    /**
     * @return <tt>true</tt> Ƿƥ
     */
    public static boolean matches(Object[] a1, int off1,
                                  Object[] a2, int off2,
                                  int length)
    {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }

        return matches0(a1, off1, a2, off2, length);
    }

    /**
     * @return <tt>true</tt> Ƿƥ
     */
    private static boolean matches0(Object[] a1, int off1,
                                    Object[] a2, int off2,
                                    int length)
    {
        int i = off1, max = off1 + length;
        int j = off2;
        Object obj1, obj2;
        for (; i < max; i++, j++) {
            obj1 = a1[i];
            obj2 = a2[j];
            if (!(obj1 == null ? obj2 == null : obj1.equals(obj2))) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param array 
     */
    public static <T> Iterator<T> iterator(T[] array)
    {
        return listIterator(array);
    }

    /**
     */
    public static <T> Iterator<T> iterator(T[] array, int off, int len)
    {
        return listIterator(array, off, len);
    }

    /**
     * @param array 
     */
    public static <T> ListIterator<T> listIterator(T[] array)
    {
        if (isInvalid(array)) {
            return CollectionUtil.emptyListIterator();
        }
        else {
            return new ArrayIterator<T>(array);
        }
    }

    /**
     */
    public static <T> ListIterator<T> listIterator(T[] array, int off, int len)
    {
        if (isInvalid(array) || len == 0 || off > len) {
            return CollectionUtil.emptyListIterator();
        }
        else {
            return new ArrayIterator<T>(array, off, len);
        }
    }

    /**
     * @param array 
     */
    public static <T> Enumeration<T> toEnum(T[] array)
    {
        if (isInvalid(array)) {
            return CollectionUtil.emptyEnum();
        }
        else {
            return new ArrayIterator<T>(array);
        }
    }

    /**
     */
    public static <T> Enumeration<T> toEnum(T[] array, int off, int len)
    {
        if (isInvalid(array) || len == 0 || off > len) {
            return CollectionUtil.emptyEnum();
        }
        else {
            return new ArrayIterator<T>(array, off, len);
        }
    }

    private static ThreadLocal<SoftReference<List>> listCache = new ThreadLocal<SoftReference<List>>();

    private static final int MAX_CAPACITY = 1024;

    /**
     * Cacheб
     */
    static <E> List<E> getList()
    {
        SoftReference ref = listCache.get();
        List<E> list;
        if (ref == null || (list = (List<E>)ref.get()) == null) {
            list = new ArrayList<E>(256);
            listCache.set(new SoftReference<List>(list));
        }
        return list;
    }

    /**
     * Cache
     */
    static void clearList(List list, int size)
    {
        if (size > MAX_CAPACITY) {
            listCache.set(new SoftReference<List>(new ArrayList(256)));
        }
        else {
            list.clear();
        }
    }

    /**
     */
    public static <E> E[] toArray(Enumeration<E> en)
    {
        List<E> list = getList();
        CollectionUtil.copy(list, en);
        int size = list.size();
        E[] array = (E[])new Object[size];
        list.toArray(array);
        clearList(list, size);
        return array;
    }

    /**
     */
    public static <E> E[] toArray(Iterator<E> it)
    {
        List<E> list = getList();
        CollectionUtil.copy(list, it);
        int size = list.size();
        E[] array = (E[])new Object[size];
        list.toArray(array);
        clearList(list, size);
        return array;
    }

    /**
     */
    public static int[] toArray(List<Integer> ints)
    {
        if (CollectionUtil.isValid(ints)) {
            int size = ints.size();
            int[] ids = new int[size];
            for (int i = 0; i < size; i++) {
                ids[i] = ints.get(i);
            }
            return ids;
        }
        else {
            return new int[0];
        }
    }

    /**
     * @param componentType 
     */
    public static <E> E[] toArray(Enumeration<E> en, Class componentType)
    {
        List<E> list = getList();
        CollectionUtil.copy(list, en);
        int size = list.size();
        E[] array = (E[])Array.newInstance(componentType, size);
        list.toArray(array);
        clearList(list, size);
        return array;
    }

    /**
     * @param componentType 
     */
    public static <E> E[] toArray(Iterator<E> it, Class componentType)
    {
        List<E> list = getList();
        CollectionUtil.copy(list, it);
        int size = list.size();
        E[] array = (E[])Array.newInstance(componentType, size);
        list.toArray(array);
        clearList(list, size);
        return array;
    }

    //From Arrays

    /**
     * @param l 
     */
    public static void sort(long[] l)
    {
        Arrays.sort(l);
    }

    /**
     * @param to    [from, l.length)
     */
    public static void sort(long[] l, int from, int to)
    {
        Arrays.sort(l, from, to);
    }

    /**
     * @param i 
     */
    public static void sort(int[] i)
    {
        Arrays.sort(i);
    }

    /**
     * @param from ʼ [0, to]
     * @param to    [from, i.length)
     */
    public static void sort(int[] i, int from, int to)
    {
        Arrays.sort(i, from, to);
    }

    /**
     */
    public static void sort(short[] s)
    {
        Arrays.sort(s);
    }

    /**
     * @param s    
     * @param from ʼ [0, to]
     * @param to    [from, s.length)
     */
    public static void sort(short[] s, int from, int to)
    {
        Arrays.sort(s, from, to);
    }

    /**
     */
    public static void sort(char[] a)
    {
        Arrays.sort(a);
    }

    /**
     */
    public static void sort(char[] a, int from, int to)
    {
        Arrays.sort(a, from, to);
    }

    /**
     */
    public static void sort(byte[] a)
    {
        Arrays.sort(a);
    }

    /**
     */
    public static void sort(byte[] a, int from, int to)
    {
        Arrays.sort(a, from, to);
    }

    /**
     */
    public static void sort(double[] a)
    {
        Arrays.sort(a);
    }

    /**
     * @param to    [from, a.length)
     */
    public static void sort(double[] a, int from, int to)
    {
        Arrays.sort(a, from, to);
    }

    /**
     * @param a 
     */
    public static void sort(float[] a)
    {
        Arrays.sort(a);
    }

    /**
     * @param a    
     * @param from ʼ [0, to]
     * @param to    [from, a.length)
     */
    public static void sort(float[] a, int from, int to)
    {
        Arrays.sort(a, from, to);
    }

    /**
     * ԶĿ
     *
     * @param a 
     */
    public static void sort(Object[] a)
    {
        Arrays.sort(a);
    }

    /**
     */
    public static void sort(Object[] a, int from, int to)
    {
        Arrays.sort(a, from, to);
    }

    /**
     */
    public static <T> void sort(T[] a, Comparator<? super T> c)
    {
        Arrays.sort(a, c);
    }

    /**
     */
    public static <T> void sort(T[] a, int from, int to,
                                Comparator<? super T> c)
    {
        Arrays.sort(a, from, to, c);
    }

    /**************************************************************************/
    /**
     * @see #sort(byte[])
     */
    public static int binarySearch(long[] a, long key)
    {
        return Arrays.binarySearch(a, key);
    }


    /**
     * @see #sort(int[])
     */
    public static int binarySearch(int[] a, int key)
    {
        return Arrays.binarySearch(a, key);
    }

    /**
     * @see #sort(short[])
     */
    public static int binarySearch(short[] a, short key)
    {
        return Arrays.binarySearch(a, key);
    }

    /**
     * @see #sort(char[])
     */
    public static int binarySearch(char[] a, char key)
    {
        return Arrays.binarySearch(a, key);
    }

    /**
     * @see #sort(byte[])
     */
    public static int binarySearch(byte[] a, byte key)
    {
        return Arrays.binarySearch(a, key);
    }

    /**
     * @see #sort(double[])
     */
    public static int binarySearch(double[] a, double key)
    {
        return Arrays.binarySearch(a, key);
    }

    /**
     * @see #sort(float[])
     */
    public static int binarySearch(float[] a, float key)
    {
        return Arrays.binarySearch(a, key);
    }

    /**
     * @see #sort(Object[])
     */
    public static int binarySearch(Object[] a, Object key)
    {
        return Arrays.binarySearch(a, key);
    }

    /**
     * @see #sort(Object[],Comparator)
     */
    public static <T> int binarySearch(T[] a, T key, Comparator<? super T> c)
    {
        return Arrays.binarySearch(a, key, c);
    }

    /**
     * @param a1 1
     * @param a2 2
     *           <tt>true</tt> Ƿƥ
     */
    public static boolean equals(long[] a1, long[] a2)
    {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }

        int length = a1.length;
        if (a2.length != length) {
            return false;
        }

        return matches(a1, 0, a2, 0, length);
    }


    /**
     * @param a1 1
     * @param a2 2
     *           <tt>true</tt> Ƿƥ
     */
    public static boolean equals(int[] a1, int[] a2)
    {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }

        int length = a1.length;
        if (a2.length != length) {
            return false;
        }

        return matches(a1, 0, a2, 0, length);
    }

    /**
     * @param a1 1
     * @param a2 2
     *           <tt>true</tt> Ƿƥ
     */
    public static boolean equals(short[] a1, short[] a2)
    {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }

        int length = a1.length;
        if (a2.length != length) {
            return false;
        }

        return matches(a1, 0, a2, 0, length);
    }

    /**
     * @param a1 1
     * @param a2 2
     *           <tt>true</tt> Ƿƥ
     */
    public static boolean equals(char[] a1, char[] a2)
    {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }

        int length = a1.length;
        if (a2.length != length) {
            return false;
        }

        return matches(a1, 0, a2, 0, length);
    }

    /**
     * @param a1 1
     * @param a2 2
     *           <tt>true</tt> Ƿƥ
     */
    public static boolean equalsIgnoreCase(char[] a1, char[] a2)
    {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }

        int length = a1.length;
        if (a2.length != length) {
            return false;
        }

        return matches(a1, 0, a2, 0, length, true);
    }

    /**
     * @param a1 1
     * @param a2 2
     *           <tt>true</tt> Ƿƥ
     */
    public static boolean equals(byte[] a1, byte[] a2)
    {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }

        int length = a1.length;
        if (a2.length != length) {
            return false;
        }

        return matches(a1, 0, a2, 0, length);
    }

    /**
     * @param a1 1
     * @param a2 2
     *           <tt>true</tt> Ƿƥ
     */
    public static boolean equalsIgnoreCase(byte[] a1, byte[] a2)
    {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }

        int length = a1.length;
        if (a2.length != length) {
            return false;
        }

        return matches(a1, 0, a2, 0, length, true);
    }

    /**
     * @param a1 1
     * @param a2 2
     *           <tt>true</tt> Ƿƥ
     */
    public static boolean equals(boolean[] a1, boolean[] a2)
    {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }

        int length = a1.length;
        if (a2.length != length) {
            return false;
        }

        return matches(a1, 0, a2, 0, length);
    }

    /**
     * @param a1 1
     * @param a2 2
     *           <tt>true</tt> Ƿƥ
     */
    public static boolean equals(double[] a1, double[] a2)
    {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }

        int length = a1.length;
        if (a2.length != length) {
            return false;
        }

        return matches(a1, 0, a2, 0, length);
    }


    /**
     * @param a1 1
     * @param a2 2
     *           <tt>true</tt> Ƿƥ
     */
    public static boolean equals(float[] a1, float[] a2)
    {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }

        int length = a1.length;
        if (a2.length != length) {
            return false;
        }

        return matches(a1, 0, a2, 0, length);
    }


    /**
     * @param a1 1
     * @param a2 2
     *           <tt>true</tt> Ƿƥ
     */
    public static boolean equals(Object[] a1, Object[] a2)
    {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }

        int length = a1.length;
        if (a2.length != length) {
            return false;
        }

        return matches(a1, 0, a2, 0, length);
    }

    /**************************************************************************/
    /**
     * ֵ
     *
     * @param a   
     * @param val ֵ
     */
    public static void fill(long[] a, long val)
    {
        //Ϊʲôֱӵ # fill(long[], int, int,long)
        //ӹ飬Ч
        int len = a.length;
        for (int i = 0; i < len; i++) {
            a[i] = val;
        }
    }

    /**
     * ֵ
     *
     * @param a    
     * @param from ʼ
     * @param to   
     * @param val  ֵ
     * @throws IllegalArgumentException
     * @throws ArrayIndexOutOfBoundsException
     */
    public static void fill(long[] a, int from, int to,
                            long val)
    {
        rangeCheck(a.length, from, to);
        for (int i = from; i < to; i++) {
            a[i] = val;
        }
    }


    /**
     * @param a   
     * @param val ֵ
     */
    public static void fill(int[] a, int val)
    {
        int len = a.length;
        for (int i = 0; i < len; i++) {
            a[i] = val;
        }
    }

    /**
     * @param a    
     * @param from ʼ
     * @param to   
     * @param val  ֵ
     * @throws IllegalArgumentException
     * @throws ArrayIndexOutOfBoundsException
     */
    public static void fill(int[] a, int from, int to,
                            int val)
    {
        rangeCheck(a.length, from, to);
        for (int i = from; i < to; i++) {
            a[i] = val;
        }
    }


    /**
     * @param a   
     * @param val ֵ
     */
    public static void fill(short[] a, short val)
    {
        int len = a.length;
        for (int i = 0; i < len; i++) {
            a[i] = val;
        }
    }

    /**
     * @param a    
     * @param from ʼ
     * @param to   
     * @param val  ֵ
     * @throws IllegalArgumentException
     * @throws ArrayIndexOutOfBoundsException
     */
    public static void fill(short[] a, int from, int to,
                            short val)
    {
        rangeCheck(a.length, from, to);
        for (int i = from; i < to; i++) {
            a[i] = val;
        }
    }


    /**
     * @param a   
     * @param val ֵ
     */
    public static void fill(char[] a, char val)
    {
        int len = a.length;
        for (int i = 0; i < len; i++) {
            a[i] = val;
        }
    }

    /**
     * @param a    
     * @param from ʼ
     * @param to   
     * @param val  ֵ
     * @throws IllegalArgumentException
     * @throws ArrayIndexOutOfBoundsException
     */
    public static void fill(char[] a, int from, int to,
                            char val)
    {
        rangeCheck(a.length, from, to);
        for (int i = from; i < to; i++) {
            a[i] = val;
        }
    }


    /**
     * @param a   
     * @param val ֵ
     */
    public static void fill(byte[] a, byte val)
    {
        int len = a.length;
        for (int i = 0; i < len; i++) {
            a[i] = val;
        }
    }

    /**
     * @param a    
     * @param from ʼ
     * @param to   
     * @param val  ֵ
     * @throws IllegalArgumentException
     * @throws ArrayIndexOutOfBoundsException
     */
    public static void fill(byte[] a, int from, int to,
                            byte val)
    {
        rangeCheck(a.length, from, to);
        for (int i = from; i < to; i++) {
            a[i] = val;
        }
    }


    /**
     * @param a   
     * @param val ֵ
     */
    public static void fill(boolean[] a, boolean val)
    {
        int len = a.length;
        for (int i = 0; i < len; i++) {
            a[i] = val;
        }
    }

    /**
     * @param a    
     * @param from ʼ
     * @param to   
     * @param val  ֵ
     * @throws IllegalArgumentException
     * @throws ArrayIndexOutOfBoundsException
     */
    public static void fill(boolean[] a, int from, int to,
                            boolean val)
    {
        rangeCheck(a.length, from, to);
        for (int i = from; i < to; i++) {
            a[i] = val;
        }
    }

    /**
     * @param a   
     * @param val ֵ
     */
    public static void fill(double[] a, double val)
    {
        int len = a.length;
        for (int i = 0; i < len; i++) {
            a[i] = val;
        }
    }

    /**
     * @throws IllegalArgumentException
     * @throws ArrayIndexOutOfBoundsException
     */
    public static void fill(double[] a, int from, int to,
                            double val)
    {
        rangeCheck(a.length, from, to);
        for (int i = from; i < to; i++) {
            a[i] = val;
        }
    }

    /**
     * @param a   
     * @param val ֵ
     */
    public static void fill(float[] a, float val)
    {
        int len = a.length;
        for (int i = 0; i < len; i++) {
            a[i] = val;
        }
    }

    /**
     * @param a    
     * @param from ʼ
     * @param to   
     * @param val  ֵ
     * @throws IllegalArgumentException
     * @throws ArrayIndexOutOfBoundsException
     */
    public static void fill(float[] a, int from, int to,
                            float val)
    {
        rangeCheck(a.length, from, to);
        for (int i = from; i < to; i++) {
            a[i] = val;
        }
    }

    /**
     * @param a   
     * @param val ֵ
     */
    public static void fill(Object[] a, Object val)
    {
        int len = a.length;
        for (int i = 0; i < len; i++) {
            a[i] = val;
        }
    }

    /**
     * @throws IllegalArgumentException
     * @throws ArrayIndexOutOfBoundsException
     */
    public static void fill(Object[] a, int from, int to,
                            Object val)
    {
        rangeCheck(a.length, from, to);
        for (int i = from; i < to; i++) {
            a[i] = val;
        }
    }


    /**
     * Returns a hash code based on the contents of the specified array.
     * For any two <tt>long</tt> arrays <tt>a</tt> and <tt>b</tt>
     * such that <tt>Arrays.equals(a, b)</tt>, it is also the case that
     * <tt>Arrays.hashCode(a) == Arrays.hashCode(b)</tt>.
     * <p></p>
     * <p>The value returned by this method is the same value that would be
     * obtained by invoking the {@link List#hashCode() <tt>hashCode</tt>}
     * method on a {@link List} containing a sequence of {@link Long}
     * instances representing the elements of <tt>a</tt> in the same order.
     * If <tt>a</tt> is <tt>null</tt>, this method returns 0.
     *
     * @param a the array whose hash value to compute
     *          a content-based hash code for <tt>a</tt>
     * @since 1.5
     */
    public static int hashCode(long a[])
    {
        if (a == null) {
            return 0;
        }

        int result = 1;
        long element;
        for (int i = 0; i < a.length; i++) {
            element = a[i];

            int elementHash = (int)(element ^ (element >>> 32));
            result = 31 * result + elementHash;
        }

        return result;
    }

    /**
     * Returns a hash code based on the contents of the specified array.
     * For any two non-null <tt>int</tt> arrays <tt>a</tt> and <tt>b</tt>
     * such that <tt>Arrays.equals(a, b)</tt>, it is also the case that
     * <tt>Arrays.hashCode(a) == Arrays.hashCode(b)</tt>.
     * <p></p>
     * <p>The value returned by this method is the same value that would be
     * obtained by invoking the {@link List#hashCode() <tt>hashCode</tt>}
     * method on a {@link List} containing a sequence of {@link Integer}
     * instances representing the elements of <tt>a</tt> in the same order.
     * If <tt>a</tt> is <tt>null</tt>, this method returns 0.
     *
     * @param a the array whose hash value to compute
     *          a content-based hash code for <tt>a</tt>
     * @since 1.5
     */
    public static int hashCode(int a[])
    {
        if (a == null) {
            return 0;
        }

        int result = 1;
        int element;
        for (int i = 0; i < a.length; i++) {
            element = a[i];

            result = 31 * result + element;
        }

        return result;
    }

    /**
     * Returns a hash code based on the contents of the specified array.
     * For any two <tt>short</tt> arrays <tt>a</tt> and <tt>b</tt>
     * such that <tt>Arrays.equals(a, b)</tt>, it is also the case that
     * <tt>Arrays.hashCode(a) == Arrays.hashCode(b)</tt>.
     * <p></p>
     * <p>The value returned by this method is the same value that would be
     * obtained by invoking the {@link List#hashCode() <tt>hashCode</tt>}
     * method on a {@link List} containing a sequence of {@link Short}
     * instances representing the elements of <tt>a</tt> in the same order.
     * If <tt>a</tt> is <tt>null</tt>, this method returns 0.
     *
     * @param a the array whose hash value to compute
     *          a content-based hash code for <tt>a</tt>
     * @since 1.5
     */
    public static int hashCode(short a[])
    {
        if (a == null) {
            return 0;
        }

        int result = 1;
        short element;
        for (int i = 0; i < a.length; i++) {
            element = a[i];

            result = 31 * result + element;
        }

        return result;
    }

    /**
     * Returns a hash code based on the contents of the specified array.
     * For any two <tt>char</tt> arrays <tt>a</tt> and <tt>b</tt>
     * such that <tt>Arrays.equals(a, b)</tt>, it is also the case that
     * <tt>Arrays.hashCode(a) == Arrays.hashCode(b)</tt>.
     * <p></p>
     * <p>The value returned by this method is the same value that would be
     * obtained by invoking the {@link List#hashCode() <tt>hashCode</tt>}
     * method on a {@link List} containing a sequence of {@link Character}
     * instances representing the elements of <tt>a</tt> in the same order.
     * If <tt>a</tt> is <tt>null</tt>, this method returns 0.
     *
     * @param a the array whose hash value to compute
     *          a content-based hash code for <tt>a</tt>
     * @since 1.5
     */
    public static int hashCode(char a[])
    {
        if (a == null) {
            return 0;
        }

        int result = 1;
        char element;
        for (int i = 0; i < a.length; i++) {
            element = a[i];

            result = 31 * result + element;
        }

        return result;
    }

    /**
     * Returns a hash code based on the contents of the specified array.
     * For any two <tt>byte</tt> arrays <tt>a</tt> and <tt>b</tt>
     * such that <tt>Arrays.equals(a, b)</tt>, it is also the case that
     * <tt>Arrays.hashCode(a) == Arrays.hashCode(b)</tt>.
     * <p></p>
     * <p>The value returned by this method is the same value that would be
     * obtained by invoking the {@link List#hashCode() <tt>hashCode</tt>}
     * method on a {@link List} containing a sequence of {@link Byte}
     * instances representing the elements of <tt>a</tt> in the same order.
     * If <tt>a</tt> is <tt>null</tt>, this method returns 0.
     *
     * @param a the array whose hash value to compute
     *          a content-based hash code for <tt>a</tt>
     * @since 1.5
     */
    public static int hashCode(byte a[])
    {
        if (a == null) {
            return 0;
        }

        int result = 1;
        byte element;
        for (int i = 0; i < a.length; i++) {
            element = a[i];

            result = 31 * result + element;
        }

        return result;
    }

    /**
     * Returns a hash code based on the contents of the specified array.
     * For any two <tt>boolean</tt> arrays <tt>a</tt> and <tt>b</tt>
     * such that <tt>Arrays.equals(a, b)</tt>, it is also the case that
     * <tt>Arrays.hashCode(a) == Arrays.hashCode(b)</tt>.
     * <p></p>
     * <p>The value returned by this method is the same value that would be
     * obtained by invoking the {@link List#hashCode() <tt>hashCode</tt>}
     * method on a {@link List} containing a sequence of {@link Boolean}
     * instances representing the elements of <tt>a</tt> in the same order.
     * If <tt>a</tt> is <tt>null</tt>, this method returns 0.
     *
     * @param a the array whose hash value to compute
     *          a content-based hash code for <tt>a</tt>
     * @since 1.5
     */
    public static int hashCode(boolean a[])
    {
        if (a == null) {
            return 0;
        }

        int result = 1;
        boolean element;
        for (int i = 0; i < a.length; i++) {
            element = a[i];
            result = 31 * result + (element ? 1231 : 1237);
        }

        return result;
    }

    /**
     * Returns a hash code based on the contents of the specified array.
     * For any two <tt>float</tt> arrays <tt>a</tt> and <tt>b</tt>
     * such that <tt>Arrays.equals(a, b)</tt>, it is also the case that
     * <tt>Arrays.hashCode(a) == Arrays.hashCode(b)</tt>.
     * <p></p>
     * <p>The value returned by this method is the same value that would be
     * obtained by invoking the {@link List#hashCode() <tt>hashCode</tt>}
     * method on a {@link List} containing a sequence of {@link Float}
     * instances representing the elements of <tt>a</tt> in the same order.
     * If <tt>a</tt> is <tt>null</tt>, this method returns 0.
     *
     * @param a the array whose hash value to compute
     *          a content-based hash code for <tt>a</tt>
     * @since 1.5
     */
    public static int hashCode(float a[])
    {
        if (a == null) {
            return 0;
        }

        int result = 1;
        float element;
        for (int i = 0; i < a.length; i++) {
            element = a[i];
            result = 31 * result + Float.floatToIntBits(element);
        }

        return result;
    }

    /**
     * Returns a hash code based on the contents of the specified array.
     * For any two <tt>double</tt> arrays <tt>a</tt> and <tt>b</tt>
     * such that <tt>Arrays.equals(a, b)</tt>, it is also the case that
     * <tt>Arrays.hashCode(a) == Arrays.hashCode(b)</tt>.
     * <p></p>
     * <p>The value returned by this method is the same value that would be
     * obtained by invoking the {@link List#hashCode() <tt>hashCode</tt>}
     * method on a {@link List} containing a sequence of {@link Double}
     * instances representing the elements of <tt>a</tt> in the same order.
     * If <tt>a</tt> is <tt>null</tt>, this method returns 0.
     *
     * @param a the array whose hash value to compute
     *          a content-based hash code for <tt>a</tt>
     * @since 1.5
     */
    public static int hashCode(double a[])
    {
        if (a == null) {
            return 0;
        }

        int result = 1;
        double element;
        for (int i = 0; i < a.length; i++) {
            element = a[i];
            long bits = Double.doubleToLongBits(element);
            result = 31 * result + (int)(bits ^ (bits >>> 32));
        }
        return result;
    }

    /**
     * Returns a hash code based on the contents of the specified array.  If
     * the array contains other arrays as elements, the hash code is based on
     * their identities rather than their contents.  It is therefore
     * acceptable to invoke this method on an array that contains itself as an
     * element,  either directly or indirectly through one or more levels of
     * arrays.
     * <p></p>
     * <p>For any two arrays <tt>a</tt> and <tt>b</tt> such that
     * <tt>Arrays.equals(a, b)</tt>, it is also the case that
     * <tt>Arrays.hashCode(a) == Arrays.hashCode(b)</tt>.
     * <p></p>
     * <p>The value returned by this method is equal to the value that would
     * be returned by <tt>Arrays.asList(a).hashCode()</tt>, unless <tt>a</tt>
     * is <tt>null</tt>, in which case <tt>0</tt> is returned.
     *
     * @param a the array whose content-based hash code to compute
     *          a content-based hash code for <tt>a</tt>
     * @see #deepHashCode(Object[])
     * @since 1.5
     */
    public static int hashCode(Object a[])
    {
        if (a == null) {
            return 0;
        }

        int result = 1;

        Object element;
        for (int i = 0; i < a.length; i++) {
            element = a[i];
            result = 31 * result + (element == null ? 0 : element.hashCode());
        }

        return result;
    }

    /**
     * Returns a hash code based on the "deep contents" of the specified
     * array.  If the array contains other arrays as elements, the
     * hash code is based on their contents and so on, ad infinitum.
     * It is therefore unacceptable to invoke this method on an array that
     * contains itself as an element, either directly or indirectly through
     * one or more levels of arrays.  The behavior of such an invocation is
     * undefined.
     * <p></p>
     * <p>For any two arrays <tt>a</tt> and <tt>b</tt> such that
     * <tt>Arrays.deepEquals(a, b)</tt>, it is also the case that
     * <tt>Arrays.deepHashCode(a) == Arrays.deepHashCode(b)</tt>.
     * <p></p>
     * <p>The computation of the value returned by this method is similar to
     * that of the value returned by {@link List#hashCode()} on a list
     * containing the same elements as <tt>a</tt> in the same order, with one
     * difference: If an element <tt>e</tt> of <tt>a</tt> is itself an array,
     * its hash code is computed not by calling <tt>e.hashCode()</tt>, but as
     * by calling the appropriate overloading of <tt>Arrays.hashCode(e)</tt>
     * if <tt>e</tt> is an array of a primitive type, or as by calling
     * <tt>Arrays.deepHashCode(e)</tt> recursively if <tt>e</tt> is an array
     * of a reference type.  If <tt>a</tt> is <tt>null</tt>, this method
     * returns 0.
     *
     * @param a the array whose deep-content-based hash code to compute
     *          a deep-content-based hash code for <tt>a</tt>
     * @see #hashCode(Object[])
     * @since 1.5
     */
    public static int deepHashCode(Object a[])
    {
        if (a == null) {
            return 0;
        }

        int result = 1;

        Object element;
        for (int i = 0; i < a.length; i++) {
            element = a[i];
            int elementHash = 0;
            if (element instanceof Object[]) {
                elementHash = deepHashCode((Object[])element);
            }
            else if (element instanceof byte[]) {
                elementHash = hashCode((byte[])element);
            }
            else if (element instanceof short[]) {
                elementHash = hashCode((short[])element);
            }
            else if (element instanceof int[]) {
                elementHash = hashCode((int[])element);
            }
            else if (element instanceof long[]) {
                elementHash = hashCode((long[])element);
            }
            else if (element instanceof char[]) {
                elementHash = hashCode((char[])element);
            }
            else if (element instanceof float[]) {
                elementHash = hashCode((float[])element);
            }
            else if (element instanceof double[]) {
                elementHash = hashCode((double[])element);
            }
            else if (element instanceof boolean[]) {
                elementHash = hashCode((boolean[])element);
            }
            else if (element != null) {
                elementHash = element.hashCode();
            }

            result = 31 * result + elementHash;
        }

        return result;
    }

    /**
     * Returns <tt>true</tt> if the two specified arrays are <i>deeply
     * equal</i> to one another.  Unlike the @link{#equals{Object[],Object[])
     * method, this method is appropriate for use with nested arrays of
     * arbitrary depth.
     * <p></p>
     * <p>Two array references are considered deeply equal if both
     * are <tt>null</tt>, or if they refer to arrays that contain the same
     * number of elements and all corresponding pairs of elements in the two
     * arrays are deeply equal.
     * <p></p>
     * <p>Two possibly <tt>null</tt> elements <tt>e1</tt> and <tt>e2</tt> are
     * deeply equal if any of the following conditions hold:
     * <ul>
     * <li> <tt>e1</tt> and <tt>e2</tt> are both arrays of object reference
     * types, and <tt>Arrays.deepEquals(e1, e2) would return true</tt>
     * <li> <tt>e1</tt> and <tt>e2</tt> are arrays of the same primitive
     * type, and the appropriate overloading of
     * <tt>Arrays.equals(e1, e2)</tt> would return true.
     * <li> <tt>e1 == e2</tt>
     * <li> <tt>e1.equals(e2)</tt> would return true.
     * </ul>
     * Note that this definition permits <tt>null</tt> elements at any depth.
     * <p></p>
     * <p>If either of the specified arrays contain themselves as elements
     * either directly or indirectly through one or more levels of arrays,
     * the behavior of this method is undefined.
     *
     * @param a1 one array to be tested for equality
     * @param a2 the other array to be tested for equality
     *           <tt>true</tt> if the two arrays are equal
     * @see #equals(Object[],Object[])
     * @since 1.5
     */
    public static boolean deepEquals(Object[] a1, Object[] a2)
    {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }
        int length = a1.length;
        if (a2.length != length) {
            return false;
        }

        for (int i = 0; i < length; i++) {
            Object e1 = a1[i];
            Object e2 = a2[i];

            if (e1 == e2) {
                continue;
            }
            if (e1 == null) {
                return false;
            }

            // Figure out whether the two elements are equal
            boolean eq;
            if (e1 instanceof Object[] && e2 instanceof Object[]) {
                eq = deepEquals((Object[])e1, (Object[])e2);
            }
            else if (e1 instanceof byte[] && e2 instanceof byte[]) {
                eq = equals((byte[])e1, (byte[])e2);
            }
            else if (e1 instanceof short[] && e2 instanceof short[]) {
                eq = equals((short[])e1, (short[])e2);
            }
            else if (e1 instanceof int[] && e2 instanceof int[]) {
                eq = equals((int[])e1, (int[])e2);
            }
            else if (e1 instanceof long[] && e2 instanceof long[]) {
                eq = equals((long[])e1, (long[])e2);
            }
            else if (e1 instanceof char[] && e2 instanceof char[]) {
                eq = equals((char[])e1, (char[])e2);
            }
            else if (e1 instanceof float[] && e2 instanceof float[]) {
                eq = equals((float[])e1, (float[])e2);
            }
            else if (e1 instanceof double[] && e2 instanceof double[]) {
                eq = equals((double[])e1, (double[])e2);
            }
            else if (e1 instanceof boolean[] && e2 instanceof boolean[]) {
                eq = equals((boolean[])e1, (boolean[])e2);
            }
            else {
                eq = e1.equals(e2);
            }

            if (!eq) {
                return false;
            }
        }
        return true;
    }


    /**
     * Returns a string representation of the contents of the specified array.
     * The string representation consists of a list of the array's elements,
     * enclosed in square brackets (<tt>"[]"</tt>).  Adjacent elements are
     * separated by the characters <tt>", "</tt> (a comma followed by a
     * space).  Elements are converted to strings as by
     * <tt>String.valueOf(long)</tt>.  Returns <tt>"null"</tt> if <tt>a</tt>
     * is <tt>null</tt>.
     *
     * @param a the array whose string representation to return
     *          a string representation of <tt>a</tt>
     * @since 1.5
     */
    public static String toString(long[] a)
    {
        if (a == null) {
            return "null";
        }
        if (a.length == 0) {
            return "[]";
        }

        StringBuilder buf = new StringBuilder();
        buf.append('[');
        buf.append(a[0]);

        for (int i = 1; i < a.length; i++) {
            buf.append(", ");
            buf.append(a[i]);
        }

        buf.append("]");
        return buf.toString();
    }

    /**
     * Returns a string representation of the contents of the specified array.
     * The string representation consists of a list of the array's elements,
     * enclosed in square brackets (<tt>"[]"</tt>).  Adjacent elements are
     * separated by the characters <tt>", "</tt> (a comma followed by a
     * space).  Elements are converted to strings as by
     * <tt>String.valueOf(int)</tt>.  Returns <tt>"null"</tt> if <tt>a</tt> is
     * <tt>null</tt>.
     *
     * @param a the array whose string representation to return
     *          a string representation of <tt>a</tt>
     * @since 1.5
     */
    public static String toString(int[] a)
    {
        if (a == null) {
            return "null";
        }
        if (a.length == 0) {
            return "[]";
        }

        StringBuilder buf = new StringBuilder();
        buf.append('[');
        buf.append(a[0]);

        for (int i = 1; i < a.length; i++) {
            buf.append(", ");
            buf.append(a[i]);
        }

        buf.append("]");
        return buf.toString();
    }

    /**
     * Int Array to String with split
     *
     * @param a
     * @param sep
     * @return
     */
    public static String toString(int[] a, char sep)
    {
        if (a == null || a.length == 0) {
            return "";
        }

        StringBuilder buf = new StringBuilder();
        buf.append(a[0]);

        for (int i = 1; i < a.length; i++) {
            buf.append(sep);
            buf.append(a[i]);
        }
        return buf.toString();
    }

    /**
     * To String
     *
     * @param a
     * @param sep
     * @return
     */
    public static String toString(String[] a, char sep)
    {
        if (a == null || a.length == 0) {
            return "";
        }

        StringBuilder buf = new StringBuilder();
        buf.append("'"+ a[0] + "'");

        for (int i = 1; i < a.length; i++) {
            buf.append(sep);
            buf.append("'"+ a[i] + "'");
        }
        return buf.toString();
    }



    /**
     * To String
     *
     * @param a
     * @param sep
     * @return
     */
    public static String toString(Object[] a, char sep)
    {
        if (a == null || a.length == 0) {
            return "";
        }

        StringBuilder buf = new StringBuilder();
        buf.append(a[0]);

        for (int i = 1; i < a.length; i++) {
            buf.append(sep);
            buf.append(a[i].toString());
        }
        return buf.toString();
    }

    /**
     * To String
     *
     * @param a
     * @param sep
     * @return
     */
    public static String toString(List a, char sep)
    {
        if (a == null || a.size() == 0) {
            return "";
        }

        StringBuilder buf = new StringBuilder();
        buf.append(a.get(0));

        int size = a.size();
        for (int i = 1; i < size; i++) {
            buf.append(sep);
            buf.append(a.get(i));
        }
        return buf.toString();
    }

    /**
     * Returns a string representation of the contents of the specified array.
     * The string representation consists of a list of the array's elements,
     * enclosed in square brackets (<tt>"[]"</tt>).  Adjacent elements are
     * separated by the characters <tt>", "</tt> (a comma followed by a
     * space).  Elements are converted to strings as by
     * <tt>String.valueOf(short)</tt>.  Returns <tt>"null"</tt> if <tt>a</tt>
     * is <tt>null</tt>.
     *
     * @param a the array whose string representation to return
     *          a string representation of <tt>a</tt>
     * @since 1.5
     */
    public static String toString(short[] a)
    {
        if (a == null) {
            return "null";
        }
        if (a.length == 0) {
            return "[]";
        }

        StringBuilder buf = new StringBuilder();
        buf.append('[');
        buf.append(a[0]);

        for (int i = 1; i < a.length; i++) {
            buf.append(", ");
            buf.append(a[i]);
        }

        buf.append("]");
        return buf.toString();
    }

    /**
     * Returns a string representation of the contents of the specified array.
     * The string representation consists of a list of the array's elements,
     * enclosed in square brackets (<tt>"[]"</tt>).  Adjacent elements are
     * separated by the characters <tt>", "</tt> (a comma followed by a
     * space).  Elements are converted to strings as by
     * <tt>String.valueOf(char)</tt>.  Returns <tt>"null"</tt> if <tt>a</tt>
     * is <tt>null</tt>.
     *
     * @param a the array whose string representation to return
     *          a string representation of <tt>a</tt>
     * @since 1.5
     */
    public static String toString(char[] a)
    {
        if (a == null) {
            return "null";
        }
        if (a.length == 0) {
            return "[]";
        }

        StringBuilder buf = new StringBuilder();
        buf.append('[');
        buf.append(a[0]);

        for (int i = 1; i < a.length; i++) {
            buf.append(", ");
            buf.append(a[i]);
        }

        buf.append("]");
        return buf.toString();
    }

    /**
     * Returns a string representation of the contents of the specified array.
     * The string representation consists of a list of the array's elements,
     * enclosed in square brackets (<tt>"[]"</tt>).  Adjacent elements
     * are separated by the characters <tt>", "</tt> (a comma followed
     * by a space).  Elements are converted to strings as by
     * <tt>String.valueOf(byte)</tt>.  Returns <tt>"null"</tt> if
     * <tt>a</tt> is <tt>null</tt>.
     *
     * @param a the array whose string representation to return
     *          a string representation of <tt>a</tt>
     * @since 1.5
     */
    public static String toString(byte[] a)
    {
        if (a == null) {
            return "null";
        }
        if (a.length == 0) {
            return "[]";
        }

        StringBuilder buf = new StringBuilder();
        buf.append('[');
        buf.append(a[0]);

        for (int i = 1; i < a.length; i++) {
            buf.append(", ");
            buf.append(a[i]);
        }

        buf.append("]");
        return buf.toString();
    }

    /**
     * Returns a string representation of the contents of the specified array.
     * The string representation consists of a list of the array's elements,
     * enclosed in square brackets (<tt>"[]"</tt>).  Adjacent elements are
     * separated by the characters <tt>", "</tt> (a comma followed by a
     * space).  Elements are converted to strings as by
     * <tt>String.valueOf(boolean)</tt>.  Returns <tt>"null"</tt> if
     * <tt>a</tt> is <tt>null</tt>.
     *
     * @param a the array whose string representation to return
     *          a string representation of <tt>a</tt>
     * @since 1.5
     */
    public static String toString(boolean[] a)
    {
        if (a == null) {
            return "null";
        }
        if (a.length == 0) {
            return "[]";
        }

        StringBuilder buf = new StringBuilder();
        buf.append('[');
        buf.append(a[0]);

        for (int i = 1; i < a.length; i++) {
            buf.append(", ");
            buf.append(a[i]);
        }

        buf.append("]");
        return buf.toString();
    }

    /**
     * Returns a string representation of the contents of the specified array.
     * The string representation consists of a list of the array's elements,
     * enclosed in square brackets (<tt>"[]"</tt>).  Adjacent elements are
     * separated by the characters <tt>", "</tt> (a comma followed by a
     * space).  Elements are converted to strings as by
     * <tt>String.valueOf(float)</tt>.  Returns <tt>"null"</tt> if <tt>a</tt>
     * is <tt>null</tt>.
     *
     * @param a the array whose string representation to return
     *          a string representation of <tt>a</tt>
     * @since 1.5
     */
    public static String toString(float[] a)
    {
        if (a == null) {
            return "null";
        }
        if (a.length == 0) {
            return "[]";
        }

        StringBuilder buf = new StringBuilder();
        buf.append('[');
        buf.append(a[0]);

        for (int i = 1; i < a.length; i++) {
            buf.append(", ");
            buf.append(a[i]);
        }

        buf.append("]");
        return buf.toString();
    }

    /**
     * Returns a string representation of the contents of the specified array.
     * The string representation consists of a list of the array's elements,
     * enclosed in square brackets (<tt>"[]"</tt>).  Adjacent elements are
     * separated by the characters <tt>", "</tt> (a comma followed by a
     * space).  Elements are converted to strings as by
     * <tt>String.valueOf(double)</tt>.  Returns <tt>"null"</tt> if <tt>a</tt>
     * is <tt>null</tt>.
     *
     * @param a the array whose string representation to return
     *          a string representation of <tt>a</tt>
     * @since 1.5
     */
    public static String toString(double[] a)
    {
        if (a == null) {
            return "null";
        }
        if (a.length == 0) {
            return "[]";
        }

        StringBuilder buf = new StringBuilder();
        buf.append('[');
        buf.append(a[0]);

        for (int i = 1; i < a.length; i++) {
            buf.append(", ");
            buf.append(a[i]);
        }

        buf.append("]");
        return buf.toString();
    }

    /**
     * Returns a string representation of the contents of the specified array.
     * If the array contains other arrays as elements, they are converted to
     * strings by the {@link Object#toString} method inherited from
     * <tt>Object</tt>, which describes their <i>identities</i> rather than
     * their contents.
     * <p></p>
     * <p>The value returned by this method is equal to the value that would
     * be returned by <tt>Arrays.asList(a).toString()</tt>, unless <tt>a</tt>
     * is <tt>null</tt>, in which case <tt>"null"</tt> is returned.
     *
     * @param a the array whose string representation to return
     *          a string representation of <tt>a</tt>
     * @see #deepToString(Object[])
     * @since 1.5
     */
    public static String toString(Object[] a)
    {
        if (a == null) {
            return "null";
        }
        if (a.length == 0) {
            return "[]";
        }

        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < a.length; i++) {
            if (i == 0) {
                buf.append('[');
            }
            else {
                buf.append(", ");
            }

            buf.append(String.valueOf(a[i]));
        }

        buf.append("]");
        return buf.toString();
    }

    /**
     * Returns a string representation of the "deep contents" of the specified
     * array.  If the array contains other arrays as elements, the string
     * representation contains their contents and so on.  This method is
     * designed for converting multidimensional arrays to strings.
     * <p></p>
     * <p>The string representation consists of a list of the array's
     * elements, enclosed in square brackets (<tt>"[]"</tt>).  Adjacent
     * elements are separated by the characters <tt>", "</tt> (a comma
     * followed  by a space).  Elements are converted to strings as by
     * <tt>String.valueOf(Object)</tt>, unless they are themselves
     * arrays.
     * <p></p>
     * <p>If an element <tt>e</tt> is an array of a primitive type, it is
     * converted to a string as by invoking the appropriate overloading of
     * <tt>Arrays.toString(e)</tt>.  If an element <tt>e</tt> is an array of a
     * reference type, it is converted to a string as by invoking
     * this method recursively.
     * <p></p>
     * <p>To avoid infinite recursion, if the specified array contains itself
     * as an element, or contains an indirect reference to itself through one
     * or more levels of arrays, the self-reference is converted to the string
     * <tt>"[...]"</tt>.  For example, an array containing only a reference
     * to itself would be rendered as <tt>"[[...]]"</tt>.
     * <p></p>
     * <p>This method returns <tt>"null"</tt> if the specified array
     * is <tt>null</tt>.
     *
     * @param a the array whose string representation to return
     *          a string representation of <tt>a</tt>
     * @see #toString(Object[])
     * @since 1.5
     */
    public static String deepToString(Object[] a)
    {
        if (a == null) {
            return "null";
        }

        int bufLen = 20 * a.length;
        if (a.length != 0 && bufLen <= 0) {
            bufLen = Integer.MAX_VALUE;
        }
        StringBuilder buf = new StringBuilder(bufLen);
        deepToString(a, buf, new HashSet());
        return buf.toString();
    }

    private static void deepToString(Object[] a, StringBuilder buf, Set dejaVu)
    {
        if (a == null) {
            buf.append("null");
            return;
        }
        dejaVu.add(a);
        buf.append('[');
        for (int i = 0; i < a.length; i++) {
            if (i != 0) {
                buf.append(", ");
            }

            Object element = a[i];
            if (element == null) {
                buf.append("null");
            }
            else {
                Class eClass = element.getClass();

                if (eClass.isArray()) {
                    if (eClass == byte[].class) {
                        buf.append(toString((byte[])element));
                    }
                    else if (eClass == short[].class) {
                        buf.append(toString((short[])element));
                    }
                    else if (eClass == int[].class) {
                        buf.append(toString((int[])element));
                    }
                    else if (eClass == long[].class) {
                        buf.append(toString((long[])element));
                    }
                    else if (eClass == char[].class) {
                        buf.append(toString((char[])element));
                    }
                    else if (eClass == float[].class) {
                        buf.append(toString((float[])element));
                    }
                    else if (eClass == double[].class) {
                        buf.append(toString((double[])element));
                    }
                    else if (eClass == boolean[].class) {
                        buf.append(toString((boolean[])element));
                    }
                    else { // element is an array of object references
                        if (dejaVu.contains(element)) {
                            buf.append("[...]");
                        }
                        else {
                            deepToString((Object[])element, buf, dejaVu);
                        }
                    }
                }
                else {  // element is non-null and not an array
                    buf.append(element.toString());
                }
            }
        }
        buf.append("]");
        dejaVu.remove(a);
    }


}
