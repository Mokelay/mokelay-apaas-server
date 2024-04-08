package com.greatbee.vendor.utils;

import com.greatbee.vendor.utils.rsaJavaTransferNet.Base64Helper;
import com.greatbee.vendor.utils.rsaJavaTransferNet.StringHelper;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;



/**
 * CryptUtil
 * 加密 解密工具类
 *
 * @author xiaobc
 * @date 18/10/24
 */
public class CryptUtil {

    Logger logger = Logger.getLogger(CryptUtil.class);

    /**
     * 转换成十六进制字符串
     *
     * @param key
     * @return lee on 2017-08-09 10:54:19
     */
    public static byte[] hex(String key) {
        String f = DigestUtils.md5Hex(key);
        byte[] bkeys = new String(f).getBytes();
        byte[] enk = new byte[24];
        for (int i = 0; i < 24; i++) {
            enk[i] = bkeys[i];
        }
        return enk;
    }

    /**
     * 3DES加密
     *
     * @param key    密钥，24位
     * @param srcStr 将加密的字符串
     * @return lee on 2017-08-09 10:51:44
     */
    public static String encode3Des(String key, String srcStr) {
        byte[] keybyte = hex(key);
        byte[] src = srcStr.getBytes();
        try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, "DESede");
            //加密
            Cipher c1 = Cipher.getInstance("DESede");
            c1.init(Cipher.ENCRYPT_MODE, deskey);

            String pwd = Base64.encodeBase64String(c1.doFinal(src));
//           return c1.doFinal(src);//在单一方面的加密或解密
            return pwd;
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 3DES解密
     *
     * @param key    加密密钥，长度为24字节
     * @param desStr 要解密的字符串
     * @return lee on 2017-08-09 10:52:54
     */
    public static String decode3Des(String key, String desStr) {
        Base64 base64 = new Base64();
        byte[] keybyte = hex(key);
        byte[] src = base64.decode(desStr);

        try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, "DESede");
            //解密
            Cipher c1 = Cipher.getInstance("DESede");
            c1.init(Cipher.DECRYPT_MODE, deskey);
            String pwd = new String(c1.doFinal(src));
//            return c1.doFinal(src);
            return pwd;
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }


    //==============下面是 RSA  加解密=====================

    public static final String CHARSET = "UTF-8";
    public static final String RSA_ALGORITHM = "RSA";

    /**
     * 创建密钥对
     *
     * @param keySize
     * @return
     */
    public static Map<String, String> createKeys(int keySize) {
        //为RSA算法创建一个KeyPairGenerator对象
        KeyPairGenerator kpg;
        try {
            kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such algorithm-->[" + RSA_ALGORITHM + "]");
        }

        //初始化KeyPairGenerator对象,密钥长度
        kpg.initialize(keySize);
        //生成密匙对
        KeyPair keyPair = kpg.generateKeyPair();
        //得到公钥
        Key publicKey = keyPair.getPublic();
        String publicKeyStr = Base64.encodeBase64URLSafeString(publicKey.getEncoded());
        //得到私钥
        Key privateKey = keyPair.getPrivate();
        String privateKeyStr = Base64.encodeBase64URLSafeString(privateKey.getEncoded());
        Map<String, String> keyPairMap = new HashMap<String, String>();
        keyPairMap.put("publicKey", publicKeyStr);
        keyPairMap.put("privateKey", privateKeyStr);

        return keyPairMap;
    }

    /**
     * 得到公钥
     *
     * @param publicKey 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过X509编码的Key指令获得公钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
        RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
        return key;
    }

    /**
     * 得到私钥
     *
     * @param privateKey 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static RSAPrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过PKCS#8编码的Key指令获得私钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
        RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
        return key;
    }

    /**
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     */
    public static String publicEncrypt(String data, RSAPublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), publicKey.getModulus().bitLength()));
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 私钥解密
     *
     * @param data
     * @param privateKey
     * @return
     */

    public static String privateDecrypt(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), privateKey.getModulus().bitLength()), CHARSET);
        } catch (Exception e) {
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 私钥加密
     *
     * @param data
     * @param privateKey
     * @return
     */

    public static String privateEncrypt(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), privateKey.getModulus().bitLength()));
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 公钥解密
     *
     * @param data
     * @param publicKey
     * @return
     */

    public static String publicDecrypt(String data, RSAPublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), publicKey.getModulus().bitLength()), CHARSET);
        } catch (Exception e) {
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
        int maxBlock = 0;
        if (opmode == Cipher.DECRYPT_MODE) {
            maxBlock = keySize / 8;
        } else {
            maxBlock = keySize / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try {
            while (datas.length > offSet) {
                if (datas.length - offSet > maxBlock) {
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                } else {
                    buff = cipher.doFinal(datas, offSet, datas.length - offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
        } catch (Exception e) {
            throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", e);
        }
        byte[] resultDatas = out.toByteArray();
        IOUtils.closeQuietly(out);
        return resultDatas;
    }

    //================ java 转 c# ===================
    private static String getRSAPrivateKeyAsNetFormat(byte[] encodedPrivkey) {
        try {
//            StringBuffer buff = new StringBuffer(1024);
            StringBuffer buff = new StringBuffer(2048);

            PKCS8EncodedKeySpec pvkKeySpec = new PKCS8EncodedKeySpec(
                    encodedPrivkey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPrivateCrtKey pvkKey = (RSAPrivateCrtKey) keyFactory
                    .generatePrivate(pvkKeySpec);

            buff.append("<RSAKeyValue>");
            buff.append("<Modulus>"
                    + b64encode(removeMSZero(pvkKey.getModulus()
                    .toByteArray())) + "</Modulus>");

            buff.append("<Exponent>"
                    + b64encode(removeMSZero(pvkKey.getPublicExponent()
                    .toByteArray())) + "</Exponent>");

            buff.append("<P>"
                    + b64encode(removeMSZero(pvkKey.getPrimeP().toByteArray()))
                    + "</P>");

            buff.append("<Q>"
                    + b64encode(removeMSZero(pvkKey.getPrimeQ().toByteArray()))
                    + "</Q>");

            buff.append("<DP>"
                    + b64encode(removeMSZero(pvkKey.getPrimeExponentP()
                    .toByteArray())) + "</DP>");

            buff.append("<DQ>"
                    + b64encode(removeMSZero(pvkKey.getPrimeExponentQ()
                    .toByteArray())) + "</DQ>");

            buff.append("<InverseQ>"
                    + b64encode(removeMSZero(pvkKey.getCrtCoefficient()
                    .toByteArray())) + "</InverseQ>");

            buff.append("<D>"
                    + b64encode(removeMSZero(pvkKey.getPrivateExponent()
                    .toByteArray())) + "</D>");
            buff.append("</RSAKeyValue>");

            return buff.toString().replaceAll("[ \t\n\r]", "");
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }

    private static String getRSAPublicKeyAsNetFormat(byte[] encodedPrivkey) {
        try {
//            StringBuffer buff = new StringBuffer(1024);
            StringBuffer buff = new StringBuffer(2048);

            PKCS8EncodedKeySpec pvkKeySpec = new PKCS8EncodedKeySpec(
                    encodedPrivkey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKey pukKey = (RSAPublicKey) keyFactory
                    .generatePublic(new X509EncodedKeySpec(encodedPrivkey));

            buff.append("<RSAKeyValue>");
            buff.append("<Modulus>"
                    + b64encode(removeMSZero(pukKey.getModulus()
                    .toByteArray())) + "</Modulus>");
            buff.append("<Exponent>"
                    + b64encode(removeMSZero(pukKey.getPublicExponent()
                    .toByteArray())) + "</Exponent>");
            buff.append("</RSAKeyValue>");
            return buff.toString().replaceAll("[ \t\n\r]", "");
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }

    public static String encodePublicKeyToXml(PublicKey key) {
        if (!RSAPublicKey.class.isInstance(key)) {
            return null;
        }
        RSAPublicKey pubKey = (RSAPublicKey) key;
        StringBuilder sb = new StringBuilder();

        sb.append("<RSAKeyValue>");
        sb.append("<Modulus>").append(
                com.sun.org.apache.xerces.internal.impl.dv.util.Base64.encode(pubKey.getModulus().toByteArray())).append(
                "</Modulus>");
        sb.append("<Exponent>").append(
                com.sun.org.apache.xerces.internal.impl.dv.util.Base64.encode(pubKey.getPublicExponent().toByteArray()))
                .append("</Exponent>");
        sb.append("</RSAKeyValue>");
        return sb.toString();
    }

    private static byte[] removeMSZero(byte[] data) {
        byte[] data1;
        int len = data.length;
        if (data[0] == 0) {
            data1 = new byte[data.length - 1];
            System.arraycopy(data, 1, data1, 0, len - 1);
        } else
            data1 = data;

        return data1;
    }

    private static String b64encode(byte[] data) {
        String b64str = new String(com.sun.org.apache.xerces.internal.impl.dv.util.Base64.encode(data));
        return b64str;
    }

    private static byte[] b64decode(String data) {
        byte[] decodeData = com.sun.org.apache.xerces.internal.impl.dv.util.Base64.decode(data);
        return decodeData;
    }
    //================ java 转 c# end ===================

    //================ c# 转 java  start ===================
    public static PublicKey decodePublicKeyFromXml(String xml) {
        xml = xml.replaceAll("\r", "").replaceAll("\n", "");
        BigInteger modulus = new BigInteger(1, Base64Helper.decode(StringHelper
                .GetMiddleString(xml, "<Modulus>", "</Modulus>")));
        BigInteger publicExponent = new BigInteger(1,
                Base64Helper.decode(StringHelper.GetMiddleString(xml,
                        "<Exponent>", "</Exponent>")));
        RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(modulus,
                publicExponent);
        KeyFactory keyf;
        try {
            keyf = KeyFactory.getInstance("RSA");
            return keyf.generatePublic(rsaPubKey);
        } catch (Exception e) {
            return null;
        }
    }
    //================ c# 转 java  end ===================


    //    测试加解密

    /**
     * !!!!!!!注意我们生成RSA 公私钥 是通过 org.apache.commons.codec.binary.Base64 编码的，转成c# 格式的加解密是通过
     * com.sun.org.apache.xerces.internal.impl.dv.util.Base64 编码的
     * @param args
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static void main(String[] args) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String s = "hello word";

        String desKey = "RZ_xKrC2ST(&SfpA";
        //3des 加解密
        String cryptStr = encode3Des(desKey,s);
        System.out.println("3des 密文："+cryptStr);
        String str = decode3Des(desKey,cryptStr);
        System.out.println("3des 明文: "+str);

        System.out.println("===========================");

        Map keys = createKeys(2048);
//        String publicKey = (String) keys.get("publicKey");
//        String privateKey = (String) keys.get("privateKey");

        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCKPcskvtZCdZ2BADma7XcSJvMW-HKeYuc593NuGzg97C0nBvQ0JmjPCc5AFq1ibUI1chAWjni98z4iDzsSZhnyZ7r9C3qur20guLCJ4VFxkPsUg4HTq01WPDy49KbnsMM48YwxtIhgquufx6PLFvNd56EOcW2S9TPCFgapBtuZ6wIDAQAB";
        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIo9yyS-1kJ1nYEAOZrtdxIm8xb4cp5i5zn3c24bOD3sLScG9DQmaM8JzkAWrWJtQjVyEBaOeL3zPiIPOxJmGfJnuv0Leq6vbSC4sInhUXGQ-xSDgdOrTVY8PLj0puewwzjxjDG0iGCq65_Ho8sW813noQ5xbZL1M8IWBqkG25nrAgMBAAECgYAD7xg91nRwdbpitmftQb62tIcMa0uPuE7ONn2Bb6LdGEB8sNT4OvOWmLfW2Q5cE7HjpXhRvAIxnZ9yBq7uqhso4JyBhZftRB0IBH_Xq72BLUlKZ-vyhzgZ8X9L74ZHduElaD9qu9TCpUMwKiHu_c7KFSXCVcMSFuh7iE0AJ77BKQJBAP2lf63iBGe1OZJ16yLmnS7aJxSXETfSQ4xr8X7xMULoWNaO-pZEzfjzCdStFv0tjSvOIuw0HDIras0tLwBMTgcCQQCLhiqUtyhIxpKFX4VoMMcY5ugM_4_qIQEsAf99F7i9qN8XK3nus8pr_Q6PtRLZbOHmiQFGKfkqugoIrGQZGlv9AkBTNgQZC1jenPlyLvUg0f8m67J-csDXS0eVvaPQPVgn1wmt-eIDgjcAQNssxkwQau0xPcL_Mmk9nDjBbt1If7_pAkA7WKIcB5wW-H-yQ2MXYkgNi4oAnWO1jrxU37SwnLpz0bSywkdnBQqAzG65KagO24Vq7bOhCJF3XxKNheY91zxxAkEAhEq0JBk7spDOXizE-mESd7zwJTE1GWc9rL6ihMhzvXND6_EJkfdV4jqZYpfNBvYibeqVRrxUaj8S3FZ3D0WqCQ";

        System.out.println("publicKey=" + publicKey);
        System.out.println("privateKey=" + privateKey);

        RSAPublicKey pk = getPublicKey(publicKey);
        System.out.println("modulus=" + pk.getModulus());
        System.out.println("exponent=" + pk.getPublicExponent());

        //        测试加解密

        String pass = publicEncrypt(s, getPublicKey(publicKey));
        System.out.println("pass="+pass);
        System.out.println(privateDecrypt(pass, getPrivateKey(privateKey)));

        //java版秘钥转c#
        String netPk = getRSAPublicKeyAsNetFormat(Base64.decodeBase64(publicKey));//！！！！！！！！转成c# 要用appache的base64
        System.out.println("netPk=" + netPk);

        //c#版转java秘钥
        RSAPublicKey pk2 = (RSAPublicKey) decodePublicKeyFromXml(netPk);
        String pass2 = publicEncrypt(s, pk2);
        System.out.println("pass2="+pass2);
        //私钥解密
        System.out.println(privateDecrypt(pass2, getPrivateKey(privateKey)));

        System.out.println("===========================");

//        String tes = "MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBAL8z2QlXCL6w7rvY0Gbl8ARtQSXY+pEW5hlUHlmspqHt4k8/SkoF796gDqk4yyOcoWhkZWLPPugK35Mn7V+m5Jyfu8C0gVKOfWOA8A0T4hxV2ThAoMUq7QtB2K6s9AoumrxDfAkMBbsXEHYwfD/hxr/3DQ3lUvSFB6BnhiHEOyzpAgMBAAECgYEAol/9qRjorEjF9XEjSr9rHddKxEGIST8RGeF+BNnCiTHkRziQdlykYIO876jzmsKhsG3STB+EZLsXM3ls9RZefcsPF5mLOCSOCow3DikfCtAy4hntsU9JwpuYE0V4A+Sgfd24fatqbu+JxE2nvpSbAPczDOgBFPNfYBkhMiuZ/iECQQDzUeq7lFcIE4uWhRGveVFjNAGuSsW+q9GOwO7tS5YwuAIQ2M+XgYGRFo8xMC6V/9SfqJtmSU1zk72pMlYufIqHAkEAySqkcKbWuobq5I9KSQISq2qCuGKtj/iUFho4PCD1YxhnQ7gcHA4OpS1dRFjtXJYQPTX9be+mmypsCFIyofE5DwJBAPGZ20wahTh9v9Lbmq3z9n5ce3bGxAcJsHDg3d09eooxi8uSnL5BV5frII+k2f0TI9rMnlE4Y/FpN5+zXaOXAi0CQQCs3Aqfjo23jJWtPv/LSo+2YnjfblPMAgNmFrO532xc8axSgZMN/HpTL28UewHD7GMZ5hnWbPcSIFrir5c4luq7AkEAi90WdnZVPxtSTqkkLYbnh4Ro2WhdwRjkfyBxBZZx8hfaM6MfLPi3A0rw9DPOSB4M/BMchtEh3bXuI7bue2tG+A==";
//        byte[] temp = b64decode(tes);
//        String ver = getRSAPrivateKeyAsNetFormat(temp);// 转换私钥
//
//        String tes1 = "MIGfMA0GCSqGSIb4DQEBAQUAA4GNADCBiQKBgQC/M9kJVwi+sO672NBm5fAEbUEl2PqRFuYZVB5ZrKah7eJPP0pKBe/eoA6pOMsjnKFoZGVizz7oCt+TJ+1fpuScn7vAtIFSjn1jgPANE+IcVdk4QKDFKu0LQdiurPQKLpq8Q3wJDAW7FxB2MHw/4ca/9w0N5VL0hQegZ4YhxDss6QIDAQAB";
//        byte[] temp1 = b64decode(tes1);
//        String ver1 = getRSAPublicKeyAsNetFormat(temp1);// 转换公钥
//        System.out.println(ver);
//        System.out.println(ver1);

    }

}
