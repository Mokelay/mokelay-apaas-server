package com.greatbee.base.util.io;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * �ɴ��л���<br>
 *
 * @author system
 * @version 1.00 Mar 12, 2002 3:49:44 PM
 * @see Serializable
 */
public interface Marshallable
    extends Serializable
{
    /**
     * ���л�
     *
     * @param oos ���������
     * @throws IOException ������I/O�쳣ʱ
     */
    public void marshal(ObjectOutputStream oos)
        throws IOException;

    /**
     * �����л�
     *
     * @param ois ����������
     * @throws IOException            ������I/O�쳣ʱ
     * @throws ClassNotFoundException
     */
    public void demarshal(ObjectInputStream ois)
        throws IOException, ClassNotFoundException;
}
