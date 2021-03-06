package com.xinleju.cloud.oa.util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;

/**
 * Java,.Net通用加密类
 * 
 * @param <T>
 */
@SuppressWarnings("unused")
public abstract class BCVFactory {

	private CipherParameters _params = null;
	private BlockCipher _engine = null;
	private Charset _charset = null;
	public static final String VIPARA = "12345678";

	public CipherParameters getParams() {
		return _params;
	}

	/**
	 * org.bouncycastle.crypto.params下所有Key类型 example: byte[]
	 * keys="abcdefgh".getBytes(); KeyParameter kp= new KeyParameter(keys);
	 * 
	 * @param value
	 */
	public void setParams(CipherParameters value) {
		_params = value;
	}

	public BlockCipher getEngine() {
		return _engine;
	}

	/**
	 * org.bouncycastle.crypto.engines下所有加密类型 example:BlowfishEngine engine=new
	 * BlowfishEngine();
	 * 
	 * @param value
	 */
	public void setEngine(BlockCipher value) {
		_engine = value;
	}

	public Charset getCharset() {
		return _charset;
	}

	/**
	 * 设置字符编码类型,"gb2312","unicode","UTF-8" Java,.Net传递,请设置为null example:Charset
	 * set=Charset.forName("unicode");
	 * 
	 * @param value
	 */
	public void setCharset(Charset value) {
		_charset = value;
	}

	/**
	 * 构造函数
	 */
	public BCVFactory() {
	}

	/**
	 * 构造函数，传入参数
	 * 
	 * @param params
	 * @param engine
	 * @param charset
	 */
	public BCVFactory(CipherParameters params, BlockCipher engine,
			Charset charset) {
		this.setParams(params);
		this.setEngine(engine);
		this.setCharset(charset);
	}

	public static void f_BlowfishtestCopyto01() {
		byte[] cifrado1 = null;
		byte[] cifrado2 = null;
		byte[] mensaje = "中文测试加密数据abcd!adfasdfdsafs".getBytes();
		try {
			// KeyGenerator kgen = KeyGenerator.getInstance("Rijndael", "BC");
			// KeyGenerator kgen = KeyGenerator.getInstance("Blowfish", "BC");
			// DES
			// kgen.init(new SecureRandom());
			// SecretKey sk = kgen.generateKey();
			// 注意进行位数补足,不足补空格
			// Key length not 128/160/192/224/256 bits.
			byte[] keys = "12345678".getBytes();
			SecretKeySpec sk = new SecretKeySpec(keys, "Blowfish");
			Date inic = new Date();
			Cipher c1 = Cipher.getInstance("Blowfish", "BC");
			c1.init(Cipher.ENCRYPT_MODE, sk);
			Cipher c2 = Cipher.getInstance("Blowfish", "BC");
			c2.init(Cipher.DECRYPT_MODE, sk);
			cifrado1 = c1.doFinal(mensaje, 0, mensaje.length);
			cifrado2 = c2.doFinal(cifrado1, 0, cifrado1.length);
			System.out.println("\nBlowfish加密 = "
					+ Usual.f_toBase64String(cifrado1));
			// System.out.println("\nBlowfish加密 = " + mensaje.length +
			// "\tmensaje    = "
			// + new String(mensaje, 0, mensaje.length));
			// System.out.println("longitud = " + cifrado1.length +
			// "\tcifrado    = " +
			// MyToolkit.toHexString(cifrado1));
			System.out.println("Blowfish解密 = " + cifrado2.length
					+ "\tdescifrado = "
					+ new String(cifrado2, 0, cifrado2.length));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 以下算法仅作参考.AES
	private static final String bm = "gb2312";

	public static Cipher iniCipher(String dataPassword, String algorithm,
			String transformation, String provider, int opmode)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException,
			NoSuchProviderException {
		return iniCipher(dataPassword, algorithm, transformation, provider,
				opmode, VIPARA);
	}

	public static Cipher iniCipher(String dataPassword, String algorithm,
			String transformation, String provider, int opmode,
			String IvParameter) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException,
			BadPaddingException, NoSuchProviderException {
		IvParameterSpec zeroIv = null;
		if (!Usual.f_isNullOrWhiteSpace(IvParameter)) {
			zeroIv = new IvParameterSpec(IvParameter.getBytes());
		}
		SecretKeySpec key = new SecretKeySpec(dataPassword.getBytes(),
				algorithm);
		Cipher cipher = Cipher.getInstance(transformation, provider);
		if (zeroIv != null) {
			cipher.init(opmode, key, zeroIv);
		} else {
			cipher.init(opmode, key);
		}
		return cipher;
	}

	// 以下算法仅作参考.Blowfish
	/**
	 * 
	 * @param dataPassword
	 * @param opmode
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws NoSuchProviderException
	 */
	public static Cipher iniBlowfishCipher(String dataPassword, int opmode)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException,
			NoSuchProviderException {
		return iniCipher(dataPassword, "Blowfish", "Blowfish", "BC", opmode);// Cipher.getInstance("AES/CBC/PKCS5Padding");
	}

	/**
	 * 
	 * @param dataPassword
	 * @param cleartext
	 * @return
	 * @throws Exception
	 */
	public static String blowfishEncrypt(String dataPassword, String cleartext)
			throws Exception {
		byte[] clearByte = cleartext.getBytes(bm);
		byte[] encryptedData = blowfishEncrypt(dataPassword, clearByte);
		return Usual.f_toBase64String(encryptedData);
	}

	/**
	 * 
	 * @param dataPassword
	 * @param clearByte
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws NoSuchProviderException
	 */
	public static byte[] blowfishEncrypt(String dataPassword, byte[] clearByte)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException,
			NoSuchProviderException {
		Cipher cipher = iniBlowfishCipher(dataPassword, Cipher.ENCRYPT_MODE);// Cipher.getInstance("AES/CBC/PKCS5Padding");
		return cipher.doFinal(clearByte);
	}

	/**
	 * 
	 * @param dataPassword
	 * @param encrypted
	 * @return
	 * @throws Exception
	 */
	public static String blowfishDecrypt(String dataPassword, String encrypted)
			throws Exception {
		byte[] byteMi = Usual.f_fromBase64String(encrypted);
		byte[] decryptedData = blowfishDecrypt(dataPassword, byteMi);
		return new String(decryptedData, bm);
	}

	/**
	 * 
	 * @param dataPassword
	 * @param byteMi
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws NoSuchProviderException
	 */
	public static byte[] blowfishDecrypt(String dataPassword, byte[] byteMi)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException,
			NoSuchProviderException {
		Cipher cipher = iniBlowfishCipher(dataPassword, Cipher.DECRYPT_MODE);// Cipher.getInstance("AES/CBC/PKCS5Padding");
		return cipher.doFinal(byteMi);
	}

	public static String getTxt() throws Exception {
		String lineTXT = null;
		try {
			String encoding = "gb2312"; // 字符编码(可解决中文乱码问题 )
			File file = new File(BCVFactory.class.getClassLoader()
					.getResource("/en.txt").getFile());
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);
				BufferedReader bufferedReader = new BufferedReader(read);

				while ((lineTXT = bufferedReader.readLine()) != null) {

					return lineTXT.toString().trim();
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件！");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容操作出错");
			e.printStackTrace();
		}
		return null;
	}

	public static String encrypt(String username) {

		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		String mStr = Usual.mEmpty;
		try {

			mStr = BCVFactory.blowfishEncrypt(BCVFactory.getTxt(), username
					+ ":");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mStr;
	}
	
	public static String decodeCorpCode(String corpCode) {

		String mStr = Usual.mEmpty;
		try {
			mStr=java.net.URLDecoder.decode(corpCode,"GB2312");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mStr;
	}

	public static void main(String[] args) {

		/*String mStr = Usual.mEmpty;
		try {
			mStr = java.net.URLDecoder.decode("%2B9jjnsy0waU%3D","GB2312");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(mStr);*/
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		String mStr = Usual.mEmpty;
		String de = Usual.mEmpty;
		try {

			mStr = BCVFactory.blowfishEncrypt("12345678", "test:");
			de = BCVFactory.blowfishDecrypt("12345678", mStr);
			System.out.println(mStr);
			System.out.println(de);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}