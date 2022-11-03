package password;

import java.util.*;

/**
 * This class represents a Password, 
 * which is the information that the program will store about the password in storage.
 * @author ferrodr (Fernando Rodríguez Martín - UVa) */
public class Password {
	private final String name;
	private final byte[] password;
	private final byte[] iv;

	/**Creates a new Password given a name, a password and the IV Parameters for decryption.
	 * @param name the password name, usually the web or site name where the password is used.
	 * @param password the encrypted password that is going to be stored.
	 * @param iv the iv parameters*/
	public Password (String name, byte[] password, byte[] iv) {
		this.name = name;
		this.password = password;
		this.iv = iv;
	}

	/**Creates a new Password given the toString() method format.
	 * @see #toString()
	 * @param format the String in the toString() method format.*/
	public Password (String format) {
		String[] passphrase = format.split("-");
		name = passphrase[0];
		password = Base64.getDecoder().decode(passphrase[1]);
		iv = Base64.getDecoder().decode(passphrase[2]);
	}

	/**Returns the name of the site where the password is used. 
	 * @return name the name of the site where the password is used.*/
	public String getName() {
		return name;
	}

	/**Returns the (encrypted) password, in Latin1 encoding.
	 * @return password returns the (encrypted) password.
	 * @return null if Latin1 not supported.*/
	public String getPassword() {
			return Base64.getEncoder().encodeToString(password);
	}

	/** Returns the password bytes.
	 *  @return passbytes the password bytes in Latin1 encryption*/
	public byte[] getPasswordBytes(){
		return password;
	}

	/** Returns the IV Parameters
	 *	@return iv the IV parameters*/
	public byte[] getIVParams(){
		return iv;
	}

	/** Returns the IV Parameters in Base64 encoding.
	 *	@return iv the IV Parameters. */
	public String getStringIVParams() {
		return Base64.getEncoder().encodeToString(iv);
	}

	/**Returns the password in the following format:
	 * <site_name>[_]<encrypted_password>[_]<iv_parameters>
	 * @return format the Password in the format <name>[_]<encrypted_password>[_]<iv_parameters>*/
	public String toString() {
		return name + "-" + Arrays.toString(password) + "-" + getStringIVParams() + "\n";
	}
}
