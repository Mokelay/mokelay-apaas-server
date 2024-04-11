package com.mokelay.base.util;

import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author system
 * @version 1.00 Mar 24, 2006 2:45:51 PM
 */
public abstract class AbstractStringMap<V>
    implements StringMap<V>
{
    /**
     *
     * @param map [StringMap]
     */
    public void putAll(StringMap<? extends V> map)
    {
        for (Entry<? extends V> e : map.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    /**
     *
     * @param map [StringMap]
     */
    public void putAll(Map<String, ? extends V> map)
    {
        for (Map.Entry<String, ? extends V> e : map.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    /**
     */
    public boolean isEmpty()
    {
        return size() == 0;
    }

    /**
     */
    public Iterator<String> getKeys()
    {
        return keySet().iterator();
    }

    /**
     */
    public Iterator<V> getValues()
    {
        return valueSet().iterator();
    }

    public Iterator<Entry<V>> getEntries()
    {
        return entrySet().iterator();
    }
}
