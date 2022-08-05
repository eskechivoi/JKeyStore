public class Debug {
	public static void main (String args[]) {
		try {
			String user_password = "stored_password";
			String store_password = "password";
			String store_name = "test01";
			PasswordStore store = new PasswordStore(store_name, store_name, store_password);
			store.setNewPassword("password1", user_password);
			store.writePasswordList();
			System.out.print("List written to disk: ");
			store.printPasswordList();
			store.readPasswordListFromFile();
			System.out.print("List read from disk: ");
			store.printPasswordList();
			System.out.println(store.getPasswordByName("password1"));
		} catch (Exception e) {
			System.out.print("Exception!: ");
			e.printStackTrace();
		}
	}
}
