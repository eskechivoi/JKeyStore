package password;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

/**
 * Suplies IO methods for writting and reading passwords from disk, 
 * that are independent of the particular password store.
 * @author ferrodr (Fernando Rodríguez Martín - UVa) */
public class PasswordIO {
	private final File datafile;
	private final File storefile;

	/**
	 * Inizialices a PasswordIO that can read and write a password list from disk.
	 * Data from disk is encrypted, and this class is not responsible for decryption.
	 * @param datapath the path where the password store file is*/
	public PasswordIO(String datapath, String storepath) {
		datafile = new File(datapath);
		storefile = new File(storepath);
	}

	/** Writes the salt into file.
	 *	@param salt the salt used to generate the key.*/
	public void writeSalt(byte[] salt) throws IOException{
		if (!storefile.exists()){storefile.createNewFile();}
		FileOutputStream fileStream = new FileOutputStream(storefile);
		fileStream.write(salt.length);
		fileStream.write(salt);
		fileStream.write("\n".getBytes());
		fileStream.close();
	}

	/**
	 * Writes all passwords into disk.
	 * Passwords are cipher so data in disk will be encrypted.
	 * @param passwords the password list to be written to disk
	 * */
	public void writePasswords(ArrayList<Password> passwords) throws IOException, IllegalArgumentException{
		if (passwords == null) {throw new IllegalArgumentException("The argument list cannot be a null reference.");}
		if (datafile.exists()) {datafile.delete();} 
		datafile.createNewFile();
		FileOutputStream fileStream = new FileOutputStream(datafile, true);
		OutputStreamWriter outputStream = new OutputStreamWriter(fileStream, StandardCharsets.UTF_8);
		for (Password next : passwords) {
			outputStream.write(next.getName() + "-" + next.getPassword() + "-" + next.getStringIVParams() + "\n");
		}
		outputStream.close();
	}

	/** 
	 * Read salt from disk.
	 * @return salt*/
	public byte[] readSalt() throws IOException {
		FileInputStream in = new FileInputStream(storefile);
		int numBytes = in.read();
		byte[] salt = new byte[numBytes];
		in.read(salt,0,numBytes);
		in.close();
		return salt;
	}

	/**
	 * Reads a password list from disk.
	 * Data needs to be decrypted with the password store key.
	 * @return passwordList the password list read from disk
	 * @return null if datafile doesn't exists.
	 * */
	public ArrayList<Password> readPasswords() throws IOException{
		if(datafile.exists()){
			InputStream is = Files.newInputStream(datafile.toPath());
			Reader isr = new InputStreamReader(is, StandardCharsets.ISO_8859_1);
			BufferedReader br = new BufferedReader(isr);
			ArrayList<Password> passwords = new ArrayList<>();
			String line; 
			while((line = br.readLine())!=null && !line.equals(""))
				passwords.add(new Password(line));
			isr.close();
			return passwords;
		} else {
			return null;
		}
	}
}
