package password;

import java.io.*;
import java.util.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

import java.security.*;
import java.security.spec.*;

/**
 * Unifies IO of passwords, working with a particular file.
 * @author ferrodr (Fernando Rodríguez Martín - UVa) */
public class PasswordStore {
	private String storeKey;
	private String storeName;
	private String datapath;
	private String secretpath;
	private ArrayList<Password> list = new ArrayList<Password>(); 

	/**
	 * Store automatically generates a file with the passwords, and another file with de 'something you own' secret.
	 * If the file with the secret already exists, this method will overwrite it.
	 * @param publicName: The name of the file that will be generated.
	 * @param privName: The name that will be shown in the running app.
	 * @param storeKey: The key used to encrypt/decrypt the passwords.*/
	public PasswordStore(String fileName, String storeName, String storeKey) throws IOException{
		this.storeName = storeName;
		this.storeKey = storeKey;
		datapath = "./" + fileName + ".dat";
		secretpath = "./" + fileName + "_secret.dat";
		if(!new File(secretpath).exists())
			writeStoreKey();
	}

	/**
	 * Creates a PasswordStore object from the password_store_path and the secret_path.
	 * @param datapath: password store path.
	 * @param secretpath: the 'something you own' secret path.
	 * */
	public PasswordStore(String datapath, String secretpath) throws IOException{
		this.datapath = datapath;
		this.secretpath = secretpath;
		PasswordIO io = new PasswordIO(datapath, secretpath);
		io.readStoreKey(); //We verify that the secretpath is correct. If it is not, we throw an IOException.
		this.storeKey = "-"; //Not necesary, will be overwriten by the secret in the store file.
		String tmp[] = datapath.split(".dat")[0].split("/");
		storeName = tmp[tmp.length-1];
	}

	private void writeStoreKey() throws IOException{
		File storefile = new File(secretpath);
		storefile.delete(); //If storefile already exists, it deletes it...
		PasswordIO io = new PasswordIO(datapath, secretpath);
		PasswordCipher cipher = new PasswordCipher(storeKey);
		io.writeStoreKey(cipher.getCipherKey()); //and a new storefile is created.
	}

	/** Returns the password store name. 
	 *  @return storeName	the password store name*/
	public String getName() {
		return storeName;
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
		io.writePasswords(list); //List cannot be null, so we dont handle the exception.
	}
	
	/**
	 * Adds a new password to the Password Store.
	 * @param name the name of the site where the password is used 
	 * @param password the plain password that is wanted to be stored, this 
	 * password will be encrypted in order to store it safe.*/
	public void setNewPassword(String name, String password) throws IOException, ShortBufferException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException{
		PasswordIO io = new PasswordIO(datapath, secretpath);
		PasswordCipher cipher = new PasswordCipher(storeKey);
		cipher.setCipherKey(io.readStoreKey()); //Reads the 'something you own' secret, and tries to encrypt with that secret.
		byte[] encryptedPassword = cipher.encrypt(password.getBytes("ISO-8859-1"));
		Password newPassword = new Password(name, encryptedPassword, cipher.getIVParams());
		list.add(newPassword);
	} 

	/** Returns the password that matches with the input name.
	 *  @param name the password name
	 *  @return password the plain password
	 *  @return null if the name doesn´t match with any of the passwords*/
	public String getPasswordByName(String name) throws IOException, ShortBufferException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException{
		PasswordIO io = new PasswordIO(datapath, secretpath);
		PasswordCipher cipher = new PasswordCipher(storeKey);
		cipher.setCipherKey(io.readStoreKey()); //Reads the 'something you own' secret, and tries to decrypt with that secret.
		Iterator<Password> it = list.iterator();
		String returnString = "not-found";
		while(it.hasNext()) {
			Password next = (Password)it.next();
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
		Iterator<Password> it = list.iterator();
		while(it.hasNext()) {
			Password next = (Password) it.next();
			if(next.getName().equals(name))
				it.remove();
		}
	}

	/**
	 * Prints the password list via standard output.*/
	public void printPasswords() throws IOException, ShortBufferException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException{
		Iterator<Password> it = list.iterator();
		while(it.hasNext()) {
			Password next = (Password)it.next();
			System.out.println("Name: " + next.getName() + ". Password: " + getPasswordByName(next.getName()));
		}
	}
}
