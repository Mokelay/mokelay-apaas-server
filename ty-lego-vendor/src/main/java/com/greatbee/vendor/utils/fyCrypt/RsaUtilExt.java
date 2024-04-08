package com.greatbee.vendor.utils.fyCrypt;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import javax.crypto.Cipher;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA加密解密工具类
 * <P>Title: RsaUtilExt</P>
 * <P>Description: create by XSDToJava V1.0</P>
 * <P>Copyright: Copyright (c) 2015 </P>
 * <P>Company: mftec </P>
 * 
 * @author 
 * @version 1.0
 * @create 2015年3月24日
 * @history
 */
public class RsaUtilExt {
	public static final int KEY_SIZE = 2048;//key长度大小
	public static final String KEY_ALGORTHM = "RSA";//
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
	public static final String PUBLIC_KEY = "RSAPublicKey";// 公钥
	public static final String PRIVATE_KEY = "RSAPrivateKey";// 私钥

	/**
	 * 初始化密钥
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> initKey() throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORTHM);
		//keyPairGenerator.initialize(KEY_SIZE);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		keyPairGenerator.initialize(KEY_SIZE, new SecureRandom(sdf.format(new Date()).getBytes()));
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		// 公钥
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		// 私钥
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		Map<String, Object> keyMap = new HashMap<String, Object>(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}

	/**
	 * 取得公钥，并转化为String类型
	 * 
	 * @param keyMap
	 * @return
	 * @throws Exception
	 */
	public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return encodeBase64(key.getEncoded());
	}

	/**
	 * 取得私钥，并转化为String类型
	 * 
	 * @param keyMap
	 * @return
	 * @throws Exception
	 */
	public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return encodeBase64(key.getEncoded());
	}

	/**
	 * 用私钥加密
	 * 
	 * @param data 加密数据
	 * @param key 密钥
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String key) throws Exception {
		// 解密密钥
		byte[] keyBytes = decodeBase64(key);
		// 取私钥
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
		Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
		// 对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}
	
	/**
	 * 用私钥解密
	 * 
	 * @param data 解密数据
	 * @param key 密钥
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception {
		// 解密密钥
		byte[] keyBytes = decodeBase64(key);
		// 取私钥
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
		Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
		// 对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}

	/**
	 * 用公钥加密
	 * 
	 * @param data 加密数据
	 * @param key 密钥
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPublicKey(byte[] data, String key) throws Exception {
		// 对公钥解密
		byte[] keyBytes = decodeBase64(key);
		// 取公钥
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
		Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
		// 对数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}

	/**
	 * 用公钥解密
	 * 
	 * @param data 加密数据
	 * @param key 密钥
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPublicKey(byte[] data, String key) throws Exception {
		// 对私钥解密
		byte[] keyBytes = decodeBase64(key);
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
		Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
		// 对数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}

	/**
	 * 用私钥对信息生成数字签名
	 * 
	 * @param data //加密数据
	 * @param privateKey //私钥
	 * @return
	 * @throws Exception
	 */
	public static String sign(byte[] data, String privateKey) throws Exception {
		// 解密私钥
		byte[] keyBytes = decodeBase64(privateKey);
		// 构造PKCS8EncodedKeySpec对象
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
		// 指定加密算法
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
		// 取私钥匙对象
		PrivateKey privateKey2 = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
		// 用私钥对信息生成数字签名
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(privateKey2);
		signature.update(data);
		return encodeBase64(signature.sign());
	}

	/**
	 * 校验数字签名
	 * 
	 * @param data 加密数据
	 * @param publicKey 公钥
	 * @param sign 数字签名
	 * @return
	 * @throws Exception
	 */
	public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
		// 解密公钥
		byte[] keyBytes = decodeBase64(publicKey);
		// 构造X509EncodedKeySpec对象
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
		// 指定加密算法
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
		// 取公钥匙对象
		PublicKey publicKey2 = keyFactory.generatePublic(x509EncodedKeySpec);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(publicKey2);
		signature.update(data);
		// 验证签名是否正常
		return signature.verify(decodeBase64(sign));
	}
	
	/**
     * base64编码
     *
     * @param input
     * @return output with base64 encoded
     * @throws Exception
     */
    public static String encodeBase64(byte[] input) throws Exception {
        Class clazz = Class
                .forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
        Method mainMethod = clazz.getMethod("encode", byte[].class);
        mainMethod.setAccessible(true);
        Object retObj = mainMethod.invoke(null, new Object[]{input});
        return (String) retObj;
    }

    /**
     * base64解码
     *
     * @param input
     * @return
     * @throws Exception
     */
    public static byte[] decodeBase64(String input) throws Exception {
        Class clazz = Class
                .forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
        Method mainMethod = clazz.getMethod("decode", String.class);
        mainMethod.setAccessible(true);
        Object retObj = mainMethod.invoke(null, input);
        return (byte[]) retObj;
    }
    
    /** 
     * 公钥转换成C#格式 
     * @param key 
     * @return 
     * @throws Exception 
     */  
    public static String encodePublicKeyToXml(PublicKey key) throws Exception {  
        if (!RSAPublicKey.class.isInstance(key)) {  
            return null;  
        }  
        RSAPublicKey pubKey = (RSAPublicKey) key;  
        StringBuilder sb = new StringBuilder();  
  
        sb.append("<RSAKeyValue>");  
        sb.append("<Modulus>")  
                .append(encodeBase64(removeMSZero(pubKey.getModulus()  
                        .toByteArray()))).append("</Modulus>");  
        sb.append("<Exponent>")  
                .append(encodeBase64(removeMSZero(pubKey.getPublicExponent()  
                        .toByteArray()))).append("</Exponent>");  
        sb.append("</RSAKeyValue>");  
        return sb.toString();  
    }  
    
    /** 
     * @param data 
     * @return 
     */  
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
    
    /** 
     * 返回包含模数modulus和指数exponent的haspMap 
     * @return 
     * @throws MalformedURLException 
     * @throws DocumentException 
     */  
    public static HashMap<String,String> rsaParameters(String xmlPublicKey) throws MalformedURLException, DocumentException{  
        HashMap<String ,String> map = new HashMap<String, String>();   
        Document doc = DocumentHelper.parseText(xmlPublicKey);  
        String mudulus = (String) doc.getRootElement().element("Modulus").getData();  
        String exponent = (String) doc.getRootElement().element("Exponent").getData();  
        map.put("mudulus", mudulus);  
        map.put("exponent", exponent);  
        return map;  
    }  
    
    /** 
     * 返回RSA公钥 
     * @param modulus
     * @param exponent 
     * @return 
     */  
    public static PublicKey getPublicKey(String modulus, String exponent){  
        try {   
            byte[] m = decodeBase64(modulus);  
            byte[] e = decodeBase64(exponent);  
            BigInteger b1 = new BigInteger(1,m);    
            BigInteger b2 = new BigInteger(1,e);    
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");    
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);    
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);    
        } catch (Exception e) {    
            e.printStackTrace();    
            return null;    
        }     
    }  
    
	public static void main(String[] args) throws Exception {
//		Map<String, Object> keyMap = RsaUtilExt.initKey();
//		String publicKey =RsaUtilExt.getPublicKey(keyMap);
//		String privateKey = RsaUtilExt.getPrivateKey(keyMap);
//		String publicKeyXml = encodePublicKeyToXml((PublicKey) keyMap.get(PUBLIC_KEY));
//
//		System.out.println("publicKey="+publicKey);
//		System.out.println("privateKey="+privateKey);
//		System.out.println("publicKeyXml="+publicKeyXml);

//		String signStr = "545sdgwetwet";
//		System.out.println("原文："+signStr);
//		String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmXAz+DgSRpzQ25BfdFFSE1+QAnMEd9NTZBvK7M+REhvPEjO+7QxXYbKwoOiiEka4SY1GVj/z0Kg6iaPCao3sgmpbS0yYkOOsj6q8JIjncXPbBmoJgnzz9p4kPUH/wsVYgA1zu3x59hgipqpOKf6QcCVjLc01CBUBZj6bbvsFuiqJNdp/E8wXDDAy8gebBHdR135ScLY+UbJrjIFnXvrxdXD2tRXYqqZss8Jrh38juYdWjoWuEwp3EqyA4qZ3aWHSeGrPmG2v/OlVdrDagi8l/ZbdqXKflGbNW+EXt7Y1Sy5mhFPy9F803hwof5HE9KhDVRdymzjGMU23CuwcqsFUeQIDAQAB";
//		String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCZcDP4OBJGnNDbkF90UVITX5ACcwR301NkG8rsz5ESG88SM77tDFdhsrCg6KISRrhJjUZWP/PQqDqJo8JqjeyCaltLTJiQ46yPqrwkiOdxc9sGagmCfPP2niQ9Qf/CxViADXO7fHn2GCKmqk4p/pBwJWMtzTUIFQFmPptu+wW6Kok12n8TzBcMMDLyB5sEd1HXflJwtj5RsmuMgWde+vF1cPa1FdiqpmyzwmuHfyO5h1aOha4TCncSrIDipndpYdJ4as+Yba/86VV2sNqCLyX9lt2pcp+UZs1b4Re3tjVLLmaEU/L0XzTeHCh/kcT0qENVF3KbOMYxTbcK7ByqwVR5AgMBAAECggEAMXdCydsJyUn061P38f/lfelo++Eop5ixYay0FvQzyKq+bxfPjYxU/2IkHzvgRBQYhV3ONzAW4kXZ2VW577Ahd0nidLwU02ES2J4qWXzpYGIBUmgM+MxfujnxZO5KDfihN2ce0GqoxvWw9hohaNhwqDblanalih17fgLhCT5T8lgSoOQjFgAPjRo2A+NNYvpEjgupql9mNIvVpmLK1yPwzR7jaUBY4RqWbWUd2QRg+gPpFnINIeXOoRLQ1/ar1Z+7xvRjfYfWnSrtbNWXA1ORcOR1eRY5mvJZ3c2O2P1C0ubpvI8FXnNV3U3chTo9SJTmDoVgaM/WE+xLUlk6LBYBoQKBgQDqNOdsWuUq0BZbZIpFCIaiBNp6y8QsPPjxKzDUYPqnuvFOcMSWyfwnL5o9/RQf2rb3g7W/x+ovBIlGlufAdWm3/ohmU1cWSFa7M6nNoKgFtB16uWLgZZgI6XPyrn6/utMCEIeqKFnt+oicwn4H6ewLYnZoHW1zPY1ErzwSWQQYZwKBgQCnt0ufJvrS32li3lDUBJJHnbZDXFRzWpanChHX/CyWSu68bV8XyoW7xlG5DMQShtYmTtI2wGEfjQFf+BkCmwnTPwtJjK5xtVRXFxSTBm2TMgq1dRsOK0UpQWKem4kgpgyHmNE0X+6K1Mwh8oCQUleeXU+IhrWHDnn8T2tCkq6gHwKBgQDTWma1y9kfJ6XlV/fHkIge0g5v3k7sNSHaUcXBArtwfi2hQfbGtQSOQFELO8XbcT7IYt2zci+NuqQ/RZNihRKgblydKvxeKW3LhjXoUOEQNJid1Yq7QjNPZ42xJXNQhsJBDLXBAqUVM4/xFzsw3RKcu4BrOJM6+gLwRssJK7ecZQKBgCgHjZsM2Kzf+e1JFo2kEa3aLJhjPxzRMEfqFVwxqgqtE9IRYLLy+XxZ10ONknbBi/dUtMOwov2+z+cx9fYzPuMWlwEU/MXS+7BVp+l3jUumk/u/WZfGKBmyq3EjulT6jN8IUFZp1yt3oskvchQAMz1vE/6H+GelwuO/qOAY1mkNAoGBAMh3p18tk60BHXG2utzEceXug05wUOfWfGhzqt7G/++B8qMX7Uz1Lc2hpycIarEGYNy9haaGys8edAej8hvDVzNp4Pky2IkT8cVffFM1rMZyAxqGfy6lOOZf6HYD/NnOs3Qxdfm4e5K5KeJ03VThQ8S6g1FbBGwV4gJraxs4lRC+";
//		String publicKeyXml = "<RSAKeyValue><Modulus>mXAz+DgSRpzQ25BfdFFSE1+QAnMEd9NTZBvK7M+REhvPEjO+7QxXYbKwoOiiEka4SY1GVj/z0Kg6iaPCao3sgmpbS0yYkOOsj6q8JIjncXPbBmoJgnzz9p4kPUH/wsVYgA1zu3x59hgipqpOKf6QcCVjLc01CBUBZj6bbvsFuiqJNdp/E8wXDDAy8gebBHdR135ScLY+UbJrjIFnXvrxdXD2tRXYqqZss8Jrh38juYdWjoWuEwp3EqyA4qZ3aWHSeGrPmG2v/OlVdrDagi8l/ZbdqXKflGbNW+EXt7Y1Sy5mhFPy9F803hwof5HE9KhDVRdymzjGMU23CuwcqsFUeQ==</Modulus><Exponent>AQAB</Exponent></RSAKeyValue>";
//		System.out.println("公钥："+publicKey);
//		System.out.println("私钥："+privateKey);
//		System.out.println("公钥Xml："+publicKeyXml);
//		HashMap<String,String> pmap = rsaParameters(publicKeyXml);
//		PublicKey pkey = getPublicKey(pmap.get("mudulus"), pmap.get("exponent"));
//		byte[] data2 = RsaUtilExt.encryptByPublicKey(signStr.getBytes("UTF-8"), encodeBase64(pkey.getEncoded()));
////		byte[] data2 = decodeBase64("NolXYIYdYQFQucGD5vBTPgkfzhwQRbd26XsIllrTmEBpebrEPSYJAkcy49hRG2/ZPxEjeyMPDhiypg7mEAqXaKxcTYi4693hE7+CuvK0ymdw3/D8LrGZ/MO9PU8UMBMNoCZWIvEDSosP0G5Hl6W3KLAOzh5XxjRtAYYXL7Iw74FtuWa0+N16CNqI6YrdmKHqvR5a3+3e0DGxWdIvXwE+U4BpAuO+D0k4uv88tzr7JFfp8Mbl7NFTyLvsNsE3wrWDgUgLPPK44zp6gPHHQAurfMamPiI0kD/YcuA0k+uwrIwM4HVcBFTYZr0FrpSg54SxzSD96IOTIekWDlR2RVrmAQ==");
//		System.out.println("密文："+encodeBase64(data2));
//		byte[] source2 = RsaUtilExt.decryptByPrivateKey(data2, privateKey);
//		System.out.println("解密后明文："+new String(source2));



		String signStr = "hello word";
		System.out.println("原文："+signStr);
		String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhmtEANaBJ3giG8idQet39sdh40gqowpgfSrzyluaVb6BoVtT+CDMuNkXR5+HFkcOh7gF5t9ERYY5sFC5AvV+F8KGM6meKUHVV+pdLzQb7547Ll2wu0l8mSqarCiTOuMcUkt36f01zBMJHUPSXYRzL3EdF5jSu8k6lJZbwQpXq3S5Ha+fNukDgfwqxwngU/NAmjE+WQbJHIiDVzXKKzrexqbesdwlyxvBHzIJDotQ1Afi/ToTq6tTNQwuaFpmpVN2iz4Jp7l4SXhp0pPqjM61m0zWqB6Z12HIODE7l9ojvDtm/PIRIaITyVtfa2UsU9YHIlOKPDT8OjtmHlzVoXwT7wIDAQAB";
		String privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCGa0QA1oEneCIbyJ1B63f2x2HjSCqjCmB9KvPKW5pVvoGhW1P4IMy42RdHn4cWRw6HuAXm30RFhjmwULkC9X4XwoYzqZ4pQdVX6l0vNBvvnjsuXbC7SXyZKpqsKJM64xxSS3fp/TXMEwkdQ9JdhHMvcR0XmNK7yTqUllvBClerdLkdr5826QOB/CrHCeBT80CaMT5ZBskciINXNcorOt7Gpt6x3CXLG8EfMgkOi1DUB+L9OhOrq1M1DC5oWmalU3aLPgmnuXhJeGnSk+qMzrWbTNaoHpnXYcg4MTuX2iO8O2b88hEhohPJW19rZSxT1gciU4o8NPw6O2YeXNWhfBPvAgMBAAECggEABmMOZiPLGz8AD+XmYv13Q8iRhCUnsXuKvifOUa0slippKmuWf9G/KIFYM9UGrC5QKG68CQ5Oxibsks4jHFrh1OsvSUEj/L3eD0FZkCaftTEFitx0y1FglD6L/uYmqMOl0ts3JW0co0e8RSG12y+x8W0kfryKNtYG7IyAa5m8NK9TgzW6nmkjycL2kgTQnL9RxYCYI8tQUcEDZEx2+ma2LFnzCz6n/IjBYI2lv0PwPeXoqsh26Gr6S4TtgWUGgKA91W1qKVj3RBspvs3vRWqv4DKeqLnFIMMGINgMAJdfdRUuz9vLBRFVjP4sHCSUXRPO7A2JxQUR8k4AvZbW9OTkIQKBgQC7oDdLOOIUCreDzxs1uRrWfEv8Biwm6etetaYUlGdo63/J9zlQeitkiuH5QCskK182Y2my1NF4DCm5q1rcuxuFK8X/NdhWdYkqKRidiDKbp9Du3MqPOeFUd+gjUdPFQ4mxHBocNDCXCbYFv0xr5v7XNC/i+MgAe8qtPweV89iBpwKBgQC3Z1X6iXlawdo++aCEAKBOGRjqffoHOjtiWD4Ne2TCY0gDshxm5LC9JmWTbjdwaayS5QZVQeWiuSTMBKQhX41nH7x5CTdmy62oZkK8DdIWwSGRsVCZx76xlVuaxQHs/C0yliXRq8+l/aePvXfoklBdKCQpRdKt3F5iwCDBGQRUeQKBgDKq54O2zsaexkQgD7cIT8kQM+zRNeckUF15LGpuUSayQgYmbyHefA1gu/l1bLENzV6ApjCW7CJBFpt/PMBS/vGNiqCKx+tBPlNWWcFg4FnMjc2TblgTR1hqMRQoqsmgnXRopFxtc5m2E6Olr1uMO9Gd1Di0+j23MFpspP3HpfYNAoGAO0FBwB5mfPIgxzPzrnJdWJ43NJvbCjbANPf5pHV3im51N+x1zLCagaJEMyvq8YDS5urj7uvJe5eB0gC13qp2jxjr1dcqdSXdR4kyinuUoxFm/SFivyyP6eQtooWAJ8cZ+zwTn/h4sFKRrpkygouzN0Xe2Q7cEJwgSpt0ea7RfVECgYAsT/vuYk4z1PDSTRzInV6k/+Rp5Tu3OZWcEY40FPU+SBiOEqh4Qhe4jj73szMZA3ha2TEuMkNZNF0l4yNPG6pCfgj1yY1tIdtgNLj4Fs07bPDvFZoacRNd5F5XuQfimMv6KfDD5jVjplKwkKEcyMNPUENZyzMrp5WuiPgZSamHSA==";
//		String publicKeyXml = "<RSAKeyValue><Modulus>w4mJNJiVXLfHG+qLtbodUaAfkFEl9wTp47xXio/5bXNiWsno5xW4XGYY04ngv3FsaVcH5cYhwB8XbjkTUnB7u4wuWsJYxR8yXjYCXcI/4y3mR+WmavIFxPEl+Xe4T86qWBWZSaqtbca7tK20i4JooAGt/2WrdqkLRv0rjxvQ6x0=</Modulus><Exponent>AQAB</Exponent></RSAKeyValue>";
		String publicKeyXml = "<RSAKeyValue><Modulus>hmtEANaBJ3giG8idQet39sdh40gqowpgfSrzyluaVb6BoVtT+CDMuNkXR5+HFkcOh7gF5t9ERYY5sFC5AvV+F8KGM6meKUHVV+pdLzQb7547Ll2wu0l8mSqarCiTOuMcUkt36f01zBMJHUPSXYRzL3EdF5jSu8k6lJZbwQpXq3S5Ha+fNukDgfwqxwngU/NAmjE+WQbJHIiDVzXKKzrexqbesdwlyxvBHzIJDotQ1Afi/ToTq6tTNQwuaFpmpVN2iz4Jp7l4SXhp0pPqjM61m0zWqB6Z12HIODE7l9ojvDtm/PIRIaITyVtfa2UsU9YHIlOKPDT8OjtmHlzVoXwT7w==</Modulus><Exponent>AQAB</Exponent></RSAKeyValue>";
		System.out.println("公钥："+publicKey);
		System.out.println("私钥："+privateKey);
		System.out.println("公钥Xml："+publicKeyXml);
		HashMap<String,String> pmap = rsaParameters(publicKeyXml);
		PublicKey pkey = getPublicKey(pmap.get("mudulus"), pmap.get("exponent"));
		byte[] data2 = RsaUtilExt.encryptByPublicKey(signStr.getBytes("UTF-8"), encodeBase64(pkey.getEncoded()));
//		byte[] data2 = decodeBase64("NolXYIYdYQFQucGD5vBTPgkfzhwQRbd26XsIllrTmEBpebrEPSYJAkcy49hRG2/ZPxEjeyMPDhiypg7mEAqXaKxcTYi4693hE7+CuvK0ymdw3/D8LrGZ/MO9PU8UMBMNoCZWIvEDSosP0G5Hl6W3KLAOzh5XxjRtAYYXL7Iw74FtuWa0+N16CNqI6YrdmKHqvR5a3+3e0DGxWdIvXwE+U4BpAuO+D0k4uv88tzr7JFfp8Mbl7NFTyLvsNsE3wrWDgUgLPPK44zp6gPHHQAurfMamPiI0kD/YcuA0k+uwrIwM4HVcBFTYZr0FrpSg54SxzSD96IOTIekWDlR2RVrmAQ==");
		System.out.println("密文："+encodeBase64(data2));
		byte[] source2 = RsaUtilExt.decryptByPrivateKey(data2, privateKey);
		System.out.println("解密后明文："+new String(source2));


	}
}
