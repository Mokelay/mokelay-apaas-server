package com.mokelay.base.util.io;

/**
 * Codec����
 *
 * @author system
 * @version 1.00 2004-8-23 16:46:22
 */
public interface CodecConstants
{
    //UNDER 255
    public static final int TYPE_NULL = 0;
    public static final int TYPE_OBJECT = 1;
    public static final int TYPE_BYTE = 2;
    public static final int TYPE_BOOLEAN = 3;
    public static final int TYPE_CHARACTER = 4;
    public static final int TYPE_SHORT = 5;
    public static final int TYPE_INTEGER = 6;
    public static final int TYPE_LONG = 7;
    public static final int TYPE_FLOAT = 8;
    public static final int TYPE_DOUBLE = 9;
    public static final int TYPE_STRING = 10;
    public static final int TYPE_BYTE_ARRAY = 11;
    public static final int TYPE_CHAR_ARRAY = 12;
    public static final int TYPE_STRING_ARRAY = 13;

    //Streamable
    public static final int TYPE_STREAMABLE = 14;

    //ֻ������
    public static final int TYPE_CLASS = 15;

    public static final int TYPE_MARSHALLABLE = 16;

    public static final int TYPE_OBJECT_ARRAY = 17;
    public static final int TYPE_EXTERNALIZABLE = 18;
}
