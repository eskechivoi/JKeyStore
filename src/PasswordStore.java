import java.io.*;
import java.util.*;

/**
 * Unifies IO of passwords, working with a particular file.
 * @author ferrodr (Fernando Rodríguez Martín - UVa) */
public class PasswordStore {
	private String storeKey;
	private String storepath;
	private String storeName;
	private ArrayList<Password> list = new ArrayList<Password>(); 

	/**
	 * Store automatically generates a file with the passwords, and sets the store path.
	 * @param publicName: The name of the file that will be generated.
	 * @param privName: The name that will be shown in the running app.
	 * @param storeKey: The key used to encrypt/decrypt the passwords.*/
	public PasswordStore(String fileName, String storeName, String storeKey) {
		this.storeName = storeName;
		this.storeKey = storeKey;
		storepath = "./" + fileName + ".dat";
	}

	/** Returns the password store name. 
	 *  @return storeName	the password store name*/
	public String getName() {
		return storeName;
	}

	/** Reads the password list from disk, and stores it internally.*/
	public void readPasswordListFromFile() throws FileNotFoundException {
		PasswordIO io = new PasswordIO(storepath);
		list.clear();
		list.addAll(io.readPasswords());
	}

	/** Writes the password list to disk. */
	public void writePasswordList() throws IOException {
		PasswordIO io = new PasswordIO(storepath);
		io.writePasswords(list); //List cannot be null, so we dont handle the exception.
	}
	
	/**
	 * Adds a new password to the Password Store.
	 * @param name the name of the site where the password is used 
	 * @param password the plain password that is wanted to be stored, this 
	 * password will be encrypted in order to store it safe.*/
	//TODO: MUST HANDLE DIFFERENT TYPES OF EXCEPTIONS
	public void setNewPassword(String name, String password) throws Exception {
		PasswordCipher cipher = new PasswordCipher(storeKey);
		byte[] encryptedPassword = cipher.encrypt(password.getBytes("UTF-8"));
		Password newPassword = new Password(name, encryptedPassword.toString());
		list.add(newPassword);
	} 

	/** Returns the password that matches with the input name.
	 *  @param name the password name
	 *  @return password the plain password
	 *  @return null if the name doesn´t match with any of the passwords*/
	//TODO: MUST HANDLE DIFFERENT TYPES OF EXCEPTIONS
	public String getPasswordByName(String name) throws Exception{
		PasswordCipher cipher = new PasswordCipher(storeKey);
		Iterator<Password> it = list.iterator();
		String returnString = null;
		while(it.hasNext()) {
			Password next = (Password)it.next();
			if(next.getName().compareTo(name) == 0) {
				returnString = new String(cipher.decrypt(next.getPassword().getBytes("UTF-8")), "UTF-8");
				break;
			}
		}
		return returnString;
	}
}
