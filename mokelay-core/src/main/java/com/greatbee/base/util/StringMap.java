package com.greatbee.base.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public interface StringMap<V>
{
    /**
     * ���ز���
     *
     * @param key [String]
     * @return [Object]
     */
    public V get(String key);

    /**
     * �����ַ����
     *
     * @param key [String]
     * @return [String]
     */
    public String getString(String key);

    /**
     * �����ַ����
     *
     * @param key      [String]
     * @param defValue [String];
     * @return [String] ����
     */
    public String getString(String key, String defValue);

    /**
     * ���ò���
     *
     * @param key   [String]
     * @param value [Object]
     */
    public V put(String key, V value);


    /**
     * ����<code>StringMap</code>�����еĲ���
     *
     * @param map [StringMap]
     */
    public void putAll(StringMap<? extends V> map);

    /**
     * ����<code>StringMap</code>�����еĲ���
     *
     * @param map [StringMap]
     */
    public void putAll(Map<String, ? extends V> map);

    /**
     * �Ƿ�ӵ�и�ļ�
     *
     * @param key [String]
     */

    public boolean containsKey(String key);

    /**
     * �Ƿ�ӵ�и��ֵ
     *
     * @param value [Object]
     */
    public boolean containsValue(Object value);


    /**
     * ������еĲ���
     */
    public void clear();

    /**
     * ɾ�����ֵ
     *
     * @param key [String]
     * @return [Object]
     */
    public V remove(String key);

    /**
     * �Ƿ�Ϊ��
     */
    public boolean isEmpty();

    /**
     * ��Ŀ
     */
    public int size();

    /**
     * �������м�
     */
    public Iterator<String> getKeys();

    /**
     * ��������ֵ
     */
    public Iterator<V> getValues();

    /**
     */
    public Iterator<Entry<V>> getEntries();

    /**
     * �������м�
     */

    public Set<String> keySet();

    /**
     * ��������ֵ
     */
    public Collection<V> valueSet();

    /**
     * ����Entry�ļ���
     */
    public Set<Entry<V>> entrySet();


    public interface Entry<V>
    {
        String getKey();

        V getValue();

        V setValue(V value);

        boolean equals(Object o);

        int hashCode();
    }
}
