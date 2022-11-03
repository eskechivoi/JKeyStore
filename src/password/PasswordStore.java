package password;

import javax.crypto.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.util.*;

/**
 * Unifies IO of passwords, working with a particular file.
 * @author ferrodr (Fernando Rodríguez Martín - UVa) */
public class PasswordStore {
	private final String passphrase;
	private final String datapath;
	private final String secretpath;
	private ArrayList<Password> list = new ArrayList<>();

	/**
	 * Store automatically generates a file with the passwords, and another file with the key parameters.
	 * If the password store already exists, uses the existing password store.
	 * @param passphrase The passphrase used to generate the key to encrypt/decrypt the passwords.
	 * @param storeName The name of the password store.*/
	public PasswordStore(String storeName, String passphrase) throws IOException{
		this.passphrase = passphrase;
		datapath = "./." + storeName + ".dat";
		secretpath = "./." + storeName + "_param.dat";
		if(!new File(secretpath).exists())
			createNewKey();
	}

	private void createNewKey() throws IOException {
		PasswordCipher cipher = new PasswordCipher(passphrase);
		PasswordIO io = new PasswordIO(datapath, secretpath);
		io.writeSalt(cipher.getSalt());
	}

	/** Reads the password list from disk, and stores it internally.
	 * This method does not decrypt the password list.*/
	public void readPasswordListFromFile() throws IOException {
		PasswordIO io = new PasswordIO(datapath, secretpath);
		list.clear();
		ArrayList<Password> passwordList = io.readPasswords();
		if(passwordList != null)
			list.addAll(passwordList);
	}

	/** Writes the password list to disk. 
	 * This method does not encrypt the password list.*/
	public void writePasswordList() throws IOException {
		PasswordIO io = new PasswordIO(datapath, secretpath);
		io.writePasswords(list); //List cannot be null, so we don't handle the exception.
	}
	
	/**
	 * Adds a new password to the Password Store.
	 * @param name the name of the site where the password is used 
	 * @param password the plain password that is wanted to be stored, this 
	 * password will be encrypted in order to store it safe.*/
	public void setNewPassword(String name, String password) throws IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException{
		PasswordIO io = new PasswordIO(datapath, secretpath);
		PasswordCipher cipher = new PasswordCipher(passphrase, io.readSalt());
		byte[] encryptedPassword = cipher.encrypt(password.getBytes(StandardCharsets.ISO_8859_1));
		Password newPassword = new Password(name, encryptedPassword, cipher.getIVParams());
		list.add(newPassword);
	} 

	/** Returns the password that matches with the input name.
	 *  @param name the password name
	 *  @return password the plain password
	 *  @return null if the name doesn´t match with any of the passwords*/
	public String getPasswordByName(String name) throws IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException{
		PasswordIO io = new PasswordIO(datapath, secretpath);
		PasswordCipher cipher = new PasswordCipher(passphrase, io.readSalt());
		Iterator<Password> it = list.iterator();
		String returnString = "not-found";
		while(it.hasNext()) {
			Password next = it.next();
			if(next.getName().equals(name)) {
				cipher.setIVParams(next.getIVParams());
				returnString = new String(cipher.decrypt(next.getPasswordBytes()));
				break;
			}
		}
		return returnString;
	}

	/**
	 * Removes the password whose name matches the input name from interal list.
	 * IMPORTANT! This method does not delete the password in disk. Disk must be overwritten.
	 * @param name the password name*/
	public void deletePassword(String name) {
		list.removeIf(next -> next.getName().equals(name));
	}

	/**
	 * Prints the password list via standard output.*/
	public void printPasswords() throws IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException{
		for (Password next : list) {
			System.out.println("Name: " + next.getName() + ". Password: " + getPasswordByName(next.getName()));
		}
	}
}
