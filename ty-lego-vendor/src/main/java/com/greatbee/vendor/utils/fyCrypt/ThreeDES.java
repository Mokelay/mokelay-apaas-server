package com.greatbee.vendor.utils.fyCrypt;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.UnsupportedEncodingException;
import java.security.Key;
public class ThreeDES {
    public static void main(String[] args) throws Exception {

//        String src = "scIa4cdqA+sjSNLKq8bZvFNT5QWrHseNhv3WxFlohblvi0KWjWf0H1aN4f1+Tz1rWiLYfLG6Coq6MWd6WTmn/yv0GaAeJ+NDovCrf1hnIE0ZHjFgfF2c1Scr/eZpcPLI16c2HjHj1zmtR0HWlKE7W26dTxKG3hviUq/Ck/0ZIghkGDMtFDfvEv7TOYZoBD/JzuWEc+bhQV2sZMPdjZMgsaoJ900gSOcKOziCRvSvLI8oFPIj+Tl5n8GbL++nE5YSXdg3jRip2mJIQPdCms8OZGUyb1wxYwqs/YMWw2uwA8PfHbyQ1dBn6iKs7NM2T9jh+G5UYAagmRUirOzTNk/Y4fDlDMy4FgYBY6P/3h4eUX/3ZgpPC0i4Fvgjizy+mEM+R9EUQzU1MXk8R6G5o1tf/gMk5z6JZTqL3TbPXPsU2/MIr1LA+PJ7iT4E3rBIortVQUVVWEKd6Apq1CWtBLx53MFVDDj1TDIyef2nWLHNBrrsHjNEgZFFXnoRspKvjRjl+UJFL3oFPp4BK8a0c8cE3B1Rwx4zW3/dEzIlTrCeHi6eXPbTKI4JHZBTXTbRO9v6ezmx+fFkKNb5QkUvegU+nlfZ2MC3QN7OayMypf3mrnmCsdzHIXSF8Whcs4/LDHMq8RzOpFk8KRetwBxJN5Wj3UE61mNz9odfmdgbG8IVHMmtwBxJN5Wj3VA3aBSsyh3/HMKCOXWYXc+K/EKZvcyIz6Zaza8DO4EZ6N4aAiIMn96NOepGWH6hw8T2DQtDcSfDDylzh9dY51bvqyZKBAiMQXGXLm6COmpZzEfsg9rQFHA=";
//        String key="W8S1s4d9U7@2F4b5";
////        String key="W9S1x4ddd@2F8b5";
//        String a = decryption(key,src);
//        System.out.println(a);
//        String encryption = encryption(key, a);
//        System.out.printf("加密:" + encryption);
//        String b = decryption(key, encryption);
//        System.out.println("解密:" + b);


        String src = "hello word";
        String key="W8S1s4d9U7@2F4b5";
        System.out.println("原始明文："+src);
        String encryptStr = encryption(key,src);
        System.out.println("密文："+encryptStr);
        String decryptStr = decryption(key,encryptStr);
        System.out.println("明文: "+decryptStr);


    }

    public static String decryption(String key,String src){
        String result = "";
        byte[]keybyte =  key.getBytes();
        byte[] newKey = new byte[24];
        System.arraycopy(keybyte, 0, newKey, 0, 16);
        System.arraycopy(keybyte, 0, newKey, 16, 8);
        byte[] data= Base64.decode(src);
        byte[] str4 = new byte[0];
        try {
            str4 = ees3DecodeECB(newKey, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            result = new String(str4, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static String encryption(String key,String src){
        String result = "";
        byte[]keybyte =  key.getBytes();
        byte[] newKey = new byte[24];
        System.arraycopy(keybyte, 0, newKey, 0, 16);
        System.arraycopy(keybyte, 0, newKey, 16, 8);
        try {
            byte[] data = src.getBytes("UTF-8");
            byte[] str4 = new byte[0];
            str4 = des3EncodeECB(newKey, data);
            result = Base64.encode(str4);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }




    /**
     * ECB,?IV
     * @param key ?
     * @param data
     * @return Base64
     * @throws Exception
     */
    public static byte[] des3EncodeECB(byte[] key, byte[] data)
            throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, deskey);
        byte[] bOut = cipher.doFinal(data);
        return bOut;
    }
    /**
     * ECB,?IV
     * @param key ?
     * @param data Base64
     * @return
     * @throws Exception
     */
    public static byte[] ees3DecodeECB(byte[] key, byte[] data)
            throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, deskey);
        byte[] bOut = cipher.doFinal(data);
        return bOut;
    }
    /**
     * CBC
     * @param key ?
     * @param keyiv IV
     * @param data
     * @return Base64
     * @throws Exception
     */
    public static byte[] des3EncodeCBC(byte[] key, byte[] keyiv, byte[] data)
            throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(keyiv);
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] bOut = cipher.doFinal(data);
        return bOut;
    }
    /**
     * CBC
     * @param key ?
     * @param keyiv IV
     * @param data Base64
     * @return
     * @throws Exception
     */
    public static byte[] des3DecodeCBC(byte[] key, byte[] keyiv, byte[] data)
            throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(keyiv);
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
        byte[] bOut = cipher.doFinal(data);
        return bOut;
    }


}