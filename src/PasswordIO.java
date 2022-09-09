import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Suplies IO methods for writting and reading passwords from disk, 
 * that are independent of the particular password store.
 * @author ferrodr (Fernando Rodríguez Martín - UVa) */
public class PasswordIO {
	private File datafile;
	private File storefile;

	/**
	 * Inizialices a PasswordIO that can read and write a password list from disk.
	 * Data from disk is encrypted, and this class is not responsible for decryption.
	 * @param datapath the path where the password store file is*/
	public PasswordIO(String datapath, String storepath) {
		datafile = new File(datapath);
		storefile = new File(storepath);
	}

	/** Writes the store key, deleting all file content. 
	 *	@param password the store key*/
	public void writeStoreKey(byte[] password) throws IOException{
		if (!storefile.exists()){storefile.createNewFile();}
		FileOutputStream fileStream = new FileOutputStream(storefile);
		fileStream.write(password.length);
		fileStream.write(password);
		fileStream.write("\n".getBytes());
		fileStream.close();
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
		FileOutputStream fileStream = new FileOutputStream(datafile, true);
		OutputStreamWriter outputStream = new OutputStreamWriter(fileStream, StandardCharsets.UTF_8);
		Iterator<Password> it = passwords.iterator(); 
		while(it.hasNext()) {
			Password next = it.next();
			outputStream.write(next.getName() + "-" + next.getPassword() + "-" + next.getStringIVParams() + "\n");
		}
		outputStream.close();
	}

	/** 
	 * Read store key from disk.
	 * @return key the store key*/
	public byte[] readStoreKey() throws IOException {
		FileInputStream in = new FileInputStream(storefile);
		int numBytes = in.read();
		byte[] key = in.readNBytes(numBytes);
		in.close();
		return key;
	}

	/**
	 * Reads a password list from disk.
	 * Data needs to be decrypted with the password store key.
	 * @return passwordList the password list read from disk
	 * @return null if datafile doesn't exists.
	 * */
	public ArrayList<Password> readPasswords() throws IOException{
		if(datafile.exists()){
			InputStream is = new FileInputStream(datafile);
			Reader isr = new InputStreamReader(is, "ISO-8859-1");
			BufferedReader br = new BufferedReader(isr);
			ArrayList<Password> passwords = new ArrayList<Password>();
			String line; 
			while((line = br.readLine())!=null && !line.equals(""))
				passwords.add(new Password(line));
			isr.close();
			return passwords;
		} else {
			return null;
		}
	}

	private String getStringRepresentation(ArrayList<Character> list) {
		StringBuilder builder = new StringBuilder(list.size());
		for(Character ch:list)
			builder.append(ch);
		return builder.toString();
	}
}
