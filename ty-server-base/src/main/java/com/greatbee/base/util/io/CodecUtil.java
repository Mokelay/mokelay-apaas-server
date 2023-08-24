package com.greatbee.base.util.io;

import com.greatbee.base.util.lang.Empty;
import com.greatbee.base.util.lang.reflect.Reflection;
import com.greatbee.base.util.lang.reflect.ReflectionException;
import com.greatbee.base.util.StringUtil;
import sun.reflect.ReflectionFactory;

import java.io.*;
import java.lang.ref.SoftReference;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;

/**
 * ������빤���࣬Ϊ���ܹ��ڷ�ceno-base��������Ȼ�ܹ�ʹ����صķ���Serialize����
 *
 * @author system
 * @version 1.00 2004-8-23 16:43:49
 */
public class CodecUtil implements CodecConstants
{
    public static final int MAX_ARRAY_LENGTH = 64 * 1024;
    public static final int MAX_STRING_LENGTH = 1024 * 1024;

    /**
     * д��һ��<code>null</code>����
     *
     * @param out �����
     * @throws IOException ����I/O�쳣ʱ�׳�
     */
    public static void writeNull(OutputStream out)
        throws IOException
    {
        writeType(out, TYPE_NULL);
    }

    /**
     * д���������еĶ�������
     *
     * @param out  �����
     * @param type ������<code>FastSerialize</code>�е�����
     * @throws IOException ����I/O�쳣ʱ�׳�
     */
    public static void writeType(OutputStream out, int type)
        throws IOException
    {
        out.write(type);
    }

    /**
     * ��ȡ�������еĶ�������
     *
     * @param in �����
     * @return type ������<code>FastSerialize</code>�е�����
     * @throws IOException ����I/O�쳣ʱ�׳�
     */
    public static int readType(InputStream in)
        throws IOException
    {
        return in.read();
    }

    /**
     * д��һ���򵥵��ֽ�
     *
     * @param out �����
     * @param b   �ֽ�
     * @throws IOException ����I/O�쳣ʱ�׳�
     */
    public static void writeByte(OutputStream out, byte b)
        throws IOException
    {
        out.write(b);
    }

    /**
     * д��һ����������
     *
     * @param out �����
     * @param b   ����ֵ
     * @throws IOException ����I/O�쳣ʱ�׳�
     */
    public static void writeBoolean(OutputStream out, boolean b)
        throws IOException
    {
        out.write(b ? 1 : 0);
    }

    /**
     * д��һ���ַ�
     *
     * @param out �����
     * @param c   �ַ�
     * @throws IOException ����I/O�쳣ʱ�׳�
     */
    public static void writeChar(OutputStream out, char c)
        throws IOException
    {
        out.write((c >>> 8) & 0xFF);
        out.write((c) & 0xFF);
    }

    /**
     * д��һ������������
     *
     * @param out �����
     * @param s   ������
     * @throws IOException ����I/O�쳣ʱ�׳�
     */
    public static void writeShort(OutputStream out, short s)
        throws IOException
    {
        out.write((s >>> 8) & 0xFF);
        out.write((s) & 0xFF);
    }

    public static void writeUnsignedShort(OutputStream out, int i)
        throws IOException
    {
//        out.write(Binary.toShortBytes(i));
        out.write((int)(i >>> 8) & 0xFF);
        out.write((int)(i) & 0xFF);
    }

    public static void writeUnsignedByte(OutputStream out, int i)
        throws IOException
    {
        out.write(i);
    }

    public static void writeInt(OutputStream out, int i)
        throws IOException
    {
        out.write((i >>> 24) & 0xFF);
        out.write((i >>> 16) & 0xFF);
        out.write((i >>> 8) & 0xFF);
        out.write((i) & 0xFF);
    }

    /**
     * ����޷������
     *
     * @param out
     * @param l
     * @throws IOException
     */
    public static void writeUnsignedInt(OutputStream out, long l)
        throws IOException
    {
        out.write((int)(l >>> 24) & 0xFF);
        out.write((int)(l >>> 16) & 0xFF);
        out.write((int)(l >>> 8) & 0xFF);
        out.write((int)(l) & 0xFF);
    }

    public static void writeLong(OutputStream out, long l)
        throws IOException
    {
        out.write((int)(l >>> 56) & 0xFF);
        out.write((int)(l >>> 48) & 0xFF);
        out.write((int)(l >>> 40) & 0xFF);
        out.write((int)(l >>> 32) & 0xFF);
        out.write((int)(l >>> 24) & 0xFF);
        out.write((int)(l >>> 16) & 0xFF);
        out.write((int)(l >>> 8) & 0xFF);
        out.write((int) (l) & 0xFF);
    }

    public static void writeFloat(OutputStream out, float f)
        throws IOException
    {
        writeInt(out, Float.floatToIntBits(f));
    }

    public static void writeDouble(OutputStream out, double d)
        throws IOException
    {
        writeLong(out, Double.doubleToLongBits(d));
    }

    public static void writeBytes(OutputStream out, byte[] bytes)
        throws IOException
    {
        int len = -1;
        if (bytes != null) {
            len = bytes.length;
        }
        writeInt(out, len);
        if (bytes != null) {
            out.write(bytes);
        }
    }

    public static byte readByte(InputStream in)
        throws IOException
    {
        return (byte)(in.read() & 0xFF);
    }

    public static boolean readBoolean(InputStream in)
        throws IOException
    {
        int ch = in.read();
        return ch != 0;
    }

    /**
     * ע�⣺�п��ܲ������û�ж��������������BufferedInputStream��ByteArrayInputStream
     * ֮��ľͲ��������⣩��Ϊ���ٶ�������ɣ����� ��ͬ
     */
    public static char readChar(InputStream in)
        throws IOException
    {
        int ch1 = in.read();
        int ch2 = in.read();
        if ((ch1 | ch2) < 0) {
            throw new EOFException();
        }
        return (char)((ch1 << 8) + (ch2));
    }

    public static int readUnsignedByte(InputStream in)
        throws IOException
    {
        int ch = in.read();
        if (ch < 0) {
            throw new EOFException();
        }
        return ch;
    }

    public static short readShort(InputStream in)
        throws IOException
    {
        int ch1 = in.read();
        int ch2 = in.read();
        if ((ch1 | ch2) < 0) {
            throw new EOFException();
        }
        return (short)((ch1 << 8) + (ch2));
    }

    public static int readUnsignedShort(InputStream in)
        throws IOException
    {
        int ch1 = in.read();
        int ch2 = in.read();
        if ((ch1 | ch2) < 0) {
            throw new EOFException();
        }
        return (ch1 << 8) + (ch2);
    }

    public static int readInt(InputStream in)
        throws IOException
    {
        int ch1 = in.read();
        int ch2 = in.read();
        int ch3 = in.read();
        int ch4 = in.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0) {
            throw new EOFException();
        }
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4));
    }

    /**
     * �����޷������
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static long readUnsignedInt(InputStream in)
        throws IOException
    {
        long ch1 = in.read();
        int ch2 = in.read();
        int ch3 = in.read();
        int ch4 = in.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0) {
            throw new EOFException();
        }
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4));
    }

    public static long readLong(InputStream in)
        throws IOException
    {
        return ((long)(readInt(in)) << 32) +
               (readInt(in) & 0xFFFFFFFFL);
    }

    public static float readFloat(InputStream in)
        throws IOException
    {
        return Float.intBitsToFloat(readInt(in));
    }

    public static double readDouble(InputStream in)
        throws IOException
    {
        return Double.longBitsToDouble(readLong(in));
    }

    public static byte[] readBytes(InputStream in)
        throws IOException
    {
        int len = readInt(in);
        if (len < 0) {
            return null;
        }

        byte[] bytes = new byte[len];
        readFully(in, bytes);
        return bytes;
    }

    /**
     * ���������ж�ȡ���ȵ����
     *
     * @param input ������
     * @param bytes Ҫ��������
     * @param off   ��ʼλ��
     * @param len   ����
     */
    public static int readFully(InputStream input,
                                      byte[] bytes, int off, int len)
        throws IOException
    {
        if (len < 0) {
            throw new IndexOutOfBoundsException();
        }

        int n = 0;
        int read = 0;
        while (n < len) {
            read = input.read(bytes, off + n, len - n);
            if (read <= 0) {
                break;
            }
            n += read;
        }
        return n;
    }

    /**
     * ���������ж�ȡ����������
     *
     * @param input ������
     * @param bytes Ҫ��������
     */
    public static int readFully(InputStream input, byte[] bytes)
        throws IOException
    {
        return readFully(input, bytes, 0, bytes.length);
    }

    /**
     * ��Reader�ж�ȡ���ȵ����
     *
     * @param reader Reader
     * @param chars  Ҫ��������
     * @param off    ��ʼλ��
     * @param len    ����
     */
    public static int readFully(Reader reader,
                                      char[] chars, int off, int len)
        throws IOException
    {
        if (len < 0) {
            throw new IndexOutOfBoundsException();
        }

        int n = 0;
        int read = 0;
        while (n < len) {
            read = reader.read(chars, off + n, len - n);
            if (read <= 0) {
                break;
            }
            n += read;
        }
        return n;
    }

    /**
     * ��Reader�ж�ȡ����������
     *
     * @param reader Reader
     * @param chars  Ҫ��������
     */
    public static int readFully(Reader reader, char[] chars)
        throws IOException
    {
        return readFully(reader, chars, 0, chars.length);
    }

    //UTF-8


    /**
     * ��ȡ�ַ�
     *
     * @param ois
     * @return
     * @throws IOException
     */
    public static String readUTF(InputStream ois)
        throws IOException
    {
        return StreamUtil.readUTF(ois);
    }

    /**
     * ���Զ����ַ�����
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static char[] readChars(InputStream in)
        throws IOException
    {
        int len = readInt(in);
        if (len < 0) {
            return null;
        }
        else if (len == 0) {
            return new char[0];
        }
        else if (len > MAX_STRING_LENGTH) {
            throw new IOException("The length of chars is so big");
        }

        int ch1, ch2;

        byte[] bytes = getByteBuffer();
        int read = 0;
        char[] chars = new char[len];
        int i = 0;
        while (len > 0) {
            read = bytes.length;
            read = read > len * 2 ? len * 2 : read;
            read = readFully(in, bytes, 0, read);
            if (read <= 0) {
                break;
            }
            for (int j = 0; j < read;) {
                ch1 = (bytes[j++] & 0xFF);
                ch2 = (bytes[j++] & 0xFF);
                chars[i++] = (char)((ch1 << 8) + (ch2));
            }
            len -= read / 2;
        }

        return chars;
    }

    /**
     * ����byte������ƵĿ���д�ַ�
     */
    public static void writeChars(OutputStream out, char[] chars)
        throws IOException
    {
        int len = -1;
        if (chars != null) {
            len = chars.length;
        }
        int v;
        writeInt(out, len);
        if (len <= 0) {
            return;
        }
        else if (len < 128) { //�ַ�Ƚ���
            byte[] bytes = new byte[2];
            for (int i = 0; i < len; i++) {
                v = chars[i];
                bytes[0] = (byte)((v >>> 8) & 0xFF);
                bytes[1] = (byte)((v) & 0xFF);
                out.write(bytes);
            }
        }
        else { //�ַ�Ƚ϶࣬����Buffer��д
            byte[] bytes = getByteBuffer();
            int j = 0;
            try {
                for (int i = 0; i < len; i++) {
                    v = chars[i];
                    bytes[j++] = (byte)((v >>> 8) & 0xFF);
                    bytes[j++] = (byte)((v) & 0xFF);
                    if (j == bytes.length) { //Full
                        out.write(bytes, 0, j);
                        j = 0;
                    }
                }
                if (j > 0) {
                    out.write(bytes, 0, j);
                }
            }
            finally {
                bytes = null;
            }
        }
    }


    /**
     * ����byte������ƵĿ���д�ַ�
     */
    public static void writeString(OutputStream out, String str)
        throws IOException
    {
        int len = -1;
        if (str != null) {
            len = str.length();
        }
        writeInt(out, len);

        if (len > 0) {
            byte[] buf = getByteBuffer();
            writeString(out, str, len, buf);
        }
    }

    protected static void writeString(OutputStream out, String str,
                                            int len, byte[] buf)
        throws IOException
    {
        int j = 0;
        int v;

        for (int i = 0; i < len; i++) {
            v = str.charAt(i);
            buf[j++] = (byte)((v >>> 8) & 0xFF);
            buf[j++] = (byte)((v) & 0xFF);
            if (j == buf.length) { //Full
                out.write(buf, 0, j);
                j = 0;
            }
        }
        if (j > 0) {
            out.write(buf, 0, j);
        }
    }

    /**
     * д���ֽ��ַ�
     */
    public static void writeSingle(OutputStream out, String s)
        throws IOException
    {
        int len = -1;
        if (s != null) {
            len = s.length();
        }
        writeInt(out, len);

        int v;
        if (len <= 0) {
            return;
        }
        else {
            byte[] bytes = getByteBuffer();
            int j = 0;
            for (int i = 0; i < len; i++) {
                v = s.charAt(i);
                bytes[j++] = (byte)((v) & 0xFF);
                if (j == bytes.length) { //Full
                    out.write(bytes, 0, j);
                    j = 0;
                }
            }
            if (j > 0) {
                out.write(bytes, 0, j);
            }
        }
    }


    private static int BYTE_BUFFER_SIZE = 1024;
    private static int CHAR_BUFFER_SIZE = 512;

    private static ThreadLocal<SoftReference<byte[]>> byteBuffer
        = new ThreadLocal<SoftReference<byte[]>>()
    {
        protected SoftReference<byte[]> initialValue()
        {
            return new SoftReference<byte[]>(new byte[BYTE_BUFFER_SIZE]);
        }
    };

    private static ThreadLocal<SoftReference<char[]>> charBuffer
        = new ThreadLocal<SoftReference<char[]>>()
    {
        protected SoftReference<char[]> initialValue()
        {
            return new SoftReference<char[]>(new char[CHAR_BUFFER_SIZE]);
        }
    };


    private static byte[] getByteBuffer()
    {
        SoftReference<byte[]> sr = byteBuffer.get();
        byte[] buf = null;
        if (sr != null) {
            buf = (byte[])sr.get();
        }
        if (buf == null) {
            buf = new byte[BYTE_BUFFER_SIZE];
            sr = new SoftReference<byte[]>(buf);
            byteBuffer.set(sr);
        }
        return buf;
    }

    private static char[] getCharBuffer()
    {
        SoftReference<char[]> sr = charBuffer.get();
        char[] buf = null;
        if (sr != null) {
            buf = (char[])sr.get();
        }
        if (buf == null) {
            buf = new char[CHAR_BUFFER_SIZE];
            sr = new SoftReference<char[]>(buf);
            charBuffer.set(sr);
        }
        return buf;
    }

    public static String readSingle(InputStream in)
        throws IOException
    {
        int len = readInt(in);

        if (len < 0) {
            return null;
        }
        else if (len == 0) {
            return "";
        }
        else if (len > MAX_STRING_LENGTH) {
            throw new IOException("The string length is so big");
        }

        byte[] bytes = getByteBuffer();
        int ch1;
        int read = 0;
        if (len <= CHAR_BUFFER_SIZE) {
            read = readFully(in, bytes, 0, len);
            if (read != len) {
                throw new IOException("No more data left:" + len + " read:" + read);
            }
            char[] chars = getCharBuffer();
            for (int i = 0, j = 0; j < read; i++) {
                ch1 = (bytes[j++] & 0xFF);
                chars[i] = (char)(ch1);
            }
            return new String(chars, 0, len);
        }
        else {
            StringBuilder sb = new StringBuilder(len);
            while (len > 0) {
                read = bytes.length > len ? len : bytes.length;
                read = readFully(in, bytes, 0, read);
                if (read <= 0) {
                    break;
                }
                for (int i = 0, j = 0; i < read; i++) {
                    ch1 = (bytes[j++] & 0xFF);
                    sb.append((char)(ch1));
                }
                len -= read;
            }
            return sb.toString();
        }
    }


    public static String readString(InputStream in)
        throws IOException
    {
        int len = readInt(in);

        if (len < 0) {
            return null;
        }
        else if (len == 0) {
            return "";
        }
        else if (len > MAX_STRING_LENGTH) {
            throw new IOException("The string length is so big");
        }

        byte[] buf = getByteBuffer();
        return readString(in, len, buf);
    }


    protected static String readString(InputStream in, int len, byte[] buf)
        throws IOException
    {
        int ch1, ch2;
        int read = 0;
        if (len <= CHAR_BUFFER_SIZE) {
            char[] chars = getCharBuffer();
            int k = 0;
            int l = len;
            while (l > 0) {
                read = buf.length > l * 2 ? l * 2 : buf.length;
                read = readFully(in, buf, 0, read);
                if (read <= 0) {
                    break;
                }
                for (int j = 0; j < read;) {
                    ch1 = (buf[j++] & 0xFF);
                    ch2 = (buf[j++] & 0xFF);
                    chars[k++] = (char)((ch1 << 8) + (ch2));
                }
                l -= (read / 2);
            }
            return new String(chars, 0, len);
        }
        else {
            StringBuilder sb = new StringBuilder(len);
            while (len > 0) {
                read = buf.length > len * 2 ? len * 2 : buf.length;
                read = readFully(in, buf, 0, read);
                if (read <= 0) {
                    break;
                }
                for (int j = 0; j < read;) {
                    ch1 = (buf[j++] & 0xFF);
                    ch2 = (buf[j++] & 0xFF);
                    sb.append((char)((ch1 << 8) + (ch2)));
                }
                len -= (read / 2);
            }
            return sb.toString();
        }
    }

    private static void writeString(OutputStream out,
                                          String str, byte[] buf)
        throws IOException
    {
        int len = -1;
        if (str != null) {
            len = str.length();
        }
        writeInt(out, len);

        if (len > 0) {
            writeString(out, str, len, buf);
        }
    }

    private static String readString(InputStream in, byte[] buf)
        throws IOException
    {
        int len = readInt(in);

        if (len < 0) {
            return null;
        }
        else if (len == 0) {
            return "";
        }
        else if (len > MAX_STRING_LENGTH) {
            throw new IOException("The string length is so big");
        }

        return readString(in, len, buf);
    }

    public static void writeStringArray(ObjectOutputStream oos, String[] array)
        throws IOException
    {
        writeStringArray((OutputStream)oos, array);
    }


    public static void writeStringArray(OutputStream oos, String[] array)
        throws IOException
    {
        if (array == null) {
            writeInt(oos, -1);
            return;
        }

        int len = array.length;
        writeInt(oos, len);
        byte[] buf = getByteBuffer();
        for (int i = 0; i < len; i++) {
            writeString(oos, array[i], buf);
        }
    }

    public static String[] readStringArray(ObjectInputStream ois)
        throws IOException
    {
        return readStringArray((InputStream)ois);
    }

    public static String[] readStringArray(InputStream ois)
        throws IOException
    {
        int len = readInt(ois);
        if (len == -1) {
            return null;
        }
        else if (len == 0) {
            return StringUtil.EMPTY_STRING_ARRAY;
        }
        else if (len > MAX_ARRAY_LENGTH) {
            throw new IOException("The length is so big");
        }

        String[] array = new String[len];
        byte[] buf = getByteBuffer();
        for (int i = 0; i < len; i++) {
            array[i] = readString(ois, buf);
        }

        return array;
    }

    //���ٴ��л���ص���


    static void writeClass(OutputStream out, Class clazz)
        throws IOException
    {
        writeSingle(out, clazz.getName());
    }

    public static void writeObject(ObjectOutputStream oos, Object object)
        throws IOException
    {
        if (object == null) {
            writeNull(oos);
            return;
        }

        if (object instanceof byte[]) {
            writeType(oos, TYPE_BYTE_ARRAY);
            writeBytes(oos, (byte[])object);
        }
        else if (object instanceof String) {
            writeType(oos, TYPE_STRING);
            writeString(oos, (String)object);
        }
        else if (object instanceof char[]) {
            writeType(oos, TYPE_CHAR_ARRAY);
            writeChars(oos, (char[])object);
        }
        else if (object instanceof Character) {
            writeType(oos, TYPE_CHARACTER);
            writeChar(oos, ((Character)object).charValue());
        }
        else if (object instanceof Number) {
            if (object instanceof Integer) {
                writeType(oos, TYPE_INTEGER);
                writeInt(oos, ((Integer)object).intValue());
            }
            else if (object instanceof Long) {
                writeType(oos, TYPE_LONG);
                writeLong(oos, ((Long)object).longValue());
            }
            else if (object instanceof Byte) {
                writeType(oos, TYPE_BYTE);
                writeByte(oos, ((Byte)object).byteValue());
            }
            else if (object instanceof Float) {
                writeType(oos, TYPE_FLOAT);
                writeFloat(oos, ((Float)object).floatValue());
            }
            else if (object instanceof Double) {
                writeType(oos, TYPE_DOUBLE);
                writeDouble(oos, ((Double)object).doubleValue());
            }
            else if (object instanceof Short) {
                writeType(oos, TYPE_SHORT);
                writeShort(oos, ((Short)object).shortValue());
            }
            else {
                writeType(oos, TYPE_OBJECT);
                oos.writeObject(object);
            }
        }
        else if (object instanceof Boolean) {
            writeType(oos, TYPE_BOOLEAN);
            writeBoolean(oos, ((Boolean)object).booleanValue());
        }
        else if (object instanceof String[]) {
            writeType(oos, TYPE_STRING_ARRAY);
            writeStringArray(oos, (String[])object);
        }
        else if (object instanceof Class) {
            writeType(oos, TYPE_CLASS);
            writeClass(oos, (Class)object);
        }
        else if (object instanceof Streamable) {
            writeType(oos, TYPE_STREAMABLE);
            writeStreamable(oos, (Streamable)object);
        }
        else if (object instanceof Marshallable) {
            //�жϸö����Ƿ�߱�public XXX(Empty) ���� public XXX() ���췽��
            //����������� TYPE_MARSHALLABLE �ķ�ʽ���д��л�
            //���������ͨ�Ĵ��л���ʽ
            //ע�⣺ǰ�߱����Լ���֤��marshal�����ܹ���ȫ������ĳ�Ա���л���ȥ
            //      �����Щ��ԱΪ<code>null</code>���������벻���ĺ��
//            if (hasConstructor(object.getClass())) {
            //д��Marshallable����
            writeType(oos, TYPE_MARSHALLABLE);
            marshal(oos, (Marshallable)object);
//            }
//            else {
//                writeType(oos, TYPE_OBJECT);
//                oos.writeObject(object);
//            }
        }
        else if (object instanceof Object[]) {
            writeType(oos, TYPE_OBJECT_ARRAY);
            writeObjectArray(oos, (Object[])object);
        }
        else {
            writeType(oos, TYPE_OBJECT);
            oos.writeObject(object);
        }
    }

    /**
     * д��<code>Marshallable</code>����<br>
     * д�������Լ�����<br>
     *
     * @see Marshallable
     */
    public static void marshal(ObjectOutputStream oos, Marshallable able)
        throws IOException
    {
        writeSingle(oos, able.getClass().getName());
        able.marshal(oos);
    }


    /**
     * ���Streamable����
     *
     * @param oos        OutputStream
     * @param streamable Streamable
     * @throws IOException
     */
    public static void writeStreamable(OutputStream oos, Streamable streamable)
        throws IOException
    {
        writeSingle(oos, streamable.getClass().getName());
        streamable.writeTo(oos);
    }

    /**
     * ��ȡ<code>Marshallable</code>����
     * ��ȡ�����Լ�����<br>
     *
     * @see Marshallable
     */
    public static Object demarshal(ObjectInputStream ois)
        throws IOException, ClassNotFoundException
    {
        String className = readSingle(ois);
        Marshallable obj = (Marshallable)newInstance(className);
        if (obj != null) {
            obj.demarshal(ois);
        }
        return obj;
    }

    /**
     * ��ȡStreamable
     *
     * @param ois InputStream
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object readStreamable(InputStream ois) throws IOException, ClassNotFoundException
    {
        String className = readSingle(ois);
        Streamable obj = (Streamable)newInstance(className);
        obj.readFrom(ois);
        return obj;
    }


    public static void writeObjectArray(ObjectOutputStream oos,
                                        Object[] array)
        throws IOException
    {
        if (array == null) {
            writeInt(oos, -1);
            return;
        }

        int len = array.length;
        writeInt(oos, len);
        writeClass(oos, array.getClass().getComponentType());
        for (int i = 0; i < len; i++) {
            writeObject(oos, array[i]);
        }
    }

    public static Object[] readObjectArray(ObjectInputStream ois)
        throws IOException, ClassNotFoundException
    {
        int len = readInt(ois);
        if (len == -1) {
            return null;
        }
        else if (len > MAX_ARRAY_LENGTH) {
            throw new IOException("The length is so big");
        }

        Class clazz = readClass(ois);
        Object[] array = (Object[])Array.newInstance(clazz, len);
        for (int i = 0; i < len; i++) {
            array[i] = readObject(ois);
        }
        return array;
    }

    static Class readClass(InputStream in)
        throws IOException, ClassNotFoundException
    {
        String className = readSingle(in);
        return Reflection.getClass(className);
    }

    /**
     * װ����
     *
     * @param name ����
     * @return �࣬�Ҳ�������<code>null</code>
     */
    static Class<?> loadClass(String name)
        throws ClassNotFoundException
    {
        return Reflection.loadClass(name);
    }

    private static ReflectionFactory reflFactory = ReflectionFactory.getReflectionFactory();


    private static <T> Constructor<T> loadConstructor(Class<T> clazz)
    {
        Class initCl = clazz;
        while (Serializable.class.isAssignableFrom(initCl)) {
            if ((initCl = initCl.getSuperclass()) == null) {
                return null;
            }
        }
        if (Marshallable.class.isAssignableFrom(clazz)) {
            try {
                return clazz.getDeclaredConstructor(Empty.EMPTY_CLASS);
            }
            catch (NoSuchMethodException e) {
            }
        }
        Constructor<T> cons = null;
        try {
            cons = initCl.getDeclaredConstructor(new Class[0]);
            int mods = cons.getModifiers();
            if ((mods & Modifier.PRIVATE) != 0 ||
                ((mods & (Modifier.PUBLIC | Modifier.PROTECTED)) == 0 &&
                 !packageEquals(clazz, initCl))) {
                return null;
            }
            cons = (Constructor<T>) reflFactory.newConstructorForSerialization(clazz, cons);
            cons.setAccessible(true);
            return cons;
        }
        catch (NoSuchMethodException ex) {
            return null;
        }
    }

    /**
     * Returns true if classes are defined in the same runtime package, false
     * otherwise.
     */
    private static boolean packageEquals(Class cl1, Class cl2)
    {
        return (cl1.getClassLoader() == cl2.getClassLoader() &&
                getPackageName(cl1).equals(getPackageName(cl2)));
    }

    /**
     * Returns package name of given class.
     */
    private static String getPackageName(Class cl)
    {
        String s = cl.getName();
        int i = s.lastIndexOf('[');
        if (i >= 0) {
            s = s.substring(i + 2);
        }
        i = s.lastIndexOf('.');
        return (i >= 0) ? s.substring(0, i) : "";
    }

    public static Object readObject(ObjectInputStream ois)
        throws OptionalDataException, ClassNotFoundException, IOException
    {
        int type = readType(ois);
        switch (type) {
            case TYPE_NULL:
                return null;
            case TYPE_BYTE_ARRAY:
                return readBytes(ois);
            case TYPE_CHAR_ARRAY:
                return readChars(ois);
            case TYPE_STRING:
                return readString(ois);
            case TYPE_INTEGER:
                return new Integer(readInt(ois));
            case TYPE_LONG:
                return new Long(readLong(ois));
            case TYPE_STREAMABLE:
                return readStreamable(ois);
            case TYPE_MARSHALLABLE: //��ȡMarshallable����
                return demarshal(ois);
            case TYPE_BYTE:
                return new Byte(readByte(ois));
            case TYPE_CHARACTER:
                return new Character(readChar(ois));
            case TYPE_BOOLEAN:
                return new Boolean(readBoolean(ois));
            case TYPE_SHORT:
                return new Short(readShort(ois));
            case TYPE_FLOAT:
                return new Float(readFloat(ois));
            case TYPE_DOUBLE:
                return new Double(readDouble(ois));
            case TYPE_OBJECT:
                return ois.readObject();
            case TYPE_CLASS:
                return readClass(ois);
            case TYPE_STRING_ARRAY:
                return readStringArray(ois);
            case TYPE_OBJECT_ARRAY:
                return readObjectArray(ois);
            default:
                throw new IOException("Invalid Type:" + type);
        }
    }

    /**
     * ʵ����<code>Marshallable</code>�Ĺ��췽��
     */
    private static HashMap<Class, Constructor> constructors = new HashMap<Class, Constructor>();

    /**
     * ����һ���µĶ���
     *
     * @param clazz ��
     */
    public static <T> T newInstance(Class<T> clazz)
    {
        if (clazz == null) {
            throw new IllegalArgumentException("Null class");
        }
        Constructor<T> constr = (Constructor<T>)constructors.get(clazz);
        if (constr == null) {
            synchronized (constructors) {
                constr = (Constructor<T>)constructors.get(clazz);
                if (constr == null) {
                    constr = loadConstructor(clazz);
                    if (constr == null) {
                        return null;
                    }
                    constructors.put(clazz, constr);
                }
            }
        }
        try {
            if (constr.getParameterTypes().length == 0) {
                return constr.newInstance();
            }
            else {
                return constr.newInstance(Empty.EMPTY_ARRAY);
            }
        }
        catch (InstantiationException e) {
            throw new ReflectionException("InstantiationException", e);
        }
        catch (IllegalAccessException e) {
            throw new ReflectionException("IllegalAccessException", e);
        }
        catch (InvocationTargetException e) {
            throw new ReflectionException("InvocationTargetException", e);
        }
    }

    /**
     * ����һ���µĶ���
     *
     * @param className ����
     */
    public static <T> T newInstance(String className)
        throws ClassNotFoundException
    {
        Class<T> clazz = (Class<T>)loadClass(className);
        return newInstance(clazz);
    }
}
