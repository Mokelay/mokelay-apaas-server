package com.mokelay.base.util.lang;

import com.mokelay.base.util.io.Marshallable;

/**
 * ����ʵ��<code>Marshallable</code>����<br>
 * ����������췽��(void����)�ĸ�����<br>
 *
 * @see Marshallable ������췽�����ڲ��Ǳ�ѡ�Ĺ��췽��
 */
public final class Empty
{
    private Empty()
    {
    }

    /**
     * ����
     */
    public static final Class TYPE = Empty.class;

    /**
     * ��ʵ��
     */
    public static final Empty EMPTY = new Empty();

    /**
     * ������
     */
    public static final Class[] EMPTY_CLASS = new Class[]{TYPE};

    /**
     * �������
     */
    public static final Object[] EMPTY_ARRAY = new Object[]{EMPTY};
}
