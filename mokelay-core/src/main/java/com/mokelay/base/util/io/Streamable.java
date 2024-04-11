package com.mokelay.base.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * ��������������Ķ�����������ӿ�
 *
 * @author system
 * @version 1.00 2005-6-4 20:58:08
 */
public interface Streamable
{
    /**
     * ���л�
     *
     * @param oos �����
     * @throws IOException ������I/O�쳣ʱ
     */
    public void writeTo(OutputStream oos)
        throws IOException;

    /**
     * �����л�
     *
     * @param ois ����
     * @throws IOException            ������I/O�쳣ʱ
     * @throws ClassNotFoundException
     */
    public void readFrom(InputStream ois)
        throws IOException, ClassNotFoundException;
}
