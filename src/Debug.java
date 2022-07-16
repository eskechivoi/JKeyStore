public class Debug {
	public static void main (String args[]) {
		try {
			String user_password = "password";
			String store_password = "Hello there!";
			PasswordCipher password = new PasswordCipher (user_password);
			byte[] encryption = password.encrypt(store_password.getBytes("UTF-8"));
			System.out.println(store_password + " -> encrypted to -> " + encryption.toString()); 
			String result = new String(password.decrypt(encryption), "UTF-8");
			System.out.println(encryption + " -> decrypted to -> " + result);
		} catch (Exception e) {
			System.out.print("Exception!: ");
			e.printStackTrace();
		}
	}
}
