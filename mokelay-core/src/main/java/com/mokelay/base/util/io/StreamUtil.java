package com.mokelay.base.util.io;

import com.mokelay.base.util.StringUtil;

import java.io.*;
import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * �����?����
 *
 * @author system
 * @version 1.00 2005-2-6 21:31:31
 */
public class StreamUtil
{
    protected static final int BYTE_BUFFER_SIZE = 4096;
    protected static final int CHAR_BUFFER_SIZE = 4096;

    protected StreamUtil()
    {
    }

    private static ThreadLocal<SoftReference<ByteBuffer>> byteLocal = new ThreadLocal<SoftReference<ByteBuffer>>();
    private static ThreadLocal<SoftReference<CharBuffer>> charLocal = new ThreadLocal<SoftReference<CharBuffer>>();

    /**
     * ���ش�СΪ4K�Ļ����
     * ע�⣺ͨ��StreamUtil#recycle(ByteBuffer)�ķ�ʽ����
     *
     * @return ByteBuffer
     */
    protected static ByteBuffer getByteBuffer()
    {
        //�ĳ�ÿ���߳�һ��Buffer���ӿ��˻�ȡ�ٶ�
        SoftReference<ByteBuffer> soft = byteLocal.get();
        ByteBuffer buffer = null;
        if (soft == null || (buffer = (ByteBuffer) soft.get()) == null) {
            buffer = ByteBuffer.allocate(BYTE_BUFFER_SIZE);
            soft = new SoftReference<ByteBuffer>(buffer);
            byteLocal.set(soft);
        }
        buffer.clear();
        return buffer;
    }

    /**
     * ���ش�СΪ4K�Ļ����
     * ע�⣺ͨ��StreamUtil#recycle(CharBuffer)�ķ�ʽ����
     *
     * @return CharBuffer
     */
    protected static CharBuffer getCharBuffer()
    {
        SoftReference<CharBuffer> soft = charLocal.get();
        CharBuffer buffer = null;
        if (soft == null || (buffer = (CharBuffer) soft.get()) == null) {
            buffer = CharBuffer.allocate(CHAR_BUFFER_SIZE);
            soft = new SoftReference<CharBuffer>(buffer);
            charLocal.set(soft);
        }
        buffer.clear();
        return buffer;
    }

    /**
     * ��������������ȫ��������д���������
     *
     * @param input  ������
     * @param output �����
     */
    public static final int streamCopy(InputStream input,
                                       OutputStream output)
        throws IOException
    {
        return streamCopy(input, output, -1);
    }

    /**
     * ��������������ȫ��������д���������
     *
     * @param input  ������
     * @param output �����
     * @param buf    ������
     */
    public static final int streamCopy(InputStream input,
                                       OutputStream output,
                                       byte[] buf)
        throws IOException
    {
        return streamCopy(input, output, -1, buf);
    }

    /**
     * ����������ͷ��ȡ���ȵ����д���������
     *
     * @param input  ������
     * @param output �����
     * @param size   ��ĳ���
     */
    public static final int streamCopy(InputStream input,
                                       OutputStream output,
                                       int size)
        throws IOException
    {
        if (size < 0) {
            size = Integer.MAX_VALUE;
        }
        if (size == 0) {
            return 0;
        }
        ByteBuffer buffer = getByteBuffer();
        byte[] buf = buffer.array();
        return streamCopy0(input, output, size, buf);
    }

    /**
     * ����������ͷ��ȡ���ȵ����д���������
     *
     * @param input  ������
     * @param output �����
     * @param size   ��ĳ���
     * @param buf    ������
     */
    public static final int streamCopy(InputStream input,
                                       OutputStream output,
                                       int size,
                                       byte[] buf)
        throws IOException
    {
        if (size < 0) {
            size = Integer.MAX_VALUE;
        }
        if (size == 0) {
            return 0;
        }
        return streamCopy0(input, output, size, buf);
    }

    private static final int streamCopy0(InputStream input,
                                         OutputStream output,
                                         int size,
                                         byte[] buf)
        throws IOException
    {
        int total = 0;
        int read = -1;
        int len = buf.length;
        while (size > 0) {
            read = size > len ? len : size;
            read = input.read(buf, 0, read);
            if (read <= 0) {
                break;
            }
            output.write(buf, 0, read);
            total += read;
            if (size != Integer.MAX_VALUE) {
                size -= read;
            }
        }
        return total;
    }

    /**
     * ��Reader�е����ȫ��������д��Writer
     *
     * @param reader Reader
     * @param writer Writer
     */
    public static final int streamCopy(Reader reader,
                                       Writer writer)
        throws IOException
    {
        return streamCopy(reader, writer, -1);
    }

    /**
     * ��Reader�е����ȫ��������д��Writer
     *
     * @param reader Reader
     * @param writer Writer
     * @param buf    ������
     */
    public static final int streamCopy(Reader reader,
                                       Writer writer,
                                       char[] buf)
        throws IOException
    {
        return streamCopy(reader, writer, -1, buf);
    }

    /**
     * ��Reader��ͷ��ȡ���ȵ����д��Writer��
     *
     * @param reader Reader
     * @param writer Writer
     * @param size   ��ĳ���
     */
    public static final int streamCopy(Reader reader,
                                       Writer writer,
                                       int size)
        throws IOException
    {
        if (size < 0) {
            size = Integer.MAX_VALUE;
        }
        if (size == 0) {
            return 0;
        }
        CharBuffer buffer = getCharBuffer();
        char[] buf = buffer.array();
        return streamCopy0(reader, writer, size, buf);
    }

    /**
     * ��Reader��ͷ��ȡ���ȵ����д��Writer��
     *
     * @param reader Reader
     * @param writer Writer
     * @param size   ��ĳ���
     * @param buf    ������
     */
    public static final int streamCopy(Reader reader,
                                       Writer writer,
                                       int size,
                                       char[] buf)
        throws IOException
    {
        if (size < 0) {
            size = Integer.MAX_VALUE;
        }
        if (size == 0) {
            return 0;
        }
        return streamCopy0(reader, writer, size, buf);
    }

    /**
     * ��Reader��ͷ��ȡ���ȵ����д��Writer��
     *
     * @param reader Reader
     * @param writer Writer
     * @param size   ��ĳ���
     * @param buf    ������
     */
    private static final int streamCopy0(Reader reader,
                                         Writer writer,
                                         int size,
                                         char[] buf)
        throws IOException
    {
        int total = 0;
        int read = -1;
        int len = buf.length;
        while (size > 0) {
            read = size > len ? len : size;
            read = reader.read(buf, 0, read);
            if (read <= 0) {
                break;
            }
            writer.write(buf, 0, read);
            total += read;
            size -= read;
        }
        return total;
    }

    /**
     * ���������ж�ȡ���ȵ����
     *
     * @param input ������
     * @param bytes Ҫ��������
     * @param off   ��ʼλ��
     * @param len   ����
     */
    public static final int readFully(InputStream input,
                                      byte[] bytes, int off, int len)
        throws IOException
    {
        return CodecUtil.readFully(input, bytes, off, len);
    }

    /**
     * ���������ж�ȡ����������
     *
     * @param input ������
     * @param bytes Ҫ��������
     */
    public static final int readFully(InputStream input, byte[] bytes)
        throws IOException
    {
        return CodecUtil.readFully(input, bytes);
    }


    /**
     * ��Reader�ж�ȡ���ȵ����
     *
     * @param reader Reader
     * @param chars  Ҫ��������
     * @param off    ��ʼλ��
     * @param len    ����
     */
    public static final int readFully(Reader reader,
                                      char[] chars, int off, int len)
        throws IOException
    {
        return CodecUtil.readFully(reader, chars, off, len);
    }


    /**
     * ��Reader�ж�ȡ����������
     *
     * @param reader Reader
     * @param chars  Ҫ��������
     */
    public static final int readFully(Reader reader, char[] chars)
        throws IOException
    {
        return CodecUtil.readFully(reader, chars);
    }

    /**
     * ��ȡһ�е����
     *
     * @param input ������
     * @param bytes ���
     * @param off   ��ʼλ��
     * @param len   ����
     */
    public static final int readLine(InputStream input,
                                     byte[] bytes, int off, int len)
        throws IOException
    {
        if (len <= 0) {
            return 0;
        }

        int count = 0, c;
        while ((c = input.read()) != -1) {
            bytes[off++] = (byte) c;
            count++;
            if (c == '\n' || count == len) {
                break;
            }
        }
        return count > 0 ? count : -1;
    }

    /**
     * ��ȡһ�е����
     *
     * @param reader ������
     * @param chars  ���
     * @param off    ��ʼλ��
     * @param len    ����
     */
    public static final int readLine(Reader reader,
                                     char[] chars, int off, int len)
        throws IOException
    {
        if (len <= 0) {
            return 0;
        }

        int count = 0, c;
        while ((c = reader.read()) != -1) {
            chars[off++] = (char) c;
            count++;
            if (c == '\n' || count == len) {
                break;
            }
        }
        return count > 0 ? count : -1;
    }

    /**
     * ��ȫ�ر�RandomAccessFile
     *
     * @param raf RandomAccessFile
     * @return �Ƿ�رճɹ������input��<code>null</code>ͬ���<code>true</code>
     */
    public static final boolean close(RandomAccessFile raf)
    {
        if (raf == null) {
            return true;
        }
        try {
            raf.close();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * ��ȫ�ر�������
     *
     * @param input ������
     * @return �Ƿ�رճɹ������input��<code>null</code>ͬ���<code>true</code>
     */
    public static final boolean close(InputStream input)
    {
        if (input == null) {
            return true;
        }
        try {
            input.close();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }


    /**
     * ��ȫ�ر�Ƕ�׵���������ǰ��Ƕ�׺���
     *
     * @param input1 ������1
     * @param input2 ������2
     */
    public static final boolean close(InputStream input1, InputStream input2)
    {
        boolean closed = false;
        if (input1 != null) {
            closed = StreamUtil.close(input1);
        }
        if (!closed) {
            closed = StreamUtil.close(input2);
        }
        return closed;
    }

    /**
     * ��ȫ�ر������
     *
     * @param output �����
     * @return �Ƿ�رճɹ������output��<code>null</code>ͬ���<code>true</code>
     */
    public static final boolean close(OutputStream output)
    {
        if (output == null) {
            return true;
        }
        try {
            output.flush();
            output.close();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * ��ȫ�ر�Ƕ�׵��������ǰ��Ƕ�׺���
     *
     * @param output1 �����1
     * @param output2 �����2
     */
    public static final boolean close(OutputStream output1, OutputStream output2)
    {
        boolean closed = false;
        if (output1 != null) {
            closed = StreamUtil.close(output1);
        }
        if (!closed) {
            closed = StreamUtil.close(output2);
        }
        return closed;
    }

    /**
     * ��ȫ�ر�Reader
     *
     * @param reader Reader
     * @return �Ƿ�رճɹ������reader��<code>null</code>ͬ���<code>true</code>
     */
    public static final boolean close(Reader reader)
    {
        if (reader == null) {
            return true;
        }

        try {
            reader.close();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * ��ȫ�ر�Ƕ�׵�Reader��ǰ��Ƕ�׺���
     *
     * @param reader1 Reader1
     * @param reader2 Reader2
     */
    public static final boolean close(Reader reader1, Reader reader2)
    {
        boolean closed = false;
        if (reader1 != null) {
            closed = StreamUtil.close(reader1);
        }
        if (!closed) {
            closed = StreamUtil.close(reader2);
        }
        return closed;
    }

    /**
     * ��ȫ�ر�Writer
     *
     * @param writer Writer
     * @return �Ƿ�رճɹ������reader��<code>null</code>ͬ���<code>true</code>
     */
    public static final boolean close(Writer writer)
    {
        if (writer == null) {
            return true;
        }
        try {
            writer.flush();
            writer.close();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * ��ȫ�ر�Ƕ�׵�Writer��ǰ��Ƕ�׺���
     *
     * @param writer1 Writer1
     * @param writer2 Writer2
     */
    public static final boolean close(Writer writer1, Writer writer2)
    {
        boolean closed = false;
        if (writer1 != null) {
            closed = StreamUtil.close(writer1);
        }
        if (!closed) {
            closed = StreamUtil.close(writer2);
        }
        return closed;
    }


    /**
     * ��UTF8����ʽ���룬ע�⣺��DataInputStream#readUTF��ʽ��ͬ
     */
    public static final String readUTF(DataInput in)
        throws IOException
    {
        int strlen = in.readInt();
        if (strlen == -1) {
            return null;
        }
        else if (strlen == 0) {
            return StringUtil.EMPTY_STRING;
        }

        int utflen = in.readInt();
        StringBuilder sb = new StringBuilder(strlen);
        ByteBuffer buffer = null;
        byte bytes [] = null;
        if (utflen <= BYTE_BUFFER_SIZE) {
            buffer = getByteBuffer();
            bytes = buffer.array();
        }
        else {
            bytes = new byte[utflen];
        }

        in.readFully(bytes, 0, utflen);
        toString(bytes, utflen, sb);
        return sb.toString();
    }

    /**
     * ��UTF8����ʽ���룬ע�⣺��DataInputStream#readUTF��ʽ��ͬ
     */
    public static final String readUTF(InputStream in)
        throws IOException
    {
        int strlen = CodecUtil.readInt(in);
        if (strlen == -1) {
            return null;
        }
        else if (strlen == 0) {
            return StringUtil.EMPTY_STRING;
        }

        int utflen = CodecUtil.readInt(in);
        StringBuilder sb = new StringBuilder(strlen);
        ByteBuffer buffer = null;
        byte bytes [] = null;
        if (utflen <= BYTE_BUFFER_SIZE) {
            buffer = getByteBuffer();
            bytes = buffer.array();
        }
        else {
            bytes = new byte[utflen];
        }

        readFully(in, bytes, 0, utflen);

        toString(bytes, utflen, sb);
//        if (buffer != null) {
//
//        }

        return sb.toString();
    }

    static final void toString(byte[] bytes, int utflen,
                               StringBuilder sb)
        throws IOException
    {
        int c, char2, char3;
        int count = 0;

        while (count < utflen) {
            c = (int) bytes[count] & 0xff;
            switch (c >> 4) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    /* 0xxxxxxx*/
                    count++;
                    sb.append((char) c);
                    break;
                case 12:
                case 13:
                    /* 110x xxxx   10xx xxxx*/
                    count += 2;
                    if (count > utflen) {
                        throw new UTFDataFormatException();
                    }
                    char2 = (int) bytes[count - 1];
                    if ((char2 & 0xC0) != 0x80) {
                        throw new UTFDataFormatException();
                    }
                    sb.append((char) (((c & 0x1F) << 6) | (char2 & 0x3F)));
                    break;
                case 14:
                    /* 1110 xxxx  10xx xxxx  10xx xxxx */
                    count += 3;
                    if (count > utflen) {
                        throw new UTFDataFormatException();
                    }
                    char2 = (int) bytes[count - 2];
                    char3 = (int) bytes[count - 1];
                    if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80)) {
                        throw new UTFDataFormatException();
                    }
                    sb.append((char) (((c & 0x0F) << 12) |
                        ((char2 & 0x3F) << 6) |
                        ((char3 & 0x3F) << 0)));
                    break;
                default:
                    /* 10xx xxxx,  1111 xxxx */
                    throw new UTFDataFormatException();
            }
        }
    }


    /**
     * Skip given number of bytes
     *
     * @param is
     * @param n
     * @throws IOException
     */
    public static final int skipFully(InputStream is, int n)
        throws IOException
    {
        //skip data
        ByteBuffer byteBuf = getByteBuffer();
        byte[] buf = byteBuf.array();
        int bufLen = 0;
        int left = n;
        while (left > 0) {
            bufLen = is.read(buf, 0, Math.min(BYTE_BUFFER_SIZE, left));
            if (bufLen > 0) {
                left -= bufLen;
            }
            else {
                break;
            }
        }
        return n - left;
    }


    /**
     * Skip given number of bytes
     *
     * @param reader
     * @param n
     * @throws IOException
     */
    public static final int skipFully(Reader reader, int n)
        throws IOException
    {
        //skip data
        CharBuffer charBuf = getCharBuffer();
        char[] buf = charBuf.array();
        int bufLen = 0;
        int left = n;
        while (left > 0) {
            bufLen = reader.read(buf, 0, Math.min(CHAR_BUFFER_SIZE, left));
            if (bufLen > 0) {
                left -= bufLen;
            }
            else {
                break;
            }
        }
        return n - left;
    }

    /**
     * ���ַ������Writer�У����������CharBuffer������writer�е�Buffer
     *
     * @param str �ַ�
     */
    public static final int writeTo(String str, Writer out)
        throws IOException
    {
        if (str == null) {
            out.write(StringUtil.NULL_CHARS);
            return 4;
        }
        else {
            return writeTo(str, 0, str.length(), out);
        }
    }

    /**
     * ���ַ������Writer�У����������CharBuffer������writer�е�Buffer
     *
     * @param str �ַ�
     */
    public static final int writeTo(String str, int off, int len, Writer out)
        throws IOException
    {
        if (str == null) {
            out.write(StringUtil.NULL_CHARS);
            return 4;
        }
        else {
            CharBuffer charBuf = getCharBuffer();
            char[] buf = charBuf.array();
            if (len < CHAR_BUFFER_SIZE) {
                str.getChars(off, (off + len), buf, 0);
                out.write(buf, 0, len);
            }
            else {
                int written = 0;
                int end = 0;
                while (len > 0) {
                    written = len <= CHAR_BUFFER_SIZE ? len : CHAR_BUFFER_SIZE;
                    end = off + written;
                    str.getChars(off, end, buf, 0);
                    out.write(buf, 0, written);
                    len -= written;
                    off += written;
                }
            }
            return len;
        }
    }

    /**
     * ���ַ������Writer�У����������CharBuffer������writer�е�Buffer
     *
     * @param sb �ַ�
     */
    public static final int writeTo(StringBuffer sb, Writer out)
        throws IOException
    {
        if (sb == null) {
            out.write(StringUtil.NULL_CHARS);
            return 4;
        }
        else {
            return writeTo(sb, 0, sb.length(), out);
        }
    }

    /**
     * ���ַ������Writer�У����������CharBuffer������writer�е�Buffer
     *
     * @param sb �ַ�
     */
    public static final int writeTo(StringBuffer sb, int off, int len, Writer out)
        throws IOException
    {
        if (sb == null) {
            out.write(StringUtil.NULL_CHARS);
            return 4;
        }
        else {
            CharBuffer charBuf = getCharBuffer();
            char[] buf = charBuf.array();
            if (len < CHAR_BUFFER_SIZE) {
                sb.getChars(off, (off + len), buf, 0);
                out.write(buf, 0, len);
            }
            else {
                int written = 0;
                int end = 0;
                while (len > 0) {
                    written = len <= CHAR_BUFFER_SIZE ? len : CHAR_BUFFER_SIZE;
                    end = off + written;
                    sb.getChars(off, end, buf, 0);
                    out.write(buf, 0, written);
                    len -= written;
                    off += written;
                }
            }
            return len;
        }
    }

}

