public class DebugReader {
	public static void main (String args[]) {
		try {
			String store_password = "password";
			String store_name = "test01";
			PasswordStore store = new PasswordStore(store_name, store_name, store_password);
			store.readPasswordListFromFile();
			System.out.println("Gmail: " + store.getPasswordByName("password1"));
			System.out.println("Steam: " + store.getPasswordByName("password2"));
		} catch (Exception e) {
			System.out.print("Exception!: ");
			e.printStackTrace();
		}
	}
}
