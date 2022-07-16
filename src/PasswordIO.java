import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Suplies IO methods for writting and reading passwords from disk, 
 * that are independent of the particular password store.
 * @author ferrodr (Fernando Rodríguez Martín - UVa) */
public class PasswordIO {
	private String datapath;
	private File datafile;

	/**
	 * Inizialices a PasswordIO that can read and write a password list from disk.
	 * Data from disk is encrypted, and this class is not responsible for decryption.
	 * @param datapath the path where the password store file is*/
	public PasswordIO(String datapath) {
		this.datapath = datapath;
		datafile = new File(datapath);
	}

	/**
	 * Writes all passwords into disk.
	 * Passwords are cipher so data in disk will be encrypted.
	 * @param passwordList the password list to be written to disk
	 * */
	public void writePasswords(ArrayList<Password> passwords) throws IOException, IllegalArgumentException{
		if (passwords == null) {throw new IllegalArgumentException("The argument list cannot be a null reference.");}
		if (datafile.exists()) {datafile.delete();}
		datafile.createNewFile();
		FileOutputStream fileStream = new FileOutputStream(datafile);
		OutputStreamWriter outputStream = new OutputStreamWriter(fileStream, StandardCharsets.UTF_8);
		Iterator<Password> it = passwords.iterator(); 
		while(it.hasNext()) 
			outputStream.write(it.next().toString() + "\n");
		outputStream.close();
	}

	/**
	 * Reads a password list from disk.
	 * Data needs to be decrypted with the password store key.
	 * @return passwordList the password list read from disk
	 * */
	public ArrayList<Password> readPasswords() throws FileNotFoundException{
		Scanner in = new Scanner(datafile);
		ArrayList<Password> passwords = new ArrayList<Password>();
		while(in.hasNextLine()){
			String passphrase = in.nextLine();
			passwords.add(new Password(passphrase));
		}
		in.close();
		return passwords;
	}
}
