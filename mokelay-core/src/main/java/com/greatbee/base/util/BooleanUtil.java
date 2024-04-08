package com.greatbee.base.util;

import java.util.HashSet;

/**
 *
 * @author system
 * @version 1.00 2005-2-6 0:00:22
 */
public class BooleanUtil
{
    /**
     * ֵ
     */
    public static final Boolean TRUE = Boolean.TRUE;
    /**
     * ֵ
     */
    public static final Boolean FALSE = Boolean.FALSE;


    private static final String[] VALID_TRUE
        = {"True", "true", "TRUE", "Yes", "yes", "YES",
        "On", "on", "ON", "T", "t", "Y", "y", "1"};

    private static final String[] VALID_FALSE
        = {"False", "false", "FALSE", "No", "no", "NO",
        "Off", "off", "OFF", "F", "f", "N", "n", "0"};

    private static final HashSet<String> TRUE_SET = new HashSet<String>(VALID_TRUE.length);
    private static final HashSet<String> FALSE_SET = new HashSet<String>(VALID_FALSE.length);

    static {
        for (int i = 0; i < VALID_TRUE.length; i++) {
            TRUE_SET.add(VALID_TRUE[i]);
        }
        for (int i = 0; i < VALID_FALSE.length; i++) {
            FALSE_SET.add(VALID_FALSE[i]);
        }
    }

    /**
     * ַתɲ
     *
     * @param value ǿԽַܵ<br>
     * @return [Boolean] 򷵻 <code>null</code>
     */
    public static final Boolean toBoolean(String value)
    {
        if (TRUE_SET.contains(value)) {
            return TRUE;
        }
        else if (FALSE_SET.contains(value)) {
            return FALSE;
        }
        else {
            return null;
        }
    }

    /**
     * ַתɲ
     *
     * @param value ǿԽַܵ<br>
     * @return [Boolean] 򷵻 <code>#toBoolean(defValue)</code>
     */
    public static final Boolean toBoolean(String value, boolean defValue)
    {
        Boolean bool = toBoolean(value);
        return bool != null ? bool : toBoolean(defValue);
    }

    /**
     * @return [Boolean] 򷵻 <code>defValue</code>
     */
    public static final boolean toBool(String value, boolean defValue)
    {
        return TRUE_SET.contains(value) || !FALSE_SET.contains(value) && defValue;
    }

    /**
     * @return [Boolean] <code>defValue</code>
     */
    public static final boolean toBool(String value)
    {
        return toBool(value, false);
    }

    /**
     */
    public static final Boolean toBoolean(boolean value)
    {
        return value ? TRUE : FALSE;
    }

    /**
     */
    public static final String toString(boolean value)
    {
        return String.valueOf(value);
    }
}
