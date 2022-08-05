public class Debug2 {
	public static void main (String args[]) {
		try {
			String user_password = "stored_password";
			String store_password = "password";
			String store_name = "test02";
			PasswordStore store = new PasswordStore(store_name, store_name, store_password);
		} catch (Exception e) {
			System.out.print("Exception!: ");
			e.printStackTrace();
		}
	}
}
