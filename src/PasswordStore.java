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

	public void readPasswordListFromFile() throws FileNotFoundException {
		PasswordIO io = new PasswordIO(storepath);
		list.clear();
		list.addAll(io.readPasswords());
	}

	public void writePasswordList() throws IOException {
		PasswordIO io = new PasswordIO(storepath);
		io.writePasswords(list); //List cannot be null, so we dont handle the exception.
	}
	
	/**
	 * Adds a new password to the Password Store.
	 * @throws IllegalArgumentException New password cannot be a null reference.*/
	public void setNewPassword(Password newPassword) throws IllegalArgumentException {
		if(newPassword == null) {throw new IllegalArgumentException("New password cannot be a null reference.");}
		list.add(newPassword);
	} 
}
