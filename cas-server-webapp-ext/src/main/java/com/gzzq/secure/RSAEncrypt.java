package com.gzzq.secure;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gzzq.resource.ReadResourceUtil;

/**
 * Java生成的公私钥格式为 pkcs8, 而openssl默认生成的公私钥格式为 pkcs1，两者的密钥实际上是不能直接互用的
 * <p/>
 * openssl默认使用的是PEM格式，经过base64编码
 * <p/>
 * java加载密钥文件，不能带有注解
 * <p/>
 * --生成私钥，编码是PKCS#1格式
 * <p/>
 * openssl genrsa -out rsa_private_key.pem 1024
 * <p/>
 * --生成公钥
 * <p/>
 * openssl rsa -in rsa_private_key.pem -out rsa_public_key.pem -pubout
 * <p/>
 * --私钥不能直接被使用，需要进行PKCS#8编码
 * <p/>
 * openssl pkcs8 -topk8 -in rsa_private_key.pem -out pkcs8_rsa_private_key.pem
 * -nocrypt
 * 
 * 将rsa_public_key.pem内容复制给客户端加密用
 * 
 * @author linzl
 *
 * @creatDate 2016年10月31日
 */
public class RSAEncrypt {
	private static final Logger logger = LoggerFactory.getLogger(RSAEncrypt.class);

	private static final String DEFAULT_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDaAss40iZzJYP0IRoog1vhBnFm"
			+ "\r" + "U2yq43EWjF3uXujTv3fHa1KMyENuNPNLDUNNllfqRgxIRe4dAl60b5v" + "\r"
			+ "Pdq8A6DHO98Au1O+5NK3DexLbQEVCxFqCYzM63DxARzTr3lKg05Q+s+OjRKVaF8A" + "\r" + "BWZQ3NUGoi387rrc7QIDAQAB"
			+ "\r";

	private static final String DEFAULT_PRIVATE_KEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKEPNyPD+taAXCfG"
			+ "\r" + "6dsqnv/h7zD9SZfHaOTqoQSfr23o3ZHWL8uZzINPXGv9PYAcY6Jc1DlXxbiIJpp4" + "\r"
			+ "1rCLtolpGG1XHW44f/ZTfvx+xwQRIQbxcOqWXQYJ8HX9OMojZqK1VLNc61GzyRiA" + "\r"
			+ "ZTvx/tWYM2BciWTeB2GfOH66gRDLAgMBAAECgYBp4qTvoJKynuT3SbDJY/XwaEtm" + "\r"
			+ "u768SF9P0GlXrtwYuDWjAVue0VhBI9WxMWZTaVafkcP8hxX4QZqPh84td0zjcq3j" + "\r"
			+ "DLOegAFJkIorGzq5FyK7ydBoU1TLjFV459c8dTZMTu+LgsOTD11/V/Jr4NJxIudo" + "\r"
			+ "MBQ3c4cHmOoYv4uzkQJBANR+7Fc3e6oZgqTOesqPSPqljbsdF9E4x4eDFuOecCkJ" + "\r"
			+ "DvVLOOoAzvtHfAiUp+H3fk4hXRpALiNBEHiIdhIuX2UCQQDCCHiPHFd4gC58yyCM" + "\r"
			+ "6Leqkmoa+6YpfRb3oxykLBXcWx7DtbX+ayKy5OQmnkEG+MW8XB8wAdiUl0/tb6cQ" + "\r"
			+ "FaRvAkBhvP94Hk0DMDinFVHlWYJ3xy4pongSA8vCyMj+aSGtvjzjFnZXK4gIjBjA" + "\r"
			+ "2Z9ekDfIOBBawqp2DLdGuX2VXz8BAkByMuIh+KBSv76cnEDwLhfLQJlKgEnvqTvX" + "\r"
			+ "TB0TUw8avlaBAXW34/5sI+NUB1hmbgyTK/T/IFcEPXpBWLGO+e3pAkAGWLpnH0Zh" + "\r"
			+ "Fae7oAqkMAd3xCNY6ec180tAe57hZ6kS+SYLKwb4gGzYaCxc22vMtYksXHtUeamo" + "\r" + "1NMLzI2ZfUoX" + "\r";

	/**
	 * 私钥
	 */
	private RSAPrivateKey privateKey;

	/**
	 * 公钥
	 */
	private RSAPublicKey publicKey;

	/**
	 * 随机生成密钥对
	 */
	public void genKeyPair() {
		KeyPairGenerator keyPairGen = null;
		try {
			keyPairGen = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		keyPairGen.initialize(1024, new SecureRandom());
		KeyPair keyPair = keyPairGen.generateKeyPair();
		this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
		this.publicKey = (RSAPublicKey) keyPair.getPublic();
	}

	/**
	 * 获取私钥
	 * 
	 * @return 当前的私钥对象
	 */
	public RSAPrivateKey getPrivateKey() {
		return privateKey;
	}

	/**
	 * 获取公钥
	 * 
	 * @return 当前的公钥对象
	 */
	public RSAPublicKey getPublicKey() {
		return publicKey;
	}

	/**
	 * 随机生成private key,base64编码后存储到文件中
	 * 
	 * @return
	 */
	public String base64PrivateKey() {
		PrivateKey key = (PrivateKey) getPrivateKey();
		return new String(Base64.encodeBase64(key.getEncoded()));
	}

	/**
	 * 随机生成public key,base64编码后存储到文件中
	 * 
	 * @return
	 */
	public String base64PublicKey() {
		RSAPublicKey key = (RSAPublicKey) getPublicKey();
		return new String(Base64.encodeBase64(key.getEncoded()));
	}

	/**
	 * 从文件中加载OpenSSL的公钥,默认使用base64 解码
	 * 
	 * @param keyFile
	 *            公钥文件
	 * @throws Exception
	 */
	public void loadBase64PubKey(File publicKey) throws Exception {
		byte[] pubKeyByte = FileUtils.readFileToByteArray(publicKey);
		pubKeyByte = Base64.decodeBase64(pubKeyByte);
		loadPublicKey(pubKeyByte);
	}

	/**
	 * 从文件中加载OpenSSL的公钥,默认使用base64 解码
	 * 
	 * @param publicKey
	 *            公钥文件
	 * @throws Exception
	 */
	public void loadBase64PubKey(InputStream publicKey) throws Exception {
		byte[] pubKeyByte = IOUtils.toByteArray(publicKey);
		pubKeyByte = Base64.decodeBase64(pubKeyByte);
		loadPublicKey(pubKeyByte);
	}

	/**
	 * 从文件中输入流中加载公钥
	 * 
	 * @param keyFile
	 *            公钥文件
	 * @param isBase64
	 *            true 表示文件使用了base64加密,需要解密
	 * @throws Exception
	 */
	public void loadPublicKey(File publicKey) throws Exception {
		byte[] pubKeyByte = FileUtils.readFileToByteArray(publicKey);
		loadPublicKey(pubKeyByte);
	}

	/**
	 * 加载公钥字节
	 * 
	 * @param pubKeyByte
	 *            公钥字节
	 * @throws Exception
	 *             加载公钥时产生的异常
	 */
	public void loadPublicKey(byte[] pubKeyByte) throws Exception {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubKeyByte);
			this.publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("公钥非法");
		} catch (NullPointerException e) {
			throw new Exception("公钥数据为空");
		}
	}

	/**
	 * 从文件中加载openssl生成的私钥,默认使用base64 解码
	 * 
	 * @param privateKey
	 *            私钥文件
	 * @throws Exception
	 */
	public void loadBase64PriKey(File privateKey) throws Exception {
		try {
			byte[] privateByte = FileUtils.readFileToByteArray(privateKey);
			privateByte = Base64.decodeBase64(privateByte);
			loadPrivateKey(privateByte);
		} catch (IOException e) {
			throw new Exception("私钥数据内容读取错误");
		}
	}

	/**
	 * 从文件中加载openssl生成的私钥,默认使用base64 解码
	 * 
	 * @param privateKey
	 *            私钥文件
	 * @throws Exception
	 */
	public void loadBase64PriKey(InputStream privateKey) throws Exception {
		try {
			byte[] privateByte = IOUtils.toByteArray(privateKey);
			privateByte = Base64.decodeBase64(privateByte);
			loadPrivateKey(privateByte);
		} catch (IOException e) {
			throw new Exception("私钥数据内容读取错误");
		}
	}

	/**
	 * 从文件中加载生成的私钥
	 * 
	 * @param keyFile
	 *            私钥文件
	 * @throws Exception
	 */
	public void loadPrivateKey(File privateKey) throws Exception {
		byte[] privateByte = null;
		try {
			privateByte = FileUtils.readFileToByteArray(privateKey);
			loadPrivateKey(privateByte);
		} catch (IOException e) {
			throw new Exception("私钥数据内容读取错误");
		}
	}

	public void loadPrivateKey(byte[] privateByte) throws Exception {
		KeyFactory keyFactory = null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
			EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateByte);
			RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);
			this.privateKey = privateKey;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("私钥非法");
		}
	}

	/**
	 * 加密过程
	 * 
	 * @param publicKey
	 *            公钥
	 * @param plainTextData
	 *            明文数据
	 * @return
	 * @throws Exception
	 *             加密过程中的异常信息
	 */
	public byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws Exception {
		if (publicKey == null) {
			throw new Exception("加密公钥为空, 请设置");
		}

		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("RSA/None/PKCS1Padding", new BouncyCastleProvider());
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] output = cipher.doFinal(plainTextData);
			return Base64.encodeBase64(output);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此加密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("加密公钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("明文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("明文数据已损坏");
		}
	}

	/**
	 * 解密过程
	 * 
	 * @param privateKey
	 *            私钥
	 * @param cipherData
	 *            密文数据
	 * @return 明文
	 * @throws Exception
	 *             解密过程中的异常信息
	 */
	public byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData) throws Exception {
		if (privateKey == null) {
			throw new Exception("解密私钥为空, 请设置");
		}

		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("RSA/None/PKCS1Padding", new BouncyCastleProvider());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			cipherData = Base64.decodeBase64(cipherData);
			byte[] output = cipher.doFinal(cipherData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此解密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("解密私钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("密文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("密文数据已损坏");
		}
	}

	public static InputStream getPublicKeyFile() {
		ReadResourceUtil util = new ReadResourceUtil("com/gzzq/secure/rsa_public_key.pem");
		InputStream input = null;
		try {
			input = util.getInputStream();
			if (logger.isDebugEnabled()) {
				logger.debug("公钥路径{}", input);
			}
			return input;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 打成jar包后，读取资源文件，只有是以流的形式获取
	 * 
	 * @return
	 */
	public static InputStream getPrivateKeyFile() {
		ReadResourceUtil util = new ReadResourceUtil("com/gzzq/secure/pkcs8_rsa_private_key.pem");
		InputStream input = null;
		try {
			input = util.getInputStream();
			if (logger.isDebugEnabled()) {
				logger.debug("私钥路径{}", input);
			}
			return input;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}