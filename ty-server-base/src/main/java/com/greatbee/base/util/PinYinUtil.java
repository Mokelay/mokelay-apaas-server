package com.greatbee.base.util;

/**
 * ƴ������
 *
 * @author CarlChen
 * @version 1.00 2007-9-13 11:11:28
 */
public class PinYinUtil {
    private static char[] chartable =
            {
//                    '��', '��', '��', '��', '��', '��', '��', '��', '��',
//                    '��', '��', '��', '��', '��', 'Ŷ', 'ž', '��', 'Ȼ',
//                    '��', '��', '��', '��', '��', '��', 'ѹ', '��', '��'
            };

    private static char[] alphatable =
            {
                    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
                    'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
                    'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
            };

    private static int[] table = new int[27];


    static {
        for (int i = 0; i < 27; ++i) {
            table[i] = gbValue(chartable[i]);
        }
    }

    /**
     *
     * @param ch
     * @return
     */
    public static char Char2Alpha(char ch) {
        if (ch >= 'a' && ch <= 'z') {
            return (char) (ch - 'a' + 'A');
        }
        if (ch >= 'A' && ch <= 'Z') {
            return ch;
        }
        if (ch >= '0' && ch <= '9') {
            return ch;
        }
        int gb = gbValue(ch);
        if (gb < table[0]) {
            return '0';
        }
        int i;
        for (i = 0; i < 26; ++i) {
            if (match(i, gb)) {
                break;
            }
        }
        if (i >= 26) {
            return '0';
        } else {
            return alphatable[i];
        }
    }

    /**
     *
     * @param SourceStr
     * @return
     */
    public static String String2PinYin(String SourceStr) {
        if (StringUtil.isValid(SourceStr)) {
            String Result = "";
            int StrLength = SourceStr.length();
            int i;
            try {
                for (i = 0; i < StrLength; i++) {
                    Result += Char2Alpha(SourceStr.charAt(i));
                }
            }
            catch (Exception e) {
                Result = "";
            }
            return Result;
        }
        return null;
    }

    private static boolean match(int i, int gb) {
        if (gb < table[i]) {
            return false;
        }
        int j = i + 1;
        //��ĸZʹ����������ǩ
        while (j < 26 && (table[j] == table[i])) {
            ++j;
        }
        if (j == 26) {
            return gb <= table[j];
        } else {
            return gb < table[j];
        }
    }


    /**
     * ȡ�����ֵı���
     *
     * @param ch
     * @return
     */
    private static int gbValue(char ch) {
        String str = new String();
        str += ch;
        try {
            byte[] bytes = str.getBytes("GB2312");
            if (bytes.length < 2) {
                return 0;
            }
            return (bytes[0] << 8 & 0xff00) + (bytes[1] & 0xff);
        }
        catch (Exception e) {
            return 0;
        }
    }


//    public static void main(String[] args) {
////        PinYinUtil obj1 = new PinYinUtil();
//    }
}