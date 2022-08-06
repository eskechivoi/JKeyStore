public class DebugWriter {
	public static void main (String args[]) {
		try {
			String user_password1 = "asdfjkl";
			String user_password2 = "zxcvbnm";
			String store_password = "password";
			String store_name = "test01";
			PasswordStore store = new PasswordStore(store_name, store_name, store_password);
			store.setNewPassword("password1", user_password1);
			store.setNewPassword("password2", user_password2);
			store.writePasswordList();
		} catch (Exception e) {
			System.out.print("Exception!: ");
			e.printStackTrace();
		}
	}
}
