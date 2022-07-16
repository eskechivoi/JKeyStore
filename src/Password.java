/**
 * This class represents a Password, 
 * which is the information that the program will store about the password in storage.
 * @author ferrodr (Fernando Rodríguez Martín - UVa) */
public class Password {
	private String name;
	private String password;

	/**Creates a new Password given a name and a password.
	 * @param name the password name, usually the web or site name where the password is used.
	 * @param password the encrypted password that is going to be stored.*/
	public Password (String name, String password) {
		this.name = name;
		this.password = password;
	}

	/**Creates a new Password given the toString() method format.
	 * @see #toString()
	 * @param format the String in the toString() method format.
	 * @throws IllegalArgumentException if the input String doesn´t follow the corresponding format.*/
	public Password (String format) throws IllegalArgumentException {
		if(!format.contains("_")){throw new IllegalArgumentException("The input String doesn´t follow the corresponding format.");}
		String[] passphrase = format.split("_");
		name = passphrase[0];
		password = passphrase[1];
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

	/**Returns the password in the following format:
	 * <site_name>_<encrypted_password>
	 * @return format the Password in the format <name>_<encrypted_password>*/
	public String toString() {
		return name + "_" + password;
	}
}
