package com.greatbee.base.util;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;


/**
 *
 * @author system
 * @version 1.02 Mar 4, 2004 14:42 PM Fixed ListIterator behavior
 * @see ListIterator
 * @see Enumeration
 */
public class ArrayIterator<V>
    implements ListIterator<V>, Iterator<V>, Enumeration<V>
{
    /**
     */
    protected int off;

    /**
     */
    protected int end;

    /**
     */
    protected int index;

    /**
     *
     */
    protected V[] array;

    /**
     *
     */
    protected ArrayIterator(V[] array)
    {
        this(array, 0, array.length);
    }

    /**
     */
    protected ArrayIterator(V[] array, int off, int len)
    {
        if (array == null) {
            throw new NullPointerException("Array is null");
        }

        if (off < 0) {
            throw new ArrayIndexOutOfBoundsException("Off=" + off);
        }
        if (len < 0) {
            throw new ArrayIndexOutOfBoundsException("Len=" + len);
        }
        if (off > array.length - len) {
            throw new ArrayIndexOutOfBoundsException("Invalid off=" + off + " or len=" + len);
        }
        this.array = array;
        this.off = off;
        this.index = off;
        this.end = off + len;
    }

    /**
     */
    public boolean hasNext()
    {
        return index < end;
    }


    /**
     */
    public V next()
    {
        if (index < end) {
            return array[index++];
        }
        throw new NoSuchElementException("No element left");
    }

    /**
     */
    public boolean hasPrevious()
    {
        return index > off;
    }

    /**
     */
    public V previous()
    {
        if (index > off) {
            return array[--index];
        }
        throw new NoSuchElementException("No element");
    }

    /**
     */
    public int nextIndex()
    {
        return index;
    }

    /**
     */
    public int previousIndex()
    {
        return index - 1;
    }


    /**
     */
    public void remove()
    {
        if (index >= off && index <= end) {
            array[index] = null;
        }
        throw new IllegalStateException("Invalid Index");
    }

    /**
     */
    public void set(V obj)
    {
        if (index >= off && index <= end) {
            array[index] = obj;
        }
        throw new IllegalStateException("Invalid Index");
    }

    /**
     */
    public void add(V o)
    {
        throw new UnsupportedOperationException("Unsupported");
    }

    /**
     */
    public boolean hasMoreElements()
    {
        return hasNext();
    }

    /**
     */
    public V nextElement()
    {
        return next();
    }
}

