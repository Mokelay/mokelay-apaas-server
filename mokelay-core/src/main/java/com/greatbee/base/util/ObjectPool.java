package com.greatbee.base.util;

/**
 * �̶���С�Ķ����
 *
 * @author system
 * @version 1.0
 */
public class ObjectPool<V>
{
    /**
     * Ĭ�ϳش�С��128��
     */
    public static final int DEFAULT_POOL_SIZE = 128;

    /**
     * ��Ŷ�������飨����أ�
     */
    private V pool[];

    /**
     * ��������ֵ
     */
    private int max;

    /**
     * ����ض�λ��
     */
    private int maxOffset;

    /**
     * ջ��λ��
     */
    private int current = -1;

    /**
     * ������
     */
    private Object lock;

    /**
     * ����Ĭ�ϴ�С�Ķ����
     */
    public ObjectPool()
    {
        this(DEFAULT_POOL_SIZE);
    }

    /**
     * ������СΪmax�Ķ����
     *
     * @param max �ش�С
     */
    public ObjectPool(int max)
    {
        this.max = max;
        this.maxOffset = max - 1;
        pool = (V[])new Object[max];
        lock = new Object();
    }

    /**
     * �Ѷ���o����������
     *
     * @param o
     */
    public void put(V o)
    {
        synchronized (lock) {
            if (current < maxOffset) {
                current += 1;
                pool[current] = o;
            }
        }
    }

    /**
     * �Ӷ������ȡ������
     */
    public V get()
    {
        V item = null;
        synchronized (lock) {
            if (current >= 0) {
                item = pool[current];
                current -= 1;
            }
        }
        return item;
    }

    /**
     * �Ƿ�Ϊ�ն����
     *
     * @return boolean
     */
    public boolean isEmpty()
    {
        return size() == 0;
    }

    /**
     * ȡ�ö���ش�С
     *
     * @return int
     */
    public int size()
    {
        return max;
    }
}
