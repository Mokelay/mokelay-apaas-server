package com.greatbee.base.util;

import org.apache.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * ʹ�� �����IP��ַ�͵�ǰʱ�� ��ɵ�32λʱ��Ψһ�������
 */
public class RandomGUIDUtil extends Object
{
    protected final Logger logger = Logger.getLogger(RandomGUIDUtil.class.getName());

    public String valueBeforeMD5 = "";
    public String valueAfterMD5 = "";
    private static Random myRand;
    private static SecureRandom mySecureRand;

    private static String s_id;
    private static final int PAD_BELOW = 0x10;
    private static final int TWO_BYTES = 0xFF;
    /*
  * Static block to take care of one time secureRandom seed.
  * It takes a few seconds to initialize SecureRandom.  You might
  * want to consider removing this static block or replacing
  * it with a "time since first loaded" seed to reduce this time.
  * This block will run only once per JVM instance.
    */

    static {
        RandomGUIDUtil.mySecureRand = new SecureRandom();
        long secureInitializer = RandomGUIDUtil.mySecureRand.nextLong();
        RandomGUIDUtil.myRand = new Random(secureInitializer);
        try {
            RandomGUIDUtil.s_id = InetAddress.getLocalHost().toString();
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }


    /*
    * Default constructor.  With no specification of security option,
    * this constructor defaults to lower security, high performance.
    */
    public RandomGUIDUtil()
    {
        getRandomGUIDUtil(false);
    }

    /*
    * Constructor with security option.  Setting secure true
    * enables each random number generated to be cryptographically
    * strong.  Secure false defaults to the standard Random function seeded
    * with a single cryptographically strong random number.
    */
    public RandomGUIDUtil(boolean secure)
    {
        getRandomGUIDUtil(secure);
    }

    /*
    * Method to generate the random GUID
    */
    private void getRandomGUIDUtil(boolean secure)
    {
        MessageDigest md5 = null;
        StringBuffer sbValueBeforeMD5 = new StringBuffer(128);

        try {
            md5 = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e) {
            logger.error("Error: " + e);
        }

        try {
            long time = System.currentTimeMillis();
            long rand = 0;

            if (secure) {
                rand = RandomGUIDUtil.mySecureRand.nextLong();
            }
            else {
                rand = RandomGUIDUtil.myRand.nextLong();
            }
            sbValueBeforeMD5.append(RandomGUIDUtil.s_id);
            sbValueBeforeMD5.append(":");
            sbValueBeforeMD5.append(Long.toString(time));
            sbValueBeforeMD5.append(":");
            sbValueBeforeMD5.append(Long.toString(rand));

            valueBeforeMD5 = sbValueBeforeMD5.toString();
            md5.update(valueBeforeMD5.getBytes());

            byte[] array = md5.digest();
            StringBuffer sb = new StringBuffer(32);
            for (int j = 0; j < array.length; ++j) {
                int b = array[j] & RandomGUIDUtil.TWO_BYTES;
                if (b < RandomGUIDUtil.PAD_BELOW) {
                    sb.append('0');
                }
                sb.append(Integer.toHexString(b));
            }

            valueAfterMD5 = sb.toString();

        }
        catch (Exception e) {
            logger.error("Error:" + e);
        }
    }

    /*
    * Convert to the standard format for GUID
    * (Useful for SQL Server UniqueIdentifiers, etc.)
    * Example: C2FEEEAC-CFCD-11D1-8B05-00600806D9B6
    */
    public String toString(int randDomNum)
    {
        String raw = valueAfterMD5.toUpperCase();
        StringBuffer sb = new StringBuffer(64);
        if (randDomNum == RANDOM_4) {
            sb.append(raw.substring(8, 12));
        }
        else if (randDomNum == RANDOM_8) {
            sb.append(raw.substring(0, 8));
        }
        else if (randDomNum == RANDOM_12) {
            sb.append(raw.substring(20));
        }
        else if (randDomNum == RANDOM_32) {
            sb.append(raw.substring(0, 8));
            sb.append("-");
            sb.append(raw.substring(8, 12));
            sb.append("-");
            sb.append(raw.substring(12, 16));
            sb.append("-");
            sb.append(raw.substring(16, 20));
            sb.append("-");
            sb.append(raw.substring(20));
        }

        return sb.toString();
    }

    public static String getRawGUID()
    {
        RandomGUIDUtil randomGUID = new RandomGUIDUtil();
        return randomGUID.valueAfterMD5;
    }

    public static String getGUID(int randomNum)
    {
        RandomGUIDUtil randomGUID = new RandomGUIDUtil();
        return randomGUID.toString(randomNum);
    }
    public static String getGUID(int randomNum, String append)
    {
        RandomGUIDUtil randomGUID = new RandomGUIDUtil();
        return append + randomGUID.toString(randomNum) + append;
    }

    public static final int RANDOM_4 = 4;
    public static final int RANDOM_8 = 8;
    public static final int RANDOM_12 = 12;
    public static final int RANDOM_32 = 32;

//    public static void main(String[] args)
//    {
//        System.out.println(RandomGUIDUtil.getGUID(RANDOM_4));
//        System.out.println(RandomGUIDUtil.getGUID(RANDOM_8));
//        System.out.println(RandomGUIDUtil.getGUID(RANDOM_12));
//        System.out.println(RandomGUIDUtil.getGUID(RANDOM_32));
//        System.out.println(RandomGUIDUtil.getRawGUID());
//    }
}
