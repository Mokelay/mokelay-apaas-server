package com.mokelay.base.util.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Char��ص���ݽӿ�����
 *
 * @author system
 * @version 1.00 2005-6-2 1:27:43
 * @see ByteArrayOutputStream
 */
public interface ByteData
{
    /**
     * ������ݴ�С
     *
     * @return
     */
    public int size();

    /**
     * Writes the complete contents of this byte array output stream to
     * the specified output stream argument, as if by calling the output
     * stream's write method using <code>out.write(buf, 0, count)</code>.
     *
     * @param out the output stream to which to write the data.
     * @throws IOException if an I/O error occurs.
     */
    public void writeTo(OutputStream out)
        throws IOException;
}
