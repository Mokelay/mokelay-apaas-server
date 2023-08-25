package com.greatbee.base.util;


import java.util.Enumeration;
import java.util.Iterator;

/**
 * @see Iterator
 * @see Enumeration
 */
public class EnumIterator<V> implements Iterator<V>, Enumeration<V>
{
    /**
     */
    private Iterator<V> iterator;

    /**
     * ���оٹ���
     *
     * @param en �о�
     */
    protected EnumIterator(Enumeration<V> en)
    {
        this.iterator = new IteratorImpl<V>(en);
    }

    /**
     * ��ö�ٹ���
     *
     * @param iterator ö��
     */
    protected EnumIterator(Iterator<V> iterator)
    {
        if (iterator == null) {
            throw new NullPointerException("Null Iterator");
        }

        this.iterator = iterator;
    }

    private static class IteratorImpl<V>
        implements Iterator<V>
    {
        /**
         * �о�
         */
        private Enumeration<V> en;

        IteratorImpl(Enumeration<V> en)
        {
            if (en == null) {
                throw new NullPointerException("Null Enumeration");
            }

            this.en = en;
        }

        public boolean hasNext()
        {
            return en.hasMoreElements();
        }

        public V next()
        {
            return en.nextElement();
        }

        public void remove()
        {
        }
    }

    public boolean hasNext()
    {
        return iterator.hasNext();
    }

    public V next()
    {
        return iterator.next();
    }

    public void remove()
    {
        iterator.remove();
    }

    public boolean hasMoreElements()
    {
        return iterator.hasNext();
    }

    public V nextElement()
    {
        return iterator.next();
    }
}
