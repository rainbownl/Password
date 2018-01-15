package com.niulei.Password;

import android.annotation.SuppressLint;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MyEncryptor {
	private static String seed = "com.niulei.password_2015_01_09";
	public static byte[] encrypt(byte[] input) throws Exception{
		byte[] rawKey = getRawKey(seed.getBytes());
		SecretKeySpec skSpec = new SecretKeySpec(rawKey, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, skSpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
		return cipher.doFinal(input);
	}
	
	public static byte[] decrypt(byte[] input) throws Exception{
		byte[] rawKey = getRawKey(seed.getBytes());
		SecretKeySpec skSpec = new SecretKeySpec(rawKey, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, skSpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
		return cipher.doFinal(input);
	}
	@SuppressLint("NewApi")
	private static byte[] getRawKey(byte[] seedBytes) throws Exception{
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom sr = null;
		if(android.os.Build.VERSION.SDK_INT >= 17){
			sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
		}
		else{
			sr = SecureRandom.getInstance("SHA1PRNG");
		}
		sr.setSeed(seedBytes);
		kgen.init(128, sr);
		SecretKey sk = kgen.generateKey();
		byte[] raw = sk.getEncoded();
		return raw;
	}
}
