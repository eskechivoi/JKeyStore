import java.io.IOException;
import java.security.*;
import java.security.spec.*;

import javax.crypto.spec.*;
import javax.crypto.SecretKeyFactory; //Operates only on secret (symmetric) keys
import javax.crypto.*;

/**	This class can encrypt and decrypt passwords using the encrypting key.
 * @author ferrodr (Fernando Rodríguez Martín - UVa)*/
public class PasswordCipher {
	private static final String AES_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
	private static final String KEY_DERIVATION_ALGORITHM = "PBKDF2WithHmacSHA1";
	private static final String AES_ALGO = "AES";
	private static final int AES_KEY_SIZE = 256 / 8;
	private static final int PBKDFIter = 12288; //Número de veces que se realiza el cálculo del hash
	private static final SecureRandom SECURE_RANDOM = new SecureRandom(); 
	private static final int SALT_LENGTH = 8;
	private static final int AES_BLOCK_SIZE = 16;

	private PBEKeySpec spec;
	private SecretKey key;
	private byte[] iv;

	/**
	 *	Creates a new password encryptor/decryptor, given a key.
	 *	@param encrypting_key the key used to encrypt/decrypt.
	 * */
	public PasswordCipher (String password) {
		//Generates an user-chosen password that can be used with PBE. (Password Based Encrypt)
		try{
			spec = new PBEKeySpec(password.toCharArray(), calcSalt(), PBKDFIter, AES_KEY_SIZE * 8);
			SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_DERIVATION_ALGORITHM);
			SecretKey tmp = factory.generateSecret(spec);
			key = new SecretKeySpec(tmp.getEncoded(), AES_ALGO);
		} catch (NoSuchAlgorithmException impossibleException) {
			//Algorithm is statically defined, and it is known to exist.
		} catch (InvalidKeySpecException dontknow) {
			//I dont know how to handle this exception.
		}
	}

	private static byte[] getSecureRandomData(int length) {
		byte[] result = new byte[length];
		SECURE_RANDOM.nextBytes(result);
		return result;
	}

	private byte[] calcSalt () {
		byte[] salt = new byte[SALT_LENGTH]; //Evita que dos hash sean iguales	
		SecureRandom secRandom = new SecureRandom();
		secRandom.nextBytes(salt); //Rellena el array salt con bytes aleatorios
		return salt;
	}

	/** Returns the encoded key used for cipher.
	 *  @return key the encoded key*/
	public byte[] getCipherKey(){
		return key.getEncoded();
	}

	/** Sets the same key used for encryption.
	 *  @param encoded_key the key used for encryption*/
	public void setCipherKey(byte[] encoded_key){
		key = new SecretKeySpec(encoded_key, AES_ALGO);
	}
	
	/** Returns the IV parameters for decryption
	 *  @return iv the IV parameters*/
	public byte[] getIVParams(){
		return iv;
	}
	
	/** Sets the IV parameters, usually for decryption.
	 *  @param ivparams the IV parameters*/
	public void setIVParams(byte[] ivparams){
		iv = ivparams; 
	}

	/** Encrypts the given password.
	 *	@param password byte sequence that represents the password to be encrypted
	 *	@return encryptedPassword byte sequence, that represents the encrypted password, in UTF-8*/
	public byte[] encrypt (byte[] password) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, ShortBufferException, IOException {
		try{
			iv = getSecureRandomData(AES_BLOCK_SIZE);
			Cipher c = Cipher.getInstance(AES_CIPHER_ALGORITHM);
			c.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
			byte[] cipherText = c.doFinal(password);
			return cipherText;
		} catch (NoSuchAlgorithmException impossibleException) {
			//Algorithm is statically defined, and it is known to exist.
			return null;
		} catch (NoSuchPaddingException impossibleException) {
			//Padding algorithm is statically defined, and it is known to exist.
			return null;
		} catch (InvalidAlgorithmParameterException impossibleException) {
			//IV Parameters are prepared to be the exact length and format for AES cipher.
			return null;
		}

	}

	/** Decrypts the given byte sequence into a password. 
	 *	@param encryptedPassword the byte sequence of the encrypted password
	 *	@return plainPassword the byte sequence of the decrypted password, in UTF-8 encoding*/
	public byte[] decrypt (byte[] enc_password) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, ShortBufferException, IOException{
		try{
			Cipher c = Cipher.getInstance(AES_CIPHER_ALGORITHM);
			c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
			byte[] plainText = c.doFinal(enc_password);
			return plainText;
		} catch (NoSuchAlgorithmException impossibleException) {
			//Algorithm is statically defined, and it is known to exist.
			return null;
		} catch (NoSuchPaddingException impossibleException) {
			//Padding algorithm is statically defined, and it is known to exist.
			return null;
		} catch (InvalidAlgorithmParameterException impossibleException) {
			//IV Parameters are prepared to be the exact length and format for AES cipher.
			return null;
		}

	}
}
