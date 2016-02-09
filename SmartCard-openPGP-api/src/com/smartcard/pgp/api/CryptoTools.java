package com.smartcard.pgp.api;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoTools {

	private static final byte[] iv16 = {
			0x01, 
			(byte) 0xD3, 
			(byte) 0xaa, 
			0x19, 
			0x12, 
			(byte) 0x9a, 
			0x58, 
			(byte) 0x99,
			0x09, 
			(byte) 0xB9, 
			(byte) 0xFF, 
			0x27, 
			(byte) 0x99, 
			(byte) 0x87, 
			0x42, 
			(byte) 0x69
			};

	public static void enableBouncyCastle() {
		Security.addProvider(new BouncyCastleProvider());
	}

	public static byte[] rsaEncrypt(PublicKey key, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		Cipher rsa = Cipher.getInstance("RSA/NONE/PKCS1Padding"); // RSA/ECB/PKCS1Padding
		rsa.init(Cipher.ENCRYPT_MODE, key);
		return rsa.doFinal(data);
	}

	public static SecretKey desKeyGenerate() throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede");
		keyGenerator.init(168);

		return keyGenerator.generateKey();
	}
	
	public static SecretKey aesKeyGenerate() throws NoSuchAlgorithmException{
		KeyGenerator gen = KeyGenerator.getInstance("AES");
		gen.init(128);
		return gen.generateKey();
	}

	public static SecretKey desKeyFromBytes(byte[] bytes) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException {
		DESedeKeySpec spec = new DESedeKeySpec(bytes);
		return SecretKeyFactory.getInstance("DESede").generateSecret(spec);
	}
	
	public static SecretKey aesKeyFromBytes(byte[] bytes) throws InvalidKeySpecException, NoSuchAlgorithmException{
		SecretKeySpec keySpec = new SecretKeySpec(bytes, "AES");
		return SecretKeyFactory.getInstance("AES").generateSecret(keySpec);
	}
	
	public static byte[] aesEncrypt(byte[] data, SecretKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec spec = new IvParameterSpec(iv16);
		cipher.init(Cipher.ENCRYPT_MODE, key, spec);
		return cipher.doFinal(data);
	}
	
	public static byte[] aesDecrypt(byte[] data, SecretKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec spec = new IvParameterSpec(iv16);
		cipher.init(Cipher.DECRYPT_MODE, key, spec);
		return cipher.doFinal(data);
	}

	public static byte[] desEncrypt(byte[] data, SecretKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		IvParameterSpec spec = new IvParameterSpec(new byte[8]);
		cipher.init(Cipher.ENCRYPT_MODE, key, spec);
		return cipher.doFinal(data);
	}

	public static byte[] desDecrypt(byte[] data, SecretKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		IvParameterSpec spec = new IvParameterSpec(new byte[8]);
		cipher.init(Cipher.DECRYPT_MODE, key, spec);
		return cipher.doFinal(data);
	}
}

