package com.mokelay.base.util.io;

/**
 * A <code>ByteArrayInputStream</code> contains
 * an internal buffer that contains bytes that
 * may be read from the stream. An internal
 * counter keeps track of the next byte to
 * be supplied by the <code>read</code> method.
 */
class ByteArrayInputStream extends java.io.ByteArrayInputStream
{
    /**
     * Creates a <code>ByteArrayInputStream</code>
     * so that it  uses <code>buf</code> as its
     * buffer array.
     * The buffer array is not copied.
     * The initial value of <code>pos</code>
     * is <code>0</code> and the initial value
     * of  <code>count</code> is the length of
     * <code>buf</code>.
     *
     * @param buf the input buffer.
     */
    ByteArrayInputStream(byte buf[])
    {
        super(buf);
    }

    /**
     * Creates <code>ByteArrayInputStream</code>
     * that uses <code>buf</code> as its
     * buffer array. The initial value of <code>pos</code>
     * is <code>offset</code> and the initial value
     * of <code>count</code> is <code>offset+len</code>.
     * The buffer array is not copied.
     * <p/>
     * Note that if bytes are simply read from
     * the resulting input stream, elements <code>buf[pos]</code>
     * through <code>buf[pos+len-1]</code> will
     * be read; however, if a <code>reset</code>
     * operation  is performed, then bytes <code>buf[0]</code>
     * through b<code>uf[pos-1]</code> will then
     * become available for input.
     *
     * @param buf    the input buffer.
     * @param offset the offset in the buffer of the first byte to read.
     * @param length the maximum number of bytes to read from the buffer.
     */
    ByteArrayInputStream(byte buf[], int offset, int length)
    {
        super(buf, offset, length);
    }

//    /**
//     * ���ص�ǰ��Bytes
//     *
//     * @return
//     */
//    public byte[] getBytes()
//    {
//        return buf;
//    }
//
//    /**
//     * �����������
//     *
//     * @return
//     */
//    public int getCount()
//    {
//        return count;
//    }

    /**
     * ����
     */
    protected void setBytes(byte[] buf)
    {
        this.buf = buf;
    }

    /**
     * ��������
     */
    protected void setCount(int count)
    {
        this.count = count;
    }
}
