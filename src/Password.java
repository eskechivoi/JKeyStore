import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * This class represents a Password, 
 * which is the information that the program will store about the password in storage.
 * @author ferrodr (Fernando Rodríguez Martín - UVa) */
public class Password {
	private String name;
	private String password;
	private byte[] iv;

	/**Creates a new Password given a name, a password and the IV Parameters for decryption.
	 * @param name the password name, usually the web or site name where the password is used.
	 * @param password the encrypted password that is going to be stored.
	 * @param iv the iv parameters*/
	public Password (String name, String password, byte[] iv) {
		this.name = name;
		this.password = password;
		this.iv = iv;
	}

	/**Creates a new Password given the toString() method format.
	 * @see #toString()
	 * @param format the String in the toString() method format.
	 * @throws IllegalArgumentException if the input String doesn´t follow the corresponding format.*/
	public Password (String format) throws IllegalArgumentException, UnsupportedEncodingException {
		if(!format.contains("-")){throw new IllegalArgumentException("The input String doesn´t follow the corresponding format.");}
		String[] passphrase = format.split("-");
		name = passphrase[0];
		password = passphrase[1];
		iv = Base64.getMimeDecoder().decode(passphrase[2]);
	}

	/**Returns the name of the site where the password is used. 
	 * @return name the name of the site where the password is used.*/
	public String getName() {
		return name;
	}

	/**Returns the (encrypted) password 
	 * @return password returns the (encrypted) password.*/
	public String getPassword() {
		return password;
	}

	/** Returns the IV Parameters
	 *	@param iv the IV parameters*/
	public byte[] getIVParams(){
		return iv;
	}

	/** Returns the IV Parameters in Latin-1 charset.
	 *	@return iv the IV Parameters*/
	public String getStringIVParams() {
			return Base64.getMimeEncoder().encodeToString(iv);
	}

	/**Returns the password in the following format:
	 * <site_name>[_]<encrypted_password>[_]<iv_parameters>
	 * @return format the Password in the format <name>[_]<encrypted_password>[_]<iv_parameters>*/
	public String toString() {
		return name + "-" + password + "-" + getStringIVParams();
	}
}
