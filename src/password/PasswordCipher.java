package password;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.security.spec.*;

/**	This class can encrypt and decrypt passwords using the encrypting key.
 * @author ferrodr (Fernando Rodríguez Martín - UVa)*/
public class PasswordCipher {
	private static final String AES_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
	private static final String KEY_DERIVATION_ALGORITHM = "PBKDF2WithHmacSHA1";
	private static final String AES_ALGO = "AES";
	private static final int AES_KEY_SIZE = 256;
	private static final int PBKDFIter = 12288; //Número de veces que se realiza el cálculo del hash
	private static final SecureRandom SECURE_RANDOM = new SecureRandom(); 
	private static final int SALT_LENGTH = 8;
	private static final int AES_BLOCK_SIZE = 16;

	private PBEKeySpec spec;
	private SecretKey key;
	private byte[] iv;

	/**
	 *	Generates a new key, based on the passphrase, used for encryption/decryption.
	 *	@param passphrase the passphrase used to generate the key. */
	public PasswordCipher (String passphrase) {
		//Generates a user-chosen password that can be used with PBE. (Password Based Encrypt)
		try{
			spec = new PBEKeySpec(passphrase.toCharArray(), calcSalt(), PBKDFIter, AES_KEY_SIZE);
			SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_DERIVATION_ALGORITHM);
			SecretKey tmp = factory.generateSecret(spec);
			key = new SecretKeySpec(tmp.getEncoded(), AES_ALGO);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException impossibleException) {
			//Algorithm is statically defined, and it is known to exist.
		}
	}

	/**
	 *	Generates a new key, based on the passphrase, used for encryption/decryption.
	 *	@param passphrase the passphrase used to generate the key. */
	public PasswordCipher (String passphrase, byte[] salt) {
		//Generates a user-chosen password that can be used with PBE. (Password Based Encrypt)
		try{
			spec = new PBEKeySpec(passphrase.toCharArray(), salt, PBKDFIter, AES_KEY_SIZE);
			SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_DERIVATION_ALGORITHM);
			SecretKey tmp = factory.generateSecret(spec);
			key = new SecretKeySpec(tmp.getEncoded(), AES_ALGO);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException impossibleException) {
			//Algorithm is statically defined, and it is known to exist.
		}
	}

	private static byte[] getSecureRandomData() {
		byte[] result = new byte[PasswordCipher.AES_BLOCK_SIZE];
		SECURE_RANDOM.nextBytes(result);
		return result;
	}

	private byte[] calcSalt () {
		byte[] salt = new byte[SALT_LENGTH]; //Avoid two hashes being equal.
		SecureRandom secRandom = new SecureRandom();
		secRandom.nextBytes(salt); //Fills the array with random bytes.
		return salt;
	}

	/**
	 * Returns the salt used to generate the key.
	 * @return salt the salt used to generate the key*/
	public byte[] getSalt(){
		return spec.getSalt();
	}
	
	/**
	 *  Returns the IV parameters for decryption
	 *  @return iv the IV parameters*/
	public byte[] getIVParams(){
		return iv;
	}
	
	/**
	 *  Sets the IV parameters, usually for decryption.
	 *  @param ivparams the IV parameters*/
	public void setIVParams(byte[] ivparams){
		iv = ivparams; 
	}

	/**
	 *  Encrypts the given password.
	 *	@param password byte sequence that represents the password to be encrypted
	 *	@return encryptedPassword byte sequence, that represents the encrypted password, in UTF-8*/
	public byte[] encrypt (byte[] password) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		try{
			iv = getSecureRandomData();
			Cipher c = Cipher.getInstance(AES_CIPHER_ALGORITHM);
			c.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
			return c.doFinal(password);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException impossibleException) {
			//Algorithm and Padding are statically defined, and it is known to exist.
			return null;
		} catch (InvalidAlgorithmParameterException impossibleException) {
			//IV Parameters are prepared to be the exact length and format for AES cipher.
			return null;
		}
	}

	/**
	 *  Decrypts the given byte sequence into a password.
	 *	@param enc_password the byte sequence of the encrypted password
	 *	@return plainPassword the byte sequence of the decrypted password, in UTF-8 encoding*/
	public byte[] decrypt (byte[] enc_password) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		try{
			Cipher c = Cipher.getInstance(AES_CIPHER_ALGORITHM);
			c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
			return c.doFinal(enc_password);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException impossibleException) {
			//Algorithm and Padding are statically defined, and it is known to exist.
			return null;
		} catch (InvalidAlgorithmParameterException impossibleException) {
			//IV Parameters are prepared to be the exact length and format for AES cipher.
			return null;
		}
	}
}
