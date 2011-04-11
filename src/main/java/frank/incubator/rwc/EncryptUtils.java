package frank.incubator.rwc;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * SunJCE Provider (implements RSA, DES, Triple DES, AES, Blowfish, ARCFOUR, RC2, PBE, Diffie-Hellman, HMAC)
 * @author frank
 *
 */
@SuppressWarnings("restriction")
public class EncryptUtils {
	public static final String KEY_SHA = "SHA";
	public static final String KEY_MD5 = "MD5";
	/**
	 * MAC算法可选以下多种算法 HmacMD5 HmacSHA1 HmacSHA256 HmacSHA384 HmacSHA512
	 */
	public static final String KEY_MAC = "HmacMD5";
	
	private static final byte[] key = "MyKey".getBytes();

	private static String Algorithm = "DES"; // 定义 加密算法,可用
	// DES,DESede,Blowfish

	static boolean debug = false;

	static {
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String src = "admin";
		System.out.println("原数据：\n" + src);
		System.out.println("--------------------MD5加密------------------");
		BigInteger md5 = new BigInteger(encryptMD5(src.getBytes()));
		String md5_target = md5.toString(16);
		System.out.println("MD5加密：\n" + md5_target);
		System.out.println("--------------------SHA加密------------------");
		BigInteger sha5 = new BigInteger(encryptSHA(src.getBytes()));
		String sha5_target = sha5.toString(32);
		System.out.println("SHA加密:\n" + sha5_target);
		System.out.println("--------------------HMAC加密-----------------");
		String key = initMacKey();
		System.out.println("HMAC密钥:\n" + key);
		BigInteger hmac = new BigInteger(encryptHMAC(src.getBytes(), key));
		String hmac_target = hmac.toString(16);
		System.out.println("HMAC加密:\n" + hmac_target);
		System.out.println("--------------------BASE64加密和解密----------");
		byte[] inputData = src.getBytes();
		String code = encryptBASE64(inputData);
		System.out.println("BASE64加密:\n" + code);
		byte[] output = decryptBASE64(code);
		String outputStr = new String(output);
		System.out.println("BASE64解密:\n" + outputStr);

		debug = false;
		String testStr = "Hello fox";
		long s = System.currentTimeMillis();
		System.out.println(encode(testStr));
		System.out.println(decode(encode(testStr)));
		System.out.println(encode(testStr));
		System.out.println(decode(encode(testStr)));
		System.out.println(encode(testStr));
		System.out.println(decode(encode(testStr)));
		System.out.println(encode(testStr));
		System.out.println(decode(encode(testStr)));
		System.out.println(System.currentTimeMillis() - s);
	}

	/**
	 * MD5加密
	 * 
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptMD5(byte[] input) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
		// md5.update( input );
		return md5.digest(input);
	}

	/**
	 * SHA加密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptSHA(byte[] data) throws Exception {
		MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
		// sha.update( data );
		return sha.digest(data);

	}

	/**
	 * BASE64加密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptBASE64(byte[] key) throws Exception {
		return (new BASE64Encoder()).encodeBuffer(key);
	}

	/**
	 * BASE64解密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptBASE64(String key) throws Exception {
		return (new BASE64Decoder()).decodeBuffer(key);
	}

	/**
	 * 初始化HMAC密钥
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String initMacKey() throws Exception {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC);

		SecretKey secretKey = keyGenerator.generateKey();
		return encryptBASE64(secretKey.getEncoded());
	}

	/**
	 * HMAC加密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptHMAC(byte[] data, String key) throws Exception {
		SecretKey secretKey = new SecretKeySpec(decryptBASE64(key), KEY_MAC);
		Mac mac = Mac.getInstance(secretKey.getAlgorithm());
		mac.init(secretKey);

		return mac.doFinal(data);
	}

	

	/** 
	 * 加密
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public static String encode(String input) throws Exception {
		if (input == null)
			return null;
		byte[] bytes = input.getBytes();
		SecretKey deskey = new javax.crypto.spec.SecretKeySpec(key, Algorithm);
		Cipher c1 = Cipher.getInstance(Algorithm);
		c1.init(Cipher.ENCRYPT_MODE, deskey);
		byte[] cipherByte = c1.doFinal(bytes);
		return byte2hex(cipherByte);
	}

	/**
	 *  解密
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public static String decode(String input) throws Exception {
		if (input == null)
			return null;
		byte[] bytes = hex2byte(input);
		SecretKey deskey = new javax.crypto.spec.SecretKeySpec(key, Algorithm);
		Cipher c1 = Cipher.getInstance(Algorithm);
		c1.init(Cipher.DECRYPT_MODE, deskey);
		byte[] clearByte = c1.doFinal(bytes);
		return new String(clearByte);
	}

	/**
	 *  字节码转换成16进制字符串
	 * @param b
	 * @return
	 */
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}

	/**
	 *  将16进制字符串转换成字节码
	 * @param hex
	 * @return
	 */
	public static byte[] hex2byte(String hex) {
		byte[] bts = new byte[hex.length() / 2];
		for (int i = 0; i < bts.length; i++) {
			bts[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2),
					16);
		}
		return bts;
	}
	
	/**
	 * 解码Base64编码的信息
	 * @param s
	 * @return
	 */
	public static String getFromBASE64(String s) {
		if (s == null)
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(s);
			return new String(b);
		} catch (Exception e) {
			return null;
		}
	}

}
