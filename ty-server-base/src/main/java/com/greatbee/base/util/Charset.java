package com.greatbee.base.util;

import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;

/**
 * @author system
 * @version 1.00 2005-2-5 21:08:00
 */
public class Charset
{
    public static final String ISO_8859_1 = "8859_1";
    public static final String GBK = "GBK";
    public static final String UTF_8 = "UTF-8";

    public static final String FILE_ENCODING = "file.encoding";
    public static final String JAVA_CHARSET = "java.charset";

    private static String fileEncoding = null;

    public static final String getFileEncoding()
    {
        if (fileEncoding == null) {
            fileEncoding = System.getProperty(FILE_ENCODING, ISO_8859_1);
        }
        return fileEncoding;
    }

    /**
     * The default server charset encoding.
     */
    private static String javaCharset = null;

    public static final String getJavaCharset()
    {
        if (javaCharset == null) {
            javaCharset = System.getProperty(JAVA_CHARSET, GBK);
        }

        return javaCharset;
    }


    /**
     * Tells whether the named charset is supported.
     *
     * @param charsetName The name of the requested charset; may be either
     *                    a canonical name or an alias
     * @return <tt>true</tt> if, and only if, support for the named charset
     *         is available in the current Java virtual machine
     * @throws IllegalCharsetNameException
     *          If the given charset name is illegal
     */
    public static final boolean isSupported(String charsetName)
    {
        try {
            return java.nio.charset.Charset.isSupported(charsetName);
        }
        catch (IllegalCharsetNameException icne) {
            return false;
        }
    }
}
