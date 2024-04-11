package com.mokelay.vendor.lego.thirdparty.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * HmacUtil
 *
 * @author xiaobc
 * @date 18/9/6
 */
public class HmacUtil {

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

//    /**
//     * 使用 HMAC-SHA1 签名方法对data进行签名
//     *
//     * @param data
//     *            被签名的字符串
//     * @param key
//     *            密钥
//     * @return
//    加密后的字符串
//     */
//    public static String hmacSha1(String data, String key) {
//        byte[] result = null;
//        try {
//            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
//            SecretKeySpec signinKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
//            //生成一个指定 Mac 算法 的 Mac 对象
//            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
//            //用给定密钥初始化 Mac 对象
//            mac.init(signinKey);
//            //完成 Mac 操作
//            byte[] rawHmac = mac.doFinal(data.getBytes());
//            result = Base64.encodeBase64(rawHmac);
//
//        } catch (NoSuchAlgorithmException e) {
//            System.err.println(e.getMessage());
//        } catch (InvalidKeyException e) {
//            System.err.println(e.getMessage());
//        }
//        if (null != result) {
//            return new String(result);
//        } else {
//            return null;
//        }
//    }

    public static String hmacSha1(String key, String datas) {
        String reString = null;

        try {
            byte[] data = key.getBytes("UTF-8");
            SecretKey secretKey = new SecretKeySpec(data, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(secretKey);
            byte[] text = datas.getBytes("UTF-8");
            byte[] text1 = mac.doFinal(text);
            reString = new String(Base64.encodeBase64String(text1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reString;
    }



//    /**
//     * 测试
//     * @param args
//     */
//    public static void main(String[] args) {
//        String genHMAC = hmacSha1("111", "T1V5wNkLNFbj8LZZY36dQAzfZvFnQlhbAmYJA451CPJbGbP8hVirswTRRxabn5lD");
//        System.out.println(genHMAC.length()); //28
//        System.out.println(genHMAC);  // O5fviq3DGCB5NrHcl/JP6+xxF6s=
//    }

}
